package com.difinite.oauth.model;

import lombok.Data;
//import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by chilly98 on 11/02/19.
 */
@Entity
@Data
@Table(name = "usr_passwd", schema = "users")
public class UserPassword implements Serializable {
    @Id
    public Long id;
    @ManyToOne
    @JoinColumn(name = "usr_profile_id")
    //@JsonIgnore
    public UserProfile userProfile;
    @CreationTimestamp
    public Date created;
    public String password;
}
