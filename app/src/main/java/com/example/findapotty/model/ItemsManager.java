package com.example.findapotty.model;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemsManager<T extends Item> {

    private HashMap<String, T> items = new HashMap<>();
    private ArrayList<T> itemList = new ArrayList<>();

    public HashMap<String, T> getItems() {
        return items;
    }

    public ArrayList<T> getItemsList() {
        return itemList;
    }

    public T getItemByIndex(int index) {
        return itemList.get(index);
    }

    public void setItems(HashMap<String, T> retrievedItems) {
        if (retrievedItems != null) {
            items = retrievedItems;
            retrievedItems.forEach((id, item) -> {
                itemList.add(0, item);
                item.setId(id);
            });
        }
    }

    public void addItem(int index, T item) {
        items.put(item.getId(), item);
        itemList.add(index, item);
    }

    public void removeItem(String id) {
        itemList.remove(items.get(id));
        items.remove(id);
    }

    public void clearItems() {
        items.clear();
        itemList.clear();
    }

    public int getCount() {
        return itemList.size();
    }

}
