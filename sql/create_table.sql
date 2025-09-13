create database if not exists template_db;

use template_db;

create table if not exists user (
    id bigint auto_increment comment 'id',
    password varchar(256) not null comment '用户密码',
    username varchar(256) default '' null comment '用户名',
    avatar varchar(256) default '' null comment '用户头像',
    profile varchar(256) default '' null  comment '用户简介',
    phone varchar(10) default '' null comment '手机号',
    email varchar(20) default '' null comment '邮箱',
    tag_list varchar(1024) default '' null comment '标签',
    user_role varchar(20) default '' null comment '角色: 管理员:admin  禁用: ban',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    is_delete tinyint default 0 comment '是否删除 0 不删除 1 删除',
    primary key(id)
    ) comment '用户' collate utf8mb4_general_ci;

create table if not exists tag (
    id bigint auto_increment comment 'id',
    name varchar(255) not null comment '标签名字',
    parent_id bigint null comment '父id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    is_delete tinyint default 0 comment '是否删除 0 不删除 1 删除',
    primary key(id)
    ) comment '标签表' collate utf8mb4_general_ci