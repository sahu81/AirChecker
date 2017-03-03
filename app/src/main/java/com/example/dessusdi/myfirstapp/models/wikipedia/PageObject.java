package com.example.dessusdi.myfirstapp.models.wikipedia;

import android.content.Context;

import com.example.dessusdi.myfirstapp.R;

/**
 * Created by Dimitri on 03/03/2017.
 */

public class PageObject {
    private int pageid;
    private int ns;
    private String title;
    private String extract;

    public PageObject(Context context) {
        this.pageid     = 0;
        this.ns         = 0;
        this.title      = context.getResources().getString(R.string.city_empty_title);
        this.extract    = context.getResources().getString(R.string.city_empty_informations);
    }

    public int getPageid() {
        return pageid;
    }

    public int getNs() {
        return ns;
    }

    public String getTitle() {
        return title;
    }

    public String getExtract() {
        return extract.replace("\n", "\n\n");
    }
}
