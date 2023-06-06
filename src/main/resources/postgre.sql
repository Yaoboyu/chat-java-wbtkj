drop database if exists wbtkj_chat;
CREATE DATABASE wbtkj_chat
    ENCODING 'UTF8'
    LC_COLLATE 'en_US.utf8'
    LC_CTYPE 'en_US.utf8';
\c wbtkj_chat;

CREATE EXTENSION vector;

drop SEQUENCE if exists admin_id_seq CASCADE;
drop SEQUENCE if exists recharge_record_id_seq CASCADE;
drop SEQUENCE if exists role_id_seq CASCADE;
drop SEQUENCE if exists third_party_model_key_id_seq CASCADE;
drop SEQUENCE if exists user_info_id_seq CASCADE;
drop SEQUENCE if exists user_role_id_seq CASCADE;

-- 执行以下代码
-- --------------------------------------
CREATE SEQUENCE admin_id_seq
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
drop table if exists "admin";
CREATE TABLE "admin" (
     "id" BIGINT PRIMARY KEY DEFAULT nextval('admin_id_seq'),
     "username" varchar(255) NOT NULL,
     "pwd" varchar(255) NOT NULL,
     "salt" varchar(5) NOT NULL,
     "create_time" timestamp NOT NULL,
     "update_time" timestamp NOT NULL
);
COMMENT ON COLUMN "admin"."id" IS '自增id';
COMMENT ON COLUMN "admin"."username" IS '用户名';
COMMENT ON COLUMN "admin"."pwd" IS '密码';
COMMENT ON COLUMN "admin"."salt" IS '密码盐';
COMMENT ON COLUMN "admin"."create_time" IS '创建时间';
COMMENT ON COLUMN "admin"."update_time" IS '修改时间';

INSERT INTO "admin" (username, pwd, salt, create_time, update_time) VALUES ('admin','45be07bc1f199c69','68723','2019-09-03 13:31:20','2023-04-06 09:54:02');

-- --------------------------------------
CREATE SEQUENCE recharge_record_id_seq
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
drop table if exists "recharge_record";
CREATE TABLE "recharge_record" (
   "id" BIGINT PRIMARY KEY DEFAULT nextval('recharge_record_id_seq'),
   "user_id" int8 NOT NULL,
   "type" int4 NOT NULL,
   "cdkey" varchar(255),
   "value" int4 NOT NULL,
   "use_time" timestamp NOT NULL
);
CREATE INDEX "idx_user_id" ON "recharge_record" USING btree (
    "user_id"
    );
CREATE INDEX "idx_cdkey" ON "recharge_record" USING btree (
    "cdkey"
    );
COMMENT ON COLUMN "recharge_record"."id" IS '自增主键';
COMMENT ON COLUMN "recharge_record"."user_id" IS '用户id';
COMMENT ON COLUMN "recharge_record"."type" IS '充值类型。0余额，1vip';
COMMENT ON COLUMN "recharge_record"."cdkey" IS '使用的卡密';
COMMENT ON COLUMN "recharge_record"."value" IS '充值点数。若为vip的话为充值期限';
COMMENT ON COLUMN "recharge_record"."use_time" IS '使用日期';


