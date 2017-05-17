package com.gmail.lusersks.notes.presenters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gmail.lusersks.notes.R;
import com.gmail.lusersks.notes.models.NotesData;

import java.util.ArrayList;

import static com.gmail.lusersks.notes.presenters.NotesActions.FLAG_TODO;

public class ParseTodoAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final int layoutResourceId;
    private final String title;
    private final SparseBooleanArray mCheckStates;

    private ArrayList<String> todoItemsState;
    private ArrayList<String> todoList;

    public ParseTodoAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, String title) {
        super(context, resource, textViewResourceId);

        this.context = context;
        this.layoutResourceId = resource;
        this.title = title;

        parseTodosAndFillArray();
        mCheckStates = new SparseBooleanArray(this.getCount());
    }

    private void parseTodosAndFillArray() {
        String sContent = NotesData.getContext(title);

        Log.d("appLog", sContent);

        String[] ss = sContent.split("\\n");
        todoItemsState = new ArrayList<>();
        for (String s1 : ss) {
            todoItemsState.add(String.valueOf(s1.charAt(0)));
        }

        String[] todoItems = sContent.split("[*,-]");
        todoList = new ArrayList<>();
        for (String s : todoItems) {
            if (!s.trim().isEmpty()) {
                todoList.add(s.trim());
            }
        }

        addAll(todoList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        AppInfoHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AppInfoHolder();

            holder.tvTask = (TextView) row.findViewById(R.id.tv_todo_task);
            holder.cbDone = (CheckBox) row.findViewById(R.id.cb_todo_done);

            row.setTag(holder);

        } else {
            holder = (AppInfoHolder) row.getTag();
        }

        String oneTask = this.getItem(position);
        holder.tvTask.setText(oneTask);

        holder.cbDone.setTag(position);
        mCheckStates.put(position, todoItemsState.get(position).charAt(0) == '*');
        holder.cbDone.setChecked(mCheckStates.get(position, false));

        return row;
    }

    private boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    private void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);
    }

    public void toggleCbState(int position) {
        setChecked(position, !isChecked(position));
    }

    public void saveCbState() {
        StringBuilder sbContent = new StringBuilder();

        for (int i = 0; i < getCount(); i++) {
            char state = mCheckStates.get(i, false) ? '*' : '-';
            sbContent.append(state).append(" ").append(todoList.get(i)).append("\n");
        }

        Log.d("appLog", String.valueOf(sbContent));

        ContentResolver cr = context.getApplicationContext().getContentResolver();
        NotesData.editItem(title, title, String.valueOf(sbContent), FLAG_TODO);
    }

    private class AppInfoHolder {
        TextView tvTask;
        CheckBox cbDone;
    }
}
