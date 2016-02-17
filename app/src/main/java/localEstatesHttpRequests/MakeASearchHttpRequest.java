package localEstatesHttpRequests;

import android.os.AsyncTask;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import android.view.View;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

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

import interfaces.AsyncResponse;

/**
 * Created by macbook on 1/19/16.
 */
public class MakeASearchHttpRequest extends AsyncTask<String, Integer, String> {
    private Exception exception;
    private ArrayList<JSONObject> advertsJsonArray;
    private String advertsNumber;
    private String advertsSearchText;
    private String arrayList;
    private SimpleArrayMap<String, String> mHeaders = new SimpleArrayMap<String,String>();
    private String responseBody;
    public AsyncResponse delegate = null;

    private CircularProgressBar progressBar;
    private int asyncStarted;

    public MakeASearchHttpRequest(CircularProgressBar progressBar, int asyncStarted) {

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
                progressBar.setVisibility(View.VISIBLE);
        }
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (progressBar!=null) {
            progressBar.setProgress(values[0]);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result!=null) {
            advertsJsonArray = new ArrayList<JSONObject>();
            try {
                JSONObject json = new JSONObject(result);
                if (json.has("adverts") ) {
                    arrayList=json.getString("adverts");
                }
                if (json.has("adverts_count") ) {
                    advertsNumber=json.getString("adverts_count");
                }
                if (json.has("search_text") ) {
                    advertsSearchText=json.getString("search_text");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("HEREHERE","vryshta null tuk");
        }

        delegate.processFinish(arrayList,advertsNumber,advertsSearchText);

    }
}



