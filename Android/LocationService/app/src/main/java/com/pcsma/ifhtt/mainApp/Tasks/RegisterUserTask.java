package com.pcsma.ifhtt.mainApp.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.pcsma.ifhtt.mainApp.Common;
import com.pcsma.ifhtt.mainApp.Listeners.OnRegisterUserTaskListener;

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
 * Created by vedantdasswain on 14/04/15.
 */
public class RegisterUserTask extends AsyncTask<Void,String,String> {

    private static final String TAG = "RegisterUserTask";
    OnRegisterUserTaskListener listener;
    String email,authToken;

    public RegisterUserTask(String email,String authToken,OnRegisterUserTaskListener listener) {
        this.email = email;
        this.listener=listener;
        this.authToken=authToken;
    }
    
    @Override
    protected String doInBackground(Void... params) {
            String msg = "";
            msg = postUserInfo(email);
            return msg;
            }

    @Override
    protected void onPostExecute(String msg) {
            Log.i(TAG, msg);
            listener.OnRegistrationComplete(msg);
            }

    private String postUserInfo(String email){
        String msg="";
        HttpClient httpClient = new DefaultHttpClient();
        // replace with your url
        HttpPost httpPost = new HttpPost(Common.USER_API);
        httpPost.setHeader("Authorization","Token "+authToken);

        JSONObject postObject=new JSONObject();

        StringEntity se;
        try {
            postObject.put("username",email);
            se = new StringEntity(postObject.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
            httpPost.setEntity(se);

            HttpResponse response = httpClient.execute(httpPost);
            // write response to log
//            Log.d(TAG,"Post user info"+ response.getStatusLine().toString())
            return "Post user info"+ response.getStatusLine().toString();
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
