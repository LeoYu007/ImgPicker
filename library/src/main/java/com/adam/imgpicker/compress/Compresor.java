package com.adam.imgpicker.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.util.Log;

import java.io.File;
import java.util.List;

/**
 * 压缩方法工具类
 *
 * @author yu
 */
public class Compresor {

    private static final String FILES_PATH = "compresor";

    Context context;
    float maxWidth = 600.0f;
    float maxHeight = 800.0f;
    int quality = 80;
    Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
    String destinationDirectoryPath;        //  存储路径
    String fileNamePrefix;                  //  文件名前缀
    String fileName;                        //  文件名

    private Compresor(Context context) {
        this.context = context;
        destinationDirectoryPath = context.getCacheDir().getPath() + File.pathSeparator + FILES_PATH;
    }

    /**
     * 批量异步压缩
     *
     * @param entities  对象集合
     * @param converter 将对象转换为图片文件对象的转换器
     * @param listener  回调
     * @param <T>       对象类型
     */
    public <T> void compressToFileAsync(List<T> entities,
                                        BatchCompressTask.Converter<T> converter,
                                        BatchCompressTask.OnBatchCompressListener<T> listener) {
        new BatchCompressTask<T>(this, converter, listener).execute(entities);
    }

    /**
     * 批量异步压缩图片
     *
     * @param files    原始文件集合
     * @param listener 回调
     */
    public void compressToFileAsync(List<File> files, BatchCompressTask.OnBatchCompressListener<File> listener) {
        compressToFileAsync(files, new BatchCompressTask.Converter<File>() {
            @Override
            public File conver(File file) {
                return file;
            }

            @Override
            public void assignin(File file, File compressFile) {
                file = compressFile;
                Log.e("compresor", "compressFile path = " + file.getAbsolutePath());
            }
        }, listener);
    }

    /**
     * 异步压缩成文件
     *
     * @param file     原始文件
     * @param listener 回调接口
     */
    public void compressToFileAsync(File file, DefaultCompressTask.OnCompressListener listener) {
        new DefaultCompressTask(this, listener).execute(file);
    }

    /**
     * 同步压缩成文件
     *
     * @param file 原始文件
     * @return 压缩后的文件
     */
    public File compressToFile(File file) {
        return ImageUtil.compressImage(context, Uri.fromFile(file), maxWidth, maxHeight,
                compressFormat, bitmapConfig, quality, destinationDirectoryPath,
                fileNamePrefix, fileName);
    }

    /**
     * 同步压缩为Bitmap
     *
     * @param file 原始文件
     * @return 压缩后的Bitmap
     */
    public Bitmap compressToBitmap(File file) {
        return ImageUtil.getScaledBitmap(context, Uri.fromFile(file), maxWidth, maxHeight, bitmapConfig);
    }


    public static class Builder {
        private Compresor mCompresor;

        public Builder(Context context) {
            mCompresor = new Compresor(context);
        }

        public Builder setMaxWidth(float maxWidth) {
            mCompresor.maxWidth = maxWidth;
            return this;
        }

        public Builder setMaxHeight(float maxHeight) {
            mCompresor.maxHeight = maxHeight;
            return this;
        }

        public Builder setCompressFormat(Bitmap.CompressFormat compressFormat) {
            mCompresor.compressFormat = compressFormat;
            return this;
        }

        public Builder setBitmapConfig(Bitmap.Config bitmapConfig) {
            mCompresor.bitmapConfig = bitmapConfig;
            return this;
        }

        public Builder setQuality(@IntRange(from = 0, to = 100) int quality) {
            mCompresor.quality = quality;
            return this;
        }

        public Builder setDestinationDirectoryPath(String destinationDirectoryPath) {
            mCompresor.destinationDirectoryPath = destinationDirectoryPath;
            return this;
        }

        public Builder setFileNamePrefix(String prefix) {
            mCompresor.fileNamePrefix = prefix;
            return this;
        }

        public Builder setFileName(String fileName) {
            mCompresor.fileName = fileName;
            return this;
        }

        public Compresor build() {
            return mCompresor;
        }
    }
}
