create database greenhouse_system_admins;
use greenhouse_system_admins;

create table administrator(
	id int primary key auto_increment,
	username varchar(50),
    password varchar(256)
);

INSERT INTO administrator (username, password)
VALUES ('admin', 'admin');

drop table administrator;
select * from administrator;