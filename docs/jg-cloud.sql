drop table if exists t_admin_account;
drop table if exists t_admin_account_login_record;
drop table if exists t_admin_permission;
drop table if exists t_admin_role;
drop table if exists t_admin_role_permission;

/*==============================================================*/
/* Table: t_admin_account                                       */
/*==============================================================*/
create table t_admin_account
(
    id                   int(11) not null auto_increment comment '主键ID',
    role_id              int(11) unsigned default 0 comment '角色主键ID',
    username             varchar(11) not null comment '用户名',
    password             varchar(32) not null comment '密码',
    head_portrait        varchar(255) default '' comment '头像',
    nick                 varchar(15) not null comment '昵名',
    status               tinyint(1) unsigned default 0 comment '账号状态（0、启用，1、禁用）',
    remark               varchar(200) default '' comment '备注',
    del_flag             tinyint(1) unsigned default 0 comment '删除标志（0、正常，1、删除）',
    create_id            int(11) unsigned not null comment '创建者ID',
    create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
    update_id            int(11) unsigned not null comment '更新者ID',
    update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key (id)
)
    auto_increment = 1
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci;

alter table t_admin_account comment '管理端用户表';

/*==============================================================*/
/* Index: idx_username                                          */
/*==============================================================*/
create unique index idx_username on t_admin_account
    (
     username
        );

/*==============================================================*/
/* Table: t_admin_account_login_record                          */
/*==============================================================*/
create table t_admin_account_login_record
(
    id                   int(11) not null auto_increment comment '主键ID',
    admin_id             int(11) unsigned not null comment '管理端用户主键ID',
    login_result_type    tinyint(1) unsigned not null comment '登录结果类型（0、登录成功，1、登出成功，2、登录失败，3、登出失败）',
    client_ip            varchar(50) not null comment '客户端IP',
    client_version       varchar(50) default '' comment '客户端版本号',
    client_mac           varchar(50) default '' comment '客户端设备硬件地址',
    client_os_name       varchar(50) default '' comment '客户端系统名称',
    client_os_version    varchar(50) default '' comment '客户端系统版本',
    remark               varchar(200) default '' comment '备注',
    create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
    primary key (id)
)
    auto_increment = 1
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci;

alter table t_admin_account_login_record comment '管理端用户登录记录表';

/*==============================================================*/
/* Index: idx_adminId                                           */
/*==============================================================*/
create unique index idx_adminId on t_admin_account_login_record
    (
     admin_id
        );

/*==============================================================*/
/* Table: t_admin_permission                                    */
/*==============================================================*/
create table t_admin_permission
(
    id                   int(11) not null auto_increment comment '主键ID',
    name                 varchar(32) not null comment '权限名称',
    permission           varchar(50) not null comment '权限标识',
    sort                 int(11) unsigned default 0 comment '排序',
    status               tinyint(1) unsigned default 0 comment '状态（0、正常，1、禁用）',
    remark               varchar(200) default '' comment '备注',
    del_flag             tinyint(1) unsigned default 0 comment '删除标志（0、正常，1、删除）',
    create_id            int(11) unsigned not null comment '创建者ID',
    create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
    update_id            int(11) unsigned not null comment '更新者ID',
    update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key (id)
)
    auto_increment = 1
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci;

alter table t_admin_permission comment '管理端权限表';

/*==============================================================*/
/* Table: t_admin_role                                          */
/*==============================================================*/
create table t_admin_role
(
    id                   int(11) not null auto_increment comment '主键ID',
    name                 varchar(32) not null comment '角色名称',
    role_flag            varchar(32) not null comment '排序',
    sort                 int(11) unsigned default 0 comment '排序',
    status               tinyint(1) unsigned default 0 comment '状态（0、正常，1、禁用）',
    remark               varchar(200) default '' comment '备注',
    del_flag             tinyint(1) unsigned default 0 comment '删除标志（0、正常，1、删除）',
    create_id            int(11) unsigned not null comment '创建者ID',
    create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
    update_id            int(11) unsigned not null comment '更新者ID',
    update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key (id)
)
    auto_increment = 1
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci;

alter table t_admin_role comment '管理端角色表';

/*==============================================================*/
/* Table: t_admin_role_permission                               */
/*==============================================================*/
create table t_admin_role_permission
(
    id                   int(11) not null auto_increment comment '主键ID',
    role_id              int(11) unsigned not null comment '角色主键ID',
    permission_id        int(11) unsigned not null comment '权限主键ID',
    remark               varchar(200) default '' comment '备注',
    del_flag             tinyint(1) unsigned default 0 comment '删除标志（0、正常，1、删除）',
    create_id            int(11) unsigned not null comment '创建者ID',
    create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
    update_id            int(11) unsigned not null comment '更新者ID',
    update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key (id)
)
    auto_increment = 1
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci;

alter table t_admin_role_permission comment '管理端角色权限关联表';
