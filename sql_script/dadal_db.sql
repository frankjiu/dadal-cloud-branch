DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
 `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
 `description` varchar(50) DEFAULT NULL COMMENT '操作描述',
 `url` varchar(500) DEFAULT NULL COMMENT '请求URL',
 `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
 `params` mediumtext COMMENT '请求参数',
 `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
 `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
 `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `version` varchar(10) DEFAULT NULL COMMENT '版本信息',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志表';

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(64) unsigned NOT NULL COMMENT '主键',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
  `country` varchar(255) DEFAULT NULL COMMENT '国家',
  `province` varchar(255) DEFAULT NULL COMMENT '省份',
  `city` varchar(255) DEFAULT NULL COMMENT '城市',
  `sex` tinyint(4) DEFAULT '1' COMMENT '性别 0:女, 1:男',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `headimgurl` varchar(512) DEFAULT NULL COMMENT '头像',
  `birthday` varchar(512) DEFAULT NULL COMMENT '生日',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：0-停用，1-启用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户信息表';

DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
 `id` bigint(64) NOT NULL COMMENT '主键',
 `user_id` bigint(64) DEFAULT NULL COMMENT '用户ID',
 `total_cost` decimal(12,2) DEFAULT NULL COMMENT '订单费用总计',
 `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
 `delete_time` bigint(20) DEFAULT NULL COMMENT '删除时间',
 `payment_mode` varchar(255) DEFAULT NULL COMMENT '支付渠道：0-微信支付; 1-支付宝支付',
 `alipay_id` varchar(255) DEFAULT NULL COMMENT '支付宝用户标识',
 `wx_openID` varchar(128) DEFAULT NULL COMMENT '微信用户标识',
 `wx_orderID` varchar(32) DEFAULT NULL COMMENT '商户内部微信订单号',
 `status` int(11) DEFAULT NULL COMMENT '订单状态:0-取消订单, 1-待支付订单, 2-已支付订单',
 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单表';

DROP TABLE IF EXISTS `ali_account`;
CREATE TABLE `ali_account` (
 `platform_id` bigint(20) NOT NULL COMMENT '主键',
 `app_id` varchar(255) DEFAULT NULL COMMENT '应用id',
 `auth_token` varchar(255) DEFAULT NULL COMMENT '授权token',
 `merchant_name` varchar(255) DEFAULT NULL COMMENT '商户名称',
 `ali_account` varchar(255) DEFAULT NULL COMMENT '商户支付宝账号',
 PRIMARY KEY (`platform_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='支付宝账户表';

DROP TABLE IF EXISTS `wx_account`;
CREATE TABLE `wx_account` (
  `platform_id` bigint(20) NOT NULL COMMENT '主键',
  `mch_id` varchar(255) DEFAULT NULL COMMENT '商户id',
  `merchant_name` varchar(255) DEFAULT NULL COMMENT '商户名称',
  `business_name` varchar(255) DEFAULT NULL COMMENT '商户业务名',
  `wx_account` varchar(255) DEFAULT NULL COMMENT '商户微信账号',
PRIMARY KEY (`platform_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='微信账户表';