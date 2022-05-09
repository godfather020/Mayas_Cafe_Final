package com.example.mayasfood.constants;

public class UserHelper {

    String name, phonenum;

    public UserHelper(){}

    public UserHelper(String name, String phonenum) {
        this.name = name;
        this.phonenum = phonenum;
    }

    public UserHelper(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }
}
