package com.quotam.fragments.steppers;


import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.quotam.R;
import com.quotam.activities.MainMenuActivity;
import com.quotam.adapters.ViewPagerAdapter;
import com.quotam.custom.CustomSnackbar;
import com.quotam.custom.CustomStepper;
import com.quotam.custom.DisableableViewPager;

import java.util.ArrayList;
import java.util.List;

public abstract class StepperFragment extends Fragment {

    private CustomStepper stepper;
    private DisableableViewPager viewPager;
    private Snackbar snackbar;
    private Button nextButton;
    protected RelativeLayout baseLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainMenuActivity) getActivity()).setDrawerEnabled(false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle bundle = getArguments();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainMenuActivity) getActivity()).setDrawerEnabled(true);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stepper,
                container, false);
        setupStatusBarBackground(rootView);
        baseLayout = rootView.findViewById(R.id.stepper_base);
        nextButton = rootView.findViewById(R.id.bottom_layout_next);
        stepper = rootView.findViewById(R.id.custom_stepper);
        stepper.addNextButton(nextButton);
        viewPager = rootView.findViewById(R.id.stepper_viewpager);
        viewPager.setPagingEnabled(false);
        setupViewpager();
        stepper.init(viewPager);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
        return rootView;
    }

    private void setupStatusBarBackground(View rootView) {
        View statusBarBackground = rootView.findViewById(R.id.status_bar_background);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBarBackground.setVisibility(View.GONE);
        }
    }

    private void setupViewpager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        addFragments(adapter);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() + 1);
    }

    protected abstract void addFragments(ViewPagerAdapter adapter);

    protected abstract void finish();

    protected abstract void applyChanges();

    private void done() {
        StepperPageFragment curFragment = getCurrentFragment();
        String error = curFragment.isDone();
        if (error == null) {
            if (snackbar != null) snackbar.dismiss();
            applyChanges();
            if (stepper.isAtLastPosition()) {
                finish();
                getFragmentManager().popBackStackImmediate();
            } else {
                stepper.setNextTab(getSkipPositions());
            }
        } else {
            View view;
            // This is used for floating action button, so the it animates when the snackbar is shown.
            if (curFragment instanceof FragmentWithFab)
                view = ((FragmentWithFab) curFragment).getCoordinatorLayout();
            else
                view = viewPager;
            snackbar = CustomSnackbar.create(getContext(), view, error, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    protected List<Integer> getSkipPositions() {
        return new ArrayList<>();
    }

    protected StepperPageFragment getCurrentFragment() {
        int currentPosition = viewPager.getCurrentItem();
        ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
        return (StepperPageFragment) adapter.getItem(currentPosition);
    }

    protected void notifyObjectChanged(Object changedObject) {
        ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            StepperPageFragment fragment = (StepperPageFragment) adapter.getItem(i);
            fragment.onObjectChanged(changedObject);
        }
    }
}

