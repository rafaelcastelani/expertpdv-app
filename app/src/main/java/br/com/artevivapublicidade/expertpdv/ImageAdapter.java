package br.com.artevivapublicidade.expertpdv;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;



public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private int count;
    private int ids[];
    private ImageView[] dataImages;
    private static int mImageWidth;
    private static int mImageHeight;
    private RelativeLayout relativeLayout;
    private RecyclerView mRecyclerView;
    private final View.OnClickListener mOnCLickListener = new OnItemClickListener();

    //Construtor
    public ImageAdapter(int imageWidth, int imageHeight, Context context, RecyclerView recyclerView) {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
        mRecyclerView = recyclerView;
        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.Images.Media._ID + " DESC");
        if(imageCursor != null) {
            this.count = imageCursor.getCount();

            //Cria um array de ints com o tamanho da lista
            ids = new int[this.count];
            dataImages = new ImageView[this.count];
            for(int i = 0; i < this.count; i++) {
                imageCursor.moveToPosition(i);
                //Pega o índice da coluna ID
                int imageColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
                //Adiciona o id atual no array ids
                ids[i] = imageCursor.getInt(imageColumnIndex);
            }
            imageCursor.close();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_items, parent, false);

        relativeLayout = view.findViewById(R.id.imageContainer);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mImageWidth, mImageHeight);
        relativeLayout.setLayoutParams(params);

        relativeLayout.setOnClickListener(mOnCLickListener);

        CheckBox ckImage = view.findViewById(R.id.checkImage);
        ckImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                RelativeLayout relativeLayout =(RelativeLayout) (ViewGroup) compoundButton.getParent();
                ImageView imageView = relativeLayout.findViewById(R.id.imageGalleryView);
                changeImageBorder(imageView, compoundButton.isChecked());
            }
        });

        //Retorna a view como parte do ViewHolder
        return new ViewHolder(view);

        /*ImageView imageView = new ImageView(parent.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mImageWidth, mImageHeight);
        params.setMargins(4, 4, 4, 4);
        imageView.setLayoutParams(params);

        return new ViewHolder(imageView);*/

    }

    @Override
    //Associa as imagens de uma determinada pasta em uma View quando esta fica visível na tela
    public void onBindViewHolder(ViewHolder holder, int position) {
        dataImages[position] = holder.getImageView();
        Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(ids[position]));
        Picasso.with(holder.getImageView().getContext()).load(imageUri).fit().centerCrop().into((holder.getImageView()));
    }

    @Override
    public int getItemCount() {
        return this.count;
    }


    private void checkImage(View view) {
        CheckBox ckImage = view.findViewById(R.id.checkImage);
        ImageView imageView = view.findViewById(R.id.imageGalleryView);
        if(ckImage.isChecked()) {
            changeImageBorder(imageView, false);
            ckImage.setChecked(false);
            ckImage.setVisibility(View.INVISIBLE);
        } else {
            changeImageBorder(imageView, true);
            ckImage.setChecked(true);
            ckImage.setVisibility(View.VISIBLE);
        }
    }

    private void changeImageBorder(ImageView imageView, Boolean change) {
        ThemeHelper themeHelper = new ThemeHelper();
        if(change) {
            imageView.setPadding(3, 3, 3, 3);
            imageView.setBackgroundColor(themeHelper.getPrimaryColor(imageView.getContext()));
        } else {
            imageView.setPadding(0, 0, 0, 0);
            imageView.setBackgroundColor(Color.parseColor("#000000"));
        }
    }

    //Associa views com o ViewHolder do RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        //Construtor
        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageGalleryView);

            //imageView = (ImageView) view;
        }

        //Faz com que o imageView seja acessível dentro do ImageAdapter
        public ImageView getImageView() {
            return imageView;
        }
    }

    private class OnItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = mRecyclerView.getChildLayoutPosition(view);
            ImageView item = dataImages[itemPosition];
            ImageView imageView = view.findViewById(R.id.imageGalleryView);
            if(imageView.getDrawable() == item.getDrawable()) {
                changeImageBorder(imageView, true);
            }
        }
    }
}
