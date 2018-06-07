package com.adam.imgpicker.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片对象
 *
 * @author lyu
 */
public class ImageItem implements Parcelable {

    public String name = "";       // 图片的名字
    public String path = "";       // 图片的路径
    public long size;         // 图片的大小
    public int width;         // 图片的宽度
    public int height;        // 图片的高度
    public String mimeType = "";   // 图片的类型
    public long addTime;      // 图片的创建时间

    public String compressPath = ""; //  压缩后的地址，可能没有
    public String remotePath = ""; //  上传后的地址，可能没有

    @Override
    public int hashCode() {
        return path.hashCode() + (int) (addTime ^ (addTime >>> 32));
    }

    /**
     * 图片的路径和创建时间相同就认为是同一张图片
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ImageItem) {
            ImageItem other = (ImageItem) o;
            return this.path.equalsIgnoreCase(other.path) && this.addTime == other.addTime;
        } else {
            return super.equals(o);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeLong(this.size);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.mimeType);
        dest.writeLong(this.addTime);
        dest.writeString(this.compressPath);
        dest.writeString(this.remotePath);
    }

    public ImageItem() {
    }

    protected ImageItem(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.size = in.readLong();
        this.width = in.readInt();
        this.height = in.readInt();
        this.mimeType = in.readString();
        this.addTime = in.readLong();
        this.compressPath = in.readString();
        this.remotePath = in.readString();
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel source) {
            return new ImageItem(source);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };
}
