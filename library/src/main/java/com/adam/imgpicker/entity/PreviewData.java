package com.adam.imgpicker.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于传递预览的图片集合
 * Created by yu on 2017/4/17.
 */
public class PreviewData implements Parcelable {

    public static final int TYPE_PREVIEW = 0;//  勾选图片后，点击预览按钮进入，只有已选的图片，可以取消选择
    public static final int TYPE_DELETE = 1;//  选择完成后，发布前点击图片进入，可以删除已选图片（微信选择完成后，点发表之前那里）

    public int type = TYPE_PREVIEW;
    public int currentPosition = 0;
    public List<ImageItem> data;

    public PreviewData(int type, int currentPosition, List<ImageItem> data) {
        this.type = type;
        this.currentPosition = currentPosition;
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.currentPosition);
        dest.writeList(this.data);
    }

    protected PreviewData(Parcel in) {
        this.type = in.readInt();
        this.currentPosition = in.readInt();
        this.data = new ArrayList<ImageItem>();
        in.readList(this.data, ImageItem.class.getClassLoader());
    }

    public static final Creator<PreviewData> CREATOR = new Creator<PreviewData>() {
        @Override
        public PreviewData createFromParcel(Parcel source) {
            return new PreviewData(source);
        }

        @Override
        public PreviewData[] newArray(int size) {
            return new PreviewData[size];
        }
    };
}
