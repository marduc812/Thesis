package marduc812.electronicengineering;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
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
    TextView  memID_tv;
    String ids;
    long idz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favs);
        setTitle("Αγαπημένα");


        dbcontrol();






        favlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                TextView textviewurl = (TextView) findViewById(R.id.link);
                String LinkURL = textviewurl.getText().toString();

                Intent i = new Intent(FavoritesActivity.this, AnnounDisplay.class);
                i.putExtra("anURL", LinkURL);
                startActivity(i);




            }
        });

        favlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                // TODO Auto-generated method stub

                memID_tv = (TextView) arg1.findViewById(R.id.member_id);

                //delete confirmation

                final Dialog dialog = new Dialog(FavoritesActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.deletedialogue);
                dialog.setCancelable(true);
                Button del = (Button)dialog.findViewById(R.id.button3);
                Button cancel = (Button)dialog.findViewById(R.id.cancelbut);
                dialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();


                        ids = memID_tv.getText().toString();
                        idz = Long.parseLong(ids);

                        dbcon.deleteData(idz);

                        Toast.makeText(getApplicationContext(),"Αφαιρέθηκε από τα αγαπημένα",Toast.LENGTH_SHORT).show();
                        dbcontrol();
                    }
                });




                return true;
            }
        });


    }

    private void dbcontrol()
    {
        dbcon = new SQLController(this);
        dbcon.open();


        Cursor cursor = dbcon.readData();

        final String[] from = new String[] { DBhelper.UID, DBhelper.NAME, DBhelper.LINK, DBhelper.DESCR, DBhelper.CATE };

        int[] to = new int[] { R.id.member_id, R.id.category_help,R.id.link, R.id.desctv, R.id.categ };

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
