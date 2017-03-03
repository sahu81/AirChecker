package com.example.dessusdi.myfirstapp.models.wikipedia;

/**
 * Created by Dimitri on 03/03/2017.
 */

public class ImageObject {
    private int pageid;
    private int ns;
    private String title;
    private Thumbnail_ImageObject thumbnail;
    private Original_ImageObject original;

    public ImageObject() {
        this.pageid = 0;
        this.ns = 0;
        this.title = "";
        this.thumbnail = new Thumbnail_ImageObject();
        this.original = new Original_ImageObject();
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

    public Thumbnail_ImageObject getThumbnail() {
        return thumbnail;
    }

    public Original_ImageObject getOriginal() {
        return original;
    }
}
