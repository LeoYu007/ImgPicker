package com.adam.imgpicker.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IntRange;

import com.adam.imgpicker.compress.tiny.Tiny;
import com.adam.imgpicker.compress.tiny.callback.FileBatchCallback;

import java.io.File;
import java.util.List;

/**
 * 压缩方法工具类
 *
 * @author yu
 */
public class Compressor {

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

    private Compressor(Context context) {
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
    public <T> void compressToFileAsync(final List<T> entities,
                                        final Converter<T> converter,
                                        final OnBatchCompressListener<T> listener) {
        if (listener != null) listener.onStart();

        File[] files = new File[entities.size()];
        for (int i = 0; i < entities.size(); i++) {
            files[i] = converter.conver(entities.get(i));
        }

        Tiny.FileCompressOptions options = new Tiny.BatchFileCompressOptions();
        Tiny.getInstance().source(files).batchAsFile().withOptions(options).batchCompress(new FileBatchCallback() {
            @Override
            public void callback(boolean isSuccess, String[] outfile) {
                if (listener != null) {
                    for (int i = 0; i < entities.size(); i++) {
                        converter.assignin(entities.get(i), outfile[i]);
                    }
                    listener.onSuccess(entities);
                    listener.onComplete();
                }
            }
        });
    }

    /**
     * 批量压缩监听
     *
     * @param <T>
     */
    public interface OnBatchCompressListener<T> {
        void onStart();

        void onSuccess(List<T> result);

        void onComplete();
    }

    public interface Converter<T> {
        /**
         * 把传入的对象转换为需要压缩的图片File
         *
         * @param t 对象
         * @return 图片文件
         */
        File conver(T t);

        /**
         * 压缩完成后，把文件或者地址赋值给对象
         *
         * @param t                对象
         * @param compressFilePath 压缩后的文件地址
         */
        void assignin(T t, String compressFilePath);
    }


    public static class Builder {
        private Compressor mCompresor;

        public Builder(Context context) {
            mCompresor = new Compressor(context);
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

        public Compressor build() {
            return mCompresor;
        }
    }
}
