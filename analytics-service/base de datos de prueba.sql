create database greenhouse_management_service;
use greenhouse_management_service;

create table greenhouse(
id int primary key auto_increment,
name varchar(30)
);

create database sensor_management_service;
use sensor_management_service;

create table sensor(
id int primary key auto_increment,
greenhouse_id int
);

create table register(
time_stamp timestamp,
temperature float,
humidity float,
sensor_id int,
foreign key (sensor_id) references sensor(id)
);

USE greenhouse_management_service;

INSERT INTO greenhouse(name)
VALUES ('Greenhouse A');


-- sensor_management_service
USE sensor_management_service;

INSERT INTO sensor(greenhouse_id)
VALUES (1);


USE sensor_management_service;

INSERT INTO register(time_stamp, temperature, humidity, sensor_id)
WITH RECURSIVE days AS (
    SELECT 0 AS n
    UNION ALL
    SELECT n+1 FROM days WHERE n < 29
),
hours AS (
    SELECT 8 AS h UNION
    SELECT 10 UNION
    SELECT 12 UNION
    SELECT 14 UNION
    SELECT 16 UNION
    SELECT 20 UNION
    SELECT 22 UNION
    SELECT 0 UNION
    SELECT 2 UNION
    SELECT 4
)
SELECT
    TIMESTAMP(DATE_ADD('2026-04-01', INTERVAL d.n DAY), MAKETIME(h.h,0,0)),
    
    CASE 
        WHEN h.h BETWEEN 8 AND 16 
            THEN 29 + (h.h/8)       -- ~29–33°C día
        ELSE 
            20 + (h.h/20)           -- ~20–24°C noche
    END,
    
    CASE 
        WHEN h.h BETWEEN 8 AND 16 
            THEN 55 - (h.h/10)      -- ~48–55% día
        ELSE 
            70 + (h.h/30)           -- ~66–73% noche
    END,
    
    1
FROM days d
CROSS JOIN hours h;