package com.gmail.lusersks.notes.models;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.gmail.lusersks.notes.database.NotesItem;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.lusersks.notes.provider.Constants.COL_BODY;
import static com.gmail.lusersks.notes.provider.Constants.COL_ID;
import static com.gmail.lusersks.notes.provider.Constants.COL_TITLE;
import static com.gmail.lusersks.notes.provider.Constants.COL_TYPE;
import static com.gmail.lusersks.notes.provider.Constants.NOTES_CONTENT_URI;
import static com.gmail.lusersks.notes.provider.NotesProvider.TAG_LOG;

public class NotesData {
    private static List<NotesItem> listNotesItems = new ArrayList<>();

    private static ContentResolver cr;

    public static void initDBHelper(ContentResolver contentResolver) {
        cr = contentResolver;

        listNotesItems.clear();

        Cursor cursor = cr.query(NOTES_CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(COL_ID);
            int titleColumnIndex = cursor.getColumnIndex(COL_TITLE);
            int contentColumnIndex = cursor.getColumnIndex(COL_BODY);
            int typeColumnIndex = cursor.getColumnIndex(COL_TYPE);

            do {
                int noteId = cursor.getInt(idColumnIndex);
                String noteTitle = cursor.getString(titleColumnIndex);
                String noteContent = cursor.getString(contentColumnIndex);
                String noteType = cursor.getString(typeColumnIndex);

                listNotesItems.add(new NotesItem(noteId, noteTitle, noteContent, noteType));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public static void addItem(String title, String content, String itemType) {
        int id = listNotesItems.isEmpty() ? 0 : listNotesItems.get(listNotesItems.size() - 1).getId();
        listNotesItems.add(new NotesItem(id + 1, title, content, itemType));

        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_BODY, content);
        cv.put(COL_TYPE, itemType);
        Uri newUri = cr.insert(NOTES_CONTENT_URI, cv);
        Log.d(TAG_LOG, "insert, result Uri : " + newUri);

        //dbHelper.insertRecord(title, content, itemType);
    }

    public static void editItem(String oldTitle, String title, String content, String itemType) {
        int index = findIndexByTitle(oldTitle);
        int id = listNotesItems.get(index).getId();
        listNotesItems.get(index).setTitle(title);
        listNotesItems.get(index).setContent(content);

        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_BODY, content);
        cv.put(COL_TYPE, itemType);
        Uri uri = ContentUris.withAppendedId(NOTES_CONTENT_URI, id);
        int cnt = cr.update(uri, cv, null, null);
        Log.d(TAG_LOG, "update, result Uri : " + cnt);

        //dbHelper.updateRecord(index + 1, title, content, itemType);
    }

    public static void deleteItem(String noteTitle) {
        int index = findIndexByTitle(noteTitle);
        deleteItem(index);
    }

    public static void deleteItem(int index) {
        Log.d(TAG_LOG, "delete, index = " + index);
        int id = listNotesItems.get(index).getId();
        Log.d(TAG_LOG, "delete, id = " + id);
        listNotesItems.remove(index);

        Uri uri = ContentUris.withAppendedId(NOTES_CONTENT_URI, id);
        int cnt = cr.delete(uri, null, null);
        Log.d(TAG_LOG, "delete, count = " + cnt);

        //dbHelper.deleteRecord(index);
    }

    public static void clearItems() {
        listNotesItems.clear();

        int cnt = cr.delete(NOTES_CONTENT_URI, null, null);
        Log.d(TAG_LOG, "delete, count = " + cnt);

        //dbHelper.clearDB();
    }

    public static String getNote(int index) {
        return listNotesItems.get(index).getTitle();
    }

    public static int getCount() {
        return listNotesItems.size();
    }

    /*public static ArrayList<String> getTodos() {
        ArrayList<String> listTodos = new ArrayList<>();
        for (int i = 0; i < listItems.size(); i++) {
            if (listItemsTypes.get(i).equals(FLAG_TODO)) {
                listTodos.add(listItems.get(i));
            }
        }
        return listTodos;
    }*/

    public static String getContext(int index) {
        return listNotesItems.get(index).getContent();
    }

    private static int findIndexByTitle(String title) {
        int index = -1;
        for (int i = 0; i < listNotesItems.size(); i++) {
            if (listNotesItems.get(i).getTitle().equals(title)) {
                index = i;
            }
        }
        return index;
    }

    public static String getContext(String title) {
        int index = findIndexByTitle(title);
        return getContext(index);
    }

    public static int getIdAtPosition(int index) {
        return listNotesItems.get(index).getId();
    }
}
