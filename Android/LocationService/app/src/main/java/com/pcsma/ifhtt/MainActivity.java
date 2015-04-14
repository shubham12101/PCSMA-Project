package com.pcsma.ifhtt;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pcsma.ifhtt.locationService.Contact;
import com.pcsma.ifhtt.locationService.DBAdapter;
import com.pcsma.ifhtt.locationService.SNMPHttpsClient;
import com.pcsma.ifhtt.mainApp.Common;
import com.pcsma.ifhtt.mainApp.HomeActivity;
import com.pcsma.ifhtt.mainApp.Listeners.OnAuthTaskListener;
import com.pcsma.ifhtt.mainApp.Listeners.OnGCMRegisterListener;
import com.pcsma.ifhtt.mainApp.Listeners.OnGetUsernameListener;
import com.pcsma.ifhtt.mainApp.Listeners.OnRegisterUserTaskListener;
import com.pcsma.ifhtt.mainApp.Tasks.AuthTask;
import com.pcsma.ifhtt.mainApp.Tasks.GCMRegisterTask;
import com.pcsma.ifhtt.mainApp.Tasks.GetUsernameTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends ActionBarActivity
        implements OnGCMRegisterListener,OnGetUsernameListener,OnAuthTaskListener,OnRegisterUserTaskListener{

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;
    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String IFHTT_REG_ID = "registration_id_ifhtt";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static SharedPreferences sharedPreferences;
    private static final String launch = "firstLaunch";
    String mEmail; // Received from newChooseAccountIntent(); passed to getToken()
    private static final String SCOPE =
            "oauth2:https://www.googleapis.com/auth/userinfo.profile " +
                    "https://www.googleapis.com/auth/userinfo.email";



    String SENDER_ID = "76480130894";
    String SENDER_ID_IFHTT="1061491278864";
	
	static final String TAG = "MainActivity";

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid,regid_ifhtt;
	String address;
    String uid;

    Button but1, histBut;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		but1 = (Button) findViewById(R.id.button1);
		histBut = (Button) findViewById(R.id.buttonHist);
		
		//WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		//WifiInfo info = manager.getConnectionInfo();
		//address = info.getMacAddress();		
		//address = "e8:4e:84:8f:53:3c";
		//String type = "uid";
		//String url = "https://192.168.207.129:9000/uid?mac=" + address +"&token=75bde082336602fde7bb121058d01449ea5b3edb2d9f3cd924882a31f0235dde";
		//new AndroidHTTPGet().execute(url,type);
		final SharedPreferences sprefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(Common.sprefs,Context.MODE_PRIVATE);
        if(!(sharedPreferences.getBoolean(launch,true)))
        {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
//        else
//        {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean(launch,false);
//            editor.commit();
//        }
	    Boolean rStart = sprefs.getBoolean("is_start", false);
	    if(rStart==true){
	    	but1.setText(R.string._stop);
	    }
	    else{
	    	but1.setText(R.string._start);
	    }
	    	
		
		but1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Boolean rStart = sprefs.getBoolean("is_start", false);
				if(rStart==true){
					String url = "https://192.168.1.40:9136/deregister";
					//String url = "https://192.168.96.128:9000/deregister";
				    String type = "register";
					new AndroidHTTPGet().execute(url,type);
					SharedPreferences.Editor editor = sprefs.edit();
				    editor.putBoolean("is_start", false);
				    editor.commit();
				    but1.setText(R.string._start);
			    }
			    else{
			    	context = getApplicationContext();

					   if (checkPlayServices()) {
				            gcm = GoogleCloudMessaging.getInstance(context);

                           regid_ifhtt=getRegistrationId(context,IFHTT_REG_ID);
                           if (regid_ifhtt.isEmpty()) {
                               new registerInIFHTTBackground().execute();
                           }
                           else
                                Log.d(TAG,"regid_ifhtt:"+regid_ifhtt);

				            regid = getRegistrationId(context,PROPERTY_REG_ID);

				            if (regid.isEmpty()) {
				                new registerInBackground().execute();
				            }

                           sendRegistrationIdToBackend();
				        } else {
				            Log.i(TAG, "No valid Google Play Services APK found.");
				        }
			    	SharedPreferences.Editor editor = sprefs.edit();
				    editor.putBoolean("is_start", true);
				    editor.commit();
				    but1.setText(R.string._stop);
			    }
			}
			
		});
		final Context myctx = getApplicationContext();
		histBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ArrayList<String> lab_array = new ArrayList<String>();
				
				DBAdapter db;
				 db = new DBAdapter(myctx);

	       		 db.open();
	       		 List<Contact> allEntries = db.getAllContacts();
	       		 db.close();
				
				Contact contact = new Contact();
				
				for(int i=0; i<allEntries.size(); i++) {
					
					contact = allEntries.get(i);
					String stripTime = contact.getTime();
					if(contact.getTime().length()>20)
					{
						stripTime = contact.getTime().substring(0, 19);
					}
					int serialno = i+1;
					String item = "Log entry: " + serialno + " Building: " + contact.getBuilding() + " Floor: " + contact.getFloor() + " Wing: "+ contact.getWing() + " Room: "+ contact.getRoom() + " Time: "+ stripTime;
					lab_array.add(item);

				}  
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				intent.setAction(".ListViewActivity");
				bundle.putStringArrayList("lab_array", lab_array);
				intent.putExtra("bundle", bundle);
				
				startActivity(intent);  
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}
	
	@Override
	protected void onPause() {
		super.onPause();		
	}
		
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	private String getRegistrationId(Context context,String PROPERTY) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY, "");
	    if (registrationId.isEmpty()) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	
	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
	    return getSharedPreferences(MainActivity.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}

    @Override
    public void onTaskCompleted(String message) {
        Log.v(TAG,"GCM registered successfully");
    }

    @Override
    public void onSignInTaskCompleted(String token) {
        Log.v(TAG,token);
        (new AuthTask(this,token,this)).execute();
    }

    @Override
    public void OnAuthTaskCompleted(String authToken, String username) {
//        (new RegisterUserTask(mEmail,authToken,this)).execute();
    }

    @Override
    public void OnRegistrationComplete(String msg) {
        Log.v(TAG,msg);
    }


    /**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private class registerInBackground extends AsyncTask<Void, Void, String> {
			@Override
	        protected String doInBackground(Void... params) {
	            String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                regid = gcm.register(SENDER_ID);
	                msg = "Device registered, registration ID=" + regid;

	                storeRegistrationId(context, regid,PROPERTY_REG_ID);
	                
	                sendRegistrationIdToBackend();

	                
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	            }
	            return msg;
	        }
	        @Override
	        protected void onPostExecute(String msg) {
//	            mDisplay.append(msg + "\n");
                Log.v(TAG,msg);
	        }
	}

    private class registerInIFHTTBackground extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            Log.d(TAG,"ifhtt registration task");
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid_ifhtt = gcm.register(SENDER_ID_IFHTT);
                msg = "Device registered with IFHTT, registration ID=" + regid_ifhtt;

                // Persist the regID - no need to register again.
                storeRegistrationId(context, regid_ifhtt,IFHTT_REG_ID);

                sendRegistrationIdToIFHTTBackend();


            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }
        @Override
        protected void onPostExecute(String msg) {
//            mDisplay.append(msg + "\n");
            Log.v(TAG,msg);
        }
    }
	
	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	 * or CCS to send messages to your app. Not needed for this demo since the
	 * device sends upstream messages to a server that echoes back the message
	 * using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    //String url = "https://192.168.96.128:9000/register?appid="+ registrationId;
		String url = "https://192.168.1.40:9136/register?appid="+ registrationId;
	    String type = "register";
		new AndroidHTTPGet().execute(url,type);
	}


    private void sendRegistrationIdToIFHTTBackend() {
        String email_id="vedant12118@iiitd.ac.in";
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(IFHTT_REG_ID, "");
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        (new GCMRegisterTask(MainActivity.this,registrationId,android_id,email_id,this)).execute();
    }
	
	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId,String REG_ID) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.i(TAG, "Saving regId on app version " + appVersion+" "+REG_ID+" "+regId);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	
	private class AndroidHTTPGet extends AsyncTask<String, Void, String[]>{
		@Override
		protected String[] doInBackground(String... args) {
			
			// Instantiate the custom HttpClient
			DefaultHttpClient client = new SNMPHttpsClient(getApplicationContext());
			
			HttpGet get = new HttpGet(args[0]);
			// Execute the GET call and obtain the response
			HttpResponse getResponse;
			StringBuilder sb = new StringBuilder();
	        
			try {
				getResponse = client.execute(get);
				HttpEntity responseEntity = getResponse.getEntity();
			    InputStream is = responseEntity.getContent();
			    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(sb!=null){
				String[] myRet = new String[2];
				myRet[0] = sb.toString();
				myRet[1] = args[1];
				return myRet;
			}
			else{
				String[] myRet = new String[2];
				myRet[0] = "invalid";
				myRet[1] = args[1];
				return myRet;
				
			}
		}
		@Override
		protected void onPostExecute(String[] result) {
			try {
				if(result[1].equals("uid"))
				{
					JSONObject retrievedJson = new JSONObject(result[0]);
				
					uid = retrievedJson.getString("uid");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}

	}

    public void onSignIn(View v){
        pickUserAccount();
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                // With the account name acquired, go get the auth token
                getUsername();
            } else if (resultCode == RESULT_CANCELED) {
                // The account picker dialog closed without selecting an account.
                // Notify users that they must pick an account to proceed.
                Toast.makeText(this, R.string.pick_account, Toast.LENGTH_SHORT).show();
            }
        }
        // Later, more code will go here to handle the result from some exceptions...
    }

    private void getUsername() {
        if (mEmail == null) {
            pickUserAccount();
        } else {
                new GetUsernameTask(this, mEmail, SCOPE).execute();
            }
    }

    /**
     * This method is a hook for background threads and async tasks that need to
     * provide the user a response UI when an exception occurs.
     */
    public void handleException(final Exception e) {
        // Because this call comes from the AsyncTask, we must ensure that the following
        // code instead executes on the UI thread.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException)e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            MainActivity.this,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

}