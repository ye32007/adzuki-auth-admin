## 数据库

docs/auth.sql

## 技术选型

1. SpringBoot
2. MySQL
3. bootstrap / AdminLTE
4. jquery / datatables
5. e7

## 打包  & docker

1. mvn clean package -Dmaven.test.skip=true -Ptest
2. docker build -t adzuki-admin .
3. docker run -d -p 8080:8080 --name adzuki-admin adzuki-admin
