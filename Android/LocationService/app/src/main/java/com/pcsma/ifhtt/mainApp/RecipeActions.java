package com.pcsma.ifhtt.mainApp;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by Shubham on 28 Mar 15.
 */
public class RecipeActions {

    private Context context;

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

    public void openCourseWebsite()
    {

    }


}
