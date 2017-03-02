package com.example.dessusdi.myfirstapp;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.dessusdi.myfirstapp.fragments.SettingsFragment;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;
import com.example.dessusdi.myfirstapp.models.search.SearchGlobalObject;
import com.example.dessusdi.myfirstapp.models.search.SearchLocationObject;
import com.example.dessusdi.myfirstapp.recycler_view.AqcinListAdapter;
import com.example.dessusdi.myfirstapp.tools.AqcinRequestService;
import com.example.dessusdi.myfirstapp.tools.LanguageUpdater;
import com.example.dessusdi.myfirstapp.tools.ThemeUpdater;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG_FRAGMENT    = "FRAG_SETTINGS";
    private AqcinRequestService async           = new AqcinRequestService(MainActivity.this);
    private List<WaqiObject> cities             = new ArrayList<>();
    private AqcinListAdapter adapter            = new AqcinListAdapter(cities, MainActivity.this);
    private LanguageUpdater langUpdater;
    private ThemeUpdater themeUpdater;
    private int radioIndex;

    public List<WaqiObject> getCities() {
        return cities;
    }
    public AqcinListAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.themeUpdater = new ThemeUpdater(this, PreferenceManager.getDefaultSharedPreferences(this));
        this.themeUpdater.loadSavedTheme();

        this.langUpdater = new LanguageUpdater(this, PreferenceManager.getDefaultSharedPreferences(this));
        this.langUpdater.loadSavedLanguage();

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        if (getFragmentManager().getBackStackEntryCount() <= 0) {
            SettingsFragment newFragment = new SettingsFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container, newFragment, TAG_FRAGMENT);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void presentSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_city);

        // Set up the input
        final EditText input = new EditText(this);
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
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this); //ERROR ShowDialog cannot be resolved to a type
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

