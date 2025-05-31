MySQL 版本：
```sql
-- Create Table for MySQL
CREATE TABLE USER_INFO (
    ID DECIMAL(22,0) PRIMARY KEY,
    USERNAME VARCHAR(255),
    PASSWORD VARCHAR(255),
    FIRST_NAME VARCHAR(255),
    LAST_NAME VARCHAR(255),
    CREATE_AT DATETIME,
    CREATE_BY VARCHAR(255),
    UPDATED_AT DATETIME,
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
    ?,              -- BigDecimal
    ?,              -- String
    ?,              -- String
    ?,              -- String
    ?,              -- String
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

