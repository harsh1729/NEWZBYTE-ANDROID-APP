package in.newzbyte.app;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.text.Html;
import android.text.Spanned;

public class Object_ListItem_MainNews implements Interface_ListItem
{
	/*		FIELDS OF NEWS TABLE  */
	private int id;
	private String heading;
	private String content;
	private int catId;
	private String date;
	private String imagePath;
	private int imageAlign;
	private double imageRatio;
	private String video;
	private String shareLink;
	private String imageTagline;
	private String catName;
	private String summary;
	private String source;
	private String author;
	private int typeId;
	public Boolean hasDetailNews;
	

	private int newsType = NEWS_TYPE_NORMAL;
	
	public static final int NEWS_TYPE_NORMAL = 1;
	public static final int NEWS_TYPE_CAT_TOP = 2;
	public static final int NEWS_TYPE_BREAKING = 3;

	private ArrayList<Object_SubNewsItem> listSubNews = null;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Spanned getHeadingSpan() {
		return Html.fromHtml(heading) ;
	}
	
	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getContent() {
		return content;
	}

	public Spanned getContentSpan() {
		if(content != null && !content.isEmpty())
			return Html.fromHtml(content);
		
		return Html.fromHtml("");
	}
	public void setContent(String content) {
		this.content = content;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",Locale.ENGLISH);
			Date newDate;
			newDate = format.parse(date);
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
			date = format.format(newDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		
		
		this.date = date;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String image) {
		this.imagePath = image;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	
	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}
	
	public String getImageTagline() {
		return imageTagline;
	}
	
	public Spanned getImageTaglineSpan() {
		return Html.fromHtml(imageTagline) ;
	}

	public void setImageTagline(String imageTagline) {
		this.imageTagline = imageTagline;
	}

	public ArrayList<Object_SubNewsItem> getListSubNews() {
		if(listSubNews == null)
			listSubNews = new ArrayList<Object_SubNewsItem>();
			
		return listSubNews;
	}

	public Boolean isListSubNewsNull(){
		if(listSubNews == null)
			return true;
		
		return false;
	}
	public void setListSubNews(ArrayList<Object_SubNewsItem> listSubNews) {
		this.listSubNews = listSubNews;
	}

	public int getNewsType() {
		return newsType;
	}

	public void setNewsType(int newsType) {
		this.newsType = newsType;
	}

	public int getImageAlign() {
		return imageAlign;
	}

	public void setImageAlign(int imageAlign) {
		this.imageAlign = imageAlign;
	}

	public double getImageRatio() {
		return imageRatio;
	}

	public void setImageRatio(double imageRatio) {
		this.imageRatio = imageRatio;
	}
	
	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Spanned getSummarySpan() {
		return Html.fromHtml(summary) ;
	}

	public String getSource() {
		return source;
	}
	public Spanned getSourceSpan() {
		return Html.fromHtml(source) ;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

}
