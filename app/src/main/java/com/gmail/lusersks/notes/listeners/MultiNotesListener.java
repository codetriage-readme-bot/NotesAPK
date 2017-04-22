package com.gmail.lusersks.notes.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.lusersks.notes.MainActivity;
import com.gmail.lusersks.notes.R;
import com.gmail.lusersks.notes.data.NotesData;
import com.gmail.lusersks.notes.data.SimpleNotesAdapter;

public class MultiNotesListener implements AbsListView.MultiChoiceModeListener {

    private final Context context;
    private final SimpleNotesAdapter adapter;
    private final AppBarLayout mainAppBarLayout;
    private final ListView listView;

    private SparseBooleanArray sbArray;
    private static int checkedCount = 0;

    public MultiNotesListener(Context context) {
        this.sbArray = new SparseBooleanArray();
        this.context = context;

        this.adapter = ((MainActivity) context).simpleNotesAdapter;
        this.mainAppBarLayout = ((MainActivity) context).appBarLayout;
        this.listView = ((MainActivity) context).listView;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//        final int checkedCount = MainActivity.listView.getCheckedItemCount();
        if (!sbArray.get(position)) {
            checkedCount++;
            listView.getChildAt(position).setBackgroundColor(Color.BLUE);
        } else {
            checkedCount--;
            listView.getChildAt(position).setBackgroundColor(Color.WHITE);
        }
        mode.setTitle(checkedCount + " Selected");
        toggleSelection(position);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.multiple_delete, menu);
        mainAppBarLayout.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        final ActionMode innerMode = mode;
        switch (item.getItemId()) {
            case R.id.select_all: {
                checkedCount = NotesData.getItems().size();
                removeSelection();
                for (int i = 0; i < checkedCount; i++) {
                    listView.setItemChecked(i, true);
                }
                mode.setTitle(checkedCount + " Selected");
                return true;
            }
            case R.id.multiple_delete: {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you  want to delete selected record(s)?");
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO  Auto-generated method stub
                    }
                });
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < sbArray.size(); i++) {
                            if (sbArray.valueAt(i)) {
                                String selectedItem = (String) adapter.getItem(sbArray.keyAt(i));
                                NotesData.deleteItem(selectedItem);
                            }
                        }
                        // Close CAB
                        innerMode.finish();
                        sbArray.clear();
                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle("Confirmation");
                alert.show();
                return true;
            }
            default: {
                return false;
            }
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mainAppBarLayout.setVisibility(View.VISIBLE);
        checkedCount = 0;
    }

    private void removeSelection() {
        sbArray = new SparseBooleanArray();
        adapter.notifyDataSetChanged();
    }

    private void toggleSelection(int position) {
        if (!sbArray.get(position)) {
            sbArray.put(position, true);
        } else {
            sbArray.delete(position);
        }
        adapter.notifyDataSetChanged();
    }
}
