# 基础镜像
FROM openjdk:8-jdk-alpine

# 指定工作目录
WORKDIR /app

ADD target/buinterface-0.0.1-SNAPSHOT.jar .

# 暴露端口
EXPOSE 8001

# 启动命令
ENTRYPOINT ["java","-jar","/app/buinterface-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]