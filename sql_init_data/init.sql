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


