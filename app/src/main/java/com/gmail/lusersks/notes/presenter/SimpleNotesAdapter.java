package com.gmail.lusersks.notes.presenter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.R;
import com.gmail.lusersks.notes.model.NotesData;

import java.util.ArrayList;

public class SimpleNotesAdapter extends ArrayAdapter<String> {

    private Context context;
    private SparseBooleanArray sbArray;

    public SimpleNotesAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.context = context;
        sbArray = new SparseBooleanArray();
        addAll(NotesData.getNotes());
    }

    public int getCount() {
        return NotesData.getItems().size();
    }

    public void clearAll() {
        clear();
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
        TextView textView = (TextView) mainActivity.listView.getChildAt(position).findViewById(R.id.tv_note_title);
        String item = textView.getText().toString();
        remove(item);
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
        return sbArray.size() == NotesData.getItems().size();
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
