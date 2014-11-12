package marduc812.electronicengineering;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by marduc on 10/10/14.
 */
public class MainMenu extends ActionBarActivity{

    ArrayList<String> title;
    ArrayList<Integer> icon;
    ListView list;
    ArrayAdapteridis arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        title = new ArrayList<String>();
        icon = new ArrayList<Integer>();

        createMenu();

        list = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapteridis (MainMenu.this, title , icon);

        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                if (position==0)
                {
                    Intent i = new Intent(MainMenu.this,MainActivity.class);
                    startActivity(i);
                }
                else if (position==1)
                {
                    Intent i = new Intent(MainMenu.this,NewsActivity.class);
                    startActivity(i);
                }
                else if (position==2)
                {
                    Intent i = new Intent(MainMenu.this,ProgrammaMathimaton.class);
                    startActivity(i);
                }
                else if (position==3)
                {
                    Intent i = new Intent(MainMenu.this,Arxeia.class);
                    startActivity(i);
                }
                else if (position==4)
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("http://pithia.teithe.gr/unistudent/"));
                    startActivity(i);
                }
                else if (position==5)
                {
                    Intent i = new Intent(MainMenu.this,Webcam.class);
                    startActivity(i);
                }
                else if (position==6)
                {
                    Intent i = new Intent(MainMenu.this,Prefs.class);
                    startActivity(i);
                }
                else if (position==7)
                {
                    Intent i = new Intent(MainMenu.this,ContactUs.class);
                    startActivity(i);
                }
                else if (position == 8)
                {
                    Intent i = new Intent(MainMenu.this,About.class);
                    startActivity(i);
                }
                else if (position == 9)
                {
                    Intent i = new Intent(MainMenu.this,FavoritesActivity.class);
                    startActivity(i);
                }
                else  if (position==10)
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("http://www.el.teithe.gr/"));
                    startActivity(i);
                }



            }
        });


    }

    private void createMenu() {

        title.add("Ανακοινώσεις");
        title.add("Νέα");
        title.add("Πρόγραμμα");
        title.add("Αρχεία");
        title.add("ΠΥΘΙΑ");
        title.add("Καιρός");
        title.add("Ρυθμίσεις");
        title.add("Επικοινωνία");
        title.add("Πληροφορίες");
        title.add("Αγαπημένα");
        title.add("Πλήρης Έκδοση");


        icon.add(R.drawable.ic_announcement_grey600_48dp);
        icon.add(R.drawable.ic_new_releases_grey600_48dp);
        icon.add(R.drawable.ic_event_available_grey600_48dp);
        icon.add(R.drawable.ic_file_download_grey600_48dp);
        icon.add(R.drawable.ic_web_grey600_48dp);
        icon.add(R.drawable.ic_wb_sunny_grey600_48dp);
        icon.add(R.drawable.ic_settings_applications_grey600_48dp);
        icon.add(R.drawable.ic_perm_contact_cal_grey600_48dp);
        icon.add(R.drawable.ic_info_outline_grey600_48dp);
        icon.add(R.drawable.ic_favorite_grey600_48dp);
        icon.add(R.drawable.ic_computer_grey600_48dp);
    }





}
