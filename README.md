![Release](https://jitpack.io/v/fishedee/spring-boot-starter-security-boost.svg)
(https://jitpack.io/#fishedee/spring-boot-starter-security-boost)

# security_boost

SpringBoost的Security工具库，功能包括有：

* ajax，默认配置就支持ajax登录，前后端分离的REST项目
* remember-me，提供下次自动登录的功能
* csrf，防csrf攻击
* session会话数量限制，默认一个用户同时只能用一个会话

## 安装

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.fishedee</groupId>
    <artifactId>spring-boot-starter-security-boost</artifactId>
    <version>1.1</version>
</dependency>
```

在项目的pom.xml加入以上配置即可

## 使用

```ini
spring.security-boost.enable=true
spring.security-boost.user-table= my_user
```

application.properties的配置

```sql
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
```

登录表，以及自动登录记录表

```java
package com.fishedee.security_boost.sample;

import com.fishedee.security_boost.SecurityBoostConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfiguration extends SecurityBoostConfiguration {
}
```

打开SecurityConfiguration，需要自定义的用户进行相关方法的覆盖就可以了

```bash
POST /login/login
表单:user=admin&password=123&remember-me=false
```

登录接口

```bash
POST /login/logout
```

登出接口

```bash
GET /login/islogin
```

查询登录态接口
