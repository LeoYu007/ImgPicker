package com.adam.imgpicker.compress;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;

/**
 * 压缩图片文件任务类
 * Created by yu on 2017/4/20.
 */
class DefaultCompressTask extends AsyncTask<File, Void, File> {

    private OnCompressListener listener;
    private Compressor compresor;

    public DefaultCompressTask(Compressor compresor, OnCompressListener listener) {
        this.listener = listener;
        this.compresor = compresor;
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
    }

    @Override
    protected File doInBackground(File... params) {
        return ImageUtil.compressImage(
                compresor.context,
                Uri.fromFile(params[0]),
                compresor.maxWidth,
                compresor.maxHeight,
                compresor.compressFormat,
                compresor.bitmapConfig,
                compresor.quality,
                compresor.destinationDirectoryPath,
                compresor.fileNamePrefix,
                compresor.fileName);
    }

    @Override
    protected void onPostExecute(File result) {
        listener.onSuccess(result);
        listener.onComplete();
    }

    public interface OnCompressListener {
        void onStart();

        void onSuccess(File result);

        void onComplete();
    }

}
