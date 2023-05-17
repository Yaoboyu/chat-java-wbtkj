drop database if exists wbtkj_chat;
CREATE DATABASE `wbtkj_chat` CHARACTER SET 'utf8mb4';
use wbtkj_chat;

drop table if exists user;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `email` VARCHAR(255) NOT NULL COMMENT '邮箱',
    `pwd` VARCHAR(255) NOT NULL COMMENT '加盐后并md5加密后的密码',
    `salt` VARCHAR(5) NOT NULL COMMENT '固定5位字符串',
    `status` INT NOT NULL COMMENT '0有效，-1封禁，1VIP',
    `vip_start_time` datetime COMMENT '会员开始时间',
    `vip_end_time` datetime COMMENT '会员结束时间',
    `balance` INT NOT NULL COMMENT '余额',
    `cash` DOUBLE NOT NULL COMMENT '提现账户（里面的余额可转为余额或者提现）',
    `remark` text COMMENT '默认空字符串',
    `my_inv_code` VARCHAR(15) NOT NULL COMMENT '我的邀请码',
    `use_inv_code` VARCHAR(15) COMMENT '使用的邀请码',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_email`(`email`) USING BTREE,
    UNIQUE INDEX `idx_my_inv_code`(`my_inv_code`) USING BTREE
) ENGINE=InnoDB;

drop table if exists recharge_record;
CREATE TABLE `recharge_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` BIGINT NOT NULL COMMENT '用户id',
  `type` INT NOT NULL COMMENT '充值类型。0余额，1vip',
  `cdkey` VARCHAR(255) COMMENT '使用的卡密',
  `value` INT NOT NULL COMMENT '充值点数。若为vip的话为充值期限',
  `use_time` datetime NOT NULL COMMENT '使用日期',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_cdkey`(`cdkey`) USING BTREE
) ENGINE=InnoDB;

drop table if exists third_party_model_key;
CREATE TABLE `third_party_model_key` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `key` VARCHAR(255) NOT NULL COMMENT 'apikey',
  `model` VARCHAR(255) NOT NULL COMMENT '调用的模型',
  `status` INT NOT NULL COMMENT '0启用，-1禁用，1余额耗尽或过期',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_key`(`key`) USING BTREE,
  INDEX `idx_status_key`(`status`) USING BTREE
) ENGINE=InnoDB;

LOCK TABLES `third_party_model_key` WRITE;
INSERT INTO `third_party_model_key` VALUES (1,'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','gpt-3.5-turbo','0','2023-04-06 09:54:02','2023-04-06 09:54:02');
INSERT INTO `third_party_model_key` VALUES (2,'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','gpt-3.5-turbo','0','2023-04-06 09:54:02','2023-04-06 09:54:02');
INSERT INTO `third_party_model_key` VALUES (3,'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','gpt-4','0','2023-04-06 09:54:02','2023-04-06 09:54:02');
UNLOCK TABLES;

drop table if exists admin;
CREATE TABLE IF NOT EXISTS `admin` (
  `id` BIGINT NOT NULL COMMENT '自增id',
  `username` VARCHAR(255) NOT NULL COMMENT '用户名',
  `pwd` VARCHAR(255) NOT NULL COMMENT '密码',
  `salt` VARCHAR(5) NOT NULL COMMENT '密码盐',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

/* 用户名：admin, 密码：admin123 */
LOCK TABLES `admin` WRITE;
INSERT INTO `admin` VALUES (1,'admin','45be07bc1f199c69','68723','2019-09-03 13:31:20','2023-04-06 09:54:02');
UNLOCK TABLES;

drop table if exists role;
CREATE TABLE IF NOT EXISTS `role` (
  `id` BIGINT NOT NULL COMMENT '自增主键',
  `user_id` BIGINT NOT NULL COMMENT '所属用户id',
  `avatar` VARCHAR(255) NOT NULL COMMENT '头像',
  `nickname` VARCHAR(255) NOT NULL COMMENT '角色昵称',
  `greeting` TEXT NOT NULL COMMENT '角色招呼语',
  `model` INT NOT NULL COMMENT '使用的模型。0-gpt3，1-gpt4',
  `system` TEXT NOT NULL COMMENT '角色指令。此次对话的system预设指令',
  `context_n` INT NOT NULL DEFAULT 16 COMMENT '上下文数。2-36的整数，默认16。AI回复时携带上下文的总条数，当小于16时，基本可以无限对话，值越小越节省token，但上下文的关联性会越差。值为2即一个回合（无上下文）。请根据对话长短自行调整。',
  `max_tokens` INT NOT NULL DEFAULT 1200 COMMENT '最大回复。1-2048的整数，默认1200。设置AI最大回复内容大小,会影响返回结果的长度。普通聊天建议100-300、短文生成建议500-900、长文、代码生成建议900-1200。',
  `temperature` DOUBLE NOT NULL DEFAULT 1 COMMENT '随机属性1。0-2的小数，默认1。通常用于生成有趣、创意性的文本。值越大回复内容越赋有多样性、创造性、随机性。设为0-根据事实回答，希望得到事实答案应该降低该参数。日常聊天建议0.5-0.8。建议随机属性1和随机属性2两个参数只改变一个，另一个保持默认值。',
  `top_p` DOUBLE NOT NULL DEFAULT 1 COMMENT '随机属性2。0-1的小数，默认1。通常用于生成技术文档、科学论文等需要准确性的文本。较大的值会导致生成的文本更具有多祥性，但是会栖牲一定的准确性和保真度。较低的值则会导致生成的文本更加保险和准确，但是可能会过于死板和缺乏新意。建议随机属性1和随机属性2两个参数只改变一个，另一个保持默认值。',
  `frequency_penalty` DOUBLE NOT NULL DEFAULT 0 COMMENT '重复属性。-2.0-2.0的小数，默认值0。值越大越能够让AI更好地避免重复使用之前说过的词汇。建议微调或不变。',
  `presence_penalty` DOUBLE NOT NULL DEFAULT 0 COMMENT '话题属性。-2.0-2.0的小数，默认值0。值越大越能够让AI更好地控制新话题的引入。建议微调或不变。',
  `logit_bias` JSON COMMENT '词汇权重。给词汇添加-100到100的权重，表示其偏差值。像-1和1之间的值应该会轻微减少或增加选择的可能性；像-100或100这样的值会导致相关词汇的禁止或独占选择。',
  `stop` VARCHAR(5) NOT NULL DEFAULT '#####' COMMENT '停止符',
  `is_market` BOOLEAN NOT NULL DEFAULT true COMMENT '是否上架应用市场',
  `market_type` INT NOT NULL COMMENT '应用市场上的分类',
  `likes` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
  `hot` INT NOT NULL DEFAULT 0 COMMENT '热度（有人对话一次热度就加1）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

drop table if exists user_role;
CREATE TABLE IF NOT EXISTS `user_role` (
    `id` BIGINT NOT NULL COMMENT '自增主键',
    `user_id` BIGINT NOT NULL COMMENT '用户id',
    `role_id` BIGINT NOT NULL COMMENT '角色id',
    `free_Balance` INT NOT NULL COMMENT '免费额度',
    `used` INT NOT NULL COMMENT '该用户在该角色上使用的点数',
    `status` INT NOT NULL COMMENT '状态。0正常使用，-1软删除',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id_status`(`user_id`,`status`) USING BTREE,
    INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE=InnoDB;
