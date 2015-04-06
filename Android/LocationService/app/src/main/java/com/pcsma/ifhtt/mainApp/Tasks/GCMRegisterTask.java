package com.pcsma.ifhtt.mainApp.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pcsma.ifhtt.mainApp.Common;
import com.pcsma.ifhtt.mainApp.Listeners.OnGCMRegisterListener;

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
 * Created by vedantdasswain on 31/03/15.
 */
public class GCMRegisterTask extends AsyncTask<Void,Void,String> {
    private static final String TAG ="GCMRegister Task" ;
    Context context;
    String regid,devid, email_ID;
    OnGCMRegisterListener listener;

    public JSONObject deviceObject=new JSONObject();

    public GCMRegisterTask(Context context, String regid, String devid, String email_ID,OnGCMRegisterListener listener)
    {
        this.context=context;
        this.regid=regid;
        this.devid=devid;
        this.email_ID=email_ID;
        this.listener=listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        String msg = "";
        buildObject();
        msg = postGCMInfo(deviceObject);
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {
        Log.i(TAG, msg);
        listener.onTaskCompleted(msg);
    }


    private void buildObject(){
        try {
            deviceObject.put("dev_id",devid);
            deviceObject.put("reg_id",regid);
            deviceObject.put("name",email_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String postGCMInfo(JSONObject deviceObject){
        String msg="";
        HttpClient httpClient = new DefaultHttpClient();
        // replace with your url
        HttpPost httpPost = new HttpPost(Common.GCM_REG_API);

        StringEntity se;
        try {
            se = new StringEntity(deviceObject.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
            httpPost.setEntity(se);

            HttpResponse response = httpClient.execute(httpPost);
            return "Post device info"+ response.getStatusLine().toString();
        } catch (ClientProtocolException | UnsupportedEncodingException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }
        return msg;
    }
}
