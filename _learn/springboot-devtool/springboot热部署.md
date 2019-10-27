1. CTRL + SHIFT + A --> 查找make project automatically --> 选中
2. CTRL + SHIFT + A --> 查找Registry --> 找到并勾选compiler.automake.allow.when.app.running
3. 加maven依赖
    ```xml
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-devtools</artifactId>
           <optional>true</optional>
           <scope>runtime</scope>
       </dependency>
    ```
4. 开启热部署
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
