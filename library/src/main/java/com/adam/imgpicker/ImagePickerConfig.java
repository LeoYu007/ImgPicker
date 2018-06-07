package com.adam.imgpicker;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;

import com.adam.imgpicker.core.ImageLoader;
import com.adam.imgpicker.core.OnSelectListener;

import java.io.Serializable;

/**
 * 配置
 * Created by yu on 2017/4/13.
 */
public class ImagePickerConfig {

    public int limited;                         //  最多选择图片数
    public boolean showCamera;                  //  第一个item是否显示相机

    public int backResId;                       //  返回按钮图标
    public String titleText;                    //  标题
    public int titleBarColor;                   //  titlebar背景色
    public int titleTextColor;                  //  标题颜色
    public int btnTextColor;                    //  确定按钮文字颜色
    public int btnResId;                        //  确定按钮背景色.

    public ImageLoader loader;                  //  自定义图片加载器
    public OnSelectListener listener;           //  选择完成的回调;

    public boolean needCrop;                    //  是否裁剪，仅单选有效
    public int aspectX;                         //  裁剪输出大小↓↓↓↓
    public int aspectY;
    public int outputX;
    public int outputY;

    public boolean compress;                    //  是否压缩
    public float maxWidth;                      //  压缩参数↓↓↓↓
    public float maxHeight;
    public int quality;

    public ImagePickerConfig(Builder builder) {
        this.needCrop = builder.needCrop;
        this.limited = builder.limited;
        this.showCamera = builder.showCamera;
        this.backResId = builder.backResId;
        this.titleText = builder.titleText;
        this.titleBarColor = builder.titleBarColor;
        this.titleTextColor = builder.titleTextColor;
        this.btnResId = builder.btnResId;
        this.btnTextColor = builder.btnTextColor;
        this.loader = builder.loader;
        this.aspectX = builder.aspectX;
        this.aspectY = builder.aspectY;
        this.outputX = builder.outputX;
        this.outputY = builder.outputY;
        this.listener = builder.listener;
        this.compress = builder.compress;
        this.maxWidth = builder.maxWidth;
        this.maxHeight = builder.maxHeight;
        this.quality = builder.quality;
    }

    public static class Builder implements Serializable {

        private boolean needCrop = false;
        private int limited = 9;
        private boolean showCamera = true;
        private int backResId = -1;
        private String titleText;
        private int titleTextColor = -1;
        private int titleBarColor = -1;
        private int btnTextColor = -1;
        private int btnResId = -1;
        private ImageLoader loader;
        private OnSelectListener listener;

        private int aspectX = 1;
        private int aspectY = 1;
        private int outputX = 400;
        private int outputY = 400;

        private boolean compress = true;
        private float maxWidth = 720f;
        private float maxHeight = 960f;
        private int quality = 80;

        public Builder() {
        }

        public Builder compress(boolean compress) {
            this.compress = compress;
            return this;
        }

        public Builder maxWidthAndHeight(float maxWidth, float maxHeight) {
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
            return this;
        }

        public Builder quality(@IntRange(from = 1, to = 100) int quality) {
            this.quality = quality;
            return this;
        }

        public Builder imageLoader(ImageLoader loader) {
            this.loader = loader;
            return this;
        }

        public Builder callback(OnSelectListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder needCrop(boolean needCrop) {
            this.needCrop = needCrop;
            return this;
        }

        public Builder limited(@IntRange(from = 1, to = 9) int maxNum) {
            this.limited = maxNum;
            return this;
        }

        public Builder showCamera(boolean needCamera) {
            this.showCamera = needCamera;
            return this;
        }

        public Builder backResId(@DrawableRes int backResId) {
            this.backResId = backResId;
            return this;
        }

        public Builder titleText(String title) {
            this.titleText = title;
            return this;
        }

        public Builder titleTextColor(@ColorInt int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder titleBarColor(@ColorInt int titleBarColor) {
            this.titleBarColor = titleBarColor;
            return this;
        }

        public Builder btnTextColor(@ColorInt int btnTextColor) {
            this.btnTextColor = btnTextColor;
            return this;
        }

        public Builder btnResId(@DrawableRes int btnResId) {
            this.btnResId = btnResId;
            return this;
        }

        public Builder cropSize(int aspectX, int aspectY, int outputX, int outputY) {
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            this.outputX = outputX;
            this.outputY = outputY;
            return this;
        }

        public ImagePickerConfig build() {
            if (loader == null)
                throw new NullPointerException("the ImageLoader is null");
            return new ImagePickerConfig(this);
        }
    }
}
