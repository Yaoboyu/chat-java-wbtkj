drop database if exists wbtkj_chat;
CREATE DATABASE `wbtkj_chat` CHARACTER SET 'utf8mb4';

drop table if exists user;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `email` VARCHAR(255) NOT NULL COMMENT '邮箱',
    `pwd` VARCHAR(255) NOT NULL COMMENT '加盐后并md5加密后的密码',
    `salt` VARCHAR(5) NOT NULL COMMENT '固定5位字符串',
    `status` INT NOT NULL COMMENT '0有效,-1封禁',
    `quota` BIGINT NOT NULL COMMENT '总额度',
    `cost` BIGINT NOT NULL COMMENT '已花费额度',
    `remark` text COMMENT '默认空字符串',
    `my_inv_code` VARCHAR(6) COMMENT '我的邀请码（二期）',
    `use_inv_code` VARCHAR(6) COMMENT '使用的邀请码（二期）',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_email`(`email`) USING BTREE
) ENGINE=InnoDB;

drop table if exists cdkey_activate;
CREATE TABLE `cdkey_activate` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` BIGINT NOT NULL COMMENT '用户id',
  `cdkey` VARCHAR(255) NOT NULL COMMENT '卡密（20位随机大小写数字组合）',
  `value` BIGINT NOT NULL COMMENT '卡密价值多少点数',
  `use_time` datetime NOT NULL COMMENT '使用日期',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_cdkey`(`cdkey`) USING BTREE
) ENGINE=InnoDB;

drop table if exists openaikey;
CREATE TABLE `openaikey` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `key` VARCHAR(255) NOT NULL COMMENT 'sk',
  `cost` DOUBLE PRECISION NOT NULL COMMENT '余额美元',
  `quota` DOUBLE PRECISION NOT NULL COMMENT '总额美元',
  `status` INTEGER NOT NULL COMMENT '0启用，-1禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;