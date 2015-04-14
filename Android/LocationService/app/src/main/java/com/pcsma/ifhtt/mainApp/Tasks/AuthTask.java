package com.pcsma.ifhtt.mainApp.Tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pcsma.ifhtt.mainApp.Common;
import com.pcsma.ifhtt.mainApp.Listeners.OnAuthTaskListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by vedantdasswain on 14/04/15.
 */
public class AuthTask extends AsyncTask<Void,String,String> {

    Context context;
    String token,authToken,username;
    long id;
    OnAuthTaskListener listener;

    private static final String TAG = "AuthTask";

    public AuthTask(Context context, String token,OnAuthTaskListener listener) {
        this.context = context;
        this.token = token;
        this.listener=listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.v(TAG,"Getting auth from backend");
        String msg = "";
        msg = postToken(token);
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {
        Log.i(TAG, "Retrieved token from backend: " + msg);
        if(msg.equals("Success")){
            listener.OnAuthTaskCompleted(authToken,username);
        }
    }


    private String postToken(String token) {
        String msg = "";
        HttpClient httpClient = new DefaultHttpClient();
        // replace with your url
        HttpPost httpPost = new HttpPost(Common.AUTH_API);
        httpPost.setHeader("Authorization", "Token " + token);

        try {
            HttpResponse response = httpClient.execute(httpPost);
            // write response to log
            Log.d(TAG, "Post token response: " + response.getStatusLine().toString());
            String responseBody = EntityUtils.toString(response.getEntity());
            Log.d(TAG, responseBody);
            if (response.getStatusLine().getStatusCode() == 200) {
                JSONObject authResponse = new JSONObject(responseBody);
                msg="Success";
                storeCredentials(authResponse);
            }
        } catch (ClientProtocolException | UnsupportedEncodingException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            // Log exception
            e.printStackTrace();
        }
        return msg;
    }

    private void storeCredentials(JSONObject authResponse) throws JSONException {
        authToken=authResponse.getString("token");
        id=authResponse.getLong("id");
        username=authResponse.getString("name");
        SharedPreferences appPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=appPreferences.edit();
        editor.putString("token",authToken);
        editor.putLong("id", id);
        editor.putString("username",username);
        editor.commit();

        Log.d(TAG,"Authenticated with: "+authToken);
    }
}
