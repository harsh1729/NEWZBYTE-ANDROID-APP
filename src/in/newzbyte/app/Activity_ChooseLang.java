package in.newzbyte.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class Activity_ChooseLang extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_lang);
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
	    	
	    	Intent i = new Intent(this, Activity_Home.class);
	    	startActivity(i);
	    	
	    	}catch(Exception ex){
	    		Log.i("HARSH", "Exception is language button");
	    	}
	    }
}
