package com.gmail.lusersks.notes;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
