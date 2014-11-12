package marduc812.electronicengineering;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AnnounList extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> AnnouTitle;
    private final ArrayList<String> AnnouDesc;
    private final ArrayList<Integer> AnnouImage;


    public AnnounList(Activity context,
                      ArrayList<String> AnnounTitle, ArrayList<String> AnnouDesc, ArrayList<Integer> AnnouImage) {
        super(context, R.layout.announcementlist, AnnounTitle);
        this.context = context;
        this.AnnouTitle = AnnounTitle;
        this.AnnouDesc = AnnouDesc;
        this.AnnouImage = AnnouImage;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.announcementlist, null, true);
        TextView AnTitle = (TextView) rowView.findViewById(R.id.textView);
        TextView AnDesc = (TextView) rowView.findViewById(R.id.textView2);
        ImageView AnView = (ImageView) rowView.findViewById(R.id.imageView);
        AnTitle.setText(AnnouTitle.get(position));
        AnDesc.setText(AnnouDesc.get(position));
        AnView.setImageResource(AnnouImage.get(position));
        return rowView;
    }
}