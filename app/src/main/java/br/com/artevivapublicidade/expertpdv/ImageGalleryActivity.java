package br.com.artevivapublicidade.expertpdv;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


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
        imageAdapter = new ImageAdapter(mImageWidth, mImageHeight, getApplicationContext(), mRecyclerView);
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
                ((ImageAdapter)imageAdapter).uncheckAllImages();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        toolbarMenu = menu;
        menu.setGroupVisible(R.id.menu_group_select_photos, false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        RecyclerView.Adapter newImageAdapter = new ImageAdapter(mImageWidth, mImageHeight, getApplicationContext(), mRecyclerView);
        mRecyclerView.swapAdapter(newImageAdapter, false);

    }

    public static Menu getMenu() {
        return toolbarMenu;
    }
}
