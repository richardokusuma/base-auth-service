package com.difinite.oauth.repos;

import com.difinite.oauth.model.UserPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by chilly98 on 11/02/19.
 */
public interface UserPasswordRepo extends JpaRepository<UserPassword, Long> {
    @Query(value = "SELECT * FROM users.usr_passwd WHERE usr_profile_id = :profId ORDER BY created DESC LIMIT 1", nativeQuery = true)
    UserPassword findFirstByUserProfile(@Param("profId") String profId);
    @Query(value = "SELECT * FROM users.usr_passwd WHERE usr_profile_id = :profId ORDER BY created DESC LIMIT 3", nativeQuery = true)
    List<UserPassword> findThreeByUserProfile(@Param("profId") String profId);
}
