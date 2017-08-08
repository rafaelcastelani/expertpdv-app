package br.com.artevivapublicidade.expertpdv;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class SelectPhotosOptionsFragment extends DialogFragment {
    private final int IMAGE_GALLERY = 1;
    private final int CAMERA = 2;

    private String photoPath = "sdcard/ExpertPDV";
    private PhotoFile photoFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoFile = PhotoFile.getInstance();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String options[] = getResources().getStringArray(R.array.select_photos_options);
        //Utiliza a classe Builder para construção da caixa de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_photos_options_fragment);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               /* Toast.makeText(getActivity(), "Acessando " + options[i] + "...",
                        Toast.LENGTH_SHORT).show();*/
                switch(i) {
                    case 0:
                        accessPhotoGallery();
                        break;
                    case 1:
                        accessCamera();
                        break;
                }
            }
        });
        return builder.create();
    }

    private void accessPhotoGallery() {
        Intent intent = new Intent(getActivity(), ImageGalleryActivity.class);
        startActivityForResult(intent, IMAGE_GALLERY);
    }

    private void accessCamera() {
        PackageManager pm = getContext().getPackageManager();
        int numberOfCameras = Camera.getNumberOfCameras();

        if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && numberOfCameras > 0) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(takePictureIntent.resolveActivity(getActivity().getPackageManager())!=null) {
                savePhotoFile();
                takePictureIntent.putExtra("photoFile", Uri.fromFile(photoFile.getFile()));
                takePictureIntent.putExtra("photoFile2", "teste 2");
                getActivity().startActivityForResult(takePictureIntent, CAMERA);
            }
        } else {
            Toast.makeText(getActivity(), R.string.error_camera_not_found,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void savePhotoFile() {
        File folder = new File(photoPath);

        if(!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(folder, System.currentTimeMillis()+"");
        photoFile.setFile(file);
    }
}
