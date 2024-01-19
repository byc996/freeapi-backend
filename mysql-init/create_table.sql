# 数据库初始化

-- 创建库
create database if not exists buapi;

-- 切换库
use buapi;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userName     varchar(256)                           null comment '用户昵称',
    userAccount  varchar(256)                           not null comment '账号',
    userAvatar   varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword varchar(512)                           not null comment '密码',
    accessKey    varchar(512)                           not null comment 'accessKey',
    secretKey    varchar(512)                           not null comment 'secretKey',
    whiteList    text                                            comment 'ip 白名单',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount
        unique (userAccount)
) comment '用户';

-- interface info
create table if not exists buapi.`interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '名称',
    `brief` varchar(512) null comment '简介',
    `url` varchar(512) not null comment '接口地址',
    `description` text null comment '描述',
    `image` varchar(512) null comment '图片etag',
    `type` varchar(64) not null comment '类型',
    `category` varchar(128) not null comment '种类',
    `requestParams` text null comment '请求参数',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '请求头',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人',
    `invokeNum` bigint not null default 0 comment '调用总次数',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '接口信息';


create table if not exists buapi.`user_interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `userId` bigint not null comment '调用用户 id',
    `interfaceInfoId` bigint not null comment '接口 id',
    `totalNum` int default 0 not null comment '总调用次数',
    `restNum` int default 0 not null comment '剩余调用次数',
    `status` int default 0 not null comment '0-正常, 1-禁用',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户调用接口关系';

insert into buapi.user (id, userName, userAccount, userAvatar, gender, userRole, userPassword, accessKey, secretKey, whiteList, createTime, updateTime, isDelete) values (3, null, 'admin', 'https://i.pravatar.cc/150?img=3', null, 'admin', '227ac5ac726e5d6e3881da7cc889aff4', '2178128597dff8d4240304e92561efbf', 'c431e8d4de79e589009743d5404bbf1d', null, '2023-03-12 15:34:43', '2024-01-15 18:40:00', 0);

