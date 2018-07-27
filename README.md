## 数据库

docs/auth.sql

## 技术选型

1. SpringBoot
2. MySQL
3. bootstrap / AdminLTE
4. jquery / datatables
5. e7

## 打包  & docker

mvn clean package -Dmaven.test.skip=true -Ptest
docker build -t adzuki-admin .
docker run -d -p 8080:8080 --name adzuki-admin adzuki-admin
