package in.newzbyte.app;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class Custom_URLs_Params {
	
	static String getURL_Disclaimer(){
		//http://www.newstest2.tk/newsci/client_requests/news/mob_get_cat_news
		//return Globals.DEFAULT_APP_SERVER_PATH+"getAppConfig.php?CatVersion="+catVersionId+"&AppConfigVersion=0"+appVersionId;
		
		Log.i("HARSH", Globals.DEFAULT_APP_SERVER_PATH+"disclaimer/get");
		return Globals.DEFAULT_APP_SERVER_PATH+"disclaimer/get";//?CatVersion="+catVersionId+"&AppConfigVersion=0"+appVersionId;
	}

	static String getURL_NewsFirstCall(){
		//http://www.newstest2.tk/newsci/client_requests/news/mob_get_cat_news
		//return Globals.DEFAULT_APP_SERVER_PATH+"getAppConfig.php?CatVersion="+catVersionId+"&AppConfigVersion=0"+appVersionId;
		
		Log.i("HARSH", Globals.DEFAULT_APP_SERVER_PATH+"news/mob_get_cat_news");
		return Globals.DEFAULT_APP_SERVER_PATH+"news/mob_get_cat_news";//?CatVersion="+catVersionId+"&AppConfigVersion=0"+appVersionId;
	}
	
	static Map<String, String> getParams_CatNewsFirstCall(int catVersionId,int appVersionId,Context context){
		HashMap<String, String> mParams = new HashMap<String, String>();
		Object_AppConfig obj = new Object_AppConfig(context);
			        mParams.put("clientid", Globals.CLIENT_ID+"");
			        //mParams.put("catversion", catVersionId+"");
			        //mParams.put("appconfigversion", appVersionId+"");
			        mParams.put("limit", Globals.FINAL_NEWS_LIMIT_FIRST_CALL+"");
			        mParams.put("langid", obj.getLangId()+"");
			        mParams.put("catid", Globals.getCatIdByComma(context));
			        //mParams.put("isneedtopnews", "true");
			        Log.i("DARSH", "getParams_CatNewsFirstCall --->" + mParams);
			        return mParams;
	}
	
	static String getURL_GetCategories(){
		//http://www.newstest2.tk/newsci/client_requests/news/mob_get_cat_news
		//return Globals.DEFAULT_APP_SERVER_PATH+"getAppConfig.php?CatVersion="+catVersionId+"&AppConfigVersion=0"+appVersionId;
		
		Log.i("HARSH", Globals.DEFAULT_APP_SERVER_PATH+"news/mob_get_category");
		return Globals.DEFAULT_APP_SERVER_PATH+"news/mob_get_category";//?CatVersion="+catVersionId+"&AppConfigVersion=0"+appVersionId;
	}
	
	static Map<String, String> getParams_GetCategories(int catVersionId,int appVersionId,Context context){
		HashMap<String, String> mParams = new HashMap<String, String>();
		Object_AppConfig obj = new Object_AppConfig(context);
			        mParams.put("clientid", Globals.CLIENT_ID+"");
			        mParams.put("langid", obj.getLangId()+"");
			        Log.i("DARSH", "getParams_CatNewsFirstCall --->" + mParams);
			        return mParams;
	}
	static String getURL_NewsByCategory(){ //int catId , int lastNewsId, String callType
		//http://www.newstest2.tk/newsci/client_requests/news/mob_get_news_by_category
		//return  Globals.Globals.DEFAULT_APP_SERVER_PATH+"getnewsbycategory.php?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType;
		
		Log.i("HARSH", Globals.DEFAULT_APP_SERVER_PATH+"news/mob_get_news_by_category");//?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType);
		return  Globals.DEFAULT_APP_SERVER_PATH+"news/mob_get_news_by_category";//?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType;
		
		//Calltype : fresh,new,old
	}

	static String getURL_ContactUs(){ //int catId , int lastNewsId, String callType
		//http://www.newstest2.tk/newsci/client_requests/news/mob_get_news_by_category
		//return  Globals.DEFAULT_APP_SERVER_PATH+"getnewsbycategory.php?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType;
		
		Log.i("HARSH", Globals.DEFAULT_APP_SERVER_PATH+"contactus/mob_contact_us");//?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType);
		return  Globals.DEFAULT_APP_SERVER_PATH+"contactus/mob_contact_us";//?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType;
		
		//Calltype : fresh,new,old
	}
	static Map<String, String> getParams_NewsByCategory(int catId,String callType,int lastNewsId,int limit){
		
		
		HashMap<String, String> mParams = new HashMap<String, String>();
			        mParams.put("catid", catId+"");
			        mParams.put("calltype", callType);
			        mParams.put("lastnewsid", lastNewsId+"");
			        mParams.put("limit", limit+"");
			        mParams.put("isneedtopnews", "true");
			        mParams.put("clientid", Globals.CLIENT_ID+"");
			        Log.i("DARSH", "getParams_NewsByCategory --->" + mParams);
			        
			        return mParams;
	}
static Map<String, String> getParams_NewsByCategoryDateWise(int catId,String callType,int lastNewsId,int limit,String startDate,String endDate ){
		
		
		HashMap<String, String> mParams = new HashMap<String, String>();
			        mParams.put("catid", catId+"");
			        mParams.put("calltype", callType);
			        mParams.put("lastnewsid", lastNewsId+"");
			        mParams.put("limit", limit+"");
			        mParams.put("startdate", startDate+"");
			        mParams.put("enddate", endDate+"");
			        mParams.put("isneedtopnews", "false");
			        mParams.put("clientid", Globals.CLIENT_ID+"");
			        Log.i("DARSH", "getParams_NewsByCategory --->" + mParams);
			        
			        return mParams;
	}
	
static Map<String, String> getParams_ContactUs(String device_uid,String contactDetail, String message , String name){
	
	
	HashMap<String, String> mParams = new HashMap<String, String>();
		        mParams.put("clientid", Globals.CLIENT_ID+"");
		        mParams.put("device_uid", device_uid);
		        mParams.put("contact_detail", contactDetail);
		        mParams.put("message", message);
		        mParams.put("name", name);
		        
		        Log.i("DARSH", "getParams_NewsByContactUs --->" + mParams);
		        
		        return mParams;
}
	static String getURL_NewsDetail(){ //int catId , int lastNewsId, String callType
		//http://www.newstest2.tk/newsci/client_requests/news/mob_get_news_by_category
		//return  Globals.DEFAULT_APP_SERVER_PATH+"getnewsbycategory.php?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType;
		
		Log.i("HARSH", Globals.DEFAULT_APP_SERVER_PATH+"news/mob_get_news_detail");//?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType);
		return  Globals.DEFAULT_APP_SERVER_PATH+"news/mob_get_news_detail";//?CatId="+catId+ "&lastNewsId=" + lastNewsId + "&callType="+ callType;
		
		//Calltype : fresh,new,old
	}

	static Map<String, String> getParams_NewsDetail(int newsId){	
		HashMap<String, String> mParams = new HashMap<String, String>();
			        mParams.put("newsid", newsId+"");			        
			        Log.i("DARSH", "getParams_NewsDetail --->" + mParams);
			        
			        return mParams;
	}
	static String getURL_PushNotification(){
		//Globals.DEFAULT_APP_SERVER_PATH
		return  Globals.DEFAULT_APP_SERVER_PATH+"gcm/mob_gcm_register";
	}
	
	static Map<String, String> getParams_PushNotification(String gcmId,Context context){	
		Object_AppConfig obj = new Object_AppConfig(context);
		
		HashMap<String, String> mParams = new HashMap<String, String>();
			        mParams.put("gcm_id", gcmId);
			        mParams.put("app_version", Globals.getAppVersion(context)+"");
			        mParams.put("client_id", Globals.CLIENT_ID+"");
			        mParams.put("device_id", Globals.getDeviceIMEI(context));			        
			        mParams.put("name", obj.getUserName());
			        mParams.put("contact", obj.getUserContact());
			        Log.i("DARSH", "getParams_PushNotification --->" + mParams);
			        
			        return mParams;
	}
}
