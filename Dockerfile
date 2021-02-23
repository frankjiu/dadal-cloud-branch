# 基于哪个镜像
FROM  openjdk:11
# 复制文件到容器
ADD  target/dadal/dadal-cloud-branch.jar  /home/frank/app/dadal/dadal-cloud-branch.jar
ADD target/classes/*.yml /home/frank/app/dadal/
#  暴露微服务启动端口, 即 对外端口 8090 : 容器暴露端口 1001
EXPOSE 1001
# 配置容器启动后执行的命令
ENTRYPOINT java -jar /home/frank/app/dadal/dadal-cloud-branch.jar

###used for manually###
# 基于哪个镜像
#FROM  openjdk:11
# 复制文件到容器
#ADD dadal-cloud-branch.jar /dadal.jar
# 暴露微服务启动端口, 对外端口 8090 : 容器暴露端口 1001
#EXPOSE 1001
# 配置容器启动后执行的命令
#ENTRYPOINT java -jar /dadal.jar
# 构建镜像
#docker build -t dadal:2.0 .
# 创建容器并启动
#docker run -p 8090:8090 dadal:2.0
# 访问
#http://192.168.75.130:8090/