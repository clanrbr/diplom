package localestates.localestates;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by macbook on 2/4/16.
 */
public class StreetViewActivity extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback {
    private String points=null;
    private String coordinates=null;
    private Toolbar mToolbarView;
    private StreetViewPanoramaFragment streetViewPanoramaFragment;

    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_700));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streetview);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String pointsExtra = extras.getString("points");
            if ( (pointsExtra!=null) && (!pointsExtra.equals("")) ) {
                points=pointsExtra;
                Log.e("HEREHERE","points");
                Log.e("HEREHERE",points);
            }

            String coordinatesExtra = extras.getString("coordinates");
            if ( (coordinatesExtra!=null) && (!coordinatesExtra.equals("")) ) {
                coordinates=coordinatesExtra;
                Log.e("HEREHERE","coordinates");
                Log.e("HEREHERE",coordinates);
            }

            String pointsTitle = extras.getString("title");
            if ( (pointsTitle!=null) && (!pointsTitle.equals("")) ) {
                setTitle(pointsTitle);
            }
        }

        streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.map);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(StreetViewActivity.this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        if ( (coordinates!=null) && coordinates.contains(",") ) {
            String[] coordsArray = coordinates.split(",");
            streetViewPanorama.setPosition(new LatLng(Double.parseDouble(coordsArray[0]), Double.parseDouble(coordsArray[1])));
        }
    }
}