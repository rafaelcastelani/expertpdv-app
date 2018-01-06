package br.com.artevivapublicidade.expertpdv.model;

/**
 * Created by Ricardo Melo on 05/01/2018.
 */

public class Campaign {
    private int campaingId;
    private String campaignName;

    public int getCampaingId() {
        return campaingId;
    }

    public void setCampaingId(int campaingId) {
        this.campaingId = campaingId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Campaign(int campaingId, String campaignName) {

        this.campaingId = campaingId;
        this.campaignName = campaignName;
    }

    public Campaign() {

    }
}
