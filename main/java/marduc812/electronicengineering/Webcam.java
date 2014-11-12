package marduc812.electronicengineering;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by marduc on 11/12/14.
 */
public class Webcam extends ActionBarActivity {

    Handler mHandler = new Handler();
    String webcamurl;
    long dataused;
    TextView data;
    Button save,change;
    Bitmap finalBitmap;
    ImageView img;
    boolean webnum;

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
        webcamurl= "http://www.el.teithe.gr/webcamimg/Camera2.jpg?1415795080660";

        dataused=0;
        webnum=true;
        data = (TextView) findViewById(R.id.textView);

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
                                data.setText(dataused+" \nrefresh rate: " + speeddata);

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
                /*Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                addImageToGallery(Webcam.this,bitmap,"image.jpg","desc.jpg");
                */
                saveImage();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                addImageToGallery(Webcam.this,bitmap,"image.jpg","desc.jpg");
                */
                saveImage();
                if (!webnum)
                {
                    webcamurl = "http://www.el.teithe.gr/webcamimg/Camera2.jpg?1415795080660";
                    webnum=true;
                    change.setText("Webcam:2");
                }
                else
                {
                    webcamurl = "http://www.el.teithe.gr/webcamimg/Camera3.jpg?1415806035575";
                    webnum=false;
                    change.setText("Webcam:1");
                }
            }
        });



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
            dataused+=sizeOf(result);

        }
    }
    protected int sizeOf(Bitmap data)
        {
            return data.getRowBytes() * data.getHeight()/28;
        }

    public static Uri addImageToGallery(Context context, String filepath, String title, String description) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filepath);

        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private void saveImage() {

        File myDir=new File("/sdcard/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File(myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

