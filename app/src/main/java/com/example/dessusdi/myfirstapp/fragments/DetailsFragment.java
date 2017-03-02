package com.example.dessusdi.myfirstapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dessusdi.myfirstapp.R;

/**
 * Created by Dimitri on 02/03/2017.
 */

public class DetailsFragment extends Fragment {

    private final static String TAG_FRAGMENT    = "FRAG_DETAILS";
    TextView title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = (TextView) view.findViewById(R.id.title);

        title.setText("Je suis un fragment");
    }

    public void setTextLol(String test) {
        this.title.setText(test);
    }
}
