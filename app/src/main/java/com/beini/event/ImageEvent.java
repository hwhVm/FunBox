package com.beini.event;

import com.beini.ui.fragment.multimedia.bean.ImageBean;

import java.util.List;

/**
 * Created by beini on 2017/3/13.
 */

public class ImageEvent {
    public List<ImageBean> imageBeens;
    public int postiton;

    public ImageEvent(List<ImageBean> imageBeens, int postiton) {
        this.imageBeens = imageBeens;
        this.postiton = postiton;
    }
}
