package br.com.artevivapublicidade.expertpdv;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by vinicius on 29/06/2017.
 */

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
    WeakReference<ImageView> imageViewReferences;
    private Resources mImageResources;
    private ContentResolver mContentResolver;
    private BitmapFactory.Options mOptions;
    private int mPosition;
    private Drawable mImageDrawable;

    public BitmapWorkerTask(ImageView imageView, int imageW, int imageH, ContentResolver contentResolver) {
        imageViewReferences = new WeakReference<ImageView>(imageView);
        mContentResolver = contentResolver;
        mOptions = new BitmapFactory.Options();
        mOptions.outWidth = imageW;
        mOptions.outHeight = imageH;
        mPosition = imageView.getId();
        mImageResources = imageView.getResources();
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        Bitmap imageBitmap = MediaStore.Images.Thumbnails.getThumbnail(mContentResolver, params[0], MediaStore.Images.Thumbnails.MINI_KIND, mOptions);
        mImageDrawable = new BitmapDrawable(mImageResources, imageBitmap);
        return imageBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        /*if(bitmap != null && imageViewReferences != null && imageViewReferences.get() != null && mPosition == imageViewReferences.get().getId()) {
            ImageView viewImage = imageViewReferences.get();
            viewImage.setImageBitmap(bitmap);
        }*/
        if(isCancelled()) {
            bitmap = null;
        }
        if(bitmap != null && imageViewReferences != null) {
            ImageView imageView = imageViewReferences.get();
            BitmapWorkerTask bitmapWorkerTask = ImageAdapter.getBitmapWorkerTask(imageView);
            if(this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public Drawable getImageDrawable() {
        return mImageDrawable;
    }
}
