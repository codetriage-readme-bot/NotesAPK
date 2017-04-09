package com.gmail.lusersks.notes;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        TextView tvShowNote = (TextView) findViewById(R.id.tvShowNote);
        TextView tvShowContent = (TextView) findViewById(R.id.tvShowContent);

        Intent intent = this.getIntent();
        tvShowNote.setText(intent.getStringExtra(MainActivity.EXTRA_NOTE));
        tvShowContent.setText(intent.getStringExtra(MainActivity.EXTRA_CONTENT));
    }
}
