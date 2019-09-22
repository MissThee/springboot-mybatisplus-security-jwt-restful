查看运行中容器  
```
docker ps
```  
查看所有容器  
```
docker ps -a
```  
查看所有镜像  
```
docker image
```  
运行镜像，i输入，t交互模式，d在后台运行  
```
docker run -it -p <主机(宿主)端口>:<docker容器端口> -e  <设置环境变量:username="ritchie"> --name <自定义容器名称> <镜像id> -d <id>  /bin/bash
```  
停止容器  
```
docker stop
```  
删除容器  
```
docker rm <id>
```  

### 操作所有容器：
容器停止  
```
docker stop $(docker ps -aq)
```  
容器删除  
```
docker rm $(docker ps -aq)
```  
镜像删除  
```
docker rmi $(docker images -q)
```  
