package in.newzbyte.app;

import android.text.Html;
import android.text.Spanned;




public class Object_SubNewsItem 
{
	/*		FIELDS OF NEWS TABLE  */
	private int newsId;
	private int newsParentId;
	private String newsHeading;
	private String newsContent;
	private String newsImagePath;
	private int imageAlign;
	private double imageRatio;
	private String newsVideo;
	private String newsImageTagline;


	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public int getNewsParentId() {
		return newsParentId;
	}

	public void setNewsParentId(int parentId) {
		this.newsParentId = parentId;
	}

	public String getNewsHeading() {
		return newsHeading;
	}
	public Spanned getNewsHeadingSpan() {
		return Html.fromHtml(newsHeading);
	}

	public void setNewsHeading(String newsHeading) {
		this.newsHeading = newsHeading;
	}

	public String getNewsContent() {
		return newsContent;
	}

	public Spanned getNewsContentSpan() {
		return Html.fromHtml(newsContent);
	}
	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}

	public String getNewsImagePath() {
		return newsImagePath;
	}
	/*
	public String getNewsImageFullPath(Context con){
		Object_AppConfig objConfig = new Object_AppConfig(con);
		return objConfig.getNewsImagesFullPath() + newsImage;
		
	}
	*/
	public void setNewsImagePath(String newsImagePath) {
		this.newsImagePath = newsImagePath;
	}

	public String getNewsVideo() {
		return newsVideo;
	}

	public void setNewsVideo(String newsVideo) {
		this.newsVideo = newsVideo;
	}

	public String getNewsImageTagline() {
		return newsImageTagline;
	}

	public Spanned getNewsImageTaglineSpan() {
		return Html.fromHtml(newsImageTagline);
	}
	public void setNewsImageTagline(String newsImageTagline) {
		this.newsImageTagline = newsImageTagline;
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
	
}
