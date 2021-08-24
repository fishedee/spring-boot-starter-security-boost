package com.fishedee.erp.framework.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fishedee.erp.user.business.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by fish on 2021/4/26.
 */
//UserDetail需要序列化的,要确保每个字段都可以被序列化.
public class MyUserDetail implements UserDetails {
    private static final long serialVersionUID = 4359709211352400087L;

    private String name;

    @JsonIgnore
    private String password;

    private Long userId;

    private User.Role role;

    private User.IsEnabled isEnabled;

    public MyUserDetail(User user){
        this.name = user.getName();
        this.password = user.getPassword();
        this.userId = user.getId();
        this.role = user.getRole();
        this.isEnabled = user.getIsEnabled();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.toString()));
        return authorities;
    }

    public Long getUserId(){
        return this.userId;
    }

    public String getPassword(){
        return this.password;
    }

    public String getUsername(){
        return this.name;
    }

    public String getName(){return this.name;}

    public User.Role getRole(){
        return this.role;
    }

    public User.IsEnabled getIsEnabled(){return this.isEnabled;}

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return this.isEnabled == User.IsEnabled.ENABLE;
    }

    @Override
    public boolean equals(Object obj){
        if( obj instanceof MyUserDetail ){
            return this.getUsername().equals(((MyUserDetail) obj).getUsername());
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return this.getUsername().hashCode();
    }
}
