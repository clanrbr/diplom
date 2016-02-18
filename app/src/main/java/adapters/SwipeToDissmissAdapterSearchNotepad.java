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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import db.PropertyVisits;
import db.PropertyVisits_Table;
import db.SearchNotepad;
import db.SearchNotepad_Table;
import localestates.localestates.R;

public class SwipeToDissmissAdapterSearchNotepad extends BaseAdapter implements
        Swappable, UndoAdapter, OnDismissCallback {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<SearchNotepad> searchList;

	public SwipeToDissmissAdapterSearchNotepad(Context context,
                                               List<SearchNotepad> advertList) {
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.searchList = advertList;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public int getCount() {
		return searchList.size();
	}

	@Override
	public SearchNotepad getItem(int position) {
		return searchList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return searchList.get(position).search_auto_id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_search_notepad, parent, false);
            holder = new ViewHolder();
            holder.searchTitle = (TextView) convertView.findViewById(R.id.itemSearchTitle);
            holder.itemSearchAddedTime = (TextView) convertView.findViewById(R.id.itemSearchAddedTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SearchNotepad searchNotepad = searchList.get(position);
        String searchTitle = searchNotepad.search_title;
        if (searchTitle.length()==0) {
            searchTitle = searchNotepad.search_title;
        }
        if (searchTitle.length()==0) {
            searchTitle = "Търсене "+ String.valueOf(searchNotepad.search_auto_id);
        }
        holder.searchTitle.setText(searchTitle);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy г. на HH:mm ч.");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        long timeAdded = searchNotepad.search_add_time;
        Date dateTimeAdded = new Date(timeAdded*1000L);
        String formattedDateTimeAdded = sdf.format(dateTimeAdded);
        holder.itemSearchAddedTime.setText(formattedDateTimeAdded);

        return convertView;
	}

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        Collections.swap(searchList, positionOne, positionTwo);
    }

    private static class ViewHolder {
        public ImageView image;
        public/* Roboto */TextView searchTitle;
        public/* Roboto */TextView itemSearchAddedTime;
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
        SQLite.delete(SearchNotepad.class)
                .where(SearchNotepad_Table.search_auto_id.is(searchList.get(position).search_auto_id))
                .query();
        searchList.remove(position);
    }
}
