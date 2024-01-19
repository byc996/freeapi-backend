## 项目介绍

FreeAPI 是一个提供API接口调用的平台，用户可以注册登录、开通接口调用权限。用户可以调试接口，并且每次调用会进行统计。管理员可以发布接口、设置接口参数、下线接口、上线接口，以及可视化接口的调用情况、数据分析。

**项目地址：**[**FreeAPI 接口平台**](http://freeapi.bc2996.com/)

### 目录结构

- common： 公共模块
- gateway：网关模块，用于鉴权，统计接口调用次数等
- backend: 后端核心业务模块，负责接口、用户等的增删改查
- buinterface: 接口模块，提供接口功能的模块
- client-sdk: 客户端sdk，封装接口调用代码，方便第三方调用

## 核心业务流程

![img](https://cdn.nlark.com/yuque/0/2024/jpeg/25961647/1705697829585-7a231791-d0b3-4dc8-92ff-42748c39bed8.jpeg)



## 技术选项

### 后端

- Java 8
- MySQL 数据库
- Spring Boot 2.7.0
- Dubbo（RPC、Nacos）模块间方法调用
- Spring Cloud Gateway API网关
- Spring Session Redis 分布式登录
- Redisson 分布式锁
- Hutool 工具类
- Spring Boot Starter (SDK 开发）

### 前端

- React 18, React Router
- Ant Design UI框架
- openapi-typescript-codegen 快速生成axios 调用后端代码
- Eslint +Prettier 代码格式化和规范代码



## 功能展示

### 首页

![img](https://cdn.nlark.com/yuque/0/2024/png/25961647/1705698804580-9f1e5be1-a4c1-4c70-ae96-813fa94ab727.png)



### 查看接口

![img](https://cdn.nlark.com/yuque/0/2024/png/25961647/1705698865839-415403bb-0f74-40aa-b067-29a7317abf89.png)

### 接口调用

![img](https://cdn.nlark.com/yuque/0/2024/png/25961647/1705699208864-c6b8c0c9-f13a-4ac2-9c5a-501c8c383b22.png)

### 接口管理

![img](https://cdn.nlark.com/yuque/0/2024/png/25961647/1705699347378-f5de6020-c46f-4775-9f8a-5c1c68f6b182.png)

### 添加/更新接口

![img](https://cdn.nlark.com/yuque/0/2024/png/25961647/1705699416197-43b1c018-4c89-42f4-91a9-84749998adc2.png)

### 个人中心 - 开发者密钥

![img](https://cdn.nlark.com/yuque/0/2024/png/25961647/1705699261284-9c813d66-ff08-4d20-89a2-f93bc0d1f6e2.png)

### 个人中心 - IP白名单

![img](https://cdn.nlark.com/yuque/0/2024/png/25961647/1705699306491-fede4882-7381-4859-8e77-7623647faabb.png)