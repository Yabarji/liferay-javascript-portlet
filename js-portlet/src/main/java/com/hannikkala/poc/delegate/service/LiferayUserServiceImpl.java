package com.hannikkala.poc.delegate.service;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tommi Hännikkälä tommi@hannikkala.com
 * Date: 29/02/16
 * Time: 10:18
 */
@Service
public class LiferayUserServiceImpl {

    public String[] listRolesForUser(User user) {
        List<String> authorities = new ArrayList<>();
        try {
            for (Role role : user.getRoles()) {
                String rolename = role.getName().toUpperCase().replaceAll(" ", "_");
                authorities.add("ROLE_" + rolename);
            }
        } catch (SystemException e) {

        }
        return authorities.toArray(new String[0]);
    }
}
