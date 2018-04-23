package com.adam.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.adam.imgpicker.ImagePicker;
import com.adam.imgpicker.ImagePickerConfig;
import com.adam.imgpicker.adapter.baseadapter.RecyclerAdapter;
import com.adam.imgpicker.core.ImageLoader;
import com.adam.imgpicker.core.SimpleSelectListener;
import com.adam.imgpicker.entity.ImageItem;
import com.adam.imgpicker.ui.PreviewDelActivity;
import com.adam.imgpicker.widget.DividerGridItemDecoration;
import com.bumptech.glide.Glide;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int maxSize = 9;// 最多可选的图片数量
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycler();

        ImagePickerConfig config = new ImagePickerConfig.Builder()
                .imageLoader(new ImageLoader() {    // 不限制图片框架，需要自己实现
                    @Override
                    public void displayImage(Context context, String path, ImageView imageView) {
                        Glide.with(context).load(path).into(imageView);
                    }
                })
                .showCamera(true)                   // 第一个item是否显示相机,默认true
                .limited(maxSize)                   // 最多能选的张数（单选填1）

//                .titleBarColor(Color.GRAY)        //titlebar的颜色和文字等自定义选项
//                .titleText("选择图片")
//                .titleTextColor(Color.parseColor("#ffffff"))
//                .btnResId(R.drawable.selector_back_press)
//                .btnTextColor(Color.parseColor("#ffffff"))
//                .backResId(R.mipmap.ic_launcher)

//                .needCrop(true)                   // 是否裁剪（只有单选时才有效）,如果裁剪就不会执行压缩
//                .cropSize(1, 1, 400, 400)         // 裁剪比例和大小
//                .compress(false)                  // 是否压缩,默认ture
//                .maxWidthAndHeight(720, 960)      // 压缩最大尺寸，默认720*960
//                .quality(80)                      // 压缩质量，默认80
                .callback(new SimpleSelectListener() {
                    @Override
                    public void onSelect(List<ImageItem> data) {
                        handleImages(data);
                    }
                })
                .build();

        ImagePicker.getInstance().setConfig(config);
    }

    private void initRecycler() {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        rv.addItemDecoration(new DividerGridItemDecoration(this));
        adapter = new SelectImageAdapter(this, null, maxSize);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                if (adapter.getItemViewType(position) == SelectImageAdapter.ITEM_TYPE_IMAGE) {
                    // 点击已选图片 ---> 预览删除
                    PreviewDelActivity.start(MainActivity.this, adapter.getDataSet(), position);
                } else {
                    // 点击加号 ---> 选择图片
                    // 比如总共能选9张，第一次选了三张，第二次只能选6张，就用这个方法
//                    ImagePicker.getInstance().open(MainActivity.this, maxSize - adapter.getDataSet().size());
                    ImagePicker.getInstance().open(MainActivity.this);
                }
            }
        });
    }

    private void handleImages(List<ImageItem> data) {
        adapter.addItems(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* 预览(可删除)后获取结果刷新已选择的图片 */
        if (requestCode == PreviewDelActivity.REQUEST_PREVIEW_CODE) {
            if (resultCode == RESULT_OK) {
                List<ImageItem> imageItems = (List<ImageItem>) data.getSerializableExtra(PreviewDelActivity.KEY_PREVIEW_DEL_DATA);
                adapter.refreshData(imageItems);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImagePicker.getInstance().clear();
    }
}
