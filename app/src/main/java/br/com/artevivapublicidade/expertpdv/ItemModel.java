package br.com.artevivapublicidade.expertpdv;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItemModel {
    private Uri uri;
    private boolean isSelected = false;
    private List<ItemModel> selectedItems;

    public ItemModel(Uri uri) {
        this.selectedItems = new ArrayList<>();
        this.uri = uri;
    }

    public Uri getUri() {
        return this.uri;
    }

    public String getPath() {
        return this.uri.getPath();
    }

    public String getName() {
        File imageFile = new File(getPath());

        return imageFile.getName();
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
