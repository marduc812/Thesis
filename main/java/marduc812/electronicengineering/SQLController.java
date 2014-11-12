package marduc812.electronicengineering;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by marduc on 9/4/14.
 */
public class SQLController {

    private DBhelper dbhelper;
    private Context context;
    private SQLiteDatabase db;

    public SQLController(Context c) {
        context = c;
    }

    public SQLController open() throws SQLException {
        dbhelper = new DBhelper(context);
        db = dbhelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbhelper.close();
    }


    public void insertData(String name, String link, String desc) {

        ContentValues cv = new ContentValues();
        cv.put(DBhelper.NAME, name);
        cv.put(DBhelper.LINK, link);
        cv.put(DBhelper.DESCR,desc);
        db.insert(DBhelper.TABLE_NAME, null, cv);
    }

    /*
    public int updateName(long memberID, String ItemName) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(DBhelper.NAME, ItemName);
        int i = db.update(DBhelper.TABLE_NAME, cvUpdate,
                DBhelper.UID + " = " + memberID, null);
        return i;
    }

    public int updatePrice(long priceID, String priceName) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(DBhelper.PRICE, priceName);
        int i = db.update(DBhelper.TABLE_NAME, cvUpdate,
                DBhelper.UID + " = " + priceID, null);
        return i;
    }

    public int updateDesc(long descID, String descName) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(DBhelper.DESCR, descName);
        int i = db.update(DBhelper.TABLE_NAME, cvUpdate,
                DBhelper.UID + " = " + descID, null);
        return i;
    }

    public int updateKind(long kindID, String kindName) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(DBhelper.KIND, kindName);
        int i = db.update(DBhelper.TABLE_NAME, cvUpdate,
                DBhelper.UID + " = " + kindID, null);
        return i;
    }*/

    public void deleteData(long memberID) {
        db.delete(DBhelper.TABLE_NAME, DBhelper.UID + "="
                + memberID, null);
    }

    public Cursor readData() {
        String[] allColumns = new String[] { DBhelper.UID,DBhelper.NAME, DBhelper.LINK, DBhelper.DESCR};
        Cursor c = db.query(DBhelper.TABLE_NAME, allColumns, null,null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor readKind(String itemKind) {
        String[] allColumns = new String[] { DBhelper.UID,DBhelper.NAME, DBhelper.LINK, DBhelper.DESCR };
        Cursor c = db.query(DBhelper.TABLE_NAME, allColumns, DBhelper.LINK+ " like '%" + itemKind + "%'",null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor readName(String ItemName)
    {
        Cursor c = null;

        String[] allColumns = new String[]{DBhelper.UID, DBhelper.NAME, DBhelper.LINK, DBhelper.DESCR};

        if (ItemName == null  ||  ItemName.length () == 0)
        {
            c = db.query(DBhelper.TABLE_NAME, allColumns, null,null, null, null, null);
            if (c != null) {
                c.moveToFirst();
            }

        }

        else{

         c= db.query(DBhelper.TABLE_NAME, allColumns, DBhelper.NAME + " like '%" + ItemName + "%'", null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        }
        return c;
    }




}
