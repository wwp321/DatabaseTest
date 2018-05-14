package com.byron.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "Byron Wang MainActivity";
    MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new MyDatabaseHelper(this, "BookStore.db", null, 5);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.create_db_button:
                databaseHelper.getWritableDatabase();
                break;
            case R.id.add_daba_button: {
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                db.insert("Book", null, values);
                values.clear();
                values.put("name", "The lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.95);
                db.insert("Book", null, values);
            }
                break;
            case R.id.update_data_button:{
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 10.1);
                db.update("Book", values, "name=?", new String[]{"The Da Vinci Code"});
            }
                break;

            case R.id.delete_data_button:{
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                db.delete("Book", "pages > ?", new String[]{"480"});
                break;
            }

            case R.id.query_data_button:{
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if(cursor.moveToFirst()) {
                    do{
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        Log.d(TAG, "onButtonClick: name:" + name + ",author:" + author + ",pages:" + pages + ",price:" + price);
                    }while (cursor.moveToNext());
                }
                break;
            }
        }
    }
}