-- --------------------------------------
CREATE SEQUENCE role_id_seq
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
drop table if exists "role";
CREATE TABLE "role" (
    "id" BIGINT PRIMARY KEY DEFAULT nextval('role_id_seq'),
    "user_id" int8 NOT NULL,
    "avatar" varchar(255) NOT NULL,
    "nickname" varchar(255) NOT NULL,
    "greeting" text NOT NULL,
    "model" varchar(20) NOT NULL,
    "system" text NOT NULL,
    "context_n" int4 NOT NULL,
    "max_tokens" int4 NOT NULL,
    "temperature" float8 NOT NULL,
    "top_p" float8 NOT NULL,
    "frequency_penalty" float8 NOT NULL,
    "presence_penalty" float8 NOT NULL,
    "logit_bias" text,
    "stop" varchar(5),
    "is_market" bool NOT NULL,
    "market_type" int4 NOT NULL,
    "likes" int4 NOT NULL,
    "hot" int4 NOT NULL,
    "create_time" timestamp NOT NULL,
    "update_time" timestamp NOT NULL
);
COMMENT ON COLUMN "role"."id" IS '自增主键';
COMMENT ON COLUMN "role"."user_id" IS '所属用户id';
COMMENT ON COLUMN "role"."avatar" IS '头像';
COMMENT ON COLUMN "role"."nickname" IS '角色昵称';
COMMENT ON COLUMN "role"."greeting" IS '角色招呼语';
COMMENT ON COLUMN "role"."model" IS '使用的模型';
COMMENT ON COLUMN "role"."system" IS '角色指令。此次对话的system预设指令';
COMMENT ON COLUMN "role"."context_n" IS '上下文数。2-36的整数，默认16。AI回复时携带上下文的总条数，当小于10时，基本可以无限对话，值越小越节省token，但上下文的关联性会越差。值为2即一个回合（无上下文）。请根据对话长短自行调整。';
COMMENT ON COLUMN "role"."max_tokens" IS '最大回复。1-2048的整数，默认1200。设置AI最大回复内容大小,会影响返回结果的长度。普通聊天建议100-300、短文生成建议500-900、长文、代码生成建议900-1200。';
COMMENT ON COLUMN "role"."temperature" IS '随机属性1。0-2的小数，默认1。通常用于生成有趣、创意性的文本。值越大回复内容越赋有多样性、创造性、随机性。设为0-根据事实回答，希望得到事实答案应该降低该参数。日常聊天建议0.5-0.8。建议随机属性1和随机属性2两个参数只改变一个，另一个保持默认值。';
COMMENT ON COLUMN "role"."top_p" IS '随机属性2。0-1的小数，默认1。通常用于生成技术文档、科学论文等需要准确性的文本。较大的值会导致生成的文本更具有多祥性，但是会栖牲一定的准确性和保真度。较低的值则会导致生成的文本更加保险和准确，但是可能会过于死板和缺乏新意。建议随机属性1和随机属性2两个参数只改变一个，另一个保持默认值。';
COMMENT ON COLUMN "role"."frequency_penalty" IS '重复属性。-2.0-2.0的小数，默认值0。值越大越能够让AI更好地避免重复使用之前说过的词汇。建议微调或不变。';
COMMENT ON COLUMN "role"."presence_penalty" IS '话题属性。-2.0-2.0的小数，默认值0。值越大越能够让AI更好地控制新话题的引入。建议微调或不变。';
COMMENT ON COLUMN "role"."logit_bias" IS '词汇权重。给词汇添加-100到100的权重，表示其偏差值。像-1和1之间的值应该会轻微减少或增加选择的可能性；像-100或100这样的值会导致相关词汇的禁止或独占选择。';
COMMENT ON COLUMN "role"."stop" IS '停止符';
COMMENT ON COLUMN "role"."is_market" IS '是否上架应用市场';
COMMENT ON COLUMN "role"."market_type" IS '应用市场上的分类';
COMMENT ON COLUMN "role"."likes" IS '点赞数';
COMMENT ON COLUMN "role"."hot" IS '热度（有人对话一次热度就加1）';
COMMENT ON COLUMN "role"."create_time" IS '创建时间';
COMMENT ON COLUMN "role"."update_time" IS '修改时间';

INSERT INTO "role" (user_id, avatar, nickname, greeting, model, system, context_n, max_tokens, temperature, top_p, frequency_penalty, presence_penalty, logit_bias, stop, is_market, market_type, likes, hot, create_time, update_time)
VALUES (0, 'https://ui-avatars.com/api/?background=70a99b&color=fff&name=GPT', '默认GPT3.5角色', '有什么需要帮助的？', 'gpt-3.5-turbo', '', 10, 1000, 1, 1, 0, 0, null, null, true, 0, 0, 0, CURRENT_DATE, CURRENT_DATE);

-- --------------------------------------
CREATE SEQUENCE third_party_model_key_id_seq
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
drop table if exists "third_party_model_key";
CREATE TABLE "third_party_model_key" (
     "id" BIGINT PRIMARY KEY DEFAULT nextval('third_party_model_key_id_seq'),
     "key" varchar(255) NOT NULL,
     "model" varchar(255) NOT NULL,
     "status" int4 NOT NULL,
     "create_time" timestamp NOT NULL,
     "update_time" timestamp NOT NULL
);
CREATE UNIQUE INDEX "idx_key" ON "third_party_model_key" USING btree (
    "key"
    );
CREATE INDEX "idx_status_key" ON "third_party_model_key" USING btree (
    "status"
    );
COMMENT ON COLUMN "third_party_model_key"."id" IS '自增主键';
COMMENT ON COLUMN "third_party_model_key"."key" IS 'apikey';
COMMENT ON COLUMN "third_party_model_key"."model" IS '调用的模型';
COMMENT ON COLUMN "third_party_model_key"."status" IS '0启用，-1禁用，1余额耗尽或过期';
COMMENT ON COLUMN "third_party_model_key"."create_time" IS '创建时间';
COMMENT ON COLUMN "third_party_model_key"."update_time" IS '修改时间';

