/*
 Navicat Premium Data Transfer

 Source Server         : local-user-1234
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : mybatis_test_db

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 29/03/2019 16:25:02
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_enable` bit(1) NULL DEFAULT b'1',
  `is_delete` bit(1) NULL DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '权限1', 'permission1', b'1', b'0');
INSERT INTO `permission` VALUES (2, '权限2', 'permission2', b'1', b'0');
INSERT INTO `permission` VALUES (3, '权限3', 'permission3', b'1', b'0');

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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL,
  `permission_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, 1, 1);
INSERT INTO `role_permission` VALUES (2, 2, 1);
INSERT INTO `role_permission` VALUES (3, 3, 2);
INSERT INTO `role_permission` VALUES (4, 4, 1);
INSERT INTO `role_permission` VALUES (5, 5, 2);
INSERT INTO `role_permission` VALUES (6, 3, 3);

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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (19, 'admin', 'admin', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0');
INSERT INTO `user` VALUES (20, 'test1', 'test1', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0');
INSERT INTO `user` VALUES (21, 're1', 're1', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0');
INSERT INTO `user` VALUES (22, 're2', 're2', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0');
INSERT INTO `user` VALUES (23, 're3', 're3', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0');
INSERT INTO `user` VALUES (25, NULL, 'string', '$2a$10$LwxTJ9XIF/iHgJZCX4MJvOc/oHbjd2AcqukDFBK60bqoUNNibBkbW', b'1', b'0');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 19, 1);
INSERT INTO `user_role` VALUES (2, 19, 2);
INSERT INTO `user_role` VALUES (3, 19, 3);
INSERT INTO `user_role` VALUES (4, 191, 4);

SET FOREIGN_KEY_CHECKS = 1;
