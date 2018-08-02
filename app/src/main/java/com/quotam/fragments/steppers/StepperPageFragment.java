package com.quotam.fragments.steppers;


public interface StepperPageFragment {
    String isDone();
    void applyChanges(Object objectToChange);
    default void onObjectChanged(Object changedObject){};
}
