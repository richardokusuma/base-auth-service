package com.difinite.oauth.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by chilly98 on 11/02/19.
 */

@Entity
@Data
@Table(name="oauth_client_token", schema = "users")
public class OauthClientToken {
    @Id
    @Column(name = "token_id")
    public String tokenId;
    @Lob
    @Column(length=100000)
    private byte[] token;
    @Column(name = "authentication_id")
    public String authenticationId;
    @Column(name = "user_name")
    public String userName;
    @Column(name = "client_id")
    public String clientId;
}
