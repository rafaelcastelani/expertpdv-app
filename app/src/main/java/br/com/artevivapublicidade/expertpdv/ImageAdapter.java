package br.com.artevivapublicidade.expertpdv;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.squareup.picasso.Picasso;



public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private int count;
    private int ids[];
    private String[] dataImages;
    private static int mImageWidth;
    private static int mImageHeight;

    //Construtor
    public ImageAdapter(int imageWidth, int imageHeight, Context context) {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.Images.Media._ID + " DESC");
        if(imageCursor != null) {
            this.count = imageCursor.getCount();

            //Cria um array de ints com o tamanho da lista
            ids = new int[this.count];
            dataImages = new String[this.count];
            for(int i = 0; i < this.count; i++) {
                imageCursor.moveToPosition(i);
                //Pega o índice da coluna ID
                int imageColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
                //Adiciona o id atual no array ids
                ids[i] = imageCursor.getInt(imageColumnIndex);
                //Pega o índice da coluna DATA
                int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);

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
        params.setMargins(4, 4, 4, 4);
        imageView.setLayoutParams(params);

        return new ViewHolder(imageView);

    }

    @Override
    //Associa as imagens de uma determinada pasta em uma View quando esta fica visível na tela
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(ids[position]));
        Picasso.with(holder.getImageView().getContext()).load(imageUri).fit().into((holder.getImageView()));
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
}
