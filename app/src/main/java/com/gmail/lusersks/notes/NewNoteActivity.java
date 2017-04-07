package com.gmail.lusersks.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class NewNoteActivity extends AppCompatActivity {

    public EditText etNoteTitle;
    public EditText etNoteContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        etNoteTitle = (EditText) findViewById(R.id.etNoteTitle);
        etNoteContent = (EditText) findViewById(R.id.etNoteBody);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent();

        switch (id) {
            case R.id.menuItemSave:
                String title = etNoteTitle.getText().toString();
                String content = etNoteContent.getText().toString();
                NotesData.addItem(title, content);

//                intent.putExtra(MainActivity.EXTRA_TEXT, title);
//                this.setResult(Activity.RESULT_OK, intent);
                this.finish();

                break;

            case R.id.menuItemSettings:
//                Intent intent = new Intent();
                intent.setClass(this, PreferencesActivity.class);
                startActivity(intent);
                break;

            default:
                return false;
        }

        return true;
    }
}
