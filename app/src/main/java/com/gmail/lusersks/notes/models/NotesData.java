package com.gmail.lusersks.notes.models;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class NotesData {
    private static List<String> listNotes = new ArrayList<>();
    private static List<String> listContents = new ArrayList<>();
    private static DBHelper dbHelper;

    public static void initDBHelper(MainActivity activity) {
        dbHelper = new DBHelper(activity);

        listNotes.clear();
        listContents.clear();

        List<List<String>> list = dbHelper.getRecords();
        listNotes.addAll(list.get(0));
        listContents.addAll(list.get(1));
    }

    public static void addItem(String title, String content) {
        listNotes.add(title);
        listContents.add(content);

        dbHelper.insertRecord(title, content);
    }

    public static void editItem(String oldTitle, String title, String content) {
        int index = findIndexByNoteTitle(oldTitle);
        listNotes.set(index, title);
        listContents.set(index, content);

        dbHelper.updateRecord(index + 1, title, content);
    }

    public static void deleteItem(String noteTitle) {
        int index = findIndexByNoteTitle(noteTitle);
        listNotes.remove(index);
        listContents.remove(index);

        dbHelper.deleteRecord(index);
    }

    public static void deleteItem(int index) {
        listNotes.remove(index);
        listContents.remove(index);

        dbHelper.deleteRecord(index);
    }

    public static void clearItems() {
        listNotes.clear();
        listContents.clear();

        dbHelper.clearDB();
    }

    public static String getNote(int index) {
        return listNotes.get(index);
    }

    public static ArrayList<String> getNotes() {
        return (ArrayList<String>) listNotes;
    }

    public static String getContext(String note) {
        int index = findIndexByNoteTitle(note);
        return listContents.get(index);
    }

    private static int findIndexByNoteTitle(String title) {
        int index = -1;
        for (int i = 0; i < listNotes.size(); i++) {
            if (listNotes.get(i).equals(title)) {
                index = i;
            }
        }
        return index;
    }
}
