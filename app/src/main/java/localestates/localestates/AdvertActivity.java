package localestates.localestates;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nineoldandroids.view.ViewHelper;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import adapters.GridViewPropertyFeaturesAdapter;
import application.LocalEstateApplication;
import constants.LocalEstateConstants;
import db.AdvertNotepad;
import db.AdvertNotepad_Table;
import db.PropertyVisits;
import db.PropertyVisits_Table;
import interfaces.AsyncResponseLoadAdvert;
import localEstatesHttpRequests.HTTPGETAdvert;
import utils.ExpandableHeightGridView;
import utils.HelpFunctions;
import utils.MaterialRippleLayout;

/**
 * Created by macbook on 1/26/16.
 */
public class AdvertActivity extends AppCompatActivity implements ObservableScrollViewCallbacks,
        AsyncResponseLoadAdvert {

    private AdvertNotepad existsAdvertNotepad;
    private PropertyVisits existsAdvertAppointment;
    private int startScroll;
    private String advertID;
    private String advertOutput;
    private int stopScroll;
    private String googleMapsAPI = "AIzaSyCNBVx3m2Q0q2oo4FQhA4UdAyuTaCQ0BRg";
    private JSONArray picturesArray;
    private String advertTitle;
    private String advertPhone;
    private ImageView mImageView;
    private Toolbar mToolbarView;
    private TextView priceLabel;
    private TextView placeLabel;
    private TextView brokerName;
    private TextView agencyName;
    private TextView agencyAddress;
    private TextView titleField;
    private TextView roundNumber;
    private TextView numberPictures;
    private LinearLayout agencyField;
    private LinearLayout personField;
    private MaterialRippleLayout callAction;

    private LinearLayout extriTitle;
    private TextView extriContent;
    private ExpandableTextView moreInfoLabelExpand;
    private FrameLayout mapBar;
    private FrameLayout mapNavBar;
    private FrameLayout mapStreetViewBar;
    private ExpandableHeightGridView feturesGridView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private JSONObject advertInto;
    private HTTPGETAdvert asyncLoadAdvert;
    private Menu mMenu;
    private CircularProgressBar progressBar;
    private LinearLayout callBar;
    private FrameLayout scrollFrame;
    private SimpleDateFormat dateFormatter;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_700));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        LocalEstateApplication application = (LocalEstateApplication) getApplication();
        mTracker = application.getDefaultTracker();

        progressBar = (CircularProgressBar)findViewById(R.id.progressBar);
        progressBar.setColor(ContextCompat.getColor(this, R.color.main_color_500));
        int animationDuration = 2500; // 2500ms = 2,5s
        progressBar.setProgressWithAnimation(65, animationDuration);

        asyncLoadAdvert = new HTTPGETAdvert(progressBar);
        asyncLoadAdvert.delegate = this;


        priceLabel = (TextView) findViewById(R.id.priceLabel);
        roundNumber = (TextView) findViewById(R.id.roundNumber);
        titleField = (TextView) findViewById(R.id.titleField);
        placeLabel = (TextView) findViewById(R.id.placeLabel);
        feturesGridView = (ExpandableHeightGridView) findViewById(R.id.features);
        mapBar = (FrameLayout) findViewById(R.id.mapBar);
        mapNavBar = (FrameLayout) findViewById(R.id.mapNavBar);
        mapStreetViewBar = (FrameLayout) findViewById(R.id.mapStreetViewBar);
        moreInfoLabelExpand = (ExpandableTextView) findViewById(R.id.moreInfoLabelExpand);
        brokerName = (TextView) findViewById(R.id.brokerName);
        agencyName = (TextView) findViewById(R.id.agencyName);
        agencyAddress = (TextView) findViewById(R.id.agencyAddress);
        agencyField = (LinearLayout) findViewById(R.id.agencyField);
        callAction = (MaterialRippleLayout) findViewById(R.id.callAction);
        numberPictures = (TextView) findViewById(R.id.numberPictures);

        personField = (LinearLayout) findViewById(R.id.personField);
        extriTitle = (LinearLayout) findViewById(R.id.extriTitle);
        extriContent = (TextView) findViewById(R.id.extriContent);

        callBar = (LinearLayout) findViewById(R.id.callBar);
        scrollFrame = (FrameLayout) findViewById(R.id.scroll2);


        callAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(AdvertActivity.this, Manifest.permission.CALL_PHONE) ) {
                    PackageManager pm = getPackageManager();
                    if ( pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) ) {
                        callActionFunction();
                    } else {
                        Toast.makeText(AdvertActivity.this,"Няма как да наберете този номер",Toast.LENGTH_LONG).show();
                    }
                } else {
                    ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{Manifest.permission.CALL_PHONE}, LocalEstateConstants.MY_PEMISSION_PHONE_CODE);
                }
            }
        });

        // change color of icon
        ImageView mapImageView = (ImageView) findViewById(R.id.mapImageView);
        mapImageView.setColorFilter(ContextCompat.getColor(AdvertActivity.this, R.color.material_lime_900));

        mapBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent mapIntent = new Intent(AdvertActivity.this, MapActivity.class);
            if (advertInto.has("points")) {
                try {
                    mapIntent.putExtra("points", advertInto.getString("points"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (advertInto.has("coordinates")) {
                try {
                    mapIntent.putExtra("coordinates", advertInto.getString("coordinates"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (advertInto.has("rub")) {
                String value = null;
                try {
                    value = advertInto.getString("rub");
                    if (advertInto.has("type_home")) {
                        value = value + " " + advertInto.getString("type_home");
                    }
                    mapIntent.putExtra("title", value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            startActivity(mapIntent);
            }
        });

        ImageView navigationImageView = (ImageView) findViewById(R.id.mapNavImageView);
        navigationImageView.setColorFilter(ContextCompat.getColor(AdvertActivity.this, R.color.material_lime_900));

        mapNavBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advertInto.has("coordinates")) {
                    try {
                        if (ActivityCompat.checkSelfPermission(AdvertActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                            if ( ActivityCompat.checkSelfPermission(AdvertActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                getLocation(advertInto.getString("coordinates"));
                            } else {
                                ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LocalEstateConstants.MY_PEMISSION_ACCESS_COARSE_LOCATION);
                            }
                        } else {
                            ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LocalEstateConstants.MY_PEMISSION_ACCESS_FINE_LOCATION);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AdvertActivity.this,"Този обект не е оказан на картата",Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageView mapStrettViewImageView = (ImageView) findViewById(R.id.mapStreetViewImageView);
        mapStrettViewImageView.setColorFilter(ContextCompat.getColor(AdvertActivity.this, R.color.material_lime_900));

        mapStreetViewBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(AdvertActivity.this, StreetViewActivity.class);
                if (advertInto.has("coordinates")) {
                    try {
                        mapIntent.putExtra("coordinates", advertInto.getString("coordinates"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (advertInto.has("rub")) {
                    String value = null;
                    try {
                        value = advertInto.getString("rub");
                        if (advertInto.has("type_home")) {
                            value = value + " " + advertInto.getString("type_home");
                        }
                        mapIntent.putExtra("title", value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                startActivity(mapIntent);
            }
        });


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String advertID = extras.getString("advertID");
            if ( (advertID!=null) && (!advertID.equals("")) ) {
                String urlGetAdvert="http://api.imot.bg/mobile_api/details?id="+advertID;
                asyncLoadAdvert.execute(urlGetAdvert);
            }
        }

        mImageView = (ImageView) findViewById(R.id.bigImage);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent largeImageIntent = new Intent(AdvertActivity.this,AdvertImagesActivity.class);
                if ( (picturesArray!=null) && (picturesArray.length()>0) ) {
                    largeImageIntent.putExtra("advertPictures",picturesArray.toString());
                }
                if ( advertTitle!=null ) {
                    largeImageIntent.putExtra("advertTitle",advertTitle);
                }
                startActivity(largeImageIntent);
            }
        });

        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.main_color_500)));
        if (mToolbarView != null) {
            mToolbarView.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            mToolbarView.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LocalEstateConstants.MY_PEMISSION_PHONE_CODE: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PackageManager pm = getPackageManager();
                    if ( pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) ) {
                        callActionFunction();
                    } else {
                        Toast.makeText(this,"Обаждането изисква разрешение",Toast.LENGTH_LONG).show();
                    }
                }
                break;
            }
            case LocalEstateConstants.MY_PEMISSION_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        getLocation(advertInto.getString("coordinates"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AdvertActivity.this, "Навигацията изисква разрешение", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case LocalEstateConstants.MY_PEMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ( ActivityCompat.checkSelfPermission(AdvertActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            getLocation(advertInto.getString("coordinates"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LocalEstateConstants.MY_PEMISSION_ACCESS_COARSE_LOCATION);
                    }
                } else {
                    Toast.makeText(AdvertActivity.this, "Навигацията изисква разрешение", Toast.LENGTH_LONG).show();
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = ContextCompat.getColor(AdvertActivity.this, R.color.main_color_500);
//        int baseColor = getResources().getColor(R.color.main_color_500);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
        startScroll = mScrollView.getScrollY();
    }

    public void getLocation(String coordinates) {
        if (ContextCompat.checkSelfPermission(AdvertActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(AdvertActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                String[] coordArray = coordinates.split(",");
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +  "&daddr=" + coordArray[0] + "," + coordArray[1]));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        }
    }

    public void callActionFunction() {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("SetCall")
                .build());
        if (ContextCompat.checkSelfPermission(AdvertActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            LayoutInflater inflater = AdvertActivity.this.getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.dialog_phone_permission, null);
            final AlertDialog.Builder builderDialog = new AlertDialog.Builder(AdvertActivity.this);
            builderDialog.setView(dialoglayout);
            final AlertDialog show = builderDialog.show();
            Button closeButton= (Button) dialoglayout.findViewById(R.id.closeDialog);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show.dismiss();
                }
            });
            Button callButton= (Button) dialoglayout.findViewById(R.id.callDialog);
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show.dismiss();
                    if (ContextCompat.checkSelfPermission(AdvertActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                        phoneIntent.setData(Uri.parse("tel:" + advertPhone));
                        startActivity(phoneIntent);
                    }
                }
            });
        }
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        stopScroll = mScrollView.getScrollY();

        if ( (startScroll==stopScroll)  && (startScroll==0) && (stopScroll==0) ) {
            Intent largeImageIntent = new Intent(AdvertActivity.this,AdvertImagesActivity.class);
            if ( (picturesArray!=null) && (picturesArray.length()>0) ) {
                largeImageIntent.putExtra("advertPictures",picturesArray.toString());
            }
            if ( advertTitle!=null ) {
                largeImageIntent.putExtra("advertTitle",advertTitle);
            }
            startActivity(largeImageIntent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        mMenu=menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addFavouritesBar) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("SetNotepad")
                    .build());
            int orderAdvert=1;
            AdvertNotepad existsAdvert = SQLite.select()
                    .from(AdvertNotepad.class)
                    .where(AdvertNotepad_Table.advert_id.is(advertID))
                    .querySingle();
            if ( existsAdvert!=null ) {
                SQLite.delete()
                        .from(AdvertNotepad.class)
                        .where(AdvertNotepad_Table.advert_id.is(advertID))
                        .query();

                MenuItem itemFavourite = mMenu.findItem(R.id.addFavouritesBar);
                if ( itemFavourite!=null ) {
                    itemFavourite.setIcon(R.drawable.ic_favorite_black_24dp);
                }
                Toast.makeText(AdvertActivity.this,"Обявата беше успешно изтрита от бележника!",Toast.LENGTH_LONG).show();
            } else {
                AdvertNotepad lastAdvertNotepad = SQLite.select()
                        .from(AdvertNotepad.class)
                        .where()
                        .orderBy(AdvertNotepad_Table.order,true)
                        .querySingle();

                if ( lastAdvertNotepad!=null ) {
                    orderAdvert=lastAdvertNotepad.order+1;
                }

                if ( (advertOutput!=null) && (advertID!=null) ) {
                    long unixTime = System.currentTimeMillis() / 1000L;

                    AdvertNotepad advnote = new AdvertNotepad();
                    advnote.advert_id=advertID;
                    advnote.advert_note="";
                    advnote.advert_list=advertOutput;
                    advnote.advert_time=unixTime;
                    advnote.advert_version_time=unixTime;
                    advnote.order=orderAdvert;
                    advnote.save();

                    MenuItem itemFavourite = mMenu.findItem(R.id.addFavouritesBar);
                    if ( itemFavourite!=null ) {
                        itemFavourite.setIcon(R.drawable.ic_favorite_white_24dp);
                    }
                    Toast.makeText(AdvertActivity.this,"Обявата беше успешно добавена в бележника!",Toast.LENGTH_LONG).show();
                }
            }

            return true;
        } else if (id == R.id.appintmentActionButton) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("SetAppointment")
                    .build());

            final Dialog dialog=new Dialog(AdvertActivity.this,android.R.style.Theme_DeviceDefault_Light_NoActionBar);
            dialog.setContentView(R.layout.notepad_appointment_add_pop_up);
            TextView titleAdvert = (TextView) dialog.findViewById(R.id.titleAdvert);
            final EditText noteText = (EditText) dialog.findViewById(R.id.noteText);
            final EditText appointmentDatePick = (EditText) dialog.findViewById(R.id.appointmentDatePick);
            final EditText appointmentHourPick = (EditText) dialog.findViewById(R.id.appointmentHourPick);
            TextView saveAppDialog = (TextView) dialog.findViewById(R.id.saveAppointmentDialog);
            TextView closeAppointmentDialog = (TextView) dialog.findViewById(R.id.closeAppointmentDialog);
            TextView deleteAppointmentDialog = (TextView) dialog.findViewById(R.id.deleteAppointmentDialog);
            final EditText additionalPhone = (EditText) dialog.findViewById(R.id.additionalPhone);

            // Check if no view has focus:
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            titleAdvert.setText(advertTitle);

            final Calendar newCalendar = Calendar.getInstance();
            appointmentDatePick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog fromDatePickerDialog = new DatePickerDialog(AdvertActivity.this, new OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                            appointmentDatePick.setText(dateFormatter.format(newDate.getTime()));
                        }

                    },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                    fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Запази", fromDatePickerDialog);
                    fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Затвори", fromDatePickerDialog);
                    fromDatePickerDialog.show();
                }
            });

            appointmentHourPick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog fromDatePickerDialog = new TimePickerDialog(AdvertActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            newDate.set(Calendar.MINUTE, minute);
                            dateFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            appointmentHourPick.setText(dateFormatter.format(newDate.getTime()));
                        }

                    },newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(AdvertActivity.this));
                    fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Запази", fromDatePickerDialog);
                    fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Затвори", fromDatePickerDialog);
                    fromDatePickerDialog.show();
                }
            });

            final PropertyVisits existsAdvert = SQLite.select()
                    .from(PropertyVisits.class)
                    .where(PropertyVisits_Table.advert_id.is(advertID))
                    .querySingle();

            if (existsAdvert!=null) {
                deleteAppointmentDialog.setVisibility(View.VISIBLE);
                appointmentDatePick.setText(existsAdvert.advert_appointment_time_date);
                appointmentHourPick.setText(existsAdvert.advert_appointment_time_hour);
                additionalPhone.setText(existsAdvert.advert_phone_number);
                noteText.setText(existsAdvert.advert_note);
            } else {
                additionalPhone.setText(advertPhone);
            }


