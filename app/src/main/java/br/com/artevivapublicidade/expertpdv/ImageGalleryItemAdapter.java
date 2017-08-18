package br.com.artevivapublicidade.expertpdv;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ImageGalleryItemAdapter extends RecyclerView.Adapter<ImageGalleryItemAdapter.ViewHolder> {

    private int count;
    private int ids[];
    private List<ImageView> imageViewList;
    private static int mImageWidth;
    private static int mImageHeight;
    private RelativeLayout relativeLayout;
    private RecyclerView mRecyclerView;
    private List<Item> imagesList;
    private Context viewContext;
    private List<Uri> imagesUriList;
    private Photo catalogPhotos;

    private int totalItemsSelected = 0;

    //Construtor
    public ImageGalleryItemAdapter(int imageWidth, int imageHeight, Context context, RecyclerView recyclerView) {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
        mRecyclerView = recyclerView;
        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.Images.Media._ID + " DESC");

        imagesList = new ArrayList<>();
        imagesUriList = new ArrayList<>();
        imageViewList = new ArrayList<>();
        if(imageCursor != null) {
            this.count = imageCursor.getCount();
            //Cria um array de ints com o tamanho da lista
            ids = new int[this.count];
            catalogPhotos = new Photo(context);
            for(int i = 0; i < imageCursor.getCount(); i++) {
                imageCursor.moveToPosition(i);
                //Pega o índice da coluna ID
                int imageColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
                ids[i] = imageCursor.getInt(imageColumnIndex);
                Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(ids[i]));

                Photo photo = catalogPhotos.findByPath(imageUri.getPath());

                Item item = new Item(imageUri);
                if(photo != null && photo.getStatusPhoto() == 1) {
                    Log.d("STATUS --->", photo.getStatusPhoto()+"");
                    item.setSelected(true);
                    totalItemsSelected += 1;
                }

                //Adiciona o id atual no array ids
                imagesList.add(item);
                imagesUriList.add(imageUri);
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
    }

    @Override
    //Associa as imagens na View quando esta fica visível na tela
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item item = imagesList.get(position);

        checkImage(holder.getImageView(), item.isSelected());

        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setSelected(!item.isSelected());

                if(item.isSelected()) {
                    addItemSelected(item);
                } else {
                    removeItemSelected(item);
                }

                checkImage(holder.getImageView(), item.isSelected());

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

        imageViewList.add(holder.getImageView());

        if(getTotalItemsSelected() > 0) {
            changeGalleryTitle(getTotalItemsSelected()+"");

        }
    }

    @Override
    public int getItemCount() {
        return this.count;
    }

    private void addItemSelected(Item item) {
        ListItem listItem = ListItem.getInstance();
        listItem.add(item);
        this.totalItemsSelected++;
    }

    private void removeItemSelected(Item item) {
        ListItem listItem = ListItem.getInstance();
        listItem.remove(item);
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
        for(int i = 0; i < imagesList.size(); i++) {
            Item item = imagesList.get(i);
            if(item.isSelected()) {
                removeItemSelected(item);
            }

            if(i < imageViewList.size()) {
                ImageView imageView = imageViewList.get(i);
                checkImage(imageView, false);
            }
            item.setSelected(false);
        }
        showMenuOptions(false);
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

        //Faz com que o imageView seja acessível dentro do ImageGalleryItemAdapter
        public ImageView getImageView() {
            return imageView;
        }
    }
}
