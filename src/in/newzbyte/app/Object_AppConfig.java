package in.newzbyte.app;

import android.content.Context;
import android.content.SharedPreferences;

public class Object_AppConfig {

	private int versionAppConfig;
	private int versionCat;
	private int theme;
	private float fontFactor;
	

	private String serverPath;
	//private String newsImagesPath;
	//private String categoryImagesPath;
	private Context context;
	
	private final String KEY_APP_CONFIG = "appConfig";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor = null;
	
	public Object_AppConfig(Context context){
		
		this.context = context;
		prefs = this.context.getSharedPreferences(KEY_APP_CONFIG, 0);
		editor = prefs.edit();
	}
	
	public int getVersionNoAppConfig() {
		if(prefs != null)
			versionAppConfig = prefs.getInt("appConfig_VersionNoAppConfig",0);
		else
			versionAppConfig = 0;
		
		return versionAppConfig;
	}
	public void setVersionNoAppConfig(int versionAppConfig) {
		if (editor != null) {
			editor.putInt("appConfig_VersionNoAppConfig", versionAppConfig);
			editor.commit();
		}

		this.versionAppConfig = versionAppConfig;
	}
	
	
	
	public int getRootCatId() {
		if(prefs != null) 
			return prefs.getInt("appConfig_RootCatId",1);
		else
			return 1;
	}
	public void setRootCatId(int rootCatId) {
		if (editor != null) {
			editor.putInt("appConfig_RootCatId", rootCatId);
			editor.commit();
		}
	}
	
	public float getFontFactor() {
		if(prefs != null)
			fontFactor = prefs.getFloat("appConfig_FontFactor",1);
		else
			fontFactor = 1;
		
		return fontFactor;
	}

	public void setFontFactor(float fontFactor) {
		
		
		if (editor != null) {
			editor.putFloat("appConfig_FontFactor", fontFactor);
			editor.commit();
		}

		this.fontFactor = fontFactor;
	}
	
	public int getVersionNoCategory() {
		if(prefs != null)
			versionCat = prefs.getInt("appConfig_VersionNoCategory",0);
		else
			versionCat = 0;
		
		return versionCat;
	}
	public void setVersionNoCategory(int versionCat) {
		if (editor != null) {
			editor.putInt("appConfig_VersionNoCategory", versionCat);
			editor.commit();
		}

		this.versionCat = versionCat;
	}
	
	public String getServerPath() {
		if(prefs != null)
			serverPath = prefs.getString("appConfig_ServerPath", Globals.DEFAULT_APP_SERVER_PATH);
		else
			serverPath = Globals.DEFAULT_APP_SERVER_PATH;

		return serverPath;
	}
	public void setServerPath(String serverPath) {
		if (editor != null) {
			editor.putString("appConfig_ServerPath", serverPath);
			editor.commit();
		}
		
		this.serverPath = serverPath;
	}
	
	
	
	public String getUserContact() {
		String contact = "";
		if(prefs != null)
			contact = prefs.getString("appConfig_contact", "");
		
		return contact;
	}
	public void setUserContact(String contact) {
		if (editor != null) {
			editor.putString("appConfig_contact", contact);
			editor.commit();
		}
	}
	
	public String getUserName() {
		String name = "";
		if(prefs != null)
			name = prefs.getString("appConfig_name", "");
		
		return name;
	}
	public void setUserName(String name) {
		if (editor != null) {
			editor.putString("appConfig_name", name);
			editor.commit();
		}
	}
	
	public String getUserEmail() {
		String email = "";
		if(prefs != null)
			email = prefs.getString("appConfig_email", "");
		
		return email;
	}
	public void setUserEmail(String mail) {
		if (editor != null) {
			editor.putString("appConfig_email", mail);
			editor.commit();
		}
	}
	
	

	public boolean isNotificationEnabled() {
		boolean notificationEnabled = true;
		if(prefs != null)
			notificationEnabled = prefs.getBoolean("appConfig_NotificationEnabled", true);
		
		return notificationEnabled;
	}

	public void setNotificationEnabled(boolean notificationEnabled) {
		
		if (editor != null) {
			editor.putBoolean("appConfig_NotificationEnabled", notificationEnabled);
			editor.commit();
		}
	}
	
	public int getLangId() {
		int id = 0;
		if(prefs != null)
			id = prefs.getInt("appConfig_langid", 0);
		
		return id;
	}
	public void setLangId(int langId) {
		if (editor != null) {
			editor.putInt("appConfig_langid", langId);
			editor.commit();
		}
	}
	
	
	
	/* Sending full path fro server now
	  
	 
	
	
	public String getNewsImagesPath() {
		if(prefs != null)
			newsImagesPath = prefs.getString("appConfig_NewsImagesPath", Globals.DEFAULT_APP_NEWS_IMAGES_PATH);
		else
			newsImagesPath = Globals.DEFAULT_APP_NEWS_IMAGES_PATH;
		
		return newsImagesPath;
	}
	public String getCategoryImagesPath() {
		if(prefs != null)
			categoryImagesPath = prefs.getString("appConfig_CatImagesPath", Globals.DEFAULT_APP_CAT_IMAGES_PATH);
		else
			categoryImagesPath = Globals.DEFAULT_APP_CAT_IMAGES_PATH;
		
		return categoryImagesPath;
	}
	
	public String getNewsImagesFullPath() {
		
		return getServerPath()+getNewsImagesPath();
	}
	
	public String getCategoryImagesFullPath() {
		
		return getServerPath()+getCategoryImagesPath();
	}
	

	public void setNewsImagesPath(String newsImagesPath) {
		if (editor != null) {
			editor.putString("appConfig_NewsImagesPath", newsImagesPath);
			editor.commit();
		}
		
		this.newsImagesPath = newsImagesPath;
	}
	
	
	public void setCategoryImagesPath(String categoryImagesPath) {
		if (editor != null) {
			editor.putString("appConfig_CatImagesPath", categoryImagesPath);
			editor.commit();
		}
		
		this.categoryImagesPath = categoryImagesPath;
	}
		*/
	
	
}
