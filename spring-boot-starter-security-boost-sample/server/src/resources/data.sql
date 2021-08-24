create table my_user(
     id integer not null auto_increment,
     name char(32) not null,
     password char(60) not null,
     role varchar(128) not null,
     remark varchar(255) not null,
     enable integer not null,
     primary key( id )
);

insert into my_user(id,name,role,remark,enable,password) values
(10001,'admin','admin','',1,'$2a$12$WtxiMJuXjgzCpa1OWT8hR.wMpxq0DbeF1fMpCJbdzCdhdYte1ZtfC'),
(10002,'fish','clerk','',1,'$2a$12$WtxiMJuXjgzCpa1OWT8hR.wMpxq0DbeF1fMpCJbdzCdhdYte1ZtfC');