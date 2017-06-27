package br.com.artevivapublicidade.expertpdv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ImageGalleryActivity extends AppCompatActivity {
    private GridView grdImages;
    private Button btnSelect;

    private ImageAdapter imageAdapter;
    private int count;
    private int ids[];
    private boolean[] thumbnailSelection;
    private String[] arrPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        grdImages = (GridView) findViewById(R.id.grdImages);
        btnSelect = (Button) findViewById(R.id.btnSelect);

        //Cria um array de string com 2 colunas
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        //Cria uma string de ordenação pelo ID da imagem
        final String orderBy = MediaStore.Images.Media._ID;

        //Faz uma query passando a URI do objeto, as colunas a serem retornadas, cláusula where, argumentos, cláusula order by
        Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

        if(imageCursor != null && imageCursor.moveToFirst()) {
            //Pega o tamanho da lista
            this.count = imageCursor.getCount();
            //Cria um array de strings com o tamanho da lista
            this.arrPath = new String[this.count];
            //Cria um array de ints com o tamanho da lista
            ids = new int[this.count];
            //Cria um array para armazenar as thumbnails com o tamanho da lista
            this.thumbnailSelection = new boolean[this.count];

            for(int i = 0; i < this.count; i++) {
                imageCursor.moveToPosition(i);
                //Pega o índice da coluna ID
                int imageColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
                //Adiciona o id atual no array ids
                ids[i] = imageCursor.getInt(imageColumnIndex);
                //Pega o índice da coluna DATA
                int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                //Adiciona o data atual no array arrPath
                arrPath[i] = imageCursor.getString(dataColumnIndex);
            }

            imageAdapter = new ImageAdapter();
            grdImages.setAdapter(imageAdapter);
            imageCursor.close();

            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int len = thumbnailSelection.length;
                    int count = 0;
                    String selectImages = "";
                    for(int i = 0; i < len; i++) {
                        if(thumbnailSelection[i]) {
                            count++;
                            selectImages = selectImages + arrPath[i] + "|";
                        }
                    }
                    if(count == 0) {
                        Toast.makeText(getApplicationContext(), "Por favor, seleciona ao menos 1 foto", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("data", selectImages);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        setResult(AppCompatActivity.RESULT_CANCELED);
        super.onBackPressed();

    }

    //Este método é utilizado para setar bitmaps
    private void setBitmap(final ImageView imageView, final int id) {
        /*
         * Permite realizar operações em
         * background e publicar resultados na thread da UI sem ter que manipular threads ou
         * handlers.
         *
         * Parâmetros (os tipos de parametros enviados para a tarefa após a execução),
         *
         * Progresso (o tipo de unidade de progresso publicado durante o processamento em
         * background),
         *
         * Resultado (o tipo de resultado do processamento em background)
         */
        new AsyncTask<Void, Void, Bitmap>() {
            //Retorna a thumbnail em tamanho micro
            @Override
            protected Bitmap doInBackground(Void... params) {
                return MediaStore.Images.Thumbnails.getThumbnail(getApplicationContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
            }

            //Seta a imagem em bitmap
            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                imageView.setImageBitmap(result);
            }
        }.execute();
    }

    /**
     * List adapter
     * Cria os elementos das imagens
     */
    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.gallery_item, null);

                holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
                holder.chkImage = (CheckBox) convertView.findViewById(R.id.chkImage);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.chkImage.setId(position);
            holder.imgThumb.setId(position);
            holder.chkImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox checkbox = (CheckBox) view;
                    int id = checkbox.getId();
                    if(thumbnailSelection[id]) {
                        checkbox.setChecked(false);
                        thumbnailSelection[id] = true;
                    }
                }
            });
            holder.imgThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = holder.chkImage.getId();
                    if (thumbnailSelection[id]) {
                        holder.chkImage.setChecked(false);
                        thumbnailSelection[id] = false;
                    } else {
                        holder.chkImage.setChecked(true);
                        thumbnailSelection[id] = true;
                    }
                }
            });
            try {
                setBitmap(holder.imgThumb, ids[position]);
            } catch(Throwable e) {
                Toast.makeText(getApplicationContext(), R.string.error_select_photos,
                        Toast.LENGTH_LONG).show();
            }

            return convertView;
        }
    }

    /**
     * Inner class
     */
    private class ViewHolder {
        ImageView imgThumb;
        CheckBox chkImage;
        int id;
    }
}


