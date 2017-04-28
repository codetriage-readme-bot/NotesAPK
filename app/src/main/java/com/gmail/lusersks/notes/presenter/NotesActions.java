package com.gmail.lusersks.notes.presenter;

import android.app.Activity;
import android.content.Intent;

import com.gmail.lusersks.notes.model.NotesData;
import com.gmail.lusersks.notes.view.FormActivity;
import com.gmail.lusersks.notes.view.ShowActivity;

import static com.gmail.lusersks.notes.MainActivity.EXTRA_CONTENT;
import static com.gmail.lusersks.notes.MainActivity.EXTRA_FORM_TITLE;
import static com.gmail.lusersks.notes.MainActivity.EXTRA_NOTE;

public class NotesActions {

    public static void addNew(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity.getApplicationContext(), FormActivity.class);
        intent.putExtra(EXTRA_FORM_TITLE, "New note");
        intent.putExtra(EXTRA_NOTE, "");
        intent.putExtra(EXTRA_CONTENT, "");
        activity.startActivityForResult(intent, 1);
    }

    public static void editSelected(ShowActivity activity) {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra(EXTRA_FORM_TITLE, "Edit Note");
        intent.putExtra(EXTRA_NOTE, activity.tvShowNote.getText());
        intent.putExtra(EXTRA_CONTENT, activity.tvShowContent.getText());
        activity.startActivityForResult(intent, 2);
    }

    public static void editSelected(Activity activity, int position) {
        String note = NotesData.getNote(position);
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra(EXTRA_FORM_TITLE, "Edit Note");
        intent.putExtra(EXTRA_NOTE, note);
        intent.putExtra(EXTRA_CONTENT, NotesData.getContext(note));
        activity.startActivityForResult(intent, 3);
    }

    public static void showSelected(Activity activity, int position) {
        String note = NotesData.getNote(position);
        Intent intent = new Intent(activity.getApplicationContext(), ShowActivity.class);
        intent.putExtra(EXTRA_NOTE, note);
        intent.putExtra(EXTRA_CONTENT, NotesData.getContext(note));
        activity.startActivity(intent);
    }

    public static void clearAll() {
        NotesData.clearItems();
    }
}
