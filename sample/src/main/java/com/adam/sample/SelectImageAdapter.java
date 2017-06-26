package com.adam.sample;

import android.content.Context;

import com.adam.imgpicker.adapter.baseadapter.RecyclerAdapter;
import com.adam.imgpicker.adapter.baseadapter.ViewHolder;
import com.adam.imgpicker.entity.ImageItem;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 仿微信图片选择, 当数量少于最大可选的数量，会自动在最后添加一个加号item
 * Created by lyu on 2017/4/20.
 */
public class SelectImageAdapter extends RecyclerAdapter<ImageItem> {

    public static final int ITEM_TYPE_IMAGE = 0;
    public static final int ITEM_TYPE_ADD = 1;// 加号

    private Context context;
    private int maxSize = 9;

    public SelectImageAdapter(Context context, List<ImageItem> data, int maxSize) {
        super(data, R.layout.item_image, R.layout.item_image_add);
        this.context = context;
        this.maxSize = maxSize;
    }

    @Override
    public int getItemCount() {
        if (mDataSet.size() < maxSize)
            return mDataSet.size() + 1;
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDataSet.size())
            return ITEM_TYPE_ADD;
        return ITEM_TYPE_IMAGE;
    }

    @Override
    public ImageItem getItem(int position) {
        if (position >= mDataSet.size())
            return null;
        return mDataSet.get(position);
    }

    @Override
    protected void onBindData(ViewHolder holder, int position, ImageItem item) {
        if (getItemViewType(position) == ITEM_TYPE_IMAGE) {
            Glide.with(context)
                    .load(item.path)
                    .centerCrop()
                    .into(holder.findViewAsImageView(R.id.iv));
        }
    }
}
