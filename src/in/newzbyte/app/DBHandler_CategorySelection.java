package in.newzbyte.app;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/* 
CREATE TABLE CategorySelection (Id integer NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,CatId integer NOT NULL UNIQUE)
 */
public class DBHandler_CategorySelection extends SQLiteOpenHelper {

	private final String TABLE_CATEGORY_SELECTION = "CategorySelection";
	public static final String KEY_ID = "Id";
	public static final String KEY_CAT_ID= "CatId";
	
	
	public DBHandler_CategorySelection(Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public ArrayList<Integer> getAllCategories() {//Context con

		Log.i("HARSH", " getAllCategories called!");
		String selectQuery = "select "+ KEY_CAT_ID+" from " + TABLE_CATEGORY_SELECTION ;

		SQLiteDatabase db = this.getReadableDatabase();

		ArrayList<Integer> Ids = new ArrayList<Integer>();

		Cursor cur = db.rawQuery(selectQuery, null);
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					
					Ids.add(Integer.valueOf(cur.getInt(cur.getColumnIndex(KEY_CAT_ID))));
					
				} while (cur.moveToNext());
			}
		}

		db.close();
		
		return Ids;

	}
	
	public boolean containsCatId(int catId) {//Context con

		Log.i("HARSH", " containsCatId called!");
		String selectQuery = "select * from " + TABLE_CATEGORY_SELECTION + " where "+ KEY_CAT_ID + " = " + catId ;

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cur = db.rawQuery(selectQuery, null);
		if (cur != null) {
			if (cur.getCount()> 0) {
				db.close();
				return true;
			}
		}

		db.close();
		
		//setRootNewsOnTop(con, Cat_group);
		
		return false;

	}
	
	public void insertSelectedCat(int catId) {

		SQLiteDatabase db = this.getWritableDatabase();
		try {
				ContentValues values = new ContentValues();
				values.put(KEY_CAT_ID, catId);
				Log.i("DARSH", "Inserting News");
				db.insert(TABLE_CATEGORY_SELECTION, null, values);
			}
			catch (Exception ex) {
				Log.i("HARSH", "Exception in inserting main news");
			}

	db.close();

}


	public void clearCategoryTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		String deleteall = "delete from " + TABLE_CATEGORY_SELECTION;
		db.execSQL(deleteall);
	}

	public void clearCategory(int CatId){
		SQLiteDatabase db = this.getWritableDatabase();
		String deleteQuery = "delete from " + TABLE_CATEGORY_SELECTION + " where "+ KEY_CAT_ID + " = "+CatId;
		db.execSQL(deleteQuery);
	}
}
