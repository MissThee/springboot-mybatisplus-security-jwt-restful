/*
 Navicat Premium Data Transfer

 Source Server         : localhost-root-1qazxsw2!
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : mybatis_test_db

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 10/10/2019 10:00:29
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
-- Table structure for dic_area_involved
-- ----------------------------
DROP TABLE IF EXISTS `dic_area_involved`;
CREATE TABLE `dic_area_involved`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `index_number` int(11) NULL DEFAULT NULL,
  `is_delete` bit(1) NULL DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '涉及领域' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dic_area_involved
-- ----------------------------
INSERT INTO `dic_area_involved` VALUES (5, '涉及领域1', 2, b'0');
INSERT INTO `dic_area_involved` VALUES (6, '涉及领域2', 2, b'0');
INSERT INTO `dic_area_involved` VALUES (7, '涉及领域3', 3, b'0');
INSERT INTO `dic_area_involved` VALUES (8, '涉及领域4', 4, b'0');
INSERT INTO `dic_area_involved` VALUES (9, '涉及领域5', 5, b'0');

-- ----------------------------
-- Table structure for dic_illegal_behavior
-- ----------------------------
DROP TABLE IF EXISTS `dic_illegal_behavior`;
CREATE TABLE `dic_illegal_behavior`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `index_number` int(11) NULL DEFAULT NULL,
  `is_delete` bit(1) NULL DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '违法行为' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dic_illegal_behavior
-- ----------------------------
INSERT INTO `dic_illegal_behavior` VALUES (5, '违法行为1', 1, b'0');
INSERT INTO `dic_illegal_behavior` VALUES (6, '违法行为2', 2, b'0');
INSERT INTO `dic_illegal_behavior` VALUES (8, '违法行为4', 4, b'0');
INSERT INTO `dic_illegal_behavior` VALUES (9, '违法行为5', 5, b'0');

-- ----------------------------
-- Table structure for dic_job_rank
-- ----------------------------
DROP TABLE IF EXISTS `dic_job_rank`;
CREATE TABLE `dic_job_rank`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `index_number` int(11) NULL DEFAULT NULL,
  `is_delete` bit(1) NULL DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '职级' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dic_job_rank
-- ----------------------------
INSERT INTO `dic_job_rank` VALUES (5, '职级管理1', 1, b'0');
INSERT INTO `dic_job_rank` VALUES (6, '职级管理2', 2, b'0');
INSERT INTO `dic_job_rank` VALUES (7, '职级管理3', 3, b'0');
INSERT INTO `dic_job_rank` VALUES (8, '职级管理4', 4, b'0');
INSERT INTO `dic_job_rank` VALUES (9, '职级管理5', 5, b'0');
INSERT INTO `dic_job_rank` VALUES (10, '职级管理6', 6, b'0');

-- ----------------------------
-- Table structure for dic_job_type
-- ----------------------------
DROP TABLE IF EXISTS `dic_job_type`;
CREATE TABLE `dic_job_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `index_number` int(11) NULL DEFAULT NULL,
  `is_delete` bit(1) NULL DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '干部类型表（是否村干部）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dic_job_type
-- ----------------------------
INSERT INTO `dic_job_type` VALUES (10, '干部类型1', 6, b'0');
INSERT INTO `dic_job_type` VALUES (11, '干部类型2', 5, b'0');
INSERT INTO `dic_job_type` VALUES (12, '干部类型3', 3, b'0');
INSERT INTO `dic_job_type` VALUES (13, '干部类型4', 4, b'0');
INSERT INTO `dic_job_type` VALUES (14, '干部类型5', 5, b'0');
INSERT INTO `dic_job_type` VALUES (15, '干部类型6', 6, b'0');

-- ----------------------------
-- Table structure for dic_result_type
-- ----------------------------
DROP TABLE IF EXISTS `dic_result_type`;
CREATE TABLE `dic_result_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `index_number` int(11) NULL DEFAULT NULL,
  `is_delete` bit(1) NULL DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '信访信访室处理结果' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dic_result_type
-- ----------------------------
INSERT INTO `dic_result_type` VALUES (1, '分类1', 1, b'0');
INSERT INTO `dic_result_type` VALUES (2, '分类2', 2, b'0');
INSERT INTO `dic_result_type` VALUES (3, '案管', 3, b'0');
INSERT INTO `dic_result_type` VALUES (4, '驳回', 4, b'0');
INSERT INTO `dic_result_type` VALUES (6, '未分类', 0, b'0');

-- ----------------------------
-- Table structure for dic_source
-- ----------------------------
DROP TABLE IF EXISTS `dic_source`;
CREATE TABLE `dic_source`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '线索来源id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '线索来源名称',
  `index_number` int(11) NULL DEFAULT NULL COMMENT '排序',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '线索来源数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dic_source
-- ----------------------------
INSERT INTO `dic_source` VALUES (1, '线索来源1', 1, b'0');
INSERT INTO `dic_source` VALUES (2, '线索来源2', 2, b'0');
INSERT INTO `dic_source` VALUES (3, '线索来源3', 3, b'0');
INSERT INTO `dic_source` VALUES (4, '线索来源4', 4, b'0');
INSERT INTO `dic_source` VALUES (5, '线索来源5', 5, b'0');
INSERT INTO `dic_source` VALUES (6, '线索来源6', 6, b'0');
INSERT INTO `dic_source` VALUES (7, '线索来源测试1', 100, b'0');
INSERT INTO `dic_source` VALUES (8, '修改线索来源测试1', 99, b'1');
INSERT INTO `dic_source` VALUES (9, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (10, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (11, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (12, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (13, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (14, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (15, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (16, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (17, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (18, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (19, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (20, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (21, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (22, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (23, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (24, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (25, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (26, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (27, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (28, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (29, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (30, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (31, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (32, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (33, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (34, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (35, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (36, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (37, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (38, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (39, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (40, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (41, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (42, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (43, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (44, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (45, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (46, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (47, '测试数据', 100, b'0');
INSERT INTO `dic_source` VALUES (48, '测试数据', 100, b'0');

-- ----------------------------
-- Table structure for let_clue
-- ----------------------------
DROP TABLE IF EXISTS `let_clue`;
CREATE TABLE `let_clue`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '线索编码',
  `reception_time` date NULL DEFAULT NULL COMMENT '受理时间（用户填写表单时选择的时间,一般为当前时间，与createdate值近似，实际可能出现补记录的情况，用户会选过去的时间）',
  `content` varchar(20000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '反映的主要问题（文本输入）',
  `create_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间(插入时动记录服务器当前时间)',
  `is_processed` bit(1) NULL DEFAULT b'0' COMMENT '归档标记，已处理标记（无用）',
  `result_type_id` int(11) NULL DEFAULT 0 COMMENT '分类结果',
  `state_id` int(11) NULL DEFAULT NULL COMMENT '信访流程进度id（无用）',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '已删状态',
  `is_event` bit(1) NULL DEFAULT NULL COMMENT '是否问题或事件（0-有被反映人,1-无被反映人）',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '信访表单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of let_clue
-- ----------------------------
INSERT INTO `let_clue` VALUES ('20190001', '2019-08-02', '45123123', '2019-08-13 09:40:04', b'0', 0, NULL, b'1', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190002', NULL, '147325643', '2019-08-13 09:43:51', b'0', 1, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190003', '2019-08-12', '12312313131', '2019-08-13 09:51:02', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190004', '2019-08-11', '1', '2019-08-13 14:38:44', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190005', '2019-08-13', '1', '2019-08-13 14:39:42', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190006', '2019-08-01', '456456', '2019-08-13 15:13:58', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190007', '2019-01-01', 'test1', '2019-08-14 16:58:22', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190008', '2019-08-01', 'test', '2019-08-22 16:19:50', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190009', '2019-08-22', 'test', '2019-08-22 16:20:21', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190010', '2019-08-07', 'test', '2019-08-22 16:21:18', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190011', '2019-08-07', 'test', '2019-08-22 16:21:18', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190012', '2019-08-07', 'test', '2019-08-22 16:21:18', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190013', '2019-08-01', '1', '2019-08-22 16:21:55', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190014', '2019-08-21', '123', '2019-08-22 16:24:47', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190015', '2019-01-01', 'test1', '2019-08-26 16:19:44', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190016', '2019-01-01', 'test1', '2019-08-26 16:19:51', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190017', '2019-01-01', 'test1', '2019-08-26 16:20:19', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190018', '2019-01-01', 'test1', '2019-08-26 16:20:25', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190019', '2019-01-01', 'test1', '2019-08-26 16:30:37', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190020', '2019-01-01', 'test2', '2019-08-26 16:32:20', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190021', '2019-01-01', 'test1', '2019-08-27 17:40:25', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190022', '2019-01-01', 'test1', '2019-08-27 21:16:02', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190023', '2019-01-01', 'test1', '2019-08-27 21:54:30', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190024', '2019-01-01', 'test1', '2019-08-27 22:00:33', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190025', '2019-01-01', 'test1', '2019-08-28 16:36:06', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190026', '2019-01-01', 'test1', '2019-08-28 23:16:38', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190030', '2019-01-01', 'test2', '2019-08-28 23:18:21', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190033', '2019-01-02', 'test1', '2019-08-28 23:40:55', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190034', '2019-01-01', 'test2', '2019-08-28 23:35:13', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190035', '2019-01-01', 'test1', '2019-08-29 18:46:40', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190036', '2019-01-01', 'test1', '2019-08-29 18:47:19', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190037', '2019-01-01', 'test1', '2019-08-29 18:48:31', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190038', '2019-01-01', 'test1', '2019-08-30 11:31:52', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190039', '2019-01-01', 'test1', '2019-08-30 11:37:06', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190040', '2019-01-01', 'test1', '2019-08-30 11:37:27', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190041', '2019-09-11', '123', '2019-09-02 09:24:47', b'0', 0, NULL, b'1', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190042', '2019-09-03', 'aaa', '2019-09-02 09:31:03', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190043', '2019-09-04', '123', '2019-09-02 09:32:21', b'0', 0, NULL, b'1', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190044', '2019-09-04', 'zzz', '2019-09-02 09:32:44', b'0', 2, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190045', '2019-09-03', '锤子丢了', '2019-09-04 11:48:25', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190046', '2019-09-03', '1', '2019-09-04 14:49:42', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190047', '2019-09-02', '1', '2019-09-04 14:51:07', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190048', '2019-09-06', '写着写着代码睡着了', '2019-09-06 14:28:07', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190049', '2019-09-09', '太贵了买不起', '2019-09-09 09:32:13', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190050', '2019-09-06', '写着写着代码睡着了', '2019-09-09 10:39:30', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190051', '2019-09-09', '太贵了买不起', '2019-09-09 15:09:06', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190052', '2019-09-09', 'test', '2019-09-09 15:45:39', b'0', 3, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190053', '2019-09-03', 'test', '2019-09-09 17:32:31', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190054', '2019-09-02', '3', '2019-09-09 17:33:14', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190055', '2019-09-02', '1', '2019-09-09 17:36:07', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190056', '2019-09-12', '33', '2019-09-09 17:36:21', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190057', '2019-09-10', '4', '2019-09-09 17:37:18', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190058', '2019-09-03', '24', '2019-09-09 17:39:56', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190059', '2019-09-09', '1', '2019-09-09 17:41:03', b'0', 3, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190060', '2019-01-01', 'test1', '2019-09-25 11:34:31', b'0', 0, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190061', '2019-01-01', 'test1', '2019-09-25 11:53:08', b'0', 3, NULL, b'0', NULL, NULL);
INSERT INTO `let_clue` VALUES ('20190062', '2019-01-01', 'test1', '2019-09-25 11:54:20', b'0', 3, NULL, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for let_clue_area_involved
-- ----------------------------
DROP TABLE IF EXISTS `let_clue_area_involved`;
CREATE TABLE `let_clue_area_involved`  (
  `clue_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `illegal_behavior_id` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '信访表单和设计领域关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of let_clue_area_involved
-- ----------------------------
INSERT INTO `let_clue_area_involved` VALUES ('20190002', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190002', 6);
INSERT INTO `let_clue_area_involved` VALUES ('20190002', 7);
INSERT INTO `let_clue_area_involved` VALUES ('20190002', 8);
INSERT INTO `let_clue_area_involved` VALUES ('20190003', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190003', 6);
INSERT INTO `let_clue_area_involved` VALUES ('20190003', 7);
INSERT INTO `let_clue_area_involved` VALUES ('20190003', 8);
INSERT INTO `let_clue_area_involved` VALUES ('20190003', 9);
INSERT INTO `let_clue_area_involved` VALUES ('20190001', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190001', 6);
INSERT INTO `let_clue_area_involved` VALUES ('20190004', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190005', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190006', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190006', 6);
INSERT INTO `let_clue_area_involved` VALUES ('20190008', 6);
INSERT INTO `let_clue_area_involved` VALUES ('20190010', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190013', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190015', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190015', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190016', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190016', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190017', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190017', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190018', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190018', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190019', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190019', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190020', 12);
INSERT INTO `let_clue_area_involved` VALUES ('20190020', 13);
INSERT INTO `let_clue_area_involved` VALUES ('20190021', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190021', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190022', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190022', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190023', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190023', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190024', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190024', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190025', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190025', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190026', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190026', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190027', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190027', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190031', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190031', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190033', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190033', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190035', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190035', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190036', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190036', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190037', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190037', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190038', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190038', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190039', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190039', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190040', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190040', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190041', 7);
INSERT INTO `let_clue_area_involved` VALUES ('20190043', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190044', 7);
INSERT INTO `let_clue_area_involved` VALUES ('20190046', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190047', 6);
INSERT INTO `let_clue_area_involved` VALUES ('20190048', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190048', 6);
INSERT INTO `let_clue_area_involved` VALUES ('20190048', 7);
INSERT INTO `let_clue_area_involved` VALUES ('20190050', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190050', 6);
INSERT INTO `let_clue_area_involved` VALUES ('20190050', 7);
INSERT INTO `let_clue_area_involved` VALUES ('20190007', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190007', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190045', 6);
INSERT INTO `let_clue_area_involved` VALUES ('20190009', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190014', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190049', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190049', 6);
INSERT INTO `let_clue_area_involved` VALUES ('20190053', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190054', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190055', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190057', 7);
INSERT INTO `let_clue_area_involved` VALUES ('20190058', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190042', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190052', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190052', 8);
INSERT INTO `let_clue_area_involved` VALUES ('20190052', 7);
INSERT INTO `let_clue_area_involved` VALUES ('20190052', 6);
INSERT INTO `let_clue_area_involved` VALUES ('20190052', 9);
INSERT INTO `let_clue_area_involved` VALUES ('20190060', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190060', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190062', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190062', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190061', 2);
INSERT INTO `let_clue_area_involved` VALUES ('20190061', 3);
INSERT INTO `let_clue_area_involved` VALUES ('20190059', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190051', 5);
INSERT INTO `let_clue_area_involved` VALUES ('20190051', 6);

-- ----------------------------
-- Table structure for let_clue_defendant
-- ----------------------------
DROP TABLE IF EXISTS `let_clue_defendant`;
CREATE TABLE `let_clue_defendant`  (
  `clue_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `defendant_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '被反映人' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of let_clue_defendant
-- ----------------------------
INSERT INTO `let_clue_defendant` VALUES ('20190001', '2019000101');
INSERT INTO `let_clue_defendant` VALUES ('20190001', '2019000102');
INSERT INTO `let_clue_defendant` VALUES ('20190001', '2019000101');
INSERT INTO `let_clue_defendant` VALUES ('20190001', '2019000102');
INSERT INTO `let_clue_defendant` VALUES ('20190001', '2019000101');
INSERT INTO `let_clue_defendant` VALUES ('20190001', '2019000102');
INSERT INTO `let_clue_defendant` VALUES ('20190002', '2019000201');
INSERT INTO `let_clue_defendant` VALUES ('20190002', '2019000202');
INSERT INTO `let_clue_defendant` VALUES ('20190004', '2019000401');
INSERT INTO `let_clue_defendant` VALUES ('20190013', '2019001301');
INSERT INTO `let_clue_defendant` VALUES ('20190015', '2019001501');
INSERT INTO `let_clue_defendant` VALUES ('20190016', '2019001601');
INSERT INTO `let_clue_defendant` VALUES ('20190017', '2019001701');
INSERT INTO `let_clue_defendant` VALUES ('20190018', '2019001801');
INSERT INTO `let_clue_defendant` VALUES ('20190019', '2019001901');
INSERT INTO `let_clue_defendant` VALUES ('20190020', '2019002001');
INSERT INTO `let_clue_defendant` VALUES ('20190021', '2019002101');
INSERT INTO `let_clue_defendant` VALUES ('20190022', '2019002201');
INSERT INTO `let_clue_defendant` VALUES ('20190023', '2019002301');
INSERT INTO `let_clue_defendant` VALUES ('20190024', '2019002401');
INSERT INTO `let_clue_defendant` VALUES ('20190025', '2019002501');
INSERT INTO `let_clue_defendant` VALUES ('20190026', '2019002601');
INSERT INTO `let_clue_defendant` VALUES ('20190027', '2019002701');
INSERT INTO `let_clue_defendant` VALUES ('20190028', '2019002801');
INSERT INTO `let_clue_defendant` VALUES ('20190029', '2019002901');
INSERT INTO `let_clue_defendant` VALUES ('20190031', '2019003101');
INSERT INTO `let_clue_defendant` VALUES ('20190032', '2019003201');
INSERT INTO `let_clue_defendant` VALUES ('20190033', '2019003301');
INSERT INTO `let_clue_defendant` VALUES ('20190035', '2019003501');
INSERT INTO `let_clue_defendant` VALUES ('20190036', '2019003601');
INSERT INTO `let_clue_defendant` VALUES ('20190037', '2019003701');
INSERT INTO `let_clue_defendant` VALUES ('20190038', '2019003801');
INSERT INTO `let_clue_defendant` VALUES ('20190039', '2019003901');
INSERT INTO `let_clue_defendant` VALUES ('20190040', '2019004001');
INSERT INTO `let_clue_defendant` VALUES ('20190046', '2019004601');
INSERT INTO `let_clue_defendant` VALUES ('20190047', '2019004701');
INSERT INTO `let_clue_defendant` VALUES ('20190048', '2019004801');
INSERT INTO `let_clue_defendant` VALUES ('20190050', '2019005001');
INSERT INTO `let_clue_defendant` VALUES ('20190007', '2019000701');
INSERT INTO `let_clue_defendant` VALUES ('20190045', '2019004501');
INSERT INTO `let_clue_defendant` VALUES ('20190009', '2019000901');
INSERT INTO `let_clue_defendant` VALUES ('20190014', '2019001401');
INSERT INTO `let_clue_defendant` VALUES ('20190049', '2019004901');
INSERT INTO `let_clue_defendant` VALUES ('20190049', '2019004902');
INSERT INTO `let_clue_defendant` VALUES ('20190058', '2019005801');
INSERT INTO `let_clue_defendant` VALUES ('20190042', '2019004201');
INSERT INTO `let_clue_defendant` VALUES ('20190052', '2019005201');
INSERT INTO `let_clue_defendant` VALUES ('20190052', '2019005202');
INSERT INTO `let_clue_defendant` VALUES ('20190060', '2019006001');
INSERT INTO `let_clue_defendant` VALUES ('20190062', '2019006201');
INSERT INTO `let_clue_defendant` VALUES ('20190061', '2019006101');
INSERT INTO `let_clue_defendant` VALUES ('20190059', '2019005901');
INSERT INTO `let_clue_defendant` VALUES ('20190051', '2019005101');

-- ----------------------------
-- Table structure for let_clue_illegal_behavior
-- ----------------------------
DROP TABLE IF EXISTS `let_clue_illegal_behavior`;
CREATE TABLE `let_clue_illegal_behavior`  (
  `clue_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `illegal_behavior_id` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '信访表单和主要违法行为关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of let_clue_illegal_behavior
-- ----------------------------
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190002', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190002', 6);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190002', 8);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190003', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190003', 6);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190003', 8);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190003', 9);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190001', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190004', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190005', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190006', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190006', 6);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190008', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190010', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190013', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190015', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190015', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190016', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190016', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190017', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190017', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190018', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190018', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190019', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190019', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190020', 11);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190020', 12);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190021', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190021', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190022', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190022', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190023', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190023', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190024', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190024', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190025', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190025', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190026', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190026', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190027', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190027', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190031', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190031', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190033', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190033', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190035', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190035', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190036', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190036', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190037', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190037', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190038', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190038', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190039', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190039', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190040', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190040', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190041', 8);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190043', 8);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190044', 6);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190046', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190047', 6);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190048', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190048', 6);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190050', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190050', 6);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190007', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190007', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190045', 8);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190009', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190014', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190049', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190049', 6);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190053', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190054', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190055', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190057', 8);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190058', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190042', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190056', 6);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190052', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190052', 6);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190052', 8);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190052', 9);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190060', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190060', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190062', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190062', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190061', 1);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190061', 2);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190059', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190051', 5);
INSERT INTO `let_clue_illegal_behavior` VALUES ('20190051', 6);

-- ----------------------------
-- Table structure for let_clue_source
-- ----------------------------
DROP TABLE IF EXISTS `let_clue_source`;
CREATE TABLE `let_clue_source`  (
  `clue_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '线索id',
  `source_id` int(11) NULL DEFAULT NULL COMMENT '来源id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '信访表单和线索来源关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of let_clue_source
-- ----------------------------
INSERT INTO `let_clue_source` VALUES ('20190002', 2);
INSERT INTO `let_clue_source` VALUES ('20190002', 3);
INSERT INTO `let_clue_source` VALUES ('20190002', 4);
INSERT INTO `let_clue_source` VALUES ('20190002', 5);
INSERT INTO `let_clue_source` VALUES ('20190002', 6);
INSERT INTO `let_clue_source` VALUES ('20190003', 1);
INSERT INTO `let_clue_source` VALUES ('20190003', 2);
INSERT INTO `let_clue_source` VALUES ('20190003', 3);
INSERT INTO `let_clue_source` VALUES ('20190003', 4);
INSERT INTO `let_clue_source` VALUES ('20190003', 5);
INSERT INTO `let_clue_source` VALUES ('20190001', 1);
INSERT INTO `let_clue_source` VALUES ('20190001', 2);
INSERT INTO `let_clue_source` VALUES ('20190004', 1);
INSERT INTO `let_clue_source` VALUES ('20190005', 1);
INSERT INTO `let_clue_source` VALUES ('20190006', 1);
INSERT INTO `let_clue_source` VALUES ('20190006', 2);
INSERT INTO `let_clue_source` VALUES ('20190008', 2);
INSERT INTO `let_clue_source` VALUES ('20190010', 1);
INSERT INTO `let_clue_source` VALUES ('20190013', 1);
INSERT INTO `let_clue_source` VALUES ('20190015', 1);
INSERT INTO `let_clue_source` VALUES ('20190015', 2);
INSERT INTO `let_clue_source` VALUES ('20190015', 3);
INSERT INTO `let_clue_source` VALUES ('20190016', 1);
INSERT INTO `let_clue_source` VALUES ('20190016', 2);
INSERT INTO `let_clue_source` VALUES ('20190016', 3);
INSERT INTO `let_clue_source` VALUES ('20190017', 1);
INSERT INTO `let_clue_source` VALUES ('20190017', 2);
INSERT INTO `let_clue_source` VALUES ('20190017', 3);
INSERT INTO `let_clue_source` VALUES ('20190018', 1);
INSERT INTO `let_clue_source` VALUES ('20190018', 2);
INSERT INTO `let_clue_source` VALUES ('20190018', 3);
INSERT INTO `let_clue_source` VALUES ('20190019', 1);
INSERT INTO `let_clue_source` VALUES ('20190019', 2);
INSERT INTO `let_clue_source` VALUES ('20190019', 3);
INSERT INTO `let_clue_source` VALUES ('20190020', 11);
INSERT INTO `let_clue_source` VALUES ('20190020', 12);
INSERT INTO `let_clue_source` VALUES ('20190020', 13);
INSERT INTO `let_clue_source` VALUES ('20190021', 1);
INSERT INTO `let_clue_source` VALUES ('20190021', 2);
INSERT INTO `let_clue_source` VALUES ('20190021', 3);
INSERT INTO `let_clue_source` VALUES ('20190022', 1);
INSERT INTO `let_clue_source` VALUES ('20190022', 2);
INSERT INTO `let_clue_source` VALUES ('20190022', 3);
INSERT INTO `let_clue_source` VALUES ('20190023', 1);
INSERT INTO `let_clue_source` VALUES ('20190023', 2);
INSERT INTO `let_clue_source` VALUES ('20190023', 3);
INSERT INTO `let_clue_source` VALUES ('20190024', 1);
INSERT INTO `let_clue_source` VALUES ('20190024', 2);
INSERT INTO `let_clue_source` VALUES ('20190024', 3);
INSERT INTO `let_clue_source` VALUES ('20190025', 1);
INSERT INTO `let_clue_source` VALUES ('20190025', 2);
INSERT INTO `let_clue_source` VALUES ('20190025', 3);
INSERT INTO `let_clue_source` VALUES ('20190027', 1);
INSERT INTO `let_clue_source` VALUES ('20190027', 2);
INSERT INTO `let_clue_source` VALUES ('20190027', 3);
INSERT INTO `let_clue_source` VALUES ('20190027', 1);
INSERT INTO `let_clue_source` VALUES ('20190027', 2);
INSERT INTO `let_clue_source` VALUES ('20190027', 3);
INSERT INTO `let_clue_source` VALUES ('20190031', 1);
INSERT INTO `let_clue_source` VALUES ('20190031', 2);
INSERT INTO `let_clue_source` VALUES ('20190031', 3);
INSERT INTO `let_clue_source` VALUES ('20190033', 1);
INSERT INTO `let_clue_source` VALUES ('20190033', 2);
INSERT INTO `let_clue_source` VALUES ('20190033', 3);
INSERT INTO `let_clue_source` VALUES ('20190035', 1);
INSERT INTO `let_clue_source` VALUES ('20190035', 2);
INSERT INTO `let_clue_source` VALUES ('20190035', 3);
INSERT INTO `let_clue_source` VALUES ('20190036', 1);
INSERT INTO `let_clue_source` VALUES ('20190036', 2);
INSERT INTO `let_clue_source` VALUES ('20190036', 3);
INSERT INTO `let_clue_source` VALUES ('20190037', 1);
INSERT INTO `let_clue_source` VALUES ('20190037', 2);
INSERT INTO `let_clue_source` VALUES ('20190037', 3);
INSERT INTO `let_clue_source` VALUES ('20190038', 1);
INSERT INTO `let_clue_source` VALUES ('20190038', 2);
INSERT INTO `let_clue_source` VALUES ('20190038', 3);
INSERT INTO `let_clue_source` VALUES ('20190039', 1);
INSERT INTO `let_clue_source` VALUES ('20190039', 2);
INSERT INTO `let_clue_source` VALUES ('20190039', 3);
INSERT INTO `let_clue_source` VALUES ('20190040', 1);
INSERT INTO `let_clue_source` VALUES ('20190040', 2);
INSERT INTO `let_clue_source` VALUES ('20190040', 3);
INSERT INTO `let_clue_source` VALUES ('20190041', 4);
INSERT INTO `let_clue_source` VALUES ('20190043', 5);
INSERT INTO `let_clue_source` VALUES ('20190044', 5);
INSERT INTO `let_clue_source` VALUES ('20190046', 1);
INSERT INTO `let_clue_source` VALUES ('20190047', 2);
INSERT INTO `let_clue_source` VALUES ('20190048', 1);
INSERT INTO `let_clue_source` VALUES ('20190048', 2);
INSERT INTO `let_clue_source` VALUES ('20190048', 4);
INSERT INTO `let_clue_source` VALUES ('20190048', 5);
INSERT INTO `let_clue_source` VALUES ('20190048', 3);
INSERT INTO `let_clue_source` VALUES ('20190050', 1);
INSERT INTO `let_clue_source` VALUES ('20190050', 2);
INSERT INTO `let_clue_source` VALUES ('20190050', 4);
INSERT INTO `let_clue_source` VALUES ('20190050', 5);
INSERT INTO `let_clue_source` VALUES ('20190050', 3);
INSERT INTO `let_clue_source` VALUES ('20190007', 1);
INSERT INTO `let_clue_source` VALUES ('20190007', 2);
INSERT INTO `let_clue_source` VALUES ('20190007', 3);
INSERT INTO `let_clue_source` VALUES ('20190045', 3);
INSERT INTO `let_clue_source` VALUES ('20190009', 1);
INSERT INTO `let_clue_source` VALUES ('20190014', 2);
INSERT INTO `let_clue_source` VALUES ('20190049', 1);
INSERT INTO `let_clue_source` VALUES ('20190049', 2);
INSERT INTO `let_clue_source` VALUES ('20190053', 1);
INSERT INTO `let_clue_source` VALUES ('20190053', 3);
INSERT INTO `let_clue_source` VALUES ('20190053', 4);
INSERT INTO `let_clue_source` VALUES ('20190054', 1);
INSERT INTO `let_clue_source` VALUES ('20190055', 3);
INSERT INTO `let_clue_source` VALUES ('20190057', 4);
INSERT INTO `let_clue_source` VALUES ('20190058', 3);
INSERT INTO `let_clue_source` VALUES ('20190042', 1);
INSERT INTO `let_clue_source` VALUES ('20190056', 2);
INSERT INTO `let_clue_source` VALUES ('20190052', 7);
INSERT INTO `let_clue_source` VALUES ('20190060', 1);
INSERT INTO `let_clue_source` VALUES ('20190060', 2);
INSERT INTO `let_clue_source` VALUES ('20190060', 3);
INSERT INTO `let_clue_source` VALUES ('20190062', 1);
INSERT INTO `let_clue_source` VALUES ('20190062', 2);
INSERT INTO `let_clue_source` VALUES ('20190062', 3);
INSERT INTO `let_clue_source` VALUES ('20190061', 1);
INSERT INTO `let_clue_source` VALUES ('20190061', 2);
INSERT INTO `let_clue_source` VALUES ('20190061', 3);
INSERT INTO `let_clue_source` VALUES ('20190059', 1);
INSERT INTO `let_clue_source` VALUES ('20190051', 1);
INSERT INTO `let_clue_source` VALUES ('20190051', 2);

-- ----------------------------
-- Table structure for let_defendant
-- ----------------------------
DROP TABLE IF EXISTS `let_defendant`;
CREATE TABLE `let_defendant`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `company_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作单位名称',
  `post_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务',
  `job_rank_id` int(255) NULL DEFAULT NULL COMMENT '职级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '被反映人' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of let_defendant
-- ----------------------------
INSERT INTO `let_defendant` VALUES ('2019000201', '147', '147', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019000202', '258', '258', NULL, 6);
INSERT INTO `let_defendant` VALUES ('2019000301', '789', '456', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019000302', '456', '123', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019000401', '258456', '258456', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019000601', '789', '789', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019000701', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019000901', '147', '147', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019001301', '123', '123', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019001401', '自行车', '自行车', NULL, 10);
INSERT INTO `let_defendant` VALUES ('2019001501', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019001601', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019001701', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019001801', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019001901', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019002001', '被反映人姓名2', '工作单位2', '职务2', 2);
INSERT INTO `let_defendant` VALUES ('2019002101', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019002201', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019002301', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019002401', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019002501', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019002601', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019002701', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019002801', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019002901', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019003101', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019003201', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019003301', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019003501', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019003601', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019003701', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019003801', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019003901', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019004001', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019004201', '1', '1', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019004501', '索尔', '打雷的', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019004601', '1', '1', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019004701', '2', '2', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019004801', '王丹', 'LJ程序员', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019004901', '肯德基', '肯德基', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019004902', '必胜客', '必胜客', NULL, 6);
INSERT INTO `let_defendant` VALUES ('2019005001', '王丹', 'LJ程序员', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019005101', '肯德基13', '肯德基', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019005201', '三只松鼠4', '品牌代言人品牌代言人品牌代言人品牌代言人品牌代言人品牌代言人品牌代言人品牌代言人', NULL, 6);
INSERT INTO `let_defendant` VALUES ('2019005202', 'test2', 'test3', NULL, 6);
INSERT INTO `let_defendant` VALUES ('2019005801', '1', '1', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019005901', '22', '22', NULL, 5);
INSERT INTO `let_defendant` VALUES ('2019006001', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019006101', '被反映人姓名1', '工作单位1', '职务', 1);
INSERT INTO `let_defendant` VALUES ('2019006201', '被反映人姓名1', '工作单位1', '职务', 1);

-- ----------------------------
-- Table structure for let_defendant_job_type
-- ----------------------------
DROP TABLE IF EXISTS `let_defendant_job_type`;
CREATE TABLE `let_defendant_job_type`  (
  `defendant_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `job_type_id` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '被反映人和是否村干关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of let_defendant_job_type
-- ----------------------------
INSERT INTO `let_defendant_job_type` VALUES ('2019000201', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019000201', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019000202', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019000202', 11);
INSERT INTO `let_defendant_job_type` VALUES ('2019000202', 14);
INSERT INTO `let_defendant_job_type` VALUES ('2019000301', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019000301', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019000301', 11);
INSERT INTO `let_defendant_job_type` VALUES ('2019000302', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019000302', 11);
INSERT INTO `let_defendant_job_type` VALUES ('2019000302', 14);
INSERT INTO `let_defendant_job_type` VALUES ('2019000401', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019000401', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019000601', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019000601', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019000601', 11);
INSERT INTO `let_defendant_job_type` VALUES ('2019001301', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019001501', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019001501', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019001501', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019001601', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019001601', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019001601', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019001701', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019001701', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019001701', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019001801', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019001801', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019001801', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019001901', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019001901', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019001901', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019002001', 11);
INSERT INTO `let_defendant_job_type` VALUES ('2019002001', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019002001', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019002101', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019002101', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019002101', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019002201', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019002201', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019002201', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019002301', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019002301', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019002301', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019002401', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019002401', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019002401', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019002501', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019002501', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019002501', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019002601', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019002601', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019002601', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019002701', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019002701', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019002701', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019002901', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019002901', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019002901', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019003101', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019003101', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019003101', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019003201', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019003201', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019003201', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019003301', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019003301', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019003301', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019003501', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019003501', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019003501', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019003601', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019003601', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019003601', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019003701', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019003701', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019003701', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019003801', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019003801', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019003801', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019003901', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019003901', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019003901', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019004001', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019004001', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019004001', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019004601', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019004701', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019004801', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019004801', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019005001', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019005001', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019000701', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019000701', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019000701', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019004501', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019004501', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019000901', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019001401', 15);
INSERT INTO `let_defendant_job_type` VALUES ('2019004901', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019004901', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019004902', 14);
INSERT INTO `let_defendant_job_type` VALUES ('2019004902', 10);
INSERT INTO `let_defendant_job_type` VALUES ('2019005801', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019004201', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019004201', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019005201', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019005201', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019005201', 11);
INSERT INTO `let_defendant_job_type` VALUES ('2019005201', 14);
INSERT INTO `let_defendant_job_type` VALUES ('2019005202', 14);
INSERT INTO `let_defendant_job_type` VALUES ('2019005202', 13);
INSERT INTO `let_defendant_job_type` VALUES ('2019005202', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019006001', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019006001', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019006001', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019006201', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019006201', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019006201', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019006101', 1);
INSERT INTO `let_defendant_job_type` VALUES ('2019006101', 2);
INSERT INTO `let_defendant_job_type` VALUES ('2019006101', 3);
INSERT INTO `let_defendant_job_type` VALUES ('2019005901', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019005101', 12);
INSERT INTO `let_defendant_job_type` VALUES ('2019005101', 13);

-- ----------------------------
-- Table structure for stuff
-- ----------------------------
DROP TABLE IF EXISTS `stuff`;
CREATE TABLE `stuff`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT 'ID号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `file` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `create_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `relation_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '关联信息类型',
  `relation_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联信息的关联id',
  `server_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拼接的服务端地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 386 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上传附件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stuff
-- ----------------------------
INSERT INTO `stuff` VALUES (219, 'id转value；label转title；children转children.txt', '/letClue/stuff/id转value；label转title；children转children.txt', '2019-09-17 18:00:55', '1', '20190042', NULL);
INSERT INTO `stuff` VALUES (220, '表格颜色.txt', '/letClue/stuff/表格颜色.txt', '2019-09-17 18:00:55', '1', '20190042', NULL);
INSERT INTO `stuff` VALUES (221, '多选删除.txt', '/letClue/stuff/多选删除.txt', '2019-09-17 18:00:55', '1', '20190042', NULL);
INSERT INTO `stuff` VALUES (308, '3.png', '/letClue/stuff/3_1568938698068.png', '2019-09-23 15:28:26', '1', '20190056', NULL);
INSERT INTO `stuff` VALUES (309, '4.doc', '/letClue/stuff/4_1568938698103.doc', '2019-09-23 15:28:26', '1', '20190056', NULL);
INSERT INTO `stuff` VALUES (310, '4.doc', '/letClue/stuff/4_1569222738197.doc', '2019-09-23 15:28:26', '1', '20190056', NULL);
INSERT INTO `stuff` VALUES (311, '5.pdf', '/letClue/stuff/5_1569222738229.pdf', '2019-09-23 15:28:26', '1', '20190056', NULL);
INSERT INTO `stuff` VALUES (312, '新建文本文档.txt', '/letClue/stuff/新建文本文档_1569223696391.txt', '2019-09-23 15:28:26', '1', '20190056', NULL);
INSERT INTO `stuff` VALUES (313, '新建文本文档.txt', '/letClue/stuff/新建文本文档_1569223698409.txt', '2019-09-23 15:28:26', '1', '20190056', NULL);
INSERT INTO `stuff` VALUES (314, '表格颜色.txt', '/letClue/stuff/表格颜色_1568859795136.txt', '2019-09-23 15:58:29', '1', '20190052', NULL);
INSERT INTO `stuff` VALUES (318, '文件名称1', '/test1', '2019-09-25 11:34:41', '1', '20190060', NULL);
INSERT INTO `stuff` VALUES (321, '文件名称1', '/test1', '2019-09-25 11:54:29', '1', '20190062', NULL);
INSERT INTO `stuff` VALUES (322, '文件名称1', '/test1', '2019-09-25 11:55:19', '1', '20190061', NULL);
INSERT INTO `stuff` VALUES (323, '2.txt', '/letClue/stuff/2_1568616664160.txt', '2019-09-25 14:41:38', '1', '20190059', NULL);
INSERT INTO `stuff` VALUES (324, '3.png', '/letClue/stuff/3_1568616664160.png', '2019-09-25 14:41:38', '1', '20190059', NULL);
INSERT INTO `stuff` VALUES (325, '4.doc', '/letClue/stuff/4_1568616664160.doc', '2019-09-25 14:41:38', '1', '20190059', NULL);
INSERT INTO `stuff` VALUES (326, '5.pdf', '/letClue/stuff/5_1568616664160.pdf', '2019-09-25 14:41:38', '1', '20190059', NULL);
INSERT INTO `stuff` VALUES (327, '树组件js.txt', '/letClue/stuff/树组件js_1568767702536.txt', '2019-09-25 14:41:38', '1', '20190059', NULL);
INSERT INTO `stuff` VALUES (328, '1.zip', '/letClue/stuff/1_1568768038219.zip', '2019-09-25 14:41:38', '1', '20190059', NULL);
INSERT INTO `stuff` VALUES (379, 'back (2).jpg', '/letClue/stuff/back (2).jpg', '2019-09-26 09:50:11', '1', '20190051', NULL);
INSERT INTO `stuff` VALUES (380, 'back (3).jpg', '/letClue/stuff/back (3).jpg', '2019-09-26 09:50:11', '1', '20190051', NULL);
INSERT INTO `stuff` VALUES (381, 'back (4).jpg', '/letClue/stuff/back (4).jpg', '2019-09-26 09:50:11', '1', '20190051', NULL);
INSERT INTO `stuff` VALUES (382, 'back (1).jpg', '/letClue/stuff/back (1).jpg', '2019-09-26 09:50:11', '1', '20190051', NULL);
INSERT INTO `stuff` VALUES (383, 'QQ图片20181219174242.gif', '/letClue/stuff/QQ图片20181219174242.gif', '2019-09-26 09:50:11', '1', '20190051', NULL);
INSERT INTO `stuff` VALUES (384, 'QQ图片20181220101615.jpg', '/letClue/stuff/QQ图片20181220101615.jpg', '2019-09-26 09:50:11', '1', '20190051', NULL);
INSERT INTO `stuff` VALUES (385, '1.jpg', '/letClue/stuff/1_1569462605901.jpg', '2019-09-26 09:50:11', '1', '20190051', NULL);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限值（唯一）',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型group,page,button',
  `is_enable` bit(1) NULL DEFAULT b'1' COMMENT '可用',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '已删除',
  `index_num` int(11) NULL DEFAULT 1000 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, 0, '系统管理', 'systemManage', 'group', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (2, 1, '权限管理', 'permission', 'page', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (3, 1, '角色管理', 'role', 'page', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (6, 1, '组织结构管理', 'unit', 'page', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (7, 1, '用户管理', 'user', 'page', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (11, 60, '信访举报表单', 'letClue', 'page', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (12, 11, '录入', 'letClue:insert', 'permission', b'1', b'1', 1000);
INSERT INTO `sys_permission` VALUES (13, 11, '删除', 'letClue:delete', 'permission', b'1', b'1', 1000);
INSERT INTO `sys_permission` VALUES (14, 11, '修改', 'letClue:update', 'permission', b'1', b'1', 1000);
INSERT INTO `sys_permission` VALUES (15, 11, '查看', 'letClue:select', 'permission', b'1', b'1', 1000);
INSERT INTO `sys_permission` VALUES (16, 60, '线索审批', 'reviewLetClue', 'page', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (17, 16, '本委领导', 'reviewLetClue:bwld', 'permission', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (18, 16, '分管副书记', 'reviewLetClue:fgfsj', 'permission', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (19, 16, '主管常委', 'reviewLetClue:zgcw', 'permission', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (20, 16, '审查室', 'reviewLetClue:scs', 'permission', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (27, 60, '信访举报分类', 'letClueType', 'page', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (28, 1, '数据字典', 'dicAll', 'page', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (60, 0, '线索管理', 'letClueManage', 'group', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (61, 60, '案件管理', 'caseManage', 'page', b'1', b'0', 1000);
INSERT INTO `sys_permission` VALUES (62, 0, 'test', '123', 'group', b'0', b'1', 1000);
INSERT INTO `sys_permission` VALUES (63, 0, 'z', 'z', 'z', b'0', b'1', 1000);
INSERT INTO `sys_permission` VALUES (64, 1, '123', '1234', 'group', b'1', b'1', 1000);
INSERT INTO `sys_permission` VALUES (65, 0, 'aa', 'aa', 'group', b'1', b'1', 1000);
INSERT INTO `sys_permission` VALUES (66, 0, 'v', 'v', 'group', b'1', b'1', 1000);
INSERT INTO `sys_permission` VALUES (67, 0, '1', '1', 'group', b'1', b'1', 1000);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色值',
  `is_enable` bit(1) NULL DEFAULT b'1' COMMENT '可用',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '角色1', 'role1', b'1', b'0');
INSERT INTO `sys_role` VALUES (2, '角色2', 'role2', b'1', b'0');
INSERT INTO `sys_role` VALUES (3, '角色3', 'role3', b'1', b'0');
INSERT INTO `sys_role` VALUES (4, '啊', 'role', b'1', b'0');
INSERT INTO `sys_role` VALUES (5, '角色4', 'role4', b'1', b'1');
INSERT INTO `sys_role` VALUES (6, '角色4', '角色4', b'1', b'0');
INSERT INTO `sys_role` VALUES (7, '角色5', '角色5', b'1', b'0');
INSERT INTO `sys_role` VALUES (8, '角色6', '角色6', b'1', b'1');
INSERT INTO `sys_role` VALUES (9, '修改角色测试1', 'rt1', b'1', b'1');
INSERT INTO `sys_role` VALUES (10, 'test1', 'review:letClue', b'1', b'0');
INSERT INTO `sys_role` VALUES (11, '', '', b'1', b'1');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `role_id` bigint(20) NULL DEFAULT NULL,
  `permission_id` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色-权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (2, 1);
INSERT INTO `sys_role_permission` VALUES (9, 2);
INSERT INTO `sys_role_permission` VALUES (9, 3);
INSERT INTO `sys_role_permission` VALUES (1, 27);
INSERT INTO `sys_role_permission` VALUES (1, 11);
INSERT INTO `sys_role_permission` VALUES (1, 28);
INSERT INTO `sys_role_permission` VALUES (1, 2);
INSERT INTO `sys_role_permission` VALUES (1, 7);
INSERT INTO `sys_role_permission` VALUES (1, 6);
INSERT INTO `sys_role_permission` VALUES (1, 3);
INSERT INTO `sys_role_permission` VALUES (1, 19);
INSERT INTO `sys_role_permission` VALUES (1, 18);
INSERT INTO `sys_role_permission` VALUES (1, 20);
INSERT INTO `sys_role_permission` VALUES (1, 17);
INSERT INTO `sys_role_permission` VALUES (3, 1);
INSERT INTO `sys_role_permission` VALUES (3, 28);
INSERT INTO `sys_role_permission` VALUES (3, 2);
INSERT INTO `sys_role_permission` VALUES (3, 7);
INSERT INTO `sys_role_permission` VALUES (3, 6);
INSERT INTO `sys_role_permission` VALUES (3, 3);
INSERT INTO `sys_role_permission` VALUES (4, 60);
INSERT INTO `sys_role_permission` VALUES (4, 1);
INSERT INTO `sys_role_permission` VALUES (4, 28);
INSERT INTO `sys_role_permission` VALUES (4, 2);
INSERT INTO `sys_role_permission` VALUES (4, 7);
INSERT INTO `sys_role_permission` VALUES (4, 6);
INSERT INTO `sys_role_permission` VALUES (4, 3);
INSERT INTO `sys_role_permission` VALUES (4, 27);
INSERT INTO `sys_role_permission` VALUES (4, 16);
INSERT INTO `sys_role_permission` VALUES (4, 19);
INSERT INTO `sys_role_permission` VALUES (4, 18);
INSERT INTO `sys_role_permission` VALUES (4, 20);
INSERT INTO `sys_role_permission` VALUES (4, 17);
INSERT INTO `sys_role_permission` VALUES (10, 60);
INSERT INTO `sys_role_permission` VALUES (10, 1);
INSERT INTO `sys_role_permission` VALUES (10, 28);
INSERT INTO `sys_role_permission` VALUES (10, 2);
INSERT INTO `sys_role_permission` VALUES (10, 7);
INSERT INTO `sys_role_permission` VALUES (10, 6);
INSERT INTO `sys_role_permission` VALUES (10, 3);
INSERT INTO `sys_role_permission` VALUES (10, 11);
INSERT INTO `sys_role_permission` VALUES (10, 16);
INSERT INTO `sys_role_permission` VALUES (10, 19);
INSERT INTO `sys_role_permission` VALUES (10, 18);
INSERT INTO `sys_role_permission` VALUES (10, 20);
INSERT INTO `sys_role_permission` VALUES (10, 17);

-- ----------------------------
-- Table structure for sys_signature
-- ----------------------------
DROP TABLE IF EXISTS `sys_signature`;
CREATE TABLE `sys_signature`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `sign_file_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签名文件路径',
  `create_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间（自动填充）',
  `is_default` bit(1) NULL DEFAULT b'0' COMMENT '是否作为默认签名（默认否）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户上传的签名文件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_unit
-- ----------------------------
DROP TABLE IF EXISTS `sys_unit`;
CREATE TABLE `sys_unit`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '是否已删除',
  `index_num` bigint(20) NULL DEFAULT 1000 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '单位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_unit
-- ----------------------------
INSERT INTO `sys_unit` VALUES (53, 0, '纪检委', '单位', b'0', 100);
INSERT INTO `sys_unit` VALUES (54, 53, '案管', '组织', b'0', 1000);
INSERT INTO `sys_unit` VALUES (57, 54, '案管室1', '案管室', b'0', 1);
INSERT INTO `sys_unit` VALUES (58, 54, '案管室2', '案管室', b'0', 2);
INSERT INTO `sys_unit` VALUES (59, 54, '案管室3', '案管室', b'0', 3);
INSERT INTO `sys_unit` VALUES (60, 54, '案管室4', '案管室', b'0', 4);
INSERT INTO `sys_unit` VALUES (61, 54, '案管室5', '案管室', b'0', 5);
INSERT INTO `sys_unit` VALUES (62, 54, '案管室6', '案管室', b'0', 6);
INSERT INTO `sys_unit` VALUES (63, 54, '案管室7', '案管室', b'0', 7);
INSERT INTO `sys_unit` VALUES (64, 54, '案管室8', '案管室', b'0', 8);
INSERT INTO `sys_unit` VALUES (65, 54, '案管室9', '案管室', b'0', 9);
INSERT INTO `sys_unit` VALUES (66, 54, '案管室10', '案管室', b'0', 10);
INSERT INTO `sys_unit` VALUES (67, 54, '案管室11', '案管室', b'0', 11);
INSERT INTO `sys_unit` VALUES (68, 54, '案管室12', '案管室', b'0', 12);
INSERT INTO `sys_unit` VALUES (69, 54, '案管室13', '案管室', b'0', 13);
INSERT INTO `sys_unit` VALUES (70, 54, '案管室14', '案管室', b'0', 14);
INSERT INTO `sys_unit` VALUES (71, 54, '案管室15', '案管室', b'0', 15);
INSERT INTO `sys_unit` VALUES (73, 0, '1', '1', b'1', 1000);
INSERT INTO `sys_unit` VALUES (74, 0, '2', '2', b'1', 1000);
INSERT INTO `sys_unit` VALUES (75, 0, '3', '3', b'1', 1000);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `is_enable` bit(1) NULL DEFAULT b'1' COMMENT '可用',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '已删除',
  `is_admin` bit(1) NULL DEFAULT b'0' COMMENT '管理员账号',
  `is_basic` bit(1) NULL DEFAULT b'0' COMMENT '基础账号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (19, 'admin', 'admin', '$2a$10$fNjjiUsLMvOg3fCp6r4ine9ol7xRY68OhCQBDfMFJgZnRuvABarpq', b'1', b'0', b'1', b'0');
INSERT INTO `sys_user` VALUES (20, 'test1', 'test1', '$2a$10$g6KjHNsNsEio.XpRZ9lg5eGEyvEcTa9ZkjV.bd7EzwV6Sgy/epv1i', b'1', b'0', b'0', b'0');
INSERT INTO `sys_user` VALUES (21, 're1', 're1', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0', b'0', b'0');
INSERT INTO `sys_user` VALUES (22, 're2', 're2', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0', b'0', b'0');
INSERT INTO `sys_user` VALUES (23, 're3', 're3', '$2a$10$Tiw7puaWS8IKx0VOzgA.DuAyxLlUsbVgQYRgDMNsdsOiT9/Lf0pxq', b'1', b'0', b'0', b'0');
INSERT INTO `sys_user` VALUES (25, 'string', 'string', '$2a$10$LwxTJ9XIF/iHgJZCX4MJvOc/oHbjd2AcqukDFBK60bqoUNNibBkbW', b'1', b'1', b'0', b'0');
INSERT INTO `sys_user` VALUES (27, '123', '123', '$2a$10$PA1JF.th3Cpe3Xm3RKSFheEzB2ZNail0T5nlU3QMuNzqF12lw5SjG', b'1', b'1', b'0', b'0');
INSERT INTO `sys_user` VALUES (29, 'test2', 'test2', '$2a$10$rqyncbXlony4lHPS.zCntu5kOpPR7ivAsVrkrKcYiHOM2gIoduEMu', b'1', b'0', b'0', b'0');
INSERT INTO `sys_user` VALUES (30, '修改用户1', 'ut1', '$2a$10$xywNU003F8UEA663jkAwC.54svE2sYth3qLxrXRVIUcogxJq7Mbjy', b'1', b'1', b'0', b'0');
INSERT INTO `sys_user` VALUES (31, '', '', '$2a$10$YO8bJwFfGURWDndHlZOSy.OfnI/c4A6wNHtP8EqbMCiXMmDmHzVPu', b'1', b'1', b'0', b'0');
INSERT INTO `sys_user` VALUES (32, '测试1', 'ceshi1', '$2a$10$5H7oicw1Iv86rOp8ZVb0Ke9kJ73sGgAVyShsJgtx57oM0e7Xxc/Im', b'1', b'0', b'0', b'0');
INSERT INTO `sys_user` VALUES (33, 'ceshi2', 'ces2', '$2a$10$PfSex0WjSMYHRKLf3zDHIecWxyVdXjFpjLb65Ho0jEcfmCRyPezPa', b'0', b'0', b'0', b'0');
INSERT INTO `sys_user` VALUES (34, '12345', '12334', '$2a$10$beAevNouUOFCjuqMbK9zVegA/ww2E0soPL1oJPgbiH4qa9HYBHI8G', b'1', b'1', b'0', b'0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户-角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (191, 4);
INSERT INTO `sys_user_role` VALUES (30, 2);
INSERT INTO `sys_user_role` VALUES (30, 3);
INSERT INTO `sys_user_role` VALUES (30, 4);
INSERT INTO `sys_user_role` VALUES (32, 4);
INSERT INTO `sys_user_role` VALUES (32, 1);
INSERT INTO `sys_user_role` VALUES (33, 10);
INSERT INTO `sys_user_role` VALUES (33, 1);
INSERT INTO `sys_user_role` VALUES (33, 4);
INSERT INTO `sys_user_role` VALUES (19, 10);
INSERT INTO `sys_user_role` VALUES (19, 7);
INSERT INTO `sys_user_role` VALUES (19, 4);
INSERT INTO `sys_user_role` VALUES (19, 1);
INSERT INTO `sys_user_role` VALUES (19, 2);
INSERT INTO `sys_user_role` VALUES (19, 3);
INSERT INTO `sys_user_role` VALUES (19, 8);
INSERT INTO `sys_user_role` VALUES (19, 6);
INSERT INTO `sys_user_role` VALUES (20, 4);

-- ----------------------------
-- Table structure for sys_user_unit
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_unit`;
CREATE TABLE `sys_user_unit`  (
  `user_id` bigint(20) NULL DEFAULT NULL,
  `unit_id` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户-单位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_unit
-- ----------------------------
INSERT INTO `sys_user_unit` VALUES (30, 2);
INSERT INTO `sys_user_unit` VALUES (21, 13);
INSERT INTO `sys_user_unit` VALUES (22, 15);
INSERT INTO `sys_user_unit` VALUES (23, 17);
INSERT INTO `sys_user_unit` VALUES (29, 8);
INSERT INTO `sys_user_unit` VALUES (32, 8);
INSERT INTO `sys_user_unit` VALUES (33, 47);
INSERT INTO `sys_user_unit` VALUES (19, 1);
INSERT INTO `sys_user_unit` VALUES (20, 13);

-- ----------------------------
-- Table structure for table_in_db1
-- ----------------------------
DROP TABLE IF EXISTS `table_in_db1`;
CREATE TABLE `table_in_db1`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `column1` int(11) NULL DEFAULT NULL,
  `column2` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of table_in_db1
-- ----------------------------
INSERT INTO `table_in_db1` VALUES (1, NULL, NULL);
INSERT INTO `table_in_db1` VALUES (2, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
