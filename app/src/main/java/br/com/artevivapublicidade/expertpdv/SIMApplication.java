package br.com.artevivapublicidade.expertpdv;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class SIMApplication {
    private static String PARTNER_TOKEN_API = "9a39aec3b40db4d58015b31705f5b2152d167dad";
    private static String PARTNER_USERNAME = "app_facaaco";
    private static String PARTNER_PASSWORD = "be4c9584bea433726eecf430ddb0cccc";
    private static int SIM_ID = 1;
    private Context mContext;

    public SIMApplication(Context context) {
        mContext = context;
    }

    private String generatePartnerAccessCode() {
        WSConnection wsConnection = new WSConnection("Authentication/getPartnerAccessCode/", mContext);
        wsConnection.setRequestMethod("POST");
        wsConnection.addField("partnerTokenApi", PARTNER_TOKEN_API);
        wsConnection.addField("partnerUsername", PARTNER_USERNAME);
        wsConnection.addField("partnerPassword", PARTNER_PASSWORD);
        wsConnection.addField("simId", SIM_ID);
        try {
            try {
                String result = wsConnection.execute().get();
                if(result != null) {
                    JSONObject jsonResult = new JSONObject();
                    if(jsonResult.getInt("requestStatus") == 0) {
                        //Caso dê algum erro de validação dos dados enviados...
                        Toast.makeText(mContext, "Erro: "+jsonResult.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        return jsonResult.getString("partnerAccessCode");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPartnerAccessCode(String partnerAccessCode) {
        SharedPreferences sharedPref = mContext.getSharedPreferences("partnerAccessCode", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("partnerAccessCode", partnerAccessCode);
        editor.commit();
    }

    public String getPartnerAccessCode() {
        SharedPreferences sharedPref = mContext.getSharedPreferences("partnerAccessCode", Activity.MODE_PRIVATE);
        String partnerAccessCode = sharedPref.getString("partnerAccessCode", null);

        if(partnerAccessCode == null) {
            this.setPartnerAccessCode(this.generatePartnerAccessCode());
        }
        return partnerAccessCode;
    }
}
