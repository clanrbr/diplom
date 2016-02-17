package localestates.localestates;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by macbook on 1/28/16.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String points=null;
    private String coordinates=null;
    private Toolbar mToolbarView;
    private MapFragment mapFragment;

    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_700));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
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

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if ( (coordinates!=null) && coordinates.contains(",") ) {
            String[] coordsArray = coordinates.split(",");
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(coordsArray[0]), Double.parseDouble(coordsArray[1])) )
                    .title("Marker"));

            LatLng cur_Latlng=new LatLng(Double.parseDouble(coordsArray[0]), Double.parseDouble(coordsArray[1])); // giving your marker to zoom to your location area.
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }
}


