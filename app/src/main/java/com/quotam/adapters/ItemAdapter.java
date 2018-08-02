package com.quotam.adapters;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.quotam.R;
import com.quotam.model.Album;
import com.quotam.model.Artist;
import com.quotam.model.Category;
import com.quotam.model.Constants;
import com.quotam.model.Image;
import com.quotam.model.SectionFooter;
import com.quotam.model.SectionHeader;
import com.quotam.utils.Helper;
import com.quotam.fragments.GalleryFragment;
import com.quotam.fragments.InAlbumFragment;
import com.quotam.fragments.InArtistFragment;
import com.quotam.fragments.InCategoryFragment;

public class ItemAdapter extends InnerSectionAdapter {

    public final int IMAGE = 0;
    public final int ALBUM = 1;
    public final int ARTIST = 2;
    public final int CATEGORY = 3;
    public final int HEADER = 4;
    public final int FOOTER = 5;
    // The items to display in your RecyclerView
    private List<Object> items;
    RequestOptions options;
    private int lastPosition = -1;

    public ItemAdapter() {
        this.items = new ArrayList<Object>();
        setImageLoaderOptions();
    }

    public ItemAdapter(List<Object> items) {
        this.items = items;
        setImageLoaderOptions();
    }

    @Override
    public void addItems(List<Object> addedItems) {
        int previousItemsCount = items.size();
        items.addAll(addedItems);
        notifyItemRangeInserted(previousItemsCount, addedItems.size());
    }

    @Override
    public void changeItems(List<Object> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    private void setImageLoaderOptions() {
        options = new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .dontAnimate();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case IMAGE:
                View imageView = inflater.inflate(R.layout.fragment_pictures_item_v2, parent, false);
                viewHolder = new ViewHolders.ImageViewHolder(imageView);
                break;
            case ALBUM:
                View albumView = inflater.inflate(R.layout.fragment_albums_item, parent, false);
                viewHolder = new ViewHolders.AlbumViewHolder(albumView);
                break;
            case ARTIST:
                View artistView = inflater.inflate(R.layout.fragment_artists_item, parent, false);
                viewHolder = new ViewHolders.ArtistViewHolder(artistView);
                break;
            case CATEGORY:
                View categoryView = inflater.inflate(R.layout.fragment_categories_item, parent, false);
                viewHolder = new ViewHolders.CategoryViewHolder(categoryView);
                break;
            case HEADER:
                View headerView = inflater.inflate(R.layout.section_header, parent, false);
                viewHolder = new ViewHolders.HeaderViewHolder(headerView);
                break;
            case FOOTER:
                View footerView = inflater.inflate(R.layout.section_footer, parent, false);
                viewHolder = new ViewHolders.FooterViewHolder(footerView);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case IMAGE:
                ViewHolders.ImageViewHolder imageViewHolder = (ViewHolders.ImageViewHolder) viewHolder;
                configureImageViewHolder(imageViewHolder, position);
                imageViewHolder.itemView.setOnClickListener(new ItemOnClickListener(new GalleryFragment(), imageViewHolder));
                break;
            case ALBUM:
                ViewHolders.AlbumViewHolder albumViewHolder = (ViewHolders.AlbumViewHolder) viewHolder;
                configureAlbumViewHolder(albumViewHolder, position);
                albumViewHolder.itemView.setOnClickListener(new ItemOnClickListener(new InAlbumFragment(), albumViewHolder));
                break;
            case ARTIST:
                ViewHolders.ArtistViewHolder artistViewHolder = (ViewHolders.ArtistViewHolder) viewHolder;
                configureArtistViewHolder(artistViewHolder, position);
                artistViewHolder.itemView.setOnClickListener(new ItemOnClickListener(new InArtistFragment(), artistViewHolder));
                setFullSpan(viewHolder);
                break;
            case CATEGORY:
                ViewHolders.CategoryViewHolder categoryViewHolder = (ViewHolders.CategoryViewHolder) viewHolder;
                configureCategoryViewHolder(categoryViewHolder, position);
                categoryViewHolder.itemView.setOnClickListener(new ItemOnClickListener(new InCategoryFragment(), categoryViewHolder));
                setFullSpan(viewHolder);
                break;
            case HEADER:
                ViewHolders.HeaderViewHolder headerViewHolder = (ViewHolders.HeaderViewHolder) viewHolder;
                configureHeaderViewHolder(headerViewHolder, position);
                setFullSpan(viewHolder);
                break;
            case FOOTER:
                ViewHolders.FooterViewHolder footerViewHolder = (ViewHolders.FooterViewHolder) viewHolder;
                configureFooterViewHolder(footerViewHolder, position);
                setFullSpan(viewHolder);
                break;
        }
//        Animation animation = AnimationUtils.loadAnimation(context,
//                (position > lastPosition) ? R.anim.slide_in_bottom
//                        : R.anim.slide_in_top);
//        if (position % 2 == 0)
//            animation.setStartOffset(50);
//        viewHolder.itemView.startAnimation(animation);

        lastPosition = position;
    }

