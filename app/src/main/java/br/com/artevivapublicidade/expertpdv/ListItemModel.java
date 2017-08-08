package br.com.artevivapublicidade.expertpdv;


import java.util.ArrayList;
import java.util.List;

class ListItemModel {
    private static final ListItemModel ourInstance = new ListItemModel();
    private List<ItemModel> listItems;

    private ListItemModel() {
        listItems = new ArrayList<>();
    }

    static ListItemModel getInstance() {
        return ourInstance;
    }

    public void add(ItemModel itemModel) {
        listItems.add(itemModel);
    }

    public void remove(ItemModel itemModel) {
        for(int i = 0; i < listItems.size(); i++) {
            if(listItems.get(i).getName().equals(itemModel.getName())) {
                listItems.remove(i);
            }
        }
    }

    public void removeAll() {
        listItems.clear();
    }

    public List<ItemModel> getListItems() {
        return listItems;
    }
}
