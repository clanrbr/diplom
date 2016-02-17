package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;
import com.nhaarman.listviewanimations.util.Swappable;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import db.PropertyVisits;
import db.PropertyVisits_Table;
import localestates.localestates.R;

public class SwipeToDissmissAdapter extends BaseAdapter implements
        Swappable, UndoAdapter, OnDismissCallback {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<PropertyVisits> advertList;

	public SwipeToDissmissAdapter(Context context,
								  List<PropertyVisits> advertList) {
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.advertList = advertList;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public int getCount() {
		return advertList.size();
	}

	@Override
	public PropertyVisits getItem(int position) {
		return advertList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return advertList.get(position).advert_auto_id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_property_view_list, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.list_item_drag_and_drop_property_image);
            holder.productName = (TextView) convertView.findViewById(R.id.list_item_drag_and_drop_property_title);
            holder.regionName = (TextView) convertView.findViewById(R.id.list_item_drag_and_drop_region_name);
            holder.price = (TextView) convertView.findViewById(R.id.list_item_drag_and_drop_shop_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PropertyVisits adv = advertList.get(position);
        JSONObject property_visit_advert = null;
        try {
            property_visit_advert = new JSONObject(adv.advert_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (property_visit_advert!=null) {
            if (property_visit_advert.has("pictures")) {
                JSONArray pictureArray = null;
                try {
                    pictureArray = property_visit_advert.getJSONArray("pictures");
                    if (pictureArray != null) {
                        Picasso.with(mContext).load(pictureArray.get(0).toString()).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(holder.image);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
                if (property_visit_advert.has("type_home")) {
                    String type_home=property_visit_advert.getString("type_home");
                    if ( type_home!=null ) {
                        if ( property_visit_advert.has("id") ) {
                            String propertyType="Продава ";
                            if (property_visit_advert.getString("id").startsWith("2")) {
                                propertyType="Дава под наем ";
                            }
                            holder.productName.setText(propertyType+type_home);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (property_visit_advert.has("town")) {
                    String address=property_visit_advert.getString("town");
                    if (property_visit_advert.has("raion")) {
                        address=address+", "+property_visit_advert.getString("raion");
                    }

                    if (property_visit_advert.has("street")) {
                        address=address+", "+property_visit_advert.getString("street");
                    }

                    holder.regionName.setText(address);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (property_visit_advert.has("price")) {
                    String price=property_visit_advert.getString("price");
                    if (property_visit_advert.has("currency")) {
                        String currency=property_visit_advert.getString("currency");
                        price = price+" "+currency;
                    }
                    holder.price.setText(price);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return convertView;
	}

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        Collections.swap(advertList, positionOne, positionTwo);
    }

    private static class ViewHolder {
        public ImageView image;
        public/* Roboto */TextView productName;
        public/* Roboto */TextView regionName;
        public/* Roboto */TextView price;
        public/* Roboto */TextView sellerName;
        public/* Roboto */TextView shipping;
//        public/* Material */TextView icon;
    }

    @Override
    @NonNull
    public View getUndoClickView(@NonNull View view) {
        return view.findViewById(R.id.undo_button);
    }

    @Override
    @NonNull
    public View getUndoView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_undo, parent, false);
        }
        return view;
    }

    @Override
    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            remove(position);
        }
    }

    public void remove(int position) {
        SQLite.delete(PropertyVisits.class)
                .where(PropertyVisits_Table.advert_auto_id.is(advertList.get(position).advert_auto_id))
                .query();
        advertList.remove(position);
    }
}
