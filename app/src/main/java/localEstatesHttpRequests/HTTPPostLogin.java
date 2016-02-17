package localEstatesHttpRequests;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by macbook on 2/5/16.
 */
public class HTTPPostLogin extends AsyncTask<String, Integer, String> {
    private Exception exception;
    private JSONObject advertInfo;
    private JSONArray advertsJsonArray;
//    public AsyncResponseLoadAdvert delegate = null;

    private CircularProgressBar progressBar;
    private int asyncStarted;

    public HTTPPostLogin(CircularProgressBar progressBar) {

        this.progressBar=progressBar;
    }

//    PersistentCookieStore cookie = new PersistentCookieStore();

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
//        CookieManager cookieManager = CookieManager.getInstance();
//        CookieHandler.setDefault(cookieManager);

        try {
            URL url= new URL(urls[0]);
//            String params=urls[1];
            String params="username=clan_rbr@yahoo.com&password=rbrrbr&remember_login=1";

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); /* milliseconds */
            conn.setConnectTimeout(15000); /* milliseconds */
            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(params);
            writer.flush();
            writer.close();
            os.close();
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
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        if ( result!=null ) {
            Log.e("HEREHERE",result);
        } else {
            Log.e("HEREHERE","vryshta null tuk");
        }


//        try {
//            delegate.processFinishLoadAdvert(advertInfo);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }
}