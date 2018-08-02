package com.quotam.fragments.sheets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quotam.R;

public class BottomSheetReport extends BottomSheetDialogFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_sheet_list, container,
                false);
        TextView title = rootView.findViewById(R.id.title);
        title.setText(R.string.action_report);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        MenuItemAdapter adapter = new MenuItemAdapter(R.menu.report_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    private class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

        private final Menu menu;

        public MenuItemAdapter(int menuID) {
            PopupMenu p = new PopupMenu(getActivity(), null);
            menu = p.getMenu();
            getActivity().getMenuInflater().inflate(menuID, menu);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View itemView = inflater.inflate(R.layout.bottom_sheet_list_item,
                    parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.title.setText(menu.getItem(position).getTitle());
            Log.e("asd", "a " + menu.getItem(position).getTitle());

            holder.itemView.setOnClickListener(new ReportOnClickListener(
                    position));
        }

        @Override
        public int getItemCount() {
            return menu.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView title;
            public View itemView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                title = itemView.findViewById(R.id.title);
            }
        }

        class ReportOnClickListener implements View.OnClickListener {
            public ReportOnClickListener(int position) {

            }

            @Override
            public void onClick(View v) {

                dismiss();

            }
        }
    }
}

