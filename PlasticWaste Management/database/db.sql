/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.7.9 : Database - plasticwaste_update
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`plasticwaste_update` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `plasticwaste_update`;

/*Table structure for table `agents` */

DROP TABLE IF EXISTS `agents`;

CREATE TABLE `agents` (
  `agent_id` int(11) NOT NULL AUTO_INCREMENT,
  `log_id` int(11) DEFAULT NULL,
  `fname` varchar(50) DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`agent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `agents` */

insert  into `agents`(`agent_id`,`log_id`,`fname`,`lname`,`phone`,`email`) values (2,3,'Bency','Baby','9656323075','as@dfs.sd'),(3,4,'Blesson','Baby','9656323075','as@dfs.sd');

/*Table structure for table `collection_request` */

DROP TABLE IF EXISTS `collection_request`;

CREATE TABLE `collection_request` (
  `request_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `route_id` int(11) DEFAULT NULL,
  `latitude` varchar(50) DEFAULT NULL,
  `longitude` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `request_status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`request_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `collection_request` */

insert  into `collection_request`(`request_id`,`user_id`,`route_id`,`latitude`,`longitude`,`date`,`request_status`) values (1,1,1,'9.976307','76.2862674','2020-03-04','Collected'),(2,1,1,'9.976288','76.2862821','2020-03-04','Collected');

/*Table structure for table `company` */

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `com_id` int(11) NOT NULL AUTO_INCREMENT,
  `log_id` int(11) DEFAULT NULL,
  `com_name` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`com_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `company` */

insert  into `company`(`com_id`,`log_id`,`com_name`,`phone`,`email`) values (3,8,'Riss','9656323075','as@dfs.sd'),(4,11,'UST','1234567890','ust@gmail.com');

/*Table structure for table `company_collection` */

DROP TABLE IF EXISTS `company_collection`;

CREATE TABLE `company_collection` (
  `collection_id` int(11) NOT NULL AUTO_INCREMENT,
  `com_id` int(11) DEFAULT NULL,
  `agent_id` int(11) DEFAULT NULL,
  `waste_quantity` varchar(50) DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`collection_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `company_collection` */

insert  into `company_collection`(`collection_id`,`com_id`,`agent_id`,`waste_quantity`,`datetime`) values (2,4,2,'12','2020-02-18 00:00:00'),(4,4,3,'100','2020-02-18 00:00:00');

/*Table structure for table `feedback` */

DROP TABLE IF EXISTS `feedback`;

CREATE TABLE `feedback` (
  `feed_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `feedback` varchar(200) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`feed_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Data for the table `feedback` */

insert  into `feedback`(`feed_id`,`user_id`,`feedback`,`date`) values (1,1,'Good','2020-02-15'),(2,2,'Good','2020-02-15'),(3,1,'hii','2020-03-04'),(4,1,'hii','2020-03-04'),(5,1,'hii','2020-03-04'),(6,1,'hii','2020-03-04'),(7,1,'hii','2020-03-04');

/*Table structure for table `login` */

DROP TABLE IF EXISTS `login`;

CREATE TABLE `login` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `usertype` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

/*Data for the table `login` */

insert  into `login`(`log_id`,`username`,`password`,`usertype`) values (1,'admin','admin','admin'),(3,'bency','bency','Agent'),(4,'bless','bless','Agent'),(8,'riss','riss','company'),(9,'baby','baby','user'),(10,'susan','susan','user'),(11,'ust','ust','company'),(12,'asdfg','asdfg','User');

/*Table structure for table `payment` */

DROP TABLE IF EXISTS `payment`;

CREATE TABLE `payment` (
  `pay_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `amount` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `payment` */

insert  into `payment`(`pay_id`,`user_id`,`amount`,`date`) values (1,1,'5023','2020-02-18');

/*Table structure for table `products` */

DROP TABLE IF EXISTS `products`;

CREATE TABLE `products` (
  `pro_id` int(11) NOT NULL AUTO_INCREMENT,
  `pro_name` varchar(50) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `amount` varchar(50) DEFAULT NULL,
  `image` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`pro_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `products` */

insert  into `products`(`pro_id`,`pro_name`,`description`,`amount`,`image`) values (1,'sdfgh','Zxcvfbgnhmjdfg ','5023','static/uploads/2cf475da-3944-49ac-ad0c-21dc8d68880aIMG-20180809-WA0032.jpg'),(3,'zxxc','	werty		','5000','static/uploads/30d1fb42-6a6e-4a8d-83b9-d3c020e5d59dIMG-20180809-WA0032.jpg');

/*Table structure for table `purchase_details` */

DROP TABLE IF EXISTS `purchase_details`;

CREATE TABLE `purchase_details` (
  `pd_id` int(11) NOT NULL AUTO_INCREMENT,
  `pm_id` int(11) DEFAULT NULL,
  `stock_id` int(11) DEFAULT NULL,
  `quantity` varchar(50) DEFAULT NULL,
  `amount` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`pd_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

/*Data for the table `purchase_details` */

insert  into `purchase_details`(`pd_id`,`pm_id`,`stock_id`,`quantity`,`amount`) values (1,1,1,'1','5023'),(2,2,1,'0','0'),(3,3,1,'0','0'),(4,4,1,'0','0'),(5,5,4,'null','5023'),(6,6,6,'null','5000'),(7,7,12,'2','5023'),(8,8,1,'3','5023'),(9,9,1,'4','5023'),(10,10,2,'2','5023'),(11,11,2,'2','5023'),(12,12,2,'2','5023'),(13,13,1,'2','5023'),(14,14,1,'2','5023'),(15,15,2,'2','5023'),(16,16,2,'3','5023'),(17,17,2,'3','5023'),(18,18,2,'4','5023');

/*Table structure for table `purchase_master` */

DROP TABLE IF EXISTS `purchase_master`;

CREATE TABLE `purchase_master` (
  `pm_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `com_id` int(11) DEFAULT NULL,
  `total_amount` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `assigned_agent` varchar(50) DEFAULT NULL,
  `delivery_status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`pm_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

/*Data for the table `purchase_master` */

insert  into `purchase_master`(`pm_id`,`user_id`,`com_id`,`total_amount`,`date`,`assigned_agent`,`delivery_status`) values (1,1,3,'5023','2020-02-18','2','Delivered'),(2,1,4,'2','2020-03-05','3','Processing'),(3,1,4,'2','2020-03-05','3','Processing'),(4,1,4,'2','2020-03-05','3','Processing'),(5,1,4,'1','2020-03-05','2','Successfully Delivered'),(6,1,4,'2','2020-03-05','NA','Processing'),(7,1,4,'UST','2020-03-05','NA','Processing'),(8,1,4,'UST','2020-03-05','NA','Processing'),(9,1,4,'20092','2020-03-05','NA','Processing'),(10,1,4,'10046','2020-03-05','NA','Processing'),(11,1,4,'10046','2020-03-05','NA','Processing'),(12,1,4,'10046','2020-03-05','NA','Processing'),(13,1,4,'10046','2020-03-05','NA','Processing'),(14,1,4,'10046','2020-03-05','NA','Processing'),(15,1,4,'10046','2020-03-05','NA','Processing'),(16,1,4,'15069','2020-03-05','NA','Processing'),(17,1,4,'15069','2020-03-05','NA','Processing'),(18,1,4,'20092','2020-03-05','NA','Processing');

/*Table structure for table `route_assign` */

DROP TABLE IF EXISTS `route_assign`;

CREATE TABLE `route_assign` (
  `assign_id` int(11) NOT NULL AUTO_INCREMENT,
  `agent_id` int(11) DEFAULT NULL,
  `route_id` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`assign_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `route_assign` */

insert  into `route_assign`(`assign_id`,`agent_id`,`route_id`,`date`) values (1,2,1,'2020-02-15'),(2,3,1,'2020-02-15'),(3,2,1,'2020-02-18');

/*Table structure for table `routes` */

DROP TABLE IF EXISTS `routes`;

CREATE TABLE `routes` (
  `route_id` int(11) NOT NULL AUTO_INCREMENT,
  `route_name` varchar(50) DEFAULT NULL,
  `route_des` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`route_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `routes` */

insert  into `routes`(`route_id`,`route_name`,`route_des`) values (1,'Kaloor','asdfghjkl asdfghjk');

/*Table structure for table `stock` */

DROP TABLE IF EXISTS `stock`;

CREATE TABLE `stock` (
  `stock_id` int(11) NOT NULL AUTO_INCREMENT,
  `com_id` int(11) DEFAULT NULL,
  `pro_id` int(11) DEFAULT NULL,
  `availability` varchar(50) DEFAULT NULL,
  `updated_date` date DEFAULT NULL,
  PRIMARY KEY (`stock_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

/*Data for the table `stock` */

insert  into `stock`(`stock_id`,`com_id`,`pro_id`,`availability`,`updated_date`) values (1,4,1,'90','2020-02-18'),(2,4,1,'666','2020-02-18'),(3,4,1,'5224','2020-02-18'),(4,4,1,'52','2020-02-18'),(5,4,3,'1224','2020-02-18'),(6,4,3,'123','2020-02-18'),(7,4,3,'12332','2020-02-18'),(8,4,3,'1230','2020-02-18'),(9,4,1,'10','2020-02-18'),(10,4,1,'10','2020-02-18'),(11,4,3,'450','2020-02-18'),(12,4,1,'444','2020-02-18');

/*Table structure for table `user_waste_collection` */

DROP TABLE IF EXISTS `user_waste_collection`;

CREATE TABLE `user_waste_collection` (
  `waste_id` int(11) NOT NULL AUTO_INCREMENT,
  `request_id` int(11) DEFAULT NULL,
  `agent_id` int(11) DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  `quantity` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`waste_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `user_waste_collection` */

insert  into `user_waste_collection`(`waste_id`,`request_id`,`agent_id`,`datetime`,`quantity`) values (1,2,0,'2020-03-04 00:00:00','10');

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `log_id` int(11) DEFAULT NULL,
  `fname` varchar(50) DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `hname` varchar(50) DEFAULT NULL,
  `place` varchar(50) DEFAULT NULL,
  `pincode` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `users` */

insert  into `users`(`user_id`,`log_id`,`fname`,`lname`,`phone`,`email`,`hname`,`place`,`pincode`) values (1,9,'Baby','Daniel','9656323076','as@asd.as','Modiyil','Kollakadavu','690509'),(2,10,'Susan','Baby','9656323076','as@asd.as','Modiyil','Kollakadavu','690509'),(3,12,'sfh','fhi','12354890','fjo@gk.com','gjk','dgj','1234590');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
