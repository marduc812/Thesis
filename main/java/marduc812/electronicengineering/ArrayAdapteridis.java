package marduc812.electronicengineering;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by marduc on 10/10/14.
 */
public class ArrayAdapteridis extends ArrayAdapter<String> {

private final Activity context;
private final ArrayList<String> AnnouTitle;
private final ArrayList<Integer> AnnouImage;


    public ArrayAdapteridis(Activity context,
                            ArrayList<String> AnnounTitle,  ArrayList<Integer> AnnouImage) {
        super(context, R.layout.foodlay, AnnounTitle);
        this.context = context;
        this.AnnouTitle = AnnounTitle;
        this.AnnouImage = AnnouImage;

        }
@Override
public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.foodlay, null, true);
        TextView AnTitle = (TextView) rowView.findViewById(R.id.textView);
        ImageView AnView = (ImageView) rowView.findViewById(R.id.imageView);
        AnTitle.setText(AnnouTitle.get(position));
        AnView.setImageResource(AnnouImage.get(position));
        return rowView;
        }
        }