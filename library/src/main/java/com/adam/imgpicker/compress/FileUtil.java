package com.adam.imgpicker.compress;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.io.File;

/**
 * 文件工具类
 */
public class FileUtil {

    private FileUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 截取文件名称
     *
     * @param fileName 文件名称
     */
    static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    /**
     * 获取文件名称
     *
     * @param context 上下文
     * @param uri     uri
     * @return 文件名称
     */
    static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /**
     * 获取真实的路径
     *
     * @param context 上下文
     * @param uri     uri
     * @return 文件路径
     */
    static String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String realPath = cursor.getString(index);
            cursor.close();
            return realPath;
        }
    }

    /**
     * 递归删除文件
     */
    public static boolean deleteFile(File file) {
        if (!file.exists())
            return true;

        if (file.isFile()) {
            return file.delete();
        } else {
            File[] files = file.listFiles();
            for (File file1 : files) {
                deleteFile(file1);
            }
            file.delete();
            return true;
        }
    }

}
