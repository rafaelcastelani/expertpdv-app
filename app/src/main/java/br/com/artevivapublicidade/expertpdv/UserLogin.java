package br.com.artevivapublicidade.expertpdv;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UserLogin {
    private Context mContext;
    private String userAccessCode;

    public UserLogin(Context context) {
        mContext = context;
    }

    public boolean doLogin(String username, String password) throws Exception {
        SIMApplication simApp = new SIMApplication(mContext);

        WSConnection wsConnection = new WSConnection("Authentication/login/?partnerAccessCode="+simApp.getPartnerAccessCode(), mContext);
        wsConnection.setRequestMethod("POST");
        wsConnection.addField("username", username);
        wsConnection.addField("password", password);

        try {
            try {
                String result = wsConnection.execute().get();
                if(result != null) {
                    JSONObject jsonResult = new JSONObject();
                    if(jsonResult.getInt("requestStatus") == 0) {
                        //Caso dê algum erro de validação dos dados enviados...
                        throw new Exception("Erro: "+jsonResult.getString("message"));
                    } else {
                        this.setUserAccessCode(jsonResult.getString("userAccessCode"));
                        return true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void setUserAccessCode(String userAccessCode) {
        SharedPreferences sharedPref = mContext.getSharedPreferences("login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userAccessCode", userAccessCode);
        editor.commit();
    }

    public String getUserAccessCode() {
        SharedPreferences sharedPref = mContext.getSharedPreferences("login", Activity.MODE_PRIVATE);
        return sharedPref.getString("userAccess", null);
    }

}
