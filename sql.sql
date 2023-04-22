CREATE DATABASE `wbtkj_chat` CHARACTER SET 'utf8mb4';

CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `email` VARCHAR(255) NOT NULL COMMENT '邮箱',
    `pwd` VARCHAR(255) NOT NULL COMMENT '加盐后并md5加密后的密码',
    `salt` VARCHAR(5) NOT NULL COMMENT '固定5位字符串',
    `status` INT NOT NULL COMMENT '0有效,1封禁',
    `quota` BIGINT NOT NULL COMMENT '总额度',
    `cost` BIGINT NOT NULL COMMENT '已花费额度',
    `remark` text COMMENT '默认空字符串',
    `my_inv_code` VARCHAR(6) COMMENT '我的邀请码（二期）',
    `use_inv_code` VARCHAR(6) COMMENT '使用的邀请码（二期）',
    `create_time` timestamp NOT NULL COMMENT '创建时间',
    `update_time` timestamp NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;