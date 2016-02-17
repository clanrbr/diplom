package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import localestates.localestates.R;

/**
 * Created by macbook on 1/27/16.
 */
public class GridViewPropertyFeaturesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CharSequence> titles;
    private ArrayList<CharSequence> items;
    private RelativeLayout inflater;

    public GridViewPropertyFeaturesAdapter(Context context, ArrayList<CharSequence> titles, ArrayList<CharSequence> items) {
        this.context = context;
        this.titles = titles;
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String title = getItemTitle(position).toString();
        String value = getItem(position).toString();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_view_header, parent, false);
        }

        TextView leftCell = (TextView) convertView.findViewById(R.id.textView1);
        leftCell.setText(title);
        TextView rightCell = (TextView) convertView.findViewById(R.id.textView2);
        rightCell.setText(value);

        return convertView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getItem(int position) {
        return items.get(position);
    }

    public CharSequence getItemTitle(int position) {
        return titles.get(position);
    }

//    @Override
//    public Object getItem(int position) {
//        return items.get(position);
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}