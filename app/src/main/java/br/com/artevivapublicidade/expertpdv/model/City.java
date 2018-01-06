package br.com.artevivapublicidade.expertpdv.model;

/**
 * Created by Ricardo Melo on 05/01/2018.
 */

public class City {
    private int cityId;
    private int stateId;
    private String cityName;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public City() {

    }

    public City(int cityId, int stateId, String cityName) {

        this.cityId = cityId;
        this.stateId = stateId;
        this.cityName = cityName;
    }


    @Override
    public String toString() {
        return this.cityName;
    }
}
