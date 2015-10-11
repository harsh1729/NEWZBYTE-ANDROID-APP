package in.newzbyte.app;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class Activity_Home extends Activity {

	private int currentNewsIndex = -1;
	private ArrayList<Integer> arraySelectedCatIds ;

	View viewMoving;
	View viewStatic;
	View viewLoading;
	View viewSettings;
	AnimationDrawable bAmin;


	float x = 0;
	float y = 0;
	long startTime;

	final int MAX_TOUCH_VALUE = 10;
	final long DEFAULT_MAX_SLIDE_DURATION = 350;
	final long DEFAULT_MIN_SLIDE_DURATION = 150;
	private Boolean isSlideInProgress = false;
	private Boolean isAnimInProgress = false;
	private Boolean isDrawerOpen = false;
	private Boolean isSlideUp = true;
	private Boolean isNoMoreNews = false;
	private Boolean isFirstResume = true;
	static Boolean comingFromPushMessage = false;
	Button btnMenu;
	ImageView imgSettingsCog;

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;


	private RelativeLayout rlytNewsContent;
	private RelativeLayout rlytMainContent;
	private RelativeLayout rlytDrawerPane;


	ArrayList<Object_ListItem_MainNews> listNewsItemServer;
	ArrayList<Object_Category> listCatItemServer = new ArrayList<Object_Category>();
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);
		initHome();

	}

	private void initHome(){

		arraySelectedCatIds = new ArrayList<Integer>();
		DBHandler_Main db = new DBHandler_Main(this);
		db.createDataBase();

		rlytDrawerPane = (RelativeLayout)findViewById(R.id.rlytDrawerPane);
		rlytNewsContent = (RelativeLayout)findViewById(R.id.rlytNewsContent);
		rlytMainContent = (RelativeLayout)findViewById(R.id.rlytMainContent);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		btnMenu = (Button)findViewById(R.id.btnMenu);
		imgSettingsCog = (ImageView)findViewById(R.id.imgSettingsCog);


		rlytDrawerPane.getLayoutParams().width = (int) (2.3 *Globals.getScreenSize(this).x / 3);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.menu1, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				Log.i("Bytes", "onDrawerClosed: " + getTitle());
				isDrawerOpen=true;
				btnMenu.setBackgroundResource(R.drawable.anim_menu_to_arrow);
				drawerEventAnim();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				Log.i("Bytes", "onDrawerClosed: " + getTitle());

				isDrawerOpen=false;
				btnMenu.setBackgroundResource(R.drawable.anim_arrow_to_menu);
				drawerEventAnim();
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset)
			{
				super.onDrawerSlide(drawerView, slideOffset);

				float moveFactor = (rlytDrawerPane.getWidth() * slideOffset);

				rlytMainContent.setTranslationX(moveFactor);
			}
			/* earlier versions.
                {
                    TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    rlytMainContent.startAnimation(anim);

                    lastTranslate = moveFactor;
                }*/

		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);	
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

		//mDrawerLayout.setEnabled(false);
		addLoadingView();

		if( (new Custom_ConnectionDetector(this)).isConnectingToInternet()){

			serverCallForCategoriesAndNews();

			new Custom_GCM_Register(this);
		}else{
			loadNewsCatFromDB();
			Globals.showAlertDialogOneButton(
					Globals.TEXT_CONNECTION_ERROR_HEADING,
					Globals.TEXT_LOADING_FROM_PREVIOUS_SESSION,
					Activity_Home.this, "OK", null, false);
		}


	}

	@Override
	protected void onResume() {
		
		super.onResume();

		if(comingFromPushMessage){
			comingFromPushMessage = false;
			if(!(GCMIntentService.pushMessageHeader.isEmpty()))
				Globals.showAlertDialogOneButton("News Flash",GCMIntentService.pushMessageHeader +"\n\n"+GCMIntentService.pushMessageText, this, "OK", null, false);
		}

		if(!isFirstResume){

			if(listNewsItemServer == null || listNewsItemServer.isEmpty() || listCatItemServer == null || listCatItemServer.isEmpty()){

				loadNewsCatFromDB();
			}


		}else{
			isFirstResume = false;

		}
	}

	private void loadNewsCatFromDB(){

		if(viewLoading != null){
			hideLoadingView();
		}
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);




		DBHandler_Category dbH2 = new DBHandler_Category(this);
		listCatItemServer = dbH2.getAllCategories();
		createDrawerCategories();

		DBHandler_MainNews dbH = new DBHandler_MainNews(this);
		listNewsItemServer = dbH.getAllMainNews();

		currentNewsIndex = -1;
		arraySelectedCatIds.clear();
		viewStatic = createNewsView();



	}
	private void  addLoadingView(){

		LayoutInflater inflater = (LayoutInflater)this.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		viewLoading = inflater.inflate(R.layout.view_loading, rlytMainContent,false);

		//ImageView imgViewLogo = (ImageView)viewLoading.findViewById(R.id.imgLogoXB);

		//int screenWidth = Globals.getScreenSize(this).x;
		//int logoWidth = screenWidth/100 * 50 ;
		//Options options = new BitmapFactory.Options();
		//options.inScaled = false;
		//Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.xb, options);
		//logo = Globals.scaleToWidth(logo,logoWidth);
		//imgViewLogo.setImageBitmap(logo);

		rlytMainContent.addView(viewLoading);

	}
	private View createNewsView(){

		if(listNewsItemServer == null ){
			Toast.makeText(this, "Please wait!", Toast.LENGTH_SHORT).show();
			return null;
		}
		else if(listNewsItemServer.size()==0)
		{
			return null;
		}
		
		Log.d("jaspal","CurrentNewsIndex:"+currentNewsIndex);

		//Object_ListItem_MainNews objNews = listNewsItemServer.get(currentNewsIndex);

		if(isSlideUp){
			if(currentNewsIndex >= listNewsItemServer.size() - 1){
				currentNewsIndex = listNewsItemServer.size() - 1;
				//Toast.makeText(this, "You are done for the day!", Toast.LENGTH_SHORT).show();
				//TODO CALL SERVER FOR MORE NEWS WITH CATEGORY
				mDialog = Globals.showLoadingDialog(mDialog,this,false);
				
				Object_ListItem_MainNews objNews = listNewsItemServer.get(currentNewsIndex);
				//getNewsDataFromServer(Integer.valueOf(objNews.getCatId()),Globals.CALL_TYPE_OLD, Integer.valueOf(objNews.getId()), Globals.FINAL_NEWS_LIMIT_LOAD_OLD);
				getNewsDataFromServer(-1,Globals.CALL_TYPE_OLD, Integer.valueOf(objNews.getId()), Globals.FINAL_NEWS_LIMIT_LOAD_OLD);
				return null;
			}

			currentNewsIndex ++;	
		}

		else{
			if(currentNewsIndex <= 0){
				currentNewsIndex  = 0;
				//Toast.makeText(this, "No more news to show at this moment.", Toast.LENGTH_SHORT).show();
				//TODO CALL SERVER FOR MORE NEWS WITH CATEGORY
				mDialog = Globals.showLoadingDialog(mDialog,this,false);
				
				Object_ListItem_MainNews objNews = listNewsItemServer.get(currentNewsIndex);
				//getNewsDataFromServer(Integer.valueOf(objNews.getCatId()),Globals.CALL_TYPE_NEW, Integer.valueOf(objNews.getId()), Globals.FINAL_NEWS_LIMIT_LOAD_NEW);
				getNewsDataFromServer(-1,Globals.CALL_TYPE_NEW, Integer.valueOf(objNews.getId()), Globals.FINAL_NEWS_LIMIT_LOAD_NEW);
				return null;
			}
			currentNewsIndex--;
		}
		Object_ListItem_MainNews objNews = listNewsItemServer.get(currentNewsIndex);
		//objNews = listNewsItemServer.get(currentNewsIndex);

		if(arraySelectedCatIds.size() > 0 && !isSelectedId(Integer.valueOf(objNews.getCatId())) ){//selectedCatId !=0  && objNews.getCatId() != selectedCatId){
			return createNewsView();
		}

		LayoutInflater inflater = (LayoutInflater)this.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		View newView = inflater.inflate(R.layout.view_news_sliding_home, rlytNewsContent,false);

		setTextContainerHeight(newView,objNews.getHeadingSpan().toString(),objNews.getContentSpan().toString());


		ImageView imgViewNews =(ImageView) newView.findViewById(R.id.imgHome);
		TextView txtViewNews =(TextView) newView.findViewById(R.id.txtHeading);
		TextView txtSummary =(TextView) newView.findViewById(R.id.txtSummary);
		
		TextView txtAuthorText=(TextView) newView.findViewById(R.id.txtAuthorText);
		TextView txtAuthor=(TextView) newView.findViewById(R.id.txtAuthor);
		TextView txtTime=(TextView) newView.findViewById(R.id.txtNewsTime);
		TextView txtSourceText=(TextView) newView.findViewById(R.id.txtSourceText);
		TextView txtSource=(TextView) newView.findViewById(R.id.txtSource);
		ImageView imgShare = (ImageView)newView.findViewById(R.id.imgShareHome);



		TextView txtTap = (TextView)newView.findViewById(R.id.txtTapToread);


		TextView txtCategory = (TextView)newView.findViewById(R.id.txtHeadingCategory);
		LinearLayout llyt = (LinearLayout)newView.findViewById(R.id.llytFooterLine);

		int catColor = this.getResources().getColor(Globals.getCategoryColor(objNews.getCatId(), this));
		imgViewNews.setBackgroundColor(catColor);
		imgShare.setBackgroundColor(catColor);
		txtCategory.setBackgroundColor(catColor);
		llyt.setBackgroundColor(catColor);
		txtTap.setTextColor(catColor);

		String textSummary = objNews.getContentSpan().toString();

		txtViewNews.setText(objNews.getHeadingSpan().toString());	
		txtSummary.setText(textSummary);
		txtCategory.setText(objNews.getCatName());
		txtAuthor.setText(objNews.getAuthor());
		txtTime.setText("/ " +getFormatedDateTime(objNews.getDate()));

		//set FONT

		Typeface tf = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_FONT);
		Typeface tfCat = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_CAT_FONT);
		//Typeface tf = Typeface.createFromAsset(getAssets(), "Raleway-Regular.ttf");
		txtSummary.setTypeface(tf);
		txtAuthorText.setTypeface(tf);
		txtAuthor.setTypeface(tf);
		txtTime.setTypeface(tf);
		txtSource.setTypeface(tf);
		txtSourceText.setTypeface(tf);
		txtViewNews.setTypeface(tf,Typeface.BOLD);
		txtTap.setTypeface(tf,Typeface.BOLD);
		txtCategory.setTypeface(tfCat);
		// get Total Lines of textView Summary
		Rect bounds = new Rect();
		Paint paint = new Paint();
		paint.setTextSize(getResources().getDimension(R.dimen.font_lbl_small_medium1));
		paint.getTextBounds(textSummary, 0, textSummary.length(), bounds);

		float width = (float) Math.ceil( bounds.width());

		float noOfLines = width /(float) (Globals.getScreenSize(this).x - Globals.dpToPx(20));

		Log.i("DARSH", "noOfLines "+noOfLines);

		if(noOfLines >12){
			txtTap.setVisibility(View.VISIBLE);
			//txtTap.setTextColor(catColor);
			objNews.hasDetailNews = true;
		}else{
			objNews.hasDetailNews = false;
		}

		if(!objNews.getSource().isEmpty()){
			txtSource.setText(Html.fromHtml(objNews.getSource()));
		}else{
			txtSourceText.setVisibility(View.GONE);
			txtSource.setVisibility(View.GONE);
		}



		Globals.loadImageIntoImageView(imgViewNews, objNews.getImagePath(), this);

		if(isSlideUp){
			rlytNewsContent.addView(newView,0);
		}

		else{
			rlytNewsContent.addView(newView);
		}


		if(isSlideInProgress && !isSlideUp){
			newView.setY(-1*rlytNewsContent.getHeight());
		}

		return newView;
	}

	private void slide(int height , long duration){

		if(viewMoving != null){

			viewMoving.animate().setDuration(duration)
			.translationY(height)
			.alpha(1.0f);
			Log.i("Bytes", "viewMoving not null");

		}else{
			Log.i("Bytes", "viewMoving is null");
		}

	}

	private void hideLoadingView(){

		if(viewLoading!= null){

			viewLoading.animate().setDuration(DEFAULT_MAX_SLIDE_DURATION)
			.translationY(viewLoading.getHeight() *-1)
			.alpha(1.0f).setListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator arg0) {
					

				}

				@Override
				public void onAnimationRepeat(Animator arg0) {
					

				}

				@Override
				public void onAnimationEnd(Animator arg0) {
					viewLoading.setVisibility(View.GONE);
					rlytMainContent.removeView(viewLoading);

				}

				@Override
				public void onAnimationCancel(Animator arg0) {
					viewLoading.setVisibility(View.GONE);
					rlytMainContent.removeView(viewLoading);

				}
			});
			Log.i("Bytes", "viewMoving not null");

		}else{
			Log.i("Bytes", "viewMoving is null");
		}

	}

	private void setTextContainerHeight(View v,String title,String content){

		LinearLayout container = (LinearLayout)v.findViewById(R.id.llytTextContainer);

		LinearLayout.LayoutParams params =(LinearLayout.LayoutParams) container.getLayoutParams();
		// Changes the height and width to the specified *pixels*
		Typeface tf = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_FONT);
		int containerWidth = Globals.getScreenSize(this).x;//container.getWidth();
		int titleHeight =Globals.getRowHeight(this,getResources().getDimension(R.dimen.font_lbl_normal) , containerWidth, true,title, tf);//
		int contentHeight = Globals.getRowHeight(this, getResources().getDimension(R.dimen.font_lbl_small_medium1), containerWidth, false,content,tf);

		//int buttonHeight  = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
		int paddingHeight  = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (10+10+10), getResources().getDisplayMetrics());

		Log.i("DARSH", "titleHeight = "+titleHeight);
		Log.i("DARSH", "contentHeight = "+contentHeight);
		Log.i("DARSH", "paddingHeight = "+paddingHeight);
		Log.i("DARSH", "screenHeight = "+Globals.getScreenSize(this).y);
		int totalheight = titleHeight *2 + contentHeight *13+paddingHeight; 
		Log.i("DARSH", "totalheight = "+totalheight);
		params.height =  totalheight;

		container.setLayoutParams(params);
	}
	private void slideComplete(float y2){
		if(isNoMoreNews){
			isNoMoreNews = false;
		}

		if(isSlideInProgress){
			int moveTo = 0;
			isSlideInProgress = false;

			if(isSlideUp){
				moveTo = -1 * rlytNewsContent.getHeight();
			}
			if(viewMoving != null){

				float currentMovement = Math.abs(y - y2) ;
				long elapseTimeMilliSec =(long) (( System.nanoTime() - startTime )/ 1000000.0);
				long newDuration = DEFAULT_MAX_SLIDE_DURATION;
				if(elapseTimeMilliSec > 0){
					long speed = (long) ((currentMovement - MAX_TOUCH_VALUE)/elapseTimeMilliSec);
					if(speed > 0)
						newDuration = (long) ((rlytNewsContent.getHeight() - currentMovement) / speed);
				}

				Log.i("Bytes", "newDuration = "+newDuration);

				if(newDuration > DEFAULT_MAX_SLIDE_DURATION)
					newDuration = DEFAULT_MAX_SLIDE_DURATION;
				else if(newDuration < DEFAULT_MIN_SLIDE_DURATION)
					newDuration = DEFAULT_MIN_SLIDE_DURATION;


				viewMoving.animate().setDuration(newDuration)
				.translationY(moveTo)
				.alpha(1.0f).setListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator animation) {
						isAnimInProgress = true;
					}

					@Override
					public void onAnimationRepeat(Animator animation) {

					}

					@Override
					public void onAnimationEnd(Animator animation) {
						animationOver();

					}

					@Override
					public void onAnimationCancel(Animator animation) {
						animationOver();
					}
				});


			}
		}


	}

	private void animationOver(){

		isAnimInProgress = false;

		if(!isSlideUp){
			rlytNewsContent.removeView(viewStatic);						
			viewStatic = viewMoving;

		}else{
			rlytNewsContent.removeView(viewMoving);
		}

		if(viewMoving != null)
			viewMoving.animate().setListener(null);
		viewMoving = null;

		rlytNewsContent.bringChildToFront(viewStatic);
		if(rlytNewsContent.getChildCount() > 1)
			rlytNewsContent.removeViews(0, rlytNewsContent.getChildCount() -1);
	}
	@Override
	public boolean onTouchEvent(MotionEvent touchevent) {	
		//return super.onTouchEvent(event);
		//Log.i("Bytes", "onTouchEvent");

		switch (touchevent.getAction())
		{
		// when user first touches the screen we get x and y coordinate
		case MotionEvent.ACTION_DOWN: 
		{
			x = touchevent.getX();
			y = touchevent.getY();


			Log.i("Bytes", "ACTION_DOWN");

			break;
		}

		case MotionEvent.ACTION_MOVE:
		{

			if(isAnimInProgress || isNoMoreNews)
				return false;

			float x2 = touchevent.getX();
			float y2 = touchevent.getY(); 

			float displacementX = Math.abs(x - x2);
			float displacementY = Math.abs(y - y2);

			if(displacementY > displacementX  ){
				if( Math.abs(y-y2) > MAX_TOUCH_VALUE && !isSlideInProgress){

					isSlideInProgress = true;
					if(y > y2){
						Log.i("Bytes", "ACTION_MOVE UP");
						isSlideUp = true;
						viewMoving = viewStatic;

						int backUpId = currentNewsIndex;
						viewStatic = createNewsView();
						if(viewStatic == null){
							viewStatic = viewMoving;
							viewMoving = null;
							isNoMoreNews = true;
							isSlideInProgress = false;
							currentNewsIndex = backUpId;
							return false;
						}
					}else{
						Log.i("Bytes", "ACTION_MOVE DOWN");
						isSlideUp = false;
						int backUpId = currentNewsIndex;
						viewMoving = createNewsView();

						if(viewMoving == null){
							isNoMoreNews = true;
							isSlideInProgress = false;
							currentNewsIndex = backUpId;
							return false;
						}
					}
					startTime = System.nanoTime();

				}

				if(isSlideInProgress){
					if(isSlideUp)
						slide(-1 *(int)Math.abs(y2-y),0); 
					else
						slide((-1*rlytNewsContent.getHeight()) + (int)Math.abs(y2-y),0);
				}

			}

			break;
		}

		case MotionEvent.ACTION_UP : 
		{ 
			float x2 = touchevent.getX();
			float y2 = touchevent.getY(); 

			float displacementX = Math.abs(x - x2);
			float displacementY = Math.abs(y - y2);

			if(displacementY > displacementX){
				if( Math.abs(y-y2) < MAX_TOUCH_VALUE ){

					tapOnView();
				}
			}
			else
			{
				if( Math.abs(x-x2) < MAX_TOUCH_VALUE){

					tapOnView();
				}
			}

			slideComplete(touchevent.getY());
			Log.i("Bytes", "ACTION_UP");
			break;
		}

		case MotionEvent.ACTION_CANCEL :{

			slideComplete(touchevent.getY());
			Log.i("Bytes", "ACTION_CANCEL");
			break;
		}

		}

		return false;		
	}


	private void tapOnView(){

		if(viewLoading!= null )
			if(viewLoading.getVisibility() == View.VISIBLE)
				return;

		if (currentNewsIndex >= 0 &&  currentNewsIndex <= listNewsItemServer.size()) {
			Object_ListItem_MainNews obj =	listNewsItemServer.get(currentNewsIndex);
			if(obj.hasDetailNews)	
				navigateToNewsDetail(obj);
		}
	}
	public void onClickMenu(View v){

		if(isDrawerOpen){                
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}else{              
			mDrawerLayout.openDrawer(Gravity.LEFT);
		}

	}

	public void onClickAllCategory(View v){

		arraySelectedCatIds.clear();
		onClickCategory();
	}

	public void onClickCategoryItem(View v){

		Integer selectedCatId = ((Integer)v.getTag(R.string.app_name)).intValue();

		if(!isSelectedId(selectedCatId))
			arraySelectedCatIds.add(selectedCatId);
		else
			removeId(selectedCatId);

		onClickCategory();

	}

	private void removeId(Integer catId){

		int cnt = -1;

		for(int i =0; i< arraySelectedCatIds.size();i++){
			Integer intgr = arraySelectedCatIds.get(i);
			if(intgr.compareTo(catId) == 0){ //equal
				cnt = i;
				break;
			}
		}
		if(cnt > -1){
			arraySelectedCatIds.remove(cnt);
		}
	}
	private boolean isSelectedId(Integer catId){

		for(Integer i : arraySelectedCatIds){
			if(i.compareTo(catId) == 0){ //equal
				return true;
			}
		}

		return false;
	}
	private void onClickCategory() {

		createDrawerCategories();
		//mDrawerLayout.closeDrawer(Gravity.LEFT);
		currentNewsIndex = -1;
		isSlideUp = true;
		View view = createNewsView();
		if(view != null){
			viewStatic = view;
		}
		viewMoving = null;

		if(rlytNewsContent.getChildCount() > 1)
			rlytNewsContent.removeViews(1, rlytNewsContent.getChildCount() -1);
	}

	public File takeScreenshot(String txt) { 
		Date now = new Date(); 
		android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

		Bitmap bitmap = null;
		String mPath = "";
		File imageFile = null;

		try { 
			// image naming and path  to include sd card  appending name you choose for file 
			mPath = Environment.getExternalStorageDirectory().toString() + "/" + now.toString().replace(" ","_").replace(":","_") + ".jpg";

			// create bitmap screen capture 
			View v1 = getWindow().getDecorView().getRootView();
			v1.setDrawingCacheEnabled(true);
			bitmap = Bitmap.createBitmap(v1.getDrawingCache());
			v1.setDrawingCacheEnabled(false);

			imageFile = new File(mPath);

			FileOutputStream outputStream = new FileOutputStream(imageFile);

			//WRITING ON IMAGE CODE START
			Canvas canvas = new Canvas(bitmap);

			Paint paint = new Paint();
			paint.setColor(Color.BLACK); // Text Color
			paint.setTypeface(Typeface.SERIF);
			paint.setAntiAlias(true);
			paint.setStrokeWidth(30); // Text Size
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
			// some more settings... 

			//Log.i("jaspal","image height from text :"+bitmap.getHeight());
			canvas.drawBitmap(bitmap, 0,0, paint);
			canvas.drawText(txt, Globals.dpToPx(10) , bitmap.getHeight()- Globals.dpToPx(5), paint);
			// WRITING ON IMAGE END

			int quality = 100;
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
			outputStream.flush();
			outputStream.close();
			//openScreenshot(imageFile);
		} catch (Throwable e) {
			// Several error may come out with file handling or OOM 
			e.printStackTrace();
		} 
		//return mPath;
		return imageFile;
	}

	public void onClickShare(View v) {

		if (currentNewsIndex >= 0 &&  currentNewsIndex <= listNewsItemServer.size()) {



			Object_ListItem_MainNews currentNewsItem = listNewsItemServer.get(currentNewsIndex);
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			//sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);

			if (android.os.Build.VERSION.SDK_INT >= 13) 
			{
				sendIntent.putExtra(Intent.EXTRA_SUBJECT,
						currentNewsItem.getHeadingSpan()  + "\n\n");
			}else{
				sendIntent.putExtra(Intent.EXTRA_SUBJECT,
						currentNewsItem.getHeadingSpan()  + "\n\n");
			}



			if (currentNewsItem.getShareLink() != null && !currentNewsItem.getShareLink().trim().equals("")) 
			{
				sendIntent.putExtra(
						Intent.EXTRA_TEXT,
						// currentNewsItem.getContent()
						"Read more @\n"
						+ getResources().getString(
								R.string.txt_company_website)
								+ "\nvia "
								+ getResources().getString(
										R.string.news_paper_name) +" for Android");
			} 
			else 
			{
				sendIntent.putExtra(
						Intent.EXTRA_TEXT,
						"Read more @\n"
								+ getResources().getString(
										R.string.txt_company_website)//+"/detail/"+currentNewsItem.getId()
										+ "\nvia "
										+ getResources().getString(
												R.string.news_paper_name)+" for Android");
			}

			File imgF = takeScreenshot("Read more @ "
					+ getResources().getString(
							R.string.txt_company_website)//+"/detail/"+currentNewsItem.getId()
							+ " via "
							+ getResources().getString(
									R.string.news_paper_name)+" for Android");

			sendIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(imgF) );
			Log.i("jaspal","image Path is :"+Uri.fromFile(imgF));

			//sendIntent.setType("text/plain");
			sendIntent.setType("image/*"); 
			sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); 
			//startActivity(sendIntent);
			startActivity(Intent.createChooser(sendIntent, "Share Via"));
		}
	}

	private void drawerEventAnim(){

		try{
			bAmin = (AnimationDrawable) btnMenu.getBackground();
			bAmin.setOneShot(true);
			bAmin.start();
		}catch(Exception ex){

		}
	}

	private void createDrawerCategories(){

		LinearLayout llytCatContainer = (LinearLayout)findViewById(R.id.llytCatContainer);
		Button btnCatAll = (Button)findViewById(R.id.btnCatAll);

		if(llytCatContainer.getChildCount() > 1){
			llytCatContainer.removeViews(1, llytCatContainer.getChildCount()-1);
		}

		if(arraySelectedCatIds.size() == 0){
			btnCatAll.setBackgroundResource(R.drawable.viewall_selected);
		}else{
			btnCatAll.setBackgroundResource(R.drawable.selector_cat_all_button);
		}

		LinearLayout row = null;
		for(int i = 0 ; i< listCatItemServer.size() ; i++){

			/*if(i%2 == 0){
				LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = (LinearLayout) inflater.inflate(R.layout.view_category_row_home, llytCatContainer ,false);
			}

			if(row != null){

				row.addView(getCatImageView(listCatItemServer.get(i),row)) ;

				if(i%2 == 0)
					llytCatContainer.addView(row);	
			}*/
			//if(i%2 == 0){
				LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = (LinearLayout) inflater.inflate(R.layout.view_category_row_home, llytCatContainer ,false);
		//	}

			//if(row != null){

				row.addView(getCatImageView(listCatItemServer.get(i),row)) ;

				//if(i%2 == 0)
					llytCatContainer.addView(row);	
			//}



		}
	}


	@SuppressLint("NewApi")
	private RelativeLayout getCatImageView(Object_Category objCat , LinearLayout row){

		int widthImage = rlytDrawerPane.getLayoutParams().width;// / 2 - 44 ;
		int catColor = this.getResources().getColor(Globals.getCategoryColor(objCat.getId(), this));

		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		RelativeLayout item = (RelativeLayout) inflater.inflate(R.layout.item_category_image_home, row ,false);
		item.setTag(R.string.app_name, Integer.valueOf(objCat.getId()));

		if(isSelectedId(Integer.valueOf(objCat.getId())))
		{

			GradientDrawable shape =  new GradientDrawable();
			//shape.setCornerRadius(10);
			shape.setColor(catColor);
			if(VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN)
			{                   
				item.setBackground(shape);
			} 
			else{ 
				item.setBackgroundDrawable(shape);  // deprecated.
			} 

		}
		else
		{
			item.setOnTouchListener(new Custom_OnTouchListener_ColoredBG(item, this, objCat.getId()));
			item.setBackgroundResource(R.drawable.bg_rounded_shadow);
		}
		//item.getLayoutParams().width = widthImage;
		//item.getLayoutParams().height = item.getLayoutParams().width;

		ImageView imgView =(ImageView) item.findViewById(R.id.imgViewCat);
		int heightImage = Globals.getScreenSize(this).y/5;
		
		item.setMinimumWidth(widthImage);
		item.setMinimumHeight(heightImage);


		/*new ImageView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 10, 10, 10);

		imgView.setLayoutParams(params);
		imgView.setScaleType(ScaleType.CENTER_CROP);
		imgView.setClickable(true);

		 */

		Globals.loadImageIntoImageView(imgView, objCat.getImageName(), 0,0, this,heightImage,widthImage);

		TextView txtCategory = (TextView)item.findViewById(R.id.txtCategory);

		Typeface tfCat = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_CAT_FONT);
		txtCategory.setTypeface(tfCat);
		txtCategory.setText(objCat.getName());
		txtCategory.setBackgroundResource(Globals.getCategoryColor(objCat.getId(), this));

		return item;

	}

	
	//TODO get main news data from server
	public void getNewsDataFromServer(final int catId, final String callType,int lastNewsId , int limit) 
	{
		Log.d("jaspal","catid:"+catId);
		Log.d("jaspal","callType:"+callType);
		Log.d("jaspal","lastNewsid:"+lastNewsId);
		Log.d("jaspal","limit:"+limit);

		try{
			
			Custom_ConnectionDetector cd = new Custom_ConnectionDetector(getApplicationContext());

			if (!cd.isConnectingToInternet()) {
				/*if (!isPullToRefresh) {
					Globals.showAlertDialogOneButton(
							Globals.TEXT_NO_INTERNET_HEADING,
							Globals.TEXT_LOADING_FROM_PREVIOUS_SESSION,
							Activity_Home.this, "OK", null, false);
					//showNewsList(catId,callType);

				} else {*/
					Toast.makeText(
							Activity_Home.this,
							Globals.TEXT_NO_INTERNET_DETAIL_TOAST,
							Toast.LENGTH_SHORT).show();
					//listViewNews.onRefreshComplete();

				/*}*/

				Globals.hideLoadingDialog(mDialog);
				return;
			}

			Log.i("HARSH", "getNewsDataFromServer Request CatId = " + catId);
			//if (!isPullToRefresh)
				//if(!isShowingLoadingScreen())
					//mDialog = Globals.showLoadingDialog(mDialog, this,false);
			
			Log.d("jaspal","URL for more news is :\n"+Custom_URLs_Params.getURL_NewsByCategory());
			Log.d("jaspal","\n\nParameters for more news is:\n"+Custom_URLs_Params.getParams_NewsByCategory(catId, callType, lastNewsId, limit));
			Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(Request.Method.POST,
					Custom_URLs_Params.getURL_NewsByCategory(), Custom_URLs_Params.getParams_NewsByCategory(catId, callType, lastNewsId, limit),
							new Listener<JSONObject>() {


						
						@Override
						public void onResponse(JSONObject response) {
							Log.d("jaspal","REsponse: \n"+response);

							gotNewsResponse(response, catId,callType);
							
						}

					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError err) {
							err.printStackTrace();
							//listViewNews.onRefreshComplete();

							/*if (!isPullToRefresh) {
								Globals.showAlertDialogOneButton(
										Globals.TEXT_CONNECTION_ERROR_HEADING,
										Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
										Activity_Home.this, "OK", null, false);
								Globals.hideLoadingDialog(mDialog);
								//showNewsList(catId,callType);
								//hideLoadingScreen();
							} else {*/
								Toast.makeText(
										Activity_Home.this,
										Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
										Toast.LENGTH_SHORT).show();
								//listViewNews.onRefreshComplete();

							/*}*/
							Globals.hideLoadingDialog(mDialog);
						}
					});

		
			Custom_AppController.getInstance().addToRequestQueue(
					jsonObjectRQST);
			
		}catch(Exception ex){

		}
	}
	
	//TODO got main news json response
	private void gotNewsResponse(JSONObject response, int catId, final String callType) {
 
		try {
			
			if(response != null){
				if(response.has("topnewsid"))
				{
					updateCatTopNewsId(catId,response.getInt("topnewsid"));
				}
				
				if(response.has("news"))
				{
					Log.d("jaspal","Found News !!!");
					parseNewsJson(response,callType);
				}
				/*if(response.has("news"))
					if(insertNewAndDeleteOldNews(response.getJSONArray("news"),catId,isPullToRefresh))
							showNewsList(catId,callType);*/
						
			}
			Globals.hideLoadingDialog(mDialog);
			
		} catch (JSONException e) {
			Globals.showAlertDialogOneButton(
					Globals.TEXT_CONNECTION_ERROR_HEADING,
					Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
					Activity_Home.this, "OK", null, false);
			
			Globals.hideLoadingDialog(mDialog);
			//hideLoadingScreen();
			//showNewsList(catId,callType);
		}
	}

	//TODO 
	private void updateCatTopNewsId(int catId, int newsId){
		DBHandler_Category dbH = new DBHandler_Category(this);
		dbH.updateCategoryTopNews(catId, newsId);
	}



	private void serverCallForCategoriesAndNews() {
		try {
			Log.i("HARSH", "FirstCall");			

			String url = Custom_URLs_Params.getURL_NewsFirstCall();
			Log.i("HARSH", "Cat URL -- "+url);

			//CustomRequest jsObjRequest = new CustomRequest(Method.POST, url, params, this.createRequestSuccessListener(), this.createRequestErrorListener());
			Object_AppConfig objAppConfig = new Object_AppConfig(Activity_Home.this);

			Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(Request.Method.POST,
					//objAppConfig.getVersionNoCategory()
					url, Custom_URLs_Params.getParams_CatNewsFirstCall(0,objAppConfig.getVersionNoAppConfig()),
					new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {

					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
					parseAppConfigJson(response);
					if(viewLoading != null){
						hideLoadingView();
					}

				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError err) {
					Log.i("DARSH", "ERROR VolleyError");

					/*
					DialogInterface.OnClickListener listner = new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Activity_Home.this.finish();

						}
					};
					 */

					loadNewsCatFromDB();
					Globals.showAlertDialogOneButton(
							Globals.TEXT_CONNECTION_ERROR_HEADING,
							Globals.TEXT_LOADING_FROM_PREVIOUS_SESSION,
							Activity_Home.this, "OK", null, false);


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
	
	
	//TODO
	private void parseNewsJson(JSONObject response,String callType) {

		if (response == null){
			
			return;
		}
		Log.i("DARSH", "RESPONsE parseAppConfigJson is : "+response.toString());
		try {

			Object_AppConfig objConfig = new Object_AppConfig(this);

			int newsCount = 0;
			//// If news is there insert new news News
			if (response.has("news")) {

				Log.i("DARSH", "insertNewAndDeleteOldNews news onResponse" + response);

				Custom_JsonParserNews parserObject = new Custom_JsonParserNews();
				//TODO 
				ArrayList<Object_ListItem_MainNews> tempMainNewsList = parserObject.getParsedJsonMainNews(response.getJSONArray("news"),objConfig.getRootCatId());
				
				 
				if(callType.equals(Globals.CALL_TYPE_NEW))
				{
					currentNewsIndex += tempMainNewsList.size();
					for(int i=tempMainNewsList.size()-1;i>=0;i--)
					{
						if(isSelectedId(tempMainNewsList.get(i).getCatId()));
							newsCount++;
						listNewsItemServer.add(0, tempMainNewsList.get(i));			
					}
				}
				else if(callType.equals(Globals.CALL_TYPE_OLD))
				{
					for(int i=0;i<tempMainNewsList.size();i++)
					{
						if(isSelectedId(tempMainNewsList.get(i).getCatId()));
							newsCount++;
						listNewsItemServer.add(tempMainNewsList.get(i));
					}
				}
				
				/*
				 * 
				 * Log.i("Bytes", "ACTION_MOVE UP");
						isSlideUp = true;
						viewMoving = viewStatic;

						int backUpId = currentNewsIndex;
						viewStatic = createNewsView();
						if(viewStatic == null){
							viewStatic = viewMoving;
							viewMoving = null;
							isNoMoreNews = true;
							isSlideInProgress = false;
							currentNewsIndex = backUpId;
							return false;
				 * */
				
				if(newsCount == 0 && callType.equals(Globals.CALL_TYPE_OLD))
				{
					Toast.makeText(this, "You are done for the day!", Toast.LENGTH_SHORT).show();
				}
				else if(newsCount > 0 && callType.equals(Globals.CALL_TYPE_OLD))
				{ 
					isSlideUp = true;
					viewMoving = viewStatic;
					viewStatic = createNewsView();
					slide(-1*rlytNewsContent.getHeight(),DEFAULT_MAX_SLIDE_DURATION);
				}
				else if(newsCount == 0 && callType.equals(Globals.CALL_TYPE_NEW))
				{
					Toast.makeText(this, "No more news to show at this moment.", Toast.LENGTH_SHORT).show();
				}
				else if(newsCount > 0 && callType.equals(Globals.CALL_TYPE_NEW))
				{
					isSlideUp = false;
					//isSlideInProgress = true;
					viewMoving = createNewsView();
					viewMoving.setY(-1*rlytNewsContent.getHeight());
					slide(0,DEFAULT_MAX_SLIDE_DURATION);
				}
				
				Log.d("jaspal","currentNEWSiNDEX AFTER adding new News :"+currentNewsIndex);
				
				//viewStatic = createNewsView();

				DBHandler_MainNews dbH = new DBHandler_MainNews(getApplicationContext());
				dbH.insertNewsItemList(tempMainNewsList,false);
			}


		} catch (Exception ex) {
			Log.i("HARSH", "Error in parsin jSOn" + ex.getMessage());
		}

	}

	private void parseAppConfigJson(JSONObject response) {

		if (response == null){
			
			return;
		}
		Log.i("DARSH", "RESPONCE parseAppConfigJson is : "+response.toString());
		try {

			Object_AppConfig objConfig = new Object_AppConfig(this);


			//// If news is there insert new news News
			if (response.has("news")) {

				Log.i("DARSH", "insertNewAndDeleteOldNews news onResponse" + response);

				Custom_JsonParserNews parserObject = new Custom_JsonParserNews();
				listNewsItemServer = parserObject.getParsedJsonMainNews(response.getJSONArray("news"),objConfig
						.getRootCatId());
				viewStatic = createNewsView();

				DBHandler_MainNews dbH = new DBHandler_MainNews(getApplicationContext());
				dbH.insertNewsItemList(listNewsItemServer,true);
			}
			// Now set Categories
			if (response.has("categories_need_update")) {

				if (response.getInt("categories_need_update") > 0) {

					if (response.has("category_version")) {
						objConfig.setVersionNoCategory(response.getInt("category_version"));

					}
					if(response.has("categories")){

						JSONArray Cat_Object_Array = response.getJSONArray("categories");
						Custom_JsonParserCategory parserObject = new Custom_JsonParserCategory(this);
						listCatItemServer = parserObject.getCategoriesFromJson(Cat_Object_Array);
						createDrawerCategories();

						DBHandler_Category dbH = new DBHandler_Category(this);
						dbH.setCategories(listCatItemServer);
					}

				} 
			} 


		} catch (Exception ex) {
			Log.i("HARSH", "Error in parsin jSOn" + ex.getMessage());
		}

	}

	private void navigateToNewsDetail(Object_ListItem_MainNews objNews) {

		Intent i = new Intent(getApplicationContext(),
				Activity_NewsDetails.class);

		//i.putExtra("newsId", listNewsItemServer.get(currentNewsIndex).getId());
		Activity_NewsDetails.objNews = objNews;
		startActivity(i);

	}

	private String getFormatedDateTime(String dateString){

		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy",Locale.ENGLISH);

		SimpleDateFormat currentdateFormat = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy",Locale.ENGLISH);
		//dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		//Assuming server time is in IST
		Date formattedDate = null;

		try {
			formattedDate = dateFormat.parse(dateString);
		} catch (Exception e) {
			Log.i("HARSH", "Exception in date formatting");
		}


		if(formattedDate != null){
			Log.i("HARSH", " dateString "+dateString);
			Log.i("HARSH", "formattedDate  "+formattedDate);

			TimeZone tz = TimeZone.getTimeZone(Globals.SERVER_TIME_ZONE);
			Calendar c = Calendar.getInstance(tz);

			String time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
					String.format("%02d" , c.get(Calendar.MINUTE))+":"+
					String.format("%02d" , c.get(Calendar.SECOND)) +" "+ 
					Globals.getTwoDigitNo(c.get(Calendar.DAY_OF_MONTH))+"-"+
					Globals.getTwoDigitNo(c.get(Calendar.MONTH)+1)+"-"+
					Globals.getTwoDigitNo(c.get(Calendar.YEAR));


			Log.i("HARSH", "new time  "+time);

			Date currentDate = new Date();
			try {
				currentDate = currentdateFormat.parse(time);
			} catch (ParseException e) {
			}//new Date();

			Log.i("HARSH", "currentGMT "+currentDate);
			long diffInMilliSec = currentDate.getTime() - formattedDate.getTime();

			Log.i("HARSH", "diffInMilliSec "+diffInMilliSec);

			long hour = diffInMilliSec / (60*60*1000);

			long day = hour/24;

			if(day > 1){

				if(day == 1)
					return day+" day ago";

				return day+" days ago";
			}

			if( hour> 0 && hour<= 24){

				if(hour == 1)
					return hour+" hour ago";

				return hour+" hours ago";
			}else{
				long min = diffInMilliSec / (60*1000);
				if( min> 0 && min< 60){
					if(min == 1)
						return min+" min ago";

					return min+" mins ago";
				}
				else{
					long sec = diffInMilliSec / (1000);
					if( sec> 0 && sec< 60){
						if(sec == 1)
							return sec+" second ago";

						return sec+" seconds ago";
					}
				}
			}

			SimpleDateFormat sdfDDMMYYYY = new SimpleDateFormat("d MMM , yyyy",Locale.ENGLISH);
			//sdfDDMMYYYY.setTimeZone(TimeZone.getTimeZone("IST"));
			return sdfDDMMYYYY.format(formattedDate);
		}

		return dateString;
	}


	public void onClickSettings(View v){

		//
		if(viewSettings == null){
			LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			
			//viewSettings = inflater.inflate(R.layout.view_cat_sliding_settings, rlytDrawerPane,false);
			viewSettings = inflater.inflate(R.layout.view_cat_sliding_settings, rlytMainContent,false);

			Object_AppConfig obj = new Object_AppConfig(this);

			TextView txt =(TextView) viewSettings.findViewById(R.id.txtNotification);
			ImageView imgImageView = (ImageView)viewSettings.findViewById(R.id.imgNotification);
			if(txt !=null){
				if(obj.isNotificationEnabled()){
					txt.setText(Globals.TEXT_NOTIFICATION_ENABLED);
					imgImageView.setImageResource(R.drawable.notificationturn_on1);
				}else{

					txt.setText(Globals.TEXT_NOTIFICATION_DISABLED);
					imgImageView.setImageResource(R.drawable.notificationturn_off2);
				}
			}

			//rlytDrawerPane.addView(viewSettings);
			rlytMainContent.addView(viewSettings);
		}
		//viewSettings.setY(-1*rlytDrawerPane.getHeight());
		viewSettings.setY(-1*rlytMainContent.getHeight());
		
		//disable functionality behind this setting activity
		
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);		
		/*btnMenu.setVisibility(View.GONE);
		imgSettingsCog.setVisibility(View.GONE);
		rlytNewsContent.setVisibility(View.GONE);*/
		
		///**********/
		

		viewSettings.animate().setDuration(DEFAULT_MAX_SLIDE_DURATION).translationY(0);
	}

	public void onClickSettings2(View v){
		if(viewSettings != null){
			
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);		
			/*btnMenu.setVisibility(View.VISIBLE);
			imgSettingsCog.setVisibility(View.VISIBLE);
			rlytNewsContent.setVisibility(View.VISIBLE);*/

			viewSettings.animate().setDuration(DEFAULT_MAX_SLIDE_DURATION)
			.translationY(-1*rlytDrawerPane.getHeight())
			.alpha(1.0f);
		}
	}

	public void onClickNotification(View v){
		Object_AppConfig obj = new Object_AppConfig(this);

		TextView txt =(TextView) rlytMainContent.findViewById(R.id.txtNotification);
		ImageView imgImageView = (ImageView)viewSettings.findViewById(R.id.imgNotification);

		if(obj.isNotificationEnabled()){
			obj.setNotificationEnabled(false);
			if(txt !=null){
				txt.setText(Globals.TEXT_NOTIFICATION_DISABLED);
				imgImageView.setImageResource(R.drawable.notificationturn_off2);
			}
		}else{
			obj.setNotificationEnabled(true);
			if(txt !=null){
				txt.setText(Globals.TEXT_NOTIFICATION_ENABLED);
				imgImageView.setImageResource(R.drawable.notificationturn_on1);
			}
		}


	}

	public void onClickPrivacyPolicy(View v){


	}

	public void onClickShareApp(View v){

	}

	public void onClickRateApp(View v){

	}
}



/*

case MotionEvent.ACTION_MOVE:
		{

			if(isAnimInProgress || isNoMoreNews)
				return false;

			float x2 = touchevent.getX();
			float y2 = touchevent.getY(); 

			float displacementX = Math.abs(x - x2);
			float displacementY = Math.abs(y - y2);

			if(displacementY > displacementX){
				if(y > y2 && Math.abs(y-y2) > MAX_TOUCH_VALUE){

					if(!isSlideInProgress){
						isSlideInProgress = true;
						isSlideUp = true;
						viewMoving = viewStatic;

						int backUpId = currentNewsIndex;
						viewStatic = createNewsView();
						if(viewStatic == null){
							viewStatic = viewMoving;
							viewMoving = null;
							isNoMoreNews = true;
							isSlideInProgress = false;
							currentNewsIndex = backUpId;
							return false;
						}
						startTime = System.nanoTime();
						Log.i("Bytes", "ACTION_MOVE UP");

					}
					slide(-1 *(int)Math.abs(y2-y),0); 
				}
				else if( y < y2 && Math.abs(y-y2) > MAX_TOUCH_VALUE){
					if(!isSlideInProgress){
						isSlideInProgress = true;
						isSlideUp = false;
						int backUpId = currentNewsIndex;
						viewMoving = createNewsView();

						if(viewMoving == null){
							isNoMoreNews = true;
							isSlideInProgress = false;
							currentNewsIndex = backUpId;
							return false;
						}
						startTime = System.nanoTime();
						Log.i("Bytes", "ACTION_MOVE DOWN");

					}
					slide((-1*rlytNewsContent.getHeight()) + (int)Math.abs(y2-y),0);
				}

			}

			break;
		}


txtSummary.post(new Runnable() {

			@Override
			public void run() {
				Layout layout = txtSummary.getLayout();
				if(layout != null) {
				    int lines = layout.getLineCount();
				    if(lines > 0) {
				        int ellipsisCount = layout.getEllipsisCount(lines-1);
				        if ( ellipsisCount > 0) {
				            Log.d("DARSH", "Text is ellipsized by "+ellipsisCount);
				            txtTap.setVisibility(View.VISIBLE);
				            txtTap.setTextColor(catColor);
				        } 
				    } 
				}

			}
		});
 */