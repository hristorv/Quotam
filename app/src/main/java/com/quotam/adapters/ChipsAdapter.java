package com.quotam.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quotam.R;

import java.util.ArrayList;
import java.util.List;

public class ChipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Chip> items;
    private OnCloseListener onCloseListener;

    public ChipsAdapter() {
        this.items = new ArrayList<>();
    }

    public ChipsAdapter(List<Chip> items) {
        this.items = items;
    }

    public boolean addChip(Chip chip) {
        if (items.contains(chip))
            return false;
        items.add(chip);
        notifyItemInserted(items.size() - 1);
        return true;
    }

    public List<Chip> getChips() {
        return items;
    }

    public void removeChip(int position) {
        if (position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void setCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View chipView = inflater.inflate(R.layout.chips_item, parent, false);
        viewHolder = new ChipViewHolder(chipView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChipViewHolder chipViewHolder = (ChipViewHolder) holder;

        Chip chip = items.get(position);

        chipViewHolder.text.setText(chip.getText());
        chipViewHolder.closeButton.setOnClickListener(new CloseOnClickListener(chipViewHolder));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnCloseListener {
        void onClose();
    }

    public static class Chip {
        String text;

        public Chip(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public boolean equals(Object object) {
            if (this.text.equals(((Chip) object).getText())) {
                return true;
            }

            return false;
        }
    }

    private static class ChipViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public ImageButton closeButton;
        public View itemView;

        public ChipViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            text = itemView.findViewById(R.id.chip_text);
            closeButton = itemView.findViewById(R.id.chip_button);
        }
    }

    private class CloseOnClickListener implements View.OnClickListener {

        private ChipViewHolder holder;

        CloseOnClickListener(ChipViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            int position = holder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                removeChip(position);
                if (onCloseListener != null)
                    onCloseListener.onClose();
            }
        }
    }
}
