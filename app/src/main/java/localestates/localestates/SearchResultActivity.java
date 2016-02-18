package localestates.localestates;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import adapters.PropertiesArrayAdapter;
import db.PropertyVisits;
import db.PropertyVisits_Table;
import db.SearchNotepad;
import interfaces.AsyncResponse;
import listeners.InfiniteScrollListener;
import localEstatesHttpRequests.MakeASearchHttpRequest;
import utils.HelpFunctions;

/**
 * Created by macbook on 1/20/16.
 */
public class SearchResultActivity extends ActionBarActivity implements AsyncResponse {

    private ArrayList<JSONObject> results;
    private String numberOfAdverts;
    private String searchText;
    private ListView listView;
    private PropertiesArrayAdapter adapterProperties;
    private TextView resultTextView;
    private LinearLayout searchTextHolder;
    private TextView editSearchButton;
    private TextView saveSearchNotepadButton;
    private ArrayList<HashMap<String,String>> searchValues;
    private MakeASearchHttpRequest asyncTask;

    private CircularProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_700));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ImageView menuItemSearch = (ImageView) findViewById(R.id.searchActionBar);
        menuItemSearch.setImageResource(R.drawable.ic_search_white_24dp);
        ImageView menuItemFavourite = (ImageView) findViewById(R.id.favouriteActionBar);
        ImageView menuItemNotification = (ImageView) findViewById(R.id.notificationActionBar);
        ImageView menuItemHome = (ImageView) findViewById(R.id.homeActionBar);

        menuItemHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getBaseContext(),StartActivity.class);
                finish();
                startActivity(homeIntent);
            }
        });

        menuItemFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favouriteIntent = new Intent(getBaseContext(),FavouriteActivity.class);
                finish();
                startActivity(favouriteIntent);
            }
        });

        menuItemNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favouriteIntent = new Intent(getBaseContext(),FavouriteActivity.class);
                finish();
                startActivity(favouriteIntent);

            }
        });

        menuItemSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getBaseContext(),AdvanceSearchActivity.class);
                finish();
                startActivity(searchIntent);
            }
        });

        editSearchButton = (TextView) findViewById(R.id.editSearchButton);
        editSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent advanceSearchResultIntent = new Intent(SearchResultActivity.this,AdvanceSearchActivity.class);
                advanceSearchResultIntent.putExtra("searchValues",searchValues);
                finish();
                startActivity(advanceSearchResultIntent);
            }
        });

        saveSearchNotepadButton = (TextView) findViewById(R.id.saveSearchNotepadButton);
        saveSearchNotepadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( searchValues!=null ) {
                    final String criteriaInString = HelpFunctions.convertSearchToString(searchValues);

                    final Dialog dialog=new Dialog(SearchResultActivity.this,android.R.style.Theme_DeviceDefault_Light_NoActionBar);
                    dialog.setContentView(R.layout.notepad_search_add_pop_up);
                    final EditText titleSearch = (EditText) dialog.findViewById(R.id.titleSearch);
                    final EditText noteText = (EditText) dialog.findViewById(R.id.noteText);
                    TextView saveAppDialog = (TextView) dialog.findViewById(R.id.saveSearchDialog);
                    TextView closeAppointmentDialog = (TextView) dialog.findViewById(R.id.closeSearchDialog);

                    // Check if no view has focus:
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    saveAppDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ( criteriaInString.length()>0 ) {
                                long unixTime = System.currentTimeMillis() / 1000L;
                                SearchNotepad searchNotepad = new SearchNotepad();
                                searchNotepad.search_title=titleSearch.getText().toString();
                                searchNotepad.search_content=criteriaInString;
                                searchNotepad.search_note=noteText.getText().toString();
                                searchNotepad.search_text_help_text=searchText;
                                searchNotepad.search_add_time=unixTime;
                                searchNotepad.save();
                                Toast.makeText(SearchResultActivity.this,"Търсенето беше успешно добавено в бележника!",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SearchResultActivity.this,"Възникна грешка!",Toast.LENGTH_LONG).show();
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

                    dialog.show();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            results = null;
        } else {
            String results = extras.getString("results");
            numberOfAdverts = extras.getString("results_numberOfAdverts");
            searchText = extras.getString("results_text");
            searchValues = (ArrayList<HashMap<String, String>>) extras.getSerializable("searchValues");

            if (results!=null) {
                this.results = new ArrayList<>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(results);
                    if (jsonArray.length()>0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (jsonArray.getJSONObject(i) != null) {
                                this.results.add(jsonArray.getJSONObject(i));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("HEREHERE","NULL");
            }
        }

        asyncTask = new MakeASearchHttpRequest(progressBar,0);
        asyncTask.delegate = this;

        resultTextView = (TextView) findViewById(R.id.resultTextView);
        searchTextHolder = (LinearLayout) findViewById(R.id.searchTextHolder);
        listView = (ListView) findViewById(R.id.listView);
        if (results!=null) {
            adapterProperties = new PropertiesArrayAdapter(getBaseContext(),R.layout.property_single_item, results);
            listView.setAdapter(adapterProperties);
            listView.setDivider(null);

            int startFromElement=5;

            if ( (numberOfAdverts!=null) && (Integer.parseInt(numberOfAdverts)<10) ) {
                startFromElement=10;
            }

            listView.setOnScrollListener(new InfiniteScrollListener(startFromElement) {
                @Override
                public void loadMore(int page, int totalItemsCount) {
                    if ( (numberOfAdverts!=null) && (totalItemsCount<Integer.parseInt(numberOfAdverts)) ) {
                        if (searchValues!=null) {
                            Log.e("HEREHERE","LOADING MORE");
                            Log.e("HEREHERE",String.valueOf(totalItemsCount));
                            Log.e("HEREHERE",String.valueOf(page));
                            searchValues.get(1).put("page", String.valueOf(Integer.parseInt(searchValues.get(1).get("page")) + 1) );
                            String searchUrl = HelpFunctions.convertToUrl(searchValues);
                            Log.e("HEREHERE",searchUrl);
                            asyncTask.execute(searchUrl);
                        }
                    } else {
                        Log.e("HEREHERE","noMoreToload");
                    }
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent searchIntent = new Intent(SearchResultActivity.this,AdvertActivity.class);
                searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                JSONObject property = results.get(position);
                if ( property.has("id") ) {
                    try {
                        searchIntent.putExtra("advertID",property.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                startActivity(searchIntent);
            }
        });

        if ( (searchText!=null) && (!searchText.equals("") ) ) {
            Log.e("HEREHERE",searchText);
            resultTextView.setText(searchText);
            searchTextHolder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void processFinish(String output, String numberOfAdverts, String searchText) {
        if (output!=null) {
            ArrayList<JSONObject> moreResults = new ArrayList<>();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(output);
                if (jsonArray.length()>0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (jsonArray.getJSONObject(i) != null) {
                            moreResults.add(jsonArray.getJSONObject(i));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if ( (moreResults!=null) && (moreResults.size()>0)  ) {
                results.addAll(moreResults);

                adapterProperties.notifyDataSetChanged();

                asyncTask = new MakeASearchHttpRequest(progressBar,0);
                asyncTask.delegate = this;
            }
        }

    }
}
