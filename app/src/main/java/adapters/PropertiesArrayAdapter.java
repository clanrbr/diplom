package adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import localestates.localestates.AdvertActivity;
import localestates.localestates.R;

/**
 * Created by Ado on 11/30/2015.
 */
public class PropertiesArrayAdapter extends ArrayAdapter<JSONObject> {

    private int textView;
    private ArrayList<JSONObject> data;
    Context context;

    public PropertiesArrayAdapter(Context context, int resource,
                           ArrayList<JSONObject> data) {
        super(context, resource, data);

        this.context=context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final JSONObject property = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.property_single_item, parent, false);
        }

        TextView propertyTitle = (TextView) convertView.findViewById(R.id.propertyTitle);
        TextView propertyCity = (TextView) convertView.findViewById(R.id.propertyCity);
        TextView propertyNeighborhood = (TextView) convertView.findViewById(R.id.propertyNeighborhood);
//        TextView propertyStreet = (TextView) convertView.findViewById(R.id.propertyStreet);
        TextView propertySize = (TextView) convertView.findViewById(R.id.propertySize);
        TextView propertyPrice = (TextView) convertView.findViewById(R.id.propertyPrice);

        ImageView bigImage = (ImageView) convertView.findViewById(R.id.bigImage);
        ImageView advertImage2 = (ImageView) convertView.findViewById(R.id.advertImage2);
        ImageView advertImage3 = (ImageView) convertView.findViewById(R.id.advertImage3);
        ImageView advertImage4 = (ImageView) convertView.findViewById(R.id.advertImage4);

        TextView openAdvert = (TextView) convertView.findViewById(R.id.openAdvert);
        openAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(context,AdvertActivity.class);
                searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if ( property.has("id") ) {
                    try {
                        searchIntent.putExtra("advertID",property.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                context.startActivity(searchIntent);
            }
        });

        try {
            if (property.has("pictures")) {
                JSONArray pictureArray=property.getJSONArray("pictures");
                if ( pictureArray!=null  ) {
                    Picasso.with(context).load(pictureArray.get(0).toString()).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(bigImage);
                    if (pictureArray.length()>=4) {
                        Picasso.with(context).load(pictureArray.get(1).toString()).resize(148, 148).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(advertImage2);
                        Picasso.with(context).load(pictureArray.get(2).toString()).resize(148, 148).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(advertImage3);
                        Picasso.with(context).load(pictureArray.get(3).toString()).resize(148, 148).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(advertImage4);
                    } else if (pictureArray.length()==3) {
                        Picasso.with(context).load(pictureArray.get(1).toString()).resize(148, 148).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(advertImage2);
                        Picasso.with(context).load(pictureArray.get(2).toString()).resize(148, 148).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(advertImage3);
                    } else if (pictureArray.length()==2) {
                        Picasso.with(context).load(pictureArray.get(1).toString()).resize(148, 148).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(advertImage2);
                    }
                } else {
                    Picasso.with(context).load(R.drawable.noproperty).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(bigImage);
                }
            } else {
                Picasso.with(context).load(R.drawable.noproperty).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(bigImage);
            }

            if (property.has("type_home")) {
                String type_home=property.getString("type_home");
                if ( type_home!=null ) {
                    if ( property.has("id") ) {
                        String propertyType="Продава ";
                        if (property.getString("id").startsWith("2")) {
                            propertyType="Дава под наем ";
                        }
                        propertyTitle.setText(propertyType+type_home);
                    }
                }
            }

            if (property.has("town")) {
                String town=property.getString("town");
                if ( town!=null ) {
                    propertyCity.setText(town);
                } else {
                    propertyCity.setVisibility(View.GONE);
                }
            }

            if (property.has("raion")) {
                String raion=property.getString("raion");

                if (property.has("street")) {
                    String street=property.getString("street");
                    if ( street!=null ) {
                        raion=raion+ " "+street;
                    }
                }

                if ( raion!=null ) {
                    propertyNeighborhood.setText(raion);
                } else {
                    propertyNeighborhood.setVisibility(View.GONE);
                }
            }

            if ( property.has("quadrature") && property.has("metric") ) {
                String size=property.getString("quadrature")+" " + property.getString("metric");
                if ( size!=null ) {
                    propertySize.setText(size);
                }
            }

            if (property.has("price")) {
                String price=property.getString("price");
                if ( price.contains("Цена") ) {
                    propertyPrice.setTextSize(12);
                } else {
                    propertyPrice.setTextSize(20);
                    if (property.has("currency")) {
                        price=price+" " +property.getString("currency");
                    }
                }

                if ( price!=null ) {
                    propertyPrice.setText(price);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;

    }
}

