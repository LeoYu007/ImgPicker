package com.adam.imgpicker.adapter.baseadapter;

import java.util.List;

/**
 * 提供操作数据的api
 *
 * @author yu.
 */
public interface DataHelper<D> {

    D getItem(int position);

    boolean contains(D d);

    void addItem(D item);

    void addItems(List<D> items);

    void addItemToHead(D item);

    void addItemsToHead(List<D> items);

    void remove(int position);

    void remove(D item);

    void modify(D oldData, D newData);

    void modify(int index, D newData);

    void clear();

    void refreshData(List<D> items);

}
