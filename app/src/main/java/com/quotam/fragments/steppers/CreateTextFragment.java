package com.quotam.fragments.steppers;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassificationManager;
import android.widget.EditText;

import com.quotam.R;
import com.quotam.adapters.Items;
import com.quotam.adapters.TextQuoteAdapter;
import com.quotam.custom.CustomSearchView;
import com.quotam.model.CreateImage;
import com.quotam.model.TextQuote;

public class CreateTextFragment extends Fragment implements StepperPageFragment {


    private RecyclerView itemsGrid;
    private EditText editTextPrimary;
    private EditText editTextSecondary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stepper_fragment_create_text, container, false);
        CustomSearchView searchView = rootView.findViewById(R.id.search_view);
        editTextPrimary = rootView.findViewById(R.id.stepper_text_primary);
        editTextSecondary = rootView.findViewById(R.id.stepper_text_secondary);

        itemsGrid = rootView.findViewById(R.id.stepper_text_items);
        setupItemsGrid();
        adjustGridItems(itemsGrid);
        return rootView;
    }

    private void setupItemsGrid() {
        Items items = new Items();
        TextQuoteAdapter adapter = new TextQuoteAdapter(items.getTextQuotes());
        adapter.setOnItemClickListener(new TextQuoteAdapter.CompactOnItemClickListener() {
            @Override
            public void onItemClick(TextQuote textQuote) {
                addSelectedQuote(textQuote);
            }
        });
        itemsGrid.setAdapter(adapter);
    }

    private void addSelectedQuote(TextQuote textQuote) {
        editTextPrimary.setText(textQuote.getText());
        editTextSecondary.setText(textQuote.getAuthor());
    }

    @NonNull
    private String getPrimaryText() {
        return editTextPrimary.getText().toString().trim();
    }

    private void adjustGridItems(RecyclerView grid) {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1);
        itemsGrid.setLayoutManager(manager);
        //new SystemUI().adjustGridColumnNum(getActivity(), grid, false);
    }

    @Override
    public String isDone() {
        String primaryText = getPrimaryText();
        if (!primaryText.isEmpty())
            return null;
        else
            return getResources().getString(R.string.create_choose_text_error);
    }


    @Override
    public void applyChanges(Object objectToChange) {
        if (objectToChange instanceof CreateImage) {
            CreateImage createImage = (CreateImage) objectToChange;
            String primaryText = getPrimaryText();
            if (!createImage.getPrimaryText().equals(primaryText))
                createImage.setPrimaryText(primaryText);
        }
    }


}
