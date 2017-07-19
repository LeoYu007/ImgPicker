package com.adam.imgpicker.compress.tiny.core;

import android.graphics.Bitmap;
import android.net.Uri;

import com.adam.imgpicker.compress.tiny.Tiny;
import com.adam.imgpicker.compress.tiny.callable.FileCompressCallableTasks;
import com.adam.imgpicker.compress.tiny.callback.Callback;
import com.adam.imgpicker.compress.tiny.callback.DefaultCallbackDispatcher;
import com.adam.imgpicker.compress.tiny.callback.FileBatchCallback;
import com.adam.imgpicker.compress.tiny.callback.FileWithBitmapBatchCallback;
import com.adam.imgpicker.compress.tiny.common.BatchCompressResult;

import java.io.File;

/**
 * Created by zhengxiaoyong on 2017/3/31.
 */
public class FileBatchCompressEngine extends CompressEngine {

    private Tiny.FileCompressOptions mCompressOptions;

    public FileBatchCompressEngine withOptions(Tiny.FileCompressOptions options) {
        options.config = CompressKit.filterConfig(options.config);
        mCompressOptions = options;
        return this;
    }

    public void batchCompress(FileBatchCallback callback) {
        impl(callback);
    }

    public void batchCompress(FileWithBitmapBatchCallback callback) {
        impl(callback);
    }

    private void impl(Callback callback) {
        if (mSource == null)
            return;

        boolean shouldReturnBitmap = false;

        if (callback != null && callback instanceof FileWithBitmapBatchCallback)
            shouldReturnBitmap = true;

        if (mCompressOptions == null)
            mCompressOptions = new Tiny.FileCompressOptions();

        if (mSourceType == CompressEngine.SourceType.FILE_ARRAY) {
            File[] files = (File[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<BatchCompressResult>(
                            new FileCompressCallableTasks.FileArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, files)
                            , new DefaultCallbackDispatcher<BatchCompressResult>(callback)
                    ));
        } else if (mSourceType == CompressEngine.SourceType.BITMAP_ARRAY) {
            Bitmap[] bitmaps = (Bitmap[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<BatchCompressResult>(
                            new FileCompressCallableTasks.BitmapArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, bitmaps)
                            , new DefaultCallbackDispatcher<BatchCompressResult>(callback)
                    ));

        } else if (mSourceType == CompressEngine.SourceType.URI_ARRAY) {
            Uri[] uris = (Uri[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<BatchCompressResult>(
                            new FileCompressCallableTasks.UriArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, uris)
                            , new DefaultCallbackDispatcher<BatchCompressResult>(callback)
                    ));
        } else if (mSourceType == CompressEngine.SourceType.RES_ID_ARRAY) {
            int[] resIds = (int[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<BatchCompressResult>(
                            new FileCompressCallableTasks.ResourceArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, resIds)
                            , new DefaultCallbackDispatcher<BatchCompressResult>(callback)
                    ));
        }
    }
}
