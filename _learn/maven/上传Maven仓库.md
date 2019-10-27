1. 提交发布申请  
    1. 创建 Issue 地址：`https://issues.sonatype.org`  
        项目类型`Community Support - Open Source Project Repository Hosting`  
        groupId为自己的域名：github可以填com.github.xxx（审核直接过），非github会有客服找你核对  
    2. 使用 GPG 生成密钥对（win可下载gpg4win，安装后若使用idea的终端，则需要重启idea）。
        ```
        gpg --version   检查安装是否成功
        gpg --gen-key   生成密钥对
        gpg --list-keys   查看公钥
        gpg --keyserver hkp://keyserver.ubuntu.com --send-keys <公钥ID>   将公钥发布到 PGP 密钥服务器
        gpg --keyserver hkp://keyserver.ubuntu.com --recv-keys <公钥ID>   查询公钥是否发布成功
        ```
2. 配置 maven  
    需要修改的 Maven 配置文件包括：setting.xml（全局级别,在IDEA设置中可查看配置文件位置）与 pom.xml（项目级别）  
    + `setting.xml`：在其中加入 server 信息，包含 Sonatype 账号的用户名与密码    
    + `pom.xml`： 在其中配置 profile,包括插件和 distributionManagement，。snapshotRepository 与 repository 中的 id 一定要与 setting.xml 中 server 的 id 保持一致。  
    (配置信息参考结尾pom.xml)
3. 上传构件到 OSS 中  
    `mvn clean deploy`   
    首次上传成功可能需在issue中回复工作人员，现在应无需通知了。具体看有没有发布成功的通知，提示发布成功则不用再找客服。
4. `https://search.maven.org/` 或 `https://repo1.maven.org/maven2/com/github/MissThee` 查看上传的文件
5. `https://oss.sonatype.org/` 登录后可查看处理中的文件（Staging Repositories），查看发布成功的文件（Artifact Search）
6. maven依赖中心可查看发布的jar，2小时或更久同步后可查

***

#### 配置参考 
1. pom.xml
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
    
        <groupId>com.github.MissThee</groupId>
        <artifactId>demotest</artifactId>
        <version>0.0.3</version>
    
        <name>${project.groupId}:${project.artifactId}</name>
        <description>empty</description>
        <url>https://github.com/MissThee/${project.name}</url>
        <properties>
              <maven.compiler.source>1.8</maven.compiler.source>
              <maven.compiler.target>1.8</maven.compiler.target>
              <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
              <java.version>1.8</java.version>
        </properties>
        <licenses>
            <license>
                <name>MIT License</name>
                <url>http://www.opensource.org/licenses/mit-license.php</url>
            </license>
        </licenses>
        <developers>
            <developer>
                <name>Ma Teng</name>
                <email>1163182715@qq.com</email>
                <organization>personal</organization>
                <organizationUrl>https://github.com/MissThee</organizationUrl>
            </developer>
        </developers>
        <scm>
            <url>https://github.com/MissThee/${project.name}</url>
        </scm>
        <distributionManagement>
            <snapshotRepository>
                <id>ossrh</id>
                <url>https://oss.sonatype.org/content/repositories/snapshots</url>
            </snapshotRepository>
            <repository>
                <id>ossrh</id>
                <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
            </repository>
        </distributionManagement>
    
        <dependencies>
    
         ...
    
        </dependencies>
    
        <build>
            <plugins>
                <!--<plugin>-->
                <!--<groupId>org.springframework.boot</groupId>-->
                <!--<artifactId>spring-boot-maven-plugin</artifactId>-->
                <!--</plugin>-->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                </plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-gpg-plugin</artifactId>
                    <version>1.6</version>
                    <executions>
                        <execution>
                            <id>demo</id>
                            <phase>verify</phase>
                            <goals>
                                <goal>sign</goal>
                            </goals>
                            <!--<configuration>-->
                            <!--&lt;!&ndash;<executable>gpg</executable>&ndash;&gt;-->
                            <!--<homedir>${gpg.homedir}</homedir>-->
                            <!--</configuration>-->
                        </execution>
                    </executions>
                </plugin>
                <plugin>
                    <groupId>org.sonatype.plugins</groupId>
                    <artifactId>nexus-staging-maven-plugin</artifactId>
                    <version>1.6.8</version>
                    <extensions>true</extensions>
                    <configuration>
                        <serverId>ossrh</serverId>
                        <nexusUrl>https://oss.sonatype.org/</nexusUrl>
                        <autoReleaseAfterClose>true</autoReleaseAfterClose>
                    </configuration>
                </plugin>
                <!-- Sources Plugin -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-source-plugin</artifactId>
                    <version>3.0.1</version>
                    <executions>
                        <execution>
                            <id>attach-sources</id>
                            <goals>
                                <goal>jar-no-fork</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
    
                <!-- Javadoc Plugin -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-javadoc-plugin</artifactId>
                    <version>3.0.1</version>
                    <executions>
                        <execution>
                            <id>attach-javadocs</id>
                            <goals>
                                <goal>jar</goal>
                            </goals>
                            <configuration>
                                <additionalOptions>-Xdoclint:none</additionalOptions>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </project>
    ```
2. settings.xml (maven配置)
    ```xml
    <settings>
        <servers>
            <server>
                <id>ossrh</id>
                <username>MissThee</username>
                <password>password here like: 123$%^ *****</password>
            </server>
        </servers>
    </settings>
    ```