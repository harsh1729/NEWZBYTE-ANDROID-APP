package in.newzbyte.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class Globals {

	public static final int APP_TRUE = 1;
	public static final int APP_FALSE = 0;
	

	public final static String APP_TITLE = "Newz Byte App";
	public final static String APP_PNAME = "in.newzbyte.app";
	public final static String SHARE_URL = "https://play.google.com/store/apps/details?id="+APP_PNAME;
	public final static String GCM_SENDER_ID = "82788197303";
	public static final String DEFAULT_APP_SERVER_PATH= "http://newstest2.tk/client_requests/";//"http://xbnews.in/newsci/client_requests/";//"http://newstest2.tk/client_requests/";//;
	public final static String SERVER_TIME_ZONE = "GMT+05:30";
	

	public static final String TEXT_LOADING_FROM_PREVIOUS_SESSION ="Loading news from previous session, check your network connection and try again!";
	public static final String TEXT_CONNECTION_ERROR_HEADING = "Error in Connection";
	public static final String TEXT_CONNECTION_ERROR_DETAIL_TOAST ="Please check your internet connection and try again.";
	public static final String TEXT_NO_INTERNET_HEADING = "No Internet Connectivity";
	public static final String TEXT_NO_INTERNET_DETAIL_TOAST= "No Internet Connectivity, please connect to a network.";
	
	public static final String TEXT_NOTIFICATION_ENABLED = "NOTIFICATION ON";
	public static final String TEXT_NOTIFICATION_DISABLED= "NOTIFICATION OFF";
	public static final int VOLLEY_TIMEOUT_MILLISECS = 15000;
	public static final int CLIENT_ID = 4;
	public static final int FINAL_NEWS_LIMIT_FIRST_CALL = 50;
	public static final int FINAL_NEWS_LIMIT_LOAD_OLD = 50;
	public static final int FINAL_NEWS_LIMIT_LOAD_NEW = 50;
	
	public static final int IMAGE_ALIGN_LEFT = 1;
	public static final int IMAGE_ALIGN_CENTER = 2;
	public static final int IMAGE_ALIGN_RIGHT = 3;
	
	public static final String CALL_TYPE_OLD = "old";
	public static final String CALL_TYPE_NEW = "new";
	
	public static final String DEFAULT_FONT = "OpenSans-Regular.ttf";
	public static final String DEFAULT_CAT_FONT = "ArchivoBlack.otf";//"Chunkfive.otf";//"Typoline_Expanded_demo.otf";//
	
	public static final int[] SHARE_INTENT_ITEMS = {
														R.drawable.share_plus, 
														R.drawable.share_gmail,
														R.drawable.share_messenger, 
														R.drawable.share_fb, 
														R.drawable.share_twitter, 
														R.drawable.share_whats_app
													};
	
	public static String getShareAppMsg() {
		return "Friends, check out this awesome news app . ";
	}
	
	
	@SuppressLint("NewApi")
	static public Point getScreenSize(Activity currentActivity){
		Display display = currentActivity.getWindowManager().getDefaultDisplay();
		Point size = new Point();


		if (android.os.Build.VERSION.SDK_INT >= 13) 
		{
			display.getSize(size);
		} 
		else 
		{
			 size.x = display.getWidth();
			 size.y = display.getHeight();
		}
		
		return size;
	}
	
	
	static public Boolean getBoolFromInt(int val){
		if(val > 0)
			return true;
		return false;
	}
	static public int getIntFromBool(Boolean val){
		if(val)
			return 1;
		
		return 0;
	}
	
	static public Bitmap scaleToWidth(Bitmap bitmap,int scaledWidth) {
		if (bitmap != null) {

			int bitmapHeight = bitmap.getHeight(); 
			int bitmapWidth = bitmap.getWidth(); 

			// scale According to WIDTH
			int scaledHeight = (scaledWidth * bitmapHeight) / bitmapWidth;

			try {

				bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
						scaledHeight, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	
	static public int getTxtViewRowsByImgHgt(int txtHeight,int lineSpacingDiff,int imgHeight)
	{
		int rows = 0;
		rows = (imgHeight-lineSpacingDiff)/txtHeight;
		//rows = rows+1;
		
		int rem = imgHeight%txtHeight;
		if(rem > 0)
			rows++;
		
		return rows;
	}
	
	
	
	static public String getStringFromResources(Context con, int resourceId){
		return con.getResources().getString(resourceId);
	}
	static public void showAlertDialogError(final Activity activity){
		
		showAlertDialogError(activity,"Error","Please try again.");
		
	}
	
	static public void showAlertDialogError(final Activity activity , String title, String msg){
	
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				dialog.cancel();
				activity.finish();
			}
		};
		
		Globals.showAlertDialog(title,msg ,activity,"OK", listener,null,null,false);
	}
	
	
static public void showAlertDialogOneButton(String title,String msg,Context context,String positiveButtonText,DialogInterface.OnClickListener listnerPositive,Boolean isCancelable){
		
		Globals.showAlertDialog(title,msg ,context,positiveButtonText, listnerPositive,null,null,isCancelable);
	}

	static public void showAlertDialog(String title,String msg,Context context,String positiveButtonText,DialogInterface.OnClickListener listnerPositive,String negativeButtonText ,DialogInterface.OnClickListener listnerNegative,Boolean isCancelable){
		
		AlertDialog alertDialog = new AlertDialog.Builder(
				context ,AlertDialog.THEME_HOLO_LIGHT).create();
		alertDialog.setTitle(Html.fromHtml(title));
		alertDialog.setMessage(Html.fromHtml(msg));
		alertDialog.setCancelable(isCancelable);
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,positiveButtonText,listnerPositive);
		
		if(negativeButtonText!= null && !negativeButtonText.equals("")){
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,negativeButtonText,listnerNegative);
		}
		alertDialog.show();
		
	}

	
	public static String convertInputStreamToString(InputStream is)
	{
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try 
		{
			while((line = rd.readLine()) != null)
			{
				total.append(line);
			}
		} 
		catch (IOException e) 
		{
			//Toast.makeText(this,"Stream Exception", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		return total.toString();
	}

	public static ProgressDialog showLoadingDialog(ProgressDialog mDialog, Activity act , Boolean cancelable) {
		
		if(mDialog == null){
		  mDialog = new ProgressDialog(act,
		  ProgressDialog.THEME_HOLO_LIGHT);
		  mDialog.setTitle("Loading");
		  mDialog.setMessage("Please wait for a moment...");
		  mDialog.setCancelable(cancelable); 
		  mDialog.setProgressDrawable(null);
		  mDialog.show();
		}else if(!mDialog.isShowing()) {
			mDialog.show();
		}
		 
		return mDialog;
	}

	public static void hideLoadingDialog(ProgressDialog mDialog) {

		 if (mDialog != null) {
			 mDialog.dismiss(); 
		 }
		 

	}
	
	public static String getTwoDigitNo(int no){
		if(no>=10 || no<-9)
			return no +"";
		
		return "0"+no;
		
	}
	
public static void loadImageIntoImageView( ImageView iv ,String imgURL, Context context  ){
		
		loadImageIntoImageView(iv, imgURL, context, 0, 0, 0, 0,0,0);
	}
	
	public static void loadImageIntoImageView( ImageView iv ,String imgURL, Context context ,int loadingImgId, int errorImgId ){
		
		loadImageIntoImageView(iv, imgURL, context, loadingImgId, errorImgId, 0, 0,0,0);
	}
	
public static void loadImageIntoImageView( ImageView iv ,String imgURL, Context context ,int loadingImgId, int errorImgId ,int height,int width){
		
		loadImageIntoImageView(iv, imgURL, context, loadingImgId, errorImgId, 0, 0,height,width);
	}
	
	public static void loadImageIntoImageView( ImageView iv ,String imgURL , int transformRadius , int transformMargin, Context context){
		
		loadImageIntoImageView(iv, imgURL, context, 0, 0, transformRadius, transformMargin,0,0);
	}
	
public static void loadImageIntoImageView( ImageView iv ,String imgURL , int transformRadius , int transformMargin, Context context,int height,int width){
		
		loadImageIntoImageView(iv, imgURL, context, 0, 0, transformRadius, transformMargin,height,width);
	}
	public static void loadImageIntoImageView( ImageView iv ,String imgURL, Context context ,int loadingImgId ,int errorImgId, int transformRadius , int transformMargin,int height,int width){
		
		try {
			Picasso p = Picasso.with(context);
			RequestCreator rq = null;

			if(!imgURL.trim().isEmpty()){
				rq = p.load(imgURL);
				if(loadingImgId != 0)
					rq.placeholder(loadingImgId);
				if(errorImgId != 0)
					rq.error(errorImgId);
				if(transformRadius != 0)
					rq.transform(new Custom_PiccasoRoundedTransformation(transformRadius,transformMargin));
				if(width != 0)
				rq.resize(width, height);
			}
			else
				rq = p.load(errorImgId);
			
			rq.into(iv, new Callback() {
				
				@Override
				public void onSuccess() {
					Log.i("DARSH", "Image Loaded");
				}

				@Override
				public void onError() {
					Log.e("DARSH", "Image Loaded ERRROR");
				}
			  
				
			});
			
			
		} catch (Exception e) {
			Log.e("DARSH", "Error in loading image with url "+ imgURL);
		}
		
	}
	
	
	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d("xerces", "I never expected this! Going down, going down!"
					+ e);
			throw new RuntimeException(e);
		}
	}
	
	public static String getDeviceIMEI(Context con){
		
		TelephonyManager telephonyManager = (TelephonyManager)con.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceImei = telephonyManager.getDeviceId();		
		return deviceImei;
		
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap , float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
		    bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
		}
	
	public static int getCategoryColor(int catId , Context context){
		int colorNo = catId % 10;
		
		int colorResourceId  = context.getResources().getIdentifier("app_cat_color_"+colorNo, "color", context.getPackageName());
		
		if(colorResourceId == 0){
			colorResourceId = R.color.app_cat_color_1;
		}
		
		return colorResourceId;
	}
	
	public static int getRowHeight(Context context, float textSize, int deviceWidth,boolean isBold, String text,Typeface typeFace) {
	    TextView textView = new TextView(context);
	    textView.setText("Harsh");
	    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
	    if(isBold)
	    	textView.setTypeface(typeFace, Typeface.BOLD);
	    else
		    textView.setTypeface(typeFace);
	    int widthMeasureSpec = MeasureSpec.makeMeasureSpec(deviceWidth, MeasureSpec.AT_MOST);
	    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
	    textView.measure(widthMeasureSpec, heightMeasureSpec);
	    return textView.getMeasuredHeight();
	}
	
	public static int dpToPx(int dp)
	{
	    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	public static int pxToDp(int px)
	{
	    return (int) (px / Resources.getSystem().getDisplayMetrics().density);
	}
	
	/*public void takeScreenshot(View v) { 
	    Date now = new Date(); 
	    android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
	 
	    try { 
	    	//Button btn = (Button)findViewById(R.id.button1);
	    	btn.setVisibility(View.GONE);
	        // image naming and path  to include sd card  appending name you choose for file 
	        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
	 
	        // create bitmap screen capture 
	        View v1 = getWindow().getDecorView().getRootView();
	        v1.setDrawingCacheEnabled(true);
	        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
	        v1.setDrawingCacheEnabled(false);
	 
	        File imageFile = new File(mPath);
	 
	        FileOutputStream outputStream = new FileOutputStream(imageFile);
	        int quality = 100;
	        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
	        outputStream.flush();
	        outputStream.close();
	        btn.setVisibility(View.VISIBLE);
	        openScreenshot(imageFile);
	    } catch (Throwable e) {
	        // Several error may come out with file handling or OOM 
	        e.printStackTrace();
	    } 
	} 
	
	private void openScreenshot(File imageFile) {
	    Intent intent = new Intent();
	    intent.setAction(Intent.ACTION_VIEW);
	    Uri uri = Uri.fromFile(imageFile);
	    intent.setDataAndType(uri, "image/*");
	    startActivity(intent);
	} */
	
}
