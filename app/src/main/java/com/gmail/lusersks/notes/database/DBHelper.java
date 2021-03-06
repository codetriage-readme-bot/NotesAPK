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

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notesDB";
    private static final String TABLE_NAME = "notesAndTodos";

    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_TYPE = "type";

    private static final String CREATE_TABLE_QUERY =
            "create table " + TABLE_NAME + " ("
            + "id integer primary key autoincrement, "
            + "title text, "
            + "body text, "
            + "type varchar);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
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
        cv.put(COLUMN_TITLE, noteTitle);
        cv.put(COLUMN_BODY, noteBody);
        cv.put(COLUMN_TYPE, itemType);

        long rowID = getWritableDatabase().insert(TABLE_NAME, null, cv);

        Log.d("appLog", "Log inserted, ID = " + rowID);
    }

    public void updateRecord(int index, String noteTitle, String noteBody, String itemType) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, noteTitle);
        cv.put(COLUMN_BODY, noteBody);
        cv.put(COLUMN_TYPE, itemType);

        long rowID = getWritableDatabase().update(TABLE_NAME, cv, "id = " + index, null);

        Log.d("appLog", "Log updated, ID = " + rowID);
    }

    public void deleteRecord(int index) {
        long rowID = getWritableDatabase().delete(TABLE_NAME, "id = " + index, null);

        Log.d("appLog", "Log updated, ID = " + rowID);
    }

    public void clearDB() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("drop table " + TABLE_NAME + ";");
        db.execSQL(CREATE_TABLE_QUERY);
    }

    public List<List<String>> getRecords() {
        List<String> listOfNotes = new ArrayList<>();
        List<String> listOfContents = new ArrayList<>();
        List<String> listOfTypes = new ArrayList<>();

        Cursor cursor = getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(COLUMN_TITLE);
            int bodyColumnIndex = cursor.getColumnIndex(COLUMN_BODY);
            int typeColumnIndex = cursor.getColumnIndex(COLUMN_TYPE);

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
