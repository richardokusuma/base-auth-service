package com.difinite.oauth.repos;

import com.difinite.oauth.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by chilly98 on 11/02/19.
 */
public interface UserProfileRepo extends JpaRepository<UserProfile, String> {
    UserProfile findByUsername(String username);
    UserProfile findByEmail(String email);
    @Query(value = "SELECT COUNT(email) FROM usr_profile WHERE email=:email", nativeQuery = true)
    int amountBasedEmail(@Param("email") String email);
    @Query(value = "SELECT COUNT(username) FROM usr_profile WHERE username=:username", nativeQuery = true)
    int amountBasedUsername(@Param("username") String username);
}
