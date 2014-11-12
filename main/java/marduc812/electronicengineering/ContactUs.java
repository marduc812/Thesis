package marduc812.electronicengineering;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marduc on 10/13/14.
 */
public class ContactUs extends Activity {


    ArrayList<String> title;
    ArrayList<Integer> images;
    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);

        setTitle("Επικοινωνία");

        additems();

        list = (ListView) findViewById(R.id.listView);
        ArrayAdapteridis adapter = new ArrayAdapteridis(ContactUs.this, title , images );

        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                if (position==0)
                {

                    double latitude = 40.655359;
                    double longitude = 22.805868;
                    String label = "ΤΕΙ Ηλεκτρονικής";
                    String uriBegin = "geo:" + latitude + "," + longitude;
                    String query = latitude + "," + longitude + "(" + label + ")";
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);

                    PackageManager manager = ContactUs.this.getPackageManager();
                    List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
                    if (infos.size() > 0) {

                        startActivity(intent);

                    }else{
                        Toast.makeText(getApplicationContext(), "Χρειάζεστε εφαρμογή maps για να δείτε την τοποθεσία", Toast.LENGTH_SHORT).show();
                    }

                }
                else if (position==1)
                {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:2310013621"));
                        startActivity(callIntent);
                    } catch (ActivityNotFoundException activityException) {

                    }
                }
                else if (position==3)
                {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto","infoel@teithe.gr", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Χρήση εφαρμογής Android");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
                else if (position==4)
                {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/564859026860591"));
                        startActivity(intent);
                    } catch(Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ElectronicsThessaloniki")));
                    }
                }




            }
        });

    }

    private void additems() {

        title = new ArrayList<String>();

        title.add("Τ.Θ. 141, 57400 Θεσσαλονίκη");
        title.add("2310 013 621");
        title.add("2310 791 132");
        title.add("infoel@teithe.gr");
        title.add("Facebook");

        images = new ArrayList<Integer>();

        images.add(R.drawable.ho);
        images.add(R.drawable.pho);
        images.add(R.drawable.fa);
        images.add(R.drawable.mai);
        images.add(R.drawable.fb);

    }




    }

