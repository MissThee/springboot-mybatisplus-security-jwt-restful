#### 运行springboot的jar包

1. 当前目录创建文件命名为Dockfile，填入内容：
    ```dockerfile
    FROM openjdk:8-jre-alpine
    VOLUME /tmp 
    ADD springbootPackage.jar /package.jar 
    ENTRYPOINT ["sh","-c","java -Djava.security.egd=file:/dev/./urandom -jar /package.jar $PARAMS"]
    ```
2. 将jar包命名为springbootPackage.jar，放置Dockfile同级目录
3. 构建docker镜像(在Dockerfile目录运行，注意结尾的.)  
    ```cmd
    docker build -t springboot_project .
    ```

   (或使用maven中的jib/jib:dockerBuild,代替以上三部，完成构建。配置文件于pom.xml的jib插件中)
4. 运行镜像并添加自定义参数  
    ```cmd
    docker run -t -i -p 18098:8098 -p 18097:8097 -e PARAMS="--spring.redis.host=host.docker.internal  --spring.rabbitmq.host=host.docker.internal --spring.datasource.primary.jdbc-url=jdbc:mysql://host.docker.internal:3306/mybatis_test_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullNamePatternMatchesAll=true&useSSL=false --spring.datasource.secondary.jdbc-url=jdbc:mysql://host.docker.internal:3306/mybatis_test_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullNamePatternMatchesAll=true&useSSL=false"  --name springboot_demo <镜像id> -d
    ```

#### 运行mysql
1. 拉取镜像  
    ```cmd
    docker pull mysql:5.7
    ```
2. 当前目录创建mysql文件夹用以存储数据库文件，win版docker会弹窗提示输入windows用户密码，已授权使用共享磁盘空间
3. 当前目录运行镜像  
    ```cmd
    docker run -t -i -p 13306:3306 -v $PWD/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=password -d --name <自定义名称> <mysql镜像id>
    ```


#### 运行dockerUI
1. 拉取镜像  
    ```cmd
    docker pull uifd/ui-for-docker
    ```
2. 运行  
    ```cmd
    docker run -it -d --name docker-web -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock docker.io/uifd/ui-for-docker
    ```


#### 运行oracle三方镜像
1. 拉取镜像  
    ```cmd
    docker pull registry.cn-hangzhou.aliyuncs.com/woqutech/oracle-database-11.2.0.4.0-ee
    ```
2. 创建数据库存储路径：  
    ```cmd
    mkdir -p /data/oracledb
    ```
3. 启动Oracle数据库容器：  
    ```cmd
    docker run -d --name oracledb \
    -p 1521:1521 \
    -e ORACLE_SID=oracledb \
    -e ORACLE_PWD=oracle \
    -e ORACLE_CHARACTERSET=ZHS16GBK \
    -e SGA_SIZE=8G \
    -e PGA_SIZE=8G \
    -e DB_ROLE=primary \
    -e ENABLE_ARCH=true \
    -v /data/oracledb:/opt/oracle/oradata \
    registry.cn-hangzhou.aliyuncs.com/woqutech/oracle-database-11.2.0.4.0-ee
    ```

#### 运行centos
1. 拉取并运行镜像   
    ```cmd
    docker run -ti --privileged=true -p 10022:22 --name centos -d <镜像id> /usr/sbin/init
    ```
2. 进入镜像
    ```cmd
    docker exec -it centos /bin/bash
    ```
3. 配置ssh/SFTP远程登录  
    1. 修改密码  
        ```
        passwd
        ```
    2. 安装ssh  
        ```
        yum install passwd openssl openssh-server -y
        ```
    3. 生成秘钥(HostKey)
        ```
        ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key
        ssh-keygen -t rsa -f /etc/ssh/ssh_host_ecdsa_key
        ssh-keygen -t rsa -f /etc/ssh/ssh_host_ed25519_key
        ```
    4. 启动ssh  
        ```
        /usr/sbin/sshd
        ```
4. 配置使用证书登录
    1. 生成证书  
        ```
        ssh-keygen -t rsa
        ```
    2. 将公钥设置为该服务器的登录公钥
        ```
        cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
        ```
    3. 下载私钥id_rsa到要登录服务器的客户端（将/root/.ssh/id_rsa拷贝至客户端）
    4. 修改ssh登录配置  
        ```
        vi /etc/ssh/sshd_config
        ```
        ```
        PasswordAuthentication no   //禁止密码登录[选择]
        PubkeyAuthentication yes    //使用rsa证书登录
        StrictModes no              //设置为yes需要严格设置.ssh相关文件的权限，否则可能一些用户无法登录（chomd 700 ~/.ssh；chomd 600 ~/.ssh/authorized_keys）
        ```
    5. 重启ssh  
        ```
        /usr/sbin/sshd restart
        ```
    6. 设置ssh启动项  
        ```
        chkconfig sshd on
        ```
5. 安装java环境：使用linux文件夹下的install-java.sh安装即可。
6. 启动jar  
    ```
    java -jar demo-0.0.1-SNAPSHOT.jar --spring.redis.host=host.docker.internal  --spring.rabbitmq.host=host.docker.internal --spring.datasource.primary.jdbc-url=jdbc:mysql://host.docker.internal:3306/mybatis_test_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullNamePatternMatchesAll=true&useSSL=false --spring.datasource.secondary.jdbc-url=jdbc:mysql://host.docker.internal:3306/mybatis_test_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullNamePatternMatchesAll=true&useSSL=false
    ```
7. 查看java进程  
    ```
    ps -ef|grep java|grep -v grep
    ```


