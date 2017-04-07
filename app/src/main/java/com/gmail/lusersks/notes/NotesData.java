package com.gmail.lusersks.notes;

import android.app.Activity;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class NotesData {

    public static final String NOTE_TITLE = "title";

    private static String[] notes = {"Test note"};
    private static String[] contents = {"The content of the first test post."};

    /*private static List<String> listNotes = new ArrayList<>(Arrays.asList(notes[0]));
    private static List<String> listContents = new ArrayList<>(Arrays.asList(contents[0]));*/
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

    public static void addItem(NewNoteActivity newNoteActivity, String title, String content) {
        listNotes.add(title);
        listContents.add(content);

        // Save to file
        FileOutputStream fos = null;
        String fileName = "note" + (listNotes.size() - 1);

        try {
            fos = newNoteActivity.openFileOutput(fileName, Context.MODE_PRIVATE);

            fos.write(title.getBytes());
            fos.write('\n');
            fos.write(content.getBytes());

            fos.close();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadItems(MainActivity mainActivity) {
        // Load notes from file
        FileInputStream fis = null;
        String fileName = "";
        String note = "";
        String content = "";

        for (int i = 0; ; i++) {
            fileName = "note" + i;
            try {
                fis = mainActivity.openFileInput(fileName);
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
}
