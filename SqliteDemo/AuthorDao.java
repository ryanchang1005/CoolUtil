package idv.haojun.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class AuthorDao {

    // table name
    public static final String TABLE_NAME = "authors";
    // pk
    private static final String ID = "id";
    // other column
    private static final String NAME = "name";
    

    static String createTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("Create Table " + TABLE_NAME + " ( ");
        sb.append(ID + " TEXT PRIMARY KEY , ");
        sb.append(NAME + " TEXT NOT NULL) ");
        
        return sb.toString();
    }

    private SQLiteDatabase db;

    public AuthorDao(Context context) {
        db = SQLiteHelper.getDatabase(context);
    }

    public boolean insert(Author author) {

        ContentValues cv = new ContentValues();

        cv.put(NAME, author.getName());

        return db.insert(TABLE_NAME, null, cv) > 0;
    }

    public boolean update(Author author) {

        ContentValues cv = new ContentValues();

        cv.put(NAME, author.getName());

        String where = ID + "=" + author.getId();

        return db.update(TABLE_NAME, cv, where, null) > 0;
    }


    public boolean delete(int id) {
        String where = ID + "=" + id;

        return db.delete(TABLE_NAME, where, null) > 0;
    }

    public List<Author> getAll() {
        List<Author> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }


    public Author get(int id) {

        Author item = null;

        String where = ID + "=" + id;

        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);


        if (result.moveToFirst()) {

            item = getRecord(result);
        }


        result.close();

        return item;
    }

    public Author getRecord(Cursor cursor) {

        Author result = new Author();
        result.setId(cursor.getString(0));
        result.setName(cursor.getString(1));

        return result;
    }


    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }
}