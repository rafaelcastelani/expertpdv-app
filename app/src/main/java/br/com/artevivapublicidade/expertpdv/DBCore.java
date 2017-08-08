package br.com.artevivapublicidade.expertpdv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCore extends SQLiteOpenHelper {
    private static final String DB_NAME = "ExpertPDV";
    private static final int DB_VERSION = 3;

    public DBCore(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE photo(_id INTEGER PRIMARY KEY AUTOINCREMENT, pathPhoto TEXT NOT NULL, filePhoto TEXT NOT NULL, statusPhoto INTEGER DEFAULT 1);");
    }

    //Caso a versão seja diferente, o banco é recriado
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE photo;");
        onCreate(sqLiteDatabase);
    }
}
