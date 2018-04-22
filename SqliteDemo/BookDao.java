package idv.haojun.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class BookDao {

    // table name
    public static final String TABLE_NAME = "books";
    // pk
    private static final String ID = "id";
    // other column
    private static final String TITLE = "title";
    private static final String AUTHOR_ID = "author_id";

    static String createTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("Create Table " + TABLE_NAME + " ( ");
        sb.append(ID + " TEXT PRIMARY KEY , ");
        sb.append(TITLE + " TEXT NOT NULL, ");
        sb.append(AUTHOR_ID + " TEXT NOT NULL) ");
        return sb.toString();
    }

    private SQLiteDatabase db;

    public BookDao(Context context) {
        db = SQLiteHelper.getDatabase(context);
    }

    public boolean insert(Book book) {

        ContentValues cv = new ContentValues();

        cv.put(TITLE, book.getTitle());
        cv.put(AUTHOR_ID, book.getAuthor().getId());

        return db.insert(TABLE_NAME, null, cv) > 0;
    }

    public boolean update(Book book) {

        ContentValues cv = new ContentValues();

        cv.put(TITLE, book.getTitle());
        cv.put(AUTHOR_ID, book.getAuthor().getId());

        String where = ID + "=" + book.getId();

        return db.update(TABLE_NAME, cv, where, null) > 0;
    }


    public boolean delete(int id) {
        String where = ID + "=" + id;

        return db.delete(TABLE_NAME, where, null) > 0;
    }

    public List<Book> getAll() {
        List<Book> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }


    public Book get(int id) {

        Book item = null;

        String where = ID + "=" + id;

        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);


        if (result.moveToFirst()) {

            item = getRecord(result);
        }


        result.close();

        return item;
    }

    public Book getRecord(Cursor cursor) {

        Book result = new Book();
        result.setId(cursor.getString(0));
        result.setTitle(cursor.getString(1));

        Author author = new Author();
        author.setId(cursor.getString(2));

        result.setAuthor(author);

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