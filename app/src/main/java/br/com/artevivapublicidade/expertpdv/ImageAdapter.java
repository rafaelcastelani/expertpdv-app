package br.com.artevivapublicidade.expertpdv;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Uri imagesFile;
    private InputStream input;
    private int count;
    private int ids[];

    //Construtor
    public ImageAdapter(Uri folderFile, Context context) {
        imagesFile = folderFile;

        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
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
                //Pega o índice da coluna DATA

            }
            imageCursor.close();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Cria a instância do ViewHolder e passa para a RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_items, parent, false);
        //Retorna a view como parte do ViewHolder
        return new ViewHolder(view);
    }

    @Override
    //Associa as imagens de uma determinada pasta em uma View quando esta fica visível na tela
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Pega o arquivo passando a posição dele
        ContentResolver contentResolver = holder.getImageView().getContext().getContentResolver();
        Bitmap imageBitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, ids[position], MediaStore.Images.Thumbnails.MINI_KIND, null);
        holder.getImageView().setImageBitmap(imageBitmap);
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
            imageView = view.findViewById(R.id.imageGalleryView);
        }

        //Faz com que o imageView seja acessível dentro do ImageAdapter
        public ImageView getImageView() {
            return imageView;
        }
    }


}
