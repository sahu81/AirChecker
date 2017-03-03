package com.example.dessusdi.myfirstapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;
import com.example.dessusdi.myfirstapp.models.wikipedia.PageObject;
import com.example.dessusdi.myfirstapp.tools.WikipediaService;

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
        Log.d("DATA", "----> " + this.city.getName());

        this.async = new WikipediaService(this.mActivity);

        async.fetchCityInformation("Lyon",
                new WikipediaService.cityInformationCallback() {
                    @Override
                    public void onSuccess(PageObject pageObject) {
                        cityTitle.setText(pageObject.getTitle());
                        cityDescription.setText(pageObject.getExtract());
                        Log.d("DATA", "Result --> " + pageObject.getTitle());
                    }
                }
        );

    }
}
