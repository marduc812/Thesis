package marduc812.electronicengineering;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marduc on 4/11/14.
 */
public class DBhelper extends SQLiteOpenHelper {

    // TABLE INFORMATTION
    public static final String TABLE_NAME = "FavoritesDB";
    public static final String UID = "_id";
    public static final String NAME = "name";
    public static final String LINK = "link";
    public static final String DESCR = "desc";


    // DATABASE INFORMATION
    static final String DB_NAME = "MEMBER.DB";
    static final int DB_VERSION = 3;

    // TABLE CREATION STATEMENT
    private static final String CREATE_TABLE = "create table "
            + TABLE_NAME + "(" + UID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " TEXT NOT NULL, "
            + LINK + " TEXT NOT NULL, "
            + DESCR + " TEXT NOT NULL);";

    public DBhelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}