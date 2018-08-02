package com.quotam.fragments.steppers;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.quotam.R;
import com.quotam.adapters.ChipsAdapter;
import com.quotam.custom.CustomAnimations;
import com.quotam.custom.CustomEditText;
import com.quotam.model.CreateImage;

public class CreateImageOptions extends Fragment implements StepperPageFragment {

    public static final int MIN_CHARS = 3;
    private static final int MAX_TAGS = 8;
    private SwitchCompat switchVisibility;
    private TextView switchText;
    private boolean isPublic = true;
    private CustomEditText addTagEditText;
    private TextView addTagTextError;
    private RecyclerView chipGrid;
    private ImageView fullImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stepper_fragment_image_options, container, false);
        fullImageView = rootView.findViewById(R.id.stepper_options_image);
        addTagEditText = rootView.findViewById(R.id.stepper_options_tags_edittext);
        addTagTextError = rootView.findViewById(R.id.stepper_options_tags_error);
        switchText = rootView.findViewById(R.id.stepper_options_switch_text);
        ImageButton addButton = rootView.findViewById(R.id.stepper_options_tags_button);
        chipGrid = rootView.findViewById(R.id.stepper_options_tags_grid);
        switchVisibility = rootView.findViewById(R.id.stepper_options_switch);
        switchVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                switchChanged(checked);
            }
        });
        addTagEditText.setOnActionDoneListener(new CustomEditText.OnActionDoneListener() {
            @Override
            public void onActionDone() {
                clearError();
                addChip();
                addTagEditText.closeAll();
                addTagEditText.clearText();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTagEditText.callOnActionDone();
            }
        });
        setupChipGrid();
        return rootView;
    }

    private void setupChipGrid() {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        manager.setAutoMeasureEnabled(true);

        chipGrid.setLayoutManager(manager);
        ChipsAdapter adapter = new ChipsAdapter();
//        adapter.setCloseListener(new ChipsAdapter.OnCloseListener() {
//            @Override
//            public void onClose() {
//
//            }
//        });
        chipGrid.setAdapter(adapter);
    }

    private void setError(String error) {
        CustomAnimations.animateTextChanged(addTagTextError, error);
    }

    private void clearError() {
        CustomAnimations.animateTextChanged(addTagTextError, "");
    }

    private void addChip() {
        String text = addTagEditText.getText().toString();
        ChipsAdapter adapter = (ChipsAdapter) chipGrid.getAdapter();
        if (adapter.getItemCount() >= MAX_TAGS) {
            setError(getResources().getString(R.string.tag_max_items_error));
            return;
        }
        if (text.length() < MIN_CHARS) {
            setError(getResources().getString(R.string.tag_min_chars));
            return;
        }
        if (text.length() > getResources().getInteger(R.integer.tag_text_max_length)) {
            setError(getResources().getString(R.string.tag_max_chars));
            return;
        }

        boolean added = adapter.addChip(new ChipsAdapter.Chip(text));
        if (!added)
            setError(getResources().getString(R.string.tag_exists_error));
    }

    private void switchChanged(boolean checked) {
        isPublic = checked;
        String text;
        if (checked)
            text = getResources().getString(R.string.public_);
        else
            text = getResources().getString(R.string.private_);

        CustomAnimations.animateTextChanged(switchText, text);
    }

    @Override
    public void onObjectChanged(Object changedObject) {
        if (changedObject instanceof CreateImage) {
            CreateImage createImage = (CreateImage) changedObject;
            Bitmap fullBitmap = createImage.getFullImage();
            if (fullBitmap != null)
                fullImageView.setImageBitmap(fullBitmap);
        }
    }

    @Override
    public String isDone() {
        return null;
    }

    @Override
    public void applyChanges(Object objectToChange) {

    }
}
