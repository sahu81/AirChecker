package com.example.dessusdi.myfirstapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.dessusdi.myfirstapp.model.WaqiObject;
import com.example.dessusdi.myfirstapp.recycler_view.AqcinListAdapter;
import com.example.dessusdi.myfirstapp.tools.AqcinDatabaseService;
import com.example.dessusdi.myfirstapp.tools.AqcinRequestService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private RecyclerView recyclerView;
    private AqcinDatabaseService dbService = new AqcinDatabaseService(getContext());
    private AqcinRequestService async = new AqcinRequestService(getContext());
    private List<WaqiObject> cities = new ArrayList<WaqiObject>();
    private AqcinListAdapter adapter = new AqcinListAdapter(cities);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        this.reloadCitiesFromDB();
        this.refreshRecyclerList();
    }

    private void reloadCitiesFromDB() {
        // Load cities from db
        //this.citiesIds = dbService.fetchSavedCities();

        this.cities.addAll(dbService.fetchSavedCities());

        Log.d("DATABASE", cities.toString());

        for (WaqiObject cityObject : this.cities) {
            Log.d("DATABASE", "ID --> " + cityObject.getIdentifier());
            Log.d("DATABASE", "Name --> " + cityObject.getName());
            cityObject.setAqcinListAdapter(this.adapter);
            cityObject.setRequestService(this.async);
            cityObject.fetchData(this.cities);
        }
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
            this.presentSearchDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void presentSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new city");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String identifier = input.getText().toString();
                WaqiObject cityObject = new WaqiObject(identifier, async, adapter);
                cityObject.save();
                cityObject.fetchData(cities);
                cities.add(cityObject);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public Context getContext() {
        return MainActivity.this;
    }
}
