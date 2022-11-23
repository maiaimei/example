-- MySQL
CREATE TABLE `shedlock`
(
    `name`       varchar(64)  NOT NULL COMMENT '定时任务名称，必须唯一',
    `lock_until` timestamp(3) NOT NULL COMMENT '锁的结束时间',
    `locked_at`  timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '锁的开始时间',
    `locked_by`  varchar(255) NOT NULL,
    PRIMARY KEY (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3;


-- H2
CREATE TABLE `shedlock`
(
    `name`       varchar(64)  NOT NULL COMMENT '定时任务名称，必须唯一',
    `lock_until` timestamp(3) NOT NULL COMMENT '锁的结束时间',
    `locked_at`  timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '锁的开始时间',
    `locked_by`  varchar(255) NOT NULL,
    PRIMARY KEY (`name`)
);

