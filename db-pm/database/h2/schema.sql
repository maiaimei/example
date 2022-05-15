-- drop table sys_user;
-- drop table sys_role;
-- drop table sys_role_user;
-- drop table sys_role_permission;
-- drop table sys_permission;


create table sys_file
(
    id                bigint unsigned primary key not null,
    original_filename varchar(200) not null,
    type              varchar(20)  not null default '',
    size              bigint unsigned not null default 0,
    group_name        varchar(20)  not null default '',
    path              varchar(200) not null,
    gmt_create        timestamp    not null
);

create table sys_user
(
    id           bigint primary key not null,
    nickname     varchar(50)        not null,
    username     varchar(20)        not null,
    password     varchar(64)        not null,
    sso_enabled  tinyint unsigned not null default 0,
    gmt_create   timestamp          not null,
    gmt_modified timestamp null
);

create table sys_role
(
    id           bigint primary key not null,
    code         varchar(50)        not null,
    name         varchar(50)        not null,
    gmt_create   timestamp          not null,
    gmt_modified timestamp null
);

create table sys_role_user
(
    id         bigint primary key not null,
    role_id    bigint             not null,
    user_id    bigint             not null,
    gmt_create timestamp          not null
);

create table sys_role_permission
(
    id            bigint primary key not null,
    role_id       bigint             not null,
    permission_id bigint             not null,
    gmt_create    timestamp          not null
);

create table sys_permission
(
    id           bigint primary key not null,
    code         varchar(50)        not null,
    name         varchar(50)        not null,
    gmt_create   timestamp          not null,
    gmt_modified timestamp null
);