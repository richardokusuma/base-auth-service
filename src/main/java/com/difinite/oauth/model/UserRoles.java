package com.difinite.oauth.model;

import lombok.Data;
//import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by chilly98 on 11/02/19.
 */

@Entity
@Data
@Table(name = "usr_roles", schema = "users")
public class UserRoles implements Serializable {
    @Id
    public Long id;
    @ManyToOne
    @JoinColumn(name = "usr_profile_id")
   // @JsonIgnore
    public UserProfile usrprofile;
    @OneToOne
    @JoinColumn(name = "roles_id")
    //@JsonIgnore
    public Roles roles;
}
