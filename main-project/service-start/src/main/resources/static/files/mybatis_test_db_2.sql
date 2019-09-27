/*
 Navicat Premium Data Transfer

 Source Server         : localhost-root-1qazxsw2!
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : mybatis_test_db_2

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 27/09/2019 11:27:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for table_in_db2
-- ----------------------------
DROP TABLE IF EXISTS `table_in_db2`;
CREATE TABLE `table_in_db2`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `column1` int(11) NULL DEFAULT NULL,
  `column2` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of table_in_db2
-- ----------------------------
INSERT INTO `table_in_db2` VALUES (1, NULL, NULL);
INSERT INTO `table_in_db2` VALUES (2, 2, 2);

SET FOREIGN_KEY_CHECKS = 1;
