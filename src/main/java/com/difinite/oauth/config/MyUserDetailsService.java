package com.difinite.oauth.config;

import com.difinite.oauth.model.UserPassword;
import com.difinite.oauth.model.UserProfile;
import com.difinite.oauth.model.UserRoles;
import com.difinite.oauth.repos.UserPasswordRepo;
import com.difinite.oauth.repos.UserProfileRepo;
import com.difinite.oauth.repos.UserRolesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    @Lazy
    public UserProfileRepo userProfileRepo;

    @Autowired
    @Lazy
    public UserPasswordRepo userPasswordRepo;

    @Autowired
    @Lazy
    public UserRolesRepo userRolesRepo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile userProfile = userProfileRepo.findByUsername(username);
        UserPassword userPassword = userPasswordRepo.findFirstByUserProfile(userProfile.id);
        List<UserRoles> userRoles = userRolesRepo.findByUsrprofile(userProfile);
        return new MyUserPrincipal(userPassword, userRoles);
    }
}
