package com.gmail.lusersks.notes;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.lusersks.notes.presenters.NotesActions;
import com.gmail.lusersks.notes.models.NotesData;
import com.gmail.lusersks.notes.presenters.SimpleNotesAdapter;
import com.gmail.lusersks.notes.views.PreferencesActivity;

import static com.gmail.lusersks.notes.provider.Constants.COL_ID;
import static com.gmail.lusersks.notes.provider.Constants.COL_TITLE;
import static com.gmail.lusersks.notes.provider.Constants.NOTES_CONTENT_URI;
import static com.gmail.lusersks.notes.provider.NotesProvider.TAG_LOG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_CONTENT = "extra_content";
    public static final String EXTRA_FORM_TITLE = "extra_form_title";

    private static boolean isSelectAll = true;
    private static boolean isNotesList = true;

    private FloatingActionButton addNewNoteBtn;
    private TextView tvPlaceholder;

    private RadioButton rbFabNote;
    public SimpleNotesAdapter adapter;
    public AppBarLayout appBarLayout;
    public ListView listView;
    public Toolbar toolbar;
    public Menu optionsMenu;

    private SimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initDBHelper();
        setButtonsBehavior();

        setSupportActionBar(toolbar);
        initLeftNavigationBar();
    }

    private void initViews() {
        appBarLayout = (AppBarLayout) findViewById(R.id.main_app_bar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        listView = (ListView) findViewById(R.id.list_notes);
        tvPlaceholder = (TextView) findViewById(R.id.there_is_no_notes);

        addNewNoteBtn = (FloatingActionButton) findViewById(R.id.fab);
        rbFabNote = (RadioButton) findViewById(R.id.fab_note);
    }

    private void initDBHelper() {
        NotesData.initDBHelper(getContentResolver());
    }

    private void setButtonsBehavior() {
        addNewNoteBtn.setOnClickListener(this);
    }

    @SuppressWarnings("deprecation")
    private void initLeftNavigationBar() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();

        final String[] PROJECTION = new String[] {COL_ID, COL_TITLE};

        // Performs a "managed query" to the ContentProvider. The Activity
        // will handle closing and requerying the cursor.
        //
        // WARNING!! This query (and any subsequent re-queries) will be
        // performed on the UI Thread!!
        Cursor cursor = managedQuery(
                NOTES_CONTENT_URI,  // The Uri constant in the ContentProvider class
                PROJECTION,         // The column to return for each data row
                null,               // No where clause
                null,               // No where clause
                null);              // No sort order

        String[] dataColumns = { COL_TITLE };
        int[] viewIDs = { R.id.tv_list_view_item };

        // Create the backing adapter for the ListView
        //
        // WARNING!! While not readily obvious, using this constructor will
        // tell the CursorAdapter to register a ContentObserver that will
        // monitor the underlying data source. As part of the monitoring
        // process, the ContentObserver will call requery() on the cursor
        // each time the data is updated. Since Cursor#requery() is performed
        // on the UI thread, this constructor should be avoided at all costs!
        cursorAdapter = new SimpleCursorAdapter(
                this,               // The Activity context
                R.layout.list_view, // Points to the XML for a listView
                cursor,             // Cursor that contain the data to display
                dataColumns,        // Bind the data in column COL_TITLE...
                viewIDs             // ...to the TextView with id "R.id.tv_list_view_item"
        );
        //NotesCursorAdapter ncAdapter = new NotesCursorAdapter(this, cursor);
        // old adapter
        //adapter = new SimpleNotesAdapter(this, R.layout.list_view, R.id.tv_list_view_item);

        // Set the ListView's adapter to be the cursor adapter that was just created
        listView.setAdapter(cursorAdapter);

        //listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        setListViewBehavior();
        //registerForContextMenu(listView);

        //listView.setVisibility(View.VISIBLE);
        if (cursorAdapter.getCount() != 0) tvPlaceholder.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) return;
        if (requestCode == 1) {
            Toast.makeText(this, "New note is added", Toast.LENGTH_SHORT).show();
            showOrHidePlaceholder();
        }
        if (requestCode == 3) {
            Toast.makeText(this, "The note is edited", Toast.LENGTH_SHORT).show();
            closeDeletingProcess();
            optionsMenu.findItem(R.id.edit_note).setVisible(false);
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
            case R.id.menu_main_clear: {
                NotesData.clearItems();
                tvPlaceholder.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.edit_note: {
                String type = rbFabNote.isChecked() ? "note" : "todo";
                NotesActions.editSelected(this, adapter.getSparseArray().keyAt(0), type);
                break;
            }
            case R.id.select_all: {
                CheckBox cbNote;
                // If SelectAll was clicked -> inverse state of the CheckBox'es
                boolean isChecked = !isSelectAll;
                for (int i = 0; i < adapter.getCount(); i++) {
                    cbNote = (CheckBox) listView.getChildAt(i).findViewById(R.id.lv_check_box);
                    cbNote.setChecked(isChecked);
                    adapter.putIntoSparseArray(i, isChecked);
                }
                break;
            }
            case R.id.multiple_delete: {
                showDeleteDialog();
                return true;
            }
            case R.id.cancel_delete: {
                closeDeletingProcess();
                break;
            }
            default: {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        adapter.renewSparseArray();

        setVisibilityForAllCheckBox(View.VISIBLE);
        optionsMenu.findItem(R.id.menu_main_clear).setVisible(false);
        optionsMenu.setGroupVisible(R.id.context_view_items, true);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        CheckBox checkBox = (CheckBox) info.targetView.findViewById(R.id.lv_check_box);
        checkBox.setChecked(true);

        adapter.putIntoSparseArray(info.position, true);
    }

    private void setVisibilityForAllCheckBox(int visibility) {
        CheckBox cbNote;
        for (int i = 0; i < listView.getChildCount(); i++) {
            cbNote = (CheckBox) listView.getChildAt(i).findViewById(R.id.lv_check_box);
            cbNote.setVisibility(visibility);
            cbNote.setChecked(false);
            cbNote.setText(String.valueOf(i));
            if (visibility == View.VISIBLE) {
                setCheckBoxBehavior(cbNote);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_notes_and_todo: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_notes_list: {
                isNotesList = true;
                //NotesData.getNotes();
                break;
            }
            case R.id.nav_todo_list: {
                isNotesList = false;
                //NotesData.getTodos();
                break;
            }
            case R.id.nav_settings: {
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_about: {
                break;
            }
            case R.id.nav_share: {
                break;
            }
            case R.id.nav_send: {
                break;
            }
            case R.id.nav_exit: {
                finish();
                break;
            }
            default: {
                return false;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab: {
                NotesActions.addNewNote(MainActivity.this);
                /*if (rbFabNote.isChecked()) {
                    Log.d("appLog", "addNewNote");
                    NotesActions.addNewNote(MainActivity.this);
                } else {
                    Log.d("appLog", "addNewTodo");
                    NotesActions.addNewTodo(MainActivity.this);
                }*/
                break;
            }
        }
    }

    private void setListViewBehavior() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotesActions.showSelected(MainActivity.this, position);
            }
        });
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                        adapter.removeSelected();
                        closeDeletingProcess();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Confirmation");
        alert.show();
    }

    private void closeDeletingProcess() {
        setVisibilityForAllCheckBox(View.GONE);
        optionsMenu.findItem(R.id.menu_main_clear).setVisible(true);
        optionsMenu.setGroupVisible(R.id.context_view_items, false);
        toolbar.setTitle("Notes");
        showOrHidePlaceholder();
    }

    private void setCheckBoxBehavior(CheckBox cbNote) {
        cbNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.putIntoSparseArray(Integer.parseInt(buttonView.getText().toString()), isChecked);
                isSelectAll = adapter.isSelectAll();

                toolbar.setTitle(adapter.getSbArraySize() + "");
                optionsMenu.findItem(R.id.edit_note).setVisible(adapter.isOnly());

                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showOrHidePlaceholder() {
        //Log.d("appLog", "Placeholder: count of items = " + adapter.getCount());
        if (cursorAdapter.getCount() != 0) {
            tvPlaceholder.setVisibility(View.GONE);
        } else {
            tvPlaceholder.setVisibility(View.VISIBLE);
        }
    }
}
