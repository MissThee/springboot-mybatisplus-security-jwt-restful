# 关于修改security，适配使用token认证的前后分离模式

## 主要修改内容
1. security默认是在服务器存储session的，首先配置其不再使用session存储用户权限信息
   ```java
   @Configuration
   @EnableWebSecurity
   @EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
   public class SecurityConfig extends WebSecurityConfigurerAdapter {
       @Override
       protected void configure(HttpSecurity http) throws Exception {
           http.csrf().disable();
           http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
       }
   }
   ```
2. 停用了session后，请求时security需要用户权限信息，那就在请求时手动构造一个用户权限信息对象，并设置进security
   ```java
    @Component
    //继承security提供的OncePerRequestFilter，使本方法在每次访问中，都会被调用。
    public class MyJWTVerificationFilter extends OncePerRequestFilter {
        private final UserInfoForSecurity userInfoForSecurity;
        private final JavaJWT javaJWT;
    
        @Autowired
        public MyJWTVerificationFilter(JavaJWT javaJWT, UserInfoForSecurity userInfoForSecurity) {
            this.javaJWT = javaJWT;
            this.userInfoForSecurity = userInfoForSecurity;
        }
    
        @Override
        protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
            String token = httpServletRequest.getHeader(JavaJWT.JWT_TOKEN_KEY);
            Authentication authentication;
            Boolean verifyToken = javaJWT.verifyToken(token);
            if (verifyToken) {
                String userId = JavaJWT.getId();
                //本项目从token中获取用户id，然后以用户id，查询数据库获取用户权限值。生产环境中开启mybatis二级缓存，并让用户信息相关表启用二级缓存，就不会每次都查询数据库，直接从缓存取。当然也能替换成别的，只要在这一步拿到用户信息就可以。
                //当然权限值也可从其他来源获取，此处要的权限值，就是普通String字符串，可任意替换来源。
                //将用户权限值写进UserDetails中。
                //此处选择UserDetails来传递用户信息，仅因为UserDetails的属性正好包含了要用的三个属性:username,password,authorities。所以直接用它了，不用自己再写一个新的类了。也能随便换成别的，只要能保证创建时UsernamePasswordAuthenticationToken的参数即可
                UserDetails userDetails = userInfoForSecurity.loadUserById(userId);
                //主要方法1：构造一个身份信息对象对象，至于给不给权限看实际情况，这个是给了授权的情况。
                authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                javaJWT.updateTokenAndSetHeaderWithAvailableMinute(token, 4 * 24 * 60);
            } else {
                //主要方法1：构造一个身份信息对象对象，至于给不给权限看实际情况，这个是无授权的情况。区别在构造函数的setAuthenticated()方法。
                authentication = new UsernamePasswordAuthenticationToken(null, null);
            }
            //主要方法2：将身份信息对象设置到Security当前上下文，即让配置的身份信息在本次请求中生效
            SecurityContextImpl securityContextImpl = new SecurityContextImpl();
            securityContextImpl.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContextImpl);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    
        @Override
        public void destroy() {
    
        }
    }
   ```
3. 本项目token签发与验证
   + 登录接口为直接暴露的可访问接口，登录成功后返回签发的token，token中包含的值：仅含用户id，及其他jwt自带属性
     + 登录获取的接口没有使用security的SuccessHandler和FailureHandler，因为个人比较希望所有与前端的json的数据交互都使用controller来进行
   + 请求其他权限限制的接口时，在过滤器中解析token，拿取用户id，由数据库（或缓存）提取对用用户权限信息，构建security认证主体对象
   + security负责后续验证，直到访问接口方法

4. 修改后的不同
   + 有效性验证：security不再缓存用户登录信息，用户信息的有效性，完全依靠token校验。
   + 信息一致性：security不缓存用户信息，每次构建用户信息对象，都进行数据库查询，但会导致频繁访问数据库查询用户信息。解决此问题依靠持久化框架的缓存，对用户信息查询相关方法做缓存（如本项目使用mybatis，可以对用户信息查询方法开启二级缓存，这样当用户修改其信息后，旧用户信息缓存也会立即失效，下次查询时生成新的缓存，同时实现了security每次查询数据库/缓存，都能获取最新的用户信息）。
   + 对security的修改，也仅仅是将用户权限信息来源从session改到从访问的token获取。之后的流程仍然依照security原有逻辑进行，无任何改动，即可满足前后分离需求。

5. 实现一个账号只能在一个地方登录
   + 此demo没有实现这个功能，账号可以任意同时登录。因为最近没有这个需求一直没有做，而且考虑到需求有很多可能性，没有什么通用方法（如限制只能一个设备登录、限制同种设备只能登录一个，实现就不同，如果做涉及到同种设备的，又要与前端配合约定设备区分参数等）
   + 预计实现方式为额外增加登录信息表，与用户信息关联，判断当前token是否为最后登录的token，或同种设备中最后登录的token，以决定其是否通过认证