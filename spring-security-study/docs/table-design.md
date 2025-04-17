# Table Design

## USER_INFO

Stores basic user information.

| Column                | Type          | Nullable | Default Value     | Primary Key | Index | Description                                  |
| --------------------- | ------------- | -------- | ----------------- | ----------- | ----- | -------------------------------------------- |
| USER_ID               | NUMBER        | NOT NULL |                   | Y           |       | Unique identifier for the user               |
| FULL_NAME             | VARCHAR2(255) | NOT NULL |                   |             |       | Full name of the user                        |
| PASSWORD_HASH         | VARCHAR2(255) | NOT NULL |                   |             |       | Hashed user password                         |
| PASSWORD_SALT         | VARCHAR2(255) | NOT NULL |                   |             |       | Salt for password hashing                    |
| EMAIL                 | VARCHAR2(255) |          |                   |             | Y     | User email address                           |
| PHONE_NUMBER          | VARCHAR2(20)  |          |                   |             |       | User phone number                            |
| USER_TYPE             | VARCHAR2(50)  | NOT NULL |                   |             |       | User type (e.g., internal, external)         |
| ENTITY_TYPE           | VARCHAR2(50)  | NOT NULL |                   |             |       | Entity type (e.g., individual, organization) |
| IS_ENABLED            | CHAR(1)       | NOT NULL | 'Y'               |             |       | Whether the user is enabled (Y/N)            |
| IS_DELETED            | CHAR(1)       | NOT NULL | 'N'               |             |       | Whether the user is deleted (Y/N)            |
| IS_LOCKED             | CHAR(1)       | NOT NULL | 'N'               |             |       | Whether the user is locked (Y/N)             |
| FAILED_LOGIN_ATTEMPTS | NUMBER        | NOT NULL | 0                 |             |       | Number of failed login attempts              |
| CREATED_AT            | TIMESTAMP     | NOT NULL | CURRENT_TIMESTAMP |             |       | Timestamp when the record was created        |
| CREATED_BY            | NUMBER        | NOT NULL |                   |             |       | ID of the user who created the record        |
| UPDATED_AT            | TIMESTAMP     | NOT NULL | CURRENT_TIMESTAMP |             |       | Timestamp when the record was updated        |
| UPDATED_BY            | NUMBER        | NOT NULL |                   |             |       | ID of the user who last updated the record   |

## USER_LOGIN_METHOD

Stores user login method.

PRIMARY KEY (LOGIN_ID, LOGIN_TYPE),   -- Composite primary key to ensure uniqueness within the same provider

| Column      | Type          | Nullable | Default Value     | Primary Key | Index | Description                                                  |
| ----------- | ------------- | -------- | ----------------- | ----------- | ----- | ------------------------------------------------------------ |
| LOGIN_ID    | VARCHAR2(255) | NOT NULL |                   |             |       | Login identifier (e.g., username, email, phone number, third-party ID) |
| LOGIN_TYPE  | VARCHAR2(50)  | NOT NULL |                   |             |       | Login methods such as username, email, phone number, third-party login (such as OAuth, WeChat, Google login, etc.) |
| PROVIDER    | VARCHAR2(50)  |          |                   |             |       | Login provider (e.g., local, google, facebook, wechat)       |
| PROVIDER_ID | VARCHAR2(255) |          |                   |             |       | Unique identifier returned by the third-party provider       |
| USER_ID     | NUMBER        | NOT NULL |                   |             |       | Associated user ID                                           |
| IS_PRIMARY  | CHAR(1)       |          | 'N'               |             |       | Whether this is the primary login method (Y/N)               |
| CREATED_AT  | TIMESTAMP     | NOT NULL | CURRENT_TIMESTAMP |             |       | Timestamp when the record was created                        |
| CREATED_BY  | NUMBER        | NOT NULL |                   |             |       | ID of the user who created the record                        |
| UPDATED_AT  | TIMESTAMP     | NOT NULL | CURRENT_TIMESTAMP |             |       | Timestamp when the record was updated                        |
| UPDATED_BY  | NUMBER        | NOT NULL |                   |             |       | ID of the user who last updated the record                   |

**Field Explanation**

- **`PROVIDER`**:
    - Used to identify the login provider (e.g., `google`, `facebook`, `wechat`, etc.).
    - For local login methods (e.g., username, email, phone number), you can use a value like `local`.
    - Example Values:
        * `local`: Local login (e.g., username, email, phone number).
        * `google`: Google third-party login.
        * `facebook`: Facebook third-party login.
        * `wechat`: WeChat third-party login.
        * `github`: GitHub third-party login.
    - Extensibility: New providers can be added as needed.
- **`PROVIDER_ID`**:
    - Used to store the unique identifier returned by the third-party login provider (e.g., the `sub` field from Google or the `openid` field from WeChat).
    - For local login methods, it can be empty or store the same value as `LOGIN_ID`.
    - Example Values:
        - For Google: The value of the `sub` field (e.g., `1234567890`).
        - For WeChat: The value of the `openid` field (e.g., `oAbc12345xyz`).
        - For local login: It can be empty or store the same value as `LOGIN_ID`.
    - Uniqueness: Must be unique within the same `PROVIDER`.

## LOGIN_HISTORY

单独设计一张登录日志表，记录每次登录的时间、IP 地址、设备信息等。

| Column | Type         | Nullable | Default Value | Primary Key | Description |
| ------ |--------------| -------- | ------------- | ----------- | ----------- |
| ID     | NUMBER       | NOT NULL |               | Y           |             |
| LOGIN_AT | TIMESTAMP    |          |               |             | Timestamp of login |
| LOGIN_IP | VARCHAR2(45) |          |               |             | IP of login |
| LOGIN_STATUS | VARCHAR2(20) |          |               |             |             |

## ORGANIZATION_INFO

Stores organization information.

| Column     | Type      | Nullable | Default Value | Primary Key | Description |
| ---------- | --------- | -------- | ------------- | ----------- | ----------- |
| ORG_ID     | NUMBER    | NOT NULL |               | Y           |             |
| ORG_NAME   | VARCHAR2(255)          |          |               |             |             |
| CREATED_AT | TIMESTAMP |          |               |             |             |
| CREATED_BY | NUMBER    |          |               |             |             |
| UPDATED_AT | TIMESTAMP |          |               |             |             |
| UPDATED_BY | NUMBER    |          |               |             |             |

## USER_ORGANIZATION

Stores the many-to-many relationship between users and organizations.
