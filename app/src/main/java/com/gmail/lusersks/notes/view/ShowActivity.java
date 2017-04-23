package com.gmail.lusersks.notes.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.controller.NotesActions;
import com.gmail.lusersks.notes.data.NotesData;
import com.gmail.lusersks.notes.R;

public class ShowActivity extends AppCompatActivity {

    public TextView tvShowNote;
    public TextView tvShowContent;

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
            case R.id.menu_show_item_edit: {
                NotesActions.editSelected(ShowActivity.this);
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
                        NotesData.deleteItem((String) tvShowNote.getText());
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Confirmation");
        alert.show();
    }
}
