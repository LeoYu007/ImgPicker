package com.adam.imgpicker.compress;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.util.List;


/**
 * 批量压缩图片任务类,传入一个图片集合,压缩之后，把压缩后的地址赋值给对应ImageEntity，然后返回原集合
 * Created by yu on 2017/4/20.
 */
public class BatchCompressTask<T> extends AsyncTask<List<T>, Void, List<T>> {

    private OnBatchCompressListener<T> listener;
    private Converter<T> converter;
    private Compressor compresor;

    public BatchCompressTask(Compressor compresor, Converter<T> converter, OnBatchCompressListener<T> listener) {
        this.listener = listener;
        this.compresor = compresor;
        this.converter = converter;
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
    }

    @Override
    protected List<T> doInBackground(List<T>... params) {
        List<T> data = params[0];
        File newfile;
        for (T t : data) {
            newfile = ImageUtil.compressImage(
                    compresor.context,
                    Uri.fromFile(converter.conver(t)),
                    compresor.maxWidth,
                    compresor.maxHeight,
                    compresor.compressFormat,
                    compresor.bitmapConfig,
                    compresor.quality,
                    compresor.destinationDirectoryPath,
                    compresor.fileNamePrefix,
                    compresor.fileName);
            converter.assignin(t, newfile);
        }
        return data;
    }

    @Override
    protected void onPostExecute(List<T> result) {
        listener.onSuccess(result);
        listener.onComplete();
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
         * @param t            对象
         * @param compressFile 压缩后的文件
         */
        void assignin(T t, File compressFile);
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
}