    private void setFullSpan(RecyclerView.ViewHolder viewHolder) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
        layoutParams.setFullSpan(true);
    }


    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    private void configureFooterViewHolder(ViewHolders.FooterViewHolder footerViewHolder, int position) {

        final SectionFooter footer = (SectionFooter) items.get(position);
        if (footer.isActive()) {
            footerViewHolder.show_more.setVisibility(View.VISIBLE);
            footerViewHolder.count.setVisibility(View.VISIBLE);
            footerViewHolder.count.setText("(" + footer.getCount() + ")");
            footerViewHolder.show_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private void configureHeaderViewHolder(ViewHolders.HeaderViewHolder headerViewHolder, int position) {

        final SectionHeader header = (SectionHeader) items.get(position);
        headerViewHolder.name.setText(header.getName());
        headerViewHolder.type.setText(header.getType());
        headerViewHolder.time.setText(header.getTime());

    }

    private void configureCategoryViewHolder(ViewHolders.CategoryViewHolder categoryViewHolder, int position) {

        final Category category = (Category) items.get(position);
        if (category != null) {
            Glide.with(categoryViewHolder.itemView.getContext())
                    .load(Helper.transform(category.getBackgroundUrl()))
                    .apply(options)
                    .into(categoryViewHolder.imageView);
            categoryViewHolder.titleBar.setText(category.getName());
        }

    }

    private void configureArtistViewHolder(ViewHolders.ArtistViewHolder artistViewHolder, int position) {
        final Artist artist = (Artist) items.get(position);
        if (artist != null) {
            Glide.with(artistViewHolder.itemView.getContext())
                    .load(Helper.transform(artist.getBackgroundUrl()))
                    .apply(options)
                    .into(artistViewHolder.profileBackground);
            Glide.with(artistViewHolder.itemView.getContext())
                    .load(Helper.transform(artist.getAvatarUrl()))
                    .apply(options)
                    .into(artistViewHolder.profileAvatar);
            artistViewHolder.profileName.setText(artist.getName());
            artistViewHolder.likesCount.setText(artist.getLikesCount());
            artistViewHolder.picturesCount.setText(artist.getPicturesCount());
            artistViewHolder.albumsCount.setText(artist.getAlbumsCount());
        }

    }

    private void configureAlbumViewHolder(ViewHolders.AlbumViewHolder albumViewHolder, int position) {
        final Album album = (Album) items.get(position);
        if (album != null) {
            Glide.with(albumViewHolder.itemView.getContext())
                    .load(Helper.transform(album.getBackgroundUrl()))
                    .apply(options)
                    .into(albumViewHolder.imageView);
            albumViewHolder.titleBar.setText(album.getName());
            albumViewHolder.pictures.setText(album.getPicturesCount());
            albumViewHolder.likes.setText(album.getLikesCount());
        }

    }

    private void configureImageViewHolder(final ViewHolders.ImageViewHolder imageViewHolder, final int position) {

        final Image image = (Image) items.get(position);
        Glide.with(imageViewHolder.itemView.getContext())
                .load(Helper.transform(image.getUrl()))
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageViewHolder.likes.setText(image.getLikes());
                        return false;
                    }
                })
                .into(imageViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof Image)
            return IMAGE;
        else if (item instanceof Album)
            return ALBUM;
        else if (item instanceof Artist)
            return ARTIST;
        else if (item instanceof Category)
            return CATEGORY;
        else if (item instanceof SectionHeader)
            return HEADER;
        else if (item instanceof SectionFooter)
            return FOOTER;
        return -1;

    }


    private class ItemOnClickListener implements View.OnClickListener {
        private final RecyclerView.ViewHolder holder;
        private Fragment fragment;

        public ItemOnClickListener(Fragment fragment, RecyclerView.ViewHolder holder) {
            this.fragment = fragment;
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            Fragment fragment = initFragment();
            if (fragment != null && !fragment.isAdded()) {
                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext())
                        .getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .add(R.id.content_frame, fragment)
                        .addToBackStack(
                                Constants.FragmentNames.BACKSTACK_FRAGMENT)
                        .commit();
            }
        }

        private Fragment initFragment() {
            int position = holder.getAdapterPosition();
            Bundle bundle = new Bundle();
            Object curItem = items.get(position);
            if (curItem instanceof Image) {
                List<Image> imagesList = new ArrayList<>();
                for (Object item : items) {
                    if (item instanceof Image)
                        imagesList.add((Image) item);
                }
                ArrayList<? extends Parcelable> imagesListParcelable = (ArrayList<? extends Parcelable>) imagesList;
                bundle.putParcelableArrayList(Constants.PARCELABLE.ADAPTER_LIST, imagesListParcelable);
            } else {
                Parcelable curItemParcelable = (Parcelable) curItem;
                bundle.putParcelable(Constants.PARCELABLE.ADAPTER_OBJECT, curItemParcelable);
            }
            bundle.putInt(Constants.Extra.POSITION, position);
            fragment.setArguments(bundle);
            return fragment;
        }
    }

}
