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

/*Table structure for table `xc_orders` */

DROP TABLE IF EXISTS `xc_orders`;

CREATE TABLE `xc_orders` (
  `order_number` varchar(32) NOT NULL COMMENT '订单号',
  `initial_price` float(8,2) DEFAULT NULL COMMENT '定价',
  `price` float(8,2) DEFAULT NULL COMMENT '交易价',
  `start_time` datetime NOT NULL COMMENT '起始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` varchar(32) DEFAULT NULL COMMENT '交易状态',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `details` varchar(1000) DEFAULT NULL COMMENT '订单明细',
  PRIMARY KEY (`order_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_orders` */

insert  into `xc_orders`(`order_number`,`initial_price`,`price`,`start_time`,`end_time`,`status`,`user_id`,`details`) values ('299036931059486720',0.01,0.01,'2018-04-05 12:26:00','2018-04-05 14:26:00','401001','49','[{\"itemId\":\"4028e581617f945f01617f9dabc40000\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299036931059486720\"}]'),('299118286820741120',0.01,0.01,'2018-04-05 17:49:17','2018-04-05 19:49:17','401001','49','[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299118286820741120\"}]'),('299118455888941056',0.01,0.01,'2018-04-05 17:49:57','2018-04-05 19:49:57','401001','49','[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299118455888941056\"}]'),('299144273360982016',0.01,0.01,'2018-04-05 19:32:33','2018-04-05 21:32:33','401002','49','[{\"itemId\":\"4028e58161bcf7f40161bcf8b77c0000\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299144273360982016\"}]'),('299202627802370048',0.01,0.01,'2018-04-05 23:24:26','2018-04-06 01:24:26','401001','49','[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299202627802370048\"}]'),('299226261577142272',0.01,0.01,'2018-04-06 00:58:50','2018-04-06 02:58:50','401001','49','[{\"itemId\":\"4028e58161bd3b380161bd3bcd2f0000\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299226261577142272\"}]'),('299226499146715136',0.01,0.01,'2018-04-06 00:59:17','2018-04-06 02:59:17','401001','49','[{\"itemId\":\"4028e58161bd3b380161bd3bcd2f0000\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299226499146715136\"}]'),('317320500991102976',0.01,0.01,'2018-05-25 23:18:23','2018-05-26 01:18:23','401001','49','[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"317320500991102976\"}]'),('317320549372399616',0.01,0.01,'2018-05-25 23:18:35','2018-05-26 01:18:35','401001','49','[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"317320549372399616\"}]'),('317326221119983616',0.01,0.01,'2018-05-25 23:41:07','2018-05-26 01:41:07','401001','49','[{\"itemId\":\"402885816243d2dd016243f24c030002\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"317326221119983616\"}]');

/*Table structure for table `xc_orders_detail` */

DROP TABLE IF EXISTS `xc_orders_detail`;

CREATE TABLE `xc_orders_detail` (
  `id` varchar(32) NOT NULL,
  `order_number` varchar(32) NOT NULL COMMENT '订单号',
  `item_id` varchar(32) NOT NULL COMMENT '商品id',
  `item_num` int(8) NOT NULL COMMENT '商品数量',
  `item_price` float(8,2) NOT NULL COMMENT '金额',
  PRIMARY KEY (`id`),
  UNIQUE KEY `xc_orders_detail_unique` (`order_number`,`item_id`),
  CONSTRAINT `fk_xc_orders_detail_order_number` FOREIGN KEY (`order_number`) REFERENCES `xc_orders` (`order_number`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_orders_detail` */

insert  into `xc_orders_detail`(`id`,`order_number`,`item_id`,`item_num`,`item_price`) values ('297e02f76397e0d7016397e11eba0000','317320500991102976','4028e58161bd22e60161bd23672a0001',1,0.01),('297e02f76397e0d7016397e14b190002','317320549372399616','4028e58161bd22e60161bd23672a0001',1,0.01),('297e02f76397e0d7016397f5ed510004','317326221119983616','402885816243d2dd016243f24c030002',1,0.01),('4028858162940c510162940de2100000','299036931059486720','4028e581617f945f01617f9dabc40000',1,0.01),('4028858162944c9801629535db390000','299118286820741120','4028e58161bd22e60161bd23672a0001',1,0.01),('4028858162944c980162953676d80002','299118455888941056','4028e58161bd22e60161bd23672a0001',1,0.01),('402885816295920a0162959464640000','299144273360982016','4028e58161bcf7f40161bcf8b77c0000',1,0.01),('402885816295be9901629668af9c0000','299202627802370048','4028e58161bd22e60161bd23672a0001',1,0.01),('402885816295be99016296bf1df10002','299226261577142272','4028e58161bd3b380161bd3bcd2f0000',1,0.01),('402885816295be99016296bf85b70004','299226499146715136','4028e58161bd3b380161bd3bcd2f0000',1,0.01);

/*Table structure for table `xc_orders_pay` */

DROP TABLE IF EXISTS `xc_orders_pay`;

CREATE TABLE `xc_orders_pay` (
  `id` varchar(32) NOT NULL,
  `order_number` varchar(32) NOT NULL COMMENT '订单号',
  `pay_number` varchar(32) DEFAULT NULL COMMENT '支付系统订单号',
  `status` varchar(32) NOT NULL COMMENT '交易状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `xc_orders_pay_order_number_unique` (`order_number`),
  UNIQUE KEY `xc_orders_pay_pay_number_unique` (`pay_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_orders_pay` */

insert  into `xc_orders_pay`(`id`,`order_number`,`pay_number`,`status`) values ('297e02f76397e0d7016397e11ec00001','317320500991102976',NULL,'402001'),('297e02f76397e0d7016397e14b190003','317320549372399616',NULL,'402001'),('297e02f76397e0d7016397f5ed510005','317326221119983616',NULL,'402001'),('40288581627cdb0e01627ce2f8ad0009','297406656009342976',NULL,'402001'),('4028858162863b6d0162864231b60001','298066138997592064',NULL,'402001'),('4028858162940c510162940de2150001','299036931059486720',NULL,'402001'),('4028858162944c9801629535db3f0001','299118286820741120',NULL,'402001'),('4028858162944c980162953676d80003','299118455888941056',NULL,'402001'),('402885816295920a0162959464670001','299144273360982016',NULL,'402002'),('402885816295be9901629668afa30001','299202627802370048',NULL,'402001'),('402885816295be99016296bf1df10003','299226261577142272',NULL,'402001'),('402885816295be99016296bf85b70005','299226499146715136',NULL,'402001');

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
