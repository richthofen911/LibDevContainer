package net.callofdroidy.libdevcontainer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 15/12/15.
 * This class can be used when some basic database conditions need to be involved when testing a lib
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String CREATE_NEWS = "create table news ("
            + "id integer primary key autoincrement, "
            + "title text, "
            + "content text)";

    private static final String ADD_A_Field_WHEN_UPGRADE_TO_VERSION_2 = "alter table news add time text;";

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "TestDB";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS);
        //db.execSQL(CREATE_COMMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == 1 && newVersion == 2)
            db.execSQL(ADD_A_Field_WHEN_UPGRADE_TO_VERSION_2);
    }
}
