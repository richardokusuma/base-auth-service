package com.difinite.oauth.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.difinite.oauth.model.UserPassword;
import com.difinite.oauth.model.UserRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class MyUserPrincipal implements UserDetails{
    private UserPassword passwords;
    private List<UserRoles> roles;

    public MyUserPrincipal(UserPassword passwords, List<UserRoles> roles) {
        this.passwords = passwords;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List auth = new ArrayList<SimpleGrantedAuthority>();
        for(UserRoles role : roles){
            auth.add(new SimpleGrantedAuthority(role.getRoles().getRole()));
        }
        return auth;
    }

    @Override
    public String getPassword() {
        return passwords.getPassword();
    }

    @Override
    public String getUsername() {
        return passwords.getUserProfile().getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
