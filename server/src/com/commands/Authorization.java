package com.commands;

import com.utility.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Авторизация клиента на сервере и занесение информации в БД
 */
public class Authorization {
    private final Connection connection;;
    private String username;
    private String password;

    public Authorization(Connection connection) {
        this.connection = connection;
    }

    public String auth(UserData userData){
        String response;
        username = userData.getLogin();
        password = userData.getPassword();
        switch (userData.getAuthMode()){
            case CREATE:
                response = createUser();
                break;
            case LOG_IN:
                response = loginUser();
                break;
            default:
                response = "Ошибка при попытке авторизации";
        }
        return response;
    }

    private String loginUser(){
        String s = "";
        try {
            PreparedStatement prst = connection.prepareStatement("SELECT COUNT(*) FROM auth WHERE login = ? AND password =? LIMIT 1;");
            prst.setString(1, username);
            prst.setString(2,password);
            ResultSet rs = prst.executeQuery();
            rs.next();
            if (rs.getInt(1)==1){
                s = "Вы успешно вошли";
            }else {
                s = "Неправильный логин или пароль";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return s;
    }

    private String createUser(){
        String response = "";
        try {
            PreparedStatement prst = connection.prepareStatement("INSERT INTO auth(login, password) VALUES (?,?);");
            prst.setString(1, username);
            prst.setString(2,password);
            prst.executeUpdate();
            response = "Вы успешно зарегистрировались!";
        } catch (SQLException e) {

            if (e.getLocalizedMessage().contains("duplicate key value")){
                response = "Вы уже зарегистрированы";
            }else {
                response = e.getMessage();
            }
        }
        return response;
    }
}
