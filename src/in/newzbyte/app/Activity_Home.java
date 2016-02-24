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
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class Activity_Home extends Activity implements 
GestureDetector.OnGestureListener,
GestureDetector.OnDoubleTapListener {

	private int currentNewsIndex = -1;
	//private ArrayList<Integer> arraySelectedCatIds ;

	View viewMoving;
	View viewStatic;
	RelativeLayout viewLoading;
	ImageView imgLoadingAnim;
	View viewSettings;
	
	AnimationDrawable bAmin;

	public int shareOptionNo = -1;
	float x = 0;
	float y = 0;
	long startTime;

	//final int MAX_TOUCH_VALUE = 0;
	final long DEFAULT_MAX_SLIDE_DURATION = 350;
	final long DEFAULT_MIN_SLIDE_DURATION = 100;
	final int  NO_OF_ROWS_NEWSCONTENT = 10;
	private Boolean isSlideInProgress = false;
	private Boolean isAnimInProgress = false;
	private Boolean isDrawerOpen = false;
	private Boolean isSlideUp = true;
	private Boolean isMovingViewCurrent = true;
	private Boolean isNoMoreNews = false;
	private Boolean isFirstResume = true;
	private Boolean isTopIconBarHidden = false;
	static Boolean comingFromPushMessage = false;
	private boolean doubleBackToExitPressedOnce = false;
	private boolean isCategoryChanged = false;
	//private boolean loadingAnimationStarted = false;
	ImageView imgMenu;
	ImageView imgGoToTop;
	
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;


	private RelativeLayout rlytNewsContent;
	private RelativeLayout rlytMainContent;
	private RelativeLayout rlytDrawerPane;


	ArrayList<Object_ListItem_MainNews> listNewsItemServer = new ArrayList<Object_ListItem_MainNews>();
	ArrayList<Object_Category> listCatItemServer = new ArrayList<Object_Category>();
	//private ProgressDialog mDialog;
	//private ArcMenu arcMenu;
	
	private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector; 

    //LinearLayout llytYellow;
    //LinearLayout llytGreen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initHome();

	}

	
	private void initHome(){

		///arraySelectedCatIds = new ArrayList<Integer>();
		DBHandler_Main db = new DBHandler_Main(this);
		db.createDataBase();

		// Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);
        mDetector.setIsLongpressEnabled(false);

        /*
		arcMenu = (ArcMenu)findViewById(R.id.arcMenu1);
		
		for(int i=0;i<Globals.SHARE_INTENT_ITEMS.length;i++)
        {
        	ImageView item = new ImageView(this);
        	item.setImageResource(Globals.SHARE_INTENT_ITEMS[i]);
        	final int position = i;
        	arcMenu.addItem(item, new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					arcMenu.switchShareArcmenu(true);
					shareOptionNo = position;
					//shareIntent(position);
				}
			});
        }
		*/
		rlytDrawerPane = (RelativeLayout)findViewById(R.id.rlytDrawerPane);
		rlytNewsContent = (RelativeLayout)findViewById(R.id.rlytNewsContent);
		rlytMainContent = (RelativeLayout)findViewById(R.id.rlytMainContent);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		imgMenu = (ImageView)findViewById(R.id.imgMenu);
		imgGoToTop = (ImageView)findViewById(R.id.imgGoToTop);
		
		//int drawerWidth = (int) (2.3 *Globals.getScreenSize(this).x / 3);
		//rlytDrawerPane.getLayoutParams().width = drawerWidth;

		//ImageView btnCatAll = (ImageView)findViewById(R.id.btnCatAll);
		//btnCatAll.getLayoutParams().width = drawerWidth;
		
		
		//Drawable d = getResources().getDrawable(R.drawable.viewall);
		//int hImage = d.getIntrinsicHeight(); 
		//int wImage = d.getIntrinsicWidth();  
		
		//int newImageHeight = hImage * (drawerWidth - Globals.dpToPx(10+10)) / wImage;
		//btnCatAll.getLayoutParams().height = newImageHeight + Globals.dpToPx(10+10);
		
		//TextView txt = (TextView)findViewById(R.id.txtCatHeading);
		//Typeface tfCat = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_CAT_FONT);
		//txt.setTypeface(tfCat);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.home_header_menu, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				Log.i("Bytes", "onDrawerClosed: " + getTitle());
				isDrawerOpen=true;
				//btnMenu.setBackgroundResource(R.drawable.anim_menu_to_arrow);
				//drawerEventAnim();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				Log.i("Bytes", "onDrawerClosed: " + getTitle());

				isDrawerOpen=false;
				//btnMenu.setBackgroundResource(R.drawable.anim_arrow_to_menu);
				//drawerEventAnim();
				
				if(isCategoryChanged){
					refresh();
					isCategoryChanged = false;
				}
					
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


		if( (new Custom_ConnectionDetector(this)).isConnectingToInternet()){
			new Custom_GCM_Register(this);
		}
		refresh();
	}

	private void refresh(){
		
		//mDrawerLayout.setEnabled(false);
		isMovingViewCurrent = false;
				

				if( (new Custom_ConnectionDetector(this)).isConnectingToInternet()){
					addLoadingView();
					serverCallForCategoriesAndNews();
					//new Custom_GCM_Register(this);
				}else{
					
					//final Handler handler = new Handler();
					//handler.postDelayed(new Runnable() {
					  //@Override
					 // public void run() {
						  
						  loadNewsCatFromDB();
					  //}
					//}, 1000);
					
					Globals.showAlertDialogOneButton(
							Globals.TEXT_CONNECTION_ERROR_HEADING,
							Globals.TEXT_LOADING_FROM_PREVIOUS_SESSION,
							Activity_Home.this, "OK", null, false);
				}
		
	}
	@Override
	protected void onResume() {
		
		super.onResume();

// Moved to service call
//		if(comingFromPushMessage){
//			comingFromPushMessage = false;
//			//if(!(GCMIntentService.pushMessageHeader.isEmpty()))
//				//Globals.showAlertDialogOneButton("News Flash",GCMIntentService.pushMessageHeader +"\n\n"+GCMIntentService.pushMessageText, this, "OK", null, false);
//		
//			isSlideUp = false;
//			currentNewsIndex = getNewsIndexById(GCMIntentService.pushMessageNewsId);
//		}

		if(!isFirstResume){

			if(listNewsItemServer == null || listNewsItemServer.isEmpty() || listCatItemServer == null || listCatItemServer.isEmpty()){

				loadNewsCatFromDB();
			}


		}else{
			isFirstResume = false;
			//refresh();
		}
	}
	
	
	
	private int getNewsIndexById(long id){
		int index = -1;
		if(listNewsItemServer != null && listNewsItemServer.size() > 0){
			
			for (int i = 0;i< listNewsItemServer.size();i++){
				Object_ListItem_MainNews newsObj = listNewsItemServer.get(i);
				if(newsObj.getId() == id)
				{
					index = i - 1 ;
					break;
				}
			}
		}
		
		return index;
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
		//Object_ListItem_MainNews objDummySettings = new Object_ListItem_MainNews();
		//objDummySettings.setId(-999);
		//listNewsItemServer.add(objDummySettings);
		currentNewsIndex = -1;
		isMovingViewCurrent = true;
		if(comingFromPushMessage){
			comingFromPushMessage = false;
			//if(!(GCMIntentService.pushMessageHeader.isEmpty()))
				//Globals.showAlertDialogOneButton("News Flash",GCMIntentService.pushMessageHeader +"\n\n"+GCMIntentService.pushMessageText, this, "OK", null, false);
		
			
			if(GCMIntentService.pushMessageNewsId > 0){
				//isMovingViewCurrent = true;
				currentNewsIndex = getNewsIndexById(GCMIntentService.pushMessageNewsId);
			}
			if(GCMIntentService.pushMessageNeedsPopUp == 1){
				//if(!(GCMIntentService.pushMessageHeader.isEmpty()))
					Globals.showAlertDialogOneButton(GCMIntentService.pushMessageHeader, GCMIntentService.pushMessageText, this, "OK", null, false);
			}
		}
		//arraySelectedCatIds.clear();
		viewStatic = createNewsView();
		///Harsh
		scaleAnimationOver();


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

		int copyCurrentNewsIndex = currentNewsIndex;
				
		if(isMovingViewCurrent){
			if(currentNewsIndex >= listNewsItemServer.size() - 1){
				
				//if(copyCurrentNewsIndex == listNewsItemServer.size() - 1){
					//currentNewsIndex++;
					//rlytNewsContent.addView(initViewSetting(),0);
					//return viewSettings;
				//}else{
					//currentNewsIndex = listNewsItemServer.size() ;
				//}
				currentNewsIndex = listNewsItemServer.size() - 1;
				Toast.makeText(this, "You are done for the day!", Toast.LENGTH_SHORT).show();
				/*mDialog = Globals.showLoadingDialog(mDialog,this,false);
				
				Object_ListItem_MainNews objNews = listNewsItemServer.get(copyCurrentNewsIndex);
				//getNewsDataFromServer(Integer.valueOf(objNews.getCatId()),Globals.CALL_TYPE_OLD, Integer.valueOf(objNews.getId()), Globals.FINAL_NEWS_LIMIT_LOAD_OLD);
				getNewsDataFromServer(-1,Globals.CALL_TYPE_OLD, Integer.valueOf(objNews.getId()), Globals.FINAL_NEWS_LIMIT_LOAD_OLD);
				*/
				return null;
			}

			copyCurrentNewsIndex ++;	
			
			//Special case when no slide
			if(copyCurrentNewsIndex == 0){
				imgGoToTop.setImageResource(R.drawable.home_header_refresh);
				currentNewsIndex = copyCurrentNewsIndex;
				imgGoToTop.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						onClickRefresh(arg0);
						
					}
				});
				setHeader();
				
			}else{
				imgGoToTop.setImageResource(R.drawable.home_header_up);
				imgGoToTop.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						setFirstView();
						
					}
				});
			}
		}

		else{
			if(copyCurrentNewsIndex <= 0){
				copyCurrentNewsIndex  = 0;
				currentNewsIndex = copyCurrentNewsIndex;
				Toast.makeText(this, "No more news to show at this moment.", Toast.LENGTH_SHORT).show();
				//////Commented to copy functionality of Murmur
				
				/*mDialog = Globals.showLoadingDialog(mDialog,this,false);
				
				Object_ListItem_MainNews objNews = listNewsItemServer.get(copyCurrentNewsIndex);
				//getNewsDataFromServer(Integer.valueOf(objNews.getCatId()),Globals.CALL_TYPE_NEW, Integer.valueOf(objNews.getId()), Globals.FINAL_NEWS_LIMIT_LOAD_NEW);
				getNewsDataFromServer(-1,Globals.CALL_TYPE_NEW, Integer.valueOf(objNews.getId()), Globals.FINAL_NEWS_LIMIT_LOAD_NEW);
				*/
				return null;
			}
			copyCurrentNewsIndex--;
		}
		Object_ListItem_MainNews objNews = listNewsItemServer.get(copyCurrentNewsIndex);
		//objNews = listNewsItemServer.get(currentNewsIndex);
		///if(arraySelectedCatIds.size() > 0 && !isSelectedId(Integer.valueOf(objNews.getCatId())) ){//selectedCatId !=0  && objNews.getCatId() != selectedCatId){
			///currentNewsIndex = copyCurrentNewsIndex;
			///return createNewsView();
		///}

		//Below code is to add custom last page .. commented as of now
		/*
		if(objNews.getId() == -999){
			currentNewsIndex = copyCurrentNewsIndex;
			View viewSettting = initViewSetting(true);
			rlytNewsContent.addView(viewSettting,0);
			return viewSettting;
		}
		*/
		LayoutInflater inflater = (LayoutInflater)this.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		View newView = inflater.inflate(R.layout.view_news_sliding_home, rlytNewsContent,false);

		///setTextContainerHeight(newView,objNews.getHeadingSpan().toString(),objNews.getContentSpan().toString());


		ImageView imgViewNews = (ImageView) newView.findViewById(R.id.imgHome);
		
		
		TextView txtViewNews = (TextView) newView.findViewById(R.id.txtHeading);
		TextView txtAuthorText=(TextView) newView.findViewById(R.id.txtAuthorText);
		TextView txtAuthor=(TextView) newView.findViewById(R.id.txtAuthorDate);
		TextView txtCategory = (TextView)newView.findViewById(R.id.txtCatFooter);
		
		//HARSH : Not using Cat Color in version 3
		//String catColor = dbCat.getCategoryColor(objNews.getCatId());
		//Log.i("Darsh", "catColor"+catColor);
		//if(!catColor.isEmpty()){
			//rlytImgContainer.setBackgroundColor(Color.parseColor(catColor));
		//}
		
		String stringAuthorAndDate = objNews.getAuthor() + " / " +getFormatedDateTime(objNews.getDate());
		
		txtViewNews.setText(objNews.getHeadingSpan().toString());
		txtCategory.setText(objNews.getCatName());

		
		txtAuthor.setText(stringAuthorAndDate);
		
		//set FONT

		///Typeface tf = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_FONT);
		///Typeface tfCat = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_CAT_FONT);
		//Typeface tf = Typeface.createFromAsset(getAssets(), "Raleway-Regular.ttf");
			
		



		Globals.loadImageIntoImageView(imgViewNews, objNews.getImagePath(), this);

		if(isMovingViewCurrent){
			rlytNewsContent.addView(newView,0);
		}

		else{
			rlytNewsContent.addView(newView);
		}


		if(isSlideInProgress && !isMovingViewCurrent){
			newView.setY(-1*rlytNewsContent.getHeight());
		}
		
		ImageView imageDeatil = (ImageView)newView.findViewById(R.id.imgShowDetail);
		//ImageView imgComment = (ImageView)newView.findViewById(R.id.imgComment);
		switch (objNews.getTypeId()) {
		case Globals.NEWS_TYPE_ID_ONLY_IMAGE:
			imageDeatil.setVisibility(View.GONE);
			//imgComment.setVisibility(View.GONE);
			break;
		case Globals.NEWS_TYPE_ID_TEXT:
			//imageDeatil.setVisibility(View.VISIBLE);
			imageDeatil.setImageResource(R.drawable.home_footer_view);
			
			break;
		case Globals.NEWS_TYPE_ID_VIDEO:
			imageDeatil.setImageResource(R.drawable.home_footer_play);
			//imgComment.setVisibility(View.GONE);
			break;

		default:
			break;
		}
		return newView;
	}
	//TODO
	public void onClickShareMain(View v){
		
	}
	
	public void shareIntent()
    {    
		if (currentNewsIndex >= 0 &&  currentNewsIndex < listNewsItemServer.size() - 1) {

			Object_ListItem_MainNews currentNewsItem = listNewsItemServer.get(currentNewsIndex);
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			//sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
			sendIntent.putExtra(Intent.EXTRA_SUBJECT,currentNewsItem.getHeadingSpan()  + "\n\n");
			
			if (currentNewsItem.getShareLink() != null && !currentNewsItem.getShareLink().trim().equals("")) 
			{
				sendIntent.putExtra(
						Intent.EXTRA_TEXT,
						// currentNewsItem.getContent()
						"Read more @\n"
						+ currentNewsItem.getShareLink()
								+ "\n\nvia "
								+ getResources().getString(
										R.string.news_paper_name) +" for Android" + "\nDownload @ "+ Globals.SHARE_URL);
			} 
			else 
			{
				sendIntent.putExtra(
						Intent.EXTRA_TEXT,
						//"Read more @\n"
								//+ getResources().getString(
										//R.string.txt_company_website)//+"/detail/"+currentNewsItem.getId()
						currentNewsItem.getHeadingSpan()+	
						"\n\nvia "
										+ getResources().getString(
												R.string.news_paper_name)+" for Android" + "\nDownload @ "+ Globals.SHARE_URL);
			}

			File imgF = takeScreenshot(
					//"Read more @ "
					//+ getResources().getString(
							//R.string.txt_company_website)//+"/detail/"+currentNewsItem.getId()
							"via "
							+ getResources().getString(
									R.string.news_paper_name)+" for Android");

			sendIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(imgF) );

			//sendIntent.setType("text/plain");
			sendIntent.setType("image/*"); 
			sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); 
			//startActivity(sendIntent);
			//startActivity(Intent.createChooser(sendIntent, "Share Via"));
			
			ArrayList<String> listPackageDetail = getPackageName(shareOptionNo);
			
			if(listPackageDetail.size()==0){
				// Open all options
				startActivity(Intent.createChooser(sendIntent, "Share Via"));
			}
			else if(listPackageDetail.size()>0)
			{
				if(isPackageInstalled(listPackageDetail.get(0),this)){
			
					sendIntent.setPackage(listPackageDetail.get(0)); 
					startActivity(Intent.createChooser(sendIntent, "Share Image"));

				}else{
					Toast.makeText(getApplicationContext(), "Please Install "+listPackageDetail.get(1), Toast.LENGTH_LONG).show();
				}
			}
		}
		
    }
	private ArrayList<String> getPackageName(int optionNumber)
    {
    	ArrayList<String> res = new ArrayList<String>();
    	switch(optionNumber)
    	{
    		case 5:
    			res.add("com.whatsapp");
    			res.add("Whatsapp");
    			break;
    		case 4:
    			res.add("com.twitter.android");
    			res.add("Twitter");
    			break;
    		case 3:
    			res.add("com.facebook.katana");
    			res.add("Facebook");;
    			break;
    		case 2: 
    			res.add("com.facebook.orca");
    			res.add("Messenger");
    			break;
    		case 1:
    			res.add("com.google.android.gm");
    			res.add("Gmail");
    			break;
    		default:
    			break;
    	}
    	return res;
    }
	//TODO
    private boolean isPackageInstalled(String packagename, Context context) {
	    PackageManager pm = context.getPackageManager();
	    try {
	        pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
	        return true;
	    } catch (NameNotFoundException e) {
	        return false;
	    }
	}

	private void slide(int height , long duration){

		if(viewMoving != null){

			viewMoving.animate().setDuration(duration)
			.translationY(height)
			.alpha(1.0f);
			Log.i("Bytes", "viewMoving not null");
			
			if(viewStatic != null && currentNewsIndex < listNewsItemServer.size()-1){
				RelativeLayout imgContainer =(RelativeLayout) viewStatic.findViewById(R.id.rlytImgContainer);
				RelativeLayout imgCover =(RelativeLayout) viewStatic.findViewById(R.id.rlytImgCover);
				float scale = Math.abs((float)viewMoving.getY() /rlytNewsContent.getHeight()) ;

				
				float alpha = 1 - scale; // scale 0 alpha is 1 and when scale is 1 aplha is 0
				scale = (float) (0.80 + scale * 0.20); // 
				 
						
				
				Log.i("DARSH","scale"+scale+ "alpha "+ alpha);
				if(imgCover!= null)
				imgCover.setAlpha(alpha);
				//imgCover.animate()
				//LinearLayout.LayoutParams params =(LinearLayout.LayoutParams) imgContainer.getLayoutParams();
				
				if(imgContainer!= null)
				imgContainer.animate().setDuration(duration).scaleX(scale).scaleY(scale);
				
			}else{
				Log.i("viewStatic", "viewMoving is null");
			}

		}else{
			Log.i("Bytes", "viewMoving is null");
		}

		
	}
	
	private void  addLoadingView(){

		if(viewLoading == null){
			LayoutInflater inflater = (LayoutInflater)this.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

			viewLoading =(RelativeLayout) inflater.inflate(R.layout.view_loading, rlytMainContent,false);
			
			int y = Globals.getScreenSize(this).y;
			
			ImageView imgLoadingFooter =(ImageView) viewLoading.findViewById(R.id.imgLoadingFooter);
			ImageView imgLoadingNewzByte =(ImageView) viewLoading.findViewById(R.id.imgLoadingNewzByte);
			
			imgLoadingFooter.getLayoutParams().height = (int) (y * 0.4);
			imgLoadingFooter.getLayoutParams().width = (int) (y * 0.4);
			
			imgLoadingNewzByte.getLayoutParams().height = (int) (y * 0.25);
			
			rlytMainContent.addView(viewLoading);
			
		}
		
		viewLoading.setVisibility(View.VISIBLE);

		//ImageView imgViewLogo = (ImageView)viewLoading.findViewById(R.id.imgLogoXB);

		//int screenWidth = Globals.getScreenSize(this).x;
		//int logoWidth = screenWidth/100 * 50 ;
		//Options options = new BitmapFactory.Options();
		//options.inScaled = false;
		//Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.xb, options);
		//logo = Globals.scaleToWidth(logo,logoWidth);
		//imgViewLogo.setImageBitmap(logo);
		
		//rlytMainContent.removeView(viewLoading);
		//rlytMainContent.addView(viewLoading);
		//imgLoadingAnim =(ImageView) viewLoading.findViewById(R.id.imgLoadingAnim);
		
		
		try{
			//imgLoadingAnim.setBackgroundResource(0);
			//imgLoadingAnim.setBackgroundResource(R.drawable.anim_newzbyte_intro);
			
			//bAmin = (AnimationDrawable) imgLoadingAnim.getBackground();
			//bAmin.setOneShot(true);
			//bAmin.start();
			/*
			loadingAnimationStarted = true;
				
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
			  @Override
			  public void run() {
				  loadingAnimationStarted = false;
			  }
			}, getTotalAnimationDuration(bAmin));
			*/
		}
		catch(OutOfMemoryError err){
			Log.e("HARSH", "Memory Error !");
			System.gc();
		}
		catch(Exception ex){

		}
		//startAnimationLoading(100);

	}

	public int getTotalAnimationDuration(AnimationDrawable bAmin) {

        int iDuration = 0;

        for (int i = 0; i < bAmin.getNumberOfFrames(); i++) {
            iDuration += bAmin.getDuration(i);
        }
        
        iDuration += 100;
        return iDuration;
    }
	private void hideLoadingView(){

		if(viewLoading!= null){

			/*
			//if(bAmin != null){
				if(loadingAnimationStarted){

					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
					  @Override
					  public void run() {
						  Activity_Home.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									hideLoadingView();
								}
							});
					  }
					}, 200);
					
					return;

				}
			//}
			 
			 
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
					imgLoadingAnim =(ImageView) viewLoading.findViewById(R.id.imgLoadingAnim);
					imgLoadingAnim.setBackgroundResource(0);
					viewLoading.setVisibility(View.GONE);
					rlytMainContent.removeView(viewLoading);
					viewLoading = null;
					bAmin = null;
				}

				@Override
				public void onAnimationCancel(Animator arg0) {
					viewLoading.setVisibility(View.GONE);
					rlytMainContent.removeView(viewLoading);
					viewLoading = null;
					bAmin = null;

				}
			});
			Log.i("Bytes", "viewMoving not null");

		}else{
			Log.i("Bytes", "viewMoving is null");
		}
		
		*/
			viewLoading.setVisibility(View.GONE);
			rlytMainContent.removeView(viewLoading);
			viewLoading = null;
		}

	}
