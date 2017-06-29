package br.com.artevivapublicidade.expertpdv;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.lang.ref.WeakReference;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private int count;
    private int ids[];
    private String[] dataImages;
    private static int mImageWidth;
    private static int mImageHeight;
    private Bitmap placeholderBitmap;

    public static class AsyncDrawable extends BitmapDrawable {
        final WeakReference<BitmapWorkerTask> taskReference;

        public AsyncDrawable(Resources resources, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(resources, bitmap);
            taskReference = new WeakReference(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return taskReference.get();
        }
    }

    //Construtor
    public ImageAdapter(Uri uriFiles, int imageWidth, int imageHeight, Context context) {
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.Media._ID + " DESC");
        if(imageCursor != null && imageCursor.moveToFirst()) {
            this.count = imageCursor.getCount();

            //Cria um array de ints com o tamanho da lista
            ids = new int[this.count];
            for(int i = 0; i < this.count; i++) {
                imageCursor.moveToPosition(i);
                //Pega o índice da coluna ID
                int imageColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
                //Adiciona o id atual no array ids
                ids[i] = imageCursor.getInt(imageColumnIndex);
                dataImages[i] = MediaStore.Images.Media.DATA;

            }
            imageCursor.close();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Cria a instância do ViewHolder e passa para a RecyclerView
/*        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_items, parent, false);
        //Retorna a view como parte do ViewHolder
        return new ViewHolder(view);*/

        ImageView imageView = new ImageView(parent.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mImageWidth, mImageHeight);
        imageView.setLayoutParams(params);

        return new ViewHolder(imageView);

    }

    @Override
    //Associa as imagens de uma determinada pasta em uma View quando esta fica visível na tela
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Pega o arquivo passando a posição dele
        ContentResolver contentResolver = holder.getImageView().getContext().getContentResolver();

/*        BitmapWorkerTask workerTask = new BitmapWorkerTask(holder.getImageView(), mImageWidth, mImageHeight, contentResolver);
        workerTask.execute(ids[position]);*/
        //holder.getImageView().setImageBitmap(imageBitmap);

        if(checkBitmapWorkerTask(imageFile, holder.getImageView())) {
            BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(holder.getImageView());
            AsyncDrawable asyncDrawable = new AsyncDrawable(holder.getImageView().getResources(),
                    placeholderBitmap,
                    bitmapWorkerTask);
            holder.getImageView().setImageDrawable(asyncDrawable);
            bitmapWorkerTask.execute(imageFile);
        }
    }

    @Override
    public int getItemCount() {
        return this.count;
    }

    //Associa views com o ViewHolder do RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        //Construtor
        public ViewHolder(View view) {
            super(view);
            //imageView = view.findViewById(R.id.imageGalleryView);
            imageView = (ImageView) view;
        }

        //Faz com que o imageView seja acessível dentro do ImageAdapter
        public ImageView getImageView() {
            return imageView;
        }
    }

    public static boolean checkBitmapWorkerTask(String dataImage, ImageView imageView) {
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if(bitmapWorkerTask != null) {
            final int workerFile = bitmapWorkerTask.getImagePosition();
            if(workerFile != null) {
                bitmapWorkerTask.cancel(true);
            } else {
                // bitmap worker task file é o mesmo que a imageview está esperando
                //então não faz nada
                return false;
            }
        }
        return true;
    }

    public static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if(drawable instanceof AsyncDrawable) {
            AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
            return asyncDrawable.getBitmapWorkerTask();
        }
        return null;
    }
}
