CREATE TABLE `menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID(根菜单为0)',
  `type` int(2) DEFAULT NULL COMMENT '菜单类型(0:目录 1:菜单 2:按钮)',
  `menu_name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(50) DEFAULT NULL COMMENT '菜单URL',
  `perm` varchar(50) DEFAULT NULL COMMENT '授权',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `menu_name_idx` (`menu_name`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8 COMMENT='菜单表';