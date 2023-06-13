# imooc-hire-dev

#### 介绍
仿BOSS直聘项目

#### 软件架构
软件架构说明


#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1.  本项目加入腾讯云短信服务，运行前请修改`hire-common`包下`tencentCloud.properties`的配置以及`SMSUtils.java`代码中的应用ID、签名、模板ID
2.  nacos需要加入jwt_config.yaml配置，键为`jwt.key`，该key用于加密jwt。详见`gateway-8000/src/main/resources/bootstrap.yml`配置
3.  xxxx

#### 模块说明

| 模块          | 说明     | 端口   |
|:------------|:-------|:-----|
| hire-common | 通用工具包  | -    |
| hire-pojo   | 实体类包   | -    |
| hire-api    | 公共模块包  | -    |
| gateway     | 网关服务   | 8000 |
| auth        | 注册认证服务 | 8111 |
| company     | 企业服务   | 6001 |
| user        | 用户服务   | 7001 |
| resource    | 资源服务   | 4001 |
| work        | 工作服务,包含简历和职位   | 3001 |

#### 参与贡献

1.  Fork 本仓库
2.  新建 feature_xxx 分支
3.  提交代码
4.  新建 Pull Request

#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
