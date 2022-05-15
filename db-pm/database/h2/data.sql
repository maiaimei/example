insert into sys_user(id, nickname, username, password, sso_enabled, gmt_create)
values (1507979825321619456, '管理员', 'admin', '$2a$10$u8MY1wPxp56w6HiATeTF6OaPJbYsf9ZViUARkNhTzK94.evHDXrku', 0,
        '2022-04-13T16:38:14.606454200');
insert into sys_user(id, nickname, username, password, sso_enabled, gmt_create)
values (1507979825321619457, '麦爱妹', 'maiam', '$2a$10$u8MY1wPxp56w6HiATeTF6OaPJbYsf9ZViUARkNhTzK94.evHDXrku', 0,
        '2022-04-13T16:38:14.606454200');


insert into sys_role(id, code, name, gmt_create)
values (1514161673055879168, 'admin', '管理员', '2022-04-13T16:38:14.606454200');
insert into sys_role(id, code, name, gmt_create)
values (1514161673202679808, 'user', '普通用户', '2022-04-13T16:38:14.606454200');


insert into sys_permission(id, code, name, gmt_create)
values (1514161673357869055, 'index:access', '访问首页', '2022-04-13T16:38:14.606454200');
insert into sys_permission(id, code, name, gmt_create)
values (1514161673357869056, 'user:create', '新增用户', '2022-04-13T16:38:14.606454200');
insert into sys_permission(id, code, name, gmt_create)
values (1514161673513058304, 'user:update', '修改用户', '2022-04-13T16:38:14.606454200');
insert into sys_permission(id, code, name, gmt_create)
values (1514162989341659136, 'user:delete', '删除用户', '2022-04-13T16:38:14.606454200');
insert into sys_permission(id, code, name, gmt_create)
values (1514162989492654080, 'user:get', '获取指定用户', '2022-04-13T16:38:14.606454200');
insert into sys_permission(id, code, name, gmt_create)
values (1514162989643649024, 'user:pageQuery', '分页查询用户', '2022-04-13T16:38:14.606454200');


insert into sys_role_user(id, role_id, user_id, gmt_create)
values (1514162989790449664, 1514161673055879168, 1507979825321619456, '2022-04-13T16:38:14.606454200');
insert into sys_role_user(id, role_id, user_id, gmt_create)
values (1514162989949833217, 1514161673202679808, 1507979825321619456, '2022-04-13T16:38:14.606454200');
insert into sys_role_user(id, role_id, user_id, gmt_create)
values (1514162989949833216, 1514161673202679808, 1507979825321619457, '2022-04-13T16:38:14.606454200');


insert into sys_role_permission(id, role_id, permission_id, gmt_create)
values (1514163734728187902, 1514161673055879168, 1514161673357869055, '2022-04-13T16:38:14.606454200');
insert into sys_role_permission(id, role_id, permission_id, gmt_create)
values (1514163734728187903, 1514161673202679808, 1514161673357869055, '2022-04-13T16:38:14.606454200');
insert into sys_role_permission(id, role_id, permission_id, gmt_create)
values (1514163734728187904, 1514161673055879168, 1514161673357869056, '2022-04-13T16:38:14.606454200');
insert into sys_role_permission(id, role_id, permission_id, gmt_create)
values (1514163734879182848, 1514161673055879168, 1514161673513058304, '2022-04-13T16:38:14.606454200');
insert into sys_role_permission(id, role_id, permission_id, gmt_create)
values (1514163735051149312, 1514161673055879168, 1514162989341659136, '2022-04-13T16:38:14.606454200');
insert into sys_role_permission(id, role_id, permission_id, gmt_create)
values (1514163735218921472, 1514161673055879168, 1514162989492654080, '2022-04-13T16:38:14.606454200');
insert into sys_role_permission(id, role_id, permission_id, gmt_create)
values (1514163735365722112, 1514161673055879168, 1514162989643649024, '2022-04-13T16:38:14.606454200');