/**
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
		int totalheight = titleHeight *2 + contentHeight * (NO_OF_ROWS_NEWSCONTENT + 3)+paddingHeight; 
		Log.i("DARSH", "totalheight = "+totalheight);
		params.height =  totalheight;

		container.setLayoutParams(params);
	}
	
	**/
	private void slideComplete(float velocity, float currentY){
		
		Log.i("DARSH", "slideComplete");
		if(isNoMoreNews){
			isNoMoreNews = false;
		}

		if(isSlideInProgress){
			int moveTo = 0;
			isSlideInProgress = false;
			boolean newViewShown = false;;
			
			if(viewMoving != null){

				if(isSlideUp ){
					moveTo = -1 * rlytNewsContent.getHeight(); 
					if(isMovingViewCurrent){

						newViewShown = true;
						if(currentNewsIndex >= listNewsItemServer.size() - 1)
							currentNewsIndex = listNewsItemServer.size() - 1;
						else{
							currentNewsIndex ++;
						}
							
					}
						
				}else if(!isMovingViewCurrent){
					newViewShown = true;
					if(currentNewsIndex <= 0)
						currentNewsIndex  = 0;
					else{
						
						currentNewsIndex--;
					}
						
				}
				
				if(currentNewsIndex == 0){
					imgGoToTop.setImageResource(R.drawable.home_header_refresh);
					imgGoToTop.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							onClickRefresh(arg0);
							
						}
					});
					
				}else{
					imgGoToTop.setImageResource(R.drawable.home_header_up);
					imgGoToTop.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							setFirstView();
							
						}
					});
				}
				
				if(newViewShown){
					
					setHeader();
				}
				
				/*
				float currentMovement = Math.abs(y - y2) ;
				long elapseTimeMilliSec =(long) (( System.nanoTime() - startTime )/ 1000000.0);
				if(elapseTimeMilliSec > 0){
				long speed = (long) ((currentMovement - MAX_TOUCH_VALUE)/elapseTimeMilliSec);
				*/
				float currentMovement = Math.abs(y - currentY) ;
				long newDuration = DEFAULT_MAX_SLIDE_DURATION;
				float speed = Math.abs(velocity);
				long delay = 10;
				if(speed > 0){
					newDuration = (long) (((rlytNewsContent.getHeight() - currentMovement) * 1000) / speed);
					delay =0;
				}
				
				
				
				Log.i("Bytes", "newDuration = "+newDuration);

				if(newDuration > DEFAULT_MAX_SLIDE_DURATION)
					newDuration = DEFAULT_MAX_SLIDE_DURATION;
				if(newDuration < DEFAULT_MIN_SLIDE_DURATION)
					newDuration = DEFAULT_MIN_SLIDE_DURATION;


				viewMoving.animate().setDuration(newDuration).setStartDelay(delay)
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
						moveAnimationOver();

					}

					@Override
					public void onAnimationCancel(Animator animation) {
						//moveAnimationOver();
					}
				});
				
				if(viewStatic != null && currentNewsIndex < listNewsItemServer.size()-1){
					RelativeLayout imgContainer =(RelativeLayout) viewStatic.findViewById(R.id.rlytImgContainer);
					RelativeLayout imgCover =(RelativeLayout) viewStatic.findViewById(R.id.rlytImgCover);
					float alpha =1;
					Log.i("DARSH", "alpha "+ alpha);
					
					if(isSlideUp ){
						alpha = 0;
					}
					if(imgCover!= null)
					imgCover.animate().setStartDelay(delay).setDuration(newDuration).alpha(alpha);
					
					//imgCover.animate()
					//LinearLayout.LayoutParams params =(LinearLayout.LayoutParams) imgContainer.getLayoutParams();
					
					if(imgContainer!= null)
					imgContainer.animate().setStartDelay(delay).setDuration(newDuration).scaleX(1-alpha).scaleY(1-alpha).setListener(new AnimatorListener() {

						@Override
						public void onAnimationStart(Animator animation) {
						}

						@Override
						public void onAnimationRepeat(Animator animation) {

						}

						@Override
						public void onAnimationEnd(Animator animation) {
							scaleAnimationOver();

						}

						@Override
						public void onAnimationCancel(Animator animation) {
							//scaleAnimationOver();
						}
					});
					
				}else{
					Log.i("viewStatic", "viewMoving is null");
				}
			}
		}
	}
	
	private void setHeader(){
		
		if(currentNewsIndex >=0 && currentNewsIndex < listNewsItemServer.size() - 1)
		{
		//RelativeLayout rlytNewsHeaderIconContainer  =(RelativeLayout)findViewById(R.id.rlytNewsHeaderIconContainer);
		//DBHandler_Category dbCat = new DBHandler_Category(this);
		Object_ListItem_MainNews objNews = listNewsItemServer.get(currentNewsIndex);
		//String catColor = dbCat.getCategoryColor(objNews.getCatId());
		//Log.i("Darsh", "catColor"+catColor);
		//if(!catColor.isEmpty()){
			//rlytNewsHeaderIconContainer.setBackgroundColor(Color.parseColor(catColor));
		//}
		//TextView txtCategory = (TextView)findViewById(R.id.txtCatHeading); //Its activity control now
		//Typeface tfCat = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_CAT_FONT);
		//txtCategory.setTypeface(tfCat);
		Log.i("DARSH", "objNews.getCatName()"+objNews.getCatName());
		//txtCategory.setText(dbCat.getCategoryName(objNews.getCatId()));
		}
	}

	private void scaleAnimationOver(){
		Log.i("DARSH", "scaleAnimationOver");
		if(!isAnimInProgress){
			if(viewStatic != null){
				RelativeLayout imgContainer =(RelativeLayout) viewStatic.findViewById(R.id.rlytImgContainer);
				imgContainer.animate().setListener(null);
				
				if((isSlideUp && isMovingViewCurrent) || (!isSlideUp && !isMovingViewCurrent)){
				
				ImageView imageDeatil = (ImageView)viewStatic.findViewById(R.id.imgShowDetail);
				//RelativeLayout rlytButtonsContainer = (RelativeLayout)findViewById(R.id.rlytButtonsContainer);
				//RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)imageDeatil.getLayoutParams();
				
				//int originalX = lp.width;
				//int originalY = lp.height;
				
				//lp.height = 10;
				//lp.width = 10;
				
				//imageDeatil.setLayoutParams(lp);
				
				Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
				//Animation zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);
				imageDeatil.setAnimation(zoomin);
				//imageDeatil.setAnimation(zoomout);
				
				imageDeatil.startAnimation(zoomin);
				
				if((isSlideUp && isMovingViewCurrent)){
					hideTopIconsBar();
				}else{
					showTopIconsBar();
				}
				
				}
			}
		}
	}
	private void moveAnimationOver(){
		
		isAnimInProgress = false;
		/*
		if(isSlideUp && isMovingViewCurrent){
			rlytNewsContent.removeView(viewMoving);
		}else if (!isSlideUp && isMovingViewCurrent){
			rlytNewsContent.removeView(viewStatic);						
			viewStatic = viewMoving;
		}else if (isSlideUp && !isMovingViewCurrent){
			rlytNewsContent.removeView(viewMoving);
		}else if(!isSlideUp && !isMovingViewCurrent)
		*/
		
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
	
	
	private void hideTopIconsBar(){
		
		if(!isTopIconBarHidden){
			
			RelativeLayout layout = (RelativeLayout)findViewById(R.id.rlytNewsHeaderIconContainer);
			layout.animate().setDuration(300).translationY(-1*layout.getHeight());
			isTopIconBarHidden = true;
		}
		
	
	}
	
	private void showTopIconsBar(){
		if(isTopIconBarHidden){
			RelativeLayout layout = (RelativeLayout)findViewById(R.id.rlytNewsHeaderIconContainer);
			layout.animate().setDuration(300).translationY(0);
			isTopIconBarHidden = false;
		}
	}
	@Override 
    public boolean onTouchEvent(MotionEvent touchevent){ 
        this.mDetector.onTouchEvent(touchevent);
        // Be sure to call the superclass implementation
        
        switch (touchevent.getAction())
		{
		// when user first touches the screen we get x and y coordinate
			case (MotionEvent.ACTION_UP): 
				 Log.d(DEBUG_TAG,"onTouchUp: " );
				 slideComplete(0,touchevent.getY());
				 return false;
			case (MotionEvent.ACTION_CANCEL): 
				 Log.d(DEBUG_TAG,"onTouchCancel: " );
				slideComplete(0,touchevent.getY());
				 return false;
				 
			default : 
	            return super.onTouchEvent(touchevent);
		}
        
        //return super.onTouchEvent(touchevent);
    }

    @Override
    public boolean onDown(MotionEvent event) { 
        Log.d(DEBUG_TAG,"onDown: " );//+ event.toString()); 
        y = event.getY();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, 
            float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: VelocityX = "+ velocityX + " VelocityY = "+ velocityY );//+ event1.toString()+event2.toString());
        
        if (e1.getX() < e2.getX()) {
            Log.d(DEBUG_TAG, "Left to Right swipe performed");
        }
     
        if (e1.getX() > e2.getX()) {
            Log.d(DEBUG_TAG, "Right to Left swipe performed");
        }
     
        if (e1.getY() < e2.getY() | e1.getY() > e2.getY()) {
            Log.d(DEBUG_TAG, "Up to Down swipe performed");
            slideComplete(velocityY,e2.getY());
            
        }
     
       // if (e1.getY() > e2.getY()) {
          //  Log.d(DEBUG_TAG, "Down to Up swipe performed");
       // }
        
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: X = " + distanceX + " and Y = "+distanceY );//+ e1.toString()+e2.toString());

        Log.d(DEBUG_TAG, "onScroll: e1.y = " + e1.getY() + " and e2.y = "+e2.getY() );
        
        if(isAnimInProgress || isNoMoreNews)
			return true;
        
        y = e1.getY(); // Motion event for first touch of scroll.
        float totalDisplacementY = Math.abs(e1.getY()-e2.getY());
        
        if(Math.abs(distanceY) > Math.abs(distanceX) ){
        	if(distanceY > 0){
        		isSlideUp = true;
				Log.i("Bytes", "ACTION_MOVE UP");
        	}else{
        		isSlideUp = false;
				Log.i("Bytes", "ACTION_MOVE DOWN");
        	}
        	
			if(!isSlideInProgress ){
				isSlideInProgress = true;
				if(isSlideUp){
					isMovingViewCurrent = true;
					viewMoving = viewStatic;
					//int backUpId = currentNewsIndex;
					viewStatic = createNewsView();
					if(viewStatic == null){
						viewStatic = viewMoving;
						viewMoving = null;
						isNoMoreNews = true;
						isSlideInProgress = false;
						//currentNewsIndex = backUpId;
						return false;
					}
				}else{
					isMovingViewCurrent = false;
					//int backUpId = currentNewsIndex;
					viewMoving = createNewsView();
					if(viewMoving == null){
						isNoMoreNews = true;
						isSlideInProgress = false;
						//currentNewsIndex = backUpId;
						return false;
					}
				}

			}

			if(isSlideInProgress){
				if(isMovingViewCurrent)
					slide(-1 *(int)totalDisplacementY,0); 
				else
					slide((-1*rlytNewsContent.getHeight()) + (int)totalDisplacementY,0);
			}
		}
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " );//+ event.toString()); 
    }
     
   
    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " );//+ event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " );//+ event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " );//+ event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " );//+ event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " );//+ event.toString());
        tapOnView();
        return true;
    }
    
	/*
	 
	 
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

*/
    private void tapOnView(){
    	
    	/*
    	if(viewStatic != null){
    		ArcMenu arcMenu = (ArcMenu)viewStatic.findViewById(R.id.arcMenu1);
    		if(arcMenu != null)
    			if(arcMenu.getArcLayout().isExpanded()){
    				arcMenu.getControlLayout().setBackgroundResource(R.drawable.arcmenu_share_share_btn);
    				arcMenu.getArcLayout().switchState(true);
    			}
    				
    	}
    	*/
    	if(isTopIconBarHidden)
    		showTopIconsBar();
    	else
    		hideTopIconsBar();
    	
    }
	public void tapOnShowDetails(View v){

		if(viewLoading!= null )
			if(viewLoading.getVisibility() == View.VISIBLE)
				return;

		if (currentNewsIndex >= 0 &&  currentNewsIndex < listNewsItemServer.size() - 1) {
			Object_ListItem_MainNews obj =	listNewsItemServer.get(currentNewsIndex);
			
			switch (obj.getTypeId()) {
			case Globals.NEWS_TYPE_ID_ONLY_IMAGE:
				break;
			case Globals.NEWS_TYPE_ID_TEXT:
					navigateToNewsDetail(obj);
				break;
			case Globals.NEWS_TYPE_ID_VIDEO:
				if(obj.getVideo() == null || obj.getVideo().isEmpty()){
					Toast.makeText(getApplicationContext(), "Video not available", Toast.LENGTH_SHORT).show();
				}
				Custom_YouTubePlayerActivity.videoKey = obj.getVideo();
				Log.d("HARSH", "videoKey"+Custom_YouTubePlayerActivity.videoKey);
				try{
				Intent i = new Intent(this, Custom_YouTubePlayerActivity.class);
		    	//this.finish();
		    	this.startActivity(i);
				}catch (Exception e) {
					
				}
				break;

			default:
				break;
			}
			
			
			
		}
	}
	public void onClickMenu(View v){

		if(isDrawerOpen){                
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}else{              
			mDrawerLayout.openDrawer(Gravity.LEFT);
		}

	}

	/*
	public void onClickAllCategory(View v){

		arraySelectedCatIds.clear();
		onClickCategory();
	}
	*/

	public void onClickCategoryItem(View v){

		Log.d("HARSH", "onClickCategoryItem");
		
		Integer selectedCatId = ((Integer)v.getTag(R.string.app_name)).intValue();
		
		DBHandler_CategorySelection dbH = new DBHandler_CategorySelection(this);
		boolean contains = dbH.containsCatId(selectedCatId);
		
		ArrayList<Integer> Ids = dbH.getAllCategories();
		
		for(Integer id : Ids){
			if(id.intValue() == selectedCatId){
				contains = true;
				break;
			}
		}
		
		if(contains){
			if(Ids.size() > 1)
				dbH.clearCategory(selectedCatId);
			else{
				Toast.makeText(this, "You have to select atleast one category!", Toast.LENGTH_SHORT).show();
				return;
			}
				
			Log.d("HARSH", "dbH.clearCategory(catId) "+selectedCatId);
		}
		else{
			dbH.insertSelectedCat(selectedCatId);
			Log.d("HARSH", "dbH.insertCategory(catId) "+selectedCatId);
		}
		
		View cView = v.findViewById(R.id.imgViewCat);
		if(cView!= null && cView.getClass() == ImageView.class){
			ImageView imageView = (ImageView)cView;
			int index = ((Integer)imageView.getTag(R.string.app_name)).intValue();
			if(listCatItemServer!= null && listCatItemServer.size() > index){
				Object_Category obj = listCatItemServer.get(index);
				if(contains){
					Log.d("HARSH", "1");
					Globals.loadImageIntoImageView(imageView, obj.getImageName(), this,R.drawable.cat_loading,R.drawable.cat_loading);
				}else{
					Log.d("HARSH", "2");
					Globals.loadImageIntoImageView(imageView, obj.getSelectedImageName(), this,R.drawable.cat_loading_selected,R.drawable.cat_loading_selected);
				}
			}
			
		}
	
		//setBootomBarStatus();
		isCategoryChanged = true;
	}
	
	

	private void setFirstView(){
		currentNewsIndex = -1;
		isMovingViewCurrent = true;
		View view = createNewsView();
		
		if(view != null){
			viewStatic = view;
			scaleAnimationOver();
		}
		viewMoving = null;

		if(rlytNewsContent.getChildCount() > 1)
			rlytNewsContent.removeViews(1, rlytNewsContent.getChildCount() -1);
	}

	
	private boolean isNotContainsId(int id){
		
		boolean returnVal = true;
		for(Object_ListItem_MainNews item : listNewsItemServer){
			if(item.getId() == id){
				returnVal = false;
				break;
			}
			
		}
		return returnVal;
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

		if (currentNewsIndex >= 0 &&  currentNewsIndex < listNewsItemServer.size() - 1) {



			Object_ListItem_MainNews currentNewsItem = listNewsItemServer.get(currentNewsIndex);
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			//sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);

			
				sendIntent.putExtra(Intent.EXTRA_SUBJECT,
						currentNewsItem.getHeadingSpan()  + "\n\n");
			



			if (currentNewsItem.getShareLink() != null && !currentNewsItem.getShareLink().trim().equals("")) 
			{
				sendIntent.putExtra(
						Intent.EXTRA_TEXT,
						// currentNewsItem.getContent()
						//"Read more @\n"
						//+ getResources().getString(
								//R.string.txt_company_website)
						currentNewsItem.getHeadingSpan()+
								"\n\nvia "
								+ getResources().getString(
										R.string.news_paper_name) +" for Android" + "\nDownload @ "+ Globals.SHARE_URL);
			} 
			else 
			{
				sendIntent.putExtra(
						Intent.EXTRA_TEXT,
						//"Read more @\n"
								//+ getResources().getString(
										//R.string.txt_company_website)//+"/detail/"+currentNewsItem.getId()
						currentNewsItem.getHeadingSpan()+			 
						"\n\nvia "
										+ getResources().getString(
												R.string.news_paper_name)+" for Android" + "\nDownload @ "+ Globals.SHARE_URL);
			}

			File imgF = takeScreenshot(
					//"Read more @ "
					//+ getResources().getString(
							//R.string.txt_company_website)//+"/detail/"+currentNewsItem.getId()
							 " via "
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
/*
	private void drawerEventAnim(){

		try{
			bAmin = (AnimationDrawable) btnMenu.getBackground();
			bAmin.setOneShot(true);
			bAmin.start();
		}catch(Exception ex){

		}
	}
*/
	private void createDrawerCategories(){

		LinearLayout llytCatContainer = (LinearLayout)findViewById(R.id.llytCatContainer);
		///ImageView btnCatAll = (ImageView)findViewById(R.id.btnCatAll);

		if(llytCatContainer.getChildCount() > 0){
			llytCatContainer.removeAllViews();
		}
		
		///if(llytCatContainer.getChildCount() > 1){
			///llytCatContainer.removeViews(1, llytCatContainer.getChildCount()-1);
		///}

		///if(arraySelectedCatIds.size() == 0){
			///btnCatAll.setImageResource(R.drawable.viewall_selected);
			///btnCatAll.setBackgroundResource(R.color.app_cat_color_5);
		///}else{
			///btnCatAll.setImageResource(R.drawable.selector_cat_all_button);
			///btnCatAll.setBackgroundResource(R.color.app_transparent);
		///}

		LinearLayout row = null;
		boolean firstInRow ;
		
		DBHandler_CategorySelection dbH = new DBHandler_CategorySelection(this);
		ArrayList<Integer> Ids = dbH.getAllCategories();
		//DBHandler_Category dbCat = new DBHandler_Category(this);
		for(int i = 0 ; i< listCatItemServer.size() ; i++){

			//if(i%3 == 0){
				firstInRow = true;
			//}else{
				//firstInRow = false;
			//}
			if(firstInRow){
				
				LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = (LinearLayout) inflater.inflate(R.layout.view_category_row_home, llytCatContainer ,false);
			}

			if(row != null){
				
				boolean contains = false;
				for(Integer id : Ids){
					if(id.intValue() == listCatItemServer.get(i).getId()){
						contains = true;
						break;
					}
				}
				
				Log.d("HARSH", "contains"+contains);
				Object_Category objCat = listCatItemServer.get(i);
				row.addView(getCatImageView(objCat,row,firstInRow,i,contains)) ;

				
				

				String catColor = objCat.getColor();//dbCat.getCategoryColor(objCat.getId());
				if(!catColor.isEmpty())
					row.setBackgroundColor(Color.parseColor(catColor));
				
//				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//						Globals.getScreenSize(this).x*3/2,      
//						LinearLayout.LayoutParams.WRAP_CONTENT
//				);
//				
//				int marginD = -1*Globals.getScreenSize(this).x/4;
//				params.setMargins(marginD, 0, marginD, 0);
//				row.setLayoutParams(params);
				
				if(firstInRow)
					llytCatContainer.addView(row);	
			}

		}
		
		ScrollView.LayoutParams params2 = new ScrollView.LayoutParams(
				ScrollView.LayoutParams.MATCH_PARENT,      
				ScrollView.LayoutParams.WRAP_CONTENT
		);
		
		int marginD = -1*Globals.getScreenSize(this).x/2;
		params2.setMargins(marginD, 0, marginD, 0);
		llytCatContainer.setLayoutParams(params2);
		
		llytCatContainer.setRotation(-1* getScrollViewDiagonalAngle());
		
		if(listCatItemServer.size() > 0){
			
			LinearLayout llytUpper = (LinearLayout)findViewById(R.id.llytDrwawerUpperHalf);
			LinearLayout llytLower = (LinearLayout)findViewById(R.id.llytDrwawerLowerHalf);
			
			
			
			String catColor1 = listCatItemServer.get(0).getColor();//dbCat.getCategoryColor(listCatItemServer.get(0).getId());
			if(!catColor1.isEmpty()){
				llytUpper.setBackgroundColor(Color.parseColor(catColor1));
			}
			
			String catColor2 = listCatItemServer.get(listCatItemServer.size() - 1).getColor();//dbCat.getCategoryColor(listCatItemServer.get(listCatItemServer.size() - 1).getId());
			if(!catColor2.isEmpty()){
				llytLower.setBackgroundColor(Color.parseColor(catColor2));
			}
			
		}
		
	}

	private float getScrollViewDiagonalAngle(){
		
		ScrollView scrollViewCat = (ScrollView)findViewById(R.id.scrollViewCat);
		
		if(scrollViewCat.getHeight() > 0 && scrollViewCat.getWidth() > 0){
			
			float angle = 90.0f - (float) Math.toDegrees(Math.atan((float)scrollViewCat.getHeight()/(float)scrollViewCat.getWidth()));
			//Log.d("DARSH","angle:"+ (float)scrollViewCat.getHeight()/(float)scrollViewCat.getWidth()+ " " +Math.atan(scrollViewCat.getHeight()/scrollViewCat.getWidth()) + " " +angle);
			//Log.d("DARSH","scrollViewCat.getHeight():"+scrollViewCat.getHeight() + "scrollViewCat.getWidth()"+scrollViewCat.getWidth());
			return angle;
		}
		
		return 30.0f;
	}
	@SuppressLint("NewApi")
	private RelativeLayout getCatImageView(Object_Category objCat , LinearLayout row,boolean firstInRow,int position,boolean contains){

		int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
		//int widthImage =(int) ((Globals.getScreenSize(this).x - 6* margin)/3.0) ;
		
		///
		int headerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, getResources().getDisplayMetrics());
		int totalHeight = Globals.getScreenSize(this).y - headerHeight;
		//ScrollView scrollViewCat = (ScrollView)findViewById(R.id.scrollViewCat);
		
		int heightImage = (int) ((totalHeight - 6* margin )/6.0 ) ;
		int widthImage = heightImage;
		///
		Log.d("DARSH","scrollViewCat.getHeight():"+totalHeight);
		Log.d("DARSH","widthImage:"+widthImage);
		//int catColor = this.getResources().getColor(Globals.getCategoryColor(objCat.getId(), this));

		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		RelativeLayout item = (RelativeLayout) inflater.inflate(R.layout.item_category_image_home, row ,false);
		item.setTag(R.string.app_name, Integer.valueOf(objCat.getId()));

		/*
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
		*/
		//item.getLayoutParams().width = widthImage;
		//item.getLayoutParams().height = item.getLayoutParams().width;

		ImageView imgView =(ImageView) item.findViewById(R.id.imgViewCat);
		imgView.setTag(R.string.app_name, position);
		//int heightImage = widthImage;//(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
				//100;//Globals.getScreenSize(this).y/5;
		
		//item.setMinimumWidth(widthImage);
		//item.setMinimumHeight(heightImage);

		
		RelativeLayout.LayoutParams params =(RelativeLayout.LayoutParams )imgView.getLayoutParams();
		params.height = heightImage;
		params.width = widthImage ;//+ margin;//;rlytDrawerPane.getLayoutParams().width - margin;
		
		//params.gravity = Gravity.RIGHT;
		imgView.setLayoutParams(params);
		
		
		//LinearLayout.LayoutParams paramsParent =(LinearLayout.LayoutParams )item.getLayoutParams();
		
		//if(!firstInRow)
			//paramsParent.leftMargin = margin;
		//params.gravity = Gravity.RIGHT;
		//item.setLayoutParams(paramsParent);

		/*
		new ImageView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 10, 10, 10);

		imgView.setLayoutParams(params);
		imgView.setScaleType(ScaleType.CENTER_CROP);
		imgView.setClickable(true);

		 */

		//Globals.loadImageIntoImageView(imgView, objCat.getImageName(), this, R.drawable.cat_loading, R.drawable.cat_loading);//(imgView, objCat.getImageName(), 0,0, this);//heightImage,widthImage

		if(contains){

			Globals.loadImageIntoImageView(imgView, objCat.getSelectedImageName(), this, R.drawable.cat_loading, R.drawable.cat_loading);//(imgView, objCat.getImageName(), 0,0, this);//heightImage,widthImage
			Globals.preloadImage(getApplicationContext(), objCat.getImageName()) ;
		}else{
			Globals.loadImageIntoImageView(imgView, objCat.getImageName(), this, R.drawable.cat_loading, R.drawable.cat_loading);//(imgView, objCat.getImageName(), 0,0, this);//heightImage,widthImage
			Globals.preloadImage(getApplicationContext(), objCat.getSelectedImageName()) ;
		}
		
		TextView txtCategory = (TextView)item.findViewById(R.id.txtCategory);

		//Typeface tfCat = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_CAT_FONT);
		//txtCategory.setTypeface(tfCat);
		txtCategory.setText(objCat.getName());
		txtCategory.setMaxWidth(widthImage-margin);
		//txtCategory.setBackgroundResource(Globals.getCategoryColor(objCat.getId(), this));

		item.setRotation(getScrollViewDiagonalAngle());
		return item;

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
					url, Custom_URLs_Params.getParams_CatNewsFirstCall(0,objAppConfig.getVersionNoAppConfig(),this),
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
	
	
	


	private void parseAppConfigJson(JSONObject response) {

		if (response == null){
			
			return;
		}
		Log.i("DARSH", "RESPONCE parseAppConfigJson is : "+response.toString());
		try {

			Object_AppConfig objConfig = new Object_AppConfig(this);

			// boolean hasNews = false;
			// boolean hasCategory = false;

			//// If news is there insert new news News
			if (response.has("news")) {

				Log.i("DARSH", "insertNewAndDeleteOldNews news onResponse" + response);

				Custom_JsonParserNews parserObject = new Custom_JsonParserNews();
				listNewsItemServer = parserObject.getParsedJsonMainNews(response.getJSONArray("news"),objConfig
						.getRootCatId());
				//Object_ListItem_MainNews objDummySettings = new Object_ListItem_MainNews();
				//objDummySettings.setId(-999);
				//listNewsItemServer.add(objDummySettings);
				currentNewsIndex = -1;
				if(comingFromPushMessage){
					comingFromPushMessage = false;
					//if(!(GCMIntentService.pushMessageHeader.isEmpty()))
						//Globals.showAlertDialogOneButton("News Flash",GCMIntentService.pushMessageHeader +"\n\n"+GCMIntentService.pushMessageText, this, "OK", null, false);
				
					if(GCMIntentService.pushMessageNewsId > 0){
						isMovingViewCurrent = true;
						currentNewsIndex = getNewsIndexById(GCMIntentService.pushMessageNewsId);
					}
					if(GCMIntentService.pushMessageNeedsPopUp == 1){
						//if(!(GCMIntentService.pushMessageHeader.isEmpty()))
							Globals.showAlertDialogOneButton(GCMIntentService.pushMessageHeader, GCMIntentService.pushMessageText, this, "OK", null, false);
					}
				}
				isMovingViewCurrent = true;
				viewStatic = createNewsView();
				rlytNewsContent.bringChildToFront(viewStatic);
				if(rlytNewsContent.getChildCount() > 1)
					rlytNewsContent.removeViews(0, rlytNewsContent.getChildCount()-1);
				preLoadImages();
				//hasNews = true;
				
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
						//hasCategory = true;
					}

				} 
			} 

			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
			  @Override
			  public void run() {
				  if(listNewsItemServer != null && listNewsItemServer.size() > 0){
		        		DBHandler_MainNews dbH = new DBHandler_MainNews(getApplicationContext());
			        	dbH.insertNewsItemList(listNewsItemServer,true);
				  }
				  if(listCatItemServer != null && listCatItemServer.size() > 0){
					  DBHandler_Category dbH2 = new DBHandler_Category(Activity_Home.this);
					  dbH2.setCategories(listCatItemServer);
				  }
				  
			  }
			}, 500);
			
			
        	
		  

		} catch (Exception ex) {
			Log.i("HARSH", "Error in parsin jSOn" + ex.getMessage());
		}

	}
	/*
	private class MyRunnable implements Runnable {
		  private boolean hasNews;
		  private boolean hasCategories;
		  public MyRunnable(boolean hasNews,boolean hasCategories) {
		    this.hasNews = hasNews;
		    this.hasCategories = hasCategories;
		  }

		  public void run() {
			  if(hasNews && listNewsItemServer != null){
	        		DBHandler_MainNews dbH = new DBHandler_MainNews(getApplicationContext());
		        	dbH.insertNewsItemList(listNewsItemServer,true);
	        	}
	        	
			  if(hasCategories && listCatItemServer != null){
				  DBHandler_Category dbH2 = new DBHandler_Category(Activity_Home.this);
				  dbH2.setCategories(listCatItemServer);
			  }
		  }
	}
	*/
	private void preLoadImages(){
		if(listNewsItemServer != null)
		for (Object_ListItem_MainNews item : listNewsItemServer) {
		     String url = item.getImagePath();
		          if (!TextUtils.isEmpty(url)) {
		        	Globals.preloadImage(getApplicationContext(), url) ;
		               
		               //
	                   // .resizeDimen(R.dimen.article_image_preview_width, R.dimen.article_image_preview_height)
	                    //.centerCrop()
		          }
		     
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

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);

		SimpleDateFormat currentdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
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

			String time = 
					Globals.getTwoDigitNo(c.get(Calendar.YEAR))+"-"+
					Globals.getTwoDigitNo(c.get(Calendar.MONTH)+1)+"-"+
					Globals.getTwoDigitNo(c.get(Calendar.DAY_OF_MONTH))+" "+
					String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
					String.format("%02d" , c.get(Calendar.MINUTE))+":"+
					String.format("%02d" , c.get(Calendar.SECOND)) +" ";


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

			if( day <=2){

				if(hour <= 6)
					return "Today";//day+" day ago";
				else if(hour <= 30)
				return "Yesterday";//day+" days ago";
			}

			/*
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
			*/

			SimpleDateFormat sdfDDMMYYYY = new SimpleDateFormat("d MMM",Locale.ENGLISH);
			//sdfDDMMYYYY.setTimeZone(TimeZone.getTimeZone("IST"));
			return sdfDDMMYYYY.format(formattedDate);
		}

		return dateString;
	}

	public void onClickRefresh(View v){
		refresh();
	}
	
	@Override
    public void onBackPressed() {
    	 if (doubleBackToExitPressedOnce) {
    	        super.onBackPressed();
    	        return;
    	    }

    	    this.doubleBackToExitPressedOnce = true;
    	    Toast.makeText(this, "Press back one more time to exit", Toast.LENGTH_SHORT).show();

    	    new Handler().postDelayed(new Runnable() {

    	        @Override
    	        public void run() {
    	            doubleBackToExitPressedOnce=false;                       
    	        }
    	    }, 2000);
    }

	public View initViewSetting( boolean isLast){
		View viewSettings = null;
		if(viewSettings == null){
			LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			
			//viewSettings = inflater.inflate(R.layout.view_cat_sliding_settings, rlytDrawerPane,false);
			viewSettings = inflater.inflate(R.layout.view_settings, rlytMainContent,false);

			Object_AppConfig obj = new Object_AppConfig(this);

			TextView txt =(TextView) viewSettings.findViewById(R.id.txtNotification);
			ImageView imgImageView1 = (ImageView)viewSettings.findViewById(R.id.imgNotification1);
			ImageView imgImageView2 = (ImageView)viewSettings.findViewById(R.id.imgNotification2);
			RelativeLayout rlytSettings =(RelativeLayout) viewSettings.findViewById(R.id.rlytSettings);
			TextView txtSeeU =(TextView) viewSettings.findViewById(R.id.txtSeeYouAt);
			
			if(isLast){
				rlytSettings.setBackgroundResource(R.color.app_white);
				txtSeeU.setText("See You At");
			}
			if(txt !=null){
				if(obj.isNotificationEnabled()){
					txt.setText(Globals.TEXT_NOTIFICATION_ENABLED);
					imgImageView1.setImageResource(R.drawable.notification_on1);
					imgImageView2.setImageResource(R.drawable.notification_on);
				}else{

					txt.setText(Globals.TEXT_NOTIFICATION_DISABLED);
					imgImageView1.setImageResource(R.drawable.notification_off1);
					imgImageView2.setImageResource(R.drawable.notification_off);
				}
			}
			
			/* Uncomment when supporting language
			TextView txtLanguage = (TextView) viewSettings.findViewById(R.id.txtLanguageSelected);

			if(obj.getLangId() == Globals.LANG_ENG){
				txtLanguage.setText("English");
			}else if(obj.getLangId() == Globals.LANG_HINDI){
				txtLanguage.setText("हिंदी");
			}
			*/
			//rlytMainContent.addView(viewSettings);
		}
		
		return viewSettings;
	}
	public void onClickSettings(View v){

		if(viewSettings == null){
			viewSettings = initViewSetting(false);
			rlytDrawerPane.addView(viewSettings);
		}
		
		viewSettings.setY(-1*rlytDrawerPane.getHeight());
		//viewSettings.setY(-1*rlytMainContent.getHeight());
		
		//disable functionality behind this setting activity
		
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);		
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

		TextView txt =(TextView) v.findViewById(R.id.txtNotification);
		ImageView imgImageView1 = (ImageView)v.findViewById(R.id.imgNotification1);
		ImageView imgImageView2 = (ImageView)v.findViewById(R.id.imgNotification2);
		if(obj.isNotificationEnabled()){
			obj.setNotificationEnabled(false);
			if(txt !=null){
				txt.setText(Globals.TEXT_NOTIFICATION_DISABLED);
				imgImageView1.setImageResource(R.drawable.notification_off1);
				imgImageView2.setImageResource(R.drawable.notification_off);
			}
		}else{
			obj.setNotificationEnabled(true);
			if(txt !=null){
				txt.setText(Globals.TEXT_NOTIFICATION_ENABLED);
				imgImageView1.setImageResource(R.drawable.notification_on1);
				imgImageView2.setImageResource(R.drawable.notification_on);
			}
		}


	}

	public void onClickPrivacyPolicy(View v){
		 
		
		Intent i = new Intent(this,Activity_Disclaimer.class);
		startActivity(i);

	}
	public void onClickLanguageChange(View v){
		Object_AppConfig obj = new Object_AppConfig(this);
		obj.setLangId(0);
		
		DBHandler_CategorySelection dbH = new DBHandler_CategorySelection(this);
		dbH.clearCategoryTable();
		Intent i = new Intent(this,Activity_ChooseLang.class);
		startActivity(i);
		
		this.finish();
	}

	public void onClickShareApp(View v){

		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, Globals.getShareAppMsg()+ "\n "+ Globals.SHARE_URL);
		//sendIntent.setPackage("com.whatsapp");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
	
	public void onClickComment(View v){
		
		Activity_NewsDetails.isNavigationForComment = true;
		tapOnShowDetails(v);
	}

	
	
	public void onClickRateApp(View v){

		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
		if(cd.isConnectingToInternet()){
			startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse("market://details?id=" + Globals.APP_PNAME)));

		}else{
			Toast.makeText(this, Globals.TEXT_NO_INTERNET_DETAIL_TOAST, Toast.LENGTH_SHORT).show();
		}
	}
	
	
}



