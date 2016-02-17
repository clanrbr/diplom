package localEstatesHttpRequests;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import interfaces.AsyncResponseTec;

/**
 * Created by macbook on 2/5/16.
 */
public class HTTPGETCheckForLogIn extends AsyncTask<String, Integer, String> {
    private Exception exception;
    private ArrayList<CharSequence> tecArray;
    public AsyncResponseTec delegate = null;

    private CircularProgressBar progressBar;

    public HTTPGETCheckForLogIn(CircularProgressBar progressBar) {

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
    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress(values[0]);
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
                Log.e("HEREHERE","Something");
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
    protected void onPostExecute(String result) {
        if (result!=null) {
            Log.e("HEREHERE","login");
            Log.e("HEREHERE",result);

        } else {
            Log.e("HEREHERE","vryshta null tuk");
        }
    }
}