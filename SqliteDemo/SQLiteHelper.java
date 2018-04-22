package idv.haojun.sqlitedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "sample.db";

    public static final int VERSION = 1;

    private static SQLiteDatabase database;

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new SQLiteHelper(context, DATABASE_NAME,
                    null, VERSION).getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BookDao.createTable());
        db.execSQL(AuthorDao.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BookDao.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AuthorDao.TABLE_NAME);
        onCreate(db);
    }
}