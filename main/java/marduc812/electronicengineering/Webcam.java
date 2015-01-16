package marduc812.electronicengineering;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by marduc on 11/12/14.
 */
public class Webcam extends ActionBarActivity {

    Handler mHandler = new Handler();
    String webcamurl;
    long dataused;
    TextView data, rrate,imagesize,imgresol;
    Button save, change;
    Bitmap finalBitmap;
    int imgsize,imgwid,imghe;
    ImageView img;
    boolean webnum;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webcam);

        save = (Button) findViewById(R.id.button);
        change = (Button) findViewById(R.id.button2);
        img = (ImageView) findViewById(R.id.imageView6);

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String speed = getPrefs.getString("webref", "2500");
        final int speeddata = Integer.valueOf(speed);
        webcamurl = "http://www.el.teithe.gr/webcamimg/Camera2.jpg?1415795080660";

        dataused = 0;
        webnum = true;
        data = (TextView) findViewById(R.id.textView);
        rrate = (TextView) findViewById(R.id.textView5);
        imagesize = (TextView) findViewById(R.id.textView6);
        imgresol = (TextView) findViewById(R.id.textView7);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(speeddata);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                // maxed


                                new DownloadImageTask((ImageView) findViewById(R.id.imageView6)).execute(webcamurl);

                                data.setText("Data used: " + byteconv(dataused, true));
                                rrate.setText("Refresh rattio: " + speeddata+ "ms");
                                imagesize.setText("Image Size: "+imgsize+" bytes");
                                imgresol.setText("Image Resolution: "+imgwid+"x"+imghe);


                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                Bitmap combination = ((BitmapDrawable) img.getDrawable()).getBitmap();
                storeImage(combination,timeStamp+".png");
                Toast.makeText(getApplicationContext(),"Image image_"+timeStamp+".png"+" got saved in "+ Environment.getExternalStorageDirectory() + "/ElectronicEngi",Toast.LENGTH_SHORT).show();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!webnum) {
                    webcamurl = "http://www.el.teithe.gr/webcamimg/Camera2.jpg?1415795080660";
                    webnum = true;
                    change.setText("Webcam:2");
                } else {
                    webcamurl = "http://www.el.teithe.gr/webcamimg/Camera3.jpg?1415806035575";
                    webnum = false;
                    change.setText("Webcam:1");
                }
            }
        });


    }

    public static String byteconv(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            dataused += sizeOf(result);
            imgsize = sizeOf(result);
            imgwid = result.getWidth();
            imghe = result.getHeight();

        }
    }

    protected int sizeOf(Bitmap data) {
        return data.getRowBytes() * data.getHeight() / 28;
    }

    private boolean storeImage(Bitmap imageData, String filename) {
        //get path to external storage (SD card)
        String iconsStoragePath = Environment.getExternalStorageDirectory() + "/ElectronicEngi/Webcam/";
        File sdIconStorageDir = new File(iconsStoragePath);

        //create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();

        try {
            filePath = sdIconStorageDir.toString() + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

            //choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.PNG, 100, bos);

            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        }

        return true;
    }
}

