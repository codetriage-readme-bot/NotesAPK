package com.gmail.lusersks.notes.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.R;
import com.gmail.lusersks.notes.presenters.ParseTodoAdapter;

public class ShowActivity extends AppCompatActivity {
    public TextView tvItemTitle;
    public TextView tvNoteContent;
    private ListView lvTodoList;
    private ParseTodoAdapter adapter;
    private boolean isTodo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        initViews();
        readExtraFields();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initListView();
    }

    private void initViews() {
        //tvItemTitle = (TextView) findViewById(R.id.tv_show_title);
        tvItemTitle = null;
        tvNoteContent = (TextView) findViewById(R.id.tv_note_content);
        lvTodoList = (ListView) findViewById(R.id.lv_todo_list);
    }

    private void readExtraFields() {
        Intent intent = getIntent();

        setTitle(intent.getStringExtra(MainActivity.EXTRA_NOTE));

        String noteContent = intent.getStringExtra(MainActivity.EXTRA_CONTENT);
        tvNoteContent.setText(noteContent);

        //Log.d("appLog", noteContent);
        isTodo = noteContent.charAt(0) == '*' || noteContent.charAt(0) == '-';
    }

    private void initListView() {
        if (isTodo) {
            adapter = new ParseTodoAdapter(this,
                    R.layout.todo_list_view,
                    R.id.tv_todo_task,
                    tvItemTitle.getText().toString()
            );

            lvTodoList.setAdapter(adapter);
            lvTodoList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

            lvTodoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CheckBox cb = (CheckBox) view.findViewById(R.id.cb_todo_done);
                    cb.setChecked(!cb.isChecked());
                    adapter.toggleCbState(position);
                }
            });

            tvNoteContent.setVisibility(View.GONE);
            lvTodoList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString("noteTitle", tvItemTitle.getText().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Exit from the activity -> Save cb state to DB
        if (isTodo) adapter.saveCbState();
    }
}
