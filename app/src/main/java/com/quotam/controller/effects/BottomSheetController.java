package com.quotam.controller.effects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.quotam.fragments.steppers.CreateImageEffects;
import com.quotam.fragments.steppers.CreateImageStepperFragment;

public abstract class BottomSheetController implements CreateImageStepperFragment.BottomSheetListener{

    public BottomSheetController() {

    }

    public void show(CreateImageEffects fragment, CharSequence title) {
        onShow();
        fragment.showBottomSheet(getContentView(fragment.getLayoutInflater(),fragment.getContext()), title, this,null);
    }

    protected abstract View getContentView(LayoutInflater layoutInflater, Context context);

    protected void onShow() {}

}
