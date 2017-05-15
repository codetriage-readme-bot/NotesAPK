package com.gmail.lusersks.notes.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import static com.gmail.lusersks.notes.provider.Constants.COL_BODY;
import static com.gmail.lusersks.notes.provider.Constants.COL_ID;
import static com.gmail.lusersks.notes.provider.Constants.COL_TITLE;
import static com.gmail.lusersks.notes.provider.Constants.COL_TYPE;
import static com.gmail.lusersks.notes.provider.Constants.NOTES_CONTENT_ITEM_TYPE;
import static com.gmail.lusersks.notes.provider.Constants.NOTES_CONTENT_TYPE;
import static com.gmail.lusersks.notes.provider.Constants.NOTES_CONTENT_URI;
import static com.gmail.lusersks.notes.provider.Constants.NOTES_DB;
import static com.gmail.lusersks.notes.provider.Constants.NOTES_TABLE;
import static com.gmail.lusersks.notes.provider.Constants.URI_NOTES;
import static com.gmail.lusersks.notes.provider.Constants.URI_NOTES_ID;
import static com.gmail.lusersks.notes.provider.Constants.VERSION_DB;
import static com.gmail.lusersks.notes.provider.Constants.uriMatcher;

public class NotesProvider extends ContentProvider {

    private static final String CREATE_TABLE_QUERY =
            "create table if not exist " + NOTES_TABLE + " ( "
                    + COL_ID + " integer primary key autoincrement, "
                    + COL_TITLE + " text, "
                    + COL_BODY + " text, "
                    + COL_TYPE + " varchar);";

    public static final String TAG_LOG = "appLog";

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Log.d(TAG_LOG, "NotesProvider.onCreate");
        dbHelper = new DBHelper(getContext(), NOTES_DB, null, VERSION_DB);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG_LOG, "NotesProvider.query, " + uri.toString());

        switch (uriMatcher.match(uri)) {
            case URI_NOTES:
                Log.d(TAG_LOG, "URI_NOTES");
                break;
            case URI_NOTES_ID:
                String id = uri.getLastPathSegment();
                Log.d(TAG_LOG, "URI_NOTES_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = COL_ID + " = " + id;
                } else {
                    selection = selection + " AND " + COL_ID + " = " + id;
                }
                break;
            default:
        }

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(NOTES_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        // ContentResolver will notify the cursor about changing data in NOTES_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(), NOTES_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG_LOG, "NotesProvider.getType");
        switch (uriMatcher.match(uri)) {
            case URI_NOTES:
                return NOTES_CONTENT_TYPE;
            case URI_NOTES_ID:
                return NOTES_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG_LOG, "NotesProvider.insert");

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(NOTES_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(NOTES_CONTENT_URI, rowID);
        Log.d(TAG_LOG, "NotesProvider.insert -- resultUri: " + resultUri);
        // notify ContentResolver about changing data in resultUri
        getContext().getContentResolver().notifyChange(resultUri, null);

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG_LOG, "NotesProvider.delete");
        db = dbHelper.getWritableDatabase();
        db.delete(NOTES_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG_LOG, "NotesProvider.update");
        return 0;
    }

    private class DBHelper extends SQLiteOpenHelper {
        DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            Log.d(TAG_LOG, "DBHelper.constructor");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG_LOG, "DBHelper.onCreate");
            db.execSQL(CREATE_TABLE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG_LOG, "DBHelper.onUpdate");
        }
    }
}
