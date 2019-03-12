package com.difinite.oauth.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by chilly98 on 10/02/19.
 */
@Entity
@Data
@Table(name = "usr_profile", schema = "users")
public class UserProfile implements Serializable {
    @Id
    public String id;
    public String email;
    public String username;
}
