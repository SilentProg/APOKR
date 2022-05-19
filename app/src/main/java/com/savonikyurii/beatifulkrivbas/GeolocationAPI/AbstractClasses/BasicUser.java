package com.savonikyurii.beatifulkrivbas.GeolocationAPI.AbstractClasses;

import java.io.Serializable;

/*Фундаментальний клас BasicUser*/
/*Призначений для зберігання базової інформації про користувача*/
public abstract class BasicUser implements Serializable {
    /*Поля класу*/
    private String username; //логін
    private String email;   //електронна пошта
    /*Конструктори*/
    public BasicUser() {}
    public BasicUser(String username, String email) {
        this.username = username;
        this.email = email;}
    /*Методи встановлення та отримання полів класу*/
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
