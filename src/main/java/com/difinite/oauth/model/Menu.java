package com.difinite.oauth.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by chilly98 on 11/02/19.
 */
@Entity
@Data
@Table(name="menu", schema = "users")
public class Menu implements Serializable {
    @Id
    public Long id;
    public String menu;
}
