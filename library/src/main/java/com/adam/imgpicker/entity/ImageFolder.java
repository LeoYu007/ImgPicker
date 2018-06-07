package com.adam.imgpicker.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 图片文件夹对象
 *
 * @author lyu
 */
public class ImageFolder implements Parcelable {

    public String name;  // 当前文件夹的名字
    public String path;  // 当前文件夹的路径
    public ImageItem cover;   // 当前文件夹需要要显示的缩略图，默认为最近的一次图片
    public ArrayList<ImageItem> images;  // 当前文件夹下所有图片的集合

    public boolean selected;//  是否当前选中的文件夹

    @Override
    public int hashCode() {
        return path.hashCode() + name.hashCode();
    }

    /**
     * 只要文件夹的路径和名字相同，就认为是相同的文件夹
     */
    @Override
    public boolean equals(Object o) {
        try {
            ImageFolder other = (ImageFolder) o;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeParcelable(this.cover, flags);
        dest.writeTypedList(this.images);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    public ImageFolder() {
    }

    protected ImageFolder(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.cover = in.readParcelable(ImageItem.class.getClassLoader());
        this.images = in.createTypedArrayList(ImageItem.CREATOR);
        this.selected = in.readByte() != 0;
    }

    public static final Creator<ImageFolder> CREATOR = new Creator<ImageFolder>() {
        @Override
        public ImageFolder createFromParcel(Parcel source) {
            return new ImageFolder(source);
        }

        @Override
        public ImageFolder[] newArray(int size) {
            return new ImageFolder[size];
        }
    };
}
