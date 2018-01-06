package br.com.artevivapublicidade.expertpdv.model;

/**
 * Created by Ricardo Melo on 05/01/2018.
 */

public class State {
    private int stateId;
    private String stateName;
    private String stateShortName;
    private int countryId;

    public State() {
    }

    public State(int stateId, String stateName, String stateShortName, int countryId) {
        this.stateId = stateId;
        this.stateName = stateName;
        this.stateShortName = stateShortName;
        this.countryId = countryId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateShortName() {
        return stateShortName;
    }

    public void setStateShortName(String stateShortName) {
        this.stateShortName = stateShortName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return this.stateName;
    }
}
