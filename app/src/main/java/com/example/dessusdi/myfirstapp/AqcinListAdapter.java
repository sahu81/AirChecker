package com.example.dessusdi.myfirstapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dessusdi.myfirstapp.model.AqcinObject;

import java.util.List;

/**
 * Created by dessusdi on 30/01/2017.
 */
public class AqcinListAdapter extends RecyclerView.Adapter<AqcinCellView> {

    List<AqcinObject> list;

    public AqcinListAdapter(List<AqcinObject> list) {
        this.list = list;
    }

    @Override
    public AqcinCellView onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new AqcinCellView(view);
    }

    @Override
    public void onBindViewHolder(AqcinCellView aqcinCellView, int index) {
        AqcinObject myObject = list.get(index);
        aqcinCellView.bind(myObject);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}