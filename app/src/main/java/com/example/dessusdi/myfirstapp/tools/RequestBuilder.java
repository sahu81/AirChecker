package com.example.dessusdi.myfirstapp.tools;

import com.example.dessusdi.myfirstapp.Constants;

/**
 * Created by dessusdi on 10/02/2017.
 * DESSUS Dimitri
 */
public abstract class RequestBuilder {
    /**
     * @param identifier city identifier
     * @return built request string
     */
    public static String buildAirQualityURL(int identifier) {
        return Constants.Url.AIR_QUALITY_BASE_URL.replace("%%CITY_ID%%", "@" + identifier);
    }

    /**
     * @param search city search string
     * @return built request string
     */
    public static String buildCityIdURL(String search) {
        String urlStr = Constants.Url.CITY_SEARCH_BASE_URL;
        urlStr = urlStr.replace("%%TOKEN%%", Constants.Url.TOKEN);
        urlStr += search;
        return urlStr;
    }

    /**
     * @param latitude user's latitude
     * @param longitude user's longitude
     * @return built request string
     */
    public static String buildCitiesAroundPositionURL(double latitude, double longitude) {
        String urlStr = Constants.Url.CITY_POSITION_BASE_URL;
        urlStr = urlStr.replace("%%TOKEN%%", Constants.Url.TOKEN);
        urlStr = urlStr.replace("%%LAT%%", String.valueOf(latitude));
        urlStr = urlStr.replace("%%LNG%%", String.valueOf(longitude));
        return urlStr;
    }

    /**
     * @param city city search string
     * @return built request string
     */
    public static String buildCityInformationURL(String city) {
        return Constants.Url.CITY_INFORMATION_BASE_URL.concat(city);
    }

    /**
     * @param city city search string
     * @return built request string
     */
    public static String buildCityImageURL(String city) {
        return Constants.Url.CITY_IMAGE_BASE_URL.concat(city);
    }
}
