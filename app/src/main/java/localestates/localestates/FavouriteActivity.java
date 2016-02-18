package localestates.localestates;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import fragments.NotepadFragment;
import fragments.PropertiesViewsFragment;
import fragments.SearchFragment;

/**
 * Created by macbook on 2/12/16.
 */
public class FavouriteActivity extends AppCompatActivity {
    private Handler mHandler;
    private TextView notepadFragmentButton;
    private TextView propertyViewFragmentButton;
    private TextView searchFragmentButton;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT>=21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_700));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);



        ImageView menuItemSearch = (ImageView) findViewById(R.id.searchActionBar);
        ImageView menuItemFavourite = (ImageView) findViewById(R.id.favouriteActionBar);
        ImageView menuItemNotification = (ImageView) findViewById(R.id.notificationActionBar);
        ImageView menuItemHome = (ImageView) findViewById(R.id.homeActionBar);
        menuItemFavourite.setImageResource(R.drawable.ic_favorite_white_24dp);

        menuItemHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favouriteIntent = new Intent(getBaseContext(), StartActivity.class);
                finish();
                startActivity(favouriteIntent);
            }
        });

        menuItemFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        mHandler = new Handler();

        notepadFragmentButton = (TextView) findViewById(R.id.notepadFragmentButton);
        propertyViewFragmentButton = (TextView) findViewById(R.id.propertyViewFragmentButton);
        searchFragmentButton = (TextView) findViewById(R.id.searchFragmentButton);

        notepadFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notepadFragmentButton.setTextColor(ContextCompat.getColor(FavouriteActivity.this, R.color.main_color_500));
                propertyViewFragmentButton.setTextColor(ContextCompat.getColor(FavouriteActivity.this, R.color.main_color_grey_900));
                searchFragmentButton.setTextColor(ContextCompat.getColor(FavouriteActivity.this, R.color.main_color_grey_900));
                fragment = NotepadFragment.newInstance();
                commitFragment(fragment);
            }
        });

        propertyViewFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notepadFragmentButton.setTextColor(ContextCompat.getColor(FavouriteActivity.this, R.color.main_color_grey_900));
                propertyViewFragmentButton.setTextColor(ContextCompat.getColor(FavouriteActivity.this, R.color.main_color_500));
                searchFragmentButton.setTextColor(ContextCompat.getColor(FavouriteActivity.this, R.color.main_color_grey_900));
                fragment = PropertiesViewsFragment.newInstance();
                commitFragment(fragment);
            }
        });

        searchFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notepadFragmentButton.setTextColor(ContextCompat.getColor(FavouriteActivity.this, R.color.main_color_grey_900));
                propertyViewFragmentButton.setTextColor(ContextCompat.getColor(FavouriteActivity.this, R.color.main_color_grey_900));
                searchFragmentButton.setTextColor(ContextCompat.getColor(FavouriteActivity.this, R.color.main_color_500));
                fragment = SearchFragment.newInstance();
                commitFragment(fragment);
            }
        });

        fragment = NotepadFragment.newInstance();
        commitFragment(fragment);
    }

    private class CommitFragmentRunnable implements Runnable {

        private Fragment fragment;

        public CommitFragmentRunnable(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void run() {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_main, fragment).commit();
        }
    }

    public void commitFragment(Fragment fragment) {
        mHandler.post(new CommitFragmentRunnable(fragment));
    }
}
