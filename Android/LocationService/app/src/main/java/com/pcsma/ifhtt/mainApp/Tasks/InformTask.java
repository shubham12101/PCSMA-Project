package com.pcsma.ifhtt.mainApp.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pcsma.ifhtt.mainApp.Common;
import com.pcsma.ifhtt.mainApp.Listeners.OnInformTaskListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by vedantdasswain on 16/04/15.
 */
public class InformTask extends AsyncTask<Void,String,String> {

    private static final String TAG = "InformTask";
    OnInformTaskListener listener;
    String email,authToken,message;

    public InformTask(String email, String message, String authToken, OnInformTaskListener listener) {
        this.email = email;
        this.listener=listener;
        this.authToken=authToken;
        this.message=message;
    }

    @Override
    protected String doInBackground(Void... params) {
        String msg = "";
        msg = postInformInfo(email, message);
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {
        Log.i(TAG, msg);
        listener.onInformTaskComplete(msg);
    }

    private String postInformInfo(String email, String message){
        String msg="";
        HttpClient httpClient = new DefaultHttpClient();
        // replace with your url
        HttpPost httpPost = new HttpPost(Common.USER_API);
        httpPost.setHeader("Authorization","Token "+authToken);

        JSONObject postObject=new JSONObject();

        StringEntity se;
        try {
            postObject.put("msg",message);
            postObject.put("to",email);
            se = new StringEntity(postObject.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
            httpPost.setEntity(se);

            HttpResponse response = httpClient.execute(httpPost);
            // write response to log
//            Log.d(TAG,"Post user info"+ response.getStatusLine().toString())
            return "Post inform info"+ response.getStatusLine().toString();
//            Log.d(TAG, EntityUtils.toString(response.getEntity()));
        } catch (ClientProtocolException | UnsupportedEncodingException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return msg;
    }
}

