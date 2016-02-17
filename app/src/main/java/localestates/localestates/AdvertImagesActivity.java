package localestates.localestates;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import adapters.LargeImageAdapter;
import utils.HackyViewPager;

/**
 * Created by macbook on 2/1/16.
 */
public class AdvertImagesActivity extends AppCompatActivity {

    private static final String ISLOCKED_ARG = "isLocked";

    private ViewPager mViewPager;
    private MenuItem menuLockItem;
    private ArrayList<String> advertPictureArray;
    private Toolbar mToolbarView;

    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_700));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_activty);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String advertArrayString = extras.getString("advertPictures");
            if ( (advertArrayString!=null) && (!advertArrayString.equals("")) ) {
                try {
                    JSONArray imgArray= new JSONArray(advertArrayString);
                    if ( imgArray.length()>0 ) {
                        advertPictureArray = new ArrayList<>();
                        for(int i=0;i<imgArray.length();i++) {
                            advertPictureArray.add(imgArray.getString(i));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String advertTitle = extras.getString("advertTitle");
            if (advertTitle!=null) {
                setTitle(advertTitle);
            }
        }

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

        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new LargeImageAdapter(AdvertImagesActivity.this,advertPictureArray));

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }

    }

    private void toggleViewPagerScrolling() {
        if (isViewPagerActive()) {
            ((HackyViewPager) mViewPager).toggleLock();
        }
    }

//    private void toggleLockBtnTitle() {
//        boolean isLocked = false;
//        if (isViewPagerActive()) {
//            isLocked = ((HackyViewPager) mViewPager).isLocked();
//        }
//        String title = (isLocked) ? getString(R.string.menu_unlock) : getString(R.string.menu_lock);
//        if (menuLockItem != null) {
//            menuLockItem.setTitle(title);
//        }
//    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }
}
