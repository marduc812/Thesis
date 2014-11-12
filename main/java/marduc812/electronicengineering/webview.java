package marduc812.electronicengineering;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by marduc on 10/14/14.
 */
public class webview extends Activity {

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("anURL");
        }

        WebView wv = (WebView) findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(url);
    }
}
