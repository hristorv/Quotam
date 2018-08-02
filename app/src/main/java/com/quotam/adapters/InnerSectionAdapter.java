package com.quotam.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class InnerSectionAdapter extends RecyclerView.Adapter {

    abstract void addItems(List<Object> addedItems);

    abstract void changeItems(List<Object> newItems);

}
