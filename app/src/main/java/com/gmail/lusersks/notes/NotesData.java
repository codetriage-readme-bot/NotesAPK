package com.gmail.lusersks.notes;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class NotesData {

    public static final String NOTE_TITLE = "title";

    private static String[] notes = {"Test note"};
    private static String[] contents = {"The content of the first test post."};

    private static List<String> listNotes = new ArrayList<>(Arrays.asList(notes[0]));
    private static List<String> listContents = new ArrayList<>(Arrays.asList(contents[0]));

    public static List<Map<String, String>> getItems() {

        List<Map<String, String>> items = new ArrayList<>();

        for (String item : listNotes) {
            Map<String, String> map = new HashMap<>();
            map.put(NOTE_TITLE, item);
            items.add(map);
        }

        return items;
    }

    public static void addItem(String title, String content) {
        listNotes.add(title);
        listContents.add(content);
    }
}
