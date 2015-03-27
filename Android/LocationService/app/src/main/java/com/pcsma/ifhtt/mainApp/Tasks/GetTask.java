package com.pcsma.ifhtt.mainApp.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pcsma.ifhtt.mainApp.Listeners.OnGetTaskListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Shubham on 28 Mar 15.
 */
public class GetTask extends AsyncTask<Void,Void,String> {

    private static final String TAG ="GetTask" ;
    Context context;
    String URL, paramKey, paramValue;
    OnGetTaskListener listener;

    public GetTask(Context context, String URL, String paramKey, String paramValue, OnGetTaskListener listener)
    {
        this.context=context;
        this.URL = URL;
        this.paramKey = paramKey;
        this.paramValue = paramValue;
        this.listener=listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        String msg = "";
//                Log.v(TAG,"Doing in background");
        msg = getAction(URL, paramKey, paramValue);
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {
        Log.i(TAG, msg);
        listener.onTaskCompleted(msg);
    }

    private String getAction(String URL, String paramKey, String paramValue){
        String msg="";
        HttpClient httpClient = new DefaultHttpClient();

//        Log.v(TAG,"Get movies");

        HttpGet httpGet = new HttpGet(URL);
//        httpGet.setHeader("Authorization","Token "+authToken);

        HttpParams params=httpGet.getParams();
        params.setParameter(paramKey,paramValue);
        httpGet.setParams(params);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            // write response to log
            Log.d(TAG,"Get Task: "+ response.getStatusLine().toString());
//            Log.d(TAG, EntityUtils.toString(response.getEntity()));
            return EntityUtils.toString(response.getEntity());
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
