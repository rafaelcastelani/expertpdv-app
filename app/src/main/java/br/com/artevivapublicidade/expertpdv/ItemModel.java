package br.com.artevivapublicidade.expertpdv;


public class ItemModel {
    private String text;
    private boolean isSelected = false;

    public ItemModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
