package com.gmail.lusersks.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT = "note_to_add";
    public static final int REQUEST_TEXT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("appLog", "onCreate");

        // Button for add note
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), NewNoteActivity.class);
//                startActivityForResult(intent, REQUEST_TEXT);
                startActivity(intent);

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("appLog", "onResume");

        // TODO: load it only once
        NotesData.loadItems(this);

        // List of all notes
        ListAdapter mAdapter = new SimpleAdapter(this,
                NotesData.getItems(),
                R.layout.activity_listview,
                new String[]{NotesData.NOTE_TITLE},
                new int[]{R.id.tvNoteTitle}
        );
        ListView mList = (ListView) findViewById(R.id.list_notes);
        mList.setAdapter(mAdapter);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("appLog", data.getStringExtra(EXTRA_TEXT));
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // TODO: do not show SAVE item in Main Activity
        /*MenuItem menuItem = menu.getItem(0);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuItemSettings) {
            Intent intent = new Intent();
            intent.setClass(this, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}
