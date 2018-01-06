package br.com.artevivapublicidade.expertpdv.model;

/**
 * Created by Ricardo Melo on 05/01/2018.
 */

public class Store {
    private int storeId;
    private String storeName;
    private String storeAddress;
    private int storeCityId;
    private String storeCnpj;
    private  int checkoutId;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public int getStoreCityId() {
        return storeCityId;
    }

    public void setStoreCityId(int storeCityId) {
        this.storeCityId = storeCityId;
    }

    public String getStoreCnpj() {
        return storeCnpj;
    }

    public void setStoreCnpj(String storeCnpj) {
        this.storeCnpj = storeCnpj;
    }

    public int getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(int checkoutId) {
        this.checkoutId = checkoutId;
    }

    public Store(int storeId, String storeName, String storeAddress, int storeCityId, String storeCnpj, int checkoutId) {

        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeCityId = storeCityId;
        this.storeCnpj = storeCnpj;
        this.checkoutId = checkoutId;
    }

    public Store() {

    }

    @Override
    public String toString() {
        return storeName;
    }
}
