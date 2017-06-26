package com.adam.imgpicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.adam.imgpicker.R;
import com.adam.imgpicker.adapter.ImagePageAdapter;
import com.adam.imgpicker.entity.ImageItem;
import com.adam.imgpicker.entity.PreviewData;
import com.adam.imgpicker.widget.ViewPagerFixed;

import java.util.ArrayList;
import java.util.List;

/**
 * PreviewDelActivity,可以删除图片的预览页面
 * Created by lyu on 2017/4/20.
 */
public class PreviewDelActivity extends BaseActivity {

    public static final int REQUEST_PREVIEW_CODE = 0x03;

    public static final String KEY_PREVIEW_DATA = "KEY_PREVIEW_DATA";
    public static final String KEY_PREVIEW_DEL_DATA = "KEY_PREVIEW_DEL_DATA";//  删除之后回传集合的key

    protected int type;                             //  预览的type，@see PreviewData.type
    protected List<ImageItem> mImageItems;          //  跳转进ImagePreview的图片文件夹
    protected int mCurrentPosition = 0;             //  跳转进ImagePreview时的序号，第几个图片
    protected TextView mTitleCount;                 //  显示当前图片的位置  例如  5/31

    protected View topBar;
    protected View bottomBar;
    protected ViewPagerFixed mViewPager;
    protected ImagePageAdapter mAdapter;
    protected Button btnOk;
    protected ImageView btnDel;
    protected CheckBox cbCheck;
    protected ImageView btnBack;

    public static void start(Activity context, List<ImageItem> data, int currentPosition) {
        Intent intent = new Intent(context, PreviewDelActivity.class);
        intent.putExtra(KEY_PREVIEW_DATA, new PreviewData(PreviewData.TYPE_DELETE, currentPosition, data));
        context.startActivityForResult(intent, REQUEST_PREVIEW_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        checkData();
        initView();
        initListener();
    }

    private void checkData() {
        PreviewData previewData = getIntent().getParcelableExtra(KEY_PREVIEW_DATA);
        if (previewData == null) {
            throw new IllegalArgumentException("----- the PreviewData is null -----");
        }
        type = previewData.type;
        mCurrentPosition = previewData.currentPosition;
        mImageItems = previewData.data;
    }

    private void initView() {
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setVisibility(type == PreviewData.TYPE_DELETE ? View.GONE : View.VISIBLE);
        cbCheck = (CheckBox) bottomBar.findViewById(R.id.cbSelected);

        topBar = findViewById(R.id.preTopBar);
        if (mConfig.titleBarColor != -1)
            topBar.setBackgroundColor(mConfig.titleBarColor);

        btnBack = (ImageView) topBar.findViewById(R.id.btnBack);
        if (mConfig.backResId != -1)
            btnBack.setImageResource(mConfig.backResId);

        mTitleCount = (TextView) topBar.findViewById(R.id.tvTitle);
        if (mConfig.titleTextColor != -1)
            mTitleCount.setTextColor(mConfig.titleTextColor);
        if (mConfig.titleText != null)
            mTitleCount.setText(mConfig.titleText);
        mTitleCount.setText(String.format("%d/%d", mCurrentPosition + 1, mImageItems.size()));

        btnOk = (Button) topBar.findViewById(R.id.btnOk);
        btnOk.setVisibility(type == PreviewData.TYPE_DELETE ? View.GONE : View.VISIBLE);
        if (mConfig.btnResId != -1)
            btnOk.setBackgroundResource(mConfig.btnResId);
        if (mConfig.btnTextColor != -1)
            btnOk.setTextColor(mConfig.btnTextColor);

        btnDel = (ImageView) topBar.findViewById(R.id.btnDel);
        btnDel.setVisibility(type == PreviewData.TYPE_DELETE ? View.VISIBLE : View.GONE);

        mViewPager = (ViewPagerFixed) findViewById(R.id.viewpager);
        mAdapter = new ImagePageAdapter(this, mImageItems);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition, false);
    }

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageItems.remove(mViewPager.getCurrentItem());
                if (mImageItems.size() == 0) {
                    onBackPressed();
                }
                mAdapter.refreshData(mImageItems);
                mTitleCount.setText(String.format("%d/%d", mViewPager.getCurrentItem() + 1, mImageItems.size()));
            }
        });
        mAdapter.setPhotoViewClickListener(new ImagePageAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1) {
                onImageSingleTap();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PreviewDelActivity.this.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 单击时，隐藏头和尾
     */
    public void onImageSingleTap() {
        if (topBar.getVisibility() == View.VISIBLE) {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_out));
            bottomBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
            topBar.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
        } else {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_in));
            bottomBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
            topBar.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
        }
    }

    public void onPageSelected(int position) {
        mCurrentPosition = position;
        mTitleCount.setText(String.format("%d/%d", position + 1, mImageItems.size()));
    }

    @Override
    public void onBackPressed() {
        setResultData();
        super.onBackPressed();
    }

    /**
     * 把删除之后的集合回调出去
     */
    public void setResultData() {
        Intent data = new Intent();
        data.putExtra(KEY_PREVIEW_DEL_DATA, new ArrayList<>(mImageItems));
        setResult(RESULT_OK, data);
    }
}