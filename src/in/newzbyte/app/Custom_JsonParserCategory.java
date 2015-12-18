package in.newzbyte.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;


public class Custom_JsonParserCategory {
	
	private ArrayList<Object_Category> listCategories;
	Context context;
	
	public Custom_JsonParserCategory(Context context) {
		this.context = context;
	}
	
	public ArrayList<Object_Category> getCategoriesFromJson(JSONArray Cat_Object_Array)
	{
		Log.i("HARSH","In GEtParsedJson");
		
			listCategories = new ArrayList<Object_Category>();
			try
			{
				for(int i=0; i<Cat_Object_Array.length(); i++)
				{
					Object_Category ob = new Object_Category();
					JSONObject Single_Cat = Cat_Object_Array.getJSONObject(i);
					ob.setId(Single_Cat.getInt("id"));
					ob.setName(Single_Cat.getString("name").trim());
					if(Single_Cat.has("image"))
						ob.setImageName(Single_Cat.getString("image").trim());
					if(Single_Cat.has("selected_image"))
						ob.setSelectedImageName(Single_Cat.getString("selected_image").trim());
					if(Single_Cat.has("langid"))
						ob.setLangId(Single_Cat.getInt("langid"));
					if(Single_Cat.has("topnewsid"))
						ob.setTopNewsId(Single_Cat.getInt("topnewsid"));
					
					Log.i("HARSH","name"+ob.getName());
					if(Single_Cat.has("parent_id"))
					ob.setParentId(Single_Cat.getInt("parent_id"));
					listCategories.add(ob);
				}
				
			}
			catch(Exception e)
			{
				Log.i("HARSH","error getPArsed JSON -- "+e.getMessage() +"\n"+e.getStackTrace());
				e.printStackTrace();
			}

		return listCategories;
	}
	
	/*
	public static byte[] getBitmapFromURL(String link) 
	{
		  
		Log.i("HARSH","getBitmapFromURL called: "+link);
		   try 
		   {
			   if(link.trim().equalsIgnoreCase("")){
				   return null;
			   }
			   DefaultHttpClient mHttpClient = new DefaultHttpClient();
			   HttpGet mHttpGet = new HttpGet(link);
			   Log.i("HARSH","Downloading image from : "+link);
			   HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);
			   if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
			   {
				   HttpEntity entity = mHttpResponse.getEntity();
			       if ( entity != null) 
			       {
			    	   Log.i("HARSH","Returning image: "+link);
			    	   byte[] imageData = EntityUtils.toByteArray(entity);	// returns blob data , pass it to query
			    	   
			    	   if(imageData != null){
			    		   Log.i("HARSH","imageData length "+imageData.length);
			    		   return imageData;
			    	   }
			    	   
			    	   return null;
			       }
			   }
		   }
		   catch (Exception e) 
		   {
			   Log.i("HARSH","Exception in fetcing"+link +" --"+e.getMessage() +"\n"+e.getStackTrace());
		   }
		return null;
	}
*/

}
