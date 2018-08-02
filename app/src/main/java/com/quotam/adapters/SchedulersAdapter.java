package com.quotam.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.quotam.R;
import com.quotam.controller.SchedulerController;
import com.quotam.model.Scheduler;
import com.quotam.custom.CustomSnackbar;

import java.util.List;


public class SchedulersAdapter extends RecyclerView.Adapter {

    public static final int ENABLE_DISABLE_DURATION = 250;
    public static final float DISABLED_ALPHA = 0.3f;
    public static final float ENABLED_ALPHA = 1.0f;
    private final List<Scheduler> items;
    private final AlphaAnimation disableAnimation;
    private final AlphaAnimation enableAnimation;
    //private Typeface font;

    public SchedulersAdapter(List<Scheduler> items) {
        this.items = items;
        disableAnimation = new AlphaAnimation(ENABLED_ALPHA, DISABLED_ALPHA);
        enableAnimation = new AlphaAnimation(DISABLED_ALPHA, ENABLED_ALPHA);
        disableAnimation.setDuration(ENABLE_DISABLE_DURATION);
        disableAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        disableAnimation.setFillAfter(true);
        disableAnimation.setFillBefore(true);
        enableAnimation.setDuration(ENABLE_DISABLE_DURATION);
        enableAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        enableAnimation.setFillAfter(true);
        enableAnimation.setFillBefore(true);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View schedulerView = inflater.inflate(R.layout.scheduler_item, parent, false);
        viewHolder = new ViewHolders.SchedulerViewHolder(schedulerView);
        //if (font == null)
        //    font = Typeface.createFromAsset(parent.getContext().getAssets(), "coming_soon.ttf");
        return viewHolder;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        ViewHolders.SchedulerViewHolder schedulerViewHolder = (ViewHolders.SchedulerViewHolder) holder;
        if (!items.get(position).isEnabled()) {
            schedulerViewHolder.collectionGrid.setAlpha(DISABLED_ALPHA);
            schedulerViewHolder.typeTextView.setAlpha(DISABLED_ALPHA);
            schedulerViewHolder.scheduleTextView.setAlpha(DISABLED_ALPHA);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolders.SchedulerViewHolder schedulerViewHolder = (ViewHolders.SchedulerViewHolder) holder;
        configureSchedulerViewHolder(schedulerViewHolder, position);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    private void configureSchedulerViewHolder(ViewHolders.SchedulerViewHolder schedulerViewHolder, int position) {
        Scheduler scheduler = items.get(position);
        if (scheduler != null) {
            //schedulerViewHolder.deleteButton.setTypeface(font, Typeface.BOLD);
            //schedulerViewHolder.disableButton.setTypeface(font, Typeface.BOLD);
            schedulerViewHolder.scheduleTextView.setText(getScheduleText(scheduler));
            schedulerViewHolder.typeTextView.setText(scheduler.getType().toString());
            schedulerViewHolder.deleteButton.setOnClickListener(new DeleteOnClickListener(schedulerViewHolder));
            schedulerViewHolder.disableButton.setOnClickListener(new DisableOnClickListener(scheduler, schedulerViewHolder));
            if (scheduler.isEnabled()) {
                schedulerViewHolder.disableButton.setText(R.string.action_disable);
            } else {
                schedulerViewHolder.disableButton.setText(R.string.action_enable);
            }
            setupCollectionGrid(schedulerViewHolder, scheduler);
        }
    }

    private String getScheduleText(Scheduler scheduler) {
        Scheduler.TimeType timeType = scheduler.getTimeType();
        if (timeType == Scheduler.TimeType.INTERVAL) {
            Scheduler.Interval schedulerTime = scheduler.getInterval();
            return String.valueOf(schedulerTime.intervalInHours) + "h";
        } else if (timeType == Scheduler.TimeType.ALARM) {
            Scheduler.Alarm schedulerTime = scheduler.getAlarm();
            return String.valueOf(schedulerTime.hour) + ":" + String.valueOf(schedulerTime.minute) + " " + String.valueOf(schedulerTime.ampm).toUpperCase();
        }
        return "";
    }

    private void setupCollectionGrid(ViewHolders.SchedulerViewHolder schedulerViewHolder, Scheduler scheduler) {
        RecyclerView grid = schedulerViewHolder.collectionGrid;
        CompactItemAdapter adapter = new CompactItemAdapter(scheduler.getCollections(), CompactItemAdapter.ManagerType.LINEAR);
        adapter.setHasStableIds(true);
        grid.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(grid.getContext(), LinearLayoutManager.HORIZONTAL, false);
        manager.setInitialPrefetchItemCount(adapter.getItemCount());
        grid.setLayoutManager(manager);
        grid.setHasFixedSize(true);
        grid.setNestedScrollingEnabled(false);
    }

    private class DisableOnClickListener implements View.OnClickListener {

        private final Scheduler scheduler;
        private final ViewHolders.SchedulerViewHolder schedulerViewHolder;

        public DisableOnClickListener(Scheduler scheduler, ViewHolders.SchedulerViewHolder schedulerViewHolder) {
            this.scheduler = scheduler;
            this.schedulerViewHolder = schedulerViewHolder;
        }

        @Override
        public void onClick(View view) {
            Button disableButton = (Button) view;
            if (scheduler.isEnabled()) {
                scheduler.setEnabled(false);
                disableButton.setText(R.string.action_enable);
                // Animate disable
                schedulerViewHolder.collectionGrid.startAnimation(disableAnimation);
                schedulerViewHolder.typeTextView.startAnimation(disableAnimation);
                schedulerViewHolder.scheduleTextView.startAnimation(disableAnimation);
                CustomSnackbar.create(view.getContext(), view, R.string.disabled, Snackbar.LENGTH_SHORT).show();
            } else {
                scheduler.setEnabled(true);
                disableButton.setText(R.string.action_disable);
                // Animate enable
                schedulerViewHolder.collectionGrid.setAlpha(ENABLED_ALPHA);
                schedulerViewHolder.typeTextView.setAlpha(ENABLED_ALPHA);
                schedulerViewHolder.scheduleTextView.setAlpha(ENABLED_ALPHA);
                schedulerViewHolder.collectionGrid.startAnimation(enableAnimation);
                schedulerViewHolder.typeTextView.startAnimation(enableAnimation);
                schedulerViewHolder.scheduleTextView.startAnimation(enableAnimation);
                CustomSnackbar.create(view.getContext(), view, R.string.enabled, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private class DeleteOnClickListener implements View.OnClickListener {

        private final ViewHolders.SchedulerViewHolder schedulerViewHolder;

        public DeleteOnClickListener(ViewHolders.SchedulerViewHolder schedulerViewHolder) {
            this.schedulerViewHolder = schedulerViewHolder;
        }

        @Override
        public void onClick(final View view) {
            final Context context = view.getContext();
            AlertDialog dialog = new AlertDialog.Builder(context, R.style.CustomDialogTheme)
                    .setMessage(R.string.scheduler_delete_message)
                    .setTitle(R.string.scheduler_delete_title)
                    .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteScheduler(context);
                            dialogInterface.dismiss();
                            CustomSnackbar.create(view.getContext(), view, R.string.deleted, Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();

            dialog.show();
        }

        private void deleteScheduler(Context context) {
            int position = schedulerViewHolder.getAdapterPosition();

            //Scheduler scheduler = items.get(position);
            new SchedulerController().deleteScheduler(context, position);

            items.remove(position);
            SchedulersAdapter.this.notifyItemRemoved(position);
        }
    }
}
