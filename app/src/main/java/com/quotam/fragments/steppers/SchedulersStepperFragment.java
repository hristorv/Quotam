package com.quotam.fragments.steppers;

import android.support.v4.app.Fragment;

import com.quotam.R;
import com.quotam.adapters.ViewPagerAdapter;
import com.quotam.controller.SchedulerController;
import com.quotam.fragments.SchedulersFragment;
import com.quotam.model.Scheduler;

public class SchedulersStepperFragment extends StepperFragment {
    Scheduler scheduler = new Scheduler();

    @Override
    protected void addFragments(ViewPagerAdapter adapter) {
        adapter.addFragment(new SchedulerType(), getResources().getString(R.string.type));
        adapter.addFragment(new SchedulerTime(), getResources().getString(R.string.time));
        adapter.addFragment(new SchedulerItems(), getResources().getString(R.string.items));
    }

    @Override
    protected void applyChanges() {
        StepperPageFragment curFragment = getCurrentFragment();
        curFragment.applyChanges(scheduler);
    }

    @Override
    protected void finish() {
        new SchedulerController().addScheduler(getContext(), scheduler);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof SchedulersFragment) {
            ((SchedulersFragment) parentFragment).onStepperDone();
        }

    }


}
