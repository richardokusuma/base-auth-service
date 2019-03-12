package com.difinite.oauth.repos;

import com.difinite.oauth.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    @Query(value = "SELECT count(token) FROM password_reset_token where token=:token", nativeQuery = true)
    int amountBasedToken(@Param("token") String token);
}
