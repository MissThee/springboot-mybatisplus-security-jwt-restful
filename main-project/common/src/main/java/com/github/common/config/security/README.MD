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
            SecurityContextHolder.setContext(new SecurityContextImpl() {{
                setAuthentication(authentication);
            }});
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    
        @Override
        public void destroy() {
    
        }
    }
   ```
3. 
   + 前后分离中与之前不同的是，服务器不再存储用户登录信息，完全依靠token来分辨用户。
   + 对security的修改，也仅仅是将用户权限信息来源从session改到从访问的token获取。之后的流程仍然依照security原有逻辑进行，无任何改动，即可满足前后分离需求。