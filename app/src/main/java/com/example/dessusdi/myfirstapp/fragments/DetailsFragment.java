package com.example.dessusdi.myfirstapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.cityTitle          = (TextView) view.findViewById(R.id.cityTitle);
        this.cityPicture        = (ImageView) view.findViewById(R.id.cityPicture);
        this.cityDescription    = (TextView) view.findViewById(R.id.cityDescription);
        this.cityEvolution      = (Button) view.findViewById(R.id.cityEvolution);
        this.favoriteButton     = (Button) view.findViewById(R.id.cityFavorite);

        this.cityEvolution.setVisibility(View.GONE);
        this.cityEvolution.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showEvolution();
            }
        });

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(settings.getInt("fav_city", 1000) == this.city.getIdentifier()) {
            this.favoriteButton.setVisibility(View.GONE);
        } else {
            this.favoriteButton.setVisibility(View.VISIBLE);
        }

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

    public void setCity(WaqiObject city) {
        this.city = city;
    }

    public void fetchCityInformation() {

        this.async = new WikipediaService(this.mActivity);

        async.fetchCityInformation(this.city.getSearchQuery(),
                new WikipediaService.cityInformationCallback() {
                    @Override
                    public void onSuccess(PageObject pageObject) {
                        cityTitle.setText(pageObject.getTitle());
                        cityDescription.setText(pageObject.getExtract());
                        cityEvolution.setVisibility(View.VISIBLE);
                    }
                }
        );

        async.fetchCityImage(this.city.getSearchQuery(),
                new WikipediaService.cityImageCallback() {
                    @Override
                    public void onSuccess(ImageObject imageObject) {

                        if(!imageObject.getOriginal().getSource().isEmpty()) {
                            Picasso.with(mActivity)
                                    .load(imageObject.getOriginal().getSource())
                                    .fit()
                                    .centerCrop()
                                    .into(cityPicture);
                        } else {
                            cityPicture.setImageResource(R.drawable.no_image);
                        }

                    }
                }
        );

    }

    private void showEvolution() {
        EvolutionFragment newFragment = new EvolutionFragment();
        newFragment.setCity(this.city);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, newFragment, TAG_FRAGMENT);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
