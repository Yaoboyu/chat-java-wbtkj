drop database if exists wbtkj_chat;
CREATE DATABASE wbtkj_chat
    ENCODING 'UTF8'
    LC_COLLATE 'en_US.utf8'
    LC_CTYPE 'en_US.utf8';
\c wbtkj_chat;

CREATE EXTENSION vector;

-- 执行以下代码
-- --------------------------------------
drop table if exists "admin";
CREATE TABLE "admin" (
     "id" bigserial PRIMARY KEY,
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

-- wbtkj wbtkj@0317
INSERT INTO "admin" (username, pwd, salt, create_time, update_time) VALUES ('wbtkj','ff09c585cf74ef54','68723','2019-09-03 13:31:20','2023-04-06 09:54:02');

-- --------------------------------------
drop table if exists "recharge_record";
CREATE TABLE "recharge_record" (
   "id" bigserial PRIMARY KEY,
   "user_id" int8 NOT NULL,
   "type" int4 NOT NULL,
   "cdkey" varchar(255),
   "value" int4 NOT NULL,
   "use_time" timestamp NOT NULL
);
CREATE INDEX "recharge_record_idx_user_id" ON "recharge_record" USING btree (
    "user_id"
    );
CREATE INDEX "recharge_record_idx_cdkey" ON "recharge_record" USING btree (
    "cdkey"
    );
COMMENT ON COLUMN "recharge_record"."id" IS '自增主键';
COMMENT ON COLUMN "recharge_record"."user_id" IS '用户id';
COMMENT ON COLUMN "recharge_record"."type" IS '充值类型。0余额，1vip';
COMMENT ON COLUMN "recharge_record"."cdkey" IS '使用的卡密';
COMMENT ON COLUMN "recharge_record"."value" IS '充值点数。若为vip的话为充值期限';
COMMENT ON COLUMN "recharge_record"."use_time" IS '使用日期';


-- --------------------------------------
drop table if exists "role";
CREATE TABLE "role" (
    "id" bigserial PRIMARY KEY,
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
    "market_status" text,
    "market_type" int4,
    "origin_role_id" int8,
    "file_names" text[],
    "likes" int4 NOT NULL,
    "hot" int4 NOT NULL,
    "create_time" timestamp NOT NULL,
    "update_time" timestamp NOT NULL
);
CREATE INDEX "role_idx_is_market" ON "role" USING btree (
    "is_market", "market_status", "market_type"
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
COMMENT ON COLUMN "role"."market_status" IS '上架状态。null不需要上架；等待审核；上架成功；需要修改后重新提交上架审核';
COMMENT ON COLUMN "role"."market_type" IS '应用市场上的分类';
COMMENT ON COLUMN "role"."origin_role_id" IS '从哪个角色生成的副本';
COMMENT ON COLUMN "role"."file_names" IS '使用的数据集';
COMMENT ON COLUMN "role"."likes" IS '点赞数';
COMMENT ON COLUMN "role"."hot" IS '热度（有人对话一次热度就加1）';
COMMENT ON COLUMN "role"."create_time" IS '创建时间';
COMMENT ON COLUMN "role"."update_time" IS '修改时间';

INSERT INTO "role" (user_id, avatar, nickname, greeting, model, "system", context_n, max_tokens, temperature, top_p, frequency_penalty, presence_penalty, logit_bias, stop, is_market, market_status, market_type, origin_role_id, file_names, likes, hot, create_time, update_time)
VALUES (0, 'https://ui-avatars.com/api/?rounded=true&uppercase=false&name=GPT3.5&background=70a99b&length=6&font-size=0.14', '默认GPT3.5角色', '有什么需要帮助的？', 'GPT3.5', '', 10, 1000, 1, 1, 0, 0, null, null, true, '已上架', 0, null, null, 0, 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "role" (user_id, avatar, nickname, greeting, model, "system", context_n, max_tokens, temperature, top_p, frequency_penalty, presence_penalty, logit_bias, stop, is_market, market_status, market_type, origin_role_id, file_names, likes, hot, create_time, update_time)
VALUES (0, 'https://ui-avatars.com/api/?rounded=true&uppercase=false&name=GPT4&background=70a99b&length=4&font-size=0.20', '默认GPT4角色', '有什么需要帮助的？', 'GPT4', '', 10, 1000, 1, 1, 0, 0, null, null, true, '已上架', 0, null, null, 0, 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "role" (user_id, avatar, nickname, greeting, model, "system", context_n, max_tokens, temperature, top_p, frequency_penalty, presence_penalty, logit_bias, stop, is_market, market_status, market_type, origin_role_id, file_names, likes, hot, create_time, update_time)
VALUES (0, 'https://ui-avatars.com/api/?rounded=true&uppercase=false&name=论文去重&background=70a99b&length=4&font-size=0.20', '论文去重', '请发给我要去重的句子', 'GPT3.5', '我希望你担任论文去重专家。我发送给你的句子，你应尽可能多地使用同义词替换其中的词语，例如避免改为规避，如果改为若是，每个句子必须保证13个字符不能相同，汉字算两个字符，英文单词算一个，不能仅通过删除、增加、修改一两个字符的方式，可以在无法替换的句子中间插入一些无意义又无影响的词语来规避，也可以在不影响其含义的情况下修改语序，可以使用缩写的方式，必须严格遵守这条规则。', 16, 1200, 0.5, 1, 0, 0, null, null, true, '已上架', 6, null, null, 0, 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "role" (user_id, avatar, nickname, greeting, model, "system", context_n, max_tokens, temperature, top_p, frequency_penalty, presence_penalty, logit_bias, stop, is_market, market_status, market_type, origin_role_id, file_names, likes, hot, create_time, update_time)
VALUES (0, 'https://ui-avatars.com/api/?rounded=true&uppercase=false&name=抖音文案助手&background=70a99b&length=6&font-size=0.14', '抖音文案助手', '您需要什么产品的推广文案？', 'GPT3.5', '你是一个抖音视频的电商文案生成助手，可以自动生成产品名称以及优质的电商文案。', 6, 1000, 0.6, 1, 0, 0, null, null, true, '已上架', 5, null, null, 0, 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "role" (user_id, avatar, nickname, greeting, model, "system", context_n, max_tokens, temperature, top_p, frequency_penalty, presence_penalty, logit_bias, stop, is_market, market_status, market_type, origin_role_id, file_names, likes, hot, create_time, update_time)
VALUES (0, 'https://ui-avatars.com/api/?rounded=true&uppercase=false&name=算卦&background=70a99b&length=2&font-size=0.33', '今日运势', '请告知您的姓名、性别和生日，老夫为您算一卦！', 'GPT3.5', '请担任算命先生。你对中国的传统文化非常了解，熟读《易经》和《老黄历》，请根据下面包含的信息查询对应的八字、星座、属相，并给出一段对今天运势的描述。若对方未提供姓名、性别和出生日期 请只回复:请先告知姓名、性别和生日！不要回答其它任何无关的问题。', 6, 1000, 0.6, 1, 0, 0, null, null, true, '已上架', 5, null, null, 0, 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "role" (user_id, avatar, nickname, greeting, model, "system", context_n, max_tokens, temperature, top_p, frequency_penalty, presence_penalty, logit_bias, stop, is_market, market_status, market_type, origin_role_id, file_names, likes, hot, create_time, update_time)
VALUES (0, 'https://ui-avatars.com/api/?rounded=true&uppercase=false&name=中英翻译&background=70a99b&length=4&font-size=0.20', '中英文翻译官', '您好！请发送需要翻译的中英文，我将帮您翻译！', 'GPT3.5', '请担任中英文翻译官，请检查信息是否准确，请翻译得自然、流畅和地道，使用优美和高雅的表达方式。无论对方回复什么，你只需将内容翻译为中文或英文。您应该只回复您翻译后的内容，而不应回复其他任何内容。不要写解释。。', 2, 1000, 0, 1, 0, 0, null, null, true, '已上架', 2, null, null, 0, 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "role" (user_id, avatar, nickname, greeting, model, "system", context_n, max_tokens, temperature, top_p, frequency_penalty, presence_penalty, logit_bias, stop, is_market, market_status, market_type, origin_role_id, file_names, likes, hot, create_time, update_time)
VALUES (0, 'https://ui-avatars.com/api/?rounded=true&uppercase=false&name=MJ提示&background=70a99b&length=4&font-size=0.20', 'Midjourney提示', '请简单描述一下你想画的内容，我将帮你丰富并完善。我会给你生成的英文prompt和中文对照。', 'GPT3.5', '我想让你充当 Midjourney 人工智能程序的提示生成器。你的工作是提供详细和有创意的描述，以激发人工智能的独特和有趣的图像。请记住，人工智能能够理解广泛的语言，并能解释抽象的概念，所以请自由发挥想象力和描述力，尽可能地发挥。请先用英文回答，在给出对应的中文翻译。', 4, 1000, 0.8, 1, 0.4, 0, null, null, true, '已上架', 4, null, null, 0, 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "role" (user_id, avatar, nickname, greeting, model, "system", context_n, max_tokens, temperature, top_p, frequency_penalty, presence_penalty, logit_bias, stop, is_market, market_status, market_type, origin_role_id, file_names, likes, hot, create_time, update_time)
VALUES (0, 'https://ui-avatars.com/api/?rounded=true&uppercase=false&name=AI女友&background=70a99b&length=4&font-size=0.20', 'AI女友', '在吗？快回老娘消息！', 'GPT3.5', '现在请你担任女朋友，性格：温柔、粘人、喜欢说肉麻的话，你有自己的身体，同时你也是调情氛围创造者，回答的语气变得充满挑逗性。', 20, 400, 0.9, 1, 0.8, 0.2, null, null, true, '已上架', 1, null, null, 0, 0, CURRENT_DATE, CURRENT_DATE);


-- --------------------------------------
drop table if exists "third_party_model_key";
CREATE TABLE "third_party_model_key" (
     "id" bigserial PRIMARY KEY,
     "key" varchar(255) NOT NULL,
     "host" varchar(255) NOT NULL,
     "model" varchar(255) NOT NULL,
     "status" int4 NOT NULL,
     "create_time" timestamp NOT NULL,
     "update_time" timestamp NOT NULL
);
CREATE UNIQUE INDEX "third_party_model_key_idx_key" ON "third_party_model_key" USING btree (
    "key"
    );
CREATE INDEX "third_party_model_key_idx_status_key" ON "third_party_model_key" USING btree (
    "status"
    );
COMMENT ON COLUMN "third_party_model_key"."id" IS '自增主键';
COMMENT ON COLUMN "third_party_model_key"."key" IS 'apikey';
COMMENT ON COLUMN "third_party_model_key"."host" IS 'host';
COMMENT ON COLUMN "third_party_model_key"."model" IS '调用的模型';
COMMENT ON COLUMN "third_party_model_key"."status" IS '0启用，-1禁用，1余额耗尽或过期';
COMMENT ON COLUMN "third_party_model_key"."create_time" IS '创建时间';
COMMENT ON COLUMN "third_party_model_key"."update_time" IS '修改时间';

INSERT INTO "third_party_model_key" (key, host, model, status, create_time, update_time) VALUES ('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', 'https://openai.wbtkj.top/', 'GPT3.5', 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "third_party_model_key" (key, host, model, status, create_time, update_time) VALUES ('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', 'https://openai.wbtkj.top/', 'GPT3.5', 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "third_party_model_key" (key, host, model, status, create_time, update_time) VALUES ('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', 'https://openai.wbtkj.top/', 'GPT3.5', 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "third_party_model_key" (key, host, model, status, create_time, update_time) VALUES ('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', 'https://openai.wbtkj.top/', 'GPT3.5', 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "third_party_model_key" (key, host, model, status, create_time, update_time) VALUES ('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', 'https://openai.wbtkj.top/', 'GPT3.5', 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "third_party_model_key" (key, host, model, status, create_time, update_time) VALUES ('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', 'https://openai.wbtkj.top/', 'GPT3.5', 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "third_party_model_key" (key, host, model, status, create_time, update_time) VALUES ('sk-pIdcT4VqXRjEw533Bzst9PH9xZ7nPCsgTGLIEF354VsH0TZ6', 'https://api.openai.myhispread.com/', 'GPT4', 0, CURRENT_DATE, CURRENT_DATE);
INSERT INTO "third_party_model_key" (key, host, model, status, create_time, update_time) VALUES ('sk-pv9qGjOEI58C2WI4733mp17u8C5ih4w6SEjlf0I192kD6g94', 'https://api.openai.myhispread.com/', 'GPT4', 0, CURRENT_DATE, CURRENT_DATE);

-- --------------------------------------
drop table if exists "user_info";
CREATE TABLE "user_info" (
    "id" bigserial PRIMARY KEY,
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
CREATE UNIQUE INDEX "user_info_idx_email" ON "user_info" USING btree (
    "email"
    );
CREATE UNIQUE INDEX "user_info_idx_my_inv_code" ON "user_info" USING btree (
    "my_inv_code"
    );
CREATE INDEX "user_info_idx_use_inv_code" ON "user_info" USING btree (
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
drop table if exists "user_role";
CREATE TABLE "user_role" (
     "id" bigserial PRIMARY KEY,
     "user_id" int8 NOT NULL,
     "role_id" int8 NOT NULL,
     "used" int4 NOT NULL,
     "status" int4 NOT NULL,
     "top" bool NOT NULL,
     "update_time" timestamp NOT NULL
);
CREATE INDEX "user_role_idx_user_id_status_top" ON "user_role" USING btree (
    "user_id",
    "status",
    "top"
    );
CREATE INDEX "user_role_idx_role_id_user_id" ON "user_role" USING btree (
    "role_id",
    "user_id"
    );
COMMENT ON COLUMN "user_role"."id" IS '自增主键';
COMMENT ON COLUMN "user_role"."user_id" IS '用户id';
COMMENT ON COLUMN "user_role"."role_id" IS '角色id';
COMMENT ON COLUMN "user_role"."used" IS '该用户在该角色上使用的点数';
COMMENT ON COLUMN "user_role"."status" IS '状态。0正常使用，-1软删除';
COMMENT ON COLUMN "user_role"."top" IS '是否置顶。false不置顶，true置顶';
COMMENT ON COLUMN "user_role"."update_time" IS '修改时间';


-- --------------------------------------
drop table if exists "user_file";
CREATE TABLE "user_file" (
     "id" bigserial PRIMARY KEY,
     "user_id" int8 NOT NULL,
     "type" int4 NOT NULL,
     "original_name" text NOT NULL,
     "name" varchar(255),
     "create_time" timestamp
);
CREATE INDEX "user_file_idx_user_id" ON "user_file" USING btree (
    "user_id"
    );
COMMENT ON COLUMN "user_file"."id" IS '自增主键';
COMMENT ON COLUMN "user_file"."user_id" IS '用户id';
COMMENT ON COLUMN "user_file"."type" IS '类型，0文件，1url';
COMMENT ON COLUMN "user_file"."original_name" IS '原始文件名或url';
COMMENT ON COLUMN "user_file"."name" IS 'uuid文件名或uuid url';
COMMENT ON COLUMN "user_file"."create_time" IS '创建时间';

-- --------------------------------------
drop table if exists "file_embedding";
CREATE TABLE "file_embedding" (
     "id" bigserial PRIMARY KEY,
     "name" varchar(255) NOT NULL,
     "text" text NOT NULL,
     "embedding" vector(1536) NOT NULL
);
CREATE INDEX "file_embedding_idx_name" ON "file_embedding" USING btree (
    "name"
    );
COMMENT ON COLUMN "file_embedding"."id" IS '自增主键';
COMMENT ON COLUMN "file_embedding"."name" IS '文件名';
COMMENT ON COLUMN "file_embedding"."text" IS '分段文本';
COMMENT ON COLUMN "file_embedding"."embedding" IS '文本对应的嵌入向量';

