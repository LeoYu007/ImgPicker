package com.adam.imgpicker.adapter.baseadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView的通用Adapter
 * 构造方法中的参数int... layoutIds用于多item类型:
 * 如果传入多个布局，需要复写getItemViewType，返回值用于layoutIds数组的角标
 *
 * @author yu
 */
public abstract class RecyclerAdapter<D> extends RecyclerView.Adapter<ViewHolder> implements DataHelper<D> {

    protected List<D> mDataSet;
    private int[] layoutIds;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * @param data      数据集
     * @param layoutIds 布局ids
     */
    public RecyclerAdapter(List<D> data, int... layoutIds) {
        if (data == null)
            this.mDataSet = new ArrayList<>();
        else
            this.mDataSet = data;
        this.layoutIds = layoutIds;
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutIds[viewType], parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final D item = getItem(position);
        onBindData(holder, position, item);
        setupItemClickListener(holder, position);
        setupItemLongClickListener(holder, position);
    }

    /**
     * 绑定数据到Item View上
     *
     * @param holder   viewholder
     * @param position 数据的位置
     * @param item     数据项
     */
    protected abstract void onBindData(ViewHolder holder, int position, D item);

    protected void setupItemClickListener(final ViewHolder viewHolder, final int position) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(pos, getItem(pos));
                }
            }
        });
    }

    protected void setupItemLongClickListener(final ViewHolder viewHolder, final int position) {
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                if (mOnItemLongClickListener != null) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(pos, getItem(pos));
                }
                return false;
            }
        });
    }

    public List<D> getDataSet() {
        return mDataSet;
    }

    @Override
    public boolean contains(D d) {
        return mDataSet.contains(d);
    }

    @Override
    public void addItem(D item) {
        mDataSet.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void addItems(List<D> items) {
        if (items != null && !items.isEmpty()) {
            mDataSet.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public void addItemToHead(D item) {
        mDataSet.add(0, item);
        notifyDataSetChanged();
    }

    @Override
    public void addItemsToHead(List<D> items) {
        if (items != null && !items.isEmpty()) {
            mDataSet.addAll(0, items);
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(int position) {
        mDataSet.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void remove(D item) {
        mDataSet.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    @Override
    public void refreshData(List<D> items) {
        mDataSet.clear();
        if (items != null && !items.isEmpty())
            mDataSet.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public D getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public void modify(D oldData, D newData) {
        modify(mDataSet.indexOf(oldData), newData);
    }

    @Override
    public void modify(int index, D newData) {
        mDataSet.set(index, newData);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Object item);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, Object item);
    }

}
