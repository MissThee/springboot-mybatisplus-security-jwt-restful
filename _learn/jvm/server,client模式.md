1. 查看工作模式
    + 在命名行输入java -version
    + 其中Server VM (build 25.31-b07, mixed mode)其实代表了JVM的Server模式了。
2. 修改JVM的启动模式呢？
    + 64位系统默认在 JAVA_HOME/jre/lib/amd64/jvm.cfg（64位虚拟机的server模式无法转换成client模式）
    + 32在目录JAVA_HOME/jre/lib/i386/jvm.cfg  
        配置为KNOWN的为使用的模式  
        ```
        -server KNOWN  
        -client IGNORE  
        ```
+ JVM工作在Server模式可以大大提高性能，但应用的启动会比client模式慢大概10%。
+ 当该参数不指定时，虚拟机启动检测主机是否为服务器，如果是，则以Server模式启动，否则以client模式启动。（J2SE5.0检测的根据是至少2个CPU和最低2GB内存）
+ 当JVM用于启动GUI界面的交互应用时适合于使用client模式，当JVM用于运行服务器后台程序时建议用Server模式。 
    + JVM在client模式默认-Xms是1M，-Xmx是64M；
    + JVM在Server模式默认-Xms是128M，-Xmx是1024M。