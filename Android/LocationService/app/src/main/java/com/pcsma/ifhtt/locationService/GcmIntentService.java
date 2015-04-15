package com.pcsma.ifhtt.locationService;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pcsma.ifhtt.MainActivity;
import com.pcsma.ifhtt.R;

public class GcmIntentService extends IntentService {
	 DBAdapter db;
	static final String TAG = "GCMDemo";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            	sendNotification("Send : " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
            	sendNotification("Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                if(intent.getExtras().getString("collapse_key")!=null){
                    Log.v(TAG,"GCM received "+intent.getExtras().getString("msg")
                            +" "+intent.getExtras().getString("collapse_key"));
                    if(intent.getExtras().getString("collapse_key").equals("inform"))
                        sendNotification(intent.getExtras().getString("msg"));
                    else if(intent.getExtras().getString("collapse_key").equals("demo_location"))
                        handleLocNotif(intent,extras);
                    else
                        handleLocNotif(intent,extras);
                }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
    public void ShowToastInIntentService(final String sText)
    {  final Context MyContext = this;
       new Handler(Looper.getMainLooper()).post(new Runnable()
       {  @Override public void run()
          {  Toast toast1 = Toast.makeText(MyContext, sText, Toast.LENGTH_LONG);
             toast1.show(); 
          }
       });
    };

    private void handleLocNotif(Intent intent, Bundle extras){
        String myfloor;
        String rest = new String();

        if(intent.getExtras().getString("floor").equals("0")){
            myfloor = "Ground";
        }
        else{
            if (intent.getExtras().getString("floor").equals("1"))
                myfloor = intent.getExtras().getString("floor") + "st";
            else if (intent.getExtras().getString("floor").equals("2"))
                myfloor = intent.getExtras().getString("floor") + "nd";
            else if (intent.getExtras().getString("floor").equals("3"))
                myfloor = intent.getExtras().getString("floor") + "rd";
            else{
                myfloor = intent.getExtras().getString("floor") + "th";
            }
        }
        if(!intent.getExtras().getString("wing").isEmpty()){
            rest+=(intent.getExtras().getString("wing") + " Wing ");
        }

        if(!intent.getExtras().getString("room").isEmpty()){
            rest += " Room No: " + intent.getExtras().getString("room") ;
        }


        sendNotification("Location Changed: New Location is " + intent.getExtras().getString("building")+ " Building " + myfloor + " Floor " + rest);
        ShowToastInIntentService("Location Changed");


        db = new DBAdapter(this);

        db.open();
        Long id = db.insertEnrty(Integer.parseInt(intent.getExtras().getString("uid")), intent.getExtras().getString("time"), intent.getExtras().getString("ap_id"), intent.getExtras().getString("label"), intent.getExtras().getString("building"), intent.getExtras().getString("floor"), intent.getExtras().getString("wing"), intent.getExtras().getString("room"));
        db.close();

        Log.i(TAG, "Received: " + extras.toString());
    }
    
    private void sendNotification(String msg) {
    	long[] pat = { 0, 200, -1 };
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.iiitd_logo)
        .setContentTitle("IFHTT Update")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setVibrate(pat)
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}

