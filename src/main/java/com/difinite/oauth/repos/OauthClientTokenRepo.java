package com.difinite.oauth.repos;

import com.difinite.oauth.model.OauthClientToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by chilly98 on 11/02/19.
 */
public interface OauthClientTokenRepo extends JpaRepository<OauthClientToken, Long> {
    OauthClientToken findByToken(byte[] token);
}
