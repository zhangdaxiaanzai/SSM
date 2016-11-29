/*
Navicat MySQL Data Transfer

Source Server         : bk
Source Server Version : 50631
Source Host           : localhost:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50631
File Encoding         : 65001

Date: 2016-11-29 16:42:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for seckill
-- ----------------------------
DROP TABLE IF EXISTS `seckill`;
CREATE TABLE `seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '秒杀开始时间',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`) USING BTREE,
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seckill
-- ----------------------------
INSERT INTO `seckill` VALUES ('1', '1000元秒杀iPhone 7', '88', '2016-11-28 11:00:00', '2016-11-30 00:00:00', '2016-11-29 14:41:01');
INSERT INTO `seckill` VALUES ('2', '500元秒杀ipad air 2', '200', '2016-11-18 00:00:00', '2016-11-19 00:00:00', '2016-11-18 13:51:28');
INSERT INTO `seckill` VALUES ('3', '300元秒杀小米5', '300', '2016-11-18 00:00:00', '2016-11-19 00:00:00', '2016-11-18 13:51:28');
INSERT INTO `seckill` VALUES ('4', '200元秒杀小米5', '400', '2016-11-18 00:00:00', '2016-11-19 00:00:00', '2016-11-18 13:51:28');

-- ----------------------------
-- Table structure for success_killed
-- ----------------------------
DROP TABLE IF EXISTS `success_killed`;
CREATE TABLE `success_killed` (
  `seckill_id` bigint(20) NOT NULL,
  `user_phone` bigint(20) NOT NULL COMMENT '用户手机号',
  `state` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '-1:无效；0：成功；1：已付款；2：已发货',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`,`user_phone`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of success_killed
-- ----------------------------
INSERT INTO `success_killed` VALUES ('1', '1232542', '0', '2016-11-22 17:18:10');
INSERT INTO `success_killed` VALUES ('1', '123234242', '0', '2016-11-22 17:06:26');
INSERT INTO `success_killed` VALUES ('1', '1232334542', '0', '2016-11-22 17:15:32');
INSERT INTO `success_killed` VALUES ('1', '12326766782', '0', '2016-11-22 17:18:27');
INSERT INTO `success_killed` VALUES ('1', '18079652332', '0', '2016-11-21 17:18:46');
INSERT INTO `success_killed` VALUES ('1', '18079658768', '0', '2016-11-21 17:18:48');
INSERT INTO `success_killed` VALUES ('1', '18079658769', '0', '0000-00-00 00:00:00');
INSERT INTO `success_killed` VALUES ('1', '18758886676', '0', '2016-11-29 14:41:01');
INSERT INTO `success_killed` VALUES ('1', '18788886676', '-1', '2016-11-29 13:28:50');
INSERT INTO `success_killed` VALUES ('1', '18888888888', '0', '2016-11-28 11:33:59');
INSERT INTO `success_killed` VALUES ('1', '19299889988', '-1', '2016-11-29 14:32:08');
INSERT INTO `success_killed` VALUES ('1', '19887977657', '-1', '2016-11-29 14:23:02');
INSERT INTO `success_killed` VALUES ('1', '123234454542', '0', '2016-11-22 17:16:54');
INSERT INTO `success_killed` VALUES ('2', '1000998236', '0', '2016-11-21 17:21:09');
INSERT INTO `success_killed` VALUES ('2', '13577698888', '0', '2016-11-21 17:20:48');

-- ----------------------------
-- Procedure structure for hi
-- ----------------------------
DROP PROCEDURE IF EXISTS `hi`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `hi`()
select 'hello'
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for seckill
-- ----------------------------
DROP PROCEDURE IF EXISTS `seckill`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `seckill`(in v_seckill_id bigint,in v_phone bigint,in v_kill_time timestamp,out r_result int)
BEGIN
	   DECLARE count int DEFAULT 0;
	   START TRANSACTION;
	   insert ignore into success_killed
	   			(seckill_id,user_phone,create_time,state)
	   values(v_seckill_id,v_phone,v_kill_time,0);

	   select ROW_COUNT() into count;

	   IF (count = 0) THEN
			 ROLLBACK;
			 SET r_result = -1;

			 ELSEIF (count < 0) THEN
			 ROLLBACK;
			 SET r_result  = -2;
			 
			 ELSE
				UPDATE seckill
				SET number=number-1
				WHERE seckill_id=v_seckill_id 
							and end_time > v_kill_time 
							and start_time < v_kill_time 
							and number > 0;
			 select row_count() into count;

			 IF (count = 0) THEN
			 ROLLBACK;
			 SET r_result = 0;
			 ELSEIF (count < 0) THEN
			 ROLLBACK;
			 SET r_result  = -2;
			 ELSE
			 COMMIT;
			 SET r_result  = 1;
			 END IF;
	  END IF;

END
;;
DELIMITER ;
