package com.difinite.oauth.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "password_reset_token")
@Data
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String token;
    @Column(name="email")
    public String email;
    @Column(name = "exp_date")
    public Date expiryDate;
}
