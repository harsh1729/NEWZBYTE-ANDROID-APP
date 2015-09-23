package in.newzbyte.app;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler_Category extends SQLiteOpenHelper {

	public static final String TABLE_CATEGORY = "Category";
	public static final String KEY_ID = "Id";
	public static final String KEY_NAME = "CatName";
	public static final String KEY_IMAGE = "CatImage";
	public static final String KEY_PARENT_ID = "ParentId";
	public static final String KEY_VERSION = "CatVersion";
	public static final String KEY_TOP_NEWS_ID =  "TopNewsId";

	public DBHandler_Category(Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public String getCategoryName(int catId) {

		String selectQuery = "select " + KEY_NAME + " from " + TABLE_CATEGORY
				+ " where " + KEY_ID + " = " + catId;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery(selectQuery, null);
		String catName = "";
		if (cur != null) {
			if (cur.moveToFirst()) {
				catName = cur.getString(cur.getColumnIndex(KEY_NAME));
			}
		}

		db.close();
		return catName;
	}

	public ArrayList<Object_Category> getCategories(Context con) {

		Log.i("HARSH", "get categories called!");
		String selectQuery = "select * from " + TABLE_CATEGORY + " where "
				+ KEY_PARENT_ID + " = 0";

		SQLiteDatabase db = this.getReadableDatabase();

		ArrayList<Object_Category> Cat_group = new ArrayList<Object_Category>();

		Cursor cur = db.rawQuery(selectQuery, null);
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					System.out.println(cur.getString(cur
							.getColumnIndex(KEY_NAME)));
					Object_Category catParent = new Object_Category();
					catParent.setName(cur.getString(cur
							.getColumnIndex(KEY_NAME)));
					catParent.setId(cur.getInt(cur.getColumnIndex(KEY_ID)));
					catParent.setParentId(cur.getInt(cur.getColumnIndex(KEY_PARENT_ID)));
					catParent.setTopNewsId(cur.getInt(cur.getColumnIndex(KEY_TOP_NEWS_ID)));
					catParent.setImageName(cur
							.getString(cur.getColumnIndex(KEY_IMAGE)));

					/***************************************************************/

					String childQuery = "select * from " + TABLE_CATEGORY
							+ " where " + KEY_PARENT_ID + " = "
							+ catParent.getId();
					Cursor childCur = db.rawQuery(childQuery, null);
					boolean tempFlag = true;
					if (childCur != null) {
						if (childCur.moveToFirst()) {
							catParent
									.setListChildCategory(new ArrayList<Object_Category>());
							do {
								if (tempFlag) {
									Object_Category catChildFirst = new Object_Category();
									catChildFirst.setName(cur.getString(cur
											.getColumnIndex(KEY_NAME)));
									catChildFirst.setImageName(cur
											.getString(cur.getColumnIndex(KEY_IMAGE)));
									catChildFirst.setId(cur.getInt(cur
											.getColumnIndex(KEY_ID)));
									catChildFirst.setTopNewsId(cur.getInt(cur.getColumnIndex(KEY_TOP_NEWS_ID)));
									catParent.getListChildCategory().add(
											catChildFirst);
									tempFlag = false;
								}

								Object_Category catChild = new Object_Category();
								catChild.setName(childCur.getString(childCur
										.getColumnIndex(KEY_NAME)));
								catChild.setImageName(childCur
										.getString(childCur.getColumnIndex(KEY_IMAGE)));
								catChild.setId(childCur.getInt(childCur
										.getColumnIndex(KEY_ID)));
								catChild.setTopNewsId(cur.getInt(cur.getColumnIndex(KEY_TOP_NEWS_ID)));
								catChild.setParentId(childCur.getInt(childCur
										.getColumnIndex(KEY_PARENT_ID)));
								catParent.getListChildCategory().add(catChild);

								System.out.println(childCur.getString(childCur
										.getColumnIndex(KEY_NAME)));
							} while (childCur.moveToNext());
						}
					}

					/***************************************************************/

					Cat_group.add(catParent);

				} while (cur.moveToNext());
			}
		}

		db.close();
		setRootNewsOnTop(con, Cat_group);
		
		return Cat_group;

	}

	public ArrayList<Object_Category> getAllCategories() {//Context con

		Log.i("HARSH", " getAllCategories called!");
		String selectQuery = "select * from " + TABLE_CATEGORY ;

		SQLiteDatabase db = this.getReadableDatabase();

		ArrayList<Object_Category> Cat_group = new ArrayList<Object_Category>();

		Cursor cur = db.rawQuery(selectQuery, null);
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					System.out.println(cur.getString(cur
							.getColumnIndex(KEY_NAME)));
					Object_Category catParent = new Object_Category();
					catParent.setName(cur.getString(cur
							.getColumnIndex(KEY_NAME)));
					catParent.setId(cur.getInt(cur.getColumnIndex(KEY_ID)));
					catParent.setParentId(cur.getInt(cur.getColumnIndex(KEY_PARENT_ID)));
					catParent.setTopNewsId(cur.getInt(cur.getColumnIndex(KEY_TOP_NEWS_ID)));
					catParent.setImageName(cur
							.getString(cur.getColumnIndex(KEY_IMAGE)));

					Cat_group.add(catParent);

				} while (cur.moveToNext());
			}
		}

		db.close();
		
		//setRootNewsOnTop(con, Cat_group);
		
		return Cat_group;

	}
	
	private void setRootNewsOnTop(Context con ,ArrayList<Object_Category> Cat_group){
		for(int i = 0; i<Cat_group.size();i++){
			Object_Category catObj = Cat_group.get(i);
			if(catObj != null){
				Object_AppConfig objConfig = new Object_AppConfig(con);
				if(catObj.getId() == objConfig.getRootCatId()){
					if(i != 0){
						Cat_group.remove(i);
						Cat_group.add(0, catObj);
					}
					break;
				}
			}
		}
	}

	private void clearCategoryTable(SQLiteDatabase db) {
		String deleteall = "delete from " + TABLE_CATEGORY;
		db.execSQL(deleteall);
	}

	public void setCategories(ArrayList<Object_Category> list) {

		SQLiteDatabase db = this.getWritableDatabase();
		clearCategoryTable(db);
		
		ContentValues values = new ContentValues();
		for (Object_Category ob : list) {
			values.put(KEY_ID, ob.getId());
			values.put(KEY_PARENT_ID, ob.getParentId());
			values.put(KEY_NAME, ob.getName());
			//values.put(KEY_IMAGE, Globals.bitmapToByteArray(ob.getImage()));
			values.put(KEY_IMAGE, ob.getImageName());
			//values.put(KEY_TOP_NEWS_ID, ob.getTopNewsId());
			db.insert(TABLE_CATEGORY, null, values);
		}
		db.close();
	}

	public void updateCategoryTopNews(int catId, int newsId) {

		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_TOP_NEWS_ID, newsId);
			
		db.update(TABLE_CATEGORY, values, KEY_ID + " = " + catId, null);


		db.close();
	}
}
