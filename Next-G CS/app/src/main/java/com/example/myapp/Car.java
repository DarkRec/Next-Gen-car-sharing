package com.example.myapp;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Car {


    public static class BasicUser{
        BasicUser(String name, int id){
            this.name = name;
            this.id = id;
        }

        public String name;
        private int id;

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "BasicUser{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    '}';
        }
    }
    public class Info{
        private String color;
        private String brand;
        private String model;
        private String license_plate;

        public String getColor() {
            return color;
        }

        public String getBrand() {
            return brand;
        }

        public String getModel() {
            return model;
        }

        public String getLicense_plate() {
            return license_plate;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "color='" + color + '\'' +
                    ", brand='" + brand + '\'' +
                    ", model='" + model + '\'' +
                    ", license_plate='" + license_plate + '\'' +
                    '}';
        }
    }
    private Integer id;
    private BasicUser owner;
    private String custom_name;
    private ArrayList<ArrayList<BasicUser>> users;
    private boolean available;
    private Info info;

    public Integer getId() {
        return id;
    }

    public BasicUser getOwner() {
        return owner;
    }

    public String getCustom_name() {
        return custom_name;
    }

    public ArrayList<ArrayList<BasicUser>> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<ArrayList<BasicUser>> users) {
        this.users = users;
    }
    public boolean isAvailable() {
        return available;
    }

    public Info getInfo() {
        return info;
    }


    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", owner=" + owner +
                ", custom_name='" + custom_name + '\'' +
                ", users=" + users +
                ", available=" + available +
                ", info=" + info +
                '}';
    }
}
