package com.pcsma.ifhtt.mainApp;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.pcsma.ifhtt.R;
import com.pcsma.ifhtt.mainApp.Database.DbFunctions;
import com.pcsma.ifhtt.mainApp.Listeners.OnGetTaskListener;
import com.pcsma.ifhtt.mainApp.Listeners.OnInformTaskListener;
import com.pcsma.ifhtt.mainApp.Listeners.OnLocationReceivedListener;
import com.pcsma.ifhtt.mainApp.Methods.JSONParser;
import com.pcsma.ifhtt.mainApp.Objects.LocationObject;
import com.pcsma.ifhtt.mainApp.Tasks.GetLocationTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class RecipeActivity extends ActionBarActivity implements OnLocationReceivedListener,OnGetTaskListener,
        OnInformTaskListener
{
    String startTimeString,endTimeString,actionString;
    Button locationButton;
    Button startTimeButton;
    Button endTimeButton,actionButton,okButton,saveButton;
    GetLocationTask getLocationTask;
    List<LocationObject> locationObjectList;
    TimePickerDialog timePickerDialogStart;
    TimePickerDialog timePickerDialogEnd;
    EditText option1EditText, option2EditText;
    private static String TAG;
    int hourStart, minStart, hourEnd, minEnd, choice;
    Calendar calendarStart,calendarEnd;
    SimpleDateFormat simpleDateFormatStart,simpleDateFormatEnd;
    ActionObject actionObject;
    String recipeLocation;
    RecipeActions recipeActions;

    public RecipeActivity()
    {
        TAG = this.getClass().getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        final SharedPreferences appPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = appPreferences.getString("token",null);
        recipeActions = new RecipeActions(RecipeActivity.this,token);
        if(token!=null)
        {
            getLocationTask = new GetLocationTask(token,RecipeActivity.this);
        }
        else
        {
            Log.d(TAG, "token not found in SharedPref");
        }
        calendarStart = new GregorianCalendar();
        calendarEnd = new GregorianCalendar();
        locationButton = (Button) findViewById(R.id.button_location);
        startTimeButton = (Button) findViewById(R.id.button_time_start);
        endTimeButton = (Button) findViewById(R.id.button_time_end);
        actionButton = (Button) findViewById(R.id.button_set_action);
        saveButton = (Button) findViewById(R.id.button_save);
        okButton = (Button) findViewById(R.id.button_okay);
        option1EditText = (EditText) findViewById(R.id.editText_option1);
        option2EditText = (EditText) findViewById(R.id.editText_option2);
        option1EditText.setVisibility(View.GONE);
        option2EditText.setVisibility(View.GONE);
        timePickerDialogStart = new TimePickerDialog(this,timeSetListenerStart,hourStart,minStart,true);
        timePickerDialogEnd = new TimePickerDialog(this,timeSetListenerEnd,hourEnd,minEnd,true);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationTask.execute();
            }
        });
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogStart.show();
            }
        });
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogEnd.show();
            }
        });
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeActionDialog();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertActionObject();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeActionObject(choice);
            }
        });
    }

    private void insertActionObject()
    {
        if(choice==2)
            actionObject = new ActionObject(recipeLocation,startTimeString,endTimeString,actionString,
                    option1EditText.toString(),"");
        else if(choice==7)
            actionObject = new ActionObject(recipeLocation,startTimeString,endTimeString,actionString,
                    option1EditText.toString(),option2EditText.toString());
        else
            actionObject = new ActionObject(recipeLocation,startTimeString,endTimeString,actionString,
                    "","");
        DbFunctions.insert(RecipeActivity.this,actionObject);

    }

    private TimePickerDialog.OnTimeSetListener timeSetListenerStart = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hourStart = hourOfDay;
            minStart = minute;
            Log.d(TAG, hourStart + " " + minStart);
            calendarStart.set(Calendar.HOUR, hourStart);
            calendarStart.set(Calendar.MINUTE,minStart);
            simpleDateFormatStart = new SimpleDateFormat("HH:mm");
            startTimeString = simpleDateFormatStart.format(calendarStart.getTimeInMillis()).toString();
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListenerEnd = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hourEnd = hourOfDay;
            minEnd = minute;
            Log.d(TAG,hourEnd+" "+minEnd);
            calendarEnd.set(Calendar.HOUR,hourEnd);
            calendarEnd.set(Calendar.MINUTE,minEnd);
            simpleDateFormatEnd = new SimpleDateFormat("HH:mm");
            endTimeString = simpleDateFormatEnd.format(calendarEnd.getTimeInMillis()).toString();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnLocationReceived(String msg) {
        JSONParser jsonParser = new JSONParser(msg);
        locationObjectList = jsonParser.parse();
        makeListDialog(locationObjectList);
    }

    public void makeActionDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                RecipeActivity.this,android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Put phone to Silent Mode");
        arrayAdapter.add("Put phone to Vibrate Mode");
        arrayAdapter.add("Open Course Website");
        arrayAdapter.add("Get Mess Menu");
        arrayAdapter.add("Get Issued Books from Library");
        arrayAdapter.add("Get Maximum populated location");
        arrayAdapter.add("Get Minimum populated location");
        arrayAdapter.add("Inform a friend by Notification");
        builder.setAdapter(arrayAdapter,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice = which;
                if(which==2)
                {
                    option1EditText.setVisibility(View.VISIBLE);
                    option1EditText.setHint("Enter the Course Name");
                }
                else if(which==7)
                {
                    option1EditText.setVisibility(View.VISIBLE);
                    option2EditText.setVisibility(View.VISIBLE);
                    option1EditText.setHint("Enter the email of the receiver");
                    option2EditText.setHint("Enter the message to be received");
                }
                else
                {
                    option1EditText.setVisibility(View.GONE);
                    option2EditText.setVisibility(View.GONE);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void makeActionObject(int n)
    {
        Log.d(TAG,"position: "+n);
        switch (n)
        {
            case 0:
                recipeActions.phoneSilent();
                actionString=Common.ACTION_SILENT;
                break;
            case 1:
                recipeActions.phoneVibrate();
                actionString=Common.ACTION_VIBRATE;
                break;
            case 2:
                recipeActions.openCourseWebsite(RecipeActivity.this, option1EditText.getText().toString());
                actionString=Common.ACTION_COURSE;
                break;
            case 3:
                recipeActions.openMenuNotif(RecipeActivity.this);
                actionString = Common.ACTION_MESS_MENU;
                break;
            case 4:
                recipeActions.openLibraryNotif(RecipeActivity.this);
                actionString=Common.ACTION_LIBRARY;
                break;
            case 5:
                recipeActions.openPopulationNotif(RecipeActivity.this,"max");
                actionString=Common.ACTION_MAX_POP;
                break;
            case 6:
                recipeActions.openPopulationNotif(RecipeActivity.this,"min");
                actionString=Common.ACTION_MIN_POP;
                break;
            case 7:
                recipeActions.postInformNotif(RecipeActivity.this,option1EditText.getText().toString(),
                        option2EditText.getText().toString());
                actionString=Common.ACTION_MESSAGE;
                break;

        }
    }

    public void makeListDialog(List<LocationObject> locList)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayAdapter<LocationObject> arrayAdapter = new ArrayAdapter<>(
                RecipeActivity.this,android.R.layout.select_dialog_singlechoice);
        for(int i=0; i<locationObjectList.size(); ++i)
        {
            arrayAdapter.add(locList.get(i));
        }
        builder.setAdapter(arrayAdapter,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                locationButton.setText(arrayAdapter.getItem(which).toString());
                recipeLocation = arrayAdapter.getItem(which).toString();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
            Toast.makeText(RecipeActivity.this,"Message Pushed Successfully",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(RecipeActivity.this,"User not found",Toast.LENGTH_LONG).show();
    }
}
