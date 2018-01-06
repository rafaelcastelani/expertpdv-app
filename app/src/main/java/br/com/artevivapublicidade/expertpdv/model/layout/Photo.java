package br.com.artevivapublicidade.expertpdv.model.layout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.artevivapublicidade.expertpdv.connection.DB;

public class Photo extends DB {
    private static String TABLE_NAME = "photo";
    private static String PRIMARY_KEY = "_id";
    private int _id;
    private String pathPhoto;
    private String filePhoto;
    /**
     * 1: selected
     * 2: sended
     */
    private int statusPhoto;

    private Context context;

    public Photo(Context context) {
        super(context);
        this.context = context;
    }

    public List<Photo> find(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy, String limit) throws Exception {

        try {
            String[] columns = new String[]{"_id", "pathPhoto", "filePhoto", "statusPhoto"};
            Cursor cursor = select(TABLE_NAME, columns, whereClause, whereArgs, groupBy, having, orderBy, limit);
            List<Photo> photoList = new ArrayList<>();

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Photo photo = new Photo(context);
                    photo.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    photo.setPathPhoto(cursor.getString(cursor.getColumnIndex("pathPhoto")));
                    photo.setFilePhoto(cursor.getString(cursor.getColumnIndex("filePhoto")));
                    photo.setStatusPhoto(cursor.getInt(cursor.getColumnIndex("statusPhoto")));

                    photoList.add(photo);
                } while (cursor.moveToNext());

            }
            cursor.close();

            return photoList;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<Photo> findAll() throws Exception {
        return this.find(null, null, null, null, null, null);
    }

    public Photo findByPath(String photoPath) throws Exception {
        List<Photo> listPhoto = this.find("pathPhoto = ?", new String[]{photoPath}, null, null, null, null);

        if (listPhoto.size() > 0) {
            return listPhoto.get(0);
        } else {
            return null;
        }
    }


    public Photo findByName(String photoName) throws Exception {
        List<Photo> listPhoto = this.find("pathPhoto = ?", new String[]{photoName}, null, null, null, null);

        if (listPhoto.size() > 0) {
            return listPhoto.get(0);
        }

        return null;
    }

    public List<Photo> findByNames(String[] photoNames) throws Exception {
        return this.find("pathPhoto IN (?)", photoNames, null, null, null, null);
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getId() {
        return this._id;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    public String getPathPhoto() {
        return this.pathPhoto;
    }

    public void setFilePhoto(String filePhoto) {
        this.filePhoto = filePhoto;
    }

    public String getFilePhoto() {
        return this.filePhoto;
    }

    public void setStatusPhoto(int statusPhoto) {
        this.statusPhoto = statusPhoto;
    }

    public int getStatusPhoto() {
        return this.statusPhoto;
    }

    public long insert(String nullColumnHack, ContentValues values) {
        return super.insertData(TABLE_NAME, nullColumnHack, values);
    }

    public void update(ContentValues values, String whereClause, String[] whereArgs) {
        super.updateData(TABLE_NAME, values, whereClause, whereArgs);
    }

    public void delete(String whereClause, String[] whereArgs) {
        super.deleteData(TABLE_NAME, whereClause, whereArgs);
    }
}
