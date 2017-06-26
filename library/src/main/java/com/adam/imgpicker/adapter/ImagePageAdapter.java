package com.adam.imgpicker.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.adam.imgpicker.ImagePicker;
import com.adam.imgpicker.ImagePickerConfig;
import com.adam.imgpicker.entity.ImageItem;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 预览大图的viewpager适配器
 * Created by yu on 2017/4/14.
 */
public class ImagePageAdapter extends PagerAdapter {

    private ImagePickerConfig config;
    private List<ImageItem> images = new ArrayList<>();
    private Activity mActivity;
    private PhotoViewClickListener listener;

    public ImagePageAdapter(Activity activity, List<ImageItem> images) {
        this.mActivity = activity;
        this.images = images;

        ImagePicker imagePicker = ImagePicker.getInstance();
        config = imagePicker.getConfig();
    }

    public void refreshData(List<ImageItem> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public void setPhotoViewClickListener(PhotoViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mActivity);
        ImageItem imageItem = images.get(position);
        config.loader.displayImage(mActivity, imageItem.path, photoView);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (listener != null)
                    listener.OnPhotoTapListener(view, x, y);
            }
        });
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface PhotoViewClickListener {
        void OnPhotoTapListener(View view, float v, float v1);
    }
}
