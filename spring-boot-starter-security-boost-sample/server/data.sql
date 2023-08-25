drop database if exists test1;
create database test1;
use test1;

create table my_user(
     id integer not null auto_increment,
     name char(32) not null,
     password char(60) not null,
     roles varchar(128) not null,
     remark varchar(255) not null,
     is_enabled char(16) not null,
     primary key( id )
);

create table persistent_logins(
      username varchar(64) not null,
      series varchar(64) not null,
      token varchar(64) not null,
      last_used datetime not null,
      primary key(series)
);


insert into my_user(id,name,roles,remark,is_enabled,password) values
(10001,'admin','admin','','ENABLE','$2a$12$WtxiMJuXjgzCpa1OWT8hR.wMpxq0DbeF1fMpCJbdzCdhdYte1ZtfC'),
(10002,'fish','clerk','','ENABLE','$2a$12$WtxiMJuXjgzCpa1OWT8hR.wMpxq0DbeF1fMpCJbdzCdhdYte1ZtfC'),
(10003,'cat','admin','','ENABLE','$2a$12$WtxiMJuXjgzCpa1OWT8hR.wMpxq0DbeF1fMpCJbdzCdhdYte1ZtfC');

drop database if exists test2;
create database test2;
use test2;

create table my_user(
    id integer not null auto_increment,
    name char(32) not null,
    password char(60) not null,
    roles varchar(128) not null,
    remark varchar(255) not null,
    is_enabled char(16) not null,
    primary key( id )
);

create table persistent_logins(
          username varchar(64) not null,
          series varchar(64) not null,
          token varchar(64) not null,
          last_used datetime not null,
          primary key(series)
);


insert into my_user(id,name,roles,remark,is_enabled,password) values
(10001,'admin','admin','','ENABLE','$2a$12$WtxiMJuXjgzCpa1OWT8hR.wMpxq0DbeF1fMpCJbdzCdhdYte1ZtfC'),
(10002,'fish','clerk','','ENABLE','$2a$12$WtxiMJuXjgzCpa1OWT8hR.wMpxq0DbeF1fMpCJbdzCdhdYte1ZtfC');