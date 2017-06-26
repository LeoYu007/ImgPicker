package com.adam.imgpicker.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.adam.imgpicker.R;
import com.adam.imgpicker.adapter.ImageFolderAdapter;
import com.adam.imgpicker.adapter.baseadapter.RecyclerAdapter;
import com.adam.imgpicker.entity.ImageFolder;
import com.adam.imgpicker.util.ScreenUtils;


/**
 * 选择图片文件夹的弹层
 * Created by yu on 2017/4/17.
 */
public class FolderPopUpWindow extends PopupWindow implements View.OnClickListener {

    private RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;

    public FolderPopUpWindow(final Context context, final ImageFolderAdapter adapter) {
        super(context);

        final LinearLayout view = new LinearLayout(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        view.setOrientation(LinearLayout.VERTICAL);
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        recyclerView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        view.addView(recyclerView);

        View marginView = new View(context);
        marginView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) context.getResources().getDimension(R.dimen.bar_height)));
        marginView.setOnClickListener(this);
        view.addView(marginView);

//        final View view = View.inflate(context, R.layout.pop_folder, null);
//        view.findViewById(R.id.margin).setOnClickListener(this);

//        recyclerView = (RecyclerView) view.findViewById(R.id.folderRv);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0));
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int maxHeight = ScreenUtils.height((Activity) view.getContext()) * 5 / 8;
                int realHeight = recyclerView.getHeight();
                ViewGroup.LayoutParams listParams = recyclerView.getLayoutParams();
                listParams.height = realHeight > maxHeight ? maxHeight : realHeight;
                recyclerView.setLayoutParams(listParams);
            }
        });
//         setAnimationStyle(R.style.popwin_anim_style);//  TODO: 2017/4/14  动画

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                adapter.setSelected(position);
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(adapter.getItem(position));
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ImageFolder folder);
    }
}
