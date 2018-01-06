package br.com.artevivapublicidade.expertpdv.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.artevivapublicidade.expertpdv.connection.FileUpload;
import br.com.artevivapublicidade.expertpdv.connection.SIMApplication;
import br.com.artevivapublicidade.expertpdv.connection.WSConnection;
import br.com.artevivapublicidade.expertpdv.model.Campaign;
import br.com.artevivapublicidade.expertpdv.model.Photo;

/**
 * Created by Ricardo Melo on 05/01/2018.
 */

public class PhotoService {

    private Context mContext;

    public PhotoService(Context context) {
        mContext = context;
    }

    public PhotoService() {

    }

    public List<Photo> getPhotos(int stageId, int campaignId) throws Exception {
        SIMApplication simApp = new SIMApplication(mContext);
        String url = "Photo/getSentPhotos/?partnerAccessCode=" + simApp.getPartnerAccessCode() + "&userAccessCode=" + new UserLogin(mContext).getUserAccessCode() + "&campaignId=" + campaignId + "&stageId=" + stageId;
        WSConnection wsConnection = new WSConnection(url, mContext);
        wsConnection.setRequestMethod("GET");

        ArrayList<Photo> fotos = new ArrayList<>();
        try {
            try {
                String result = wsConnection.execute().get();
                if (result != null) {
                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray fotosJson = jsonResult.getJSONArray("photoList");
                    for (int i = 0; i < fotosJson.length(); i++) {
                        JSONObject obj = fotosJson.getJSONObject(i);
                        fotos.add(new Photo(obj.getInt("photoId"), obj.getString("FileURL")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return fotos;
    }

    public String sendPhoto(int stageId, int campaignId, int storeId, File file) throws Exception {
        SIMApplication simApp = new SIMApplication(mContext);
        String url = "Photo/sendPhoto/?partnerAccessCode=" + simApp.getPartnerAccessCode() + "&userAccessCode=" + new UserLogin(mContext).getUserAccessCode();
        FileUpload upload = new FileUpload(url, mContext);
        upload.addField("campaignId", campaignId);
        upload.addField("arrayStageId", "[" + stageId + "]");
        upload.addField("storeId", storeId);
        upload.setFile(file);

        try {
            Log.v("Service", "Chamar envio");
            String result = upload.doUpload();
            Log.v("Service", "Envio realizado");
            if (result != null) {
                JSONObject jsonResult = new JSONObject(result);
                if (jsonResult.has("photoId")) {
                    return "ok";
                } else {
                    if (jsonResult.has("message")) {
                        return jsonResult.getString("message");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
