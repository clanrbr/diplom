package localEstatesHttpRequests;

import android.os.AsyncTask;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ado on 11/30/2015.
 */
public class HTTPGetProperties extends AsyncTask<String, Integer, String> {
    private Exception exception;
    private SimpleArrayMap<String, String> mHeaders = new SimpleArrayMap<String,String>();
    private String responseBody;
    private CircularProgressBar progressBar;

    public HTTPGetProperties(CircularProgressBar progressBar) {
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
//            URL url = new URL(myurl);
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
                return contentAsString;
            } else {
                return null;
            }
        } catch (Exception e) {
            this.exception = e;
            Log.e("HEREHERE", "ne");
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (progressBar!=null) {
            progressBar.setProgress(values[0]);
        }
//        super.onProgressUpdate(values);
    }

//    @Override
//    protected void onPostExecute(String result) {
//        if (result!=null) {
//            Log.e("HEREHERE", "OT TUK LI GO PRINTI?");
//            Log.e("HEREHERE",result);
//        } else {
//            Log.e("HEREHERE", "EMPTY");
//        }
//
//    }
}
