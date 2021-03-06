CREATE TABLE `perm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `perm_name` varchar(50) DEFAULT NULL COMMENT '权限名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_perm_name` (`perm_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';