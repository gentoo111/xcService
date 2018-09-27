/*
SQLyog v10.2 
MySQL - 5.7.21-log : Database - xc_order
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
USE `xc_order`;

/*Table structure for table `xc_task` */

DROP TABLE IF EXISTS `xc_task`;

CREATE TABLE `xc_task` (
  `id` varchar(32) NOT NULL COMMENT '任务id',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `task_type` varchar(32) DEFAULT NULL COMMENT '任务类型',
  `mq_exchange` varchar(64) DEFAULT NULL COMMENT '交换机名称',
  `mq_routingkey` varchar(64) DEFAULT NULL COMMENT 'routingkey',
  `request_body` varchar(512) DEFAULT NULL COMMENT '任务请求的内容',
  `version` int(10) DEFAULT NULL COMMENT '乐观锁版本号',
  `status` varchar(32) DEFAULT NULL COMMENT '任务状态',
  `errormsg` varchar(512) DEFAULT NULL COMMENT '任务错误信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_task` */

/*Table structure for table `xc_task_his` */

DROP TABLE IF EXISTS `xc_task_his`;

CREATE TABLE `xc_task_his` (
  `id` varchar(32) NOT NULL COMMENT '任务id',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `task_type` varchar(32) DEFAULT NULL COMMENT '任务类型',
  `mq_exchange` varchar(64) DEFAULT NULL COMMENT '交换机名称',
  `mq_routingkey` varchar(64) DEFAULT NULL COMMENT 'routingkey',
  `request_body` varchar(512) DEFAULT NULL COMMENT '任务请求的内容',
  `version` int(10) DEFAULT '0' COMMENT '乐观锁版本号',
  `status` varchar(32) DEFAULT NULL COMMENT '任务状态',
  `errormsg` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_task_his` */

insert  into `xc_task_his`(`id`,`create_time`,`update_time`,`delete_time`,`task_type`,`mq_exchange`,`mq_routingkey`,`request_body`,`version`,`status`,`errormsg`) values ('10','2018-04-04 22:58:20','2018-04-04 22:58:54','2018-04-04 22:58:55',NULL,NULL,NULL,'{\"userId\":\"49\",\"ids\":\"4028e581617f945f01617f9dabc40000\"}',NULL,NULL,NULL),('4028858162959ce5016295b604ba0000','2018-04-05 20:09:17','2018-04-05 20:09:18','2018-04-05 20:09:21','add_choosecourse','XC_TASK_EXCHANGE','request_choosecourse_task_queue_routing_key','{\"ids\":\"4028e58161bcf7f40161bcf8b77c0000,\",\"userId\":\"49\"}',NULL,'10201',NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
