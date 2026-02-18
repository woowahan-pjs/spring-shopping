create table category
(
    id          bigint auto_increment primary key,
    name        varchar(255) not null unique,
    color       varchar(7)   not null,
    image_url   varchar(255) not null,
    description varchar(255)
);

create table product
(
    id          bigint auto_increment primary key,
    name        varchar(15)  not null,
    price       int          not null,
    image_url   varchar(255) not null,
    category_id bigint       not null,
    foreign key (category_id) references category (id)
);

create table member
(
    id                 bigint auto_increment primary key,
    email              varchar(255) not null unique,
    password           varchar(255),
    kakao_access_token varchar(512),
    point              int          not null default 0
);

create table wish
(
    id         bigint auto_increment primary key,
    member_id  bigint not null,
    product_id bigint not null,
    foreign key (member_id) references member (id),
    foreign key (product_id) references product (id)
);

create table options
(
    id         bigint auto_increment primary key,
    product_id bigint      not null,
    name       varchar(50) not null,
    quantity   int         not null,
    foreign key (product_id) references product (id)
);

create table orders
(
    id              bigint auto_increment primary key,
    option_id       bigint    not null,
    member_id       bigint    not null,
    quantity        int       not null,
    message         varchar(255),
    order_date_time timestamp not null,
    foreign key (option_id) references options (id),
    foreign key (member_id) references member (id)
);
