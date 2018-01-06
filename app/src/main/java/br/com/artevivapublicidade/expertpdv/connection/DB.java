package br.com.artevivapublicidade.expertpdv.connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class DB<T> {
    private SQLiteDatabase db;

    public DB(Context context) {
        //Permite o acesso de edição e de busca do Banco de dados
        DBCore auxDB = new DBCore(context);
        db = auxDB.getWritableDatabase();
    }

    protected long insertData(String tableName, String nullColumnHack, ContentValues values) {
        return db.insert(tableName, nullColumnHack, values);
    }

    protected int updateData(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        return db.update(tableName, values, whereClause, whereArgs);
    }

    protected int deleteData(String tableName, String whereClause, String[] whereArgs) {
        return db.delete(tableName, whereClause, whereArgs);
    }

    protected Cursor select(String tableName, String[] columns, String whereClause, String[] whereArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = db.query(tableName, columns, whereClause, whereArgs, groupBy, having, orderBy, limit);

        return cursor;
    }
}
