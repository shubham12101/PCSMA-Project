package com.pcsma.ifhtt.mainApp.Tasks;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by vedantdasswain on 31/03/15.
 */
public class RegisterTask extends AsyncTask<Void,Void,String> {
    Context context;
    String regid, username, email_ID;

    public RegisterTask(Context context, String regid, String username, String email_ID)
    {
        this.context=context;
        this.regid=regid;
        this.username=username;
        this.email_ID=email_ID;
    }

    @Override
    protected String doInBackground(Void... params) {
//        DefaultHttpClient httpclient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(Common.URL+Common.REG_API);

        return null;
    }
}
