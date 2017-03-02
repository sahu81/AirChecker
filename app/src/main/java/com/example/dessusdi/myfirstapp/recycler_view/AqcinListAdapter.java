package com.example.dessusdi.myfirstapp.recycler_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;

import java.util.List;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class AqcinListAdapter extends RecyclerView.Adapter<AqcinCellView> {

    List<WaqiObject> list;
    Context context;

    public AqcinListAdapter(List<WaqiObject> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public AqcinCellView onCreateViewHolder(final ViewGroup viewGroup, final int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        final AqcinCellView aqcinView = new AqcinCellView(view);
        aqcinView.setContext(this.context);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = aqcinView.getAdapterPosition();
                Toast.makeText(context, "Recycle Click" + position, Toast.LENGTH_SHORT).show();
            }
        });

        return aqcinView;
    }

    @Override
    public void onBindViewHolder(AqcinCellView aqcinCellView, int index) {
        WaqiObject myObject = list.get(index);
        aqcinCellView.setWaqiObject(myObject);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
