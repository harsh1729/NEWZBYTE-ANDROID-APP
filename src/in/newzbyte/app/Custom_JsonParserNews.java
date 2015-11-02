package in.newzbyte.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;



public class Custom_JsonParserNews {


	public Custom_JsonParserNews() {
	
	}
	public ArrayList<Object_ListItem_MainNews> getParsedJsonMainNews(JSONArray mainArray, int catId)
	{
		ArrayList<Object_ListItem_MainNews> listNews = new ArrayList<Object_ListItem_MainNews>();
		
		if(mainArray != null )
		{
			try 
			{
				if(mainArray != null)
				for(int i=0;i<mainArray.length();i++)
				{
					Object_ListItem_MainNews objMainNews = new Object_ListItem_MainNews();

					JSONObject jsonObjNews = mainArray.getJSONObject(i);
					

					int news_id = -1;
					
					if(jsonObjNews.has("id")){
						news_id = jsonObjNews.getInt("id");
						objMainNews.setId(news_id);
					}
					
					if(jsonObjNews.has("catid")){
						objMainNews.setCatId(jsonObjNews.getInt("catid"));
					}else{
						objMainNews.setCatId(catId);
					}
					
					if(jsonObjNews.has("heading")){
						objMainNews.setHeading(jsonObjNews.getString("heading").trim());
					}
					if(jsonObjNews.has("content")){
						objMainNews.setContent(jsonObjNews.getString("content").trim());
					}
					if(jsonObjNews.has("image")){
						objMainNews.setImagePath(jsonObjNews.getString("image").trim());
					}
					if(jsonObjNews.has("image_ratio")){
						objMainNews.setImageRatio(jsonObjNews.getDouble("image_ratio"));
					}
					if(jsonObjNews.has("image_align")){
						objMainNews.setImageAlign(jsonObjNews.getInt("image_align"));
					}
					if(jsonObjNews.has("datetime")){
						objMainNews.setDate(jsonObjNews.getString("datetime").trim());
					}
					if(jsonObjNews.has("imgtagline")){
						objMainNews.setImageTagline(jsonObjNews.getString("imgtagline").trim());
					}
					if(jsonObjNews.has("sharelink")){
						objMainNews.setShareLink(jsonObjNews.getString("sharelink").trim());
					}
					if(jsonObjNews.has("video")){
						objMainNews.setVideo(jsonObjNews.getString("video").trim());
					}
					if(jsonObjNews.has("categoryname")){
						objMainNews.setCatName(jsonObjNews.getString("categoryname").trim());
					}
					if(jsonObjNews.has("summary")){
						objMainNews.setSummary(jsonObjNews.getString("summary").trim());
					}
					if(jsonObjNews.has("sources")){
						objMainNews.setSource(jsonObjNews.getString("sources").trim());
					}
					if(jsonObjNews.has("reportername")){
						objMainNews.setAuthor(jsonObjNews.getString("reportername").trim());
					}
					
					if(jsonObjNews.has("linked_news") && news_id > 0){
					//JSONArray subNewsArray = jsonObjNews.getJSONArray("linked_news");
					/*
					ArrayList<Object_SubNewsItem> listSubNews = new ArrayList<Object_SubNewsItem>();
					
					for(int j=0;j<subNewsArray.length();j++)
					{
						Object_SubNewsItem objSubNews = new Object_SubNewsItem();

						JSONObject objSubNewsJSON = subNewsArray.getJSONObject(j);
						
						if(objSubNewsJSON.has("news_id")){
							objSubNews.setNewsId(objSubNewsJSON.getInt("news_id"));
						}
						objSubNews.setNewsParentId(news_id);
						
						if(objSubNewsJSON.has("heading")){
							objSubNews.setNewsHeading(objSubNewsJSON.getString("heading").trim());
						}
						if(objSubNewsJSON.has("content")){
							objSubNews.setNewsContent(objSubNewsJSON.getString("content").trim());
						}
						if(objSubNewsJSON.has("image")){
							objSubNews.setNewsImagePath(objSubNewsJSON.getString("image").trim());
						}
						if(objSubNewsJSON.has("tagline")){
							objSubNews.setNewsImageTagline(objSubNewsJSON.getString("tagline").trim());
						}
						if(objSubNewsJSON.has("video")){
							objSubNews.setNewsVideo(objSubNewsJSON.getString("video").trim());
						}
						
						listSubNews.add(objSubNews);
						
						}
					

					objMainNews.setListSubNews(listSubNews);
					*/
					}
					listNews.add(objMainNews);
				} 
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		}
		Log.i("DARSH", "COunt of NEws : "+ listNews.size());
		return listNews;
	}
	
	
	public ArrayList<Object_SubNewsItem> getParsedJsonSubNews(JSONArray mainArray,int parentNewsId)
	{
		ArrayList<Object_SubNewsItem> listNews = new ArrayList<Object_SubNewsItem>();
		if(mainArray != null )
			try 
			{
				if(mainArray != null)
				for(int i=0;i<mainArray.length();i++)
				{
					Object_SubNewsItem objSubNews = new Object_SubNewsItem();

					JSONObject jsonObjNews = mainArray.getJSONObject(i);
					

					int news_id = -1;
					
					if(jsonObjNews.has("id")){
						news_id = jsonObjNews.getInt("id");
						objSubNews.setNewsId(news_id);
					}
					objSubNews.setNewsParentId(parentNewsId);
									
					if(jsonObjNews.has("heading")){
						objSubNews.setNewsHeading(jsonObjNews.getString("heading").trim());
					}
					if(jsonObjNews.has("content")){
						objSubNews.setNewsContent(jsonObjNews.getString("content").trim());
					}
					if(jsonObjNews.has("image")){
						objSubNews.setNewsImagePath(jsonObjNews.getString("image").trim());
					}
					if(jsonObjNews.has("image_ratio")){
						objSubNews.setImageRatio(jsonObjNews.getDouble("image_ratio"));
					}
					if(jsonObjNews.has("image_align")){
						objSubNews.setImageAlign(jsonObjNews.getInt("image_align"));
					}
					
					if(jsonObjNews.has("imgtagline")){
						objSubNews.setNewsImageTagline(jsonObjNews.getString("imgtagline").trim());
					}
					
					if(jsonObjNews.has("video")){
						objSubNews.setNewsVideo(jsonObjNews.getString("video").trim());
					}
						 
					listNews.add(objSubNews);
				} 
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		
		return listNews;
	}
	
}
