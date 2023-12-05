# stan-hire

#### 介绍
仿BOSS直聘项目

#### 软件架构
软件架构说明
本项目依赖组件：mysql、nacos、redis、rabbitmq..（更新ing）

#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1. 默认所有通用组件部署在同一台服务器上，运行前请修改父工程pom文件下`<virtualIp>`标签的ip地址
2. 本项目加入腾讯云短信服务，运行前请修改common包下`tencentCloud.properties`的配置以及`SMSUtils.java`代码中的应用ID、签名、模板ID
3. nacos需要加入配置`jwt_config.yaml`，键为`jwt.key`，该key用于加密jwt。详见网关服务下`bootstrap.yml`

#### 模块说明

| 模块          | 说明           | 端口   |
|:------------|:-------------|:-----|
| hire-common | 通用包          | -    |
| hire-pojo   | 实体类包         | -    |
| hire-api    | 公共模块包        | -    |
| gateway     | 网关服务         | 8000 |
| auth        | 注册认证服务       | 8111 |
| company     | 企业服务         | 6001 |
| user        | 用户服务         | 7001 |
| resource    | 资源消息服务       | 4001 |
| work        | 工作服务,包含简历和职位 | 3001 |

#### 参与贡献

1.  Fork 本仓库
2.  新建 feature_xxx 分支
3.  提交代码
4.  新建 Pull Request
