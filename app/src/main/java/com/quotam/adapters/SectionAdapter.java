package com.quotam.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.quotam.R;
import com.quotam.model.Section;
import com.quotam.listeners.SystemUI;
import com.quotam.adapters.ViewHolders.SectionViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Section> items;
    private final boolean simpleLayout;
    private int maxItemsLoading;

    public SectionAdapter(List<Section> items, boolean simpleLayout) {
        this.simpleLayout = simpleLayout;
        this.items = items;
    }

    public SectionAdapter(List<Section> items, boolean simpleLayout, int maxItemsLoading) {
        this(items, simpleLayout);
        this.maxItemsLoading = maxItemsLoading;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View sectionView = inflater.inflate(R.layout.section, parent, false);
        SectionViewHolder sectionViewHolder = new SectionViewHolder(sectionView);
        configureGrid(sectionViewHolder.itemsGrid);
        configureSimpleLayout(sectionViewHolder);
        return sectionViewHolder;
    }

    private void configureSimpleLayout(SectionViewHolder sectionViewHolder) {
        if (simpleLayout) {
            sectionViewHolder.time.setVisibility(View.GONE);
            sectionViewHolder.button.setVisibility(View.GONE);
        }
    }

    private void configureGrid(RecyclerView grid) {
        InnerSectionAdapter adapter = createInnerAdapter();
        adapter.setHasStableIds(true);
        grid.setAdapter(adapter);
        RecyclerView.LayoutManager manager = createInnerLayoutManager();
        manager.setItemPrefetchEnabled(true);
        grid.setLayoutManager(manager);
//        grid.setNestedScrollingEnabled(false);
//        grid.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
    }

    protected abstract RecyclerView.LayoutManager createInnerLayoutManager();

    protected abstract InnerSectionAdapter createInnerAdapter();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SectionViewHolder sectionViewHolder = (SectionViewHolder) viewHolder;
        configure(sectionViewHolder, position);
        //sectionViewHolder.itemView.setOnClickListener();
    }

    private void configure(final SectionViewHolder sectionViewHolder, int position) {
        Context context = sectionViewHolder.itemView.getContext();
        final Section section = items.get(position);
        RecyclerView grid = sectionViewHolder.itemsGrid;
        final InnerSectionAdapter adapter = (InnerSectionAdapter) grid.getAdapter();

        final boolean hasFooter = addItems(section, adapter);

        //new SystemUI().adjustGridColumnNum(context, grid, false);
        // Header
        sectionViewHolder.name.setText(section.header.getName());
        if (section.header.getType().isEmpty())
            sectionViewHolder.header_divider.setVisibility(View.GONE);
        else {
            sectionViewHolder.header_divider.setVisibility(View.VISIBLE);
            sectionViewHolder.type.setText(section.header.getType());
        }
        if (!simpleLayout)
            sectionViewHolder.time.setText(section.header.getTime());
        // Footer
        if (hasFooter) {
            sectionViewHolder.show_more_button.setVisibility(View.VISIBLE);
            int itemsleft = section.items.size() - maxItemsLoading;
            changeFooterCount(sectionViewHolder, String.valueOf(itemsleft));
            sectionViewHolder.show_more_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hasFooter) {
                        showMore(sectionViewHolder, adapter, section);
                    }
                }
            });
        } else {
            sectionViewHolder.show_more_button.setVisibility(View.GONE);
        }
    }

    private void changeFooterCount(SectionViewHolder sectionViewHolder, String count) {
        sectionViewHolder.count.setText("(" + count + ")");
    }

    private void showMore(SectionViewHolder sectionViewHolder, InnerSectionAdapter adapter, Section section) {
        int nextSize = adapter.getItemCount() + maxItemsLoading;
        if (nextSize < section.items.size()) {
            // Adds + 1 to nextVisibleItem, because subList toIndex is exclusive.
            adapter.addItems(getItemsSublist(section, adapter.getItemCount(), nextSize));
            // Adds + 1 to lastVisibleItem to get last items count.
            int itemsLeft = section.items.size() - adapter.getItemCount();
            changeFooterCount(sectionViewHolder, String.valueOf(itemsLeft));
        } else {
            adapter.addItems(getItemsSublist(section, adapter.getItemCount(), section.items.size()));
            sectionViewHolder.show_more_button.setVisibility(View.GONE);
            sectionViewHolder.count.setVisibility(View.GONE);
        }
    }

    private List<Object> getItemsSublist(Section section, int fromIndex, int toIndex) {
        return new ArrayList<>(section.items.subList(fromIndex, toIndex));
    }

    private boolean addItems(Section section, InnerSectionAdapter adapter) {
        int itemsSize = section.items.size();
        final boolean hasFooter = (maxItemsLoading > 0 && itemsSize > maxItemsLoading);
        if (hasFooter) {
            adapter.changeItems(getItemsSublist(section, 0, maxItemsLoading));
        } else
            adapter.changeItems(section.items);
        return hasFooter;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
