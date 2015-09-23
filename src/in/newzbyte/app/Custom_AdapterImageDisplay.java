package in.newzbyte.app;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class Custom_AdapterImageDisplay extends PagerAdapter {

		private Activity _activity;
	    private ArrayList<String> _imagePaths;
	    private LayoutInflater inflater;
	   
	    // constructor
	    public Custom_AdapterImageDisplay(Activity activity,
	            ArrayList<String> imagePaths) {
	        this._activity = activity;
	        this._imagePaths = imagePaths;
	       
	    }
	 
	    @Override
	    public int getCount() {
	        return this._imagePaths.size();
	    }
	 
	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view == ((RelativeLayout) object);
	    }
	     
	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {
	    	Custom_touchImageView imgDisplay;
	      View viewLayout = null;
	        inflater = (LayoutInflater) _activity
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        viewLayout = inflater.inflate(R.layout.custom_show_image, container,
	                false);
	        
	        imgDisplay = (Custom_touchImageView) viewLayout.findViewById(R.id.imgDisplay);
	       
	         
	       
	        Picasso.with(_activity)
	        .load(_imagePaths.get(position))
	        .into(imgDisplay);
	       /* BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	        Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
	        imgDisplay.setImageBitmap(bitmap);*/
	         
	        // close button click event
	       
	        
	        ((ViewPager) container).addView(viewLayout);
	  
	        return viewLayout;
	    }
	     
	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        ((ViewPager) container).removeView((RelativeLayout) object);
	  
	    }
	}
	

