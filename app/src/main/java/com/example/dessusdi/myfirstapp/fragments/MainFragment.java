package com.example.dessusdi.myfirstapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dessusdi.myfirstapp.MainActivity;
import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;
import com.example.dessusdi.myfirstapp.recycler_view.AqcinListAdapter;
import com.example.dessusdi.myfirstapp.tools.AqcinRequestService;

import java.util.List;

/**
 * Created by Dimitri on 01/03/2017.
 */

public class MainFragment extends Fragment {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private TextView emptyRecyclerTextView;
    private AqcinRequestService async;
    private List<WaqiObject> cities;
    private AqcinListAdapter adapter;
    private int radioIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.recyclerView           = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.emptyRecyclerTextView  = (TextView) view.findViewById(R.id.emptyRecycler);
        this.swipeRefresh           = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        this.async      = new AqcinRequestService(getActivity());
        this.cities     = ((MainActivity) getActivity()).getCities();
        this.adapter    = ((MainActivity) getActivity()).getAdapter();

        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadCitiesFromDB();
            }
        });

        this.setupRecyclerView();
        this.reloadCitiesFromDB();
        this.refreshRecyclerList();
        //this.setupBackgroundService();
    }

    private void setupRecyclerView() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.delete_confirmation);

                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Remove cell at specific position
                            adapter.notifyItemRemoved(position);
                            cities.get(position).delete();
                            cities.remove(position);
                            checkIfRecyclerEmpty();
                            return;
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Replace cell at same position
                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                            checkIfRecyclerEmpty();
                            return;
                        }
                    }).show();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView); //set swipe to recyclerview
    }

    private void checkIfRecyclerEmpty() {
        if (this.cities.size() > 0) {
            emptyRecyclerTextView.setVisibility(View.INVISIBLE);
        } else {
            emptyRecyclerTextView.setVisibility(View.VISIBLE);
        }
    }
    private void reloadCitiesFromDB() {
        // Load cities from db
        this.cities.clear();
        this.adapter.notifyDataSetChanged();
        this.cities.addAll(WaqiObject.listAll(WaqiObject.class));

        for (WaqiObject cityObject : this.cities) {
            cityObject.setAqcinListAdapter(this.adapter);
            cityObject.setRequestService(this.async);
            cityObject.fetchData();
        }

        this.checkIfRecyclerEmpty();
        this.swipeRefresh.setRefreshing(false);
    }

    public void refreshRecyclerList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(this.adapter);
    }
}
