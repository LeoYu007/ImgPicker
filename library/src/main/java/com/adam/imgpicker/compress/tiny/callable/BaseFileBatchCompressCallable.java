package com.adam.imgpicker.compress.tiny.callable;


import com.adam.imgpicker.compress.tiny.Tiny;
import com.adam.imgpicker.compress.tiny.common.BatchCompressResult;

import java.util.concurrent.Callable;

/**
 * Created by zhengxiaoyong on 2017/3/31.
 */
abstract class BaseFileBatchCompressCallable implements Callable<BatchCompressResult> {

    Tiny.FileCompressOptions mCompressOptions;

    boolean shouldReturnBitmap;

    BaseFileBatchCompressCallable(Tiny.FileCompressOptions options, boolean withBitmap) {
        mCompressOptions = options;
        shouldReturnBitmap = withBitmap;
    }

}
