package com.example.administrator.baseapp.bean;

import java.util.List;

/**
 * Created by beini on 2017/2/18.
 */

public class BaseBean<T> {
    private int id;
    private List<T> baseList;

    public BaseBean(int id, List<T> baseList) {
        this.id = id;
        this.baseList = baseList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<T> getBaseList() {
        return baseList;
    }

    public void setBaseList(List<T> baseList) {
        this.baseList = baseList;
    }
}
