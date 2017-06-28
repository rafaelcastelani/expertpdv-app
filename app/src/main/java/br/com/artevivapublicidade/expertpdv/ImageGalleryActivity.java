package br.com.artevivapublicidade.expertpdv;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.File;


public class ImageGalleryActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Uri mGalleryFolder = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        mRecyclerView = (RecyclerView) findViewById(R.id.galleryRecyclerView);
        //Cria um GridLayout com 1 coluna
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        //Associa este layout ao RecyclerView
        mRecyclerView.setLayoutManager(layoutManager);

        //Cria o objeto do layout e associa ao RecyclerVIew
        RecyclerView.Adapter imageAdapter = new ImageAdapter(mGalleryFolder, this);
        mRecyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onBackPressed() {
        setResult(AppCompatActivity.RESULT_CANCELED);
        super.onBackPressed();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        RecyclerView.Adapter newImageAdapter = new ImageAdapter(mGalleryFolder, this);
        mRecyclerView.swapAdapter(newImageAdapter, false);
    }
}


