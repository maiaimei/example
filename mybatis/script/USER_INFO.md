MySQL 版本：
```sql
-- Create Table for MySQL
CREATE TABLE USER_INFO (
    ID DECIMAL(22,0) PRIMARY KEY,
    USERNAME VARCHAR(255) NOT NULL COMMENT '用户名',
    PASSWORD VARCHAR(255) NOT NULL COMMENT '密码',
    FIRST_NAME VARCHAR(255) COMMENT '名',
    LAST_NAME VARCHAR(255) COMMENT '姓',
    IS_ENABLED TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
    IS_DELETED TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    CREATE_AT DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CREATE_BY VARCHAR(255) COMMENT '创建人',
    UPDATED_AT DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UPDATED_BY VARCHAR(255) COMMENT '更新人',
    INDEX idx_username (USERNAME),
    INDEX idx_is_deleted_enabled (IS_DELETED, IS_ENABLED)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';


-- Insert statement
INSERT INTO USER_INFO (
    ID,
    USERNAME,
    PASSWORD,
    FIRST_NAME,
    LAST_NAME,
    CREATE_AT,
    CREATE_BY,
    UPDATED_AT,
    UPDATED_BY
) VALUES (
    ?,              -- BigDecimal
    ?,              -- String
    ?,              -- String
    ?,              -- String
    ?,              -- String
    ?,              -- Boolean
    ?,              -- Boolean
    ?,              -- LocalDateTime
    ?,              -- String
    ?,              -- LocalDateTime
    ?               -- String
);

-- Example insert with values
INSERT INTO USER_INFO VALUES (
    2025053116530670200001,
    'johndoe',
    'encrypted_password',
    'John',
    'Doe',
    0,
    1,
    NOW(),
    'system',
    NOW(),
    'system'
);
```

PostgreSQL 版本：

```sql
-- Create Table for PostgreSQL
CREATE TABLE USER_INFO (
    ID NUMERIC(22,0) PRIMARY KEY,
    USERNAME VARCHAR(255),
    PASSWORD VARCHAR(255),
    FIRST_NAME VARCHAR(255),
    LAST_NAME VARCHAR(255),
    CREATE_AT TIMESTAMP,
    CREATE_BY VARCHAR(255),
    UPDATED_AT TIMESTAMP,
    UPDATED_BY VARCHAR(255)
);

-- Insert statement
INSERT INTO USER_INFO (
    ID,
    USERNAME,
    PASSWORD,
    FIRST_NAME,
    LAST_NAME,
    CREATE_AT,
    CREATE_BY,
    UPDATED_AT,
    UPDATED_BY
) VALUES (
    $1,             -- BigDecimal
    $2,             -- String
    $3,             -- String
    $4,             -- String
    $5,             -- String
    $6,             -- LocalDateTime
    $7,             -- String
    $8,             -- LocalDateTime
    $9              -- String
);

-- Example insert with values
INSERT INTO USER_INFO VALUES (
    2025053116530670200001,
    'johndoe',
    'encrypted_password',
    'John',
    'Doe',
    CURRENT_TIMESTAMP,
    'system',
    CURRENT_TIMESTAMP,
    'system'
);
```

