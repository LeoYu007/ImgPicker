package com.adam.imgpicker.core;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * 框架不限定使用的图片加载器，需要自己实现
 *
 * @author lyu
 */
public interface ImageLoader extends Serializable {
    void displayImage(Context context, String path, ImageView imageView);
}