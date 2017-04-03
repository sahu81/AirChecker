package com.example.dessusdi.myfirstapp.models.wikipedia;

/**
 * Created by Dimitri on 03/03/2017.
 * DESSUS Dimitri
 */

public class ImageObject {
    private final int pageid;
    private final int ns;
    private final String title;
    private final Thumbnail_ImageObject thumbnail;
    private final Original_ImageObject original;

    public ImageObject() {
        this.pageid = 0;
        this.ns = 0;
        this.title = "";
        this.thumbnail = new Thumbnail_ImageObject();
        this.original = new Original_ImageObject();
    }

    public Original_ImageObject getOriginal() {
        return original;
    }
}
