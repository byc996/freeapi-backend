# 数据库初始化
# @author <a href="https://github.com/liyupi">程序员鱼皮</a>
# @from <a href="https://yupi.icu">编程导航知识星球</a>

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
    `invokeMethodName` varchar(128) not null comment '调用方法名',
    `brief` varchar(512) null comment '简介',
    `url` varchar(512) not null comment '接口地址',
    `description` text null comment '描述',
    `image` varchar(512) not null comment '图片etag',
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


insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (1, '获取用户名', null, 'POST', 'http://localhost:8002/api/name/user', '获取用户名', '获取用户名', '"53bbbecde8700ec7d0d11ecb984401c4"', '免费', '其他', '[{"value":"harvey","type":"string","required":"是","name":"username"}]', '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 1, 3, 12, '2023-03-12 15:41:01', '2024-01-15 20:11:41', 0);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (2, '接口2', null, 'POST', 'http://www.example.com/api/2', null, '这是接口2的描述', null, '免费', '趣味娱乐', null, '', '', 0, 2, 0, '2022-01-02 00:00:00', '2024-01-14 10:49:30', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (3, '接口3', null, 'GET', 'http://www.example.com/api/3', null, '接口3', null, '免费', '趣味娱乐', null, '{"Content-Type": "application/json"}', null, 0, 3, 0, '2022-01-03 00:00:00', '2024-01-14 10:49:32', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (4, '接口4', null, 'DELETE', 'http://www.example.com/api/4', null, '这是接口4的描述', null, '免费', '趣味娱乐', null, '{"Content-Type": "application/json", "Authorization": "Bearer token456"}', null, 1, 4, 0, '2022-01-04 00:00:00', '2024-01-12 11:06:52', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (5, '接口5', null, 'PUT', 'http://www.example.com/api/5', null, '接口5', null, '免费', '趣味娱乐', null, '{"Content-Type": "application/json"}', '{"Content-Type": "application/json", "Authorization": "Bearer token789"}', 1, 5, 0, '2022-01-05 00:00:00', '2024-01-14 10:49:35', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (6, '接口6', null, 'GET', 'http://www.example.com/api/6', null, '接口6', null, '免费', '趣味娱乐', null, null, null, 0, 1, 0, '2022-01-06 00:00:00', '2024-01-14 10:49:37', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (7, '接口7', null, 'POST', 'http://www.example.com/api/7', null, '这是接口7的描述', null, '免费', '生活服务', null, '{"Content-Type": "application/json"}', '{"Content-Type": "application/json"}', 0, 2, 0, '2022-01-07 00:00:00', '2024-01-14 10:49:38', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (8, '接口8', null, 'GET', 'http://www.example.com/api/8', null, '接口8', null, '免费', '生活服务', null, '{"Content-Type": "application/json"}', '{"Content-Type": "application/json", "Authorization": "Bearer token123"}', 0, 3, 0, '2022-01-08 00:00:00', '2024-01-14 10:49:40', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (9, '接口9', null, 'POST', 'http://www.example.com/api/9', null, '这是接口9的描述', null, '免费', '生活服务', null, '{"Content-Type": "application/json"}', null, 1, 4, 0, '2022-01-09 00:00:00', '2024-01-14 10:49:41', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (10, '接口10', null, 'PUT', 'http://www.example.com/api/10', null, '接口10', null, '免费', '生活服务', null, '{"Content-Type": "application/json", "Authorization": "Bearer token789"}', '{"Content-Type": "application/json", "Authorization": "Bearer token789"}', 1, 5, 0, '2022-01-10 00:00:00', '2024-01-14 10:49:43', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (11, 'abaab', null, 'GET', 'http://localhost/api/kkk', null, 'sdfwefsdfdsfsdf', null, '免费', '生活服务', null, 'dawe', 'fdsd', 0, 1, 0, '2023-03-11 14:47:25', '2024-01-12 11:06:52', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (12, '接口1', null, 'GET', 'http://www.example.com/api/1', null, '这是接口1的描述', null, '免费', '功能应用', null, '{"Content-Type": "application/json"}', '{"Content-Type": "application/json", "Authorization": "Bearer token123"}', 1, 1, 0, '2022-01-01 00:00:00', '2024-01-12 11:06:52', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (14, '域名状态', null, 'GET', 'http://localhost:8002/api/functionality/domain', '查看域名是否已被注册', '快速查看域名是否有效，是否已被注册', '"265cd978b370bd6af35b7d8029ab036c"', '限次', '功能应用', '[{"Parameter Name":"domain","type":"string","required":"是","name":"domain","value":"bc2996.com"}]', '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 1, 3, 8, '2023-03-19 22:05:16', '2024-01-15 19:55:53', 0);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (15, 'IP信息', null, 'GET', 'http://localhost:8001/api/functionality/ip', '获取IP的信息', '获取IP的信息', null, '限次', '功能应用', '[{"name":"ip","type":"string","required":"是"}]', '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 0, 3, 1, '2023-03-20 12:04:00', '2024-01-15 19:05:43', 0);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (16, '测试', null, 'GET', 'http://test', '', '', null, '免费', '功能应用', '', '', '', 0, 3, 0, '2024-01-11 23:55:26', '2024-01-14 10:28:20', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (17, '测试', null, 'GET', 'http://test', '', '', null, '免费', '功能应用', '', '', '', 0, 3, 0, '2024-01-11 23:57:34', '2024-01-14 10:28:18', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (18, '测试', null, 'GET', 'http://localhost/test', '', '', '"4c7a11bd60ca1c65d7b2010113070716"', '限次', '数据智能', '', '', '', 0, 3, 0, '2024-01-12 00:05:39', '2024-01-14 10:25:55', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (19, '测试1', null, 'GET', 'http://test1', '', '', '"4c7a11bd60ca1c65d7b2010113070716"', '限次', '数据智能', '', '', '', 0, 3, 0, '2024-01-12 09:04:42', '2024-01-14 10:25:46', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (20, '测试2', null, 'GET', 'http://test2', '测试2', null, '"4c7a11bd60ca1c65d7b2010113070716"', '限次', '数据智能', null, '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 0, 3, 0, '2024-01-12 09:08:16', '2024-01-14 10:25:47', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (21, '测试2', null, 'GET', 'http://test2', '测试2', null, '"dd67962f3acef407979a8087aa4d922d"', '限次', '数据智能', null, '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 0, 3, 0, '2024-01-12 09:39:22', '2024-01-14 10:25:48', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (22, '测试2', null, 'GET', 'http://test22', '测试2', null, '"4c7a11bd60ca1c65d7b2010113070716"', '限次', '数据智能', null, '[{"key":"Content-Type","value":"application/json"},{"key":"Authorization","value":"sfewfwefewfwfwefe"}]', '[{"key":"Content-Type","value":"application/json"}]', 0, 3, 0, '2024-01-12 09:39:51', '2024-01-14 10:25:45', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (23, '测试3', null, 'POST', 'http://test3', '测试3', '测试3', '"4c7a11bd60ca1c65d7b2010113070716"', '限次', '数据智能', null, '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 0, 3, 0, '2024-01-12 10:38:18', '2024-01-14 10:25:43', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (24, '文本自动摘要', 'dataTextAnalyze', 'POST', 'http://localhost:8001/api/data/text-abstract', '智能分析文本关键内容', '通过人工智能算法，智能分析自动抽取文本中的关键内容并生成指定长度的摘要。适合在新闻标题生成、文本简介、各类文献摘要的生成和商品评论抽取等。注意：length参数设置过短，会抽出空白简介并成功计数，因此请设置合理长度。', '"53bbbecde8700ec7d0d11ecb984401c4"', '免费', '新闻资讯', '[{"name":"text","value":"在寂静的夜晚，星星闪烁着微弱的光芒，仿佛是宇宙的一颗颗眼睛注视着人间。月光洒在静寂的大地上，投下斑驳的影子。","type":"string","required":"是"},{"type":"number","required":"是","name":"length","value":"40"}]', '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 1, 3, 0, '2024-01-12 14:03:41', '2024-01-14 10:25:56', 1);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (25, '获取名称', null, 'GET', 'http://localhost:8002/api/name/get', '获取名称1', null, '"12ee595b59b3a56c6831d918f0c0f6f8"', '限次', '其他', '[{"key":"name","value":"harvey","type":"string","required":"是","name":"name"}]', '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 0, 3, 0, '2024-01-13 23:01:56', '2024-01-15 11:41:57', 0);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (26, '文本自动摘要', 'dataTextAnalyze', 'POST', 'http://localhost:8002/api/data/text-abstract', '智能分析文本关键内容', '通过人工智能算法，智能分析自动抽取文本中的关键内容并生成指定长度的摘要。适合在新闻标题生成、文本简介、各类文献摘要的生成和商品评论抽取等。注意：length参数设置过短，会抽出空白简介并成功计数，因此请设置合理长度。', '"12ee595b59b3a56c6831d918f0c0f6f8"', '限次', 'AI智能', '[{"name":"text","value":"在寂静的夜晚，星星闪烁着微弱的光芒，仿佛是宇宙的一颗颗眼睛注视着人间。月光洒在静寂的大地上，投下斑驳的影子。","type":"string","required":"是"},{"type":"number","required":"是","name":"length","value":"40"}]', '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 1, 3, 2, '2024-01-14 10:27:48', '2024-01-15 19:30:10', 0);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (27, '测试GET', null, 'GET', 'http://localhost:8002/api/test/get', '测试GET(测试接口)', null, '"4c7a11bd60ca1c65d7b2010113070716"', '免费', '其他', '[]', '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 1, 3, 5, '2024-01-14 20:04:19', '2024-01-15 19:48:41', 0);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (28, '测试POST', null, 'POST', 'http://localhost:8002/api/test/post', '测试POST(测试接口)', null, '"4c7a11bd60ca1c65d7b2010113070716"', '免费', '其他', '[]', '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 1, 3, 3, '2024-01-14 21:20:11', '2024-01-15 19:52:46', 0);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (29, '中英翻译', null, 'GET', 'http://localhost:8002/api/data/translate', '中文翻译成英文', '快速准确的将中文翻译成英文，限制1000字', '"ff34c66378fb63930d685371d606dcca"', '限次', 'AI智能', '[{"type":"string","required":"是","name":"text","value":"今天天气很好"}]', '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 1, 3, 6, '2024-01-15 09:40:18', '2024-01-15 20:55:24', 0);
insert into buapi.interface_info (id, name, invokeMethodName, method, url, brief, description, image, type, category, requestParams, requestHeader, responseHeader, status, userId, invokeNum, createTime, updateTime, isDelete) values (30, '诗句生成', null, 'GET', 'http://localhost:8002/api/qa/poetry', '根据描述生成诗句', '根据描述信息生成诗句', '"2d8876dd8f60edffbcc3f3001c51a37d"', '限次', '知识问答', '[{"type":"string","required":"是","name":"description","value":"定要攀登到那最高峰，俯瞰在面前显得渺小的群山"}]', '[{"key":"Content-Type","value":"application/json"}]', '[{"key":"Content-Type","value":"application/json"}]', 1, 3, 6, '2024-01-15 10:02:55', '2024-01-15 19:54:17', 0);
