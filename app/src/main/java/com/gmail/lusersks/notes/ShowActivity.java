package com.gmail.lusersks.notes;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ShowActivity extends AppCompatActivity implements DialogNoteDeleteListener {

    private TextView tvShowNote;
    private TextView tvShowContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        tvShowNote = (TextView) findViewById(R.id.tvShowNote);
        tvShowContent = (TextView) findViewById(R.id.tvShowContent);

        Intent intent = this.getIntent();
        tvShowNote.setText(intent.getStringExtra(MainActivity.EXTRA_NOTE));
        tvShowContent.setText(intent.getStringExtra(MainActivity.EXTRA_CONTENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_show_item_edit:
                Intent intent = new Intent(this, FormActivity.class);
                intent.putExtra(MainActivity.EXTRA_FORM_TITLE, "Edit Note");
                intent.putExtra(MainActivity.EXTRA_NOTE, tvShowNote.getText());
                intent.putExtra(MainActivity.EXTRA_CONTENT, tvShowContent.getText());
//                startActivity(intent);
                startActivityForResult(intent, 2);
                return true;

            case R.id.menu_show_item_delete:
                new DeleteDialog().show(getFragmentManager(), "deleteNoteDialog");
                finish();
                return true;

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            tvShowNote.setText(data.getStringExtra(MainActivity.EXTRA_NOTE));
            tvShowContent.setText(data.getStringExtra(MainActivity.EXTRA_CONTENT));

            Snackbar.make(tvShowNote, "The note is edited", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("noteTitle", tvShowNote.getText().toString());
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String noteTitle) {
        if (dialog instanceof DeleteDialog) {
            NotesData.deleteItem(noteTitle);
        }
    }
}
