package com.hansung.android.ai_speaker_socket;

import android.provider.BaseColumns;

public final class DBContact {
    public static final String DB_NAME="photo.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private DBContact() {}

    public static class DBcontact implements BaseColumns {
        public static final String Member = "MemberPhotos";
        public static final String MEMBER_NAME = "Name";
        public static final String KEY_IMAGE = "Image";

        public static final String CREATE_TABLE = "CREATE TABLE " + Member + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                MEMBER_NAME + TEXT_TYPE + COMMA_SEP +
                KEY_IMAGE+ TEXT_TYPE + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + Member;
    }


}
