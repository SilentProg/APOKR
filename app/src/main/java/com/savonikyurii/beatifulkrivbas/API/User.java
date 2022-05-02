package com.savonikyurii.beatifulkrivbas.API;
import java.io.Serializable;

public class User extends BasicUser implements Serializable {
    private int count;

    public User() {
    }

    public User(String username, String email, int count) {
        super(username, email);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}