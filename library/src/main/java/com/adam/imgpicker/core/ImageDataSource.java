package com.adam.imgpicker.core;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.adam.imgpicker.R;
import com.adam.imgpicker.entity.ImageFolder;
import com.adam.imgpicker.entity.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageDataSource implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ALL = 0;        // 加载所有图片
    private static final int LOADER_CATEGORY = 1;   // 分类加载图片

    private final String[] IMAGE_PROJECTION = {     // 查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   // 图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           // 图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           // 图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          // 图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         // 图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      // 图片的类型，image/jpeg
            MediaStore.Images.Media.DATE_ADDED      // 图片被添加的时间，long型  1450518608
    };

    private String path;
    private FragmentActivity activity;
    private OnImagesLoadedListener loadedListener;                     // 图片加载完成的回调接口
    private ArrayList<ImageFolder> imageFolders = new ArrayList<>();   // 所有的图片文件夹

    public ImageDataSource(@NonNull FragmentActivity activity, @NonNull OnImagesLoadedListener loadedListener) {
        this(activity, null, loadedListener);
    }

    /**
     * @param activity       用于初始化LoaderManager，需要兼容到2.3
     * @param path           指定扫描的文件夹目录，可以为 null，表示扫描所有图片
     * @param loadedListener 图片加载完成的监听
     */
    public ImageDataSource(@NonNull FragmentActivity activity,
                           @Nullable String path,
                           @NonNull OnImagesLoadedListener loadedListener) {
        this.activity = activity;
        this.loadedListener = loadedListener;
        this.path = path;
    }

    public void loadImages() {
        LoaderManager loaderManager = activity.getSupportLoaderManager();
        if (path == null) {
            // 加载所有的图片
            loaderManager.initLoader(LOADER_ALL, null, this);
        } else {
            // 加载指定目录的图片
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            loaderManager.initLoader(LOADER_CATEGORY, bundle, this);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;

        if (id == LOADER_CATEGORY) {
            // 扫描某个图片文件夹
            cursorLoader = new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'",
                    null, IMAGE_PROJECTION[6] + " DESC");
        } else {
            // 扫描所有图片
            cursorLoader = new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        imageFolders.clear();
        if (data != null) {
            ArrayList<ImageItem> allImages = new ArrayList<>();   // 所有图片的集合,不分文件夹
            while (data.moveToNext()) {
                // 查询数据
                String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                // 封装实体
                ImageItem imageItem = new ImageItem();
                imageItem.name = imageName;
                imageItem.path = imagePath;
                imageItem.size = imageSize;
                imageItem.width = imageWidth;
                imageItem.height = imageHeight;
                imageItem.mimeType = imageMimeType;
                imageItem.addTime = imageAddTime;
                allImages.add(imageItem);
                // 根据父路径分类存放图片
                File imageFile = new File(imagePath);
                File imageParentFile = imageFile.getParentFile();
                ImageFolder imageFolder = new ImageFolder();
                imageFolder.name = imageParentFile.getName();
                imageFolder.path = imageParentFile.getAbsolutePath();

                if (!imageFolders.contains(imageFolder)) {
                    ArrayList<ImageItem> images = new ArrayList<>();
                    images.add(imageItem);
                    imageFolder.cover = imageItem;
                    imageFolder.images = images;
                    imageFolders.add(imageFolder);
                } else {
                    imageFolders.get(imageFolders.indexOf(imageFolder)).images.add(imageItem);
                }
            }
            // 防止没有图片报异常
            if (data.getCount() > 0) {
                // 构造所有图片的集合
                ImageFolder allImagesFolder = new ImageFolder();
                allImagesFolder.name = activity.getResources().getString(R.string.all_images);
                allImagesFolder.path = "/";
                if (!allImages.isEmpty())
                    allImagesFolder.cover = allImages.get(0);
                allImagesFolder.images = allImages;
                allImagesFolder.selected = true;
                imageFolders.add(0, allImagesFolder);  // 确保第一条是所有图片
            }
        }

        // 回调接口，通知图片数据准备完成
        if (null != loadedListener) {
            loadedListener.onImagesLoaded(imageFolders);
        }
        loadedListener = null;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    /**
     * 所有图片加载完成的回调接口
     */
    public interface OnImagesLoadedListener {
        void onImagesLoaded(List<ImageFolder> imageFolders);
    }
}
