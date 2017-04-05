package com.beini.bean.green;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by beini on 2017/4/5.
 */
@Entity
public class User {
    @Id(autoincrement = true)//自增长
    private Long id;

    @NonNull
    @Property(nameInDb = "USERNAME")
    private String username;

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 588070699)
    public User(Long id, @NonNull String username) {
        this.id = id;
        this.username = username;
    }
    @Generated(hash = 586692638)
    public User() {
    }
}