INSERT INTO "third_party_model_key" (key, model, status, create_time, update_time) VALUES ('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', 'gpt-3.5-turbo', 0, '2023-04-06 09:54:02', '2023-04-06 09:54:02');
INSERT INTO "third_party_model_key" (key, model, status, create_time, update_time) VALUES ('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', 'gpt-3.5-turbo', 0, '2023-04-06 09:54:02', '2023-04-06 09:54:02');
INSERT INTO "third_party_model_key" (key, model, status, create_time, update_time) VALUES ('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', 'gpt-4', 1, '2023-04-06 09:54:02', '2023-04-06 09:54:02');
INSERT INTO "third_party_model_key" (key, model, status, create_time, update_time) VALUES ('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', 'gpt-3.5-turbo', 0, '2023-05-31 20:26:15', '2023-05-31 20:26:19');
INSERT INTO "third_party_model_key" (key, model, status, create_time, update_time) VALUES ('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', 'gpt-3.5-turbo', 0, '2023-05-31 20:27:31', '2023-05-31 20:27:33');
INSERT INTO "third_party_model_key" (key, model, status, create_time, update_time) VALUES ('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', 'gpt-3.5-turbo', 0, '2023-05-31 20:28:00', '2023-05-31 20:28:02');

-- --------------------------------------
CREATE SEQUENCE user_info_id_seq
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
drop table if exists "user_info";
CREATE TABLE "user_info" (
    "id" BIGINT PRIMARY KEY DEFAULT nextval('user_info_id_seq'),
    "email" varchar(255) NOT NULL,
    "pwd" varchar(255) NOT NULL,
    "salt" varchar(5) NOT NULL,
    "status" int4 NOT NULL,
    "vip_start_time" timestamp,
    "vip_end_time" timestamp,
    "balance" int4 NOT NULL,
    "cash" float8 NOT NULL,
    "remark" text,
    "my_inv_code" varchar(15) NOT NULL,
    "use_inv_code" int8,
    "create_time" timestamp NOT NULL,
    "update_time" timestamp NOT NULL
);
CREATE UNIQUE INDEX "idx_email" ON "user_info" USING btree (
    "email"
    );
CREATE UNIQUE INDEX "idx_my_inv_code" ON "user_info" USING btree (
    "my_inv_code"
    );
CREATE INDEX "idx_use_inv_code" ON "user_info" USING btree (
    "use_inv_code"
    );
COMMENT ON COLUMN "user_info"."id" IS '自增主键';
COMMENT ON COLUMN "user_info"."email" IS '邮箱';
COMMENT ON COLUMN "user_info"."pwd" IS '加盐后并md5加密后的密码';
COMMENT ON COLUMN "user_info"."salt" IS '固定5位字符串';
COMMENT ON COLUMN "user_info"."status" IS '0有效，-1封禁，1VIP';
COMMENT ON COLUMN "user_info"."vip_start_time" IS '会员开始时间';
COMMENT ON COLUMN "user_info"."vip_end_time" IS '会员结束时间';
COMMENT ON COLUMN "user_info"."balance" IS '余额';
COMMENT ON COLUMN "user_info"."cash" IS '提现账户（里面的余额可转为余额或者提现）';
COMMENT ON COLUMN "user_info"."remark" IS '默认空字符串';
COMMENT ON COLUMN "user_info"."my_inv_code" IS '我的邀请码';
COMMENT ON COLUMN "user_info"."use_inv_code" IS '使用的邀请码';
COMMENT ON COLUMN "user_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "user_info"."update_time" IS '修改时间';

-- --------------------------------------
CREATE SEQUENCE user_role_id_seq
    START 1
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
drop table if exists "user_role";
CREATE TABLE "user_role" (
     "id" BIGINT PRIMARY KEY DEFAULT nextval('user_role_id_seq'),
     "user_id" int8 NOT NULL,
     "role_id" int8 NOT NULL,
     "used" int4 NOT NULL,
     "status" int4 NOT NULL,
     "top" bool NOT NULL
);
CREATE INDEX "idx_user_id_status_top" ON "user_role" USING btree (
    "user_id",
    "status",
    "top"
    );
CREATE INDEX "idx_role_id_user_id" ON "user_role" USING btree (
    "role_id",
    "user_id"
    );
COMMENT ON COLUMN "user_role"."id" IS '自增主键';
COMMENT ON COLUMN "user_role"."user_id" IS '用户id';
COMMENT ON COLUMN "user_role"."role_id" IS '角色id';
COMMENT ON COLUMN "user_role"."used" IS '该用户在该角色上使用的点数';
COMMENT ON COLUMN "user_role"."status" IS '状态。0正常使用，-1软删除';
COMMENT ON COLUMN "user_role"."top" IS '是否置顶。0不置顶，1置顶';



