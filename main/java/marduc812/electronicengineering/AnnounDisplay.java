package marduc812.electronicengineering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by marduc on 10/9/14.
 */
public class AnnounDisplay extends ActionBarActivity {

    String linkURL;
    TextView title,infos,body;
    ProgressDialog mProgressDialog;
    String AnnTitle;
    String AnnBody;
    String AnnInfos;
    ListView list;
    ArrayList<String> AnnAtt,AnnAttTitle;
    boolean visible;
    ArrayAdapterAttach arrayAdapter;
    ArrayList<Integer> images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcemedisp);



        visible=false;

        AnnAtt = new ArrayList<String>();
        AnnAttTitle = new ArrayList<String>();
        images = new ArrayList<Integer>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            linkURL = extras.getString("anURL");

        }


        setTitle("Ανακοίνωση");

        title = (TextView) findViewById(R.id.textView);
        infos = (TextView) findViewById(R.id.textView2);
        body = (TextView) findViewById(R.id.textView3);
        list = (ListView) findViewById(R.id.listView);

        body.setMovementMethod(new ScrollingMovementMethod());

        new MyTask().execute();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                String LinkURL = AnnAtt.get(position);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(LinkURL));
                startActivity(browserIntent);


            }
        });



    }


    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(AnnounDisplay.this);
            mProgressDialog.setTitle("Dowloading data");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Σύνδεση στην σελίδα
                Document doc = Jsoup.connect(linkURL).get();

                // Επιλογή td με όνομα GridViewRow1
                Elements td = doc.select("div.PT");
                AnnInfos = doc.select("div.SmallText").text();
                Element price = doc.select("div.Text").get(4);
                String bodyz = price.toString();
                String bodies = br2nl(bodyz); // antikathistw <br> me \n
                AnnBody = bodies.replace("&nbsp;"," "); // afairw ta &nbsp; tags ta kena



                Elements links = doc.select("a[href]");
                for (Element link : links)
                {
                    String AnnAtts = link.attr("abs:href");

                    String AnnLinkTitle = link.select("a[href]").text();

                    //Κρατάει links που περιέχουν μόνο το παρακάτω query
                    if (AnnAtts.contains("File=ANNOUNCEMENTS"))
                    {
                        AnnAtt.add(AnnAtts);
                        AnnAttTitle.add(AnnLinkTitle);
                        images.add(R.drawable.ic_action_download);
                        Log.d("links",AnnAtts);
                    }

                }




                AnnTitle = td.text();





            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView



            arrayAdapter = new ArrayAdapterAttach (AnnounDisplay.this, AnnAttTitle,images);

            list.setAdapter(arrayAdapter);

            title.setText(AnnTitle);
            infos.setText(AnnInfos);
            body.setText(AnnBody);
            mProgressDialog.dismiss();
        }


    }

    public static String br2nl(String html) {
        if(html==null)
            return html;
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        String s = document.html().replaceAll("\\\\n", "\n");
        return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inpostmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.attach) {



            if (visible)
            {
                list.setVisibility(View.GONE);
                visible=false;

            }

            else
            {

                list.setVisibility(View.VISIBLE);
                visible=true;
            }

            return true;
        }
        else if (id == R.id.fullview) {

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(linkURL));
            startActivity(i);

            return true;
        }
        else if (id== R.id.share)
        {

        }
        return super.onOptionsItemSelected(item);

    }

}
