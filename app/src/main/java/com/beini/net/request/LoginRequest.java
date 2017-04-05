package com.beini.net.request;


/**
 * Created by beini on 2017/2/10.
 */

public class LoginRequest extends BaseRequestJson {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
