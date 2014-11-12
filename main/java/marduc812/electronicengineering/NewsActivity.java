package marduc812.electronicengineering;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by marduc on 10/14/14.
 */
public class NewsActivity extends ActionBarActivity {

    String url = "http://www.el.teithe.gr/News.aspx?View=List";
    ProgressDialog mProgressDialog;
    ArrayList<String> AnnounceTitles,AnnounceDesc,AnnounceLinks;
    ArrayList<Integer> AnnounceImages;


    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        setTitle("Νέα");
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

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(LinkURL));
                startActivity(i);




            }
        });

    }


    private class MyTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(NewsActivity.this);
            mProgressDialog.setTitle("Dowloading data");
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
                    if (AnnLink.contains("News.aspx?View=Content&Page"))
                    {
                        AnnounceLinks.add(AnnLink);
                    }

                }

                Elements image = doc.select("img");
                for (Element img : image)
                {
                    AnnounceImages.add(R.drawable.newsicon);

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

            AnnounList adapter = new AnnounList(NewsActivity.this, AnnounceTitles, AnnounceDesc, AnnounceImages);

            list.setAdapter(adapter);

            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean vibrate = getPrefs.getBoolean("vibr",true);

            if (vibrate)
                v.vibrate(400);

            mProgressDialog.dismiss();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
