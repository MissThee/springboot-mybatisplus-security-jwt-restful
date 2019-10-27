1. 拷贝容器中配置文件至本机当前目录  
    ```
    docker cp <容器id>:/etc/mysql/mysql.cnf ./mysql.cnf
    ```  
    或
    ```
    docker exec -it <容器id> bash
    ```
    进入容器修改文件

2. 配置：  
    1. 主：  
    ```
    [mysqld]
    ## 同一局域网内注意要唯一
    server-id=100
    ## 开启二进制日志功能，可以随便取（关键）
    log-bin=mysql-bin
    ```
    2. 从：
    ```
    [mysqld]
    ## 设置server_id,注意要唯一
    server-id=101
    ## 开启二进制日志功能，以备Slave作为其它Slave的Master时使用
    log-bin=mysql-slave-bin
    ## relay_log配置中继日志
    relay_log=edu-mysql-relay-bin
    ```
3. 连接主数据库  
    使用```show master status```查看File值（如：mysql-bin.000001），Position值（154）

4. 连接从数据库  
    1. 配置
        ```
        change master to master_host='172.17.0.2', master_user='root', master_password='password', master_port=3306, master_log_file='mysql-bin.000001', master_log_pos= 154, master_connect_retry=30;
        ```
    2. 启动从库复制
        ```cmd
        START SLAVE
        ```
    3. 查看状态
        ```
        show slave status
        ```
    注：
    ```
    1中：
    master_host ：Master的地址，指的是容器的独立ip,可以通过docker inspect --format='{{.NetworkSettings.IPAddress}}' 容器名称|容器id查询容器的ip。若使用宿主机host，则查询ipconfig查看宿主机在docker中ip地址
    master_port：Master的端口号，指的是容器的端口号
    master_user：用于数据同步的用户
    master_password：用于同步的用户的密码
    master_log_file：指定 Slave 从哪个日志文件开始复制数据，即上文中提到的 File 字段的值
    master_log_pos：从哪个 Position 开始读，即上文中提到的 Position 字段的值
    master_connect_retry：如果连接失败，重试的时间间隔，单位是秒，默认是60秒
    3中：
    查询结果Slave_IO_Running 和 Slave_SQL_Running 都是Yes，说明主从复制已经开启
    查询结果Last_IO_Error可查看报错
    ```

5. 解决从库Slave_SQL_Running为No  
    1. 此时可能因数据库不一致，或从库进行了写操作，执行语句错误导致数据库不能同步
        1. 查看错误状态
            ```
            show slave status
            ```  
           查看slave status中的Relay_Log_File和Relay_Log_Pos两个值
        2. 执行以下语句，结果中找上面俩个值
            ```
            show relaylog events in '<Relay_Log_File>' from <Relay_Log_Pos> limit n;
            ```
    2. 解决方法：
        1. 若为可以直接跳过不影响结果的语句，使用：（GLOBAL SQL_SLAVE_SKIP_COUNTER=1会跳过一个事务，即可能会跳过多个sql语句，若第一条语句错误，第二条语句为创建100条记录，则条过后，两条语句均不执行，会出现100条记录的差异）
            ```
            STOP SLAVE
            set GLOBAL SQL_SLAVE_SKIP_COUNTER=1
            START SLAVE
            ```
        2. 若需补齐因错误未执行的语句，使用
            ```
            set sql_log_bin=OFF
            create table db58_user_credit_general(id int) -- 根据实际自行编写sql语句
            set sql_log_bin=ON
            ```