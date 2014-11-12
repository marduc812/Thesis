package marduc812.electronicengineering;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by marduc on 11/4/14.
 */
public class FavoritesActivity extends ActionBarActivity {

    SQLController dbcon;
    ListView favlist;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favs);


        dbcontrol();



        /*if(cursor.getCount() == 0)
        {
            setContentView(R.layout.emptyviewfavs);
        }
        else
            setContentView(R.layout.favs);*/




        favlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                TextView textviewurl = (TextView) findViewById(R.id.link);
                String LinkURL = textviewurl.getText().toString();

                Intent i = new Intent(FavoritesActivity.this,AnnounDisplay.class);
                i.putExtra("anURL",LinkURL);
                startActivity(i);


            }
        });


    }

    private void dbcontrol()
    {
        dbcon = new SQLController(this);
        dbcon.open();


        Cursor cursor = dbcon.readData();

        final String[] from = new String[] { DBhelper.UID, DBhelper.NAME, DBhelper.LINK, DBhelper.DESCR };

        int[] to = new int[] { R.id.member_id, R.id.member_name,R.id.link, R.id.desctv };

        adapter = new SimpleCursorAdapter(
                FavoritesActivity.this, R.layout.view_fav_entry, cursor, from, to);
        favlist = (ListView) findViewById(R.id.listView);

        adapter.notifyDataSetChanged();
        favlist.setAdapter(adapter);
    }

    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        DBhelper dbh = new DBhelper(FavoritesActivity.this);
        SQLiteDatabase db = dbh.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(dbh.TABLE_NAME, null, null);
        dbcontrol();
        Toast.makeText(getApplicationContext(),"Τα αγαπημένα άδειασαν",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.deletedb) {
            removeAll();


            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
