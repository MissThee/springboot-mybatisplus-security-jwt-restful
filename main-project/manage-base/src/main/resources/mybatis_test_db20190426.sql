/* 仅包含基础数据表及简单测试信息
 Navicat Premium Data Transfer

 Source Server         : local-user-1234
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : mybatis_test_db

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 26/04/2019 14:31:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for compute
-- ----------------------------
DROP TABLE IF EXISTS `compute`;
CREATE TABLE `compute`  (
  `id` bigint(20) NULL DEFAULT NULL,
  `column1` bigint(20) NULL DEFAULT NULL,
  `column2` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of compute
-- ----------------------------
INSERT INTO `compute` VALUES (4, 5, 6);
INSERT INTO `compute` VALUES (7, 8, 9);
INSERT INTO `compute` VALUES (1, 3, 4);
INSERT INTO `compute` VALUES (33, 4, 4);
INSERT INTO `compute` VALUES (1, 5, 5);
INSERT INTO `compute` VALUES (1, 6, 6);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限值（唯一）',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型group,page,button',
  `is_enable` bit(1) NULL DEFAULT b'1' COMMENT '可用',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, NULL, '权限1', 'permission1', NULL, b'1', b'0');
INSERT INTO `permission` VALUES (2, NULL, '权限2', 'permission2', NULL, b'1', b'0');
INSERT INTO `permission` VALUES (3, NULL, '权限3', 'permission3', NULL, b'1', b'0');
INSERT INTO `permission` VALUES (6, NULL, NULL, NULL, NULL, b'1', b'0');
INSERT INTO `permission` VALUES (7, NULL, '信访室', 'LetterRoom', NULL, b'1', b'0');
INSERT INTO `permission` VALUES (8, NULL, '案管室', 'CaseRoom', NULL, b'1', b'0');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色值',
  `is_enable` bit(1) NULL DEFAULT b'1' COMMENT '可用',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '角色1', 'role1', b'1', b'0');
INSERT INTO `role` VALUES (2, '角色2', 'role2', b'1', b'0');
INSERT INTO `role` VALUES (3, '角色3', 'role3', b'1', b'0');
INSERT INTO `role` VALUES (4, NULL, 'role', b'1', b'0');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `role_id` bigint(20) NULL DEFAULT NULL,
  `permission_id` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, 1);
INSERT INTO `role_permission` VALUES (2, 1);
INSERT INTO `role_permission` VALUES (3, 2);
INSERT INTO `role_permission` VALUES (3, 3);
INSERT INTO `role_permission` VALUES (4, 1);
INSERT INTO `role_permission` VALUES (5, 2);

-- ----------------------------
-- Table structure for unit
-- ----------------------------
DROP TABLE IF EXISTS `unit`;
CREATE TABLE `unit`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `is_enable` bit(1) NULL DEFAULT b'1' COMMENT '可用',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '已删除',
  `is_admin` bit(1) NULL DEFAULT b'0' COMMENT '管理员账号',
  `is_basic` bit(1) NULL DEFAULT b'0' COMMENT '基础账号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (10, NULL, '*23AE809DDACAF96AF0FD78ED04B6A265E05AA257', '1', b'1', b'0', b'0', b'0');
INSERT INTO `user` VALUES (11, NULL, '', '1', b'1', b'0', b'0', b'0');
INSERT INTO `user` VALUES (19, 'admin', 'admin', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0', b'0', b'0');
INSERT INTO `user` VALUES (20, 'test1', 'test1', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0', b'0', b'0');
INSERT INTO `user` VALUES (21, 're1', 're1', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0', b'0', b'0');
INSERT INTO `user` VALUES (22, 're2', 're2', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0', b'0', b'0');
INSERT INTO `user` VALUES (23, 're3', 're3', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0', b'0', b'0');
INSERT INTO `user` VALUES (25, NULL, 'string', '$2a$10$LwxTJ9XIF/iHgJZCX4MJvOc/oHbjd2AcqukDFBK60bqoUNNibBkbW', b'1', b'0', b'0', b'0');
INSERT INTO `user` VALUES (27, NULL, '123', '$2a$10$PA1JF.th3Cpe3Xm3RKSFheEzB2ZNail0T5nlU3QMuNzqF12lw5SjG', b'1', b'0', b'0', b'0');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `user_id` bigint(20) NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (19, 1);
INSERT INTO `user_role` VALUES (19, 2);
INSERT INTO `user_role` VALUES (19, 3);
INSERT INTO `user_role` VALUES (191, 4);

-- ----------------------------
-- Table structure for user_unit
-- ----------------------------
DROP TABLE IF EXISTS `user_unit`;
CREATE TABLE `user_unit`  (
  `user_id` bigint(20) NULL DEFAULT NULL,
  `unit_id` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_unit
-- ----------------------------
INSERT INTO `user_unit` VALUES (19, 1);

SET FOREIGN_KEY_CHECKS = 1;
