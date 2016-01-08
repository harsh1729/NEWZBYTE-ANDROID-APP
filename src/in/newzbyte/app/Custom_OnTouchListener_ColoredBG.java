package in.newzbyte.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Custom_OnTouchListener_ColoredBG implements OnTouchListener {

	final View view;
	Context context;
	int catId;
	public Custom_OnTouchListener_ColoredBG(final View view , Context context , int catId) {
		super();
		this.view = view;
		this.context = context;
		this.catId = catId;
	}

	@SuppressLint("NewApi")
	public boolean onTouch(final View view, final MotionEvent motionEvent) {	
			switch (motionEvent.getAction()) {

			case MotionEvent.ACTION_DOWN:      	
				
				DBHandler_Category dbCat = new DBHandler_Category(context);
				GradientDrawable shape =  new GradientDrawable();
				 //shape.setCornerRadius(10);
				 
				String catColor = dbCat.getCategoryColor(catId);
				if(!catColor.isEmpty()){
					shape.setColor(Color.parseColor(catColor));
				}
				//int catColor = context.getResources().getColor(Globals.getCategoryColor(catId, context));
				
				
				 if(VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN){                   
					 view.setBackground(shape);
				    } 
				    else{ 
				    	view.setBackgroundDrawable(shape);  // deprecated, use only in Android OS<3.0.
				    } 
				 
				break;

			case MotionEvent.ACTION_UP: 
				//view.setBackgroundResource(R.drawable.bg_rounded_shadow);
				break;
			case MotionEvent.ACTION_OUTSIDE: 
				view.setBackgroundResource(R.drawable.bg_rounded_shadow);
				break;

			case MotionEvent.ACTION_CANCEL: 
				view.setBackgroundResource(R.drawable.bg_rounded_shadow);
				break;

			default   :
				break;

			}
		return false;
	}

}
