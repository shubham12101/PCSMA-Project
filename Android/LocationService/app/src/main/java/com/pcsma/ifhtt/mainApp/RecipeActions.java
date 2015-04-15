package com.pcsma.ifhtt.mainApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import com.pcsma.ifhtt.mainApp.Listeners.OnGetTaskListener;
import android.preference.PreferenceManager;

import com.pcsma.ifhtt.mainApp.Listeners.OnGetTaskListener;
import com.pcsma.ifhtt.mainApp.Listeners.OnInformTaskListener;
import com.pcsma.ifhtt.mainApp.Tasks.GetTask;
import com.pcsma.ifhtt.mainApp.Tasks.InformTask;

/**
 * Created by Shubham on 28 Mar 15.
 */
public class RecipeActions {

    private static final String TAG ="RecipeActions" ;
    private Context context;
    private static final String COURSE_API_PARAM_KEY = "course_name";
    private static String COURSE_API_PARAM_VALUE = "";
    private static final String MENU_API_PARAM_KEY = "time";
    private static String MENU_API_PARAM_VALUE;
    private static final String LIBRARY_API_PARAM_KEY = "";
    private static String LIBRARY_API_PARAM_VALUE="";
    private static final String POPULATION_API_PARAM_KEY = "population";
    private static String POPULATION_API_PARAM_VALUE="";
    private String authToken;

    public RecipeActions(Context cnt)
    {
        context = cnt;
        final SharedPreferences appPreferences= PreferenceManager.getDefaultSharedPreferences(cnt);
        authToken = appPreferences.getString("token","");
    }

    public void phoneVibrate()
    {
        AudioManager am;
    am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
}

    public void phoneSilent()
    {
        AudioManager am;
        am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    public void openCourseWebsite(OnGetTaskListener onGetTaskListener, String courseName)
    {
//        GetTask getCourseURL = new GetTask(context,Common.COURSES_API,COURSE_API_PARAM_KEY,COURSE_API_PARAM_VALUE,onGetTaskListener,"course");
//        getCourseURL.execute();
        COURSE_API_PARAM_VALUE=courseName;
        GetTask getCourseURL = new GetTask(context,Common.COURSES_API,COURSE_API_PARAM_KEY,
                COURSE_API_PARAM_VALUE,onGetTaskListener,"course",authToken);
        getCourseURL.execute();
    }

    public void openMenuNotif(OnGetTaskListener onGetTaskListener)
    {
        MENU_API_PARAM_VALUE = String.valueOf(System.currentTimeMillis());
//        GetTask getCourseURL = new GetTask(context,Common.MENU_API,MENU_API_PARAM_KEY,MENU_API_PARAM_VALUE,onGetTaskListener,"menu");
//        getCourseURL.execute();
        GetTask getMenuURL = new GetTask(context,Common.MENU_API,MENU_API_PARAM_KEY,
                MENU_API_PARAM_VALUE,onGetTaskListener,"menu",authToken);
        getMenuURL.execute();
    }

    public void openLibraryNotif(OnGetTaskListener onGetTaskListener)
    {
        GetTask getLibraryURL = new GetTask(context,Common.LIBRARY_API,LIBRARY_API_PARAM_KEY,
                LIBRARY_API_PARAM_VALUE,onGetTaskListener,"library",authToken);
        getLibraryURL.execute();
    }

    public void openPopulationNotif(OnGetTaskListener onGetTaskListener, String param)
    {
        GetTask getPopulationURL = new GetTask(context,Common.LOCATION_API,POPULATION_API_PARAM_KEY,
                POPULATION_API_PARAM_VALUE,onGetTaskListener,"population",authToken);
        getPopulationURL.execute();
    }

    public void postInformNotif(OnInformTaskListener listener, String email,String message)
    {
        (new InformTask(email,message,authToken,listener)).execute();
    }

}
