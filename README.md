# wkwk-backend(哇酷哇酷音视频)

## 项目介绍

本项目名为WKWK，是一个基于七牛云的对象存储服务制作的音视频软件，实现了基本的视频观看、视频互动、用户交互等功能。

## 项目架构

### 总览：

本项目采用前后端分离的架构，前端使用Vue框架，后端使用SpringBoot+SpringCloudAlibaba框架。使用到的中间件有MySQL，MongoDB，Redis，ElasticSearch，Nacos，RocketMQ，具体版本与初始化语句见 [后端启动](#后端启动)。

### 框架

本项目使用的后端框架为spring全家桶，框架版本对应为 jdk17 - springboot2.6.11 - springcloud2021.0.4 - springcloudalibaba2021.0.4.0 。

### 全局架构图

### 架构与功能梳理

## 功能描述




### 亮点功能架构概述

## 项目快速启动

### 前端启动

### 后端启动

#### 1. 项目使用到的中间件以及其版本如下表：

| 中间件        | 版本   |
| ------------- | ------ |
| MySQL         | 8.0.27 |
| MongoDB       | 5.0.5  |
| Redis         | 6.2.6  |
| Nacos         | 2.03   |
| ElasticSearch | 7.12.1 |
| RocketMQ      | 4.5.1  |

#### 2. 初始化MySQL数据库表结构

可以使用 resource/DB/wkwk.sql 的sql脚本快速生成表结构。

#### 3.初始化ElasticSearch索引库

本项目针对用户表和视频表建立了索引库，用来搜索用户名或者视频标题。

向ElasticSearch索引库添加Mapping映射的json语句在 resource/ES 路径下， 有esmapping-user.json与esmapping-video.json两个json文件, 分别用来建立用户与视频的索引映射。

#### 4.修改配置文件中各项中间件的地址端口用户名密码等信息

#### 5.依次启动各个服务







