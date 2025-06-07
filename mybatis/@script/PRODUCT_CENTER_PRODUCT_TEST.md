MySQL版本:

```sql
-- 建表语句
CREATE TABLE PRODUCT_TEST (
    ID DECIMAL(22,0) PRIMARY KEY,
    PRODUCT_NAME VARCHAR(100) NOT NULL,
    PRICE DECIMAL(10,2),
    STOCK_QUANTITY INT,
    DESCRIPTION TEXT,
    TAGS JSON,
    STATUS VARCHAR(20),
    CREATE_TIME TIMESTAMP,
    PROPERTIES JSON,
    SEARCH_VECTOR TEXT,
    IS_ACTIVE BOOLEAN DEFAULT TRUE
);

-- 插入测试数据
INSERT INTO PRODUCT_TEST (ID, PRODUCT_NAME, PRICE, STOCK_QUANTITY, DESCRIPTION, TAGS, STATUS, CREATE_TIME, PROPERTIES, SEARCH_VECTOR, IS_ACTIVE) VALUES
(2025060710400222400001, 'iPhone 13', 999.99, 100, 'Latest iPhone model', '["phone", "apple", "5G"]', 'IN_STOCK', '2024-01-01 10:00:00', '{"color": "black", "storage": "128GB"}', 'phone apple mobile', true),
(2025060710400222400002, 'Samsung S21', 899.99, 50, 'Android flagship', '["phone", "samsung", "5G"]', 'IN_STOCK', '2024-01-01 11:00:00', '{"color": "white", "storage": "256GB"}', 'phone samsung android', true),
(2025060710400222400003, 'MacBook Pro', 1299.99, 0, 'Professional laptop', '["laptop", "apple"]', 'OUT_OF_STOCK', '2024-01-02 09:00:00', '{"color": "silver", "ram": "16GB"}', 'laptop apple macbook', true),
(2025060710400222400004, 'AirPods Pro', 249.99, 200, 'Wireless earbuds', '["audio", "apple"]', 'IN_STOCK', '2024-01-03 14:00:00', '{"color": "white", "wireless": true}', 'audio apple airpods', false);
```

Oracle版本:

```sql
-- 建表语句
CREATE TABLE PRODUCT_TEST (
    ID NUMBER(22) PRIMARY KEY,
    PRODUCT_NAME VARCHAR2(100) NOT NULL,
    PRICE NUMBER(10,2),
    STOCK_QUANTITY NUMBER(10),
    DESCRIPTION CLOB,
    TAGS CLOB,
    STATUS VARCHAR2(20),
    CREATE_TIME TIMESTAMP,
    PROPERTIES CLOB,
    SEARCH_VECTOR VARCHAR2(4000),
    IS_ACTIVE NUMBER(1) DEFAULT 1
);

-- 插入测试数据
INSERT INTO PRODUCT_TEST (ID, PRODUCT_NAME, PRICE, STOCK_QUANTITY, DESCRIPTION, TAGS, STATUS, CREATE_TIME, PROPERTIES, SEARCH_VECTOR, IS_ACTIVE) VALUES
(2025060710400222400001, 'iPhone 13', 999.99, 100, 'Latest iPhone model', '["phone", "apple", "5G"]', 'IN_STOCK', TIMESTAMP '2024-01-01 10:00:00', '{"color": "black", "storage": "128GB"}', 'phone apple mobile', 1);
INSERT INTO PRODUCT_TEST (ID, PRODUCT_NAME, PRICE, STOCK_QUANTITY, DESCRIPTION, TAGS, STATUS, CREATE_TIME, PROPERTIES, SEARCH_VECTOR, IS_ACTIVE) VALUES
(2025060710400222400002, 'Samsung S21', 899.99, 50, 'Android flagship', '["phone", "samsung", "5G"]', 'IN_STOCK', TIMESTAMP '2024-01-01 11:00:00', '{"color": "white", "storage": "256GB"}', 'phone samsung android', 1);
INSERT INTO PRODUCT_TEST (ID, PRODUCT_NAME, PRICE, STOCK_QUANTITY, DESCRIPTION, TAGS, STATUS, CREATE_TIME, PROPERTIES, SEARCH_VECTOR, IS_ACTIVE) VALUES
(2025060710400222400003, 'MacBook Pro', 1299.99, 0, 'Professional laptop', '["laptop", "apple"]', 'OUT_OF_STOCK', TIMESTAMP '2024-01-02 09:00:00', '{"color": "silver", "ram": "16GB"}', 'laptop apple macbook', 1);
INSERT INTO PRODUCT_TEST (ID, PRODUCT_NAME, PRICE, STOCK_QUANTITY, DESCRIPTION, TAGS, STATUS, CREATE_TIME, PROPERTIES, SEARCH_VECTOR, IS_ACTIVE) VALUES
(2025060710400222400004, 'AirPods Pro', 249.99, 200, 'Wireless earbuds', '["audio", "apple"]', 'IN_STOCK', TIMESTAMP '2024-01-03 14:00:00', '{"color": "white", "wireless": true}', 'audio apple airpods', 0);
```

