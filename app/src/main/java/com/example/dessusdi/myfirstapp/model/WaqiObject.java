package com.example.dessusdi.myfirstapp.model;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.dessusdi.myfirstapp.MainActivity;
import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.recycler_view.AqcinListAdapter;
import com.example.dessusdi.myfirstapp.tools.AqcinRequestService;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class WaqiObject {
    private AqcinRequestService waqiService;
    private GlobalObject globalObject;
    private AqcinListAdapter adpaterList;

    public WaqiObject(AqcinRequestService waqiService, AqcinListAdapter adpater) {
        this.waqiService = waqiService;
        this.adpaterList = adpater;
    }

    public void fetchData() {
        this.waqiService.sendRequestWithUrl("https://api.waqi.info/api/feed/@3069/obs.en.json",
        new AqcinRequestService.VolleyCallback() {
            @Override
            public void onSuccess(GlobalObject global) {
                globalObject = global;
                adpaterList.notifyDataSetChanged();
                //mTextView.setText(global.getRxs().getObs().get(0).getMsg().getCity().getName());
            }
        });
        // TODO: Completion block and assign to globalobject
    }

    public interface DataRetrievedCallback{
        void onSuccess();
    }

    public String getName() {
        String name = "Loading...";
        if (this.globalObject != null) {
            name = this.globalObject.getRxs().getObs().get(0).getMsg().getCity().getName();
        }
        return name;
    }

    public String getId() {
        return this.globalObject.getRxs().getObs().get(0).getMsg().getCity().getId();
    }
}
