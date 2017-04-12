package com.beini.net.request;

/**
 * Created by beini on 2017/4/12.
 */
public class UserRequest extends BaseRequestJson{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
