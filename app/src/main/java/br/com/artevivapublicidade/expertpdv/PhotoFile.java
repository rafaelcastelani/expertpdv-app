package br.com.artevivapublicidade.expertpdv;

import android.net.Uri;

import java.io.File;

/**
 * Created by vinicius on 07/08/2017.
 */

public class PhotoFile {
    private static final PhotoFile ourInstance = new PhotoFile();
    private File photoFile;
    public static PhotoFile getInstance() {
        return ourInstance;
    }


    private PhotoFile() {
    }

    public void setFile(File photoFile) {
        this.photoFile = photoFile;
    }

    public File getFile() {
        return this.photoFile;
    }
}
