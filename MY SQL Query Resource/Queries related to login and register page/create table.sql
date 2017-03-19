use applogindb;

create table if not exists users(
 uid int(11) AUTO_INCREMENT NOT NULL,
 unique_id varchar(23) NOT NULL UNIQUE,
 name varchar(50) NOT NULL,
 email varchar(100) NOT NULL UNIQUE,
 encrpted_password varchar(10) NOT NULL,
 salt varchar(10) NOT NULL,
 created_at datetime,
 updated_at datetime NULL,
 CONSTRAINT constraint_primary_key PRIMARY KEY(uid)

 );
