package com.adam.imgpicker;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IntRange;

import com.adam.imgpicker.entity.ImageFolder;
import com.adam.imgpicker.entity.ImageItem;
import com.adam.imgpicker.ui.GridActivity;
import com.adam.imgpicker.util.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ImagePicker入口
 * Created by yu on 2017/4/13.
 */
public class ImagePicker {

    private ImagePickerConfig mConfig;

    private List<ImageFolder> mImageFolders;      // 所有的图片文件夹
    private Set<ImageItem> mSelectedImages = new HashSet<>();   // 选中的图片集合

    public static ImagePicker getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static ImagePicker INSTANCE = new ImagePicker();
    }

    public ImagePickerConfig getConfig() {
        return mConfig;
    }

    public void setConfig(ImagePickerConfig mConfig) {
        this.mConfig = mConfig;
    }

    public List<ImageFolder> getImageFolders() {
        return mImageFolders;
    }

    public void setImageFolders(List<ImageFolder> mImageFolders) {
        this.mImageFolders = mImageFolders;
    }

    public Set<ImageItem> getSelectedImages() {
        return mSelectedImages;
    }

    //  ***********************************************************
    public void open(Activity context) {
        mSelectedImages.clear();
        context.startActivity(new Intent(context, GridActivity.class));
    }

    /**
     * 以新的数量打开图库，比如总共能选9张，第一次选了3张，第二次可以更新数量为6张
     *
     * @param context context
     * @param limit   最多选择的数量
     */
    public void open(Activity context, @IntRange(from = 1, to = 9) int limit) {
        mSelectedImages.clear();
        mConfig.limited = limit;
        context.startActivity(new Intent(context, GridActivity.class));
    }

    /**
     * 释放资源
     */
    public void clear() {
        mImageFolders = null;
        mSelectedImages.clear();
        Utils.deleteTempFile();
    }
}
