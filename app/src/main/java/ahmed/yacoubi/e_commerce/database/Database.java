package ahmed.yacoubi.e_commerce.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

import ahmed.yacoubi.e_commerce.model.User;
import ahmed.yacoubi.e_commerce.utils.UtilDB;

public class Database extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "database_name";

    // Table Names
    private static final String DB_TABLE = "table_image";

    // column names
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";

    // Table create statement
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + DB_TABLE + "(" +
            KEY_NAME + " TEXT unique ," +
            KEY_IMAGE + " BLOB);";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_IMAGE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_TABLE_IMAGE);

    }

    private static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public Bitmap getImage(String path) {
        if (path != null) {
            byte[] image = getImageFromaDataBase(path);
            if (image != null) {
                Bitmap b = BitmapFactory.decodeByteArray(image, 0, image.length);
                return b;
            }
        }
        return null;


    }


    public void addImage(Bitmap bitmap, String name) throws SQLiteException {


        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_IMAGE, getBytes(bitmap));
        database.insert(DB_TABLE, null, cv);
    }

    public boolean isFound(String name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                        DB_TABLE + " WHERE " + KEY_NAME + " LIKE ?"
                , new String[]{name});

        if (cursor != null)
            if (cursor.moveToFirst()) {
                return true;


            }
        return false;
    }

    public byte[] getImageFromaDataBase(String path) {
        byte[] bytes = null;
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TABLE + " WHERE " + KEY_NAME + " =? "
                    , new String[]{path});
            if (cursor.moveToFirst()) {


                bytes = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE));
                Log.d("aaaaaa", "getImageFromaDataBase: " + cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                return bytes;

            }


        } catch (Exception e) {

        }


        return bytes;
    }
}
