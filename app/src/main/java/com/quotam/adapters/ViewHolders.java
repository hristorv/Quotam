package com.quotam.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quotam.R;

public class ViewHolders {

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private static final String DEFAULT_LIKES_SIGN = "-";
        public ImageView imageView;
        public TextView likes;
        public View itemView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ImageViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be
            // used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.picture_imageview);
            likes = itemView.findViewById(R.id.picture_likes_count);
            likes.setText(DEFAULT_LIKES_SIGN);
        }
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public ImageView imageView;
        public TextView titleBar;
        public TextView likes;
        public TextView pictures;
        public View itemView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public AlbumViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be
            // used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.compact_item_background);
            likes = itemView.findViewById(R.id.album_likes_count);
            pictures = itemView.findViewById(R.id.album_pictures_count);
            titleBar = itemView.findViewById(R.id.compact_item_title);
        }
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public ImageView profileAvatar;
        public ImageView profileBackground;
        public TextView likesCount;
        public TextView picturesCount;
        public TextView albumsCount;
        public TextView profileName;
        public View itemView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ArtistViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be
            // used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.itemView = itemView;
            profileAvatar = itemView.findViewById(R.id.profile_image_avatar);
            profileBackground = itemView.findViewById(R.id.profile_image_background);
            profileName = itemView.findViewById(R.id.profile_name);
            likesCount = itemView.findViewById(R.id.profile_count_likes);
            picturesCount = itemView.findViewById(R.id.profile_count_pictures);
            albumsCount = itemView.findViewById(R.id.profile_count_albums);
        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public ImageView imageView;
        public TextView titleBar;
        public View itemView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public CategoryViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be
            // used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.category_image);
            titleBar = itemView.findViewById(R.id.category_title);
        }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        public ImageView profileAvatar;
        public TextView profileName;
        public TextView commentText;
        public View itemView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            profileAvatar = itemView.findViewById(R.id.profile_avatar);
            profileName = itemView.findViewById(R.id.profile_name);
            commentText = itemView.findViewById(R.id.comment_text);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView type;
        public TextView time;
        public View itemView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            name = itemView.findViewById(R.id.header_name);
            type = itemView.findViewById(R.id.header_type);
            time = itemView.findViewById(R.id.header_time);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public Button show_more;
        public TextView count;
        public View itemView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            count = itemView.findViewById(R.id.footer_count);
            show_more = itemView.findViewById(R.id.footer_show_more);
        }
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        public final TextView name;
        public final TextView type;
        public final TextView time;
        public final TextView count;
        public final LinearLayout show_more_button;
        public final ImageButton button;
        public final View header_divider;
        public View itemView;
        public final RecyclerView itemsGrid;

        public SectionViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemsGrid = itemView.findViewById(R.id.section_items_grid);
            // Header
            name = itemView.findViewById(R.id.header_name);
            type = itemView.findViewById(R.id.header_type);
            time = itemView.findViewById(R.id.header_time);
            button = itemView.findViewById(R.id.header_button);
            header_divider = itemView.findViewById(R.id.header_divider);
            // Footer
            count = itemView.findViewById(R.id.footer_count);
            show_more_button = itemView.findViewById(R.id.footer_button);
        }
    }

    public static class SchedulerViewHolder extends RecyclerView.ViewHolder {
        public final TextView typeTextView;
        public final TextView scheduleTextView;
        public final RecyclerView collectionGrid;
        public final Button deleteButton;
        public final Button disableButton;
        public View itemView;

        public SchedulerViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.typeTextView = itemView.findViewById(R.id.scheduler_type);
            this.scheduleTextView = itemView.findViewById(R.id.scheduler_schedule);
            this.collectionGrid = itemView.findViewById(R.id.scheduler_collection_grid);
            this.deleteButton = itemView.findViewById(R.id.scheduler_delete);
            this.disableButton = itemView.findViewById(R.id.scheduler_disable);

        }

    }
}
