package com.pcsma.ifhtt.locationService;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pcsma.ifhtt.MainActivity;
import com.pcsma.ifhtt.R;
import com.pcsma.ifhtt.mainApp.ActionObject;
import com.pcsma.ifhtt.mainApp.Common;
import com.pcsma.ifhtt.mainApp.Database.DbFunctions;
import com.pcsma.ifhtt.mainApp.Listeners.OnGetTaskListener;
import com.pcsma.ifhtt.mainApp.Listeners.OnInformTaskListener;
import com.pcsma.ifhtt.mainApp.Objects.LocationObject;
import com.pcsma.ifhtt.mainApp.RecipeActions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GcmIntentService extends IntentService implements OnGetTaskListener,OnInformTaskListener {
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
                    Log.v(TAG, "GCM received " + intent.getExtras().getString("msg")
                            + " " + intent.getExtras().getString("collapse_key"));
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

        checkLocation(intent);

        db = new DBAdapter(this);

        db.open();
        try
        {
            Long id = db.insertEnrty(Integer.parseInt(intent.getExtras().getString("uid")), intent.getExtras().getString("time"), intent.getExtras().getString("ap_id"), intent.getExtras().getString("label"), intent.getExtras().getString("building"), intent.getExtras().getString("floor"), intent.getExtras().getString("wing"), intent.getExtras().getString("room"));
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        db.close();

        Log.i(TAG, "Received: " + extras.toString());
    }

    private void checkLocation(Intent intent){
        LocationObject loc=new LocationObject();
        loc.setBuilding(intent.getExtras().getString("building")+" Building");
        loc.setFloor("Floor "+intent.getExtras().getString("floor"));
        loc.setRoom("Room "+intent.getExtras().getString("room"));
        loc.setWing("Wing "+intent.getExtras().getString("wing"));
        Log.d(TAG,"locationBuilding"+loc.getBuilding());
        Log.d(TAG,"locationFloor"+loc.getFloor());
        Log.d(TAG,"locationWing"+loc.getWing());
        Log.d(TAG,"locationRoom"+loc.getRoom());
        ArrayList<ActionObject> actionObjectArrayList = DbFunctions.getActions(getApplicationContext(), loc.toString());
        Log.v(TAG,"Size "+actionObjectArrayList.size());
        if(actionObjectArrayList!=null)
        {
            for(int i=0;i<actionObjectArrayList.size();++i)
                Log.d(TAG,actionObjectArrayList.get(i).getAction());
        }
        else
            Log.d(TAG,"null found");
        filterActionObject(actionObjectArrayList);
    }

    public void filterActionObject(ArrayList<ActionObject> list)
    {
        Log.d(TAG,"in filterActionObject");
        final SharedPreferences appPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = appPreferences.getString("token",null);
        RecipeActions recipeActions = new RecipeActions(getApplicationContext(),token);
        ActionObject actionObject;
        for(int i=0; i<list.size(); ++i)
        {
            Log.d(TAG,"i:"+i);
            actionObject = list.get(i);
            Log.d(TAG,"actionObject:"+actionObject.toString());
            switch (actionObject.getAction()) {
                case Common.ACTION_SILENT:
                    recipeActions.phoneSilent();
                    break;

                case Common.ACTION_VIBRATE:
                    recipeActions.phoneVibrate();
                    break;

                case Common.ACTION_COURSE:
                    recipeActions.openCourseWebsite(GcmIntentService.this, actionObject.getOption1());
                    break;

                case Common.ACTION_MESS_MENU:
                    recipeActions.openMenuNotif(GcmIntentService.this);
                    break;

                case Common.ACTION_LIBRARY:
                    recipeActions.openLibraryNotif(GcmIntentService.this);
                    break;

                case Common.ACTION_MAX_POP:
                    recipeActions.openPopulationNotif(GcmIntentService.this, actionObject.getOption1());
                    break;

                case Common.ACTION_MIN_POP:
                    recipeActions.openPopulationNotif(GcmIntentService.this, actionObject.getOption1());
                    break;

                case Common.ACTION_MESSAGE:
                    recipeActions.postInformNotif(GcmIntentService.this, actionObject.getOption1(),
                            actionObject.getOption2());
                    break;

                default:
                    Toast.makeText(GcmIntentService.this,"No action found",Toast.LENGTH_LONG).show();
                    break;
            }
        }
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

    @Override
    public void onTaskCompleted(String message, String type) {
        if(type.equals(Common.ACTION_COURSE))
        {
            Log.d(TAG,message);
            try {
                JSONObject jsonObject = new JSONObject(message);
                String websiteURL = jsonObject.getString("website");
                Log.d(TAG, websiteURL);
                openBrowser(websiteURL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals(Common.ACTION_MESS_MENU))
        {
            Log.d(TAG,message);
            try {
                JSONObject jsonObject = new JSONObject(message);
                String menuItems = jsonObject.getString("items");
                String timeSlot = jsonObject.getString("time_slot");
                Log.d(TAG, menuItems);
                makeMenuNotification(timeSlot, menuItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals(Common.ACTION_MAX_POP))
        {
            Log.d(TAG,message);
            try{
                JSONObject jsonObject = new JSONObject(message);
                String building = jsonObject.getString("building");
                String floor = jsonObject.getString("floor");
                String wing = jsonObject.getString("wing");
                String room = jsonObject.getString("room");
                makePopulationNotification(building,floor,wing,room,"Most");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals(Common.ACTION_MIN_POP))
        {
            Log.d(TAG,message);
            try{
                JSONObject jsonObject = new JSONObject(message);
                String building = jsonObject.getString("building");
                String floor = jsonObject.getString("floor");
                String wing = jsonObject.getString("wing");
                String room = jsonObject.getString("room");
                makePopulationNotification(building,floor,wing,room,"Least");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals(Common.ACTION_LIBRARY))
        {
            try{
                JSONArray jsonArray = new JSONArray(message);
                StringBuilder booksName = new StringBuilder();
                for(int i=0; i<jsonArray.length();++i)
                {
                    JSONObject json = jsonArray.getJSONObject(i);
                    booksName.append(json.getString("title"));
                    booksName.append(" ");
                }
                makeLibraryNotification(booksName.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void openBrowser(String url)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void makeLibraryNotification(String books)
    {
        long[] pattern = { 0, 200, -1 };
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.iiitd_logo)
                .setContentTitle("Issued Books")
                .setContentText(books).setVibrate(pattern).setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        int notifId = 002;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notifId,notifBuilder.build());
    }

    public void makePopulationNotification(String b, String f, String w, String r, String type)
    {
        long[] pattern = { 0, 200, -1 };
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.iiitd_logo)
                .setContentTitle(type+" Populated Place")
                .setContentText(b + " Building, Floor " + f + ", Wing " + w + ", Room " + r).setVibrate(pattern).setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        int notifId = 002;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notifId,notifBuilder.build());
    }

    public void makeMenuNotification(String timeSlot,String menuItems)
    {
        long[] pattern = { 0, 200, -1 };
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.iiitd_logo)
                .setContentTitle("Mess Menu for "+timeSlot).setContentText(menuItems).setVibrate(pattern).setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        int notifId = 001;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notifId,notifBuilder.build());
    }

    @Override
    public void onInformTaskComplete(String msg) {
        if(msg.equals("200"))
            Toast.makeText(GcmIntentService.this,"Message Pushed Successfully",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(GcmIntentService.this,"User not found",Toast.LENGTH_LONG).show();
    }
}

