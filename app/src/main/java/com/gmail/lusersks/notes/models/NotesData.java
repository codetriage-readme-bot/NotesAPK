package com.gmail.lusersks.notes.models;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.lusersks.notes.presenters.NotesActions.FLAG_TODO;

public class NotesData {
    private static List<String> listItems = new ArrayList<>();
    private static List<String> listItemsContents = new ArrayList<>();
    private static List<String> listItemsTypes = new ArrayList<>();

    private static DBHelper dbHelper;

    public static void initDBHelper(MainActivity activity) {
        dbHelper = new DBHelper(activity);

        listItems.clear();
        listItemsContents.clear();
        listItemsTypes.clear();

        List<List<String>> list = dbHelper.getRecords();
        listItems.addAll(list.get(0));
        listItemsContents.addAll(list.get(1));
        listItemsTypes.addAll(list.get(2));
    }

    public static void addItem(String title, String content, String itemType) {
        listItems.add(title);
        listItemsContents.add(content);
        listItemsTypes.add(itemType);

        dbHelper.insertRecord(title, content, itemType);
    }

    public static void editItem(String oldTitle, String title, String content, String itemType) {
        int index = findIndexByTitle(oldTitle);
        listItems.set(index, title);
        listItemsContents.set(index, content);
        listItemsTypes.set(index, itemType);

        dbHelper.updateRecord(index + 1, title, content, itemType);
    }

    public static void deleteItem(String noteTitle) {
        int index = findIndexByTitle(noteTitle);
        listItems.remove(index);
        listItemsContents.remove(index);
        listItemsTypes.remove(index);

        dbHelper.deleteRecord(index);
    }

    public static void deleteItem(int index) {
        listItems.remove(index);
        listItemsContents.remove(index);
        listItemsTypes.remove(index);

        dbHelper.deleteRecord(index);
    }

    public static void clearItems() {
        listItems.clear();
        listItemsContents.clear();
        listItemsTypes.clear();

        dbHelper.clearDB();
    }

    public static String getNote(int index) {
        return listItems.get(index);
    }

    public static ArrayList<String> getNotes() {
        return (ArrayList<String>) listItems;
    }

    public static ArrayList<String> getTodos() {
        ArrayList<String> listTodos = new ArrayList<>();
        for (int i = 0; i < listItems.size(); i++) {
            if (listItemsTypes.get(i).equals(FLAG_TODO)) {
                listTodos.add(listItems.get(i));
            }
        }
        return listTodos;
    }

    public static String getContext(String title) {
        int index = findIndexByTitle(title);
        return listItemsContents.get(index);
    }

    private static int findIndexByTitle(String title) {
        int index = -1;
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).equals(title)) {
                index = i;
            }
        }
        return index;
    }
}
