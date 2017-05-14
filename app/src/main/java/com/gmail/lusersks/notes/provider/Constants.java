package com.gmail.lusersks.notes.provider;


import android.content.UriMatcher;
import android.net.Uri;

public class Constants {

    // DB
    public static final String NOTES_DB = "notesDB";
    public static final int VERSION_DB = 1;

    public static final String NOTES_TABLE = "notes";

    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_BODY = "body";
    public static final String COL_TYPE = "type";


    // Uri
    public static final String AUTHORITY = "com.gmail.lusersks.notes.noteslist";
    public static final String NOTES_PATH = "notes";

    public static final Uri NOTES_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + NOTES_PATH);

    public static final String NOTES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + NOTES_PATH;
    public static final String NOTES_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + NOTES_PATH;


    // UriMatcher
    public static final int URI_NOTES = 1;
    public static final int URI_NOTES_ID = 2;

    public static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, NOTES_PATH, URI_NOTES);
        uriMatcher.addURI(AUTHORITY, NOTES_PATH + "/#", URI_NOTES_ID);
    }


    private Constants() {
    }
}
