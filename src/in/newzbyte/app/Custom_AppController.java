package in.newzbyte.app;
import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class Custom_AppController extends Application {

	 private RequestQueue requestQueue;
	 public static final boolean DEVELOPER_MODE = false;
	 private static Custom_AppController appControllerContext;
	 
	 @Override
	 public void onCreate() 
	 { 
	  super.onCreate();
	  System.out.println("AppController onCreate Called!");
	  appControllerContext = this;
	  
	  if (DEVELOPER_MODE) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.detectAll().penaltyDialog().build());
StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			.detectAll().penaltyDeath().build());
}
	}

	 }
	 
	 public static synchronized Custom_AppController getInstance()
	 {
		 return appControllerContext;
	 }
	 
	 public RequestQueue getRequestQueue()
	 {
	  if(requestQueue == null)
	  {
	   requestQueue = Volley.newRequestQueue(appControllerContext); 
	  }
	  return requestQueue;
	 }
	 
	 public <T> void addToRequestQueue(Request<T> req)
	 {
		 req.setRetryPolicy(new DefaultRetryPolicy(
				 	Globals.VOLLEY_TIMEOUT_MILLISECS, 
	                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
	                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		 getRequestQueue().add(req);
	 }
	 public void cancelPendingRequest(Object tag)
	 {
	  if(requestQueue != null)
	  {
	   requestQueue.cancelAll(tag);
	  }
	 }
	 
	
}
