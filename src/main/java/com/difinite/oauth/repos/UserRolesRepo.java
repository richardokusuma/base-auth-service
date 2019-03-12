package com.difinite.oauth.repos;

import com.difinite.oauth.model.UserRoles;
import com.difinite.oauth.model.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by chilly98 on 11/02/19.
 */
public interface UserRolesRepo extends CrudRepository<UserRoles, Long> {
    List<UserRoles> findByUsrprofile(UserProfile usr_profile);
}
