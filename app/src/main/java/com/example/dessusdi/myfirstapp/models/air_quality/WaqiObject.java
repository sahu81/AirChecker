package com.example.dessusdi.myfirstapp.models.air_quality;

import android.graphics.Color;
import android.util.Log;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.recycler_view.AqcinListAdapter;
import com.example.dessusdi.myfirstapp.services.AqcinRequestService;
import com.example.dessusdi.myfirstapp.tools.RequestBuilder;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;

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
    private AqcinListAdapter adapterList;

    @Ignore
    private String url = "";

    private String searchQuery = "";
    private int identifier = 0;

    public WaqiObject() {
        super();
    }

    public WaqiObject(int cityID) {
        this.identifier = cityID;
    }

    public WaqiObject(int cityID, AqcinRequestService waqiService, AqcinListAdapter adapter) {
        this.url            = RequestBuilder.buildAirQualityURL(cityID);
        this.waqiService    = waqiService;
        this.adapterList    = adapter;
        this.identifier     = cityID;
    }

    /**
     * Fetching global data (aqi, name etc.)
     */
    public void fetchData() {
        this.waqiService.fetchAirQuality(this.identifier,
        new AqcinRequestService.GlobalObjectCallback() {
            @Override
            public void onSuccess(GlobalObject global) {
                setGlobalObject(global);
                Log.d("SERVICE", "Data fetched !");
            }
        });
    }

    /**
     * Aqcin setter
     * @param waqiService
     */
    public void setRequestService(AqcinRequestService waqiService) {
        this.waqiService = waqiService;
    }

    /**
     * Global object setter
     * @param globalObject
     */
    public void setGlobalObject(GlobalObject globalObject) {
        this.globalObject = globalObject;

        if(this.globalObject != null)
            Log.d("SERVICE", "Status " + globalObject.getRxs().getObs().get(0).getStatus());

        if(adapterList != null)
            adapterList.notifyDataSetChanged();
    }

    /**
     * List adapter setter
     * @param adapterList
     */
    public void setAqcinListAdapter(AqcinListAdapter adapterList) {
        this.adapterList = adapterList;
    }

    /**
     * Search query setter
     * @param searchQuery
     */
    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    /**
     * @return Search query string
     */
    public String getSearchQuery() {
        return searchQuery;
    }

    /**
     * @return City name
     */
    public String getName() {
        String name = "";

        if (this.globalObject != null) {
            int index = globalObject.getRxs().getObs().size() - 1;
            name = this.globalObject.getRxs().getObs().get(index).getMsg().getCity().getName();
        }
        return name;
    }

    /**
     * @return Air quality of the station
     */
    public int getAirQuality() {
        int airQuality = 0;
        if (this.globalObject != null) {
            int index = globalObject.getRxs().getObs().size() - 1;
            airQuality = this.globalObject.getRxs().getObs().get(index).getMsg().getAqi();
        }
        return airQuality;
    }

    /**
     * @param airQuality
     * @return Properly color according to air quality level
     */
    public static String getColorCode(int airQuality) {
        String color = "#e74c3c";

        if(airQuality < 40)
            color = "#1abc9c";
        else if(airQuality >= 40 && airQuality < 90)
            color = "#f39c12";

        return color;
    }

    /**
     * @return Properly sound according to air quality level
     */
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

    /**
     * @return Formatted string of station location
     */
    public String getGPSCoordinate() {
        String airQuality = "Loading...";
        if (this.globalObject != null) {
            int index = globalObject.getRxs().getObs().size() - 1;
            airQuality = String.format("GPS : %.6f - %.6f", this.globalObject.getRxs().getObs().get(index).getMsg().getCity().getGeo().get(0), this.globalObject.getRxs().getObs().get(index).getMsg().getCity().getGeo().get(1));
        }
        return airQuality;
    }

    /**
     * @return Minimum temperature
     */
    public String getMinTemp() {
        String minTemp = "";
        if (this.globalObject != null) {
            int index = globalObject.getRxs().getObs().size() - 1;
            if(this.globalObject.getRxs().getObs().get(index).getMsg().getForecast().getAqi().size() > 0) {
                minTemp = String.format("Min : %d°C", this.globalObject.getRxs().getObs().get(index).getMsg().getForecast().getAqi().get(0).getV().get(0));
            }
        }
        return minTemp;
    }

    /**
     * @return Maximum temperature
     */
    public String getMaxTemp() {
        String maxTemp = "";
        if (this.globalObject != null) {
            int index = globalObject.getRxs().getObs().size() - 1;
            if(this.globalObject.getRxs().getObs().get(index).getMsg().getForecast().getAqi().size() > 0) {
                maxTemp = String.format("Max : %d°C", this.globalObject.getRxs().getObs().get(index).getMsg().getForecast().getAqi().get(0).getV().get(1));
            }
        }
        return maxTemp;
    }

    /**
     * Build chart data of station's forecast
     * @return Forecast line chart data
     */
    public LineChartData getForecastChartData() {
        int indexArray = globalObject.getRxs().getObs().size() - 1;
        ArrayList<AqiObject> forecast = this.globalObject.getRxs().getObs().get(indexArray).getMsg().getForecast().getAqi();

        // Dates formatter
        SimpleDateFormat fullFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd");
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");

        List<PointValue> values = new ArrayList<>();
        List<Float> xAxisValues1 = new ArrayList<>();
        List<String> xAxisValues2 = new ArrayList<>();

        int index = 0;
        float maxVal = 0;
        String day = "--";
        for (AqiObject aqcin : forecast) {
            values.add(new PointValue(index, aqcin.getV().get(0)));
            xAxisValues1.add((float) index);

            // Display x axis values
            try {
                Date date = fullFormatter.parse(aqcin.getT());

                String dayName = dayFormatter.format(date);
                String month = monthFormatter.format(date);

                day = dayName + "/" + month;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            xAxisValues2.add(day);

            // Searching max value
            if(maxVal < aqcin.getV().get(0).floatValue())
                maxVal = aqcin.getV().get(0).floatValue();

            index++;
        }

        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        Axis xAxis = Axis.generateAxisFromCollection(xAxisValues1, xAxisValues2);
        xAxis.setAutoGenerated(false);
        xAxis.setTextSize(8);
        xAxis.setHasLines(true);

        Axis yAxis = Axis.generateAxisFromRange(0f, maxVal, 1f);
        yAxis.setTextSize(8);
        yAxis.setHasLines(true);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisYLeft(yAxis);
        data.setAxisXBottom(xAxis);

        return data;
    }

    /**
     * @return Station's identifier
     */
    public int getIdentifier() {
        return this.identifier;
    }
}
