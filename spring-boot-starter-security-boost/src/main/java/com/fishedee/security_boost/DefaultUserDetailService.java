package com.fishedee.security_boost;

import com.fishedee.security_boost.autoconfig.SecurityBoostProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Types;
import java.util.List;

/**
 * Created by fish on 2021/4/26.
 */
@Slf4j
public class DefaultUserDetailService implements UserDetailsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SecurityBoostProperties securityBoostProperties;

    private String selectByNameSql;

    @PostConstruct
    public void init(){
        selectByNameSql = String.format("select id,name,password,roles,enabled from %s where name = ?",securityBoostProperties.getUserTable());
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        List<DefaultUserDetail> userList = jdbcTemplate.query(this.selectByNameSql,
                new Object[]{username},
                new int[]{Types.VARCHAR},
                new BeanPropertyRowMapper(DefaultUserDetail.class));
        if( userList.size() ==  0){
            //这个异常是固定的,不能改其他的
            throw new UsernameNotFoundException("用户不存在");
        }
        return userList.get(0);
    }
}
