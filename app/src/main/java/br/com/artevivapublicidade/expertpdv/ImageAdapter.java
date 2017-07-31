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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.Menu;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private int count;
    private int ids[];
    private ImageView[] dataImages;
    private static int mImageWidth;
    private static int mImageHeight;
    private RelativeLayout relativeLayout;
    private RecyclerView mRecyclerView;
    private List<ItemModel> imagesList;
    private Context viewContext;

    private int totalItemsSelected = 0;

    //Construtor
    public ImageAdapter(int imageWidth, int imageHeight, Context context, RecyclerView recyclerView) {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
        mRecyclerView = recyclerView;
        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.Images.Media._ID + " DESC");

        imagesList = new ArrayList<>();
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
                imagesList.add(new ItemModel("Imagem " + i));

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
        viewContext = view.getContext();

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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ItemModel itemModel = imagesList.get(position);
        checkImage(holder.getImageView(), itemModel.isSelected());

        if(getTotalItemsSelected() > 0) {
            changeGalleryTitle(getTotalItemsSelected()+"");
        }

        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemModel.setSelected(!itemModel.isSelected());

                if(itemModel.isSelected()) {
                    addItemSelected();
                } else {
                    removeItemSelected();
                }

                checkImage(holder.getImageView(), itemModel.isSelected());

                if(getTotalItemsSelected() > 0) {
                    showMenuOptions(true);
                    changeGalleryTitle(getTotalItemsSelected() + "");
                } else {
                    showMenuOptions(false);
                    changeGalleryTitle((String)viewContext.getResources().getText(R.string.activity_image_gallery));
                }
            }
        });

        Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(ids[position]));
        Picasso.with(holder.getImageView().getContext()).load(imageUri).fit().centerCrop().into((holder.getImageView()));
    }

    @Override
    public int getItemCount() {
        return this.count;
    }

    private void addItemSelected() {
        this.totalItemsSelected++;
    }

    private void removeItemSelected() {
        this.totalItemsSelected--;
    }

    private int getTotalItemsSelected() {
        return this.totalItemsSelected;
    }

    private void checkImage(ImageView imageView, Boolean change) {
        View parentView = (View) imageView.getParent();
        CheckBox ckImage = parentView.findViewById(R.id.checkImage);
        RelativeLayout bgCheckedImage = parentView.findViewById(R.id.bgCheckedImage);
        ImageView checkedImageIcon = parentView.findViewById(R.id.checkedImageIcon);
        ckImage.setChecked(change);
        if(change) {
            bgCheckedImage.setVisibility(View.VISIBLE);
            checkedImageIcon.setVisibility(View.VISIBLE);
        } else {
            bgCheckedImage.setVisibility(View.INVISIBLE);
            checkedImageIcon.setVisibility(View.INVISIBLE);
        }
    }

    public void uncheckAllImages() {
        final int totalImagesContainer = mRecyclerView.getChildCount();
        for (int i = 0; i < totalImagesContainer; i++) {
            View v = mRecyclerView.getChildAt(i);
            ImageView imageView = v.findViewById(R.id.imageGalleryView);
            checkImage(imageView, false);
            showMenuOptions(false);
            ItemModel itemModel = imagesList.get(i);
            itemModel.setSelected(false);
        }
        changeGalleryTitle((String)viewContext.getResources().getText(R.string.activity_image_gallery));
    }

    private void changeGalleryTitle(String title) {
        if(viewContext instanceof ImageGalleryActivity) {
            ((ImageGalleryActivity) viewContext).setTitle(title);
        }
    }

    private void showMenuOptions(boolean show) {
        ImageGalleryActivity.getMenu().setGroupVisible(R.id.menu_group_select_photos, show);
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
}
