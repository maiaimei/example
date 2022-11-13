use testdb2;

CREATE TABLE `sys_user`
(
    `id`           bigint    NOT NULL,
    `nickname`     varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
    `username`     varchar(50)                                            DEFAULT NULL,
    `password`     varchar(50)                                            DEFAULT NULL,
    `gmt_create`   timestamp NULL                                         DEFAULT NULL,
    `gmt_modified` timestamp NULL                                         DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;
