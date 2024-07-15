CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account_name` varchar(100) DEFAULT NULL,
  `balance` decimal(13,2) DEFAULT NULL,
  `version` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=249 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `transaction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `source_id` int DEFAULT NULL,
  `source` varchar(100) DEFAULT NULL,
  `destination_id` int DEFAULT NULL,
  `destination` varchar(100) DEFAULT NULL,
  `amount` decimal(13,2) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `version` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25171 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
