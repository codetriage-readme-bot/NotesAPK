package com.gmail.lusersks.notes.data;

import android.content.Context;
import android.util.Log;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.view.FormActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotesData {

    public static final String NOTE_TITLE = "title";

    private static List<String> listNotes = new ArrayList<>();
    private static List<String> listContents = new ArrayList<>();

    public static List<Map<String, String>> getItems() {

        List<Map<String, String>> items = new ArrayList<>();

        for (String item : listNotes) {
            Map<String, String> map = new HashMap<>();
            map.put(NOTE_TITLE, item);
            items.add(map);
        }

        return items;
    }

    public static void addItem(FormActivity activity, String title, String content) {
        listNotes.add(title);
        listContents.add(content);

        String fileName = "note" + (listNotes.size() - 1);
        saveDataToFile(activity, title, content, fileName);
    }

    public static void editItem(FormActivity activity, String oldTitle, String title, String content) {
        int index = findIndexByNoteTitle(oldTitle);
        listNotes.set(index, title);
        listContents.set(index, content);

        String fileName = "note" + index;
        saveDataToFile(activity, title, content, fileName);
    }

    public static void deleteItem(String noteTitle) {
        int index = findIndexByNoteTitle(noteTitle);
        listNotes.remove(index);
        listContents.remove(index);

        String fileName = "note" + index;

        Log.d("appLog", "NOTE: " + noteTitle + " - is deleted");
    }

    public static void clearItems() {
        listNotes.clear();
        listContents.clear();
    }

    public static void loadItems(MainActivity activity) {
        // Load notes from file
        FileInputStream fis = null;
        String fileName = "";
        String note = "";
        String content = "";

        for (int i = 0; ; i++) {
            fileName = "note" + i;
            try {
                fis = activity.openFileInput(fileName);
                int bufSize = fis.available();
                byte[] buffer = new byte[bufSize];

                fis.read(buffer);

                String data = new String(buffer);
                int j = 0;
                while (data.charAt(j++) != '\n');

                note = data.substring(0, j - 1);
                content = data.substring(j, bufSize);

                listNotes.add(note);
                listContents.add(content);

            } catch (FileNotFoundException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getNote(int index) {
        return listNotes.get(index);
    }

    public static String getContext(String note) {
        int index = findIndexByNoteTitle(note);
        return listContents.get(index);
    }

    private static void saveDataToFile(FormActivity activity, String title, String content, String fileName) {
        try {
            FileOutputStream fos = activity.openFileOutput(fileName, Context.MODE_PRIVATE);

            fos.write(title.getBytes());
            fos.write('\n');
            fos.write(content.getBytes());

            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
