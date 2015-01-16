package marduc812.electronicengineering;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by marduc on 10/29/14.
 */
public class About extends Activity {

    ArrayList<String> titles,descs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        ListView ls = (ListView)findViewById(R.id.listView2);

        LoadAbout();

        ArrayAdapterAbout adapter = new ArrayAdapterAbout(About.this, titles,descs);

        ls.setAdapter(adapter);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {


                View toolbar = view.findViewById(R.id.hiddenlay);

                // Creating the expand animation for the item
                ExpandAnimation expandAni = new ExpandAnimation(toolbar, 100);

                // Start the animation on the toolbar
                toolbar.startAnimation(expandAni);


            }
        });

        Button git = (Button) findViewById(R.id.button4);

        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/marduc812/Thesis"));
                startActivity(browserIntent);
            }
        });




    }


    public void LoadAbout() {

        titles = new ArrayList<String>();
        titles.add("Ανακοινώσεις");
        titles.add("Νεα");
        titles.add("Πρόγραμμα");
        titles.add("Αρχεία");
        titles.add("Καιρός");
        titles.add("Webcam");
        titles.add("Αγαπημένα");
        titles.add("Γενικά");

        descs = new ArrayList<String>();
        descs.add(getResources().getString(R.string.anakoinoseis));
        descs.add(getResources().getString(R.string.nea));
        descs.add(getResources().getString(R.string.programma));
        descs.add(getResources().getString(R.string.Arxeia));
        descs.add(getResources().getString(R.string.kairoshelp));
        descs.add(getResources().getString(R.string.webcam));
        descs.add(getResources().getString(R.string.agapimena));
        descs.add(getResources().getString(R.string.genika));

    }
}
