package br.com.artevivapublicidade.expertpdv.controller;

import android.app.Activity;
import android.content.Intent;

import br.com.artevivapublicidade.expertpdv.util.ActivityResult;

public class GalleryController {

    private static volatile GalleryController sSoleInstance = new GalleryController();

    //private constructor.
    private GalleryController(){}

    public static GalleryController getInstance() {
        return sSoleInstance;
    }

    public void openGallery(Activity context) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        context.startActivityForResult(getIntent, ActivityResult.IMAGE_GALLERY);
    }
}
