<h1 style="text-align: center">Jason摄影展系统</h1>

## 项目简介
为了安利我们家鸽鸽，我们开发出了一个坤坤平台，这个系统可以聊天，可以发帖子交流等一系列功能。当然了，发布的内容与我们家鸽鸽无关的话，会提示非法内容而无法发布哦。所以请各位真爱粉们为坤坤博客贡献一份力量吧。

## 技术选型
### 1、系统环境
- Java EE 17
- Servlet 3.0
- Apache Maven 3.6.3

### 2、主框架

- Spring Boot 3.1.0

### 3、持久层

- Apache Mybatis-plus 3.5.1

## 🎨 系统演示
### 👉 演示地址：http://112.74.169.107:8888/search-html/html/test.html

## 🍪 技术架构


## ❓ 部分截图


## 🔧 功能特点
* 坤坤表情包搜索 (开发中)
* 坤坤聊天室 (开发中)
* 坤坤论坛 (开发中)
* 坤坤小游戏 (开发中)
* 坤坤周报商城 (开发中)
* 坤坤音乐播放器 (开发中)
* 坤坤精彩小视频点播 (开发中)

## 🗿 文件结构
```lua
kun-cloud -- 父项目,各模块分离，方便集成和微服务
│  ├─conf -- 项目相关配置文件
│  ├─kun-common -- 核心通用模块，主模块
│  │  ├─kun-common-core -- 封装通用模块
│  │  ├─kun-common-database -- 封装Mybatis及数据库基础模块
│  │  ├─kun-common-file -- 封装文件服务器模块
│  │  ├─kun-common-http -- 封装http模块
│  │  ├─kun-common-log -- 封装日志模块
│  │  ├─kun-common-rabbitmq -- 封装rabbitmq消息队列模块
│  │  ├─kun-common-redis -- 封装redis缓存模块
│  │  ├─kun-common-web -- 封装web模块
│  │─kun-gateway -- 统一网关模块 [10001]
│  │─kun-gen -- 坤坤平台代码生成模块
│  │─kun-modules -- 平台模块项目，目前包含系统子模块
│  │  ├─kun-blog -- 坤坤博客模块 [9200]
```
## 🔧 docker部署
`docker build -t kun-blog .` <br>
`docker run -d --name blog -p 9200:9200 kun-blog`

## 🔧 前端部署
``


## ✨ 特别鸣谢
特别感谢ikun对`KunCloud`项目提供的技术支持！