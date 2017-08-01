package br.com.artevivapublicidade.expertpdv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DB {
    private SQLiteDatabase db;

    public DB(Context context) {
        //Permite o acesso de edição e de busca do Banco de dados
        DBCore auxDB = new DBCore(context);
        db = auxDB.getWritableDatabase();
    }

    public void insert(String tableName, String nullColumnHack, ContentValues values) {
        db.insert(tableName, nullColumnHack, values);
    }

    public void update(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        db.update(tableName, values, whereClause, whereArgs);
    }

    public void delete(String tableName, String whereClause, String[] whereArgs) {
        db.delete(tableName, whereClause, whereArgs);
    }

    public List select(String tableName, String[] columns, String whereClause, String[] whereArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = db.query(tableName, columns, whereClause, whereArgs, groupBy, having, orderBy, limit);
        List list = new ArrayList();

        return list;
    }
}
