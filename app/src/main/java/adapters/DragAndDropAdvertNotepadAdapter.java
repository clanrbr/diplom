package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
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

import db.AdvertNotepad;
import db.AdvertNotepad_Table;
import localestates.localestates.R;

/**
 * Created by macbook on 2/3/16.
 */
public class DragAndDropAdvertNotepadAdapter extends BaseAdapter implements Swappable, UndoAdapter, OnDismissCallback {

    private Context mContext;
    private LayoutInflater mInflater;
//    private ArrayList<JSONObject> advertList;
    private List<AdvertNotepad> advertList;

    public DragAndDropAdvertNotepadAdapter(Context context, List<AdvertNotepad> advertList) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public AdvertNotepad getItem(int position) {
        return advertList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return advertList.get(position).advert_auto_id;
    }

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        Log.e("HEREHERE",String.valueOf(positionOne)+" to "+String.valueOf(positionTwo));
        Log.e("HEREHERE", String.valueOf(advertList.size()));

//        Handler mHandler = new Handler();
//        ResultReceiver resultReceiver = new ResultReceiver(mHandler) {
//            @Override
//            protected void onReceiveResult(int resultCode, Bundle resultData) {
//                // here we receive the result
//            }
//        };
//        DBTransactionInfo info = DBTransactionInfo.create("cool",BaseTransaction.PRIORITY_UI);
//        Model model = new Model


//        ProcessModelInfo<AdvertNotepad> processModelInfo = ProcessModelInfo.withModels()
//                .result(resultReceiver)
//                .info(info);

        // or directly to the queue
//        TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(processModelInfo));

        // Native SQL wrapper
        SQLite.update(AdvertNotepad.class)
            .set(AdvertNotepad_Table.order.eq(advertList.get(positionOne).order))
            .where(AdvertNotepad_Table.advert_auto_id.is(advertList.get(positionTwo).advert_auto_id))
            .query();

        Log.e("HEREHERE",String.valueOf(advertList.get(positionOne).order));
        Log.e("HEREHERE",String.valueOf(advertList.get(positionTwo).order));

        SQLite.update(AdvertNotepad.class)
            .set(AdvertNotepad_Table.order.eq(advertList.get(positionTwo).order))
            .where(AdvertNotepad_Table.advert_auto_id.is(advertList.get(positionOne).advert_auto_id))
            .query();
//        Where<AdvertNotepad> update = SQLite.update(AdvertNotepad.class)
//                .set(AdvertNotepad_Table.order.eq(advertList.get(positionOne).order))
//                .where(AdvertNotepad_Table.advert_auto_id.is(advertList.get(positionTwo).advert_auto_id));
//        TransactionManager.getInstance().addTransaction(new QueryTransaction(DBTransactionInfo.create(BaseTransaction.PRIORITY_UI),update));
        // Native SQL wrapper
//        Where<AdvertNotepad> updateSecond = SQLite.update(AdvertNotepad.class)
//                .set(AdvertNotepad_Table.order.eq(advertList.get(positionTwo).order))
//                .where(AdvertNotepad_Table.advert_auto_id.is(advertList.get(positionOne).advert_auto_id));
//        TransactionManager.getInstance().addTransaction(new QueryTransaction(DBTransactionInfo.create(BaseTransaction.PRIORITY_UI), updateSecond));

        Collections.swap(advertList, positionOne, positionTwo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_notepad_list, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.list_item_drag_and_drop_property_image);
            holder.productName = (TextView) convertView.findViewById(R.id.list_item_drag_and_drop_property_title);
            holder.oldPrice = (TextView) convertView.findViewById(R.id.list_item_drag_and_drop_region_name);
            holder.price = (TextView) convertView.findViewById(R.id.list_item_drag_and_drop_shop_price);
            holder.icon = (TextView) convertView.findViewById(R.id.list_item_drag_and_drop_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AdvertNotepad adv = advertList.get(position);
        JSONObject notepad_advert = null;
        try {
            notepad_advert = new JSONObject(adv.advert_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (notepad_advert!=null) {
            if (notepad_advert.has("pictures")) {
                JSONArray pictureArray = null;
                try {
                    pictureArray = notepad_advert.getJSONArray("pictures");
                    if (pictureArray != null) {
                        Picasso.with(mContext).load(pictureArray.get(0).toString()).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(holder.image);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
                if (notepad_advert.has("type_home")) {
                    String type_home=notepad_advert.getString("type_home");
                    holder.productName.setText("Продава "+type_home);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (notepad_advert.has("town")) {
                    String address=notepad_advert.getString("town");
                    if (notepad_advert.has("raion")) {
                        address=address+", "+notepad_advert.getString("raion");
                    }

                    if (notepad_advert.has("street")) {
                        address=address+", "+notepad_advert.getString("street");
                    }

                    holder.oldPrice.setText(address);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (notepad_advert.has("price")) {
                    String price=notepad_advert.getString("price");
                    if (notepad_advert.has("currency")) {
                        String currency=notepad_advert.getString("currency");
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

    private static class ViewHolder {
        public ImageView image;
        public/* Roboto */TextView productName;
        public/* Roboto */TextView oldPrice;
        public/* Roboto */TextView price;
        public/* Roboto */TextView sellerName;
        public/* Roboto */TextView shipping;
        public/* Material */TextView icon;
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
        SQLite.delete(AdvertNotepad.class)
        .where(AdvertNotepad_Table.advert_auto_id.is(advertList.get(position).advert_auto_id))
        .query();
        advertList.remove(position);
    }
}
