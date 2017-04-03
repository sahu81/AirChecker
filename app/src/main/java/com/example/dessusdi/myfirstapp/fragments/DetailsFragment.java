package com.example.dessusdi.myfirstapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;
import com.example.dessusdi.myfirstapp.models.wikipedia.ImageObject;
import com.example.dessusdi.myfirstapp.models.wikipedia.PageObject;
import com.example.dessusdi.myfirstapp.services.WikipediaService;
import com.squareup.picasso.Picasso;

/**
 * Created by Dimitri on 02/03/2017.
 */

public class DetailsFragment extends Fragment {

    private WaqiObject city;
    private TextView cityTitle;
    private ImageView cityPicture;
    private TextView cityDescription;
    private Button cityEvolution;
    private Button favoriteButton;

    private WikipediaService async;
    private Activity mActivity;
    private final static String TAG_FRAGMENT = "FRAG_EVOLUTION";

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.cityTitle          = (TextView) view.findViewById(R.id.cityTitle);
        this.cityPicture        = (ImageView) view.findViewById(R.id.cityPicture);
        this.cityDescription    = (TextView) view.findViewById(R.id.cityDescription);
        this.cityEvolution      = (Button) view.findViewById(R.id.cityEvolution);
        this.favoriteButton     = (Button) view.findViewById(R.id.cityFavorite);

        this.cityEvolution.setVisibility(View.GONE);
        this.favoriteButton.setVisibility(View.GONE);
        this.cityEvolution.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showEvolution();
            }
        });

        // Read if city is marked as favorite
        // In order to show/hide button
        this.readFavoriteCity();

        this.favoriteButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("fav_city", city.getIdentifier());
                editor.commit();
                favoriteButton.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.favorite_added, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mActivity = this.getActivity();

        if(this.city != null) {
            this.fetchCityInformation();
        }
    }

    /**
     * @param city
     */
    public void setCity(WaqiObject city) {
        this.city = city;
    }

    /**
     * This method is used to fetch city information from Wikipedia
     * Two async request was sent :
     * 1. Get Title & Description text (from Wikipedia)
     * 2. Fetch main image.
     */
    public void fetchCityInformation() {

        // Read if city is marked as favorite
        // In order to show/hide button
        this.readFavoriteCity();
        this.async = new WikipediaService(this.mActivity);

        // Fetch Title & Description text
        async.fetchCityInformation(this.city.getSearchQuery(),
                new WikipediaService.cityInformationCallback() {
                    @Override
                    public void onSuccess(PageObject pageObject) {
                        if(pageObject == null)
                            return;

                        cityTitle.setText(pageObject.getTitle());
                        cityDescription.setText(pageObject.getExtract());
                        cityEvolution.setVisibility(View.VISIBLE);
                    }
                }
        );

        // Fetch main URL image
        async.fetchCityImage(this.city.getSearchQuery(),
                new WikipediaService.cityImageCallback() {
                    @Override
                    public void onSuccess(ImageObject imageObject) {
                        if(imageObject == null)
                            return;

                        // If image URL not empty, perform a new request with Picasso
                        // in order to retrieve the image
                        if(!imageObject.getOriginal().getSource().isEmpty()) {
                            Picasso.with(mActivity)
                                    .load(imageObject.getOriginal().getSource())
                                    .fit()
                                    .centerCrop()
                                    .into(cityPicture);
                        } else {
                            // Setting default image if image URL not exist
                            cityPicture.setImageResource(R.drawable.no_image);
                        }

                    }
                }
        );
    }

    /**
     * Private method allowing to launch evolution fragment.
     * This method is triggered when user click on the 'Evolution' button.
     */
    private void showEvolution() {
        EvolutionFragment newFragment = new EvolutionFragment();
        newFragment.setCity(this.city); // Setting city object to 'EvolutionFragment' class
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, newFragment, TAG_FRAGMENT);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Private method used to compare loaded city identifier with the saved one.
     * If city identifier is the same, hide favorite button else show it.
     */
    private void readFavoriteCity() {
        // Detecting if favorite button & city object not null
        if(this.favoriteButton == null || this.city == null)
            return;

        // Getting city identifier from preferences
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(settings.getInt("fav_city", 99999) == this.city.getIdentifier()) {
            // Set button invisible
            this.favoriteButton.setVisibility(View.GONE);
        } else {
            // Set button visible
            this.favoriteButton.setVisibility(View.VISIBLE);
        }
    }
}
