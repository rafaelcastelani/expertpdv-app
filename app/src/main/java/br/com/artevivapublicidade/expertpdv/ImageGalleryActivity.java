package br.com.artevivapublicidade.expertpdv;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class ImageGalleryActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public static int mColumnCount = 3;
    public static int mImageWidth;
    public static int mImageHeight;
    private static Menu toolbarMenu;
    private RecyclerView.Adapter imageAdapter;

    //Necessário para habilitar exibição de imagens em vetor (no caso o ícone de "check" na seleção de foto)
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        //Pega o width do display para casar com o tamanho das colunas do grid
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mImageWidth = displayMetrics.widthPixels / mColumnCount;
        //Ratio das imagens que são tiradas pela câmera (em portrait mode);
        mImageHeight = mImageWidth * 4 / 3;

        mRecyclerView = (RecyclerView) findViewById(R.id.galleryRecyclerView);
        //Cria um GridLayout com 1 coluna
        GridLayoutManager layoutManager = new GridLayoutManager(this, mColumnCount);
        //Associa este layout ao RecyclerView
        mRecyclerView.setLayoutManager(layoutManager);
        //Cria o objeto do layout e associa ao RecyclerVIew
        imageAdapter = new ImageGalleryItemAdapter(mImageWidth, mImageHeight, getApplicationContext(), mRecyclerView);
        mRecyclerView.setAdapter(imageAdapter);
    }


    @Override
    public void onBackPressed() {
        setResult(AppCompatActivity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.deselect_photos:
                ((ImageGalleryItemAdapter)imageAdapter).uncheckAllImages();
                break;
            case R.id.select_photos:
                ListItemModel listItemModel = ListItemModel.getInstance();

                Photo catalogPhoto = new Photo(getApplicationContext());
                List<Photo> listAllPhotos = catalogPhoto.findAll();
                catalogPhoto.delete(null, null);

                List<String> photoFileList = new ArrayList<>();
                List<String> itemModelNameList = new ArrayList<>();

                for(Photo photo: listAllPhotos) {
                    photoFileList.add(photo.getFilePhoto());
                }

                if(listItemModel.getListItems().size() > 0) {
                    //Salva os itens no banco de dados
                    for(ItemModel itemModel: listItemModel.getListItems()) {
                        itemModelNameList.add(itemModel.getName());
                        Photo photo = catalogPhoto.findByName(itemModel.getName());
                        if(photo == null) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("pathPhoto", itemModel.getPath());
                            contentValues.put("filePhoto", itemModel.getName());
                            catalogPhoto.insert(null, contentValues);
                        }
                    }
                }

                List<String> diffPhotoFileList = new ArrayList<>(photoFileList);
                diffPhotoFileList.removeAll(itemModelNameList);

                //Deleta os itens do banco que não estão mais selecionados
                catalogPhoto.delete("filePhoto IN (?)", new String[]{TextUtils.join(",", diffPhotoFileList)});

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Photo catalogPhoto = new Photo(getApplicationContext());
        List<Photo> listPhotos = catalogPhoto.findAll();
        boolean visibility = false;

        if(listPhotos.size() > 0) {
            visibility = true;
        }

        toolbarMenu = menu;
        menu.setGroupVisible(R.id.menu_group_select_photos, visibility);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        RecyclerView.Adapter newImageAdapter = new ImageGalleryItemAdapter(mImageWidth, mImageHeight, getApplicationContext(), mRecyclerView);
        mRecyclerView.swapAdapter(newImageAdapter, false);

    }

    public static Menu getMenu() {
        return toolbarMenu;
    }
}
