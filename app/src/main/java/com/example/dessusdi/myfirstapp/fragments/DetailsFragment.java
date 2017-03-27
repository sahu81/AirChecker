package com.example.dessusdi.myfirstapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;
import com.example.dessusdi.myfirstapp.models.wikipedia.ImageObject;
import com.example.dessusdi.myfirstapp.models.wikipedia.PageObject;
import com.example.dessusdi.myfirstapp.tools.WikipediaService;
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

        this.cityEvolution.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showEvolution();
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
