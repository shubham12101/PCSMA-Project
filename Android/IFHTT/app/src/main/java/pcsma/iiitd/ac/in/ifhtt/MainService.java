package pcsma.iiitd.ac.in.ifhtt;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Shubham on 05 Mar 15.
 */
public class MainService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MainService(String name) {
        super(name);
    }

    public MainService() {
        super("MainService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
