package com.adam.imgpicker.core;


import com.adam.imgpicker.entity.ImageItem;

import java.util.List;

/**
 * 选择图片的回调
 * Created by yu on 2017/4/17.
 */
public interface OnSelectListener {
    // 选择成功，单选时只有1张
    void onSelect(List<ImageItem> data);

    void onSelectFail(String msg);

    void onSelectImageCancel();
}