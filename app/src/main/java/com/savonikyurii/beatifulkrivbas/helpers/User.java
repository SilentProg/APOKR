package com.savonikyurii.beatifulkrivbas.helpers;
import java.io.Serializable;

public class User implements Serializable {

    String username;
    String email;
    int count;

    public User() {
    }

    public User(String username, String email, int count) {
        this.username = username;
        this.email = email;
        this.count = count;
    }

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}