//            TransactionListener<PropertyVisits> transactionListener = new TransactionListener<PropertyVisits>() {
//                @Override
//                public void onResultReceived(PropertyVisits result) {
//                    if ( result!=null ) {
//
//                    } else {
//
//                    }
//                }
//
//                @Override
//                public boolean onReady(BaseTransaction<PropertyVisits> transaction) {
//                    return false;
//                }
//
//                @Override
//                public boolean hasResult(BaseTransaction<PropertyVisits> transaction, PropertyVisits result) {
//                    return false;
//                }
//            };

            saveAppDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (existsAdvert==null) {
                        if ( (advertOutput!=null) && (advertID!=null) ) {
                            long unixTime = System.currentTimeMillis() / 1000L;

                            PropertyVisits advAppointment = new PropertyVisits();
                            advAppointment.advert_id=advertID;
                            advAppointment.advert_list=advertOutput;
                            advAppointment.advert_note=noteText.getText().toString();
                            advAppointment.advert_time=unixTime;
                            advAppointment.advert_appointment_time_date=appointmentDatePick.getText().toString();
                            advAppointment.advert_appointment_time_hour=appointmentHourPick.getText().toString();
                            advAppointment.advert_phone_number=additionalPhone.getText().toString();
                            advAppointment.advert_additional_address="";
                            advAppointment.save();

                            MenuItem itemAppointmentActionButton = mMenu.findItem(R.id.appintmentActionButton);
                            if ( itemAppointmentActionButton!=null ) {
                                itemAppointmentActionButton.setIcon(R.drawable.ic_date_range_white_24dp);
                            }
                            Toast.makeText(AdvertActivity.this,"Обявата беше успешно добавена в огледи!",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        SQLite.update(PropertyVisits.class)
                                .set(PropertyVisits_Table.advert_list.eq(advertOutput),
                                        PropertyVisits_Table.advert_note.eq(noteText.getText().toString()),
                                        PropertyVisits_Table.advert_appointment_time_date.eq(appointmentDatePick.getText().toString()),
                                        PropertyVisits_Table.advert_appointment_time_hour.eq(appointmentHourPick.getText().toString()),
                                        PropertyVisits_Table.advert_phone_number.eq(additionalPhone.getText().toString()),
                                        PropertyVisits_Table.advert_additional_address.eq(appointmentHourPick.getText().toString()))
                                .where(PropertyVisits_Table.advert_id.is(advertID))
                                .query();
                        Toast.makeText(AdvertActivity.this,"Обявата беше успешно запазена в огледи!",Toast.LENGTH_LONG).show();
                    }

                    dialog.dismiss();
                }
            });

            closeAppointmentDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            deleteAppointmentDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = AdvertActivity.this.getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.dialog_delete_appointment, null);
                    final AlertDialog.Builder builderDialog = new AlertDialog.Builder(AdvertActivity.this);
                    builderDialog.setView(dialoglayout);
                    final AlertDialog show = builderDialog.show();
                    Button closeButton = (Button) dialoglayout.findViewById(R.id.closeAppointmentButton);
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            show.dismiss();
                        }
                    });
                    Button callButton= (Button) dialoglayout.findViewById(R.id.deleteAppointmentButton);
                    callButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            show.dismiss();
                            SQLite.delete()
                                    .from(PropertyVisits.class)
                                    .where(PropertyVisits_Table.advert_id.is(advertID))
                                    .query();

                            Toast.makeText(AdvertActivity.this,"Обявата беше успешно изтрита от огледи!",Toast.LENGTH_LONG).show();
                            MenuItem itemFavourite = mMenu.findItem(R.id.addFavouritesBar);
                            if ( itemFavourite!=null ) {
                                itemFavourite.setIcon(R.drawable.ic_favorite_black_24dp);
                            }
                            dialog.dismiss();
                        }
                    });
                }
            });


            dialog.show();


