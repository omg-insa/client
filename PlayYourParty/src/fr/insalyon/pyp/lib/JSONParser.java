package fr.insalyon.pyp.lib;

import java.io.InputStream;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;
 
public class JSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    // constructor
    private JSONParser() {
 
    }
 
    public static JSONObject getJSONObject(String response) {

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(response);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
 
    }
}