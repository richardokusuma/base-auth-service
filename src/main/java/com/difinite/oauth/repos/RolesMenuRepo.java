package com.difinite.oauth.repos;

import com.difinite.oauth.model.RolesMenu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by chilly98 on 11/02/19.
 */
public interface RolesMenuRepo extends JpaRepository<RolesMenu, Long>, RolesMenuRepoAddon {
}
