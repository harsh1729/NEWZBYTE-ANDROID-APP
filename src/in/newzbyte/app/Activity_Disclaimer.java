package in.newzbyte.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Disclaimer extends Activity {

	private String disclaimer= "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disclaimer);
		
		TextView txt = (TextView)findViewById(R.id.txtHeading);
		Typeface tf = Typeface.createFromAsset(getAssets(), Globals.FONT_ROBOTO);
		txt.setTypeface(tf);
		
		initWebView();
	}
	

	@SuppressLint("JavascriptInterface")
	private void initWebView(){
		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(
				getApplicationContext());
		if (!cd.isConnectingToInternet()) {

			Globals.showAlertDialogError(this,Globals.TEXT_CONNECTION_ERROR_HEADING,Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST);
			return;
		}else{
			Toast.makeText(this, "Loading Disclaimer, please wait!", Toast.LENGTH_SHORT).show();
			serverCallForDisclaimer();
		}
		
		/*
		
		myWebView = (WebView) findViewById(R.id.webView);
        
        //enable Javascript
        myWebView.getSettings().setJavaScriptEnabled(true);
         
        //loads the WebView completely zoomed out
        myWebView.getSettings().setLoadWithOverviewMode(true);
         
        //true makes the Webview have a normal viewport such as a normal desktop browser 
        //when false the webview will have a viewport constrained to it's own dimensions
        myWebView.getSettings().setUseWideViewPort(true);
         
        //override the web client to open all links in the same webview
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.setWebChromeClient(new MyWebChromeClient());
         
        //Injects the supplied Java object into this WebView. The object is injected into the 
        //JavaScript context of the main frame, using the supplied name. This allows the 
        //Java object's public methods to be accessed from JavaScript.
        myWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
         
        //load the home page URL
        myWebView.loadUrl("http://newzbyte.in/disclaimer/");
        
        TextView txt = (TextView)findViewById(R.id.txtHeading);
		Typeface tfCat = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_CAT_FONT);
		txt.setTypeface(tfCat);
		
		*/
		
		
	}
	
	private void serverCallForDisclaimer(){
	try {
		String url = Custom_URLs_Params.getURL_Disclaimer();
		Log.i("HARSH", "Cat serverCallForDisclaimer -- "+url);

		Custom_VolleyArrayRequest jsonObjectRQST = new Custom_VolleyArrayRequest(Request.Method.POST,
				//objAppConfig.getVersionNoCategory()
				url, null,
				new Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray response) {
				parseJson(response);
				

			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError err) {
				Log.i("DARSH", "ERROR VolleyError");
				Globals.showAlertDialogOneButton(
						Globals.TEXT_CONNECTION_ERROR_HEADING,
						Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
						Activity_Disclaimer.this, "OK", null, false);


			}
		});

		Custom_AppController.getInstance().addToRequestQueue(
				jsonObjectRQST);

	}

	catch (Exception e) {
		Log.i("HARSH",
				"Excetion FIRSTCALL" + e.getMessage() + "\n"
						+ e.getStackTrace());


	}
	}
	
	private void parseJson(JSONArray response){
		
		for(int i=0; i<response.length(); i++){
			JSONObject obj;
			try {
				obj = response.getJSONObject(i);

				disclaimer = obj.getString("text");
				 
				 
				 Activity_Disclaimer.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						TextView txt =(TextView) Activity_Disclaimer.this.findViewById(R.id.txtdisclaimer);
						txt.setText(Html.fromHtml(disclaimer));
						Typeface tf = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_FONT);
						txt.setTypeface(tf, Typeface.NORMAL);
						
					}
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	 public void goBack(View v){
		 this.finish();
	 }
	 
	/*
	//customize your web view client to open links from your own site in the 
	 //same web view otherwise just open the default browser activity with the URL
	 private class MyWebViewClient extends WebViewClient {
	     @Override
	     public boolean shouldOverrideUrlLoading(WebView view, String url) {
	         if (Uri.parse(url).getHost().contains("newzbyte.in")) {
	             return false;
	         }
	         Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	         startActivity(intent);
	         return true;
	     }
	 }
	  
	 private class MyWebChromeClient extends WebChromeClient {
	      
	  //display alert message in Web View
	  @Override
	     public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
	         Log.d(LOG_TAG, message);
	         new AlertDialog.Builder(view.getContext())
	          .setMessage(message).setCancelable(true).show();
	         result.confirm();
	         return true;
	     }
	 
	 }
	  
	 public class JavaScriptInterface {
	     Context mContext;
	 
	     // Instantiate the interface and set the context 
	     JavaScriptInterface(Context c) {
	         mContext = c;
	     }
	      
	     //using Javascript to call the finish activity
	     public void closeMyActivity() {
	         finish();
	     }
	      
	 }
	  
	 //Web view has record of all pages visited so you can go back and forth
	 //just override the back button to go back in history if there is page
	 //available for display
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
	         myWebView.goBack();
	         return true;
	     }
	     return super.onKeyDown(keyCode, event);
	 }
	 
	
	 
	 */
}
