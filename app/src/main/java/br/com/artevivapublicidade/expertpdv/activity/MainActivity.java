package br.com.artevivapublicidade.expertpdv.activity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.artevivapublicidade.expertpdv.R;
import br.com.artevivapublicidade.expertpdv.activity.fragment.SelectPhotosOptionsFragment;
import br.com.artevivapublicidade.expertpdv.adapter.RecyclerGalleryAdapter;
import br.com.artevivapublicidade.expertpdv.model.Campaign;
import br.com.artevivapublicidade.expertpdv.model.City;
import br.com.artevivapublicidade.expertpdv.model.Photo;
import br.com.artevivapublicidade.expertpdv.model.Stage;
import br.com.artevivapublicidade.expertpdv.model.State;
import br.com.artevivapublicidade.expertpdv.service.CampaignService;
import br.com.artevivapublicidade.expertpdv.service.PhotoService;
import br.com.artevivapublicidade.expertpdv.service.UserLogin;
import br.com.artevivapublicidade.expertpdv.controller.GalleryController;
import br.com.artevivapublicidade.expertpdv.controller.CameraController;
import br.com.artevivapublicidade.expertpdv.util.ActivityResult;
import br.com.artevivapublicidade.expertpdv.util.FileUtil;
import br.com.artevivapublicidade.expertpdv.util.PermissionID;


public class MainActivity extends AppCompatActivity {
    private final int PICK_IMAGE_MULTIPLE = 1;
    private List<Photo> photos;

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

        if (userAccessCode == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);
        initFilter();
    }

    private void initFilter() {
        Spinner spnEtapaFiltro = (Spinner) findViewById(R.id.spnEtapaFiltro);
        CampaignService campaignService = new CampaignService(this);
        List<Campaign> campaigns = null;
        try {
            campaigns = campaignService.getCampaigns();
            if (campaigns.size() > 0) {
                List<Stage> stages = campaignService.getStages(campaigns.get(0).getCampaingId());
                ArrayAdapter<Stage> adapter = new ArrayAdapter<Stage>(getApplicationContext(), R.layout.spinner_item, stages);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spnEtapaFiltro.setAdapter(adapter);
                spnEtapaFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        try {
                            Stage stage = (Stage) parent.getItemAtPosition(position);
                            PhotoService photoService = new PhotoService(MainActivity.this);
                            photos = photoService.getPhotos(stage.getStageId(), stage.getCampaignId());
                            initRecycler();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        photos.clear();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecycler() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerGalleryAdapter adapter = new RecyclerGalleryAdapter(getApplicationContext(), photos);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    public void openSelectPhotosDialog(View view) {
        DialogFragment fragment = new SelectPhotosOptionsFragment();
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> paths = new ArrayList<String>();
        switch (requestCode) {
            case ActivityResult.CAMERA: {
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap photoCapturedBitmap = (Bitmap) extras.get("data");
                    Uri tempUri = FileUtil.getImageUri(getApplicationContext(), photoCapturedBitmap);

                    Intent intent = new Intent(MainActivity.this, SendImageActivity.class);
                    paths.add(FileUtil.getRealPathFromURI(this, tempUri));
                    intent.putExtra("picture", paths);
                    startActivity(intent);
                }
                break;
            }
            case ActivityResult.IMAGE_GALLERY: {
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        return;
                    }
                    try {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            ClipData clipData = data.getClipData();

                            if (clipData != null) {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    paths.add(FileUtil.getRealPathFromURI(this, clipData.getItemAt(i).getUri()));
                                }
                            } else {
                                paths.add(FileUtil.getRealPathFromURI(this, data.getData()));
                            }
                            Intent intent = new Intent(MainActivity.this, SendImageActivity.class);
                            intent.putExtra("picture", paths);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, SendImageActivity.class);
                            paths.add(FileUtil.getRealPathFromURI(this, data.getData()));
                            intent.putExtra("picture", paths);
                            startActivity(intent);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionID.PERMISSIONS_REQUEST_EXTERNAL_FILES: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GalleryController.getInstance().openGallery(this);
                }
                break;
            }
            case PermissionID.PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    CameraController.getInstance().openCamera(this);
                }
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_option: {
                UserLogin userLogin = new UserLogin(this);
                userLogin.clearUserAccessCode();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                return true;
            }
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


}
