package com.gmail.lusersks.notes.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.presenters.NotesActions;
import com.gmail.lusersks.notes.R;
import com.gmail.lusersks.notes.presenters.ParseTodoAdapter;

public class ShowActivity extends AppCompatActivity {
    public TextView tvItemTitle, tvNoteContent;
    private ListView lvTodoList;
    private ParseTodoAdapter adapter;
    private boolean isTodo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        initViews();
        Intent intent = getIntent();
        readExtraFields(intent);
        setTypeToTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initListView();
    }

    private void initViews() {
        tvItemTitle = (TextView) findViewById(R.id.tv_show_title);
        tvNoteContent = (TextView) findViewById(R.id.tv_note_content);
        lvTodoList = (ListView) findViewById(R.id.lv_todo_list);
    }

    private void readExtraFields(Intent intent) {
        tvItemTitle.setText(intent.getStringExtra(MainActivity.EXTRA_NOTE));
        tvNoteContent.setText(intent.getStringExtra(MainActivity.EXTRA_CONTENT));
        String str = tvNoteContent.getText().toString();
        Log.d("appLog", str);
        isTodo = str.charAt(0) == '*' || str.charAt(0) == '-';
    }

    private void setTypeToTitle() {
        String type = isTodo ? "Todo" : "Note";
        setTitle(type);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_show_item_edit: {
                String type = isTodo ? "todo" : "note";
                NotesActions.editSelected(ShowActivity.this, type);
                return true;
            }
            case R.id.menu_show_item_delete: {
                showDeleteDialog();
                return true;
            }
            default: {
                return false;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            readExtraFields(intent);
            Toast.makeText(this, "The note is edited", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("noteTitle", tvItemTitle.getText().toString());
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this);
        builder.setMessage("Do you  want to delete selected record(s)?")
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO  Auto-generated method stub
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NotesActions.deleteSelected(tvItemTitle.getText().toString());
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Confirmation");
        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Exit from the activity -> Save cb state to DB
        if (isTodo) adapter.saveCbState();
    }
}
