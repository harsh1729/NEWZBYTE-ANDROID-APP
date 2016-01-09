package in.newzbyte.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_ChooseCat extends Activity {

	ArrayList<Object_Category> listCatItemServer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_cat);
		listCatItemServer = new ArrayList<Object_Category>();
		serverCallForCategories();
	}
	
	 public void goBack(View v){
		 this.finish();
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
			Object_AppConfig objAppConfig = new Object_AppConfig(Activity_ChooseCat.this);

			Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(Request.Method.POST,
					//objAppConfig.getVersionNoCategory()
					url, Custom_URLs_Params.getParams_GetCategories(0,objAppConfig.getVersionNoAppConfig(),Activity_ChooseCat.this),
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
							Activity_ChooseCat.this, "OK", null, false);


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
						showToast();
						
			}

		} catch (Exception ex) {
			Log.i("HARSH", "Error in parsin jSOn" + ex.getMessage());
		}

	}
    
    private void createDrawerCategories(){

    	TextView txt = (TextView)findViewById(R.id.txtIntroScreen3Msg);
		txt.setVisibility(View.GONE);
		LinearLayout llytCatContainer = (LinearLayout)findViewById(R.id.llytCatContainer);
		
		if(llytCatContainer.getChildCount() > 0){
			llytCatContainer.removeAllViews();
		}
		

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
				Log.d("HARSH", "catColor"+catColor);
				if(!catColor.isEmpty())
					row.setBackgroundColor(Color.parseColor(catColor));

				
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
			
			
			
			String catColor1 =listCatItemServer.get(0).getColor(); //dbCat.getCategoryColor(listCatItemServer.get(0).getId());
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
		ScrollView scrollViewCat = (ScrollView)findViewById(R.id.scrollViewCat);
		int heightImage = (int) ((scrollViewCat.getHeight() - 6* margin )/6.0 ) ;
		int widthImage = heightImage;
		///
		Log.d("DARSH","scrollViewCat.getHeight():"+scrollViewCat.getHeight());
		Log.d("DARSH","widthImage:"+widthImage);
		//int catColor = this.getResources().getColor(Globals.getCategoryColor(objCat.getId(), this));

		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		RelativeLayout item = (RelativeLayout) inflater.inflate(R.layout.item_category_image_home, row ,false);
		item.setTag(R.string.app_name, Integer.valueOf(objCat.getId()));

		

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
		}
    
    public void moveToNewsScreen(View v){
    	
    	DBHandler_CategorySelection dbH = new DBHandler_CategorySelection(this);		
		ArrayList<Integer> Ids = dbH.getAllCategories();
    	
		if(Ids.size() > 2){
		 Intent i = new Intent(this, Activity_Home.class);
        startActivity(i);
        
        this.finish();
		}else{
			showToast();
		}
	}
    private void showToast(){
    	Toast.makeText(this, "Please choose atleast three categories!", Toast.LENGTH_SHORT).show();
    }
}
