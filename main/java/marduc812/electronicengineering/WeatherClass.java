package marduc812.electronicengineering;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import marduc812.electronicengineering.model.Weather;

/**
 * Created by marduc on 10/27/14.
 */
public class WeatherClass extends Activity {

    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    ProgressDialog mProgressDialog;

    private TextView hum;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String location = getPrefs.getString("locationweat","1");


        String city = "Sindos,%20GR";

        setTitle("Καιρός ");

        cityText = (TextView) findViewById(R.id.cityText);
        condDescr = (TextView) findViewById(R.id.condDescr);
        temp = (TextView) findViewById(R.id.temp);
        hum = (TextView) findViewById(R.id.hum);
        press = (TextView) findViewById(R.id.press);
        windSpeed = (TextView) findViewById(R.id.windSpeed);
        windDeg = (TextView) findViewById(R.id.windDeg);
        imgView = (ImageView) findViewById(R.id.condIcon);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});
    }




    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(WeatherClass.this);
            mProgressDialog.setTitle("Dowloading data");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JsonWeatherHandle.getWeather(data);

                // Let's retrieve the icon
                weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }




        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);


            String weatherevent = weather.currentCondition.getDescr();

            if (weatherevent.contains("rain"))
                imgView.setImageDrawable(getResources().getDrawable(R.drawable.rain));
            else if (weatherevent.contains("snow"))
                imgView.setImageDrawable(getResources().getDrawable(R.drawable.snow));
            else if (weatherevent.contains("thunder"))
                imgView.setImageDrawable(getResources().getDrawable(R.drawable.thunderstorm));
            else if (weatherevent.contains("mist"))
                imgView.setImageDrawable(getResources().getDrawable(R.drawable.mist));
            else if (weatherevent.contains("clouds"))
                imgView.setImageDrawable(getResources().getDrawable(R.drawable.few_clouds));
            else
                imgView.setImageDrawable(getResources().getDrawable(R.drawable.clear_sky));


            mProgressDialog.dismiss();
            cityText.setText(weather.location.getCity());
            condDescr.setText(weather.currentCondition.getCondition() + "\n" + weatherevent );
            temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            press.setText("" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            windDeg.setText("" + Math.round(weather.wind.getDeg()) + "°");

            //+ "(" + weather.currentCondition.getDescr() + ")"
        }







    }
}