package com.example.dessusdi.myfirstapp.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.model.WaqiObject;

/**
 * Created by dessusdi on 30/01/2017.
 */
public class AqcinCellView extends RecyclerView.ViewHolder{

    private TextView textViewView;

    public AqcinCellView(View itemView) {
        super(itemView);

        textViewView = (TextView) itemView.findViewById(R.id.city_name);
    }

    public void bind(WaqiObject myObject){
        textViewView.setText("TEST");
    }

}
