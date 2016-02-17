package localEstatesHttpRequests;

import android.os.AsyncTask;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import android.view.View;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import interfaces.AsyncResponseExtri;

/**
 * Created by macbook on 1/25/16.
 */
public class HTTPGETExtri extends AsyncTask<String, Void, String> {
    private Exception exception;
    private ArrayList<String> extriArray;
    private String advertsNumber;
    private String advertsSearchText;
    private String arrayList;
    private SimpleArrayMap<String, String> mHeaders = new SimpleArrayMap<String,String>();
    private String responseBody;
    public AsyncResponseExtri delegate = null;

    private CircularProgressBar progressBar;
    private int asyncStarted;

    public HTTPGETExtri(CircularProgressBar progressBar, int asyncStarted) {

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
            extriArray=new ArrayList<>();
            JSONArray  jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                extriArray = new ArrayList<>();
                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                    extriArray.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("HEREHERE",jsonArray.toString());

        } else {
            Log.e("HEREHERE","vryshta null tuk");
        }

        delegate.processGetExtriFinish(extriArray);

    }
}




