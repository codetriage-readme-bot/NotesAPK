package com.gmail.lusersks.notes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gmail.lusersks.notes.provider.Constants.COL_BODY;
import static com.gmail.lusersks.notes.provider.Constants.COL_ID;
import static com.gmail.lusersks.notes.provider.Constants.COL_TITLE;
import static com.gmail.lusersks.notes.provider.Constants.COL_TYPE;
import static com.gmail.lusersks.notes.provider.Constants.NOTES_DB;
import static com.gmail.lusersks.notes.provider.Constants.NOTES_TABLE;

public class DBHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_QUERY =
            "create table if not exist " + NOTES_TABLE + " ("
            + COL_ID + " integer primary key autoincrement, "
            + COL_TITLE + " text, "
            + COL_BODY + " text, "
            + COL_TYPE + " varchar);";

    public DBHelper(Context context) {
        super(context, NOTES_DB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertRecord(String noteTitle, String noteBody, String itemType) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, noteTitle);
        cv.put(COL_BODY, noteBody);
        cv.put(COL_TYPE, itemType);

        long rowID = getWritableDatabase().insert(NOTES_TABLE, null, cv);

        Log.d("appLog", "Log inserted, ID = " + rowID);
    }

    public void updateRecord(int index, String noteTitle, String noteBody, String itemType) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, noteTitle);
        cv.put(COL_BODY, noteBody);
        cv.put(COL_TYPE, itemType);

        long rowID = getWritableDatabase().update(NOTES_TABLE, cv, COL_ID + " = " + index, null);

        Log.d("appLog", "Log updated, ID = " + rowID);
    }

    public void deleteRecord(int index) {
        long rowID = getWritableDatabase().delete(NOTES_TABLE, COL_ID + " = " + index, null);

        Log.d("appLog", "Log updated, ID = " + rowID);
    }

    public void clearDB() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("drop table " + NOTES_TABLE + ";");
        db.execSQL(CREATE_TABLE_QUERY);
    }

    public List<List<String>> getRecords() {
        List<String> listOfNotes = new ArrayList<>();
        List<String> listOfContents = new ArrayList<>();
        List<String> listOfTypes = new ArrayList<>();

        Cursor cursor = getReadableDatabase().query(NOTES_TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(COL_TITLE);
            int bodyColumnIndex = cursor.getColumnIndex(COL_BODY);
            int typeColumnIndex = cursor.getColumnIndex(COL_TYPE);

            do {
                String noteTitle = cursor.getString(titleColumnIndex);
                String noteBody = cursor.getString(bodyColumnIndex);
                String noteType = cursor.getString(typeColumnIndex);

                listOfNotes.add(noteTitle);
                listOfContents.add(noteBody);
                listOfTypes.add(noteType);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return new ArrayList<>(Arrays.asList(listOfNotes, listOfContents, listOfTypes));
    }
}
