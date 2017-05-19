package com.gmail.lusersks.notes.presenters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.models.NotesData;
import com.gmail.lusersks.notes.views.FormActivity;
import com.gmail.lusersks.notes.views.ShowActivity;

import static com.gmail.lusersks.notes.MainActivity.EXTRA_CONTENT;
import static com.gmail.lusersks.notes.MainActivity.EXTRA_FORM_TITLE;
import static com.gmail.lusersks.notes.MainActivity.EXTRA_NOTE;
import static com.gmail.lusersks.notes.provider.Constants.COL_BODY;
import static com.gmail.lusersks.notes.provider.Constants.COL_ID;
import static com.gmail.lusersks.notes.provider.Constants.COL_TITLE;
import static com.gmail.lusersks.notes.provider.Constants.COL_TYPE;
import static com.gmail.lusersks.notes.provider.Constants.NOTES_CONTENT_URI;
import static com.gmail.lusersks.notes.provider.Constants.URI_NOTES;

public class NotesActions {

    public static final String EXTRA_TYPE = "type";
    public static final String FLAG_NOTE = "note";
    public static final String FLAG_TODO = "todo";
    public static final String EXTRA_ID = "id";

    public static final int REQUEST_CODE_NEW = 1;
    public static final int REQUEST_CODE_EDIT = 2;
    public static final int REQUEST_CODE_SHOW = 3;

    private static void addNew(Activity activity, String type) {
        Intent intent = new Intent();
        intent.setClass(activity.getApplicationContext(), FormActivity.class);
        intent.putExtra(EXTRA_FORM_TITLE, "New Note");
        intent.putExtra(EXTRA_NOTE, "");
        intent.putExtra(EXTRA_CONTENT, "");
        intent.putExtra(EXTRA_TYPE, type);
        activity.startActivityForResult(intent, REQUEST_CODE_NEW);
    }

    public static void editSelected(ShowActivity activity, String type) {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra(EXTRA_FORM_TITLE, "Edit Note");
        intent.putExtra(EXTRA_NOTE, activity.tvItemTitle.getText());
        intent.putExtra(EXTRA_CONTENT, activity.tvNoteContent.getText());
        intent.putExtra(EXTRA_TYPE, type);
        activity.startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    public static void editSelected(Activity activity, int position, String type) {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra(EXTRA_FORM_TITLE, "Edit Note");
        intent.putExtra(EXTRA_ID, position);
        intent.putExtra(EXTRA_NOTE, NotesData.getNote(position));
        intent.putExtra(EXTRA_CONTENT, NotesData.getContext(position));
        intent.putExtra(EXTRA_TYPE, type);
        activity.startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    public static void showSelected(Activity activity, int position) {
        //String note = NotesData.getNote(position);
        String note = "";
        String content = "";

        // TODO why not work by this method, what wrong with Selection Clause
        /*String[] mProjection = {COL_ID, COL_TITLE, COL_BODY};
        String mSelectionClause = COL_ID + " = ?";
        String[] mSelectionArgs = {String.valueOf(position)};

        Cursor cursor = activity.getApplicationContext().getContentResolver().query(
                NOTES_CONTENT_URI,
                mProjection,
                mSelectionClause,
                mSelectionArgs,
                null);

        if (cursor.moveToFirst()) {
            note = cursor.getString(cursor.getColumnIndex(COL_TITLE));
            Log.d("appLog", "note = " + note);
            content = cursor.getString(cursor.getColumnIndex(COL_BODY));
            Log.d("appLog", "content = " + content);
        }
        cursor.close();*/

        Log.d("appLog", "NotesData.getIdAtPosition(position) = " + NotesData.getIdAtPosition(position));
        Uri uri = Uri.parse(NOTES_CONTENT_URI + "/" + (NotesData.getIdAtPosition(position)));
        ContentResolver cr = activity.getApplicationContext().getContentResolver();

        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            note = cursor.getString(cursor.getColumnIndex(COL_TITLE));
            Log.d("appLog", "note = " + note);
            content = cursor.getString(cursor.getColumnIndex(COL_BODY));
            Log.d("appLog", "content = " + content);
        }
        cursor.close();

        Intent intent = new Intent(activity.getApplicationContext(), ShowActivity.class);
        intent.putExtra(EXTRA_NOTE, note);
        intent.putExtra(EXTRA_CONTENT, content); //NotesData.getContext(note));
        activity.startActivity(intent);
    }

    public static void addNewNote(Activity activity) {
        addNew(activity, FLAG_NOTE);
    }

    public static void addNewTodo(MainActivity activity) {
        addNew(activity, FLAG_TODO);
    }

    public static void deleteSelected(String title) {
        NotesData.deleteItem(title);
    }
}
