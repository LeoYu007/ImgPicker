package com.adam.imgpicker.adapter;

import android.content.Context;
import android.view.View;

import com.adam.imgpicker.ImagePicker;
import com.adam.imgpicker.R;
import com.adam.imgpicker.adapter.baseadapter.RecyclerAdapter;
import com.adam.imgpicker.adapter.baseadapter.ViewHolder;
import com.adam.imgpicker.core.ImageLoader;
import com.adam.imgpicker.entity.ImageFolder;

import java.util.List;

/**
 * 文件夹选择弹窗
 * Created by yu on 2017/4/14.
 */
public class ImageFolderAdapter extends RecyclerAdapter<ImageFolder> {

    private ImageLoader imageLoader;
    private Context context;

    public ImageFolderAdapter(Context context, List<ImageFolder> data) {
        super(data, R.layout.adapter_folder_list_item);
        this.context = context;
        this.imageLoader = ImagePicker.getInstance().getConfig().loader;
    }

    @Override
    protected void onBindData(ViewHolder holder, int position, ImageFolder item) {
        holder.setText(R.id.tvFolderName, item.name)
                .setText(R.id.tvImageCount, String.format("共%d张", item.images.size()))
                .setVisibility(R.id.ivFolderCheck, item.selected ? View.VISIBLE : View.GONE);
        imageLoader.displayImage(context, item.cover.path, holder.findViewAsImageView(R.id.ivCover));
    }

    /**
     * 设置选中状态
     */
    public void setSelected(int position) {
        for (int i = 0; i < mDataSet.size(); i++) {
            mDataSet.get(i).selected = (i == position);
        }
        notifyDataSetChanged();
    }
}
