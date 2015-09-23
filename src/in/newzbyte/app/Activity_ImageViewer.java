package in.newzbyte.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Activity_ImageViewer extends Activity {

	ViewPager pager;
	ImageButton btnClose;
	String contectNumber;
	static int selectedImageNo = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer);
		final TextView txtout = (TextView)findViewById(R.id.txtoutTxt);
		
		 btnClose = (ImageButton)findViewById(R.id.btnClose);
		
		
		pager = (ViewPager)findViewById(R.id.pager);
		
		Custom_AdapterImageDisplay adapter = new Custom_AdapterImageDisplay(this, Activity_NewsDetails.allImages);
		pager.setAdapter(adapter);
		
		if(selectedImageNo!=-1)
		 pager.setCurrentItem(selectedImageNo);
		
		btnClose.setOnClickListener(new View.OnClickListener() {       
	         @Override
	         public void onClick(View v) {
	             Activity_ImageViewer.this.finish();
	         }
	     });
		
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int pos, float arg1, int arg2) {
				
				txtout.setText((pos+1)+" / "+Activity_NewsDetails.allImages.size());
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void call(View v){
		if(contectNumber!=null){
			if(!contectNumber.trim().isEmpty()){
			//on call 
			Globals.showAlertDialog("Alert", "Are you sure to Call ?",
					this, "Ok", new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog,int id) {
		                	
		                	Intent callIntent = new Intent(Intent.ACTION_CALL);
							callIntent.setData(Uri.parse("tel:"+contectNumber));
							startActivity(callIntent); 
		                	
							
		                }
		              }, "Cancel", new DialogInterface.OnClickListener() {
		                  public void onClick(DialogInterface dialog,int id) {
		                      
		                	 
		                  }
		                }, false);
			
		  }
		}
		
	} 
}
