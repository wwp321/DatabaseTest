package com.byron.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {
    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;

    public static final String AUTHORITY = "com.byron.databasetest.provider";

    private static UriMatcher uriMatcher;
    private MyDatabaseHelper myDatabaseHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    public DatabaseProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        int deleteRows = 0;

        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deleteRows = db.delete("book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRows = db.delete("book", "id=?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRows = db.delete("category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRows = db.delete("category", "id=?", new String[]{categoryId});
                break;
        }

        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".book";
//                break;
            case  BOOK_ITEM:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".book";
//                break;
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".category";
//                break;
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".category";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("book", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("category", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/category/" + newCategoryId);
                break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        myDatabaseHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = null;

        switch (uriMatcher.match(uri)){
            case BOOK_DIR:{
                cursor = db.query("Book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case BOOK_ITEM: {
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book", projection, "id=?",new String[]{bookId}, null, null, sortOrder);
                break;
            }

            case CATEGORY_DIR: {
                cursor = db.query("category", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            case CATEGORY_ITEM: {
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("category", projection, "id=?", new String[]{categoryId}, null, null, sortOrder);
                break;
            }

            default:
                break;
        }

        return  cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        int updateRows = 0;

        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                updateRows = db.update("Book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows = db.update("Book", values, "id=?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows = db.update("category", values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRows = db.update("category", values, "id=?", new String[]{categoryId});
                break;
        }

        return updateRows;
    }
}
