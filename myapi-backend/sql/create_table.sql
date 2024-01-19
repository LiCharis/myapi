# 数据库初始化
# @author <a href="https://github.com/liyupi">程序员鱼皮</a>
# @from <a href="https://yupi.icu">编程导航知识星球</a>

-- 创建库
create database if not exists myapi;

-- 切换库
use myapi;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    `accessKey`  varchar(512)                           not null comment 'accessKey',
    `secretKey`  varchar(512)                           not null comment 'secretKey',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount unique (userAccount)
) comment '用户' collate = utf8mb4_unicode_ci;


-- 接口信息表
create table if not exists interface_info
(
    id             bigint auto_increment comment 'id' primary key,
    `name`         varchar(256)                       not null comment '名称',
    `description`  varchar(256)                       null comment '接口描述',
    url            varchar(512)                       not null comment '接口地址',
    requestHeader  text                               null comment '请求头',
    responseHeader text                               null comment '响应头',
    `status`       int      default 0                 not null comment '接口状态 0-关闭 1-开启',
    method         varchar(256)                       not null comment '请求类型',
    userId         bigint                             not null comment '创建用户 id',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '接口信息' collate = utf8mb4_unicode_ci;


-- 模型调用信息表
SELECT id,host,port,account,password,img_order,env,model_path,`order`,local_path,result_path,createTime,updateTime,isDelete,interfaceInfoId FROM model_interface_info WHERE isDelete=0 AND (interfaceInfoId = 2)
create table if not exists model_interfaceInfo
(
    id            bigint auto_increment comment 'id' primary key,
    `host`        varchar(256)                       not null comment '服务器地址',
    `port`          varchar(64)                        not null comment '服务器端口',
    `account`     varchar(256)                       null comment '',
    `password`    varchar(256)                       null comment '服务器密码',
    `img_order`   varchar(64)                        null comment '参数类型',
    `env`         varchar(256)                       null comment '模型环境',
    `model_path`  varchar(256)                       null comment '模型路径',
    `order`       varchar(256)                       null comment '执行命令',
    `local_path`  varchar(256)                       null comment '结果所存放的本地地址',
    `result_path` varchar(256)                       null comment '结果生成地址',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除'
) comment '接口信息' collate = utf8mb4_unicode_ci;


-- 用户-接口信息表
create table if not exists user_interface_info
(
    id              bigint auto_increment comment 'id' primary key,
    userId          bigint                             not null comment '用户 id',
    interfaceInfoId bigint                             not null comment '接口 id',
    `status`        int      default 0                 not null comment '用户状态 0-正常 1-禁用',
    totalNum        int      default 0                 not null comment '接口调用次数',
    leftNum         int      default 0                 not null comment '剩余调用次数',

    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '用户-接口信息' collate = utf8mb4_unicode_ci;


-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';
