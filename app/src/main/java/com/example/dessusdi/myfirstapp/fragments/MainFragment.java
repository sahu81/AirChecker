package com.example.dessusdi.myfirstapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.example.dessusdi.myfirstapp.models.air_quality_position.PositionGlobalObject;
import com.example.dessusdi.myfirstapp.recycler_view.AqcinListAdapter;
import com.example.dessusdi.myfirstapp.services.AqcinRequestService;

import java.util.List;

/**
 * Created by Dimitri on 01/03/2017.
 */

public class MainFragment extends Fragment {

    private TextView cityNameTextView;
    private TextView air_qualityPositionTextView;
    private TextView gpsPositionTextView;

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private TextView emptyRecyclerTextView;
    private AqcinRequestService async;
    private List<WaqiObject> cities;
    private AqcinListAdapter adapter;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.cityNameTextView               = (TextView) view.findViewById(R.id.city_namePositionTextView);
        this.air_qualityPositionTextView    = (TextView) view.findViewById(R.id.air_qualityPositionTextView);
        this.gpsPositionTextView            = (TextView) view.findViewById(R.id.gpsPositionTextView);

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

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(this.adapter);
    }

    /**
     * Private method used to setting up recycler view object.
     * Declaring some events like 'move' & 'swipe'.
     */
    private void setupRecyclerView() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            /**
             * Disable cell moving
             * @param recyclerView
             * @param viewHolder
             * @param target
             * @return
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            /**
             * Swipe event allowing user to delete cells.
             * @param viewHolder
             * @param direction
             */
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                // Getting cell position
                final int position = viewHolder.getAdapterPosition();

                // Enabling right & left swipe on cell
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {

                    // Show dialog when event triggered
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
        itemTouchHelper.attachToRecyclerView(recyclerView); //set swipe to RecyclerView
    }

    /**
     * Private method used to check if recycler is empty.
     * Show/hide empty label on the center of the recycler view.
     */
    private void checkIfRecyclerEmpty() {
        if (this.cities.size() > 0) {
            emptyRecyclerTextView.setVisibility(View.INVISIBLE);
        } else {
            emptyRecyclerTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Private method used to reload cities from the local database.
     * Cities object was loaded on a local array.
     * All cities data was fetched after array populated.
     */
    private void reloadCitiesFromDB() {
        // Clear local array.
        this.cities.clear();
        this.adapter.notifyDataSetChanged();
        // Populate local array.
        this.cities.addAll(WaqiObject.listAll(WaqiObject.class));

        // Trigger fetch url on all city object.
        for (WaqiObject cityObject : this.cities) {
            cityObject.setAqcinListAdapter(this.adapter);
            cityObject.setRequestService(this.async);
            cityObject.fetchData();
        }

        this.retrieveUserPosition();
        this.checkIfRecyclerEmpty();
        this.swipeRefresh.setRefreshing(false);
    }

    /**
     * Retrieve user's position in order to retrieve cities around this position.
     */
    private void retrieveUserPosition() {
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        if (location != null) {
            retrieveCityAroundMe(location.getLatitude(), location.getLongitude());
        }
    }

    /**
     * Retrieve city near user's location
     * @param userLatitude latitude of the user
     * @param userLongitude longitude of the user
     */
    private void retrieveCityAroundMe(double userLatitude, double userLongitude) {

        async.fetchCitiesAroundPosition(userLatitude, userLongitude,
                new AqcinRequestService.PositionQueryCallback() {
                    @Override
                    public void onSuccess(PositionGlobalObject positionGlobalObject) {
                        cityNameTextView.setText(positionGlobalObject.getName());
                        gpsPositionTextView.setText(positionGlobalObject.getGPSCoordinate());
                        air_qualityPositionTextView.setText(positionGlobalObject.getAqi());
                        air_qualityPositionTextView.setBackgroundColor(Color.parseColor(positionGlobalObject.getColorCode()));
                    }
                }
        );
    }

    // Location listener (executed when user's location changed)
    private final LocationListener locationListener = new LocationListener() {
        /**
         * When the location change (latitude & longitude)
         * @param location
         */
        public void onLocationChanged(Location location) {
            retrieveCityAroundMe(location.getLatitude(), location.getLongitude());
        }

        /**
         * @param arg0
         */
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub

        }

        /**
         * @param arg0
         */
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub

        }

        /**
         * @param arg0
         * @param arg1
         * @param arg2
         */
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }
    };
}
