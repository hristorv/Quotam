package com.quotam.fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.quotam.R;
import com.quotam.listeners.SystemUI;
import com.quotam.adapters.CommentsAdapter;
import com.quotam.adapters.Items;

public class CommentsFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comments,
                container, false);
        RecyclerView grid = rootView.findViewById(R.id.grid);
        fillGrid(grid);
        // Fix Keyboard for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final AppCompatEditText addComment = rootView.findViewById(R.id.add_comment_edit_text);
        setupAddComment(addComment);
        final ImageButton sendButton = rootView.findViewById(R.id.comments_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sendButton.getWindowToken(), 0);
                addComment.clearFocus();
                addComment.getText().clear();

            }
        });

        return rootView;
    }

    private void setupAddComment(AppCompatEditText addComment) {
        //Typeface font = Typeface.createFromAsset(getContext().getAssets(), "coming_soon.ttf");
        //Typeface boldFont = Typeface.create(font, Typeface.BOLD);
        //addComment.setTypeface(boldFont);
    }

    private void fillGrid(RecyclerView grid) {
        final Items items = new Items();
        grid.setAdapter(new CommentsAdapter(items.getRandomComments()));
        adjustGrid(grid, true);
    }

    private void adjustGrid(RecyclerView grid, boolean isLargeGridItems) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        grid.setLayoutManager(manager);
        new SystemUI().adjustGridColumnNum(getActivity(), grid, isLargeGridItems);
    }

}

