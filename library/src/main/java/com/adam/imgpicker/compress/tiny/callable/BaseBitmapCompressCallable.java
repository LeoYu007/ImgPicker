package com.adam.imgpicker.compress.tiny.callable;

import android.graphics.Bitmap;

import com.adam.imgpicker.compress.tiny.Tiny;

import java.util.concurrent.Callable;

/**
 * Created by zhengxiaoyong on 2017/3/11.
 */
abstract class BaseBitmapCompressCallable implements Callable<Bitmap> {

    Tiny.BitmapCompressOptions mCompressOptions;

    BaseBitmapCompressCallable(Tiny.BitmapCompressOptions options) {
        mCompressOptions = options;
    }

}
