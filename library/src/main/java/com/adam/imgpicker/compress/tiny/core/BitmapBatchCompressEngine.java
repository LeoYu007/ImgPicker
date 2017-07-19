package com.adam.imgpicker.compress.tiny.core;

import android.graphics.Bitmap;
import android.net.Uri;

import com.adam.imgpicker.compress.tiny.Tiny;
import com.adam.imgpicker.compress.tiny.callable.BitmapCompressCallableTasks;
import com.adam.imgpicker.compress.tiny.callback.BitmapBatchCallback;
import com.adam.imgpicker.compress.tiny.callback.Callback;
import com.adam.imgpicker.compress.tiny.callback.DefaultCallbackDispatcher;

import java.io.File;

/**
 * Created by zhengxiaoyong on 2017/3/31.
 */
public class BitmapBatchCompressEngine extends CompressEngine {

    private Tiny.BitmapCompressOptions mCompressOptions;

    public BitmapBatchCompressEngine withOptions(Tiny.BitmapCompressOptions options) {
        options.config = CompressKit.filterConfig(options.config);
        mCompressOptions = options;
        return this;
    }

    public void batchCompress(BitmapBatchCallback callback) {
        impl(callback);
    }

    private void impl(Callback callback) {
        if (mSource == null)
            return;

        if (mCompressOptions == null)
            mCompressOptions = new Tiny.BitmapCompressOptions();

        if (mSourceType == CompressEngine.SourceType.FILE_ARRAY) {
            File[] file = (File[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap[]>(
                            new BitmapCompressCallableTasks.FileArrayAsBitmapCallable(mCompressOptions, file)
                            , new DefaultCallbackDispatcher<Bitmap[]>(callback)
                    ));

        } else if (mSourceType == CompressEngine.SourceType.BITMAP_ARRAY) {
            Bitmap[] bitmaps = (Bitmap[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap[]>(
                            new BitmapCompressCallableTasks.BitmapArrayAsBitmapCallable(mCompressOptions, bitmaps)
                            , new DefaultCallbackDispatcher<Bitmap[]>(callback)
                    ));

        } else if (mSourceType == CompressEngine.SourceType.URI_ARRAY) {
            Uri[] uris = (Uri[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap[]>(
                            new BitmapCompressCallableTasks.UriArrayAsBitmapCallable(mCompressOptions, uris)
                            , new DefaultCallbackDispatcher<Bitmap[]>(callback)
                    ));
        } else if (mSourceType == CompressEngine.SourceType.RES_ID_ARRAY) {
            int[] resIds = (int[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap[]>(
                            new BitmapCompressCallableTasks.ResourceArrayAsBitmapCallable(mCompressOptions, resIds)
                            , new DefaultCallbackDispatcher<Bitmap[]>(callback)
                    ));
        }
    }
}
