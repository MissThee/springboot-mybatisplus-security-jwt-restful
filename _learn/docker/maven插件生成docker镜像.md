1. 配置
    ```xml
        <plugin>
            <groupId>com.google.cloud.tools</groupId>
            <artifactId>jib-maven-plugin</artifactId>
            <version>1.0.0</version>
            <configuration>
                <container>
                    <useCurrentTimestamp>true</useCurrentTimestamp>
                    <entrypoint>
                        <arg>/bin/sh</arg>
                        <arg>-c</arg>
                        <arg>java -cp /app/resources/:/app/classes/:/app/libs/* com.github.missthee.WebApplication
                            $PARAMS
                        </arg>
                    </entrypoint>
                </container>
                <from>
                    <image>openjdk:8-jre-alpine</image>
                </from>
                <to>
                    <image>${project.name}2019:${project.version}</image>
                </to>
            </configuration>
        </plugin>
    ```
2. maven 运行 jib