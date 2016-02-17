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

import interfaces.AsyncResponseLoadAdvert;

/**
 * Created by macbook on 1/26/16.
 */
public class HTTPGETAdvert extends AsyncTask<String, Integer, String> {
    private Exception exception;
    private JSONObject advertInfo;
    private JSONArray advertsJsonArray;
    public AsyncResponseLoadAdvert delegate = null;

    private CircularProgressBar progressBar;
    private int asyncStarted;

    public HTTPGETAdvert(CircularProgressBar progressBar) {
        this.progressBar=progressBar;
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
//                conn.
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
        super.onPreExecute();
        if ( progressBar!=null ) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        if (progressBar!=null) {
            progressBar.setProgress(values[0]);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        advertInfo=null;
        if (result!=null) {
            advertsJsonArray = new JSONArray();
            Log.e("HEREHERE","ADVERTINFO");
            Log.e("HEREHERE",result);
            try {
                JSONObject json = new JSONObject(result);
                if (json.has("adverts") ) {
                    advertsJsonArray=json.getJSONArray("adverts");
                    if (advertsJsonArray.length()>0) {
                        advertInfo=advertsJsonArray.getJSONObject(0);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("HEREHERE","vryshta null tuk");
        }


        try {
            delegate.processFinishLoadAdvert(advertInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

