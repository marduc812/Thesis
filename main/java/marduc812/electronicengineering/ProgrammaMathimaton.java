package marduc812.electronicengineering;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by marduc on 10/13/14.
 */
public class ProgrammaMathimaton extends Activity {


    String linkURL;
    ProgressDialog mProgressDialog;
    String title;
    String urls;
    Button dl,cancel;
    RadioGroup rg;
    RadioButton rb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.programmamathimaton);

        dl = (Button) findViewById(R.id.download);
        cancel = (Button) findViewById(R.id.cancel);
        rg = (RadioGroup) findViewById(R.id.downs);
        setTitle("Πρόγραμμα");


        linkURL= "http://www.el.teithe.gr/";
        title=null;

        dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MyTask().execute();


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });








    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ProgrammaMathimaton.this);
            mProgressDialog.setTitle("Κατέβασμα Προγράμματος");
            mProgressDialog.setMessage("Παρακαλώ περιμένετε");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document doc = Jsoup.connect(linkURL).get();



                Elements links = doc.select("a[href]");
                for (Element link : links)
                {
                    String AnnLink = link.attr("abs:href");
                    String title = link.select("a[href]").text();

                    // get selected radio button from radioGroup
                    int selectedId = rg.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    rb = (RadioButton) findViewById(selectedId);

                    if (selectedId == R.id.mathimaton)
                    {
                        if (title .equals("Πρόγραμμα μαθημάτων"))
                        {
                            urls = AnnLink;
                        }

                    }

                    else if (selectedId == R.id.iouniou)
                    {
                        if (title .contains("Πρόγραμμα εξετάσεων Ιουνίου"))
                        {
                            urls = AnnLink;
                        }
                    }

                    else if (selectedId == R.id.fevrouariou)
                    {
                        if (title .contains("Πρόγραμμα εξετάσεων Φεβ"))
                        {
                            urls = AnnLink;
                        }
                    }

                    else if (selectedId == R.id.septemvriou)
                    {
                        if (title .contains("Πρόγραμμα εξετάσεων Σεπτ"))
                        {
                            urls = AnnLink;
                        }
                    }



                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
            startActivity(browserIntent);

            mProgressDialog.dismiss();
        }


    }

}
