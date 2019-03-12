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
@Table(name = "roles_menu", schema = "users")
public class RolesMenu implements Serializable {
    @Id
    public Long id;
    @ManyToOne
    @JoinColumn(name = "roles_id")
  //  @JsonIgnore
    public Roles roles;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    //@JsonIgnore
    public Menu menu;
    public String scopes;
}
