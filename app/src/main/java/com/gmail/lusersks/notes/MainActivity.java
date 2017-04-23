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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.gmail.lusersks.notes.controller.NotesActions;
import com.gmail.lusersks.notes.data.NotesData;
import com.gmail.lusersks.notes.data.SimpleNotesAdapter;
import com.gmail.lusersks.notes.listeners.CheckedNotesListener;
import com.gmail.lusersks.notes.view.PreferencesActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_CONTENT = "extra_content";
    public static final String EXTRA_FORM_TITLE = "extra_form_title";

    private static final boolean DELETING_PROCESS_START = true;
    private static final boolean DELETING_PROCESS_END = false;

    private static boolean checkedAllCheckBoxes;

    public AppBarLayout appBarLayout;
    public Toolbar toolbar;
    public SimpleNotesAdapter notesAdapter;
    public ListView listView;
    public Menu optionsMenu;

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
        notesAdapter = new SimpleNotesAdapter(this);
        (listView = getListView()).setAdapter(notesAdapter);

        // Button for add note
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotesActions.addNew(MainActivity.this);
                notesAdapter.notifyDataSetChanged();
            }
        });
    }

    private ListView getListView() {
        ListView listView = (ListView) findViewById(R.id.list_notes);
//        notesAdapter = new SimpleNotesAdapter(this);
//        listView.setAdapter(notesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotesActions.showSelected(MainActivity.this, position);
            }
        });

        registerForContextMenu(listView);

//        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
//        listView.setMultiChoiceModeListener(new MultiNotesListener(this));

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
        this.optionsMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_add: {
                NotesActions.addNew(MainActivity.this);
                notesAdapter.notifyDataSetChanged();
                return true;
            }
            case R.id.menu_main_clear: {
                NotesActions.clearAll();
                notesAdapter.notifyDataSetChanged();
                return true;
            }
            case R.id.menu_settings: {
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.select_all: {
//                setCheckingForAllCheckBoxes(!checkedAllCheckBoxes);
                setCheckingForAllCheckBoxes(true);
                return true;
            }
            case R.id.multiple_delete: {
                // TODO: do it as in MultiNoteListener
                return true;
            }
            case R.id.cancel_delete: {
                setVisibilityForAllCheckBox(View.GONE);
                setVisibilityOfMenuMainItems(DELETING_PROCESS_END);
                toolbar.setTitle("Notes");
                CheckedNotesListener.setCheckedCount(0);
                return true;
            }
            default: {
                return false;
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        setVisibilityForAllCheckBox(View.VISIBLE);
        setVisibilityOfMenuMainItems(DELETING_PROCESS_START);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        CheckBox checkBox = (CheckBox) info.targetView.findViewById(R.id.lv_check_box);
        checkBox.setChecked(true);
    }

    private void setVisibilityForAllCheckBox(int visibility) {
        CheckBox cbNote;
        for (int i = 0; i < listView.getChildCount(); i++) {
            cbNote = (CheckBox) listView.getChildAt(i).findViewById(R.id.lv_check_box);
            cbNote.setVisibility(visibility);
            cbNote.setChecked(false);
            if (visibility == View.VISIBLE) {
                cbNote.setOnCheckedChangeListener(new CheckedNotesListener(this));
            }
        }
    }

    private void setCheckingForAllCheckBoxes(boolean checked) {
        CheckBox cbNote;
        for (int i = 0; i < listView.getChildCount(); i++) {
            cbNote = (CheckBox) listView.getChildAt(i).findViewById(R.id.lv_check_box);
            cbNote.setChecked(checked);
        }
    }

    private void setVisibilityOfMenuMainItems(boolean isProcess) {
        // Main menu items
        optionsMenu.findItem(R.id.menu_main_add).setVisible(!isProcess);
        optionsMenu.findItem(R.id.menu_main_clear).setVisible(!isProcess);
        optionsMenu.findItem(R.id.menu_settings).setVisible(!isProcess);
        // Deleting menu items
        optionsMenu.findItem(R.id.edit_note).setVisible(isProcess);
        optionsMenu.findItem(R.id.cancel_delete).setVisible(isProcess);
        optionsMenu.findItem(R.id.select_all).setVisible(isProcess);
        optionsMenu.findItem(R.id.multiple_delete).setVisible(isProcess);
    }
}
