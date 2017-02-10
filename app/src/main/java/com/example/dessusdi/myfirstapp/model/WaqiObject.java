package com.example.dessusdi.myfirstapp.model;

import android.util.Log;

import com.example.dessusdi.myfirstapp.recycler_view.AqcinListAdapter;
import com.example.dessusdi.myfirstapp.tools.AqcinRequestService;
import com.example.dessusdi.myfirstapp.tools.Constants;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

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

    String identifier = "";

    public WaqiObject() {
        super();
    }

    public WaqiObject(String cityID, AqcinRequestService waqiService, AqcinListAdapter adpater) {
        this.url            = this.getUrl(cityID);
        this.waqiService    = waqiService;
        this.adpaterList    = adpater;
        this.identifier     = cityID;
    }

    public void fetchData(final List<WaqiObject> array) {
        this.url = this.getUrl(this.identifier);
        this.waqiService.sendRequestWithUrl(this.url,
        new AqcinRequestService.VolleyCallback() {
            @Override
            public void onSuccess(GlobalObject global) {
                setGlobalObject(global);
                adpaterList.notifyDataSetChanged();
                Log.d("DATABASE", "Data fetched !");
            }
        });
    }

    public String getUrl(String identifier) {
        return Constants.Url.BASE_URL.replace("%%CITY_ID%%", identifier);
    }

    public void setRequestService(AqcinRequestService waqiService) {
        this.waqiService = waqiService;
    }

    public void setGlobalObject(GlobalObject globalObject) {
        this.globalObject = globalObject;
    }

    public void setAqcinListAdapter(AqcinListAdapter adpaterList) {
        this.adpaterList = adpaterList;
    }

    public String getName() {
        String name = "Loading...";
        if (this.globalObject != null) {
            name = this.globalObject.getRxs().getObs().get(0).getMsg().getCity().getName();
        }
        return name;
    }

    public String getAirQuality() {
        int airQuality = 0;
        if (this.globalObject != null) {
            airQuality = this.globalObject.getRxs().getObs().get(0).getMsg().getAqi();
        }
        return String.valueOf(airQuality);
    }

    public String getColorCode() {
        int airQuality = Integer.parseInt(this.getAirQuality());
        String color = "#e74c3c";

        if(airQuality < 40)
            color = "#1abc9c";
        else if(airQuality >= 40 && airQuality < 90)
            color = "#f39c12";

        return color;
    }

    public String getGPSCoordinate() {
        String airQuality = "(not found)";
        if (this.globalObject != null) {
            airQuality = String.format("GPS : %.6f - %.6f", this.globalObject.getRxs().getObs().get(0).getMsg().getCity().getGeo().get(0), this.globalObject.getRxs().getObs().get(0).getMsg().getCity().getGeo().get(1));
        }
        return airQuality;
    }

    public String getMinTemp() {
        String minTemp = "0";
        if (this.globalObject != null) {
            if(this.globalObject.getRxs().getObs().get(0).getMsg().getForecast().getAqi().size() > 0) {
                minTemp = String.format("Min : %d°C", this.globalObject.getRxs().getObs().get(0).getMsg().getForecast().getAqi().get(0).getV().get(0));
            }
        }
        return minTemp;
    }

    public String getMaxTemp() {
        String maxTemp = "0";
        if (this.globalObject != null) {
            if(this.globalObject.getRxs().getObs().get(0).getMsg().getForecast().getAqi().size() > 0) {
                maxTemp = String.format("Max : %d°C", this.globalObject.getRxs().getObs().get(0).getMsg().getForecast().getAqi().get(0).getV().get(1));
            }
        }
        return maxTemp;
    }

    public String getIdentifier() {
        return this.identifier;
    }
}
