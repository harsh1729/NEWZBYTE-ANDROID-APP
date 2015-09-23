package in.newzbyte.app;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Custom_DrawerLayout extends DrawerLayout {

	public Custom_DrawerLayout(Context context) {
		super(context);
	}
	
	public Custom_DrawerLayout(Context context, AttributeSet attrs){
	    super(context, attrs);
	}

	public Custom_DrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
	    super(context, attrs, defStyleAttr);
	}

	

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
	    super.onTouchEvent(arg0);
	    float edge = 50;//that's for a left drawer obviously. Use <parentWidth - 30> for the right one.
	   View mDrawerListView = findViewById(R.id.rlytDrawerPane);

	    if(isDrawerOpen(mDrawerListView) || 
	            isDrawerVisible(mDrawerListView)){
	        return true;
	    } else if(arg0.getAction() == MotionEvent.ACTION_DOWN && arg0.getX() > edge){
	        return false;
	    }

	    return true;
	    //return false;
	}
	
}
