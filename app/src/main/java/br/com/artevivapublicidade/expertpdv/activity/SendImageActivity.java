package br.com.artevivapublicidade.expertpdv.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import br.com.artevivapublicidade.expertpdv.R;
import br.com.artevivapublicidade.expertpdv.model.Campaign;
import br.com.artevivapublicidade.expertpdv.model.City;
import br.com.artevivapublicidade.expertpdv.model.Stage;
import br.com.artevivapublicidade.expertpdv.model.State;
import br.com.artevivapublicidade.expertpdv.model.Store;
import br.com.artevivapublicidade.expertpdv.model.User;
import br.com.artevivapublicidade.expertpdv.service.CampaignService;
import br.com.artevivapublicidade.expertpdv.service.LocaleService;
import br.com.artevivapublicidade.expertpdv.service.PhotoService;

public class SendImageActivity extends AppCompatActivity {
    private ViewFlipper flipper;
    private Spinner spnEstado;
    private Spinner spnCidade;
    private Spinner spnLoja;
    private Spinner spnEtapa;
    private AdapterView.OnItemSelectedListener estadoListener;
    private AdapterView.OnItemSelectedListener cidadeListener;
    private Button btnEnviar;

    private LocaleService localeService;
    private CampaignService campaignService;
    private List<State> states;
    private List<City> cities;
    private List<Store> stores;

    private ArrayList<String> filePaths;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_image);

        localeService = new LocaleService(this);
        campaignService = new CampaignService(this);

        Bundle extras = getIntent().getExtras();
        this.filePaths = extras.getStringArrayList("picture");

        spnEstado = (Spinner) findViewById(R.id.spnEstado);
        spnCidade = (Spinner) findViewById(R.id.spnCidade);
        spnLoja = (Spinner) findViewById(R.id.spnLoja);
        spnEtapa = (Spinner) findViewById(R.id.spnEtapa);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        flipper = (ViewFlipper) findViewById(R.id.flipper);

        initSpinners();
        initButton();
        initFlipper();
    }

    private void initFlipper() {
        for (int i = 0; i < this.filePaths.size(); i++) {
            Bitmap bmp = BitmapFactory.decodeFile(this.filePaths.get(i));
            ImageView imgView = new ImageView(this);
            imgView.setImageBitmap(bmp);
            flipper.addView(imgView);
        }
        flipper.setAutoStart(true);
        flipper.setFlipInterval(1000);
        flipper.startFlipping();
    }

    private void initButton() {
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoService srvc = new PhotoService(getApplicationContext());
                boolean erro = false;

                int a = spnLoja.getSelectedItemPosition();
                if (spnLoja.getSelectedItemPosition() < 0) {
                    Toast.makeText(getApplicationContext(), "Selecione uma loja", Toast.LENGTH_SHORT).show();
                    erro = true;
                    return;
                }

                if (spnEtapa.getSelectedItemPosition() < 0) {
                    Toast.makeText(getApplicationContext(), "Selecione uma etapa", Toast.LENGTH_SHORT).show();
                    erro = true;
                    return;
                }

                Stage stage = (Stage) spnEtapa.getSelectedItem();
                Store store = (Store) spnLoja.getSelectedItem();
                ArrayList<File> files = new ArrayList<File>();
                for (int i = 0; i < filePaths.size(); i++) {
                    files.add(new File(filePaths.get(i)));
                }

                final ProgressDialog progressDialog = new ProgressDialog(SendImageActivity.this);
                progressDialog.setMessage("Enviando...");
                progressDialog.show();
                UploadTask task = new UploadTask(getApplicationContext(), progressDialog, stage.getStageId(), stage.getCampaignId(), store.getStoreId(), files);
                task.execute();
            }
        });
    }

    private void initSpinners() {
        estadoListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                try {
                    State state = (State) parent.getItemAtPosition(position);
                    cities = localeService.getCity(state.getStateId());
                    ArrayAdapter<City> adapter = new ArrayAdapter<City>(getApplicationContext(), R.layout.spinner_item, cities);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spnCidade.setAdapter(adapter);
                    spnCidade.setOnItemSelectedListener(cidadeListener);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                stores.clear();
                ((BaseAdapter) spnLoja.getAdapter()).notifyDataSetChanged();
                spnLoja.invalidate();
            }
        };

        cidadeListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                try {
                    City state = (City) parent.getItemAtPosition(position);
                    stores = localeService.getStore(state.getCityId());
                    ArrayAdapter<Store> adapter = new ArrayAdapter<Store>(getApplicationContext(), R.layout.spinner_item, stores);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spnLoja.setAdapter(adapter);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cities.clear();
                ((BaseAdapter) spnCidade.getAdapter()).notifyDataSetChanged();
                spnCidade.invalidate();
            }
        };

        try {
            states = localeService.getState();
            ArrayAdapter<State> adapter = new ArrayAdapter<State>(getApplicationContext(), R.layout.spinner_item, states);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spnEstado.setAdapter(adapter);
            spnEstado.setOnItemSelectedListener(estadoListener);

            List<Campaign> campaigns = campaignService.getCampaigns();
            if (campaigns.size() > 0) {
                List<Stage> stages = campaignService.getStages(campaigns.get(0).getCampaingId());
                ArrayAdapter<Stage> adapter2 = new ArrayAdapter<Stage>(getApplicationContext(), R.layout.spinner_item, stages);
                adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spnEtapa.setAdapter(adapter2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class UploadTask extends AsyncTask<Boolean, Void, String> {
        private int stageId;
        private int campaignId;
        private int storeId;
        private  List<File> files;
        private Context context;
        private ProgressDialog dialog;

        public UploadTask(Context context, ProgressDialog dialog, int stageId, int campaignId, int storeId, List<File> files) {
            this.stageId = stageId;
            this.campaignId = campaignId;
            this.storeId = storeId;
            this.files = files;
            this.context = context;
            this.dialog = dialog;
        }

        @Override
        protected String doInBackground(Boolean... booleans) {
            PhotoService srvc = new PhotoService(this.context);
            try {
                String message = "";
                for (File f: files) {
                    String messagetmp = srvc.sendPhoto(stageId, campaignId, storeId, f);
                    Log.v("TaskEnvio", messagetmp);
                    if(messagetmp != "ok"){
                        message = "Ocorreu um erro";
                    }
                }
                if(message == ""){
                    message = "Sucesso!";
                }
                return message;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String message) {
            if (message != null) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Houve um erro no upload!", Toast.LENGTH_SHORT).show();
            }

            runOnUiThread(new Thread() {
                public void run() {
                    dialog.dismiss();
                    finish();
                }
            });
        }

    }

}
