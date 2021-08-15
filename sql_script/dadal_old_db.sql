DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo`  (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
   `CARD_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡名',
   `CARD_NUMBER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
   `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 300001 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `pass_word` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(64) DEFAULT NULL COMMENT '盐',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 0:禁用 1:正常',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) DEFAULT NULL COMMENT '角色名',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name_idx` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单表';

DROP TABLE IF EXISTS `menu`;
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
    UNIQUE KEY `uk_menu_name` (`menu_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

DROP TABLE IF EXISTS `perm`;
CREATE TABLE `perm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `perm_name` varchar(50) DEFAULT NULL COMMENT '权限名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_perm_name` (`perm_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

DROP TABLE IF EXISTS `log`;
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

DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `CARD_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡名',
  `CARD_NUMBER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 300001 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

#user
INSERT INTO `dadal`.`user`(`id`, `user_name`, `pass_word`, `salt`, `email`, `mobile`, `status`, `update_time`) VALUES (1, 'admin', 'f27d93e9121306fcbd820c16dc768f569fa475b1ca3e9046007248661404e78c', '2ouDSNU78YGYGwleaKKj', '2309094456@qq.com', '19985102265', 1, 1610086064);

#user_role
INSERT INTO `dadal`.`user_role`(`id`, `user_id`, `role_id`) VALUES (1, 1, 0);

#role
INSERT INTO `dadal`.`role`(`id`, `role_name`, `remark`, `update_time`) VALUES (0, 'SUPER', '超级管理员', 1610086713);
INSERT INTO `dadal`.`role`(`id`, `role_name`, `remark`, `update_time`) VALUES (1, 'VIP', 'VIP用户', 1610086961);
INSERT INTO `dadal`.`role`(`id`, `role_name`, `remark`, `update_time`) VALUES (2, 'FREE', '普通用户', 1610086969);

#role_menu
INSERT INTO `dadal`.`role_menu`(`id`, `role_id`, `menu_id`) VALUES (10010, 1, 1);
INSERT INTO `dadal`.`role_menu`(`id`, `role_id`, `menu_id`) VALUES (10011, 1, 2);
INSERT INTO `dadal`.`role_menu`(`id`, `role_id`, `menu_id`) VALUES (10012, 1, 3);

#menu
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (0, 0, 0, 'root', '/', '', 0, 1610016700);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (1, 0, 0, '用户管理', '/user', 'user:management', 1, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (2, 0, 0, '角色管理', '/role', 'role:management', 2, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (3, 0, 0, '菜单管理', '/menu', 'menu:management', 3, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (4, 0, 0, '权限管理', '/perm', 'perm:management', 4, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (5, 0, 0, '授权管理', '/permission', 'permission:management', 5, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (6, 0, 0, '日志管理', '/log', 'log:management', 6, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (8, 1, 1, '查看用户', '/user', 'user:info', 101, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (9, 1, 1, '增改用户', '/user', 'user:save', 102, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (10, 1, 1, '删除用户', '/user', 'user:delete', 103, 1610077109);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (11, 2, 1, '查看角色', '/role', 'role:info', 201, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (12, 2, 1, '增改角色', '/role', 'role:save', 202, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (13, 2, 1, '删除角色', '/role', 'role:delete', 203, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (14, 3, 1, '查看菜单', '/menu', 'menu:info', 301, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (15, 3, 1, '增改菜单', '/menu', 'menu:save', 302, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (16, 3, 1, '删除菜单', '/menu', 'menu:delete', 303, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (17, 4, 1, '查看权限', '/perm', 'perm:info', 401, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (18, 4, 1, '增改权限', '/perm', 'perm:save', 402, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (19, 4, 1, '删除权限', '/perm', 'perm:delete', 403, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (20, 5, 1, '查看授权', '/permission', 'permission:info', 501, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (21, 5, 1, '执行授权', '/permission', 'permission:save', 502, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (22, 6, 1, '查看日志', '/log', 'log:info', 601, 1610076928);
INSERT INTO `dadal`.`menu`(`id`, `parent_id`, `type`, `menu_name`, `url`, `perm`, `order_num`, `update_time`) VALUES (23, 6, 1, '清理日志', '/log', 'log:delete', 602, 1610076928);

#perm
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (1, 'user:management', '用户管理', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (2, 'role:management', '角色管理', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (3, 'menu:management', '菜单管理', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (4, 'perm:management', '权限管理', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (5, 'permission:management', '授权管理', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (6, 'log:management', '日志管理', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (7, 'user:info', '查看用户', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (8, 'user:save', '增改用户', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (9, 'user:delete', '删除用户', 1610077109);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (10, 'role:info', '查看角色', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (11, 'role:save', '增改角色', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (12, 'role:delete', '删除角色', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (13, 'menu:info', '查看菜单', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (14, 'menu:save', '增改菜单', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (15, 'menu:delete', '删除菜单', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (16, 'perm:info', '查看权限', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (17, 'perm:save', '增改权限', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (18, 'perm:delete', '删除权限', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (19, 'permission:info', '查看授权', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (20, 'permission:save', '执行授权', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (21, 'log:info', '查看日志', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (22, 'log:delete', '清理日志', 1610076928);
INSERT INTO `dadal`.`perm`(`id`, `perm_name`, `remark`, `update_time`) VALUES (10001, 'user:user:test', '测试', 1610074093);

#demo
INSERT INTO `demo` VALUES (1, '中国银行', '9999555566657890', '2020-06-10 15:18:22');
INSERT INTO `demo` VALUES (2, '中国银行', '9999555566657891', '2020-06-10 15:18:22');
INSERT INTO `demo` VALUES (3, '中国银行', '9999555566657892', '2020-06-10 15:18:22');
INSERT INTO `demo` VALUES (4, '农业银行', '8888555566657896', '2020-06-10 15:18:22');
INSERT INTO `demo` VALUES (5, '工商银行', '3333555566657895', '2020-06-10 15:18:22');
INSERT INTO `demo` VALUES (6, '建设银行', '4444555566657898', '2020-06-10 15:18:22');
INSERT INTO `demo` VALUES (7, '交通银行', '7777555566657893', '2020-06-10 15:18:22');
INSERT INTO `demo` VALUES (8, '农业银行', '8888555566657897', '2020-06-10 15:18:22');
INSERT INTO `demo` VALUES (9, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (10, 'ABC', NULL, '2005-03-05 00:00:00');
INSERT INTO `demo` VALUES (11, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (12, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (13, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (14, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (15, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (16, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (17, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (18, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (19, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (20, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (21, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (22, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (23, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (24, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (25, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (26, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (27, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (28, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (29, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (30, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (31, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (32, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (33, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (34, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (35, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (36, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (37, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (38, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (39, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (40, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (41, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (42, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (43, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (44, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (45, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (46, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (47, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (48, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (49, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (50, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (51, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (52, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (53, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (54, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (55, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (56, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (57, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (58, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (59, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (60, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (61, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (62, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (63, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (64, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (65, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (66, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (67, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (68, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (69, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (70, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (71, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (72, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (73, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (74, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (75, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (76, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (77, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (78, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (79, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (80, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (81, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (82, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (83, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (84, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (85, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (86, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (87, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (88, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (89, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (90, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (91, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (92, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (93, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (94, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (95, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (96, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (97, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (98, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (99, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (100, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (101, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (102, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (103, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (104, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (105, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (106, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (107, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (108, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (109, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (110, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (111, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (112, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (113, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (114, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (115, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (116, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (117, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (118, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (119, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (120, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (121, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (122, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (123, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (124, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (125, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (126, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (127, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (128, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (129, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (130, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (131, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (132, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (133, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (134, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (135, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (136, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (137, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (138, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (139, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (140, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (141, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (142, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (143, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
INSERT INTO `demo` VALUES (144, '中国银行', '9999555566657890', '2020-07-16 15:18:22');
