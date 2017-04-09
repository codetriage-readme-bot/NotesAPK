package com.gmail.lusersks.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class FormActivity extends AppCompatActivity {

    public EditText etNoteTitle;
    public EditText etNoteContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        etNoteTitle = (EditText) findViewById(R.id.etNoteTitle);
        etNoteContent = (EditText) findViewById(R.id.etNoteContent);

        Intent intent = this.getIntent();
        setTitle(intent.getStringExtra(MainActivity.EXTRA_FORM_TITLE));
        etNoteTitle.setText(intent.getStringExtra(MainActivity.EXTRA_NOTE));
        etNoteContent.setText(intent.getStringExtra(MainActivity.EXTRA_CONTENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_save) {
            String title = etNoteTitle.getText().toString();
            String content = etNoteContent.getText().toString();
            NotesData.addItem(this, title, content);

            this.finish();
            return true;
        }

        if (id == R.id.menu_item_settings) {
            Intent intent = new Intent();
            intent.setClass(this, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}
