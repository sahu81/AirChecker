package com.example.dessusdi.myfirstapp;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.dessusdi.myfirstapp.fragments.MainFragment;
import com.example.dessusdi.myfirstapp.fragments.SettingsFragment;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;
import com.example.dessusdi.myfirstapp.models.search.SearchGlobalObject;
import com.example.dessusdi.myfirstapp.models.search.SearchLocationObject;
import com.example.dessusdi.myfirstapp.recycler_view.AqcinListAdapter;
import com.example.dessusdi.myfirstapp.services.AqcinRequestService;
import com.example.dessusdi.myfirstapp.services.BackgroundRefresher;
import com.example.dessusdi.myfirstapp.services.AirCheckerProvider;
import com.example.dessusdi.myfirstapp.tools.LanguageUpdater;
import com.example.dessusdi.myfirstapp.tools.ThemeUpdater;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG_FRAGMENT    = "FRAG_SETTINGS";
    private final AqcinRequestService async     = new AqcinRequestService(MainActivity.this);
    private final List<WaqiObject> cities       = new ArrayList<>();
    private final AqcinListAdapter adapter      = new AqcinListAdapter(cities, MainActivity.this);
    private LanguageUpdater langUpdater;
    private ThemeUpdater themeUpdater;
    private int radioIndex;

    public List<WaqiObject> getCities() {
        return cities;
    }
    public AqcinListAdapter getAdapter() {
        return adapter;
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.themeUpdater = new ThemeUpdater(this, PreferenceManager.getDefaultSharedPreferences(this));
        this.themeUpdater.loadSavedTheme();

        this.langUpdater = new LanguageUpdater(this, PreferenceManager.getDefaultSharedPreferences(this));
        this.langUpdater.loadSavedLanguage();

        this.setupBackgroundService();
        // this.testContentProvider();

        setContentView(R.layout.activity_main);
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * @param item
     * @return
     */
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

    /**
     * Replace current fragment by settings.
     */
    private void showUserSettings() {
        if (getFragmentManager().getBackStackEntryCount() <= 0) {
            SettingsFragment newFragment = new SettingsFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container, newFragment, TAG_FRAGMENT);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * Override back button pressed.
     * Return to previous fragment.
     */
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Present search dialog to find city
     */
    private void presentSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_city);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.validate_action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String inputText = input.getText().toString();

                async.fetchCityID(inputText,
                        new AqcinRequestService.SearchQueryCallback() {
                            @Override
                            public void onSuccess(SearchGlobalObject searchGlobalObject) {
                                if(searchGlobalObject != null) {
                                    if (searchGlobalObject.getData().size() > 0)
                                        presentRadioList(searchGlobalObject.getData(), inputText);
                                    else
                                        presentCityNotFoundDialog();
                                } else {
                                    presentCityNotFoundDialog();
                                }
                            }
                        }
                );

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

    /**
     * Present city not found dialog.
     */
    private void presentCityNotFoundDialog() {
        AlertDialog.Builder noCityDialog = new AlertDialog.Builder(MainActivity.this);
        noCityDialog.setMessage(R.string.city_not_found);
        noCityDialog.setCancelable(true);

        noCityDialog.setPositiveButton(
                R.string.validate_action,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertCityNotFound = noCityDialog.create();
        alertCityNotFound.show();
    }

    /**
     * Present cities searched into dialog.
     * @param locationArray cities found array
     * @param searchQuery search query
     */
    private void presentRadioList(final ArrayList<SearchLocationObject> locationArray, final String searchQuery) {

        // Cities string name array
        List<String> citiesName = new ArrayList<>();
        for (SearchLocationObject location : locationArray) {
            citiesName.add(location.getStation().getName());
        }

        if(citiesName.size() <= 0)
            return;

        final String[] items = new String[ citiesName.size() ];
        citiesName.toArray( items );

        AlertDialog.Builder builder = new AlertDialog.Builder(this); //ERROR ShowDialog cannot be resolved to a type
        builder.setTitle(R.string.choose_location);
        //noinspection UnusedAssignment
        AlertDialog.Builder radioBuilder = builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        radioIndex = item;
                    }
                });

        builder.setPositiveButton(R.string.add_action, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                WaqiObject cityObject = new WaqiObject(locationArray.get(radioIndex).getUid(), async, adapter);
                cityObject.setSearchQuery(searchQuery);
                cityObject.save();
                cityObject.fetchData();
                cities.add(cityObject);

                // Perform checking on main cities list.
                MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment);
                mainFragment.checkIfRecyclerEmpty();
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

    /**
     * Setup background service task.
     * Delayed to 10000 ms (10 sec) to avoid multiple requests.
     */
    private void setupBackgroundService() {
        final Intent myIntent = new Intent(this, BackgroundRefresher.class);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startService(myIntent);
            }
        }, 10000);

    }

    /**
     * Testing content provider.
     * 1. Adding 'Amsterdam' to database.
     * 2. Removing 'Amsterdam' to database.
     */
    private void testContentProvider() {
        // Inserting new city (Amsterdam)
        ContentValues aqi = new ContentValues();
        aqi.put("identifier", "5771");
        getContentResolver().insert(AirCheckerProvider.CONTENT_URI, aqi);

        // Deleting city (Amsterdam)
        getContentResolver().delete(AirCheckerProvider.CONTENT_URI, "5771", null);

    }

}

