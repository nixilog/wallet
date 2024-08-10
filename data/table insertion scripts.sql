INSERT INTO `wallet`.`account`
(`account_name`,`balance`,`version`)
VALUES
("nikko",50000.0,0),
("client715",780000.0,0),
("client964",163700.0,0),
("client1082",410000.0,0),
("client2867",620000.0,0),
("client3319",380000.0,0);

INSERT INTO `wallet`.`transaction`
(`amount`,`destination`,`destination_id`,`source`,`source_id`,`timestamp`,`version`)
VALUES
(4000,"client715",2,"nikko",1,"2020-01-19 03:14:07",0),
(20000,"nikko",1,"client715",2,"2022-09-07 09:48:29",0);