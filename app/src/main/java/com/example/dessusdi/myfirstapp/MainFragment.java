package com.example.dessusdi.myfirstapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;
import com.example.dessusdi.myfirstapp.models.search.SearchGlobalObject;
import com.example.dessusdi.myfirstapp.models.search.SearchLocationObject;
import com.example.dessusdi.myfirstapp.recycler_view.AqcinListAdapter;
import com.example.dessusdi.myfirstapp.tools.AqcinRequestService;
import com.example.dessusdi.myfirstapp.tools.LanguageUpdater;
import com.example.dessusdi.myfirstapp.tools.ThemeUpdater;

import java.util.ArrayList;
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
    private LanguageUpdater langUpdater;
    private ThemeUpdater themeUpdater;
    private int radioIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.themeUpdater = new ThemeUpdater(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()));

        // Changing theme at startup
        this.themeUpdater.loadSavedTheme();

        this.recyclerView           = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.emptyRecyclerTextView  = (TextView) view.findViewById(R.id.emptyRecycler);
        this.swipeRefresh           = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        this.async      = new AqcinRequestService(getActivity());
        this.cities     = ((MainActivity) getActivity()).getCities();
        this.adapter    = ((MainActivity) getActivity()).getAdapter();

        this.langUpdater = new LanguageUpdater(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()));
        this.langUpdater.loadSavedLanguage();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search) {
            this.presentSearchDialog();
            return true;
        } else if (id == R.id.menu_settings) {
            this.showUserSettings();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showUserSettings() {
        Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void presentSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_city);

        // Set up the input
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.validate_action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = input.getText().toString();

                async.fetchCityID(inputText,
                        new AqcinRequestService.SearchQueryCallback() {
                            @Override
                            public void onSuccess(SearchGlobalObject searchGlobalObject) {
                                if(searchGlobalObject.getData().size() > 0)
                                    presentRadioList(searchGlobalObject.getData());
                                else
                                    presentCityNotFoundDialog();
                            }
                        });

            }
        });
        builder.setNegativeButton(R.string.cancel_action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void presentCityNotFoundDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(R.string.city_not_found);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.validate_action,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertCityNotFound = builder1.create();
        alertCityNotFound.show();
    }

    private void presentRadioList(final ArrayList<SearchLocationObject> locationArray) {

        List<String> citiesName = new ArrayList<String>();
        for (SearchLocationObject location : locationArray) {
            citiesName.add(location.getStation().getName());
        }

        if(citiesName.size() <= 0)
            return;

        final String[] items = new String[ citiesName.size() ];
        citiesName.toArray( items );

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//ERROR ShowDialog cannot be resolved to a type
        builder.setTitle(R.string.choose_location);
        AlertDialog.Builder builder1 = builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        radioIndex = item;
                    }
                });

        builder.setPositiveButton(R.string.add_action, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                WaqiObject cityObject = new WaqiObject(locationArray.get(radioIndex).getUid(), async, adapter);
                cityObject.save();
                cityObject.fetchData();
                cities.add(cityObject);
                checkIfRecyclerEmpty();
            }
        });

        builder.setNegativeButton(R.string.cancel_action, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
