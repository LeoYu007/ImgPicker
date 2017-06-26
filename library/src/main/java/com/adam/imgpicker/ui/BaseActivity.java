package com.adam.imgpicker.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.adam.imgpicker.ImagePicker;
import com.adam.imgpicker.ImagePickerConfig;
import com.adam.imgpicker.R;
import com.adam.imgpicker.util.StatusBarUtil;


/**
 * BaseActivity
 */
public class BaseActivity extends AppCompatActivity {

    protected ImagePickerConfig mConfig;
    protected ImagePicker mImgPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImgPicker = ImagePicker.getInstance();
        mConfig = mImgPicker.getConfig();

        if (mConfig == null) {
            throw new NullPointerException("ImagePicker.getConfig() == null");
        }

        if (mConfig.titleBarColor != -1) {
            StatusBarUtil.setColor(this, mConfig.titleBarColor);
        } else {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.main_color));
        }
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showToast(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }
}
