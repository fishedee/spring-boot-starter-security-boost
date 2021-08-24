package com.fishedee.erp.framework.auth;

import com.fishedee.erp.user.business.User;
import com.fishedee.erp.user.infrastructure.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fish on 2021/4/26.
 */
@Component
@Slf4j
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        List<User> user = userRepository.getByNameForRead(username);
        if( user.size() ==  0){
            //这个异常是固定的,不能改其他的
            throw new UsernameNotFoundException("用户不存在");
        }
        return new MyUserDetail(user.get(0));
    }
}
