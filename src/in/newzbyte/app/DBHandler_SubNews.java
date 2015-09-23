package in.newzbyte.app;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler_SubNews extends SQLiteOpenHelper {
	
	//CREATE TABLE SubNewsTable (Id integer,ParentId integer,
	//Heading text,Content text,Image text,Video text,ImageTagline text)

	
	public   final static String TABLE_SUBNEWS = "SubNewsTable";
	private  final String KEY_NEWS_ID = "Id";
	private  final String KEY_NEWS_PARENT_ID = "ParentId";
	private  final String KEY_NEWS_HEADING = "Heading";
	private  final String KEY_NEWS_CONTENT = "Content";
	private  final String KEY_NEWS_IMAGE = "Image";
	private  final String KEY_NEWS_VIDEO = "Video";
	private  final String KEY_NEWS_TAGLINE = "ImageTagline";
	
	Context context;
	
	public DBHandler_SubNews(Context context) 
	{
		super(context,DBHandler_Main.DB_NAME,null, DBHandler_Main.DB_VERSION);
		this.context = context;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
	}
	
	
	
	 public ArrayList<Object_SubNewsItem> getAllSubNewsItem(int newsId ,String defaultHeading )
	 {
		 ArrayList<Object_SubNewsItem> list = new ArrayList<Object_SubNewsItem>();
		 String query = " SELECT * FROM "+TABLE_SUBNEWS+" WHERE " + KEY_NEWS_PARENT_ID +" = "+newsId;
		 SQLiteDatabase db = this.getReadableDatabase();
		 Cursor cur = db.rawQuery(query, null);
		 if(cur!=null)
		 {
			 if(cur.moveToFirst())
			 {
				 do
				 {					 
					 Object_SubNewsItem objNews = new Object_SubNewsItem();
					 
					 objNews.setNewsContent(cur.getString(cur.getColumnIndex(KEY_NEWS_CONTENT)));
					 
					 //if(cur.getString(cur.getColumnIndex(KEY_NEWS_HEADING)).equals(Globals.DEFAULT_NEWS_HEADING))
						 //objNews.setNewsHeading(defaultHeading);
					 //else
						 objNews.setNewsHeading(cur.getString(cur.getColumnIndex(KEY_NEWS_HEADING)));
						 
					 objNews.setNewsId(cur.getInt(cur.getColumnIndex(KEY_NEWS_ID)));
					 objNews.setNewsImagePath(cur.getString(cur.getColumnIndex(KEY_NEWS_IMAGE)));
					 objNews.setNewsImageTagline(cur.getString(cur.getColumnIndex(KEY_NEWS_TAGLINE)));
					 objNews.setNewsParentId(cur.getInt(cur.getColumnIndex(KEY_NEWS_PARENT_ID)));
					 objNews.setNewsVideo(cur.getString(cur.getColumnIndex(KEY_NEWS_VIDEO)));
					 
					 list.add(objNews);
				 }while(cur.moveToNext());
			 }
		 }
		 db.close();
		 return list;
	 }
	 
	 public void insertSubNewsItemList(ArrayList<Object_SubNewsItem> list) {

			SQLiteDatabase db = this.getWritableDatabase();
			insertSubNewsItemList(list, db);
			db.close();
			
	 }
	 public void insertSubNewsItemList(ArrayList<Object_SubNewsItem> list ,SQLiteDatabase db )
	 {
		 ContentValues values = new ContentValues();
		 
		 for(Object_SubNewsItem obMain : list)
		 {
			 try{
			 values.put(KEY_NEWS_CONTENT, obMain.getNewsContent());
			 values.put(KEY_NEWS_HEADING, obMain.getNewsHeading());
			 values.put(KEY_NEWS_IMAGE, obMain.getNewsImagePath());
			 values.put(KEY_NEWS_VIDEO, obMain.getNewsVideo());
			 values.put(KEY_NEWS_TAGLINE, obMain.getNewsImageTagline());
			 values.put(KEY_NEWS_ID, obMain.getNewsId());
			 values.put(KEY_NEWS_PARENT_ID, obMain.getNewsParentId());
			 
			 db.insert(TABLE_SUBNEWS, null, values);		
			 }catch(Exception ex){
				 Log.i("HARSH", "Exception in inserting child news");
			 }
		}
		 
	 }
	 
	 
	 public void clearSubNewsTable(String parentSelectQuery ,SQLiteDatabase db) {
		 
			String query = "DELETE FROM " + TABLE_SUBNEWS + " WHERE "+KEY_NEWS_PARENT_ID +" IN ("+parentSelectQuery+")";
			db.execSQL(query);
		}
}
