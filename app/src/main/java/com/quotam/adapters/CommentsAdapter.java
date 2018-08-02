package com.quotam.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.quotam.R;
import com.quotam.model.Comment;
import com.quotam.utils.Helper;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> items;
    RequestOptions options;
    private int lastPosition = -1;

    public CommentsAdapter(List<Object> items) {
        this.items = items;
        setImageLoaderOptions();
    }

    private void setImageLoaderOptions() {
        options = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .dontAnimate();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View commentView = inflater.inflate(R.layout.comment_item, parent, false);
        viewHolder = new ViewHolders.CommentViewHolder(commentView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolders.CommentViewHolder commentViewHolder = (ViewHolders.CommentViewHolder) holder;
        configureAlbumViewHolder(commentViewHolder, position);
        //albumViewHolder.itemView.setOnClickListener(new ItemOnClickListener(new InAlbumFragment(), position));

//        Animation animation = AnimationUtils.loadAnimation(context,
//                (position > lastPosition) ? R.anim.slide_in_bottom
//                        : R.anim.slide_in_top);
//        if (position % 2 == 0)
//            animation.setStartOffset(50);
//        viewHolder.itemView.startAnimation(animation);

        lastPosition = position;
    }

    private void configureAlbumViewHolder(ViewHolders.CommentViewHolder commentViewHolder, int position) {
        Context context = commentViewHolder.itemView.getContext();
        final Comment comment = (Comment) items.get(position);
        if (comment != null) {
            Glide.with(context)
                    .load(Helper.transform(comment.getProfileAvatarUrl()))
                    .apply(options)
                    .into(commentViewHolder.profileAvatar);
            commentViewHolder.profileName.setText(comment.getProfileName());
            commentViewHolder.commentText.setText(comment.getCommentText());

        }

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }


    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }
}