//            private void setDateTimeField() {
//                fromDateEtxt.setOnClickListener(this);
//                toDateEtxt.setOnClickListener(this);
//
//                Calendar newCalendar = Calendar.getInstance();
//                fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
//
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        Calendar newDate = Calendar.getInstance();
//                        newDate.set(year, monthOfYear, dayOfMonth);
//                        fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
//                    }
//
//                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//
//                toDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
//
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        Calendar newDate = Calendar.getInstance();
//                        newDate.set(year, monthOfYear, dayOfMonth);
//                        toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
//                    }
//
//                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//            }

        }  else if (id == R.id.shareActionButton) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("SetShare")
                    .build());
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, advertTitle);
            String advertLink="http://www.imot.bg/pcgi/imot.cgi?act=5&adv="+advertID;
            sendIntent.putExtra(Intent.EXTRA_TEXT, advertLink);
            sendIntent.putExtra("com.facebook.platform.extra.APPLICATION_ID","78949132252");
            sendIntent.setType("text/plain");
            AdvertActivity.this.startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void processFinishLoadAdvert(JSONObject output) throws JSONException {
        progressBar.setVisibility(View.GONE);
        callBar.setVisibility(View.VISIBLE);

        advertInto=new JSONObject();
        if (output!=null) {
            scrollFrame.setVisibility(View.VISIBLE);
            advertOutput=output.toString();
            advertInto=output;
            Log.e("HEREHERE",output.toString());
            ArrayList<CharSequence> gridValueTitle = new ArrayList<CharSequence>();
            ArrayList<CharSequence> gridValue = new ArrayList<CharSequence>();
            if ( advertInto.has("id") ) {
                advertID=advertInto.getString("id");
            }


            if ( advertInto.has("pictures") ) {
                picturesArray = new JSONArray();
                picturesArray=advertInto.getJSONArray("pictures");
                if (picturesArray.length()>0) {
                    Picasso.with(AdvertActivity.this).load(picturesArray.get(0).toString()).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(mImageView);
                }
            }

            if ( advertInto.has("pictures_count") ) {
                Integer number = advertInto.getInt("pictures_count");
                if (number ==0 ) {
                    numberPictures.setText("0 снимки");
                } else if ( number==1 ) {
                    numberPictures.setText("1 снимка");
                } else {
                    numberPictures.setText(String.valueOf(number)+" снимки");
                }
            }

            if ( advertInto.has("rub") ) {
                String value=advertInto.getString("rub");
                if ( advertInto.has("type_home") ) {
                    value=value + " " + advertInto.getString("type_home");
                }
                advertTitle=value;
                titleField.setText(advertTitle);
                setTitle(advertTitle);

                mTracker.setScreenName("AdvertActivity "+advertTitle);
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }

            String price="";
            if ( advertInto.has("price") ) {
                String value = advertInto.getString("price");
                price=value;
                if ( advertInto.has("currency") ) {
                    value=value + " " + advertInto.getString("currency");
                }
                priceLabel.setText(value);
            }

            if ( advertInto.has("quadrature") ) {
                String value = advertInto.getString("quadrature");

                int averagePrice=Math.round(Integer.parseInt(price)/Integer.parseInt(value));
                roundNumber.setText(String.valueOf(averagePrice)+" EUR/ кв.м.");

                if ( advertInto.has("metric") ) {
                    value=value +" "+ advertInto.getString("metric");
                }
                gridValueTitle.add("Квадратура:");
                gridValue.add(value);
            }

            if ( advertInto.has("town") ) {
                String value = advertInto.getString("town");
                if ( advertInto.has("raion") ) {
                    value=value + ", " + advertInto.getString("raion");
                }
                placeLabel.setText(value);
            }

            if ( advertInto.has("additional_information") ) {
                moreInfoLabelExpand.setText(advertInto.getString("additional_information"));
            } else {
                moreInfoLabelExpand.setText("Няма допълнителна информация");
            }

            if ( advertInto.has("floor_regulation") ) {
                String value=advertInto.getString("floor_regulation");
                if ( advertInto.has("floor_max") ) {
                    value=value + " от " + advertInto.getString("floor_max");
                }
                gridValueTitle.add("Етаж:");
                gridValue.add(value);
            }

            if ( advertInto.has("phone_electricity") )  {
                String typeHome = advertInto.getString("type_home");
                if ( typeHome.contains(",")) {
                    typeHome = typeHome.replace(",", "");
                }
                int groupNumber= HelpFunctions.returnGroupNumberOfProperty(typeHome,null);
                if ( groupNumber==4 ) {
                    gridValueTitle.add("Електричество:");
                } else {
                    gridValueTitle.add("Телефон:");
                }
                gridValue.add(advertInto.getString("phone_electricity"));
            }

            if ( advertInto.has("tec_watter") ) {
                String typeHome = advertInto.getString("type_home");
                if ( typeHome.contains(",")) {
                    typeHome = typeHome.replace(",", "");
                }
                int groupNumber = HelpFunctions.returnGroupNumberOfProperty(typeHome,null);
                if ( groupNumber==4 ) {
                    gridValueTitle.add("Вода:");
                } else {
                    gridValueTitle.add("ТEЦ:");
                }
                gridValue.add(advertInto.getString("tec_watter"));
            }

            if (advertInto.has("agency")) {
                agencyField.setVisibility(View.VISIBLE);
                if (advertInto.has("broker_id")) {
                    if ( advertInto.has("broker_name") ) {
                        brokerName.setVisibility(View.VISIBLE);
                        brokerName.setText("Брокер: "+ advertInto.getString("broker_name"));
                    }
                }

                JSONObject agencyObject = advertInto.getJSONObject("agency");
                if ( agencyObject.has("name") ) {
                    agencyName.setVisibility(View.VISIBLE);
                    agencyName.setText("Агенция: "+ agencyObject.getString("name"));
                }

                if ( agencyObject.has("address") ) {
                    agencyAddress.setVisibility(View.VISIBLE);
                    agencyAddress.setText(agencyObject.getString("address"));
                }
            } else {
//                personName.setText("Частно лице");
                personField.setVisibility(View.VISIBLE);
            }

            if ( advertInto.has("extri") ) {
                JSONArray extriArr = advertInto.getJSONArray("extri");
                if ( extriArr.length()>0 ) {
                    gridValueTitle.add("Вид строителство:");
                    gridValue.add(extriArr.getString(0));
                }

                String extri="";
                if (extriArr.length()>1) {
                    for (int i=1; i<extriArr.length();i++ ) {
                        extri=extri+extriArr.getString(i)+", ";
                    }

                    String sourceString = "<b>Особености</b> ";
                    if (extri.length()>0) {
                        extri = extri.substring(0, extri.length()-2);
                        extriTitle.setVisibility(View.VISIBLE);
                        extriContent.setText(Html.fromHtml(sourceString+extri));
                    }
                }
            }

            if ( advertInto.has("phone") ) {
                advertPhone=advertInto.getString("phone");
            }


            if ( gridValue.size()>0 ) {
                GridViewPropertyFeaturesAdapter adapter = new GridViewPropertyFeaturesAdapter(AdvertActivity.this,gridValueTitle,gridValue);
                feturesGridView.setAdapter(adapter);
            }


            existsAdvertNotepad = SQLite.select().from(AdvertNotepad.class)
                    .where(AdvertNotepad_Table.advert_id.is(advertID))
                    .querySingle();

            if (mMenu!=null) {
                MenuItem itemFavourite = mMenu.findItem(R.id.addFavouritesBar);
                if ( itemFavourite!=null ) {
                    if (existsAdvertNotepad!=null) {
                        itemFavourite.setIcon(R.drawable.ic_favorite_white_24dp);
                    } else {
                        itemFavourite.setIcon(R.drawable.ic_favorite_black_24dp);
                    }
                }
            }

            existsAdvertAppointment = SQLite.select().from(PropertyVisits.class)
                    .where(PropertyVisits_Table.advert_id.is(advertID))
                    .querySingle();

            if (mMenu!=null) {
                MenuItem itemAppointmentActionButton = mMenu.findItem(R.id.appintmentActionButton);
                if ( itemAppointmentActionButton!=null ) {
                    if (existsAdvertAppointment!=null) {
                        itemAppointmentActionButton.setIcon(R.drawable.ic_date_range_white_24dp);
                    } else {
                        itemAppointmentActionButton.setIcon(R.drawable.ic_date_range_black_24dp);
                    }
                }
            }
        }
    }


//    public void hideKeyboard(View view) {
//        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        latitude=String.valueOf(location.getLatitude());
//        longitude=String.valueOf(location.getLongitude());
//    }
}
