package com.quotam.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quotam.R;
import com.quotam.utils.Convertor;
import com.quotam.utils.DrawableTint;

import java.util.ArrayList;
import java.util.List;

public class CustomBottomBar extends LinearLayout {

    private static final int SCROLLABLE_ITEM_WIDTH = 70;
    private static final int SCROLLABLE_ITEM_MARGIN = 5;
    private OnMenuClickListener onMenuClickListener;
    private Menu menu;
    private Context context;
    private ArrayList<Badge> badges;
    private Animation expandIn;
    private Mode mode;
    private View lastSelectedView;
    private int curSelectedItemPosition = 0;
    private ArrayList<Integer> selectedItemsPositions = new ArrayList<>();
    private boolean onClickFromUser;

    public enum Mode {
        NORMAL, SINGLE, MULTI
    }


    public CustomBottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void addMenu(List<CharSequence> items, Context context, boolean showTitle, Mode mode, OnMenuClickListener onMenuClickListener) {

        PopupMenu popupMenu = new PopupMenu(context, null);
        menu = popupMenu.getMenu();
        for (int i = 0; i < items.size(); i++)
            menu.add(Menu.NONE, Menu.NONE, i, items.get(i));

        setup(context, showTitle, mode, onMenuClickListener);
    }

    public void addMenu(int menuID, Context context, boolean showTitle, Mode mode, OnMenuClickListener onMenuClickListener) {

        PopupMenu popupMenu = new PopupMenu(context, null);
        menu = popupMenu.getMenu();
        popupMenu.getMenuInflater().inflate(menuID, menu);

        setup(context, showTitle, mode, onMenuClickListener);
    }

    private void setup(Context context, boolean showTitle, Mode mode, OnMenuClickListener onMenuClickListener) {
        this.context = context;
        this.mode = mode;
        this.onMenuClickListener = onMenuClickListener;
        this.onClickFromUser = true;
        update(showTitle);
        expandIn = AnimationUtils.loadAnimation(this.context, R.anim.button_popup);
        badges = new ArrayList<>(menu.size());
        if (mode == Mode.SINGLE)
            setSelectedItem(0);
    }

    public void setSelectedItem(int position) {
        if (position >= 0 && position < getChildCount()) {
            onClickFromUser = false;
            getChildAt(position).callOnClick();
        }
    }

    public int getSelectedItemPosition() {
        return curSelectedItemPosition;
    }

    public void setSelectedItemsPositions(ArrayList<Integer> newItemsPositions) {
        if (newItemsPositions != null) {
            this.selectedItemsPositions = newItemsPositions;
            for (Integer position : selectedItemsPositions) {
                MenuItem selectedItem = menu.getItem(position);
                View selectedView = getChildAt(position);
                select(selectedView, selectedItem);
            }
        }
    }

    public ArrayList<Integer> getSelectedItemsPositions() {
        return selectedItemsPositions;
    }

    private void checkForScrolling(ViewGroup viewGroup) {
        ViewParent parent = getParent();
        if (parent instanceof HorizontalScrollView) {
            LinearLayout.LayoutParams params = (LayoutParams) viewGroup.getLayoutParams();
            params.width = Convertor.convertDpToPixel(SCROLLABLE_ITEM_WIDTH);
            params.leftMargin = Convertor.convertDpToPixel(SCROLLABLE_ITEM_MARGIN);
            params.rightMargin = Convertor.convertDpToPixel(SCROLLABLE_ITEM_MARGIN);
        }
    }

