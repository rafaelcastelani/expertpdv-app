package br.com.artevivapublicidade.expertpdv.model;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import br.com.artevivapublicidade.expertpdv.connection.SIMApplication;
import br.com.artevivapublicidade.expertpdv.connection.WSConnection;

public class User {
    private Context mContext;

    private String fullName;
    private String login;
    private String doc;
    private String phoneNumber;
    private String address;
    private String email;
    private String lastAccess;

    public User(Context context) {
        this.mContext = context;
    }
    public User findUserByUserAccessCode(String userAccessCode) throws Exception {
        SIMApplication simApp = new SIMApplication(mContext);

        WSConnection wsConnection = new WSConnection("User/getUserData/", mContext);
        wsConnection.setRequestMethod("GET");
        wsConnection.addField("partnerAccessCode", simApp.getPartnerAccessCode());
        wsConnection.addField("userAccessCode", userAccessCode);

        try {
            try {
                String result = wsConnection.execute().get();
                if(result != null) {
                    JSONObject jsonResult = new JSONObject();
                    if(jsonResult.getInt("requestStatus") == 0) {
                        //Caso dê algum erro de validação dos dados enviados...
                        throw new Exception("Erro: "+jsonResult.getString("message"));
                    } else {
                        this.setFullName(jsonResult.getString("fullName"));
                        this.setLogin(jsonResult.getString("login"));
                        this.setDoc(jsonResult.getString("doc"));
                        this.setPhoneNumber(jsonResult.getString("phoneNumber"));
                        this.setAddress(jsonResult.getString("address"));
                        this.setEmail(jsonResult.getString("email"));
                        this.setLastAccess(jsonResult.getString("lastAccess"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(String lastAccess) {
        this.lastAccess = lastAccess;
    }
}
