package com.gmail.lusersks.notes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.lusersks.notes.R;
import com.gmail.lusersks.notes.model.NotesData;
import com.gmail.lusersks.notes.presenter.ParseTodoAdapter;

import static com.gmail.lusersks.notes.model.IntentConstants.EXTRA_CONTENT;
import static com.gmail.lusersks.notes.model.IntentConstants.EXTRA_NOTE;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvShowTitle, tvShowContent;
    private EditText etShowTitle, etShowContent;
    private ListView lvTodoList;
    private ParseTodoAdapter adapter;
    private boolean isTodo;
    private String oldTitle;

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
        tvShowTitle = (TextView) findViewById(R.id.tv_show_title);
        tvShowTitle.setOnClickListener(this);

        tvShowContent = (TextView) findViewById(R.id.tv_show_content);
        tvShowContent.setOnClickListener(this);

        etShowTitle = (EditText) findViewById(R.id.et_show_title);
        etShowContent = (EditText) findViewById(R.id.et_show_content);

        lvTodoList = (ListView) findViewById(R.id.lv_todo_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_show_title: {
                // Show EditText for title
                tvShowTitle.setVisibility(View.INVISIBLE);
                etShowTitle.setVisibility(View.VISIBLE);
                etShowTitle.setText(tvShowTitle.getText());

                // Hide Edit Text for content
                etShowContent.setVisibility(View.INVISIBLE);
                tvShowContent.setVisibility(View.VISIBLE);
                if (!etShowContent.getText().toString().isEmpty()) {
                    tvShowContent.setText(etShowContent.getText());
                }

                break;
            }
            case R.id.tv_show_content: {
                // Show EditText for content
                tvShowContent.setVisibility(View.INVISIBLE);
                etShowContent.setVisibility(View.VISIBLE);
                etShowContent.setText(tvShowContent.getText());

                // Hide Edit Text for title
                etShowTitle.setVisibility(View.INVISIBLE);
                tvShowTitle.setVisibility(View.VISIBLE);
                if (!etShowTitle.getText().toString().isEmpty()) {
                    tvShowTitle.setText(etShowTitle.getText());
                }

                break;
            }
        }
    }

    private void readExtraFields() {
        Intent intent = getIntent();

        oldTitle = intent.getStringExtra(EXTRA_NOTE);
        tvShowTitle.setText(oldTitle);

        String noteContent = intent.getStringExtra(EXTRA_CONTENT);
        tvShowContent.setText(noteContent);

        //Log.d("appLog", noteContent);
        isTodo = noteContent.charAt(0) == '*' || noteContent.charAt(0) == '-';
        setTitle(isTodo ? "Todo" : "Note");
    }

    private void initListView() {
        if (isTodo) {
            adapter = new ParseTodoAdapter(this,
                    R.layout.todo_list_view,
                    R.id.tv_todo_task,
                    tvShowTitle.getText().toString()
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

            tvShowContent.setVisibility(View.GONE);
            lvTodoList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString("noteTitle", tvShowTitle.getText().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();

        String title, content;

        if (tvShowTitle.getVisibility() == View.VISIBLE) title = tvShowTitle.getText().toString();
        else title = etShowTitle.getText().toString();

        if (tvShowContent.getVisibility() == View.VISIBLE) content = tvShowContent.getText().toString();
        else content = etShowContent.getText().toString();

        String type = isTodo ? "todo" : "note";
        NotesData.editItem(oldTitle, title, content, type);

        // Exit from the activity -> Save cb state to DB
        if (isTodo) adapter.saveCbState();
    }
}
