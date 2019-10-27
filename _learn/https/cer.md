java ： jdk1.8   
证书库：用于在本地测试的、locahost域名证书库。   
证书库密码：密码为“localhost”。   
证书库位置：当前路径   
证书库文件名：localhost.jks   
证书库格式：JKS  

1. 创建证书库及localhost证书  
  ```
  keytool -genkey -alias localhost -keyalg RSA -keysize 4096 -keypass localhost -keystore localhost.jks -storepass localhost -dname "cn=localhost,ou=localhost,o=localhost,l=beijing,st=beijing,c=cn" -ext "SAN:c=DNS:localhost,IP:127.0.0.1"-ext "SAN:c=DNS:localhost,IP:127.0.0.1" -validity 36500
  ```
2. 查看证书库
  ```
  keytool -list -keystore localhost.jks -storetype JKS -store
  ```
3. 导出证书，.cer给客户端安装
  ```
  keytool -export -alias localhost -file localhost.cer -keystore localhost.jks -storetype JKS -storepass localhost
  ```