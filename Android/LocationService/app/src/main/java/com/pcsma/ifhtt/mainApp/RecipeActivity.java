package com.pcsma.ifhtt.mainApp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TimePicker;
import com.pcsma.ifhtt.R;
import com.pcsma.ifhtt.mainApp.Listeners.OnLocationReceivedListener;
import com.pcsma.ifhtt.mainApp.Methods.JSONParser;
import com.pcsma.ifhtt.mainApp.Objects.LocationObject;
import com.pcsma.ifhtt.mainApp.Tasks.GetLocationTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class RecipeActivity extends ActionBarActivity implements OnLocationReceivedListener {
    String startTimeString,endTimeString;
    Button locationButton;
    Button startTimeButton;
    Button endTimeButton;
    GetLocationTask getLocationTask;
    List<LocationObject> locationObjectList;
    TimePickerDialog timePickerDialogStart;
    TimePickerDialog timePickerDialogEnd;
    private static String TAG;
    int hourStart, minStart, hourEnd, minEnd;
    Calendar calendarStart,calendarEnd;
    SimpleDateFormat simpleDateFormatStart,simpleDateFormatEnd;

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
    }

    private TimePickerDialog.OnTimeSetListener timeSetListenerStart = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hourStart = hourOfDay;
            minStart = minute;
            Log.d(TAG,hourStart+" "+minStart);
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

    public void makeListDialog(List<LocationObject> locList)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayAdapter<LocationObject> arrayAdapter = new ArrayAdapter<LocationObject>(
                RecipeActivity.this,android.R.layout.select_dialog_singlechoice);
        for(int i=0; i<locationObjectList.size(); ++i)
        {
            Log.d(TAG, locationObjectList.get(i).toString());
            arrayAdapter.add(locList.get(i));
        }
        builder.setAdapter(arrayAdapter,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG,arrayAdapter.getItem(which).toString());
                locationButton.setText(arrayAdapter.getItem(which).toString());
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
