package br.com.artevivapublicidade.expertpdv.activity.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import br.com.artevivapublicidade.expertpdv.R;
import br.com.artevivapublicidade.expertpdv.controller.GalleryController;
import br.com.artevivapublicidade.expertpdv.controller.CameraController;
import br.com.artevivapublicidade.expertpdv.util.PermissionID;

public class SelectPhotosOptionsFragment extends DialogFragment {


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
                switch (i) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(this.getContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(this.getContext());
                    }
                    builder.setTitle("Permissão de galeria")
                            .setMessage("É necessária permissão dos seus arquivos para acessar as fotos.")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PermissionID.PERMISSIONS_REQUEST_EXTERNAL_FILES);
                                    }
                                }
                            })
                            .show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PermissionID.PERMISSIONS_REQUEST_EXTERNAL_FILES);
                }
            } else {
                GalleryController.getInstance().openGallery(this.getActivity());
//                Intent intent = new Intent(getActivity(), ImageGalleryActivity.class);
//                startActivityForResult(intent, ActivityResult.IMAGE_GALLERY);
            }
        } else {
            GalleryController.getInstance().openGallery(this.getActivity());
//            Intent intent = new Intent(getActivity(), ImageGalleryActivity.class);
//            startActivityForResult(intent, ActivityResult.IMAGE_GALLERY);
        }
    }

    private void accessCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(this.getContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(this.getContext());
                    }
                    builder.setTitle("Permissão de câmera e gravação de arquivo")
                            .setMessage("É necessária permissão da sua câmera para tirar uma fotografia.")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionID.PERMISSIONS_REQUEST_CAMERA);
                                    }
                                }
                            })
                            .show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionID.PERMISSIONS_REQUEST_CAMERA);
                }
            } else {
                CameraController.getInstance().openCamera(this.getActivity());
            }

        } else {
            CameraController.getInstance().openCamera(this.getActivity());
        }
    }


}