    private void update(boolean showTitle) {
        if (menu != null) {
            this.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(context);
            for (int position = 0; position < menu.size(); position++) {
                MenuItem item = menu.getItem(position);
                CharSequence title = item.getTitle();
                Drawable icon = item.getIcon();
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.custom_bottom_bar_item, this, false);
                checkForScrolling(layout);
                ImageView iconView = layout.findViewById(R.id.bottom_bar_item_icon);
                TextView titleView = layout.findViewById(R.id.bottom_bar_item_title);
                if (icon != null) {
                    iconView.setImageDrawable(icon);
                    iconView.setContentDescription(title);
                } else {
                    // If there is no icon, than we show only text and increase the size
                    showTitle = true;
                    titleView.setTextSize(16);
                }
                if (showTitle) {
                    titleView.setText(title);
                    titleView.setVisibility(View.VISIBLE);
                }
                layout.setOnClickListener(new ClickListener(position));
                this.addView(layout, position);
            }
        }
    }


    public void addBadge(int index) {
        View view = getChildAt(index);
        Badge badge = new Badge(context, view, index, R.color.accent_color, R.color.primary_text_color);
        badges.add(index, badge);
    }

    public void setBadgeCount(int index, int count) {
        Badge badge = badges.get(index);
        if (count <= 0 && badge.isVisible())
            badge.hide();
        else if (count > 0 && !badge.isVisible())
            badge.show();
        badge.setCount(count);
    }

    public Badge getBadge(int index) {
        return badges.get(index);
    }

    public void setTextSize(int size) {
        for (int i = 0; i < getChildCount(); i++) {
            LinearLayout child = (LinearLayout) getChildAt(i);
            TextView textView = child.findViewById(R.id.bottom_bar_item_title);
            textView.setTextSize(size);
        }
    }

    public CharSequence getItemTitle(int position) {
        LinearLayout child = (LinearLayout) getChildAt(position);
        TextView textView = child.findViewById(R.id.bottom_bar_item_title);
        return textView.getText();
    }

    public void changeItemTitle(int position, CharSequence title) {
        LinearLayout child = (LinearLayout) getChildAt(position);
        TextView textView = child.findViewById(R.id.bottom_bar_item_title);
        textView.setText(title);
    }

    public interface OnMenuClickListener {
        void onMenuItemClick(MenuItem item);
    }

    private void animateSelected(View selectedView) {
        if (selectedView != null)
            selectedView.startAnimation(expandIn);
    }

    private void animateItemColors(View view, int colorID) {
        ImageView icon = view.findViewById(R.id.bottom_bar_item_icon);
        Drawable iconDrawable = icon.getDrawable();
        TextView text = view.findViewById(R.id.bottom_bar_item_title);
        if (iconDrawable != null) {
            Drawable drawable = DrawableTint.tintDrawable(getContext(), iconDrawable, colorID);
            icon.setImageDrawable(drawable);
        }
        text.setTextColor(getResources().getColor(colorID));
    }

    private void checkMenuItem(MenuItem item) {
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setChecked(false);
        }
        item.setChecked(true);
    }

    private void select(View selectedView, MenuItem item) {
        animateItemColors(selectedView, R.color.accent_color);
        selectedView.setSelected(true);
        item.setChecked(true);
    }

    private void deselect(View selectedView, MenuItem item) {
        animateItemColors(selectedView, R.color.white);
        selectedView.setSelected(false);
        item.setChecked(false);
    }

    private class ClickListener implements OnClickListener {

        int position;

        public ClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View selectedView) {
            MenuItem selectedItem = menu.getItem(position);
            if (onClickFromUser)
                animateSelected(selectedView);
            if (mode == Mode.MULTI) {
                if (selectedView.isSelected()) {
                    deselect(selectedView, selectedItem);
                    selectedItemsPositions.remove(Integer.valueOf(position));
                } else {
                    select(selectedView, selectedItem);
                    selectedItemsPositions.add(Integer.valueOf(position));
                }
            }
            if (mode == Mode.SINGLE) {
                if (!selectedView.isSelected()) {
                    select(selectedView, selectedItem);
                    if (lastSelectedView != null)
                        deselect(lastSelectedView, selectedItem);
                    lastSelectedView = selectedView;
                    curSelectedItemPosition = position;
                }
            }
            if (onMenuClickListener != null)
                onMenuClickListener.onMenuItemClick(selectedItem);
            onClickFromUser = true;
        }
    }

}

