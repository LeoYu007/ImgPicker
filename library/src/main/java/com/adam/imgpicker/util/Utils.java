package com.adam.imgpicker.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.adam.imgpicker.ImagePickerConfig;
import com.adam.imgpicker.compress.FileUtil;
import com.adam.imgpicker.entity.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 调用拍照和裁剪的工具包
 * Created by lyu on 2017/4/17.
 */
public class Utils {

    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/";
    private static final String COMPRESS_DIR_PATH = ROOT_PATH + "compress/";
    private static final String CAMERA_DIR_PATH = ROOT_PATH + "camera/";
    private static final String CROP_DIR_PATH = ROOT_PATH + "crop/";

    /**
     * 删除临时文件
     */
    public static void deleteTempFile() {
        FileUtil.deleteFile(new File(ROOT_PATH));
    }

    /**
     * 调用相机拍摄一张图片
     *
     * @param activity    接收的activity
     * @param requestCode 请求code
     * @return 返回原图存储的文件
     */
    public static File takePhoto(Activity activity, int requestCode) {
        File file = createFile(CAMERA_DIR_PATH, System.currentTimeMillis() + ".jpeg");
        //通过FileProvider创建一个content类型的Uri
        Uri imageUri = FileProvider7.getUriForFile(activity, file);

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //这一句表示对目标应用临时授权该Uri所代表的文件
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);// 设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);// 将拍取的照片保存到指定URI
        activity.startActivityForResult(intent, requestCode);

        return file;
    }

    /**
     * 裁剪一张图片
     *
     * @param activity    接收的activity
     * @param requestCode 请求code
     * @param sourceImage 原始图片
     * @param config      用于获取裁剪输出参数
     * @return 返回裁剪之后存储的图片文件
     */
    public static File crop(Activity activity, int requestCode, File sourceImage, ImagePickerConfig config) {
        Uri imageUri = FileProvider7.getUriForFile(activity, sourceImage);

        File outputFile = createFile(CROP_DIR_PATH, System.currentTimeMillis() + ".jpeg");
        Uri outputUri = FileProvider7.getUriForFile(activity, outputFile);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", config.aspectX);
        intent.putExtra("aspectY", config.aspectY);
        intent.putExtra("outputX", config.outputX);
        intent.putExtra("outputY", config.outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);

        // 将存储图片的uri读写权限授权给剪裁工具应用，否则会出现无法存储裁剪图片的情况
        List<ResolveInfo> resInfoList = activity.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            activity.grantUriPermission(packageName, outputUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        activity.startActivityForResult(intent, requestCode);

        return outputFile;
    }

    private static File createFile(String dir, String fileName) {
        File file = new File(dir, fileName);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        return file;
    }

    public static String getCompressDir() {
        return COMPRESS_DIR_PATH;
    }

    /**
     * 拍照或者裁剪之后，通过文件创建一个新的ImageItem对象,并加入集合
     */
    public static List<ImageItem> generateImageItemList(File file) {
        ImageItem imageItem = new ImageItem();
        imageItem.name = file.getName();
        imageItem.path = file.getAbsolutePath();
        imageItem.size = file.length();
        imageItem.mimeType = "image/jpeg";
        imageItem.addTime = file.lastModified();

        ArrayList<ImageItem> data = new ArrayList<>();
        data.add(imageItem);
        return data;
    }
}
