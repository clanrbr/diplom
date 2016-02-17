package interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by macbook on 1/26/16.
 */
public interface AsyncResponseLoadAdvert {
    void processFinishLoadAdvert(JSONObject output) throws JSONException;
}
