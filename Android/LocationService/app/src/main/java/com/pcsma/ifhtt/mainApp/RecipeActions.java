package com.pcsma.ifhtt.mainApp;

import android.content.Context;
import android.media.AudioManager;
import com.pcsma.ifhtt.mainApp.Listeners.OnGetTaskListener;

/**
 * Created by Shubham on 28 Mar 15.
 */
public class RecipeActions {

    private Context context;
    private static final String COURSE_API_PARAM_KEY = "course_name";
    private static final String COURSE_API_PARAM_VALUE = "Media Security";
    private static final String MENU_API_PARAM_KEY = "time";
    private static String MENU_API_PARAM_VALUE;

    public RecipeActions(Context cnt)
    {
        context = cnt;
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

    public void openCourseWebsite(OnGetTaskListener onGetTaskListener)
    {
//        GetTask getCourseURL = new GetTask(context,Common.COURSES_API,COURSE_API_PARAM_KEY,COURSE_API_PARAM_VALUE,onGetTaskListener,"course");
//        getCourseURL.execute();
    }

    public void openMenuNotif(OnGetTaskListener onGetTaskListener)
    {
        MENU_API_PARAM_VALUE = String.valueOf(System.currentTimeMillis());
//        GetTask getCourseURL = new GetTask(context,Common.MENU_API,MENU_API_PARAM_KEY,MENU_API_PARAM_VALUE,onGetTaskListener,"menu");
//        getCourseURL.execute();
    }

}
