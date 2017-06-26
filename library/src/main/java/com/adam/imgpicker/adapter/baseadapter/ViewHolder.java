package com.adam.imgpicker.adapter.baseadapter;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;


/**
 * ViewHolder操作子视图的实现类
 *
 * @author yu
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    /**
     * 缓存子视图,key为view id, 值为View。
     */
    private SparseArray<View> mCahceViews = new SparseArray<>();

    public ViewHolder(View itemView) {
        super(itemView);
    }

    public View getItemView() {
        return itemView;
    }

    /**
     * 根据id查找view
     */
    public <T extends View> T findViewById(int viewId) {
        View target = mCahceViews.get(viewId);
        if (target == null) {
            target = itemView.findViewById(viewId);
            mCahceViews.put(viewId, target);
        }
        return (T) target;
    }

    public ImageView findViewAsImageView(int viewId) {
        return findViewById(viewId);
    }

    /**
     * @param viewId
     * @param stringId
     */
    public ViewHolder setText(int viewId, int stringId) {
        TextView textView = findViewById(viewId);
        textView.setText(stringId);
        return this;
    }

    public ViewHolder setText(int viewId, String text) {
        TextView textView = findViewById(viewId);
        textView.setText(text);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int color) {
        TextView textView = findViewById(viewId);
        textView.setTextColor(color);
        return this;
    }

    /**
     * @param viewId
     * @param color
     */
    public ViewHolder setBackgroundColor(int viewId, int color) {
        View target = findViewById(viewId);
        target.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundResource(int viewId, int resId) {
        View target = findViewById(viewId);
        target.setBackgroundResource(resId);
        return this;
    }

    public ViewHolder setBackgroundDrawable(int viewId, Drawable drawable) {
        View target = findViewById(viewId);
        target.setBackgroundDrawable(drawable);
        return this;
    }

    @TargetApi(16)
    public ViewHolder setBackground(int viewId, Drawable drawable) {
        View target = findViewById(viewId);
        target.setBackground(drawable);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView target = findViewById(viewId);
        target.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView target = findViewById(viewId);
        target.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView target = findViewById(viewId);
        target.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Uri uri) {
        ImageView target = findViewById(viewId);
        target.setImageURI(uri);
        return this;
    }

    @TargetApi(16)
    public ViewHolder setImageAlpha(int viewId, int alpha) {
        ImageView target = findViewById(viewId);
        target.setImageAlpha(alpha);
        return this;
    }

    /**
     * @param viewId
     * @param checked
     */
    public ViewHolder setChecked(int viewId, boolean checked) {
        Checkable checkable = findViewById(viewId);
        checkable.setChecked(checked);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = findViewById(viewId);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = findViewById(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setMax(int viewId, int max) {
        ProgressBar view = findViewById(viewId);
        view.setMax(max);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating) {
        RatingBar view = findViewById(viewId);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setSelected(int viewId, boolean isSelected) {
        View view = findViewById(viewId);
        view.setSelected(isSelected);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = findViewById(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setVisibility(int viewId, int visibility) {
        View view = findViewById(viewId);
        view.setVisibility(visibility);
        return this;
    }

    /**
     * @param viewId
     * @param listener
     */
    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = findViewById(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = findViewById(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public ViewHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener) {
        AdapterView view = findViewById(viewId);
        view.setOnItemClickListener(listener);
        return this;
    }

    public ViewHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener listener) {
        AdapterView view = findViewById(viewId);
        view.setOnItemLongClickListener(listener);
        return this;
    }

    public ViewHolder setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener) {
        AdapterView view = findViewById(viewId);
        view.setOnItemSelectedListener(listener);
        return this;
    }
}
