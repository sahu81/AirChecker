package com.example.dessusdi.myfirstapp.tools;

/**
 * Created by dessusdi on 10/02/2017.
 * DESSUS Dimitri
 */
public abstract class RequestBuilder {
    public static String buildAirQualityURL(String identifier) {
        return Constants.Url.AIR_QUALITY_BASE_URL.replace("%%CITY_ID%%", identifier.replaceAll("\\s+",""));
    }

    public static String buildCityIdURL(String search) {
        String urlStr = Constants.Url.CITY_SEARCH_BASE_URL;
        urlStr = urlStr.replace("%%TOKEN%%", Constants.Url.TOKEN);
        urlStr += search;
        return urlStr;
    }
}
