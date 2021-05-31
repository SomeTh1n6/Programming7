package com.utility;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class UserData implements Serializable {

    private String login;
    private String password;
    private AuthMode authMode;

    public UserData(String login, String password,AuthMode authMode){
        this.login = login;
        this.password = password;
        this.authMode = authMode;
    }

    public String getLogin() {
        return login;
    }

    public AuthMode getAuthMode() {
        return authMode;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return authMode == userData.authMode &&
                Objects.equals(login, userData.login) &&
                Objects.equals(password, userData.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authMode, login, password);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "mode=" + authMode +
                ", login='" + login + '\'' +
                ", password=" + password +
                '}';
    }
}
