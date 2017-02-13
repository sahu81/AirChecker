package com.example.dessusdi.myfirstapp.models.air_quality;

import android.util.Log;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.recycler_view.AqcinListAdapter;
import com.example.dessusdi.myfirstapp.tools.AqcinRequestService;
import com.example.dessusdi.myfirstapp.tools.RequestBuilder;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class WaqiObject extends SugarRecord {
    @Ignore
    private AqcinRequestService waqiService;

    @Ignore
    private GlobalObject globalObject;

    @Ignore
    private AqcinListAdapter adpaterList;

    @Ignore
    private String url = "";

    int identifier = 0;

    public WaqiObject() {
        super();
    }

    public WaqiObject(int cityID, AqcinRequestService waqiService, AqcinListAdapter adpater) {
        this.url            = RequestBuilder.buildAirQualityURL(cityID);
        this.waqiService    = waqiService;
        this.adpaterList    = adpater;
        this.identifier     = cityID;
    }

    public void fetchData() {
        this.waqiService.fetchAirQuality(this.identifier,
        new AqcinRequestService.GlobalObjectCallback() {
            @Override
            public void onSuccess(GlobalObject global) {
                setGlobalObject(global);
                Log.d("DATABASE", "Data fetched !");
            }
        });
    }

    public void setRequestService(AqcinRequestService waqiService) {
        this.waqiService = waqiService;
    }

    public void setGlobalObject(GlobalObject globalObject) {
        this.globalObject = globalObject;
        adpaterList.notifyDataSetChanged();
    }

    public void setAqcinListAdapter(AqcinListAdapter adpaterList) {
        this.adpaterList = adpaterList;
    }

    public String getName() {
        String name = "";
        if (this.globalObject != null) {
            name = this.globalObject.getRxs().getObs().get(0).getMsg().getCity().getName();
        }
        return name;
    }

    public int getAirQuality() {
        int airQuality = 0;
        if (this.globalObject != null) {
            airQuality = this.globalObject.getRxs().getObs().get(0).getMsg().getAqi();
        }
        return airQuality;
    }

    public String getColorCode() {
        int airQuality = this.getAirQuality();
        String color = "#e74c3c";

        if(airQuality < 40)
            color = "#1abc9c";
        else if(airQuality >= 40 && airQuality < 90)
            color = "#f39c12";

        return color;
    }

    public int getSoundResId() {
        int soundResID;
        int airQuality = this.getAirQuality();
        if(airQuality < 100)
            soundResID = R.raw.no_alert;
        else if (airQuality >= 100 && airQuality < 200)
            soundResID = R.raw.alert_mid;
        else
            soundResID = R.raw.alert_max;

        return soundResID;
    }

    public String getGPSCoordinate() {
        String airQuality = "Loading...";
        if (this.globalObject != null) {
            airQuality = String.format("GPS : %.6f - %.6f", this.globalObject.getRxs().getObs().get(0).getMsg().getCity().getGeo().get(0), this.globalObject.getRxs().getObs().get(0).getMsg().getCity().getGeo().get(1));
        }
        return airQuality;
    }

    public String getMinTemp() {
        String minTemp = "";
        if (this.globalObject != null) {
            if(this.globalObject.getRxs().getObs().get(0).getMsg().getForecast().getAqi().size() > 0) {
                minTemp = String.format("Min : %d°C", this.globalObject.getRxs().getObs().get(0).getMsg().getForecast().getAqi().get(0).getV().get(0));
            }
        }
        return minTemp;
    }

    public String getMaxTemp() {
        String maxTemp = "";
        if (this.globalObject != null) {
            if(this.globalObject.getRxs().getObs().get(0).getMsg().getForecast().getAqi().size() > 0) {
                maxTemp = String.format("Max : %d°C", this.globalObject.getRxs().getObs().get(0).getMsg().getForecast().getAqi().get(0).getV().get(1));
            }
        }
        return maxTemp;
    }

    public int getIdentifier() {
        return this.identifier;
    }
}