PostgreSQL版本:

```sql
-- 建表语句
CREATE TABLE "PRODUCT_TEST" (
    "ID" NUMBER(22) PRIMARY KEY,
    "PRODUCT_NAME" VARCHAR(100) NOT NULL,
    "PRICE" DECIMAL(10,2),
    "STOCK_QUANTITY" INTEGER,
    "DESCRIPTION" TEXT,
    "TAGS" JSONB,
    "STATUS" VARCHAR(20),
    "CREATE_TIME" TIMESTAMP,
    "PROPERTIES" JSONB,
    "SEARCH_VECTOR" TSVECTOR,
    "IS_ACTIVE" BOOLEAN DEFAULT TRUE
);

-- 创建GIN索引用于全文搜索和JSON查询
CREATE INDEX "IDX_PRODUCT_SEARCH_VECTOR" ON "PRODUCT_TEST" USING GIN("SEARCH_VECTOR");
CREATE INDEX "IDX_PRODUCT_TAGS" ON "PRODUCT_TEST" USING GIN("TAGS");
CREATE INDEX "IDX_PRODUCT_PROPERTIES" ON "PRODUCT_TEST" USING GIN("PROPERTIES");

-- 插入测试数据
INSERT INTO "PRODUCT_TEST" ("ID", "PRODUCT_NAME", "PRICE", "STOCK_QUANTITY", "DESCRIPTION", "TAGS", "STATUS", "CREATE_TIME", "PROPERTIES", "SEARCH_VECTOR", "IS_ACTIVE") VALUES
(2025060710400222400001, 'iPhone 13', 999.99, 100, 'Latest iPhone model', '["phone", "apple", "5G"]', 'IN_STOCK', '2024-01-01 10:00:00', '{"color": "black", "storage": "128GB"}', to_tsvector('english', 'phone apple mobile'), true);

INSERT INTO "PRODUCT_TEST" ("ID", "PRODUCT_NAME", "PRICE", "STOCK_QUANTITY", "DESCRIPTION", "TAGS", "STATUS", "CREATE_TIME", "PROPERTIES", "SEARCH_VECTOR", "IS_ACTIVE") VALUES
(2025060710400222400002, 'Samsung S21', 899.99, 50, 'Android flagship', '["phone", "samsung", "5G"]', 'IN_STOCK', '2024-01-01 11:00:00', '{"color": "white", "storage": "256GB"}', to_tsvector('english', 'phone samsung android'), true);

INSERT INTO "PRODUCT_TEST" ("ID", "PRODUCT_NAME", "PRICE", "STOCK_QUANTITY", "DESCRIPTION", "TAGS", "STATUS", "CREATE_TIME", "PROPERTIES", "SEARCH_VECTOR", "IS_ACTIVE") VALUES
(2025060710400222400003, 'MacBook Pro', 1299.99, 0, 'Professional laptop', '["laptop", "apple"]', 'OUT_OF_STOCK', '2024-01-02 09:00:00', '{"color": "silver", "ram": "16GB"}', to_tsvector('english', 'laptop apple macbook'), true);

INSERT INTO "PRODUCT_TEST" ("ID", "PRODUCT_NAME", "PRICE", "STOCK_QUANTITY", "DESCRIPTION", "TAGS", "STATUS", "CREATE_TIME", "PROPERTIES", "SEARCH_VECTOR", "IS_ACTIVE") VALUES
(2025060710400222400004, 'AirPods Pro', 249.99, 200, 'Wireless earbuds', '["audio", "apple"]', 'IN_STOCK', '2024-01-03 14:00:00', '{"color": "white", "wireless": true}', to_tsvector('english', 'audio apple airpods'), false);
```

