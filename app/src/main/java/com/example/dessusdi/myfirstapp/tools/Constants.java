package com.example.dessusdi.myfirstapp.tools;

import android.provider.BaseColumns;

/**
 * Created by dessusdi on 10/02/2017.
 * DESSUS Dimitri
 */
public final class Constants {
    public final class Url {
        public static final String BASE_URL = "https://api.waqi.info/api/feed/%%CITY_ID%%/obs.en.json";

        private Url() {}
    }

    public static class Database implements BaseColumns {

        public static final int DATABASE_VERSION    = 1;
        public static final String DATABASE_NAME    = "waqi.db";
        public static final String TABLE_NAME       = "AIR_QUALITY";
        public static final String COLUMN_CITY_ID   = "identifier";

        private Database() {}
    }

    private Constants() {}
}