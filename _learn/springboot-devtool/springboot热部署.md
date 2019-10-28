1. File --> Settings --> Build,Execution,Developer --> Compiler --> Build Project automatically --> 选中
2. ctrl + shift + alt + / --> Registry --> 找到并勾选compiler.automake.allow.when.app.running
3. 加maven依赖
    ```xml
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-devtools</artifactId>
           <!--传递依赖。true不传递，父项目不会启用devtool；false传递，父项目会启用devtool-->
           <optional>true</optional>
           <scope>runtime</scope>
       </dependency>
    ```
4. 开启热部署（多级项目可配置在root的pom中）
    ```xml
       <build>
          <plugins>
              <plugin>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-maven-plugin</artifactId>
                  <configuration>
                       <!--重要-->
                      <fork>true</fork>
                  </configuration>
              </plugin>
          </plugins>
       </build>
    ```
5. 配置文件（非必须）
    ```properties
    #热部署生效
    spring.devtools.restart.enabled=true
    #设置重启的目录
    #spring.devtools.restart.additional-paths=src/main/java
    #classpath目录下的WEB-INF文件夹内容修改不重启
    spring.devtools.restart.exclude=WEB-INF/**
    ```
6. 说明
   + devtools可以实现页面热部署（即页面修改后会立即生效，这个可以直接在application.properties文件中配置spring.thymeleaf.cache=false来实现），

   + 配置了后在修改java文件后也就支持了热启动，不过这种方式是属于项目重启（速度比较快的项目重启），会清空session中的值，也就是如果有用户登陆的话，项目重启后需要重新登陆。
   
   + 默认情况下，/META-INF/maven，/META-INF/resources，/resources，/static，/templates，/public这些文件夹下的文件修改不会使应用重启，但是会重新加载（devtools内嵌了一个LiveReload server，当资源发生改变时，浏览器刷新）。