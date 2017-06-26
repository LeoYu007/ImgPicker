package com.adam.imgpicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.adam.imgpicker.R;
import com.adam.imgpicker.entity.ImageItem;
import com.adam.imgpicker.entity.PreviewData;

import java.util.List;
import java.util.Set;

/**
 * 预览页面,可以选择图片
 * Created by lyu on 2017/4/20.
 */
public class PreviewActivity extends PreviewDelActivity {

    private Set<ImageItem> mSelectedImages;   // 所有已经选中的图片

    public static void start(Activity context, List<ImageItem> data, int currentPosition) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(KEY_PREVIEW_DATA, new PreviewData(PreviewData.TYPE_PREVIEW, currentPosition, data));
        context.startActivityForResult(intent, REQUEST_PREVIEW_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectedImages = mImgPicker.getSelectedImages();
        btnOk.setEnabled(mSelectedImages.size() > 0);
        cbCheck.setChecked(mSelectedImages.contains(mImageItems.get(mCurrentPosition)));

        setBtnOkText();
        cbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedImages.size() == mConfig.limited && cbCheck.isChecked()) {
                    showToast(String.format("最多能选%d张", mConfig.limited));
                    cbCheck.setChecked(false);
                    return;
                }

                cbCheck.setChecked(cbCheck.isChecked());
                ImageItem item = mImageItems.get(mCurrentPosition);
                if (cbCheck.isChecked()) {
                    mSelectedImages.add(item);
                } else {
                    mSelectedImages.remove(item);
                }

                setBtnOkText();
            }
        });
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        cbCheck.setChecked(mSelectedImages.contains(mImageItems.get(position)));
    }

    /**
     * 这个页面肯定是预览选择页面进入的，不需要回调集合出去
     */
    @Override
    public void setResultData() {
        setResult(RESULT_CANCELED);
    }

    private void setBtnOkText() {
        if (mSelectedImages.size() > 0) {
            btnOk.setText(String.format("完成(%d/%d)", mSelectedImages.size(), mConfig.limited));
            btnOk.setEnabled(true);
        } else {
            btnOk.setText(getString(R.string.top_bar_ok));
            btnOk.setEnabled(false);
        }
    }
}