package com.example.myapp;

import java.util.ArrayList;
import java.util.List;

public class User {

    String username, phone;
    int id;
    public ArrayList<ArrayList<Integer>> cars;

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public int getId() {
        return id;
    }

    public ArrayList<ArrayList<Integer>> getCars() {
        return cars;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", id=" + id +
                ", cars=" + cars +
                '}';
    }
}
