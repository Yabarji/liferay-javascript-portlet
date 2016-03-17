package com.hannikkala.poc.delegate.controller;

import com.hannikkala.poc.delegate.service.LiferayUserServiceImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 29/02/16
 * Time: 10:32
 */
@Controller
@RequestMapping("/rest/me")
public class MeController {

    @Autowired
    private LiferayUserServiceImpl liferayUserService;

    private static final Log _log = LogFactoryUtil.getLog(MeController.class);

    @Value("${jwt.secretkey}")
    private String jwtSecretkey;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> token(HttpServletRequest request) throws SystemException, PortalException {
        User user = PortalUtil.getUser(request);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String[] rolesForUser = liferayUserService.listRolesForUser(user);
        String jwt = Jwts.builder().setSubject(user.getScreenName())
                .claim("roles", rolesForUser).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, jwtSecretkey).compact();
        return ResponseEntity.ok(new TokenResponse(jwt, user.getEmailAddress(), rolesForUser));
    }

    public static class TokenResponse implements Serializable {
        private String jwttoken;
        private String user;
        private String[] roles;

        public TokenResponse(String jwttoken, String user, String[] roles) {
            this.jwttoken = jwttoken;
            this.user = user;
            this.roles = roles;
        }

        public String getJwttoken() {
            return jwttoken;
        }

        public void setJwttoken(String jwttoken) {
            this.jwttoken = jwttoken;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }
    }
}
