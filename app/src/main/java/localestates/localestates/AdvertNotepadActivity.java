package localestates.localestates;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import adapters.DragAndDropAdvertNotepadAdapter;
import adapters.PropertiesArrayAdapter;
import constants.LocalEstateConstants;
import db.AdvertNotepad;
import db.AdvertNotepad_Table;
import localEstatesHttpRequests.HTTPGETAdvert;

/**
 * Created by macbook on 2/2/16.
 */
public class AdvertNotepadActivity extends AppCompatActivity {

    private ArrayList<JSONObject> allNotepadAdverts;
    private List<AdvertNotepad> allNotepadAdvertsObj;
    private DynamicListView mDynamicListView;
    private DragAndDropAdvertNotepadAdapter adapter;
    private PropertiesArrayAdapter adapterProperties;
    private ListView listView;
    private CircularProgressBar progressBar;
    private JSONObject advertInfo;
    private String advertPhone;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_700));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
//        setContentView(R.layout.activity_start);

        ImageView menuItemSearch = (ImageView) findViewById(R.id.searchActionBar);
        ImageView menuItemFavourite = (ImageView) findViewById(R.id.favouriteActionBar);
        menuItemFavourite.setImageResource(R.drawable.ic_favorite_white_24dp);
        ImageView menuItemNotification = (ImageView) findViewById(R.id.notificationActionBar);
        ImageView menuItemHome = (ImageView) findViewById(R.id.homeActionBar);

        menuItemHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favouriteIntent = new Intent(getBaseContext(), StartActivity.class);
                finish();
                startActivity(favouriteIntent);
            }
        });

        menuItemFavourite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        menuItemNotification.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        menuItemSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getBaseContext(), AdvanceSearchActivity.class);
                finish();
                startActivity(searchIntent);
            }
        });

        allNotepadAdvertsObj = SQLite.select()
                .from(AdvertNotepad.class)
                .where()
                .orderBy(AdvertNotepad_Table.order, true)
                .queryList();

        if (allNotepadAdvertsObj != null) {
            allNotepadAdverts = new ArrayList<>();

            for (int i = 0; i < allNotepadAdvertsObj.size(); i++) {
                try {
                    JSONObject property = new JSONObject(allNotepadAdvertsObj.get(i).advert_list);
                    allNotepadAdverts.add(property);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        if ((allNotepadAdverts != null) && (allNotepadAdverts.size() > 0)) {

            mDynamicListView = (DynamicListView) findViewById(R.id.activity_list_view_drag_and_drop_dynamic_listview);
            mDynamicListView.setDividerHeight(0);
            setUpDragAndDrop();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LocalEstateConstants.MY_PEMISSION_PHONE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callActionFunction();
                } else {
//                    Toast.makeText(AdvertNotepadActivity.this, "Обаждането изисква разрешение", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case LocalEstateConstants.MY_PEMISSION_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        getLocation(advertInfo.getString("coordinates"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AdvertNotepadActivity.this, "Навигацията изисква разрешение", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case LocalEstateConstants.MY_PEMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ( ActivityCompat.checkSelfPermission(AdvertNotepadActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            getLocation(advertInfo.getString("coordinates"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ActivityCompat.requestPermissions(AdvertNotepadActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, LocalEstateConstants.MY_PEMISSION_ACCESS_COARSE_LOCATION);
                    }
                } else {
                    Toast.makeText(AdvertNotepadActivity.this, "Навигацията изисква разрешение", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void setUpDragAndDrop() {
        adapter = new DragAndDropAdvertNotepadAdapter(this, allNotepadAdvertsObj);
        final SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(adapter, this, new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    adapter.remove(position);
                }
            }
        });

        swipeUndoAdapter.setAbsListView(mDynamicListView);
        mDynamicListView.setAdapter(swipeUndoAdapter);
        mDynamicListView.enableSimpleSwipeUndo();

        mDynamicListView.enableDragAndDrop();
        TouchViewDraggableManager tvdm = new TouchViewDraggableManager(R.id.icon);
        mDynamicListView.setDraggableManager(tvdm);
        mDynamicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view, int position, long id) {
                mDynamicListView.startDragging(position);
                return true;
            }
        });

        mDynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final Dialog dialog=new Dialog(AdvertNotepadActivity.this,android.R.style.Theme_DeviceDefault_Light_NoActionBar);
                dialog.setContentView(R.layout.notepad_pop_up);
                TextView titleAdvert = (TextView) dialog.findViewById(R.id.titleAdvert);
                final TextView timeAddedTextView = (TextView) dialog.findViewById(R.id.timeAdded);
                final EditText noteText = (EditText) dialog.findViewById(R.id.noteText);
                final TextView updateAdvert = (TextView) dialog.findViewById(R.id.updatedAdvertText);
                progressBar = (CircularProgressBar) dialog.findViewById(R.id.progressBar);
                FrameLayout mapBar = (FrameLayout) dialog.findViewById(R.id.mapBar);
                FrameLayout mapNavBar = (FrameLayout) dialog.findViewById(R.id.mapNavBar);
                FrameLayout mapStreetViewBar = (FrameLayout) dialog.findViewById(R.id.mapStreetViewBar);
                TextView saveNoteDialog = (TextView) dialog.findViewById(R.id.saveNoteDialog);
                TextView closeNoteDialog = (TextView) dialog.findViewById(R.id.closeNoteDialog);
                FrameLayout openAdvert = (FrameLayout) dialog.findViewById(R.id.openAdvert);

                // Check if no view has focus:
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


                final AdvertNotepad selectedAdvert = allNotepadAdvertsObj.get(position);
                try {
                    advertInfo = new JSONObject(selectedAdvert.advert_list);
                    if (advertInfo.has("rub")) {
                        String value = null;
                        try {
                            value = advertInfo.getString("rub");
                            if (advertInfo.has("type_home")) {
                                value = value + " " + advertInfo.getString("type_home");
                            }
                            titleAdvert.setText(value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if ( advertInfo.has("phone") ) {
                        advertPhone=advertInfo.getString("phone");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                saveNoteDialog.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLite.update(AdvertNotepad.class)
                                .set(AdvertNotepad_Table.advert_note.eq(noteText.getText().toString()))
                                .where(AdvertNotepad_Table.advert_id.is(selectedAdvert.advert_id))
                                .query();
                        Toast.makeText(AdvertNotepadActivity.this,"Бележката беше успешно записана.",Toast.LENGTH_LONG).show();
                        allNotepadAdvertsObj.get(position).advert_note=noteText.getText().toString();
                        dialog.dismiss();
                    }
                });

                closeNoteDialog.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ImageView mapImageView = (ImageView) dialog.findViewById(R.id.mapImageView);
                mapImageView.setColorFilter(ContextCompat.getColor(AdvertNotepadActivity.this, R.color.material_lime_900));

                mapBar.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mapIntent = new Intent(AdvertNotepadActivity.this, MapActivity.class);
                        if (advertInfo.has("points")) {
                            try {
                                mapIntent.putExtra("points", advertInfo.getString("points"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (advertInfo.has("coordinates")) {
                            try {
                                mapIntent.putExtra("coordinates", advertInfo.getString("coordinates"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (advertInfo.has("rub")) {
                            String value = null;
                            try {
                                value = advertInfo.getString("rub");
                                if (advertInfo.has("type_home")) {
                                    value = value + " " + advertInfo.getString("type_home");
                                }
                                mapIntent.putExtra("title", value);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        startActivity(mapIntent);
                    }
                });

                ImageView navigationImageView = (ImageView) dialog.findViewById(R.id.mapNavImageView);
                navigationImageView.setColorFilter(ContextCompat.getColor(AdvertNotepadActivity.this, R.color.material_lime_900));

                mapNavBar.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (advertInfo.has("coordinates")) {
                            try {
                                if (ActivityCompat.checkSelfPermission(AdvertNotepadActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                                    if ( ActivityCompat.checkSelfPermission(AdvertNotepadActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                        getLocation(advertInfo.getString("coordinates"));
                                    } else {
                                        ActivityCompat.requestPermissions(AdvertNotepadActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, LocalEstateConstants.MY_PEMISSION_ACCESS_COARSE_LOCATION);
                                    }
                                } else {
                                    ActivityCompat.requestPermissions(AdvertNotepadActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LocalEstateConstants.MY_PEMISSION_ACCESS_FINE_LOCATION);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(AdvertNotepadActivity.this,"Този обект не е оказан на картата",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                ImageView mapStrettViewImageView = (ImageView) dialog.findViewById(R.id.mapStreetViewImageView);
                mapStrettViewImageView.setColorFilter(ContextCompat.getColor(AdvertNotepadActivity.this, R.color.material_lime_900));

                mapStreetViewBar.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mapIntent = new Intent(AdvertNotepadActivity.this, StreetViewActivity.class);
                        if (advertInfo.has("coordinates")) {
                            try {
                                mapIntent.putExtra("coordinates", advertInfo.getString("coordinates"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (advertInfo.has("rub")) {
                            String value = null;
                            try {
                                value = advertInfo.getString("rub");
                                if (advertInfo.has("type_home")) {
                                    value = value + " " + advertInfo.getString("type_home");
                                }
                                mapIntent.putExtra("title", value);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        startActivity(mapIntent);
                    }
                });

                openAdvert.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent searchIntent = new Intent(AdvertNotepadActivity.this,AdvertActivity.class);
                        searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        searchIntent.putExtra("advertID",selectedAdvert.advert_id);
                        startActivity(searchIntent);
                        dialog.dismiss();
                    }
                });


                FrameLayout updateAdvertAction = (FrameLayout) dialog.findViewById(R.id.updateAdvert);
                updateAdvertAction.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        HTTPGETAdvert updateAdvertAsync = new HTTPGETAdvert(progressBar) {
                            @Override
                            protected void onPostExecute(String result) {
                                progressBar.setVisibility(View.GONE);
                                if (result!=null) {
                                    try {
                                        JSONObject json = new JSONObject(result);
                                        if (json.has("adverts") ) {
                                            JSONArray advertsJsonArray_=json.getJSONArray("adverts");
                                            if (advertsJsonArray_.length()>0) {
                                                long unixTime = System.currentTimeMillis() / 1000L;
                                                JSONObject advertInfo_ = advertsJsonArray_.getJSONObject(0);
                                                SQLite.update(AdvertNotepad.class)
                                                        .set(AdvertNotepad_Table.advert_list.eq(advertInfo_.toString()),AdvertNotepad_Table.advert_version_time.eq(unixTime))
                                                        .where(AdvertNotepad_Table.advert_id.is(selectedAdvert.advert_id))
                                                        .query();
                                                Toast.makeText(AdvertNotepadActivity.this,"Информацията за тази обява беше успешно обновена.",Toast.LENGTH_LONG).show();
                                                allNotepadAdvertsObj.get(position).advert_list=advertInfo_.toString();
                                                allNotepadAdvertsObj.get(position).advert_version_time=unixTime;
                                                swipeUndoAdapter.notifyDataSetChanged();
                                                reinitFields(allNotepadAdvertsObj.get(position),timeAddedTextView,noteText,updateAdvert);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(AdvertNotepadActivity.this,"Тази обява е изтрита, не може да бъде обновена информацията.",Toast.LENGTH_LONG).show();
                                }
                            }
                        };
                        String urlGetAdvert="http://api.imot.bg/mobile_api/details?id="+selectedAdvert.advert_id;
                        updateAdvertAsync.execute(urlGetAdvert);
                    }
                });


                reinitFields(selectedAdvert,timeAddedTextView,noteText,updateAdvert);
                dialog.show();
            }
        });
    }

    private void reinitFields(AdvertNotepad selectedAdvert, TextView timeAddedTextView, EditText noteText, TextView updateAdvert) {

        String advert_note= selectedAdvert.advert_note;
        noteText.setText(advert_note);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));

        long timeAdded = selectedAdvert.advert_time;
        Date dateTimeAdded = new Date(timeAdded*1000L);
        String formattedDateTimeAdded = sdf.format(dateTimeAdded)+" г.";
        timeAddedTextView.setText(formattedDateTimeAdded);


        long updateTimeAdded = selectedAdvert.advert_version_time;
        Date dateUpdateTime = new Date(updateTimeAdded*1000L);
        String formattedDateUpdateTime = sdf.format(dateUpdateTime)+" г.";
        updateAdvert.setText(formattedDateUpdateTime);
    }

    public void getLocation(String coordinates) {
        if (ContextCompat.checkSelfPermission(AdvertNotepadActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(AdvertNotepadActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                String[] coordArray = coordinates.split(",");
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +  "&daddr=" + coordArray[0] + "," + coordArray[1]));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        }
    }

    public void callActionFunction() {
        if (ContextCompat.checkSelfPermission(AdvertNotepadActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            LayoutInflater inflater = AdvertNotepadActivity.this.getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.dialog_phone_permission, null);
            final AlertDialog.Builder builderDialog = new AlertDialog.Builder(AdvertNotepadActivity.this);
            builderDialog.setView(dialoglayout);
            final AlertDialog show = builderDialog.show();
            Button closeButton= (Button) dialoglayout.findViewById(R.id.closeDialog);
            closeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    show.dismiss();
                }
            });
            Button callButton= (Button) dialoglayout.findViewById(R.id.callDialog);
            callButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    show.dismiss();
                    if (ContextCompat.checkSelfPermission(AdvertNotepadActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                        phoneIntent.setData(Uri.parse("tel:" + advertPhone));
                        startActivity(phoneIntent);
                    }
                }
            });
        }
    }
}

