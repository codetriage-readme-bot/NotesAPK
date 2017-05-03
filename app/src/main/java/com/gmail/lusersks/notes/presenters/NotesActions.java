package com.gmail.lusersks.notes.presenters;

import android.app.Activity;
import android.content.Intent;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.models.NotesData;
import com.gmail.lusersks.notes.views.FormActivity;
import com.gmail.lusersks.notes.views.ShowActivity;

import static com.gmail.lusersks.notes.MainActivity.EXTRA_CONTENT;
import static com.gmail.lusersks.notes.MainActivity.EXTRA_FORM_TITLE;
import static com.gmail.lusersks.notes.MainActivity.EXTRA_NOTE;

public class NotesActions {

    public static final String EXTRA_TYPE = "type";
    public static final String FLAG_NOTE = "note";
    public static final String FLAG_TODO = "todo";

    private static void addNew(Activity activity, String type) {
        Intent intent = new Intent();
        intent.setClass(activity.getApplicationContext(), FormActivity.class);
        intent.putExtra(EXTRA_FORM_TITLE, "New Note");
        intent.putExtra(EXTRA_NOTE, "");
        intent.putExtra(EXTRA_CONTENT, "");
        intent.putExtra(EXTRA_TYPE, type);
        activity.startActivityForResult(intent, 1);
    }

    public static void editSelected(ShowActivity activity, String type) {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra(EXTRA_FORM_TITLE, "Edit Note");
        intent.putExtra(EXTRA_NOTE, activity.tvItemTitle.getText());
        intent.putExtra(EXTRA_CONTENT, activity.tvNoteContent.getText());
        intent.putExtra(EXTRA_TYPE, type);
        activity.startActivityForResult(intent, 2);
    }

    public static void editSelected(Activity activity, int position, String type) {
        String note = NotesData.getNote(position);
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra(EXTRA_FORM_TITLE, "Edit Note");
        intent.putExtra(EXTRA_NOTE, note);
        intent.putExtra(EXTRA_CONTENT, NotesData.getContext(note));
        intent.putExtra(EXTRA_TYPE, type);
        activity.startActivityForResult(intent, 3);
    }

    public static void showSelected(Activity activity, int position) {
        String note = NotesData.getNote(position);
        Intent intent = new Intent(activity.getApplicationContext(), ShowActivity.class);
        intent.putExtra(EXTRA_NOTE, note);
        intent.putExtra(EXTRA_CONTENT, NotesData.getContext(note));
        activity.startActivity(intent);
    }

    public static void addNewNote(MainActivity activity) {
        addNew(activity, FLAG_NOTE);
    }

    public static void addNewTodo(MainActivity activity) {
        addNew(activity, FLAG_TODO);
    }

    public static void deleteSelected(String title) {
        NotesData.deleteItem(title);
    }
}
