package com.adam.imgpicker.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adam.imgpicker.R;
import com.adam.imgpicker.adapter.ImageFolderAdapter;
import com.adam.imgpicker.adapter.ImageGridAdapter;
import com.adam.imgpicker.adapter.baseadapter.RecyclerAdapter;
import com.adam.imgpicker.compress.BatchCompressTask;
import com.adam.imgpicker.compress.Compresor;
import com.adam.imgpicker.core.ImageDataSource;
import com.adam.imgpicker.core.OnSelectedListSizeChangeListener;
import com.adam.imgpicker.entity.ImageFolder;
import com.adam.imgpicker.entity.ImageItem;
import com.adam.imgpicker.util.Utils;
import com.adam.imgpicker.widget.DividerGridItemDecoration;
import com.adam.imgpicker.widget.FolderPopUpWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.adam.imgpicker.ui.PreviewDelActivity.REQUEST_PREVIEW_CODE;

/**
 * 相册列表页面
 * Created by yu on 2017/4/13.
 */
public class GridActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;

    public static final int REQUEST_CAPTURE_CODE = 0x04;
    public static final int REQUEST_CROP_CODE = 0x05;

    private RelativeLayout mFolderRoot;
    private Button mBtnOk;
    private Button mBtnDir;
    private Button mBtnPre;
    private ImageGridAdapter mGridAdapter;
    private ImageFolderAdapter mFolderAdapter;
    private FolderPopUpWindow mFolderPopUpWindow;
    private File mPhotoFile;
    private File mCropFile;
    private Compresor mCompresor;
    private View compressProgress;

    private RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            setSelectNumber();
        }
    };
    private RecyclerAdapter.OnItemClickListener onItemClickListener = new RecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, Object item) {
            if (mConfig.showCamera && position == 0) {
                if (checkPermission(Manifest.permission.CAMERA)) {
                    mPhotoFile = Utils.takePhoto(GridActivity.this, REQUEST_CAPTURE_CODE);
                } else {
                    ActivityCompat.requestPermissions(GridActivity.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                }
                return;
            }

            if (mConfig.limited > 1) {
                PreviewActivity.start(GridActivity.this, mGridAdapter.getDataSet(),
                        mConfig.showCamera ? position - 1 : position);
            } else {
                ImageItem imageItem = mGridAdapter.getItem(position);
                if (mConfig.needCrop) {
                    mCropFile = Utils.crop(GridActivity.this, REQUEST_CROP_CODE, new File(imageItem.path), mConfig);
                } else {
                    ArrayList<ImageItem> data = new ArrayList<>();
                    data.add(imageItem);

                    if (mConfig.compress) {
                        compressAndFinish(data);//  单选压缩
                    } else {
                        callListenerSingleSelected(data);
                        finish();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        initView();

        if (mImgPicker.getImageFolders() != null) {
            loadCompleted(mImgPicker.getImageFolders());
        } else {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                loadImages();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        }

        if (mConfig.compress) {
            mCompresor = new Compresor.Builder(GridActivity.this)
                    .setMaxWidth(mConfig.maxWidth)
                    .setMaxHeight(mConfig.maxHeight)
                    .setQuality(mConfig.quality)
                    .setDestinationDirectoryPath(Utils.getCompressDir())
                    .build();
        }

    }

    private void initView() {
        mFolderRoot = (RelativeLayout) findViewById(R.id.rlFolderRoot);
        compressProgress = findViewById(R.id.compress);

        View topBar = findViewById(R.id.preTopBar);
        if (mConfig.titleBarColor != -1)
            topBar.setBackgroundColor(mConfig.titleBarColor);

        ImageView btnBack = (ImageView) topBar.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        if (mConfig.backResId != -1)
            btnBack.setImageResource(mConfig.backResId);

        TextView tvTitle = (TextView) topBar.findViewById(R.id.tvTitle);
        if (mConfig.titleTextColor != -1)
            tvTitle.setTextColor(mConfig.titleTextColor);
        if (mConfig.titleText != null)
            tvTitle.setText(mConfig.titleText);

        mBtnOk = (Button) topBar.findViewById(R.id.btnOk);
        mBtnOk.setOnClickListener(this);
        if (mConfig.btnResId != -1)
            mBtnOk.setBackgroundResource(mConfig.btnResId);
        if (mConfig.btnTextColor != -1)
            mBtnOk.setTextColor(mConfig.btnTextColor);

        mBtnDir = (Button) findViewById(R.id.btnDir);
        mBtnDir.setOnClickListener(this);
        mBtnPre = (Button) findViewById(R.id.btnPreview);
        mBtnPre.setOnClickListener(this);

        setSelectNumber();

        if (mConfig.limited > 1) {
            mBtnOk.setVisibility(View.VISIBLE);
            mBtnPre.setVisibility(View.VISIBLE);
        } else {
            mBtnOk.setVisibility(View.GONE);
            mBtnPre.setVisibility(View.GONE);
        }

        //  设置recyclerview
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.gridRv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mGridAdapter = new ImageGridAdapter(this, null);
        mGridAdapter.setOnItemClickListener(onItemClickListener);
        mGridAdapter.setOnSelectedListSizeChangeListener(new OnSelectedListSizeChangeListener() {
            @Override
            public void onChange() {
                setSelectNumber();
            }
        });
        mGridAdapter.registerAdapterDataObserver(mObserver);
        mRecyclerView.setAdapter(mGridAdapter);


        //  设置文件夹选择弹窗
        mFolderAdapter = new ImageFolderAdapter(this, null);
        mFolderPopUpWindow = new FolderPopUpWindow(this, mFolderAdapter);
        mFolderPopUpWindow.setOnItemClickListener(new FolderPopUpWindow.OnItemClickListener() {
            @Override
            public void onItemClick(ImageFolder folder) {
                mGridAdapter.refreshData(folder.images);
                mBtnDir.setText(folder.name);
            }
        });
    }

    private void loadImages() {
        Utils.deleteTempFile();
        new ImageDataSource(this, null, new ImageDataSource.OnImagesLoadedListener() {
            @Override
            public void onImagesLoaded(List<ImageFolder> imageFolders) {
                loadCompleted(imageFolders);
            }
        }).loadImages();
    }

    private void loadCompleted(List<ImageFolder> imageFolders) {
        for (int i = 0; i < imageFolders.size(); i++) {
            imageFolders.get(i).selected = i == 0;
        }

        mImgPicker.setImageFolders(imageFolders);
        mFolderAdapter.refreshData(imageFolders);
        mGridAdapter.refreshData(imageFolders.get(0).images);
    }

    /**
     * 刷新选中数量
     */
    private void setSelectNumber() {
        if (mImgPicker.getSelectedImages().size() == 0) {
            mBtnOk.setText(getString(R.string.top_bar_ok));
            mBtnOk.setEnabled(false);
            mBtnPre.setEnabled(false);
        } else {
            mBtnOk.setText(String.format("完成(%d/%d)", mImgPicker.getSelectedImages().size(), mConfig.limited));
            mBtnOk.setEnabled(true);
            mBtnPre.setEnabled(true);
        }
        mBtnPre.setText(String.format("预览(%d)", mImgPicker.getSelectedImages().size()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages();
            } else {
                showToast(getString(R.string.read_permission_denied));
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPhotoFile = Utils.takePhoto(this, REQUEST_CAPTURE_CODE);
            } else {
                showToast(getString(R.string.camera_permission_denied));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PREVIEW_CODE:
                //  预览页面返回,肯定是多选
                if (resultCode == RESULT_CANCELED) {
                    mGridAdapter.notifyDataSetChanged();
                } else {
                    //  预览页面点击完成按钮
                    callListenerMultiSelected();
                }
                break;
            case REQUEST_CAPTURE_CODE:
                if (resultCode == RESULT_OK) {
                    if (mConfig.limited == 1 && mConfig.needCrop) {
                        //  拍照之后裁剪，只有单选裁剪才生效
                        mCropFile = Utils.crop(this, REQUEST_CROP_CODE, mPhotoFile, mConfig);
                    } else if (mConfig.compress) {
                        //  拍照之后压缩
                        compressAndFinish(Utils.generateImageItemList(mPhotoFile));
                    } else {
                        //  拍照完成后不裁剪也不压缩，直接返回
                        callListenerSingleSelected(mPhotoFile);
                        finish();
                    }
                } else {
                    callListenerWithFail(getString(R.string.camera_error));
                    finish();
                }
                break;
            case REQUEST_CROP_CODE:
                if (resultCode == RESULT_OK) {
                    callListenerSingleSelected(mCropFile);
                } else {
                    callListenerWithFail(getString(R.string.crop_error));
                }
                finish();
                break;
        }
    }

    /**
     * 错误回调
     *
     * @param msg 消息
     */
    private void callListenerWithFail(String msg) {
        if (mConfig.listener != null) {
            mConfig.listener.onSelectFail(msg);
        }
    }

    /**
     * 单选回调，不压缩和裁剪
     *
     * @param data 回调的集合
     */
    private void callListenerSingleSelected(List<ImageItem> data) {
        if (mConfig.listener != null) {
            mConfig.listener.onSelect(data);
        }
    }

    /**
     * 单选回调(拍照或者裁剪后，通过file生成ImageItem)
     *
     * @param file 图片源文件
     */
    private void callListenerSingleSelected(File file) {
        if (mConfig.listener != null) {
            mConfig.listener.onSelect(Utils.generateImageItemList(file));
        }
    }

    /**
     * 多选回调listener
     */
    private void callListenerMultiSelected() {
        if (mConfig.listener != null) {
            if (mConfig.compress) {//  多选压缩
                compressAndFinish(new ArrayList<>(mImgPicker.getSelectedImages()));
            } else {
                mConfig.listener.onSelect(new ArrayList<>(mImgPicker.getSelectedImages()));
                finish();
            }
        }
    }

    /**
     * 压缩图片之后回调接口并关闭页面
     *
     * @param data 待压缩的图片集合
     */
    private void compressAndFinish(List<ImageItem> data) {
        mCompresor.compressToFileAsync(data, new BatchCompressTask.Converter<ImageItem>() {
            @Override
            public File conver(ImageItem item) {
                return new File(item.path);
            }

            @Override
            public void assignin(ImageItem item, File compressFile) {
                item.compressPath = compressFile.getAbsolutePath();
            }
        }, new BatchCompressTask.OnBatchCompressListener<ImageItem>() {
            @Override
            public void onStart() {
                compressProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(List<ImageItem> result) {
                compressProgress.setVisibility(View.GONE);
                if (mConfig.listener != null) {
                    mConfig.listener.onSelect(result);
                }
            }

            @Override
            public void onComplete() {
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnBack) {
            finish();
        } else if (i == R.id.btnDir) {
            mFolderPopUpWindow.showAtLocation(mFolderRoot, Gravity.BOTTOM, 0, 0);
        } else if (i == R.id.btnPreview) {
            PreviewActivity.start(GridActivity.this, new ArrayList<>(mImgPicker.getSelectedImages()), 0);
        } else if (i == R.id.btnOk) {
            callListenerMultiSelected();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mConfig.listener != null) {
            mConfig.listener.onSelectImageCancel();
        }
    }

    @Override
    protected void onDestroy() {
        mGridAdapter.unregisterAdapterDataObserver(mObserver);
        super.onDestroy();
    }
}
