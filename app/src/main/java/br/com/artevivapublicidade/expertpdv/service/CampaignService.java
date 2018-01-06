package br.com.artevivapublicidade.expertpdv.service;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.artevivapublicidade.expertpdv.connection.SIMApplication;
import br.com.artevivapublicidade.expertpdv.connection.WSConnection;
import br.com.artevivapublicidade.expertpdv.model.Campaign;
import br.com.artevivapublicidade.expertpdv.model.Stage;

/**
 * Created by Ricardo Melo on 05/01/2018.
 */

public class CampaignService {

    private Context mContext;

    public CampaignService(Context context) {
        mContext = context;
    }

    public CampaignService() {

    }

    public List<Campaign> getCampaigns() throws Exception {
        SIMApplication simApp = new SIMApplication(mContext);
        String url = "Campaign/getCampaignList/?partnerAccessCode=" + simApp.getPartnerAccessCode() + "&userAccessCode=" + new UserLogin(mContext).getUserAccessCode();
        WSConnection wsConnection = new WSConnection(url, mContext);
        wsConnection.setRequestMethod("GET");

        ArrayList<Campaign> campaigns = new ArrayList<>();
        try {
            try {
                String result = wsConnection.execute().get();
                if (result != null) {
                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray campanhasJson = jsonResult.getJSONArray("campaignList");
                    for (int i = 0; i < campanhasJson.length(); i++) {
                        JSONObject obj = campanhasJson.getJSONObject(i);
                        campaigns.add(new Campaign(obj.getInt("campaignId"), obj.getString("campaignName")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return campaigns;
    }

    public List<Stage> getStages(int campaignId) throws Exception {
        SIMApplication simApp = new SIMApplication(mContext);
        String url = "Stage/getStageListByCampaignId/?partnerAccessCode=" + simApp.getPartnerAccessCode() + "&userAccessCode=" + new UserLogin(mContext).getUserAccessCode() + "&campaignId=" + campaignId;
        WSConnection wsConnection = new WSConnection(url, mContext);
        wsConnection.setRequestMethod("GET");

        ArrayList<Stage> stages = new ArrayList<>();
        try {
            try {
                String result = wsConnection.execute().get();
                if (result != null) {
                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray etapasJson = jsonResult.getJSONArray("stageList");
                    for (int i = 0; i < etapasJson.length(); i++) {
                        JSONObject obj = etapasJson.getJSONObject(i);
                        stages.add(new Stage(obj.getInt("idEtapa"), obj.getString("tituloEtapa"), obj.getInt("idCampanha")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return stages;
    }
}
