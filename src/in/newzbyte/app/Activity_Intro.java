package in.newzbyte.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class Activity_Intro extends FragmentActivity {

	/**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    boolean doubleBackToExitPressedOnce = false;
    boolean isBootomBarHidden = false;
    ArrayList<Object_Category> listCatItemServer = new ArrayList<Object_Category>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		if(!checkIfNeedsIntro()){
			moveToNewsScreen();
            return;
		}
		setContentView(R.layout.activity_intro);
		
		DBHandler_Main db = new DBHandler_Main(this);
		db.createDataBase();
		// Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new Custom_ZoomOutPageTransform());
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	Object_AppConfig obj = new Object_AppConfig(Activity_Intro.this);
            	RadioGroup group = (RadioGroup)findViewById(R.id.radiogroup);
            	 switch (position){
                 case 0:
                     radioGroup.check(R.id.radioButton);
                     
             		group.setVisibility(View.VISIBLE);
                     break;
                 case 1:
                     radioGroup.check(R.id.radioButton2);
                     
                     if(obj.getLangId() != 0)
                    	 onClickLang(obj.getLangId(),obj);
                     
             		group.setVisibility(View.VISIBLE);
                     break;
                 case 2:
                     radioGroup.check(R.id.radioButton3);
                     //if(listCatItemServer == null || listCatItemServer.size() == 0)
                     //{
                     if(obj.getLangId() != 0)
                    	 serverCallForCategories();
                     else{
                    	 Toast.makeText(Activity_Intro.this, "Please first select language !", Toast.LENGTH_SHORT).show();
                    	 mPager.setCurrentItem(1);
                     }
                     
                     setBootomBarStatus();
                     //}else{
                    	 //createDrawerCategories();
                     //} user can go back and change language.
                     break;
             }
            }
        });
	}
	
	private void moveToNewsScreen(){
		 Intent i = new Intent(this, Activity_Home.class);
         startActivity(i);
         
         this.finish();
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
	            }
	   

	            return false;
	}
	
	 /**
     * A simple pager adapter that represents 5 {@link Custom_IntroSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return Custom_IntroSlidePageFragment.create(position);
        }

        
       /* @Override
        public Object instantiateItem(ViewGroup collection, int position) {
        	
        	LayoutInflater inflater = (LayoutInflater)Activity_Intro.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	
            View v = getViewForPage(inflater, collection, position);
            ((ViewPager) collection).addView(v,0);
            return v;
        }
        */

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
    
    
    private void serverCallForCategories(){
    	
    	try {
			Log.i("HARSH", "FirstCall");			

			String url = Custom_URLs_Params.getURL_GetCategories();
			Log.i("HARSH", "Cat URL -- "+url);

			TextView txt = (TextView)findViewById(R.id.txtIntroScreen3Msg);
			txt.setVisibility(View.VISIBLE);
			LinearLayout llytCatContainer = (LinearLayout)findViewById(R.id.llytCatContainer);
			///ImageView btnCatAll = (ImageView)findViewById(R.id.btnCatAll);

			if(llytCatContainer.getChildCount() > 0){
				llytCatContainer.removeAllViews();
			}
			//CustomRequest jsObjRequest = new CustomRequest(Method.POST, url, params, this.createRequestSuccessListener(), this.createRequestErrorListener());
			Object_AppConfig objAppConfig = new Object_AppConfig(Activity_Intro.this);

			Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(Request.Method.POST,
					//objAppConfig.getVersionNoCategory()
					url, Custom_URLs_Params.getParams_GetCategories(0,objAppConfig.getVersionNoAppConfig(),Activity_Intro.this),
					new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {

					parseJson(response);
					

				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError err) {
					Log.i("DARSH", "ERROR VolleyError");

					Globals.showAlertDialogOneButton(
							Globals.TEXT_CONNECTION_ERROR_HEADING,
							Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
							Activity_Intro.this, "OK", null, false);


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
    
    private void parseJson(JSONObject response) {

		if (response == null){
			
			return;
		}
		Log.i("DARSH", "RESPONCE parseAppConfigJson is : "+response.toString());
		try {

			// Set Categories
			if(response.has("categories")){

						JSONArray Cat_Object_Array = response.getJSONArray("categories");
						Custom_JsonParserCategory parserObject = new Custom_JsonParserCategory(this);
						listCatItemServer = parserObject.getCategoriesFromJson(Cat_Object_Array);
						createDrawerCategories();
			}

		} catch (Exception ex) {
			Log.i("HARSH", "Error in parsin jSOn" + ex.getMessage());
		}

	}
    private void createDrawerCategories(){

    	TextView txt = (TextView)findViewById(R.id.txtIntroScreen3Msg);
		txt.setVisibility(View.GONE);
		
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
		
		for(int i = 0 ; i< listCatItemServer.size() ; i++){

			if(i%3 == 0){
				firstInRow = true;
			}else{
				firstInRow = false;
			}
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
				
				row.addView(getCatImageView(listCatItemServer.get(i),row,firstInRow,i,contains)) ;

				if(firstInRow)
					llytCatContainer.addView(row);	
			}

		}
	}
    
    private RelativeLayout getCatImageView(Object_Category objCat , LinearLayout row,boolean firstInRow,int position,boolean contains){

		int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
		int widthImage =(int) ((Globals.getScreenSize(this).x - 6* margin)/3.0) ;
		
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		RelativeLayout item = (RelativeLayout) inflater.inflate(R.layout.item_category_image_home, row ,false);
		item.setTag(R.string.app_name, Integer.valueOf(objCat.getId()));


		ImageView imgView =(ImageView) item.findViewById(R.id.imgViewCat);
		imgView.setTag(R.string.app_name, position);
		int heightImage = widthImage;

		
		RelativeLayout.LayoutParams params =(RelativeLayout.LayoutParams )imgView.getLayoutParams();
		params.height = heightImage;
		params.width = widthImage ;
		
		imgView.setLayoutParams(params);
		
		
		LinearLayout.LayoutParams paramsParent =(LinearLayout.LayoutParams )item.getLayoutParams();
		
		if(!firstInRow)
			paramsParent.leftMargin = margin;
		item.setLayoutParams(paramsParent);

		
		if(contains){

			Globals.loadImageIntoImageView(imgView, objCat.getSelectedImageName(), this, R.drawable.cat_loading, R.drawable.cat_loading);//(imgView, objCat.getImageName(), 0,0, this);//heightImage,widthImage
			Globals.preloadImage(getApplicationContext(), objCat.getImageName()) ;
		}else{
			Globals.loadImageIntoImageView(imgView, objCat.getImageName(), this, R.drawable.cat_loading, R.drawable.cat_loading);//(imgView, objCat.getImageName(), 0,0, this);//heightImage,widthImage
			Globals.preloadImage(getApplicationContext(), objCat.getSelectedImageName()) ;
		}
		
		
		TextView txtCategory = (TextView)item.findViewById(R.id.txtCategory);

		Typeface tfCat = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_CAT_FONT);
		txtCategory.setTypeface(tfCat);
		txtCategory.setText(objCat.getName());
		txtCategory.setMaxWidth(widthImage-margin);

		return item;

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
    
    public void onClickEnglish(View v){
    	Object_AppConfig obj = new Object_AppConfig(this);
    	obj.setLangId(Globals.LANG_ENG);
    	onClickLang(Globals.LANG_ENG,obj);
    }
    
    public void onClickHindi(View v){
    	Object_AppConfig obj = new Object_AppConfig(this);
    	obj.setLangId(Globals.LANG_HINDI);
    	onClickLang(Globals.LANG_HINDI,obj);
    }
    
    private void onClickLang(int langId,Object_AppConfig obj ){
    	
    	try{
    	
    	
    	ImageView imageViewEnglish = (ImageView)findViewById(R.id.imageViewEnglish);
    	ImageView imageViewHindi= (ImageView)findViewById(R.id.imageViewHindi);
    	
    	if(obj.getLangId() == Globals.LANG_ENG){
    		imageViewEnglish.setImageResource(R.drawable.english);
    	}else{
    		imageViewEnglish.setImageResource(R.drawable.english_unselected);
    	}
    	
    	if(obj.getLangId() == Globals.LANG_HINDI){
    		imageViewHindi.setImageResource(R.drawable.hindi);
    	}else{
    		imageViewHindi.setImageResource(R.drawable.hindi_unselected);
    	}
    	}catch(Exception ex){
    		Log.i("HARSH", "Exception is language button");
    	}
    }
    
    public void onClickCategoryItem(View v){

		Integer selectedCatId = ((Integer)v.getTag(R.string.app_name)).intValue();
		boolean contains = Globals.categoryClick(selectedCatId, this);
		
		View cView = v.findViewById(R.id.imgViewCat);
		if(cView!= null && cView.getClass() == ImageView.class){
			ImageView imageView = (ImageView)cView;
			int index = ((Integer)imageView.getTag(R.string.app_name)).intValue();
			if(listCatItemServer!= null && listCatItemServer.size() > index){
				Object_Category obj = listCatItemServer.get(index);
				if(contains){
					Globals.loadImageIntoImageView(imageView, obj.getImageName(), this,R.drawable.cat_loading,R.drawable.cat_loading);
				}else{
					Globals.loadImageIntoImageView(imageView, obj.getSelectedImageName(), this,R.drawable.cat_loading_selected,R.drawable.cat_loading_selected);
				}
			}
			
		}
		
		///Thread t = new Thread(new Runnable() {
			
			///@Override
			///public void run() {
				setBootomBarStatus();
				
				

			///}
		///});
		///t.start();
		
		
			
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
}
