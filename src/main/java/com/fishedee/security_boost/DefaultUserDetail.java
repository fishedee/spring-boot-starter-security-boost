package com.fishedee.security_boost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
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
@Data
public class DefaultUserDetail implements UserDetails {
    private static final long serialVersionUID = 4359709211352400087L;

    private String id;

    private String name;

    @JsonIgnore
    private String password;

    private String roles;

    private int is_enabled;

    public DefaultUserDetail(){
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities(){
        String[] roleList = this.roles.split(",");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for( int i = 0 ;i != roleList.length;i++){
            String single = roleList[i].trim();
            if( single.length() == 0 ){
                continue;
            }
            authorities.add(new SimpleGrantedAuthority(single));
        }
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername(){
        return this.name;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled(){
        return this.is_enabled == 1;
    }

    @Override
    public boolean equals(Object obj){
        if( obj instanceof DefaultUserDetail){
            return this.getUsername().equals(((DefaultUserDetail) obj).getUsername());
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return this.getUsername().hashCode();
    }
}
