package com.pcsma.ifhtt.mainApp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.pcsma.ifhtt.MainActivity;
import com.pcsma.ifhtt.R;
import com.pcsma.ifhtt.mainApp.Listeners.OnGCMRegisterListener;
import com.pcsma.ifhtt.mainApp.Listeners.OnGetTaskListener;
import com.pcsma.ifhtt.mainApp.Tasks.GCMRegisterTask;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends ActionBarActivity implements OnGetTaskListener,OnGCMRegisterListener{

    private static final String TAG = "HomeActivity";
    public static final String IFHTT_REG_ID = "registration_id_ifhtt";

    Button silentButton;
    Button vibrateButton;
    Button courseButton;
    Button menuButton;
    Button recipeButton;


    private RecipeActions recipeActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recipeActions= new RecipeActions(HomeActivity.this);

        silentButton = (Button) findViewById(R.id.button_silent);
        vibrateButton = (Button) findViewById(R.id.button_vibrate);
        courseButton = (Button) findViewById(R.id.button_course);
        menuButton = (Button) findViewById(R.id.button_menu);
        recipeButton = (Button) findViewById(R.id.button_recipe);

        silentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeActions.phoneSilent();
            }
        });

        vibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeActions.phoneVibrate();
            }
        });

        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeActions.openCourseWebsite(HomeActivity.this,"");
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeActions.openMenuNotif(HomeActivity.this);
//                makeNotification("Demo");
            }
        });
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,RecipeActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.action_gcm_register){
            sendRegistrationIdToIFHTTBackend();
            return true;
        }
        else if(id==R.id.action_recipes){
            Intent intent=new Intent(HomeActivity.this,RecipeListActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void sendRegistrationIdToIFHTTBackend() {
        final SharedPreferences prefs = getGCMPreferences(this);
        String registrationId = prefs.getString(IFHTT_REG_ID, "");
        String mEmail = prefs.getString("Email","");
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        (new GCMRegisterTask(HomeActivity.this,registrationId,android_id,mEmail,this)).execute();
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    @Override
    public void onTaskCompleted(String message, String type) {
        if(type.equals("course"))
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
        else if (type.equals("menu"))
        {
            Log.d(TAG,message);
            try {
                JSONObject jsonObject = new JSONObject(message);
                String menuItems = jsonObject.getString("items");
                String timeSlot = jsonObject.getString("time_slot");
                Log.d(TAG, menuItems);
                makeNotification(timeSlot,menuItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void openBrowser(String url)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void makeNotification(String timeSlot,String menuItems)
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
    public void onTaskCompleted(String message) {
        Log.v(TAG,"GCM Registered successfully");
    }
}
