create table sys_user
(
    id          bigint,
    nickname    nvarchar(50),
    username    varchar(50),
    password    varchar(50),
    version     int,
    is_deleted  int,
    create_time datetime,
    update_time datetime
);
