package com.gmail.lusersks.notes.presenters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.R;
import com.gmail.lusersks.notes.models.NotesData;

import java.util.ArrayList;

import static com.gmail.lusersks.notes.provider.Constants.COL_TITLE;

public class NotesCursorAdapter extends SimpleCursorAdapter {

    private Context context;
    private SparseBooleanArray sbArray;
    private ArrayList<String> listNotes = new ArrayList<>();

    public NotesCursorAdapter(@NonNull Context context, Cursor cursor) {
        super(context,
                R.layout.list_view,
                cursor,
                new String[]{COL_TITLE},
                new int[]{R.id.tv_list_view_item}
        );
        this.context = context;

        for (int i = 0; i < NotesData.getCount(); i++) {
            listNotes.add(NotesData.getNote(i));
        }

        sbArray = new SparseBooleanArray();
    }

    public int getCount() {
        return NotesData.getCount();
    }

    public void clearAll() {
        listNotes.clear();
        NotesData.clearItems();
    }

    public void removeSelected() {
        int i = sbArray.size();
        do {
            remove(sbArray.keyAt(--i));
        } while (i > 0);
        this.sbArray.clear();
    }

    private void remove(int position) {
        MainActivity mainActivity = (MainActivity) this.context;
        TextView textView = (TextView) mainActivity.listView.getChildAt(position).findViewById(R.id.tv_list_view_item);
        String item = textView.getText().toString();
        listNotes.remove(item);
        NotesData.deleteItem(position);
    }

    public void renewSparseArray() {
        sbArray.clear();
    }

    public void putIntoSparseArray(int position, boolean isChecked) {
        if (isChecked) {
            sbArray.put(position, true);
        } else {
            sbArray.delete(position);
        }
    }

    public boolean isSelectAll() {
        return sbArray.size() == NotesData.getCount();
    }

    public int getSbArraySize() {
        return sbArray.size();
    }

    public boolean isOnly() {
        return sbArray.size() == 1;
    }

    public SparseBooleanArray getSparseArray() {
        return sbArray;
    }
}
