package com.gmail.lusersks.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.gmail.lusersks.notes.controller.NotesActions;
import com.gmail.lusersks.notes.data.NotesData;
import com.gmail.lusersks.notes.data.SimpleNotesAdapter;
import com.gmail.lusersks.notes.listeners.MultiNotesListener;
import com.gmail.lusersks.notes.view.PreferencesActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_CONTENT = "extra_content";
    public static final String EXTRA_FORM_TITLE = "extra_form_title";

    public AppBarLayout appBarLayout;
    public Toolbar toolbar;
    public SimpleNotesAdapter simpleNotesAdapter;
    public ListView listView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarLayout = (AppBarLayout) findViewById(R.id.main_app_bar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NotesData.loadItems(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Initialize the list of notes
        simpleNotesAdapter = new SimpleNotesAdapter(this);
        (listView = getListView()).setAdapter(simpleNotesAdapter);

        // Button for add note
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotesActions.addNew(MainActivity.this);
                simpleNotesAdapter.notifyDataSetChanged();
            }
        });
    }

    private ListView getListView() {
        ListView listView = (ListView) findViewById(R.id.list_notes);
//        simpleNotesAdapter = new SimpleNotesAdapter(this);
//        listView.setAdapter(simpleNotesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotesActions.showSelected(MainActivity.this, position);
            }
        });

        registerForContextMenu(listView);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new MultiNotesListener(this));

        return listView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Snackbar.make(fab, "New note is added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_add: {
                NotesActions.addNew(MainActivity.this);
                simpleNotesAdapter.notifyDataSetChanged();
                return true;
            }
            case R.id.menu_main_clear: {
                NotesActions.clearAll();
                simpleNotesAdapter.notifyDataSetChanged();
                return true;
            }
            case R.id.menu_settings: {
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
