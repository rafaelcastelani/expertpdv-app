package br.com.artevivapublicidade.expertpdv.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

import br.com.artevivapublicidade.expertpdv.R;
import br.com.artevivapublicidade.expertpdv.util.ActivityResult;

/**
 * Created by Ricardo Melo on 05/01/2018.
 */

public class CameraController {
    private File photoFile;
    private String photoPath = "sdcard/ExpertPDV";

    private static volatile CameraController sSoleInstance = new CameraController();

    //private constructor.
    private CameraController(){}

    public static CameraController getInstance() {
        return sSoleInstance;
    }

    public void openCamera(Activity context) {
        PackageManager pm = context.getPackageManager();
        int numberOfCameras = Camera.getNumberOfCameras();

        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && numberOfCameras > 0) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
//                savePhotoFile();
//                takePictureIntent.putExtra("photoFile", Uri.fromFile(getFile()));
                context.startActivityForResult(takePictureIntent, ActivityResult.CAMERA);
            }
        } else {
            Toast.makeText(context, R.string.error_camera_not_found,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void savePhotoFile() {
        File folder = new File(photoPath);

        if (!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(folder, System.currentTimeMillis() + ".jpg");
        setFile(file);
    }

    public void setFile(File photoFile) {
        this.photoFile = photoFile;
    }

    public File getFile() {
        return this.photoFile;
    }
}
