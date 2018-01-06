package br.com.artevivapublicidade.expertpdv.model;

/**
 * Created by Ricardo Melo on 05/01/2018.
 */

public class Photo {
    private int photoId;
    private String fileURL;

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public Photo() {

    }

    public Photo(int photoId, String fileURL) {
        this.photoId = photoId;
        this.fileURL = fileURL;
    }
}
