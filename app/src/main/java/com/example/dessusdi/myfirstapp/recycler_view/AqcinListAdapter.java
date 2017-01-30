package com.example.dessusdi.myfirstapp.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.model.IaqiObject;

import java.util.List;

/**
 * Created by dessusdi on 30/01/2017.
 */
public class AqcinListAdapter extends RecyclerView.Adapter<AqcinCellView> {

    List<IaqiObject> list;

    public AqcinListAdapter(List<IaqiObject> list) {
        this.list = list;
    }

    @Override
    public AqcinCellView onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new AqcinCellView(view);
    }

    @Override
    public void onBindViewHolder(AqcinCellView aqcinCellView, int index) {
        IaqiObject myObject = list.get(index);
        aqcinCellView.bind(myObject);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
