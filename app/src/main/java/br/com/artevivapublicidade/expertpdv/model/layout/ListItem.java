package br.com.artevivapublicidade.expertpdv.model.layout;


import java.util.ArrayList;
import java.util.List;

public class ListItem {
    private static final ListItem ourInstance = new ListItem();
    private List<Item> listItems;

    private ListItem() {
        listItems = new ArrayList<>();
    }

    public  static ListItem getInstance() {
        return ourInstance;
    }

    public void add(Item item) {
        listItems.add(item);
    }

    public void remove(Item item) {
        for(int i = 0; i < listItems.size(); i++) {
            if(listItems.get(i).getName().equals(item.getName())) {
                listItems.remove(i);
            }
        }
    }

    public void removeAll() {
        listItems.clear();
    }

    public List<Item> getListItems() {
        return listItems;
    }
}
