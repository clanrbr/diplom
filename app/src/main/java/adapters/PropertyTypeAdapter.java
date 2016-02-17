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
import utils.HelpFunctions;

/**
 * Created by Ado on 12/26/2015.
 */
public class PropertyTypeAdapter extends ArrayAdapter<String> {

    private int textView;
    private ArrayList<String> data;
    private TextView propertyTypeCheckbox;
    private TextView propertyTitle;
    private LinearLayout propertyTypeWholeLane;
    private ArrayList<String> selectedValues;
    Context context;
    int position=0;
//    ArrayList<String> checkboxSelected;

    public PropertyTypeAdapter(Context context, int resource,
                                  ArrayList<String> data,ArrayList<String> selectedStartValues) {
        super(context, resource, data);
        this.context=context;

        Log.e("HEREHERE","rebuild");

        this.data = data;
        this.selectedValues=selectedStartValues;
        if (this.selectedValues==null) {
            this.selectedValues=new ArrayList<String>();
        }
    }

    public ArrayList<String> returnSelectedFields() {
        return selectedValues;
    }

    public void clearSelectedFields() {
        selectedValues=new ArrayList<String>();
    }

    public void updateResults() {
        // assign the new result list to your existing list it will work
        Log.e("HEREHERE","GOES HERE");
        notifyDataSetChanged();
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

        if ( selectedValues.indexOf(type)>-1 ) {
            propertyTypeCheckbox.setText(context.getString(R.string.material_icon_checked_full));
        } else {
            propertyTypeCheckbox.setText(context.getString(R.string.material_icon_check_empty));
            convertView.setEnabled(HelpFunctions.checkGroupOfPropertyType(type,selectedValues));
        }

        propertyTypeWholeLane = (LinearLayout) convertView.findViewById(R.id.whole_lane);
        propertyTypeWholeLane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = (View)v.getParent();
                if ( parent!=null ) {
                    TextView propertyTypeCheckbox = (TextView) parent.findViewById(R.id.fragment_property_type_checkbox);
                    TextView  propertyTitle = (TextView) parent.findViewById(R.id.type_of_property_single_line);

                    if ( position==0) {
                        selectedValues=new ArrayList<String>();
                    }

                    if ( parent.isEnabled() ) {
                        if ( selectedValues.indexOf(propertyTitle.getText())>-1 ) {
                            propertyTypeCheckbox.setText(context.getString(R.string.material_icon_check_empty));
                            selectedValues.remove(propertyTitle.getText());
                        } else {
                            propertyTypeCheckbox.setText(context.getString(R.string.material_icon_checked_full));
                            if ( (selectedValues.size()>0) && selectedValues.get(0).equals("ВСИЧКИ")) {
                                selectedValues.remove(0);
                            }
                            selectedValues.add(propertyTitle.getText().toString());
                        }
                    } else {
                        if ( propertyTypeCheckbox.getText()==context.getString(R.string.material_icon_checked_full) ) {
                            propertyTypeCheckbox.setText(context.getString(R.string.material_icon_check_empty));
                            selectedValues.remove(propertyTitle.getText());
                        }
                    }

                    updateResults();
                }
            }
        });

        return convertView;
    }
}