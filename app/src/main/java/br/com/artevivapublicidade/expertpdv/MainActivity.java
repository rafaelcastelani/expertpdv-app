package br.com.artevivapublicidade.expertpdv;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final int PICK_IMAGE_MULTIPLE = 1;

    //Necessário para habilitar exibição de imagens em vetor (no caso o ícone de "check" na seleção de foto)
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserLogin userLogin = new UserLogin(getApplicationContext());
        String userAccessCode = userLogin.getUserAccessCode();

        if(userAccessCode == null) {
            Intent intentSearchUserInfo = new Intent(this, SearchUserInfoActivity.class);
            startActivity(intentSearchUserInfo);
        }
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

            Bundle extras = data.getExtras();
            Bitmap photoCapturedBitmap = (Bitmap) extras.get("data");

            // Pega a URI do Bitmap para recuperar o arquivo
            Uri tempUri = getImageUri(getApplicationContext(), photoCapturedBitmap);
            String photoPath = tempUri.getPath();
            File photoFile = new File(photoPath);
            String photoName = photoFile.getName();

            Photo catalogPhoto = new Photo(getApplicationContext());
            ContentValues contentValues = new ContentValues();
            contentValues.put("pathPhoto", photoPath);
            contentValues.put("filePhoto", photoName);

            catalogPhoto.insert(null, contentValues);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, null, null);
        return Uri.parse(path);
    }
}
