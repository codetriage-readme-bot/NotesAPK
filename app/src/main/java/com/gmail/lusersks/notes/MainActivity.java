package com.gmail.lusersks.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_CONTENT = "extra_content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NotesData.loadItems(this);

        // Button for add note
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), NewActivity.class);
                startActivity(intent);

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // List of all notes
        final ListAdapter mAdapter = new SimpleAdapter(this,
                NotesData.getItems(),
                R.layout.activity_listview,
                new String[]{NotesData.NOTE_TITLE},
                new int[]{R.id.tvNoteTitle}
        );
        final ListView mList = (ListView) findViewById(R.id.list_notes);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);

//        registerForContextMenu(mList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String note = NotesData.getNote(position);
        Intent intent = new Intent(this, ShowActivity.class);
        intent.putExtra(EXTRA_NOTE, note);
        intent.putExtra(EXTRA_CONTENT, NotesData.getContext(note));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // TODO: EDIT and DELETE notes

        if (id == R.id.menu_item_settings) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }


    // Maybe not needed
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        View view = item.getActionView();
        Log.d("appLog", ((TextView) view).getText().toString());

        switch (item.getItemId()) {
            case R.id.menu_note_show:
//                showNote();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
