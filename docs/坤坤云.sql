drop index idx_phone on k_user;

drop table if exists k_user;

drop table if exists k_user_auth;

drop table if exists k_user_image;

drop table if exists k_user_login_record;

/*==============================================================*/
/* Table: k_user                                                */
/*==============================================================*/
create table k_user
(
    id                   int(11) not null auto_increment comment '用户表主键ID',
    phone                varchar(11) not null comment '手机号',
    head_portrait        varchar(255) not null comment '头像',
    nick_name            varchar(15) comment '昵名',
    sex                  tinyint(1) comment '性别（0、男，1、女）',
    birthday             date comment '生日',
    personal_signature   varchar(30) comment '个性签名',
    industry             varchar(50) comment '行业',
    area                 varchar(100) comment '地区',
    register_source      tinyint(1) not null comment '注册来源（1、手机号验证码登录，2、手机号一键登录，3、账号密码登录，4、QQ登录，5、微信登录）',
    status               tinyint(1) not null default 0 comment '账号状态（0、启用，1、禁用，3、注销）',
    remark               varchar(200) not null default '' comment '备注',
    del_flag             tinyint(1) not null default 0 comment '删除标志（0、正常，1、删除）',
    create_by            varchar(32) not null comment '创建者',
    create_time          datetime not null default CURRENT_TIMESTAMP comment '创建时间',
    update_by            varchar(32) not null comment '更新者',
    update_time          datetime not null default CURRENT_TIMESTAMP comment '更新时间',
    primary key (id)
)
    auto_increment = 1;

alter table k_user comment '坤坤云用户表';

/*==============================================================*/
/* Index: idx_phone                                             */
/*==============================================================*/
create unique index idx_phone on k_user
    (
     phone
        );

/*==============================================================*/
/* Table: k_user_auth                                           */
/*==============================================================*/
create table k_user_auth
(
    id                   int(11) not null auto_increment comment '主键ID',
    user_id              int(11) not null comment '用户主键ID',
    login_type           tinyint(1) not null comment '登录类型（1、手机号验证码登录 ，2、手机号一键登录， 3、账号密码登录）',
    account              varchar(32) comment '手机号、用户名或第三方应用的唯一标识',
    password             varchar(32) comment '密码(站内的保存密码，站外的不保存或保存token)',
    remark               varchar(200) not null default '' comment '备注',
    del_flag             tinyint(1) not null default 0 comment '删除标志（0、正常，1、删除）',
    create_time          datetime not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time          datetime not null default CURRENT_TIMESTAMP comment '更新时间',
    primary key (id)
)
    auto_increment = 1;

alter table k_user_auth comment '坤坤云用户登录授权表';

/*==============================================================*/
/* Table: k_user_image                                          */
/*==============================================================*/
create table k_user_image
(
    id                   int(11) not null auto_increment comment '主键ID',
    user_id              int(11) not null comment '用户主键ID',
    type                 tinyint(1) not null comment '图片类型（1、头像，2、背景图）',
    image_name           varchar(100) not null comment '图片名称',
    image_path           varchar(500) not null comment '图片路径',
    sort                 int(11) not null default 0 comment '排序',
    remark               varchar(200) not null default '' comment '备注',
    del_flag             tinyint(1) not null default 0 comment '删除标志（0、正常，1、删除）',
    create_time          datetime not null default CURRENT_TIMESTAMP comment '创建时间',
    primary key (id)
)
    auto_increment = 1;

alter table k_user_image comment '坤坤云用户图片表';

/*==============================================================*/
/* Table: k_user_login_record                                   */
/*==============================================================*/
create table k_user_login_record
(
    id                   int(11) not null auto_increment comment '主键ID',
    user_id              int(11) not null comment '用户主键ID',
    auth_id              int(11) not null comment '用户登录授权主键ID',
    login_status         tinyint(1) not null comment '操作类型（0、登录成功，1、登出成功，2、登录失败，3、登出失败）',
    app_version          varchar(32) comment '客户端版本号',
    mac                  varchar(64) comment '设备硬件地址',
    ip                   varchar(32) not null comment '登录ip',
    os                   varchar(16) comment '登录系统，IOS等',
    os_version           varchar(32) comment '系统版本',
    remark               varchar(200) not null default '' comment '备注',
    del_flag             tinyint(1) not null default 0 comment '删除标志（0、正常，1、删除）',
    create_time          datetime not null default CURRENT_TIMESTAMP comment '创建时间',
    primary key (id)
)
    auto_increment = 1;

alter table k_user_login_record comment '坤坤云用户登录记录表';
