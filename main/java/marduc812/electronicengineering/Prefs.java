package marduc812.electronicengineering;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by marduc on 10/14/14.
 */
public class Prefs extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);



    }


}
