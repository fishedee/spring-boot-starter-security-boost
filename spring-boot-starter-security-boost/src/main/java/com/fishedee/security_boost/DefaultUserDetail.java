package com.fishedee.security_boost;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created by fish on 2021/4/26.
 */
//UserDetail需要序列化的,要确保每个字段都可以被序列化.
@Data
public class DefaultUserDetail implements UserDetails {
    private static final long serialVersionUID = 4359709211352400087L;

    private String tenantId;

    private String sceneId;

    private String id;

    private String name;

    @JsonIgnore
    private String password;

    private String roles;

    private String is_enabled;

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
        return this.is_enabled.equalsIgnoreCase("enable");
    }

    public String getSceneId(){
        if( this.sceneId == null ){
            return "";
        }
        return this.sceneId;
    }

    @Override
    public boolean equals(Object obj){
        if( obj instanceof DefaultUserDetail){
            DefaultUserDetail right = (DefaultUserDetail)obj;
            if( this.getUsername().equals(right.getUsername()) == false ){
                return false;
            }
            String tenantId = this.getTenantId();
            if( tenantId == null ){
                return right.getTenantId() == null;
            }
            if( SecurityBoostConfiguration.unLimitSessionTenantSet.contains(tenantId)){
                //当这个租户不限制同时登录人数的时候，equals总是返回false
                //以保证同一个用户可以多次登录
                return false;
            }
            if( tenantId.equals(right.getTenantId()) == false ){
                return false;
            }
            //登录场景校验
            if( this.getSceneId().equals(right.getSceneId()) == false ){
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        String link = "";
        String tenantId = "0";
        if( this.getTenantId() != null ){
            tenantId = this.getTenantId();
        }
        String sceneId = this.getSceneId();
        link = tenantId+"#"+sceneId+"#"+this.getUsername();
        return link.hashCode();
    }
}
