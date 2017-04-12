package com.beini.net.request;

/**
 * Created by beini on 2017/4/12.
 */
public class PageRequest extends BaseRequestJson {
    private Integer start;
    private Integer num;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
