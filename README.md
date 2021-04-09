#  dubbo整合spring cloud gateway

> - [x] [Dubbo想要个网关怎么办？试试整合微服务统一网关解决方案：Spring Cloud Gateway](https://blog.liudongyang.top/zh/spring-boot-nacos/%E5%BE%AE%E6%9C%8D%E5%8A%A1%E7%BB%9F%E4%B8%80%E6%9C%8D%E5%8A%A1%E7%BD%91%E5%85%B3%E6%90%AD%E5%BB%BA.html)
> - [x] [Spring Cloud Alibaba整合Nacos实现配置中心](https://blog.liudongyang.top/zh/spring-cloud-alibaba/Nacos-Config-%E6%9C%8D%E5%8A%A1%E7%AB%AF%E5%88%9D%E5%A7%8B%E5%8C%96.html#%E5%88%86%E5%B8%83%E5%BC%8F%E9%85%8D%E7%BD%AE%E4%B8%AD%E5%BF%83)
> - [ ] [Spring Cloud Alibaba整合SkyWalking实现链路追踪](https://blog.liudongyang.top/zh/spring-cloud-alibaba/%E4%B8%BA%E4%BB%80%E4%B9%88%E9%9C%80%E8%A6%81%E9%93%BE%E8%B7%AF%E8%BF%BD%E8%B8%AA.html)
> - [ ] [Spring Cloud Alibaba整合RocketMQ实现异步通信](https://blog.liudongyang.top/zh/spring-cloud-alibaba/%E6%B6%88%E6%81%AF%E9%98%9F%E5%88%97%E7%9A%84%E6%B5%81%E6%B4%BE.html)

## 依赖环境

>* 参阅[组件版本说明](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)

|依赖|版本|
|---|---|
| jdk                 |1.8|
| Nacos               |2.0.0-ALPHA.2|
| Spring Boot         |2.3.2.RELEASE|
| Spring Cloud        |Hoxton.SR8|
| Spring Cloud Alibaba|2.2.5.RELEASE|

## 微服务综合能力平台具体服务介绍

### 通用表格数据导出服务

>* [演示地址](http://nacos.liudongyang.top:8081)

>* 主体功能

- [x] 展示mysql数据库中的所有表信息,包含表名称及表字段;
- [x] 测试sftp文件服务器连通性;
- [x] 预览指定表数据信息,并可指定字段筛选预览,不进行勾选字段时默认展示所有字段数据;
- [x] 导出指定表数据信息的excel文件,并可指定字段筛选导出,不进行勾选字段时默认导出所有字段数据;
- [x] 指定sftp文件服务器上传指定表数据信息的excel文件,并可指定字段筛选上传,不进行勾选字段时默认上传所有字段数据;
- [x] 前端可拖拽字段顺序,通过自定义字段显示顺序定制导出excel文件;
- [x] 前端显示总记录数,可索引表指定字段数据进行相应操作;
- [x] 表单二次提交问题优化,提示信息模态框展示,提升用户体验;
- [x] 增加自动添加sftp服务参数 增加自定义后台sql查询数据方式导出上传操作
- [x] 添加sftp服务数据管理功能;
- [x] 定制化表头导入数据到excel并提交到指定sftp文件服务器;
- [x] 前端表单校验,后台其他配置参数优化;
- [x] 导出性能优化,提升后台服务文件导出效率,异步文件上传操作;

>* 使用时注意事项
    
>* 1.此服务平台依赖于mysql原生sql开发,数据库在创建数据库表时应注意保证数据库表字段信息的命名规范,一定要注意保证表字段注释完整.
>* 2.测试sftp服务器连通性时不必输入目录,这里输入的目录为文件上传到文件服务器的完整目录.
>* 3.上传操作需要保证在sftp服务器联通测试成功的基础上执行,并且需要指定上传的目录.
>* 4.当后台数据库表数据过多时可能需要较长时间完成导出和上传操作,请耐心等待。

>* 服务页面展示

>* 列表展示页面

<p align="center"><img src="https://cdn.jsdelivr.net/gh/gitldy1013/dubboSpringCloud@main/docs/通用表格数据导出服务.jpg" alt="通用表格数据导出服务"></p>

>* 详情展示页面
<p align="center"><img src="https://cdn.jsdelivr.net/gh/gitldy1013/dubboSpringCloud@main/docs/预览详情展示页面.jpg" alt="预览详情展示页面"></p>

>* 上传服务管理
<p align="center"><img src="https://cdn.jsdelivr.net/gh/gitldy1013/dubboSpringCloud@main/docs/上传服务管理.jpg" alt="上传服务管理"></p>


### 异步定时任务服务

>* 主体功能

- [x] 系统启动初始化加载已有的定时任务
- [x] 启动or暂停定时任务
- [x] 定时任务删除功能
- [x] 立即运行定时任务
- [x] 实时新增及修改定时任务
- [x] 定时任务的详细监控
- [x] 集成支持kafka服务
- [x] 支持http(get/post) kafka sftp(定制模板)推动方式 

>* 定时任务服务列表页面
<p align="center"><img src="https://cdn.jsdelivr.net/gh/gitldy1013/dubboSpringCloud@main/docs/定时任务服务列表页面.jpg" alt="定时任务服务列表页面"></p>

>* 内网部署环境

|涉及spring的资产IP|所涉及系统的端口|归属部门|归属系统|
|---|---|---|---|
|172.24.26.30|8081|技术创新中心|支付清算协会|
|172.24.26.34|18080|技术创新中心|大数据接口平台|
|172.24.26.35|18080|技术创新中心|大数据接口平台|
|55.144.17.35|8866|技术创新中心|湖南中石化|
|55.144.17.35|19090|技术创新中心|统一网关服务|
|55.144.26.35|8888|技术创新中心|sftp 文件扫描服务|
|55.144.26.35|19090|技术创新中心|统一网关服务|
