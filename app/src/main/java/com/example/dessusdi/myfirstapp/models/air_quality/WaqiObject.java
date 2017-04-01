package com.example.dessusdi.myfirstapp.models.air_quality;

import android.graphics.Color;
import android.util.Log;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.recycler_view.AqcinListAdapter;
import com.example.dessusdi.myfirstapp.tools.AqcinRequestService;
import com.example.dessusdi.myfirstapp.tools.RequestBuilder;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
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
    private AqcinListAdapter adpaterList;

    @Ignore
    private String url = "";

    private String searchQuery = "";
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
                Log.d("SERVICE", "Data fetched !");
            }
        });
    }

    public void setRequestService(AqcinRequestService waqiService) {
        this.waqiService = waqiService;
    }

    public void setGlobalObject(GlobalObject globalObject) {
        this.globalObject = globalObject;

        if(adpaterList != null)
            adpaterList.notifyDataSetChanged();
    }

    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public String getName() {
        String name = "";
        if (this.globalObject != null) {
        }
        return name;
    }

    public int getAirQuality() {
        int airQuality = 0;
        if (this.globalObject != null) {
        }
        return airQuality;
    }

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
        }
        return airQuality;
    }

    public String getMinTemp() {
        String minTemp = "";
        if (this.globalObject != null) {
            }
        }
        return minTemp;
    }

    public String getMaxTemp() {
        String maxTemp = "";
        if (this.globalObject != null) {
            int index = globalObject.getRxs().getObs().size() - 1;
            if(this.globalObject.getRxs().getObs().get(index).getMsg().getForecast().getAqi().size() > 0) {
            }
        }
        return maxTemp;
    }

    public LineChartData getForecastChartData() {

        // Dates formatter
        SimpleDateFormat fullFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd");
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");

        List<PointValue> values = new ArrayList<PointValue>();
        List<Float> xAxisValues1 = new ArrayList<Float>();
        List<String> xAxisValues2 = new ArrayList<String>();

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
        List<Line> lines = new ArrayList<Line>();
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

    public int getIdentifier() {
        return this.identifier;
    }
}
