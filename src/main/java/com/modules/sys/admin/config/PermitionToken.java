package com.modules.sys.admin.config;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 */
public class PermitionToken implements AuthenticationToken {

    private String token;

    public PermitionToken(String token) {
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
