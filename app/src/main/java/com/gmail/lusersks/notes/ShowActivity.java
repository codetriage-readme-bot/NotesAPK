package com.gmail.lusersks.notes;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ShowActivity extends AppCompatActivity {

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
                startActivity(intent);
                return true;
            case R.id.menu_show_item_delete:
                return true;
        }
        return false;
    }
}
