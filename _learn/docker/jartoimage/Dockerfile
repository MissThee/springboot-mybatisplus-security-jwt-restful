FROM java:8
#使用以下jre镜像需docker账户登录才可用。docker login --username=<dockerhub用户名，不是邮箱!>
#FROM store/oracle/serverjre:8
VOLUME /tmp 
ADD springbootPackage.jar /package.jar
#暴露端口，仅暴露给docker中其他容器；而使用-v是映射到本机
#EXPOSE 8105
#EXPOSE 8106
ENTRYPOINT ["sh","-c","java -jar /package.jar $PARAMS"]