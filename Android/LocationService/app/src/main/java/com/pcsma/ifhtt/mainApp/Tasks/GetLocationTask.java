package com.pcsma.ifhtt.mainApp.Tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.pcsma.ifhtt.mainApp.Common;
import com.pcsma.ifhtt.mainApp.Listeners.OnLocationReceivedListener;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Shubham on 15 Apr 15.
 */
public class GetLocationTask extends AsyncTask<Void,Void,String> {

    public static String TAG;
    private String authToken;
    private OnLocationReceivedListener listener;

    public GetLocationTask(String token,OnLocationReceivedListener listener) {
        TAG = this.getClass().getSimpleName();
        this.authToken = token;
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG,"response received:"+s);
        if(s!=null) {
            listener.OnLocationReceived(s);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(Common.LOCATION_API);
        httpGet.setHeader("Authorization", "Token " + authToken);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                Log.d(TAG, result.toString());
                return result.toString();
            }
            else
            {
                Log.d(TAG, "server error code" + response.getStatusLine().getStatusCode());
                return null;
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
