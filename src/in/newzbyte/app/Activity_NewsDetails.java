package in.newzbyte.app;

import java.util.ArrayList;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class Activity_NewsDetails extends Activity {

	private int newsId;
	private Boolean firstTime = true;
	private ProgressDialog mDialog;
	
	public static Object_ListItem_MainNews objNews = null;
	public static ArrayList<String> allImages;// = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_details);
		allImages = new ArrayList<String>();
		
		
		
		if(objNews != null){
			
			//RelativeLayout rlytHEader= (RelativeLayout)findViewById(R.id.llyt_detailHeader);
			//int catColor = this.getResources().getColor(Globals.getCategoryColor(objNews.getCatId(), this));
			//txtHeading.setBackgroundColor(catColor);
			//rlytHEader.setBackgroundColor(catColor);
			
			//TextView txt = (TextView)findViewById(R.id.txtCat);
			//Typeface tfCat = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_CAT_FONT);
			//txt.setTypeface(tfCat);
			//txt.setText(objNews.getCatName());
			
			newsId = objNews.getId();
			getNewsDetail();
		}
		else {
			Globals.showAlertDialogError(this,"Error","Please try again");
		}
		
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (firstTime) {
			firstTime = false;
				
		}
	}
	
	public void goBack(View v)
	{
		this.finish();
	}
	public void shareClick(View v)
	{
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		//sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
		
		if (android.os.Build.VERSION.SDK_INT >= 13) 
		{
			sendIntent.putExtra(Intent.EXTRA_SUBJECT,
					objNews.getHeadingSpan()  + "\n\n");
		}else{
			sendIntent.putExtra(Intent.EXTRA_SUBJECT,
					objNews.getHeadingSpan()  + "\n\n");
		}
		
		
		
		if (objNews.getShareLink() != null && !objNews.getShareLink().trim().equals("")) 
		{
			sendIntent.putExtra(
					Intent.EXTRA_TEXT,
					// currentNewsItem.getContent()
					"Read more @\n"
							+ objNews.getShareLink()
							+ "\nvia "
							+ getResources().getString(
									R.string.news_paper_name) +" for Android");
		} 
		else 
		{
			sendIntent.putExtra(
					Intent.EXTRA_TEXT,
					"Read more @\n"
							+ getResources().getString(
									R.string.txt_company_website)//+"/detail/"+currentNewsItem.getId()
							+ "\nvia "
							+ getResources().getString(
									R.string.news_paper_name)+" for Android");
		}
		
		
		sendIntent.setType("text/plain"); 
		startActivity(sendIntent);
		// startActivity(Intent.createChooser(sendIntent, "Share Via"));
	}
	public void settingsClick(View v)
	{
		Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
	}
	
	
	private void getNewsDetail() {
		try {
			
			Custom_ConnectionDetector cd = new Custom_ConnectionDetector(
					getApplicationContext());
			if (!cd.isConnectingToInternet()) {

				Globals.showAlertDialogError(this,Globals.TEXT_CONNECTION_ERROR_HEADING,Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST);
				return;
			}

			mDialog = Globals.showLoadingDialog(mDialog, this, true);
			
			
			Custom_VolleyArrayRequest jsonObjectRQST = new Custom_VolleyArrayRequest(
					Request.Method.POST,
					Custom_URLs_Params.getURL_NewsDetail(),
					Custom_URLs_Params.getParams_NewsDetail(newsId),
					new Listener<JSONArray>() {

						@Override
						public void onResponse(JSONArray response) {
							gotNewsDetailResponse(response);

						}

					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError err) {
							err.printStackTrace();
							//handleNetworkError();
						}
					});

			Custom_AppController.getInstance().addToRequestQueue(jsonObjectRQST);

		} catch (Exception ex) {
			//handleNetworkError();
		}
	}
	
	public void imgClicked(View v)
	{
		Intent i = new Intent(this,Activity_ImageViewer.class);
		Log.i("DARSH", "imgClicked");
		if(v.getTag(R.id.img_newsimage) != null){
			Integer intgr = (Integer) v.getTag(R.id.img_newsimage);
			if(intgr != null){
				Log.i("DARSH", "intgr "+intgr.intValue());
				Activity_ImageViewer.selectedImageNo = intgr.intValue();
				
			}
		}
		
		startActivity(i);
	}
	//private void setLeftImageContent(LinearLayout llyt_mainContainer,final Object_ListItem_MainNews objNews,Typeface tf)
	@SuppressLint("NewApi")
	private void setLeftImageContent(int imgtag,LinearLayout llyt_mainContainer,Typeface tf,double imgratio,String imgpath,final Spanned content)
	{
		View singleNewsView = getLayoutInflater().inflate(R.layout.custom_newsdetail_single_left, llyt_mainContainer, false);
		
//		TextView hdg = (TextView)singleNewsView.findViewById(R.id.txt_newsheading);
//		hdg.setVisibility(View.GONE);
		
		ImageView img = (ImageView)singleNewsView.findViewById(R.id.img_newsimage);
		img.setTag(R.id.img_newsimage,Integer.valueOf(imgtag));
	
		
		final int wdth = (Globals.getScreenSize(this).x-20)*3/5;
		//int hgt = (int) (wdth*objNews.getImageRatio());
		int hgt = (int) (wdth*imgratio);
		
		//Log.i("jaspal","img widht:"+wdth+"   hgt:"+hgt);
		RelativeLayout.LayoutParams img_lp = new RelativeLayout.LayoutParams(wdth,hgt);
		img.setLayoutParams(img_lp);
		
		//Globals.loadImageIntoImageView(img, objNews.getImagePath(), this,R.drawable.loading_image_small,R.drawable.no_image_small);
		//loadImageIntoImageView( ImageView iv ,String imgURL, Context context ,int loadingImgId, int errorImgId ,int height,int width)
		//Globals.loadImageIntoImageView(img, objNews.getImagePath(),this,R.drawable.loading, R.drawable.loading,hgt,wdth);
		Globals.loadImageIntoImageView(img, imgpath,this,R.drawable.loading, R.drawable.loading,hgt,wdth);
		
		final TextView txt = (TextView)singleNewsView.findViewById(R.id.txt_newscontent);
		int widthSpec = MeasureSpec.makeMeasureSpec(LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY);
		int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		txt.measure(widthSpec, heightSpec);
		final int rows = Globals.getTxtViewRowsByImgHgt(txt.getMeasuredHeight(),(int)txt.getLineSpacingMultiplier(),hgt);
		//Log.i("jaspal","Rows found:"+rows);
		
		final RelativeLayout.LayoutParams txt_lp = new RelativeLayout.LayoutParams(txt.getLayoutParams());
		txt_lp.setMargins(wdth+6, 0, 0, 0);
		txt.setLayoutParams(txt_lp);
		txt.setTypeface(tf);
		txt.setText(content);
		
		
		 txt.getViewTreeObserver().addOnGlobalLayoutListener( new OnGlobalLayoutListener() { 
			 
	            @SuppressLint("NewApi")
				public void onGlobalLayout() { 
	 
	                int linesCount = txt.getLayout().getLineCount();
	                // restore the margin 
	                txt_lp.setMargins(0, 0, 0, 0); 
	                SpannableString spanS =  new  SpannableString ( content );
	 
	                if (linesCount <= rows) {
	                	//Log.i("jaspal","lines count less than Rows,linescount:"+linesCount);
	                    //spanS.setSpan(new MyLeadingMarginSpan2(lines, width), 0, spanS.length(), 0);
	                    spanS.setSpan(new Custom_LeadingMarginSpace(rows, wdth+6),0, spanS.length(),0);
	                    txt.setText(spanS);
	                } 
	                else 
	                { 
	                	//Log.i("jaspal","lines count greater than Rows,linescount:"+linesCount);
	                    // find the breakpoint where to break the String. 
	                    int breakpoint = txt.getLayout().getLineEnd(rows-1);
	                    //Log.i("jaspal","breakpoint at :"+breakpoint);
	 
	                    Spannable s1 = new SpannableStringBuilder(spanS, 0, breakpoint);
	                   // Log.i("jaspal","s1 spannable is :"+Html.toHtml(s1));
	                    s1.setSpan(new Custom_LeadingMarginSpace(rows, wdth+6),0, s1.length(),0);
	                    Spannable s2 = new SpannableStringBuilder(System.getProperty("line.separator"));
	                    Spannable s3 = new SpannableStringBuilder(spanS, breakpoint, spanS.length());
	                    // It is needed to set a zero-margin span on for the text under the image to prevent the space on the right!
	                    s3.setSpan(new Custom_LeadingMarginSpace(0,0),0, s3.length(),0);
	                    txt.setText(TextUtils.concat(s1, s2, s3));
	                } 
	 
	                // remove the GlobalLayoutListener 
	                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
	                     txt.getViewTreeObserver().removeOnGlobalLayoutListener(this);                           
	                 } else { 
	                     txt.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	                 } 
	            } 
	        });
		llyt_mainContainer.addView(singleNewsView);
	}
	
	//private void setCenterImageContent(LinearLayout llyt_mainContainer,final Object_ListItem_MainNews objNews,Typeface tf)
	private void setCenterImageContent(int imgtag,LinearLayout llyt_mainContainer,Typeface tf,double imgratio,String imgpath,final Spanned content)
	{
		View singleNewsView = getLayoutInflater().inflate(R.layout.custom_newsdetail_single_center, llyt_mainContainer, false);
		
//		TextView hdg = (TextView)singleNewsView.findViewById(R.id.txt_newsheading);
//		hdg.setVisibility(View.GONE);
		
		ImageView img = (ImageView)singleNewsView.findViewById(R.id.img_newsimage);
		int wdth = (Globals.getScreenSize(this).x-20);
		int hgt = (int) (wdth*imgratio);
		
		img.setTag(R.id.img_newsimage,Integer.valueOf(imgtag));
		
		LinearLayout.LayoutParams img_lp = new LinearLayout.LayoutParams(wdth,hgt);
		img.setLayoutParams(img_lp);
		
		//Globals.loadImageIntoImageView(img, objNews.getImagePath(), this,R.drawable.loading_image_small,R.drawable.no_image_small);
		Globals.loadImageIntoImageView(img, imgpath,this,R.drawable.loading, R.drawable.loading,hgt,wdth);
		TextView txt = (TextView)singleNewsView.findViewById(R.id.txt_newscontent);
		txt.setTypeface(tf);
		txt.setText(content);
		
		llyt_mainContainer.addView(singleNewsView);
	}
	
	//private void setRightImageContent(LinearLayout llyt_mainContainer,final Object_ListItem_MainNews objNews,Typeface tf)
	@SuppressLint("NewApi")
	private void setRightImageContent(int imgtag,LinearLayout llyt_mainContainer,Typeface tf,double imgratio,String imgpath,final Spanned content)
	{
		View singleSubNewsView = getLayoutInflater().inflate(R.layout.custom_newsdetail_single_right, llyt_mainContainer, false);
		
		//TextView hdg = (TextView)singleSubNewsView.findViewById(R.id.txt_newsheading);
		//if(object_SubNewsItem.getNewsHeadingSpan().equals(""))
			//hdg.setVisibility(View.GONE);
		//else
			//hdg.setText(object_SubNewsItem.getNewsHeadingSpan());
		
		ImageView subimg = (ImageView)singleSubNewsView.findViewById(R.id.img_newsimage);
		//subimg.setTag(R.id.img_newsimage,Integer.valueOf(i));
		subimg.setTag(R.id.img_newsimage,Integer.valueOf(imgtag));
		
		final int imgwdth = (Globals.getScreenSize(this).x-20)*3/5;
		int imghgt = (int) (imgwdth*imgratio);
		RelativeLayout.LayoutParams subimg_lp = new RelativeLayout.LayoutParams(imgwdth,imghgt);
		subimg_lp.setMargins(Globals.getScreenSize(this).x-20-imgwdth, 0, 0, 0);
		subimg.setLayoutParams(subimg_lp);
		
		//Globals.loadImageIntoImageView(subimg,object_SubNewsItem.getNewsImagePath(), this,R.drawable.loading_image_small,R.drawable.no_image_small);
		
		Globals.loadImageIntoImageView(subimg, imgpath,this,R.drawable.loading, R.drawable.loading,imghgt,imgwdth);
		
		final TextView subtxt_imgside = (TextView)singleSubNewsView.findViewById(R.id.txt_newscontent_imgside);
		final TextView subtxt = (TextView)singleSubNewsView.findViewById(R.id.txt_newscontent);
		int subwidthSpec = MeasureSpec.makeMeasureSpec(LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY);
		int subheightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		subtxt_imgside.measure(subwidthSpec, subheightSpec);
		subtxt_imgside.setTypeface(tf);
		subtxt.setTypeface(tf);
		final int subrows = Globals.getTxtViewRowsByImgHgt(subtxt_imgside.getMeasuredHeight(),(int)(subtxt_imgside.getLineSpacingMultiplier()+subtxt_imgside.getLineSpacingExtra()),imghgt);
		
		final RelativeLayout.LayoutParams subtxt_lp = new RelativeLayout.LayoutParams(subtxt_imgside.getLayoutParams());
		subtxt_lp.setMargins(0, 0, imgwdth+6, 0);
		subtxt_imgside.setLayoutParams(subtxt_lp);
		subtxt_imgside.setText(content);
		
		
		subtxt_imgside.getViewTreeObserver().addOnGlobalLayoutListener( new OnGlobalLayoutListener() { 
			 
            public void onGlobalLayout() { 
 
                int linesCount = subtxt_imgside.getLayout().getLineCount();
                // restore the margin 
                //subtxt_lp.setMargins(0, 0, 0, 0); 
                SpannableString spanS =  new  SpannableString (content);
 
                if (linesCount <= subrows) {
                    //spanS.setSpan(new MyLeadingMarginSpan2(lines, width), 0, spanS.length(), 0);
                    spanS.setSpan(new Custom_LeadingMarginSpace(subrows, imgwdth+6),0, spanS.length(),0);
                    subtxt_imgside.setText(spanS);
                } 
                else 
                { 
                    // find the breakpoint where to break the String. 
                    int breakpoint = subtxt_imgside.getLayout().getLineEnd(subrows-1);
                    Log.i("jaspal","breakpoint at :"+breakpoint);
                    
                    
 
                    Spannable s1 = new SpannableStringBuilder(spanS, 0, breakpoint);
                    Spannable s3 = new SpannableStringBuilder(spanS, breakpoint, spanS.length());
                    // It is needed to set a zero-margin span on for the text under the image to prevent the space on the right!
                    s3.setSpan(new Custom_LeadingMarginSpace(0,0),0, s3.length(),0);
                    //subtxt_imgside.setText(TextUtils.concat(s1, s2));
                    subtxt_imgside.setText(s1);
                    subtxt.setText(s3);
                } 
 
                // remove the GlobalLayoutListener 
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                	subtxt_imgside.getViewTreeObserver().removeOnGlobalLayoutListener(this);                           
                 } else { 
                	 subtxt_imgside.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                 } 
            } 
        });
		
		llyt_mainContainer.addView(singleSubNewsView);
	}
	
	private void gotNewsDetailResponse(JSONArray response) {

		ArrayList<Object_SubNewsItem> subNewsList;
		Log.i("DARSH", "gotNewsDetailResponce onResponse" + response);
		Custom_JsonParserNews parserObject = new Custom_JsonParserNews();

		subNewsList = parserObject.getParsedJsonSubNews(response, newsId);
		
		Log.i("jaspal","subnews list size:"+subNewsList.size());
		
		LinearLayout llyt_mainContainer = (LinearLayout)findViewById(R.id.llyt_mainContainer);
		
		TextView txtHeading = (TextView)findViewById(R.id.txtHeading);
		
		txtHeading.setText(objNews.getHeadingSpan());
		Typeface tf = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_FONT);
		txtHeading.setTypeface(tf, Typeface.BOLD);
		
		if(!objNews.getImagePath().equals(""))
			allImages.add(objNews.getImagePath());
		
		if(objNews.getImageAlign() == Globals.IMAGE_ALIGN_LEFT)
		{
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
				setLeftImageContent(0,llyt_mainContainer,tf,objNews.getImageRatio(),objNews.getImagePath(),objNews.getContentSpan());                          
             }else{
           	  setCenterImageContent(0,llyt_mainContainer, tf, objNews.getImageRatio(), objNews.getImagePath(), objNews.getContentSpan());
             }
			
			
		}
		else if(objNews.getImageAlign() == Globals.IMAGE_ALIGN_CENTER )
		{
			setCenterImageContent(0,llyt_mainContainer, tf, objNews.getImageRatio(), objNews.getImagePath(), objNews.getContentSpan());
			
		}
		else if(objNews.getImageAlign() == Globals.IMAGE_ALIGN_RIGHT)
		{
			 if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
				 setRightImageContent(0,llyt_mainContainer, tf, objNews.getImageRatio(), objNews.getImagePath(), objNews.getContentSpan());                           
              }else{
            	  setCenterImageContent(0,llyt_mainContainer, tf, objNews.getImageRatio(), objNews.getImagePath(), objNews.getContentSpan());
              }
		}
				
		
		int i =0;
		for (final Object_SubNewsItem object_SubNewsItem : subNewsList) {
			
			i++;
			if(!object_SubNewsItem.getNewsImagePath().equals(""))
				allImages.add(object_SubNewsItem.getNewsImagePath());
			
			if(object_SubNewsItem.getImageAlign() == Globals.IMAGE_ALIGN_LEFT)
			{
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
					setLeftImageContent(i,llyt_mainContainer,tf,object_SubNewsItem.getImageRatio(),object_SubNewsItem.getNewsImagePath(),object_SubNewsItem.getNewsContentSpan());             
	              }else{
	            	  setCenterImageContent(i,llyt_mainContainer, tf, object_SubNewsItem.getImageRatio(), object_SubNewsItem.getNewsImagePath(), object_SubNewsItem.getNewsContentSpan());
	              }
			}
			else if( object_SubNewsItem.getImageAlign() == Globals.IMAGE_ALIGN_CENTER)
			{
				setCenterImageContent(i,llyt_mainContainer, tf, object_SubNewsItem.getImageRatio(), object_SubNewsItem.getNewsImagePath(), object_SubNewsItem.getNewsContentSpan());

				
			}else if(object_SubNewsItem.getImageAlign() == Globals.IMAGE_ALIGN_RIGHT)
			{
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
					setRightImageContent(i,llyt_mainContainer, tf, object_SubNewsItem.getImageRatio(), object_SubNewsItem.getNewsImagePath(), object_SubNewsItem.getNewsContentSpan());                     
	              }else{
	            	  setCenterImageContent(i,llyt_mainContainer, tf, object_SubNewsItem.getImageRatio(), object_SubNewsItem.getNewsImagePath(), object_SubNewsItem.getNewsContentSpan());
	              }
		
			}
		}

		Globals.hideLoadingDialog(mDialog);
		//showNewsDetails();
	}
}
