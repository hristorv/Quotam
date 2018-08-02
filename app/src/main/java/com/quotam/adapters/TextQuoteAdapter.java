package com.quotam.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quotam.R;
import com.quotam.model.TextQuote;

import java.util.List;

public class TextQuoteAdapter extends RecyclerView.Adapter {


    private final List<TextQuote> items;
    private CompactOnItemClickListener onItemClickListner;

    public TextQuoteAdapter(List<TextQuote> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View textQuote = inflater.inflate(R.layout.text_quote_item, parent, false);
        TextQuoteViewHolder viewHolder = new TextQuoteViewHolder(textQuote);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TextQuoteViewHolder textQuoteViewHolder = (TextQuoteViewHolder) holder;
        configureTextQuoteViewHolder(textQuoteViewHolder, position);
        textQuoteViewHolder.itemView.setOnClickListener(new OnClickListener(textQuoteViewHolder));
    }

    private void configureTextQuoteViewHolder(TextQuoteViewHolder textQuoteViewHolder, int position) {
        TextQuote textQuote = items.get(position);
        if (textQuote != null) {
            textQuoteViewHolder.text.setText(textQuote.getText());
            textQuoteViewHolder.author.setText(textQuote.getAuthor());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(CompactOnItemClickListener onItemClickListener) {
        this.onItemClickListner = onItemClickListener;
    }

    public interface CompactOnItemClickListener {
        void onItemClick(TextQuote textQuote);
    }

    private class OnClickListener implements View.OnClickListener {


        private final TextQuoteViewHolder holder;

        public OnClickListener(TextQuoteViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            int position = holder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onItemClickListner != null)
                onItemClickListner.onItemClick(items.get(position));
        }
    }

    private class TextQuoteViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView author;
        public View itemView;

        public TextQuoteViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            text = itemView.findViewById(R.id.textquote_text);
            author = itemView.findViewById(R.id.textquote_author);
        }
    }

}
