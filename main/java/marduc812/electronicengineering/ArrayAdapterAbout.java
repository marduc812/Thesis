package marduc812.electronicengineering;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by marduc on 10/10/14.
 */
public class ArrayAdapterAbout extends ArrayAdapter<String> {

private final Activity context;
private final ArrayList<String> Title,Desc;


    public ArrayAdapterAbout(Activity context,
                             ArrayList<String> Title, ArrayList<String> Desc) {
        super(context, R.layout.view_items, Title);
        this.context = context;
        this.Title = Title;
        this.Desc = Desc;

        }
@Override
public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.view_items, null, true);
        TextView AbTitle = (TextView) rowView.findViewById(R.id.category_help);
        TextView AbDesc = (TextView) rowView.findViewById(R.id.desctv);
        AbTitle.setText(Title.get(position));
        AbDesc.setText(Desc.get(position));
        return rowView;
        }
        }