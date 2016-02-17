package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import localestates.localestates.R;

/**
 * Created by Ado on 1/6/2016.
 */
public class SimpleMultiChoiceAdapter extends ArrayAdapter<String> {

    private int textView;
    private ArrayList<String> data;
    private TextView propertyTypeCheckbox;
    private TextView propertyTitle;
    private LinearLayout propertyTypeWholeLane;
    private ArrayList<String> selectedValues;
    Context context;
    int position=0;

    public SimpleMultiChoiceAdapter(Context context, int resource,
                               ArrayList<String> data,ArrayList<String> selectedStartValues) {
        super(context, resource, data);
        this.context=context;
        this.data = data;


        this.selectedValues=selectedStartValues;
        if (this.selectedValues==null) {
            this.selectedValues=new ArrayList<String>();
        }
    }

    public void updateResults() {
        // assign the new result list to your existing list it will work
        notifyDataSetChanged();
    }

    public ArrayList<String> returnSelectedFields() {
        return selectedValues;
    }

    public void clearSelectedFields() {
        this.selectedValues=new ArrayList<String>();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String type = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.propertytype_single_item, parent, false);
        }

        propertyTitle = (TextView) convertView.findViewById(R.id.type_of_property_single_line);
        propertyTitle.setText(type);

        propertyTypeCheckbox = (TextView) convertView.findViewById(R.id.fragment_property_type_checkbox);
        if ( this.selectedValues!=null  ) {
            Log.e("HEREHERE",selectedValues.toString());
        }

        if ( selectedValues.indexOf(type)>-1 ) {
            propertyTypeCheckbox.setText(context.getString(R.string.material_icon_checked_full));
        } else {
            propertyTypeCheckbox.setText(context.getString(R.string.material_icon_check_empty));
        }

        propertyTypeWholeLane = (LinearLayout) convertView.findViewById(R.id.whole_lane);
        propertyTypeWholeLane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = (View)v.getParent();
                if ( parent!=null ) {
                    TextView propertyTypeCheckbox = (TextView) parent.findViewById(R.id.fragment_property_type_checkbox);
                    TextView  propertyTitle = (TextView) parent.findViewById(R.id.type_of_property_single_line);

                    if ( selectedValues.indexOf(propertyTitle.getText())>-1 ) {
                        propertyTypeCheckbox.setText(context.getString(R.string.material_icon_check_empty));
                        selectedValues.remove(propertyTitle.getText());
                    } else {
                        propertyTypeCheckbox.setText(context.getString(R.string.material_icon_checked_full));
                        selectedValues.add(propertyTitle.getText().toString());
                    }
                }
            }
        });

        return convertView;

    }
}
