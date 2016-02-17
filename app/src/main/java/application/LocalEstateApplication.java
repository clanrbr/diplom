package application;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.raizlabs.android.dbflow.config.FlowManager;
import localestates.localestates.R;

/**
 * Created by macbook on 2/2/16.
 */
public class LocalEstateApplication extends Application {
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker("UA-73919450-1");
            mTracker.enableAutoActivityTracking(true);
        }
        return mTracker;
    }

}