package com.example.dessusdi.myfirstapp.recycler_view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dessusdi.myfirstapp.MainActivity;
import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.fragments.DetailsFragment;
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
                Toast.makeText(context, "Recycle Click --> " + list.get(position).getName(), Toast.LENGTH_SHORT).show();

                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

                // Testing if layout support multiple screen
                if(dpWidth < 800.0) {
                    // Doesn't support so replace with a new fragment
                    DetailsFragment fragment = new DetailsFragment();
                    Bundle args = new Bundle();
                    args.putString("data", "This data has sent to FragmentTwo");
                    fragment.setArguments(args);
                    FragmentTransaction transaction = ((MainActivity) context).getFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_container, fragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    // Support multiple screen so use current details fragment
                    FragmentManager fragmentManager = ((MainActivity) context).getFragmentManager();
                    DetailsFragment detailsFragment = (DetailsFragment) fragmentManager.findFragmentById(R.id.detail_fragment);
                    detailsFragment.setCity(list.get(position));
                }
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
