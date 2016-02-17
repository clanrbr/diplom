package localEstatesHttpRequests;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import interfaces.AsyncResponseLandCategory;

/**
 * Created by macbook on 1/25/16.
 */
public class HTTPGETLandCategory extends AsyncTask<String, Void, String> {
    private Exception exception;
    private ArrayList<String> landCategoryArray;
    public AsyncResponseLandCategory delegate = null;

    private CircularProgressBar progressBar;
    private int asyncStarted;

    public HTTPGETLandCategory(CircularProgressBar progressBar, int asyncStarted) {

        this.progressBar=progressBar;
        this.asyncStarted=asyncStarted;
    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {

        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }

        return total.toString();
    }

    @Override
    protected String doInBackground(String... urls) {
        InputStream is = null;
        try {
            URL url= new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); /* milliseconds */
            conn.setConnectTimeout(15000); /* milliseconds */
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            if ( response==200 ) {
                is = conn.getInputStream();
                String contentAsString = readIt(is);
                Log.e("HEREHERE",contentAsString);
                return contentAsString;
            } else {
                return null;
            }
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        if (progressBar!=null) {
            if ( asyncStarted==0 ) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result!=null) {
            landCategoryArray=new ArrayList<String>();
            try {
                JSONArray  jsonArray = new JSONArray(result);
                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if ( jsonObject!=null && (jsonObject.getString("value").length()>0) ) {
                            landCategoryArray.add(jsonObject.getString("value"));
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("HEREHERE","vryshta null tuk");
        }

        delegate.processGetLandCategoryFinish(landCategoryArray);

    }
}
