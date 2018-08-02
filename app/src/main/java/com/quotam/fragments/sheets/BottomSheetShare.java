package com.quotam.fragments.sheets;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import com.quotam.R;
import com.quotam.model.Constants;

public class BottomSheetShare extends BottomSheetDialogFragment {

    private Intent imageIntent;
    private static final int SHARE_IMAGE_REQUEST_CODE = 0;
    private AppAdapter adapter;
    private Parcelable bitmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitmap = getArguments().getParcelable(Constants.PARCELABLE.BITMAP);
        imageIntent = new Intent();
        imageIntent.setAction(Intent.ACTION_SEND);
        imageIntent.setType("image/jpeg");
        PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> launchables = packageManager.queryIntentActivities(
                imageIntent, 0);
        Collections.sort(launchables,
                new ResolveInfo.DisplayNameComparator(packageManager));
        adapter = new AppAdapter(packageManager, launchables);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_sheet_base, container,
                false);
        View base = inflater.inflate(R.layout.bottom_sheet_grid, (ViewGroup) rootView, true);
        RecyclerView grid = base.findViewById(R.id.bottom_sheet_grid);
        TextView titleView = base.findViewById(R.id.bottom_sheet_title);
        titleView.setText(R.string.share_with);
        grid.setAdapter(adapter);
        adjustGrid(grid);
        return rootView;
    }

    private void adjustGrid(RecyclerView grid) {
        GridLayoutManager manager = new GridLayoutManager(getActivity(),3);
        grid.setLayoutManager(manager);
        grid.setHasFixedSize(true);
        //new SystemUI().adjustGridColumnNum(getActivity(), grid, false);
    }

    private class LaunchAsyncTask extends AsyncTask<Void, Void, Void> {


        private final ResolveInfo launchable;

        public LaunchAsyncTask(ResolveInfo launchable) {
            this.launchable = launchable;
        }

        @Override
        protected void onPreExecute() {
            //dialogWait.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            //dialogWait.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(final Void... params) {
            imageIntent.putExtra(Intent.EXTRA_STREAM,
                    bitmap);
            ActivityInfo activity = launchable.activityInfo;
            ComponentName name = new ComponentName(
                    activity.applicationInfo.packageName,
                    activity.name);
            imageIntent
                    .addCategory(Intent.CATEGORY_LAUNCHER);
            imageIntent.setComponent(name);
            startActivityForResult(imageIntent,
                    SHARE_IMAGE_REQUEST_CODE);
            return null;
        }
    }

    private class AppAdapter extends RecyclerView.Adapter<ResolveInfoViewHolder> {

        private final List<ResolveInfo> launchables;
        private final PackageManager packageManager;

        public AppAdapter(PackageManager packageManager, List<ResolveInfo> launchables) {
            this.packageManager = packageManager;
            this.launchables = launchables;
        }

        @Override
        public ResolveInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View shareItem = inflater.inflate(R.layout.bottom_sheet_share_item, parent, false);
            ResolveInfoViewHolder viewHolder = new ResolveInfoViewHolder(shareItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ResolveInfoViewHolder holder, final int position) {
            final ResolveInfo launchable = launchables.get(position);
            holder.label.setText(launchable.loadLabel(packageManager));
            holder.icon.setImageDrawable(launchable.loadIcon(packageManager));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new LaunchAsyncTask(launchable).execute();
                }
            });
        }

        @Override
        public int getItemCount() {
            return launchables.size();
        }
    }

    private class ResolveInfoViewHolder extends RecyclerView.ViewHolder {
        private final TextView label;
        private final ImageView icon;
        public View itemView;

        public ResolveInfoViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            label = itemView.findViewById(R.id.label);
            icon = itemView.findViewById(R.id.icon);
        }

    }

}
