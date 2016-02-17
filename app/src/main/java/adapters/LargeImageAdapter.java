package adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import localestates.localestates.R;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by macbook on 2/1/16.
 */

public class LargeImageAdapter extends PagerAdapter {

    private ArrayList<String> imageArray;
    private Context context;
//    private static final int[] sDrawables = { R.drawable.noproperty, R.drawable.noproperty, R.drawable.noproperty,
//            R.drawable.noproperty, R.drawable.noproperty, R.drawable.noproperty };

    public LargeImageAdapter(Context context, ArrayList<String> imageArray) {
        this.imageArray=imageArray;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageArray.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        Picasso.with(context).load(imageArray.get(position)).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(photoView);

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}