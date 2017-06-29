package br.com.artevivapublicidade.expertpdv;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;


public class ImageGalleryActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Uri mGalleryFolder = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static int mColumnCount = 3;
    private static int mImageWidth;
    private static int mImageHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        //Pega o width do display para casar com o tamanho das colunas do grid
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mImageWidth = displayMetrics.widthPixels / mColumnCount;
        //Ratio das imagens que são tiradas pela câmera (em portrait mode);
        mImageHeight = mImageWidth * 4 / 3;

        mRecyclerView = (RecyclerView) findViewById(R.id.galleryRecyclerView);
        //Cria um GridLayout com 1 coluna
        GridLayoutManager layoutManager = new GridLayoutManager(this, mColumnCount);
        //Associa este layout ao RecyclerView
        mRecyclerView.setLayoutManager(layoutManager);

        //Cria o objeto do layout e associa ao RecyclerVIew
        RecyclerView.Adapter imageAdapter = new ImageAdapter(mGalleryFolder, mImageWidth, mImageHeight, this);
        mRecyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onBackPressed() {
        setResult(AppCompatActivity.RESULT_CANCELED);
        super.onBackPressed();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        RecyclerView.Adapter newImageAdapter = new ImageAdapter(mGalleryFolder, mImageWidth, mImageHeight, this);
        mRecyclerView.swapAdapter(newImageAdapter, false);
    }
}


