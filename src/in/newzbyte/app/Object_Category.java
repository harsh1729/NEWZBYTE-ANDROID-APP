package in.newzbyte.app;

import java.util.ArrayList;

import android.graphics.Bitmap;


public class Object_Category {
	private int id;
	private int parentId;
	private int topNewsId;
	private int langId;
	private String Name;
	private String imageName;
	private String selectedImageName;
	private int isSelected;
	private String color;
	//private Bitmap image;
	private ArrayList<Object_Category> listChildCategory= null;
	
	
	public ArrayList<Object_Category> getListChildCategory() {
		return listChildCategory;
	}

	public void setListChildCategory(ArrayList<Object_Category> listChildCategory) {
		this.listChildCategory = listChildCategory;
	}

	public String getImageName() {
		if(imageName != null)
			return imageName.trim();
		
		return "";
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id=id;
	}
	
	public int getParentId()
	{
		return parentId;
	}
	
	public void setParentId(int parentId)
	{
		this.parentId=parentId;
	}
	
	public String getName()
	{
		return Name;
	}
	
	/*
	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
*/
	public void setName(String Name)
	{
		this.Name=Name;
	}

	public int getTopNewsId() {
		return topNewsId;
	}

	public void setTopNewsId(int topNewsId) {
		this.topNewsId = topNewsId;
	}

	public String getSelectedImageName() {
		return selectedImageName;
	}

	public void setSelectedImageName(String selectedImageName) {
		this.selectedImageName = selectedImageName;
	}

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	public int getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(int isSelected) {
		this.isSelected = isSelected;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
