package br.com.artevivapublicidade.expertpdv.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.artevivapublicidade.expertpdv.connection.SIMApplication;
import br.com.artevivapublicidade.expertpdv.connection.WSConnection;
import br.com.artevivapublicidade.expertpdv.model.Campaign;
import br.com.artevivapublicidade.expertpdv.model.City;
import br.com.artevivapublicidade.expertpdv.model.State;
import br.com.artevivapublicidade.expertpdv.model.Store;

public class LocaleService {
    private Context mContext;

    public LocaleService(Context context) {
        mContext = context;
    }

    public LocaleService() {

    }

    public List<State> getState() throws Exception {
        SIMApplication simApp = new SIMApplication(mContext);
        String url = "Locale/getStateList/?partnerAccessCode=" + simApp.getPartnerAccessCode();
        WSConnection wsConnection = new WSConnection(url, mContext);
        wsConnection.setRequestMethod("GET");

        ArrayList<State> estados = new ArrayList<>();
        try {
            try {
                String result = wsConnection.execute().get();
                if (result != null) {
                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray estadosJson = jsonResult.getJSONArray("stateList");
                    for (int i = 0; i < estadosJson.length(); i++) {
                        JSONObject obj = estadosJson.getJSONObject(i);
                        estados.add(new State(obj.getInt("stateId"), obj.getString("stateName"), obj.getString("stateShortName"), obj.getInt("countryId")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return estados;
    }

    public List<City> getCity(int stateId) throws Exception {
        SIMApplication simApp = new SIMApplication(mContext);
        String url = "Locale/getCityList/?partnerAccessCode=" + simApp.getPartnerAccessCode() + "&stateId=" + stateId;
        WSConnection wsConnection = new WSConnection(url, mContext);
        wsConnection.setRequestMethod("GET");

        ArrayList<City> cidades = new ArrayList<>();
        try {
            try {
                String result = wsConnection.execute().get();
                if (result != null) {
                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray cidadesJson = jsonResult.getJSONArray("cityList");
                    for (int i = 0; i < cidadesJson.length(); i++) {
                        JSONObject obj = cidadesJson.getJSONObject(i);
                        cidades.add(new City(obj.getInt("cityId"), obj.getInt("stateId"), obj.getString("cityName")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return cidades;
    }

    public List<Store> getStore(int cityId) throws Exception {
        SIMApplication simApp = new SIMApplication(mContext);
        String url = "Store/getStoreList/?partnerAccessCode=" + simApp.getPartnerAccessCode()+ "&userAccessCode="+ new UserLogin(mContext).getUserAccessCode() + "&idCity=" + cityId;
        WSConnection wsConnection = new WSConnection(url, mContext);
        wsConnection.setRequestMethod("GET");

        ArrayList<Store> stores = new ArrayList<>();
        try {
            try {
                String result = wsConnection.execute().get();
                if (result != null) {
                    JSONObject jsonResult = new JSONObject(result);
                    JSONArray lojasJson = jsonResult.getJSONArray("storeList");
                    for (int i = 0; i < lojasJson.length(); i++) {
                        JSONObject obj = lojasJson.getJSONObject(i);
                        stores.add(new Store(obj.getInt("idLoja"), obj.getString("nomeLoja"), obj.getString("enderecoLoja"), obj.getInt("idCidadeLoja"), obj.getString("cnpjLoja"), obj.getInt("idCategoriaPdv")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return stores;
    }



}
