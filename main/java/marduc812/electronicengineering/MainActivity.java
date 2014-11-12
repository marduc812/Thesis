package marduc812.electronicengineering;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    String url = "http://www.el.teithe.gr/Announcements.aspx?View=List";
    ProgressDialog mProgressDialog;
    ArrayList<String> AnnounceTitles,AnnounceDesc,AnnounceLinks;
    ArrayList<Integer> AnnounceImages;
    SQLController dbcon;
    String LinkURL,title,desc;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        setTitle("Ανακοινώσεις");
        AnnounceTitles = new ArrayList<String>();
        AnnounceDesc = new ArrayList<String>();
        AnnounceLinks = new ArrayList<String>();
        AnnounceImages = new ArrayList<Integer>();

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean wifiOnly = getPrefs.getBoolean("wifionly",false);

        if (wifiOnly)
        {
            if (mWifi.isConnected()) {
                new MyTask().execute();
            } else
                setContentView(R.layout.emptyview);
        }

        else
        {
            if (isNetworkAvailable())
            {
                new MyTask().execute();
            }

            else
                setContentView(R.layout.emptyview);
        }



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                String LinkURL = AnnounceLinks.get(position);

                Intent i = new Intent(getApplicationContext(), AnnounDisplay.class);
                i.putExtra("anURL",LinkURL);
                startActivity(i);


            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                // TODO Auto-generated method stub

                LinkURL = AnnounceLinks.get(pos);
                title = AnnounceTitles.get(pos);
                desc = AnnounceDesc.get(pos);


                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.helpdialogue);
                dialog.setCancelable(true);
                ImageButton hint = (ImageButton)dialog.findViewById(R.id.imageButton3);
                ImageButton open = (ImageButton)dialog.findViewById(R.id.imageButton);
                ImageButton share = (ImageButton)dialog.findViewById(R.id.imageButton2);
                Button cancel = (Button)dialog.findViewById(R.id.cancelbut);
                dialog.show();

                hint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dbcon = new SQLController(MainActivity.this);
                        dbcon.open();
                        dbcon.insertData(title,LinkURL,desc);
                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(),title + " προστέθηκε στα αγαπημένα",Toast.LENGTH_SHORT).show();

                    }
                });

                open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String linkurl = AnnounceLinks.get(pos);
                        dialog.dismiss();

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(linkurl));
                        startActivity(i);


                    }
                });

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String linkurl = AnnounceLinks.get(pos);
                        dialog.dismiss();

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, linkurl);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);


                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });



                return true;
            }
        });



    }


    private class MyTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {
                // Σύνδεση στην σελίδα
                Document doc = Jsoup.connect(url).get();
                // Επιλογή td με όνομα GridViewRow1
                Elements td = doc.select("td.GridViewRow1");
                Elements links = doc.select("a[href]");
                for (Element link : links)
                {
                    String AnnLink = link.attr("abs:href");

                    //Κρατάει links που περιέχουν μόνο το παρακάτω query
                    if (AnnLink.contains("Announcements.aspx?View=Content&Page"))
                    {
                        AnnounceLinks.add(AnnLink);
                    }

                }

                Elements image = doc.select("img");
                for (Element img : image)
                {
                    String imgSrc = img.attr("src");
                    if (imgSrc.contains("pin"))
                        AnnounceImages.add(R.drawable.pin);
                    else if (imgSrc.contains("announcement"))
                        AnnounceImages.add(R.drawable.note);
                }



                for( Element element : td )
                {

                    String title = element.select("a[href]").text();
                    String extra = element.select("div.SmallText").text();
                    //String imgSrc = element.attr("img");


                    //Ελεγχος για να μην εχω κενά
                    //Χωρις if παιρνει και την εικόνα και αφήνει μία κενή γραμμή για κάθε εικόνα
                    if (title.equals("") || title.equals(" "))
                    {

                    }
                    else

                    {
                        AnnounceTitles.add(title);
                        AnnounceDesc.add(extra);

                    }



                }





            } catch (IOException e) {
                e.printStackTrace();
            }
            return AnnounceTitles;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {



            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);



            AnnounList adapter = new AnnounList(MainActivity.this, AnnounceTitles, AnnounceDesc, AnnounceImages);

            list.setAdapter(adapter);

            mProgressDialog.dismiss();

            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean vibrate = getPrefs.getBoolean("vibr",true);

            if (vibrate)
            v.vibrate(400);
        }


    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.refresh) {

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
