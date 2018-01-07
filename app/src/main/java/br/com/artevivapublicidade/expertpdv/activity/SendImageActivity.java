package br.com.artevivapublicidade.expertpdv.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.artevivapublicidade.expertpdv.R;
import br.com.artevivapublicidade.expertpdv.model.Campaign;
import br.com.artevivapublicidade.expertpdv.model.City;
import br.com.artevivapublicidade.expertpdv.model.Stage;
import br.com.artevivapublicidade.expertpdv.model.State;
import br.com.artevivapublicidade.expertpdv.model.Store;
import br.com.artevivapublicidade.expertpdv.service.CampaignService;
import br.com.artevivapublicidade.expertpdv.service.LocaleService;
import br.com.artevivapublicidade.expertpdv.service.PhotoService;

public class SendImageActivity extends AppCompatActivity {
    private ViewFlipper flipper;
    private Spinner spnEstado;
    private Spinner spnCidade;
    private AutoCompleteTextView txtLoja;
    private Spinner spnEtapa;
    private AdapterView.OnItemSelectedListener cidadeListener;
    private Button btnEnviar;

    private LocaleService localeService;
    private CampaignService campaignService;
    private List<City> cities;
    private List<Store> stores;
    private Store storeSelected;

    private ArrayList<String> filePaths;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_image);

        localeService = new LocaleService(this);
        campaignService = new CampaignService(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.filePaths = extras.getStringArrayList("picture");
            spnEstado = (Spinner) findViewById(R.id.spnEstado);
            spnCidade = (Spinner) findViewById(R.id.spnCidade);
            spnEtapa = (Spinner) findViewById(R.id.spnEtapa);
            btnEnviar = (Button) findViewById(R.id.btnEnviar);
            flipper = (ViewFlipper) findViewById(R.id.flipper);
            txtLoja = (AutoCompleteTextView) findViewById(R.id.autoTxtLoja);

            initAutoComplete();
            initSpinners();
            initButton();
            initFlipper();
        }
    }

    private void initAutoComplete(){
        txtLoja.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                SendImageActivity.this.storeSelected = (Store) parent.getItemAtPosition(pos);
            }
        });

        txtLoja.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SendImageActivity.this.storeSelected = null;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
                boolean erro = false;

                if (SendImageActivity.this.storeSelected == null) {
                    Toast.makeText(getApplicationContext(), "Selecione uma loja", Toast.LENGTH_SHORT).show();
                    erro = true;
                }

                if (!erro && spnEtapa.getSelectedItemPosition() < 0) {
                    Toast.makeText(getApplicationContext(), "Selecione uma etapa", Toast.LENGTH_SHORT).show();
                    erro = true;
                }

                if (!erro) {
                    Stage stage = (Stage) spnEtapa.getSelectedItem();
                    ArrayList<File> files = new ArrayList<>();
                    for (int i = 0; i < filePaths.size(); i++) {
                        files.add(new File(filePaths.get(i)));
                    }

                    final ProgressDialog progressDialog = new ProgressDialog(SendImageActivity.this);
                    progressDialog.setMessage("Enviando...");
                    progressDialog.show();
                    UploadTask task = new UploadTask(getApplicationContext(), progressDialog, stage.getStageId(), stage.getCampaignId(), SendImageActivity.this.storeSelected.getStoreId(), files);
                    task.execute();
                }
            }
        });
    }

    private void initSpinners() {
        cidadeListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                try {
                    City state = (City) parent.getItemAtPosition(position);
                    stores = localeService.getStore(state.getCityId());

                    ArrayAdapter<Store> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.autocomplete_item, stores);
//                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    txtLoja.setAdapter(adapter);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                stores.clear();
                storeSelected = null;
                ArrayAdapter<Store> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.autocomplete_item, stores);
//                adapter.setDropDownViewResource(R.layout.spinner_item);
                txtLoja.setAdapter(adapter);
            }
        };

        try {
            List<State> states = localeService.getState();
            ArrayAdapter<State> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, states);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spnEstado.setAdapter(adapter);
            spnEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                    try {
                        State state = (State) parent.getItemAtPosition(position);
                        cities = localeService.getCity(state.getStateId());
                        ArrayAdapter<City> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, cities);
                        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        spnCidade.setAdapter(adapter);
                        spnCidade.setOnItemSelectedListener(cidadeListener);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    cities.clear();
                    ArrayAdapter<City> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, cities);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spnCidade.setAdapter(adapter);
                    spnCidade.setOnItemSelectedListener(cidadeListener);

                    stores.clear();
                    storeSelected = null;
                    ArrayAdapter<Store> adapter2 = new ArrayAdapter<>(getApplicationContext(), R.layout.autocomplete_item, stores);
                    txtLoja.setAdapter(adapter2);
                }
            });

            List<Campaign> campaigns = campaignService.getCampaigns();
            if (campaigns.size() > 0) {
                List<Stage> stages = campaignService.getStages(campaigns.get(0).getCampaingId());
                ArrayAdapter<Stage> adapter2 = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, stages);
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
        private List<File> files;
        private Context context;
        private ProgressDialog dialog;

        private UploadTask(Context context, ProgressDialog dialog, int stageId, int campaignId, int storeId, List<File> files) {
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
                for (File f : files) {
                    String messagetmp = srvc.sendPhoto(stageId, campaignId, storeId, f);
                    Log.v("TaskEnvio", messagetmp);
                    if (!messagetmp.equals("ok")) {
                        message = "Ocorreu um erro";
                    }
                }
                if (message.isEmpty()) {
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
                Toast.makeText(getApplicationContext(), "Houve um erro no upload!", Toast.LENGTH_LONG).show();
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
