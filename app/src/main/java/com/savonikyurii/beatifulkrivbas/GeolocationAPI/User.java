package com.savonikyurii.beatifulkrivbas.GeolocationAPI;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.AbstractClasses.BasicUser;

import java.io.Serializable;
/*Клас нащадок BasicUser*/
/*Слугує для розширення полями класу предка та зберігання інформації про користувача*/
public class User extends BasicUser implements Serializable {
    /*Поля класу*/
    private int count; //кількість відвіданих місць
    /*Конструктори*/
    public User(){}
    public User(String username, String email, int count) {
        super(username, email);
        this.count = count;}
    /*Геттери та Сеттери*/
    public int getCount() {return count;}
    public void setCount(int count) {this.count = count;}
}