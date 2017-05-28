package com.gmail.lusersks.notes.presenter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.model.NotesData;
import com.gmail.lusersks.notes.activity.NewActivity;
import com.gmail.lusersks.notes.activity.ShowActivity;

import static com.gmail.lusersks.notes.model.IntentConstants.EXTRA_CONTENT;
import static com.gmail.lusersks.notes.model.IntentConstants.EXTRA_FORM_TITLE;
import static com.gmail.lusersks.notes.model.IntentConstants.EXTRA_NOTE;
import static com.gmail.lusersks.notes.model.IntentConstants.EXTRA_TYPE;
import static com.gmail.lusersks.notes.model.IntentConstants.REQUEST_CODE_NEW;
import static com.gmail.lusersks.notes.provider.Constants.COL_BODY;
import static com.gmail.lusersks.notes.provider.Constants.COL_TITLE;
import static com.gmail.lusersks.notes.provider.Constants.NOTES_CONTENT_URI;

public class NotesActions {

    static final String FLAG_NOTE = "note";
    static final String FLAG_TODO = "todo";

    private static void addNew(Activity activity, String type) {
        Intent intent = new Intent();
        intent.setClass(activity.getApplicationContext(), NewActivity.class);
        intent.putExtra(EXTRA_NOTE, "");
        intent.putExtra(EXTRA_CONTENT, "");
        intent.putExtra(EXTRA_TYPE, type);
        activity.startActivityForResult(intent, REQUEST_CODE_NEW);
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
