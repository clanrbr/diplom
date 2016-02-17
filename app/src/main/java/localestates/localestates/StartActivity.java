package localestates.localestates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import io.fabric.sdk.android.Fabric;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.ArrayList;

import adapters.PropertiesArrayAdapter;
import application.LocalEstateApplication;
import localEstatesHttpRequests.HTTPGETCheckForLogIn;
import localEstatesHttpRequests.HTTPGetProperties;
import localEstatesHttpRequests.HTTPPostLogin;
import utils.SiCookieStore2;

public class StartActivity extends AppCompatActivity {

    private ArrayList<JSONObject> advertsJsonArray = new ArrayList<JSONObject>();
    private PropertiesArrayAdapter adapterProperties;
    private ListView listView;
    private CircularProgressBar progressBar;
    private TextView searchStartActivityButton;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT>=21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(StartActivity.this, R.color.main_color_700));
        }

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_start);

        LocalEstateApplication application = (LocalEstateApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Start Activity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        ImageView menuItemSearch = (ImageView) findViewById(R.id.searchActionBar);
        ImageView menuItemFavourite = (ImageView) findViewById(R.id.favouriteActionBar);
        ImageView menuItemNotification = (ImageView) findViewById(R.id.notificationActionBar);
        ImageView menuItemHome = (ImageView) findViewById(R.id.homeActionBar);
        menuItemHome.setImageResource(R.drawable.ic_home_white_24dp);

        menuItemHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


        progressBar = (CircularProgressBar)findViewById(R.id.progressBar);
        progressBar.setColor(ContextCompat.getColor(this, R.color.main_color_500));
//        circularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundProgressBarColor));
//        circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progressBarWidth));
//        circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.backgroundProgressBarWidth));
        int animationDuration = 2500; // 2500ms = 2,5s
        progressBar.setProgressWithAnimation(65, animationDuration);
        searchStartActivityButton= (TextView) findViewById(R.id.searchStartActivityButton);
        searchStartActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getBaseContext(),AdvanceSearchActivity.class);
                finish();
                startActivity(searchIntent);
            }
        });



        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent searchIntent = new Intent(StartActivity.this,AdvertActivity.class);
                searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                JSONObject property = advertsJsonArray.get(position);
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

        progressBar.setVisibility(View.VISIBLE);


        SiCookieStore2 siCookieStore = new SiCookieStore2(StartActivity.this);
        CookieManager cookieManager = new CookieManager((CookieStore) siCookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        HTTPGetProperties getProperty = new HTTPGetProperties(progressBar) {
            @Override
            protected void onPostExecute(String result) {
                progressBar.setVisibility(View.GONE);
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);

                        JSONArray jsonArray = json.getJSONArray("adverts");
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (jsonArray.getJSONObject(i) != null) {
                                    advertsJsonArray.add(jsonArray.getJSONObject(i));
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (advertsJsonArray!=null) {
//                        Attempt to invoke virtual method 'java.lang.Object android.content.Context.getSystemService(java.lang.String)' on a null object reference
                        // nai-veroqtno zaradi getActivity dava ??
                        adapterProperties = new PropertiesArrayAdapter(getBaseContext(),R.layout.property_single_item, advertsJsonArray);
                        listView.setAdapter(adapterProperties);
                        listView.setDivider(null);
                    }
                } else {
                    Log.e("HEREHERE", "EMPTY");
                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progressBar.setProgress(values[0]);
            }
        };

        getProperty.execute("http://api.imot.bg/mobile_api/search");



        HTTPPostLogin getLogin = new HTTPPostLogin(progressBar) {
            @Override
            protected void onPostExecute(String result) {
                progressBar.setVisibility(View.GONE);
                if (result != null) {
                    Log.e("HEREHERE",result);
                } else {
                    Log.e("HEREHERE", "EMPTY");
                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progressBar.setProgress(values[0]);
            }
        };
        getLogin.execute("http://api.imot.bg/mobile_api/users/login");


        HTTPGETCheckForLogIn checkLogin = new HTTPGETCheckForLogIn(progressBar) {
            @Override
            protected void onPostExecute(String result) {
                progressBar.setVisibility(View.GONE);
                if (result != null) {
                    Log.e("HEREHERE",result);
                } else {
                    Log.e("HEREHERE", "EMPTY");
                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progressBar.setProgress(values[0]);
            }
        };
        checkLogin.execute("http://api.imot.bg/mobile_api/users");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_start, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
