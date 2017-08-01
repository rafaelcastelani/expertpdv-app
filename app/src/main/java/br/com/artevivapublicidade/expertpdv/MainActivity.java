package br.com.artevivapublicidade.expertpdv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout lnrImages;
    private ArrayList<String> imagesPathList;
    private Bitmap imageBitmap;
    private Bitmap resized;
    private final int PICK_IMAGE_MULTIPLE = 1;

    //Necessário para habilitar exibição de imagens em vetor (no caso o ícone de "check" na seleção de foto)
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lnrImages = (LinearLayout) findViewById(R.id.lnrImages);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnSelectPhotos:
                Intent intent = new Intent(MainActivity.this, ImageGalleryActivity.class);
                startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
        }
    }

    public void openSelectPhotosDialog(View view) {
        DialogFragment fragment = new SelectPhotosOptionsFragment();
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            //if(requestCode == )
        }
    }
}
