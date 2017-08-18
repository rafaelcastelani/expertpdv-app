package br.com.artevivapublicidade.expertpdv;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WSConnection extends AsyncTask<Boolean, Void, String> {
    private static String WS_URL = "http://sivemapi.artevivapublicidade.com.br/v1/";

    private String scopeAction;
    private String requestMethod;
    private Uri.Builder listFields;
    private Context context;

    public WSConnection(String scopeAction, Context context) {
        this.listFields = new Uri.Builder();
        this.scopeAction = scopeAction;
        this.context = context;
    }

    @Override
    protected String doInBackground(Boolean... isSandbox) {
        //Adiciona ou não a sandbox, como padrão está true.
        Boolean boolIsSandbox = true;
        if(isSandbox.length > 0) {
            boolIsSandbox = isSandbox[0];
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            String strUrl = WS_URL+scopeAction;
            if(boolIsSandbox) {
                strUrl += "?sandbox=true";
            }

            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(getRequestMethod());
            //Timeout durante 20 segundos
            urlConnection.setConnectTimeout(20000);

            String query = getQueryString();
            if(query != null) {
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            }

            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            StringBuffer buffer = new StringBuffer();
            while((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }
            return buffer.toString();
        } catch(Exception e) {
            e.printStackTrace();
            Handler handler =  new Handler(context.getMainLooper());
            handler.post( new Runnable(){
                public void run(){
                    Toast.makeText(context, "Erro: sem conexão com a internet",Toast.LENGTH_LONG).show();
                }
            });
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void onPostExecute(String data) {

    }

    public void setRequestMethod(String method) {
        this.requestMethod = method;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    public void addField(String key, String value) {
        listFields.appendQueryParameter(key, value);
    }

    public void addField(String key, int value) {
        listFields.appendQueryParameter(key, value+"");
    }

    public Uri.Builder getFields() {
        return this.listFields;
    }

    public String getQueryString() {
        return this.getFields().build().getEncodedQuery();
    }
}
