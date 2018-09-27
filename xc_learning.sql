/*
SQLyog v10.2 
MySQL - 5.7.21-log : Database - xc_learning
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
USE `xc_learning`;

/*Table structure for table `xc_learning_course` */

DROP TABLE IF EXISTS `xc_learning_course`;

CREATE TABLE `xc_learning_course` (
  `id` varchar(32) NOT NULL,
  `course_id` varchar(32) NOT NULL COMMENT '课程id',
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `charge` varchar(32) DEFAULT NULL COMMENT '收费规则',
  `price` float(8,2) DEFAULT NULL COMMENT '课程价格',
  `valid` varchar(32) DEFAULT NULL COMMENT '有效性',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `status` varchar(32) DEFAULT NULL COMMENT '选课状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `xc_learning_list_unique` (`course_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xc_learning_course` */

insert  into `xc_learning_course`(`id`,`course_id`,`user_id`,`charge`,`price`,`valid`,`start_time`,`end_time`,`status`) values ('40288581629123300162912af5630001','4028e581617f945f01617f9dabc40000','49',NULL,0.01,NULL,NULL,NULL,'501001'),('4028858162936228016295b613650000','4028e58161bcf7f40161bcf8b77c0000','49',NULL,0.01,NULL,'2018-04-01 10:50:31','2020-04-01 10:50:37','501001'),('402885816298a126016298ac09a10001','4028e58161bd22e60161bd23672a0001','49',NULL,NULL,NULL,'2018-04-01 10:50:31','2020-04-01 10:50:37','501001');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
