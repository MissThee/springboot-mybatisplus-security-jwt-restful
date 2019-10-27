1. 使用maven-compiler-plugin打包
    配置
    ```xml
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.7.0</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <compilerArguments>
                    <!-- 打包这个目录的中的本地jar包(将引用的本地jar文件一起打包) -->
                    <extdirs>${project.basedir}/lib</extdirs>
                </compilerArguments>
            </configuration>
        </plugin>
    ```
2. 配置全局resources路径
    ```xml
       <resources>
           <!--将自定义jar打包。主要为打包oracle驱动-->
           <resource>
               <directory>lib</directory>
               <targetPath>BOOT-INF/lib</targetPath>
               <includes>
                   <include>*.jar</include>
               </includes>
           </resource>
           <!--若不设置<resources>配置，mybatis generator读取默认resources目录;
               若设置了<resources>配置，myabtis generator仅读取配置的目录。因此处需打包本地jar（oracle驱动为本地jar包时），需配置jar包打进指定目录的<resources>，但会影响mybatis generator仅读取lib目录，需再将原resources目录也配置在此处-->
           <resource>
               <directory>src/main/resources</directory>
               <!--默认在打包时放置到此目录，无需设置，默认就是这个目录-->
               <!--<targetPath>BOOT-INF/classes/</targetPath>-->
               <includes>
                   <!--<include>**/*.properties</include>-->
                   <include>**/*.*</include>
               </includes>
           </resource>
       </resources>
    ```