package com.gmail.lusersks.notes.listeners;


import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.R;

public class CheckedNotesListener implements CompoundButton.OnCheckedChangeListener {
    private static int checkedCount;
    private final MainActivity mainActivity;

    public CheckedNotesListener(Context context) {
        this.mainActivity = (MainActivity) context;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            checkedCount++;
        } else {
            checkedCount--;
        }
        mainActivity.toolbar.setTitle(checkedCount);
        if (checkedCount > 1) {
            mainActivity.optionsMenu.findItem(R.id.edit_note).setVisible(false);
        } else {
            mainActivity.optionsMenu.findItem(R.id.edit_note).setVisible(true);
        }
    }

    public static void setCheckedCount(int count) {
        checkedCount = count;
    }

    public static int getCheckedCount() {
        return checkedCount;
    }
}
