create table t_user
(
    id       bigint,
    nickname nvarchar(50),
    username varchar(50),
    password varchar(50)
);

create table t_order
(
    id       bigint,
    user_id  bigint,
    total_amount decimal(18,2)
);

create table t_order_item
(
    id       bigint,
    order_id  bigint,
    product_id bigint,
    product_amount decimal(18,2),
    product_count int
);

create table t_dictionary
(
    id       bigint,
    pid  bigint,
    dic_key nvarchar(50),
    dic_val nvarchar(50)
);
