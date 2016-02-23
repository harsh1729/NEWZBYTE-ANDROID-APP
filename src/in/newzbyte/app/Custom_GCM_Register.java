package in.newzbyte.app;

import java.io.IOException;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;


@SuppressLint("NewApi")
public class Custom_GCM_Register{

	private GoogleCloudMessaging gcm;

	private Activity context = null;
	String gcmId;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	

	public static final String GCM_ID = "gcmIdNB";
	private final String KEY_GCM_PREF = "GCM_Prefs";
	TextView txtgcmId;

	
	public Custom_GCM_Register(Activity act) {
		context = act;
		if(checkPlayServices())
			registerGCM();
			
	}
	
	
	private boolean checkPlayServices() {
		Log.d("GCM",
				"checkPlayServices called");
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			/*
		if(!googlePlayServicesChecked()){
			Log.d("GCM", "checkplayservices connectionresult success");
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, context,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.d("GCM",
						"this device is not supported google play services !");
			
			}
			}
			*/
			return false;
		}
		return true;
	}

	
	  public void registerGCM() {
	  
	  gcm = GoogleCloudMessaging.getInstance(context); 
	  gcmId = getRegistrationId(context);
	  
	 }
	 

	  
	  private boolean googlePlayServicesChecked(){
			
			SharedPreferences prefs = this.context.getSharedPreferences(KEY_GCM_PREF, Context.MODE_PRIVATE);
			boolean  isAlreadyChecked = prefs.getBoolean("GPS_checked", false);
			
			//SharedPreferences.Editor editor = prefs.edit();
			//editor.putBoolean("GPS_checked", true);
			//editor.commit();
			
			return isAlreadyChecked;
			
		}
	  
	@SuppressLint("NewApi")
	private String getRegistrationId(Context context) {
		Log.i("GCM", "getRegistrationId start" );
		final SharedPreferences prefs = context.getSharedPreferences(
				KEY_GCM_PREF, Context.MODE_PRIVATE);
		
		String registrationId = prefs.getString(GCM_ID, "");
		
		if (registrationId.isEmpty()) {
			Log.i("GCM", "Saved Registration not found.");
			registerInBackground();
			return "";
		}
		
		return registrationId;
	}


	private void registerInBackground() {
		Log.i("GCM", "registerInBackground start" );
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "Success";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					gcmId = gcm.register(Globals.GCM_SENDER_ID);
                   // storeRegistrationId(context, gcmId);
                   
                    
				} catch (IOException ex) {
					Log.e("GCM", "Error: " + ex.getMessage());
					msg = "Error";
				}
				Log.i("GCM", "registerInBackground completed: " );
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Log.i("GCM", "registerInBackground completed: " );
				registerOnServer();
			}
		}.execute(null, null, null);
	}
	
	private void registerOnServer(){
		Log.i("GCM", " registerOnServer START !!");
		Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(Request.Method.POST,
				Custom_URLs_Params.getURL_PushNotification(), Custom_URLs_Params.getParams_PushNotification(gcmId, context),
						new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.i("GCM", "json registerOnServer recieved !!"
								+ response.toString());
						storeRegistrationId(context, gcmId);
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError err) {
						Log.i("GCM", "ERROR VolleyError registerOnServer");

					}
				});

		Custom_AppController.getInstance().addToRequestQueue(
				jsonObjectRQST);
	}
	private void storeRegistrationId(Context context, String gcmId) {
		
		final SharedPreferences prefs = context.getSharedPreferences(
				KEY_GCM_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(GCM_ID, gcmId);
		editor.commit();
	}
	

}
