package com.example.dessusdi.myfirstapp;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dessusdi.myfirstapp.model.WaqiObject;
import com.example.dessusdi.myfirstapp.recycler_view.AqcinListAdapter;
import com.example.dessusdi.myfirstapp.tools.AqcinDatabaseHelper;
import com.example.dessusdi.myfirstapp.tools.AqcinDatabaseService;
import com.example.dessusdi.myfirstapp.tools.AqcinRequestService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private RecyclerView recyclerView;
    private List<String> citiesIds;
    private List<WaqiObject> cities = new ArrayList<>();
    private AqcinListAdapter adapter = new AqcinListAdapter(cities);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        AqcinDatabaseService dbService  = new AqcinDatabaseService(getContext());
        AqcinRequestService async       = new AqcinRequestService(getContext());

        /*
        dbService.addCity("@3071");
        dbService.addCity("@3069");
        dbService.addCity("@3067");
        */

        // Load cities from db
        this.citiesIds = dbService.fetchSavedCities();

        for (String id : citiesIds) {
            WaqiObject cityObject = new WaqiObject(id, async, adapter);
            cityObject.fetchData();
            cities.add(cityObject);
        }

        /*
        dbService.removeCity("@3067");
        */

        Log.d("DATABASE", dbService.fetchSavedCities().toString());

        this.refreshRecyclerList();
    }

    public void refreshRecyclerList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Context getContext() {
        return MainActivity.this;
    }
}