/*

	private void startAnimationLoading(int delay){
		if(viewLoading == null || viewLoading.getVisibility() == View.GONE)
			return;
		
		
		viewLoading.removeAllViews();
		
		LayoutInflater inflater = (LayoutInflater)this.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);
	    llytGreen=(LinearLayout) inflater.inflate(R.layout.view_empty_layout, viewLoading, false);
		llytGreen.setBackgroundResource(R.color.app_loading_color_1);
		
		llytGreen.setY(-1*Globals.getScreenSize(this).y);
		viewLoading.addView(llytGreen);
		
		llytGreen.animate().setDuration(400).setStartDelay(delay)
		.translationY(0).setListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {///
				animationYellowMove();
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	private void animationYellowMove(){

		
		LayoutInflater inflater = (LayoutInflater)Activity_Home.this.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);
	    llytYellow=(LinearLayout) inflater.inflate(R.layout.view_empty_layout, viewLoading, false);
	    llytYellow.setBackgroundResource(R.color.app_loading_color_2);
		
	    llytYellow.setY(Globals.getScreenSize(Activity_Home.this).y);
		viewLoading.addView(llytYellow);
		
		llytYellow.animate().setDuration(400).setStartDelay(300)
		.translationY(0).setListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) { ///
				
				animationYellowFade();
				
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void animationYellowFade(){
		viewLoading.removeView(llytGreen);
		ImageView imgView = new ImageView(Activity_Home.this);
		imgView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		imgView.setImageResource(R.drawable.collarge);
		
		viewLoading.addView(imgView, 0);
		Animation fadeOut = AnimationUtils.loadAnimation(Activity_Home.this, R.anim.fade_out);
		fadeOut.setStartOffset(300);
		fadeOut.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {////
            	//llytYellow.setVisibility(View.GONE);
            	animationWhiteFadeIn();
            }
        });
        llytYellow.startAnimation(fadeOut);
	}
	private void animationWhiteFadeIn(){
		llytYellow.setAnimation(null);
		viewLoading.removeView(llytYellow);
    	LayoutInflater inflater = (LayoutInflater)Activity_Home.this.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);
    	LinearLayout llytWhite=(LinearLayout) inflater.inflate(R.layout.view_empty_layout, viewLoading, false);
    	llytWhite.setBackgroundResource(R.color.app_white);
    	Animation fadeIn = AnimationUtils.loadAnimation(Activity_Home.this, R.anim.fade_in);
    	viewLoading.addView(llytWhite);
    	fadeIn.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            	animationMOveNewzByteLogo();
            }
        });
    	fadeIn.setStartOffset(100);
    	
    	llytWhite.startAnimation(fadeIn);
	}
	
	private void animationMOveNewzByteLogo(){
		
		ImageView imgView = new ImageView(Activity_Home.this);
		int size = 200;
		imgView.setLayoutParams(new LayoutParams(size, size));
		imgView.setImageResource(R.drawable.newzbyte);
		viewLoading.addView(imgView);
		
		imgView.setY(Globals.getScreenSize(Activity_Home.this).y);
		imgView.setX(Globals.getScreenSize(Activity_Home.this).x/2 - size/2);
			
		imgView.animate().setDuration(300).setStartDelay(100)
			.translationY(Globals.getScreenSize(Activity_Home.this).y/2 - size/2).setListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animator arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animator arg0) { 
					//startAnimationLoading(1000);
				}
				
				@Override
				public void onAnimationCancel(Animator arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	}
	
private void updateCatTopNewsId(int catId, int newsId){
		DBHandler_Category dbH = new DBHandler_Category(this);
		dbH.updateCategoryTopNews(catId, newsId);
	}
	
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

//				} else {
//					Toast.makeText(
//							Activity_Home.this,
//							Globals.TEXT_NO_INTERNET_DETAIL_TOAST,
//							Toast.LENGTH_SHORT).show();
//					//listViewNews.onRefreshComplete();
//
//				/}

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
							} else {///
								Toast.makeText(
										Activity_Home.this,
										Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
										Toast.LENGTH_SHORT).show();
								//listViewNews.onRefreshComplete();

							/*}
							Globals.hideLoadingDialog(mDialog);
						}
					});

		
			Custom_AppController.getInstance().addToRequestQueue(
					jsonObjectRQST);
			
		}catch(Exception ex){

		}
	}
	
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
				///if(response.has("news"))
					///if(insertNewAndDeleteOldNews(response.getJSONArray("news"),catId,isPullToRefresh))
							///showNewsList(catId,callType);
						
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
				 
				ArrayList<Object_ListItem_MainNews> tempMainNewsList = parserObject.getParsedJsonMainNews(response.getJSONArray("news"),objConfig.getRootCatId());
				
				 
				if(callType.equals(Globals.CALL_TYPE_NEW))
				{
					currentNewsIndex += tempMainNewsList.size();
					for(int i=tempMainNewsList.size()-1;i>=0;i--)
					{
						if(isSelectedId(tempMainNewsList.get(i).getCatId()));
							newsCount++;
						if(isNotContainsId(tempMainNewsList.get(i).getId()))	
							listNewsItemServer.add(0, tempMainNewsList.get(i));			
					}
				}
				else if(callType.equals(Globals.CALL_TYPE_OLD))
				{
					for(int i=0;i<tempMainNewsList.size();i++)
					{
						if(isSelectedId(tempMainNewsList.get(i).getCatId()));
							newsCount++;
						if(isNotContainsId(tempMainNewsList.get(i).getId()))
							listNewsItemServer.add(tempMainNewsList.get(i));
					}
				}
				
//				 Log.i("Bytes", "ACTION_MOVE UP");
//						isSlideUp = true;
//						viewMoving = viewStatic;
//
//						int backUpId = currentNewsIndex;
//						viewStatic = createNewsView();
//						if(viewStatic == null){
//							viewStatic = viewMoving;
//							viewMoving = null;
//							isNoMoreNews = true;
//							isSlideInProgress = false;
//							currentNewsIndex = backUpId;
//							return false;
				
				
				if(newsCount == 0 && callType.equals(Globals.CALL_TYPE_OLD))
				{
					Toast.makeText(this, "You are done for the day!", Toast.LENGTH_SHORT).show();
				}
				else if(newsCount > 0 && callType.equals(Globals.CALL_TYPE_OLD))
				{ 
					isMovingViewCurrent = true;
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
					isMovingViewCurrent = false;
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

	
//	private void removeId(Integer catId){
//
//		int cnt = -1;
//
//		for(int i =0; i< arraySelectedCatIds.size();i++){
//			Integer intgr = arraySelectedCatIds.get(i);
//			if(intgr.compareTo(catId) == 0){ //equal
//				cnt = i;
//				break;
//			}
//		}
//		if(cnt > -1){
//			arraySelectedCatIds.remove(cnt);
//		}
//	}
//	private boolean isSelectedId(Integer catId){
//
//		for(Integer i : arraySelectedCatIds){
//			if(i.compareTo(catId) == 0){ //equal
//				return true;
//			}
//		}
//
//		return false;
//	}
 */