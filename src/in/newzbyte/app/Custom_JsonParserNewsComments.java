package in.newzbyte.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;


public class Custom_JsonParserNewsComments {
	
	private ArrayList<Object_NewsComment> listComments;
	
	public Custom_JsonParserNewsComments() {
	}
	
	public ArrayList<Object_NewsComment> getCommentsFromJson(JSONArray jsonArray)
	{
		Log.i("HARSH","In GEtParsedJson");
		
		listComments = new ArrayList<Object_NewsComment>();
			try
			{
				for(int i=0; i<jsonArray.length(); i++)
				{
					Object_NewsComment ob = new Object_NewsComment();
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					
					if(jsonObj.has("name"))
						ob.setName(jsonObj.getString("name").trim());
					if(jsonObj.has("comment"))
						ob.setComment(jsonObj.getString("comment").trim());
					if(jsonObj.has("date"))
						ob.setComment(jsonObj.getString("date").trim());
					
					listComments.add(ob);
				}
				
			}
			catch(Exception e)
			{
				Log.i("HARSH","error getPArsed JSON listComments -- "+e.getMessage() +"\n"+e.getStackTrace());
				e.printStackTrace();
			}

		return listComments;
	}
	
	
}
