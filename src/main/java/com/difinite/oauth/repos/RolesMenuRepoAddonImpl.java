package com.difinite.oauth.repos;

import com.difinite.oauth.model.RoleScope;
import com.difinite.oauth.model.UserRoles;
import com.difinite.oauth.model.UserProfile;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Richardo Kusuma on 2/18/2019.
 */

@Repository
@Transactional(readOnly=true)
public class RolesMenuRepoAddonImpl implements RolesMenuRepoAddon {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserProfileRepo userProfileRepo;

    @Autowired
    UserRolesRepo userRolesRepo;

    class roleScope{
        public String role;
        public String scope;
        public roleScope(String role,String scope){this.role = role; this.scope = scope;}
    }

    @Override
    public List getRolesMenu(String userProfileId) {

        UserProfile userProfileToFind = userProfileRepo.findById(userProfileId).get();
        List<UserRoles> userRolesToFind = userRolesRepo.findByUsrprofile(userProfileToFind);
        List rolesMenus = new ArrayList<RoleScope>();
        for(UserRoles roles : userRolesToFind){
            Query query = entityManager.createNativeQuery("SELECT menu.menu FROM roles, menu, roles_menu\n" +
                    "WHERE roles_menu.roles_id = roles.id AND roles_menu.menu_id = menu.id AND roles.id = :roles_id");
            query.setParameter("roles_id", roles.getRoles().getId());
            List fetched = query.getResultList();
            for(int i = 0; i < fetched.size(); i++){
                rolesMenus.add(fetched.get(i));
            }
        }

        // remove duplicate
        Set<String> set = new LinkedHashSet<>();
        set.addAll(rolesMenus);
        rolesMenus.clear();
        rolesMenus.addAll(set);
        return rolesMenus;
    }

    @Getter
    @Setter
    class RolesMenu{
        String role;
        String menu;
    }
}
