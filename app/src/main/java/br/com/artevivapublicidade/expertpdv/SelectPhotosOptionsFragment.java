package br.com.artevivapublicidade.expertpdv;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class SelectPhotosOptionsFragment extends DialogFragment {
    private final int IMAGE_GALLERY = 1;
    private final int CAMERA = 2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            startActivityForResult(takePictureIntent, CAMERA);
        } else {
            Toast.makeText(getActivity(), R.string.error_camera_not_found,
                    Toast.LENGTH_LONG).show();
        }

    }
}
