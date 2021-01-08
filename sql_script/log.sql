CREATE TABLE `log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志';