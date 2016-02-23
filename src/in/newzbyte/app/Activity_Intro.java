package in.newzbyte.app;

import com.google.android.gms.internal.db;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Activity_Intro extends Activity {

	/**
     * The number of pages (wizard steps) to show in this demo.
     */
    //private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    //private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
	private ImageView img;
    //private PagerAdapter mPagerAdapter;
    boolean doubleBackToExitPressedOnce = false;
    boolean isBootomBarHidden = false;
    AnimationDrawable bAmin;
    private View viewLoading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		DBHandler_Main db = new DBHandler_Main(this);
		db.createDataBase();
		if(!checkIfNeedsIntro()){
			moveToNewsScreen();
            return;
		}
		setContentView(R.layout.activity_intro);
		
		RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.rlytmain);
		LayoutInflater inflater = (LayoutInflater)this.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);
		viewLoading =(RelativeLayout) inflater.inflate(R.layout.view_loading, mainLayout,false);
		
		int y = Globals.getScreenSize(this).y;
		
		ImageView imgLoadingFooter =(ImageView) viewLoading.findViewById(R.id.imgLoadingFooter);
		ImageView imgLoadingNewzByte =(ImageView) viewLoading.findViewById(R.id.imgLoadingNewzByte);
		
		
		imgLoadingFooter.getLayoutParams().height = (int) (y * 0.4) ;
		imgLoadingFooter.getLayoutParams().width = (int) (y * 0.4) ;
		
		imgLoadingNewzByte.getLayoutParams().height = (int) (y * 0.25) ;
		
		mainLayout.addView(viewLoading);
		
		viewLoading.setVisibility(View.VISIBLE);
		
		new Handler().postDelayed(new Runnable()
		{
		   @Override
		   public void run()
		   {
			   // moveToChooseLang(); use this as future enhancement
			   moveToChooseCategory();
			   
		   }
		}, 2000);
		
		/*
		
		img = (ImageView)findViewById(R.id.imgAnimIntro);
		img.setBackgroundResource(R.drawable.loading);
		
		try{
			bAmin = (AnimationDrawable) img.getBackground();
			bAmin.setOneShot(true);
			bAmin.start();
		}catch(Exception ex){

		}
		new Handler().postDelayed(new Runnable()
		{
		   @Override
		   public void run()
		   {
			   // moveToChooseLang(); use this as future enhancement
			   moveToChooseCategory();
			   
		   }
		}, getTotalDuration(bAmin));
		*/
		
	}
	
	public int getTotalDuration(AnimationDrawable bAmin) {

        int iDuration = 0;

        for (int i = 0; i < bAmin.getNumberOfFrames(); i++) {
            iDuration += bAmin.getDuration(i);
        }

        iDuration += 500; // 
        return iDuration;
    }
	
	private void moveToNewsScreen(){
		 Intent i = new Intent(this, Activity_Home.class);
         startActivity(i);
         
         this.finish();
	}
	
	private void moveToChooseCategory(){
		
		Intent i = new Intent(this, Activity_ChooseCat.class);
    	startActivity(i);
    	this.finish();
	}
	
	private void moveToChooseLang(){
		
		Intent i = new Intent(this, Activity_ChooseLang.class);
        startActivity(i);
        this.finish();
	}
	
	
	public void onClickIntroFinish(View v){
		moveToNewsScreen();
	}
	private boolean checkIfNeedsIntro(){
	            //  Create a new boolean and preference and set it to true         
	            Object_AppConfig obj = new Object_AppConfig(this);


	            //  If the activity has never started before...
	            if ((obj.getLangId() == 0)) {
	                //  Launch app needs intro

	            	return true;
	            }else{
	            	try{
	            		DBHandler_CategorySelection dbH = new DBHandler_CategorySelection(this);
	            		if(dbH.getAllCategories().size() == 0)
	            			return true;
	            	}catch(Exception ex){
	            		
	            	}
	            }
	            return false;
	}
	
	 /**
     * A simple pager adapter that represents 5 {@link Custom_IntroSlidePageFragment} objects, in
     * sequence.
     */
   
  
    /*
     * 


 private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return Custom_IntroSlidePageFragment.create(position);
        }

        
      

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
        	
        	if(view.getClass() == View.class)
        		collection.removeView((View) view);
            
            super.destroyItem(collection, position, view);
        }
        
        @Override
        public int getCount() {
            return NUM_PAGES;
        }
        
        private View getViewForPage(LayoutInflater inflater, ViewGroup container,int pageNo){
        	
        	View view = null;
        	
        	switch (pageNo) {
    		case 0:
    			view = (View) inflater
                .inflate(R.layout.view_intro_first, container, false);
    			break;
    		case 1:
    			view = (View) inflater
                .inflate(R.layout.view_intro_second, container, false);
    			break;
    		case 2:
    			view = (View) inflater
                .inflate(R.layout.view_intro_third, container, false);
    			break;

    		default:
    			view = (View) inflater
                .inflate(R.layout.view_intro_first, container, false);
    			break;
    		}
        	
        	return view;
        }

    }
    

private void hideBottomView(){
		
		
		if(!isBootomBarHidden){
			
			LinearLayout layout = (LinearLayout)findViewById(R.id.llytIntroScreenThird2);
			
			layout.animate().setDuration(10).translationY(layout.getHeight());
			isBootomBarHidden = true;
			
			RadioGroup group = (RadioGroup)findViewById(R.id.radiogroup);
			group.setVisibility(View.VISIBLE);
		}
	}
	private void showBottomView(){
		if(isBootomBarHidden){
			LinearLayout layout = (LinearLayout)findViewById(R.id.llytIntroScreenThird2);
			
			layout.animate().setDuration(300).translationY(0);
			isBootomBarHidden = false;
			
		}
		RadioGroup group = (RadioGroup)findViewById(R.id.radiogroup);
		group.setVisibility(View.GONE);
	}
    private void setBootomBarStatus(){
    	DBHandler_CategorySelection dbh = new  DBHandler_CategorySelection(Activity_Intro.this);
		int count = dbh.getAllCategories().size();
		
    	TextView txtView = (TextView)findViewById(R.id.txtIntroScreen3Info);
    	switch (count) {
    	case 0:
    		txtView.setText("");
    		hideBottomView();
    		txtView.setClickable(false);
			break;
		case 1:
			txtView.setText("2 more to go!");
			txtView.setClickable(false);
			showBottomView();
			break;
		case 2:
			txtView.setText("Just one more.");
			txtView.setClickable(false);
			showBottomView();
			break;

		default:
			break;
		}
    	
    	if(count > 2){
    		txtView.setText("Bingo! Click here ...");
    		txtView.setClickable(true);
    		showBottomView();
    	}
    }
    
    */
}
