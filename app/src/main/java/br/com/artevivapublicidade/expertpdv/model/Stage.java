package br.com.artevivapublicidade.expertpdv.model;

/**
 * Created by Ricardo Melo on 05/01/2018.
 */

public class Stage {
    private int stageId;
    private String stageTitle;
    private int campaignId;

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public String getStageTitle() {
        return stageTitle;
    }

    public void setStageTitle(String stageTitle) {
        this.stageTitle = stageTitle;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public Stage(int stageId, String stageTitle, int campaignId) {
        this.stageId = stageId;
        this.stageTitle = stageTitle;
        this.campaignId = campaignId;

    }

    public Stage() {

    }

    @Override
    public String toString() {
        return stageTitle;
    }
}
