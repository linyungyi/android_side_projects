package com.cht.channeldata;

public class ChannelItem{
	
	private String ItemID;
	private String PropTitle;
	private String Prop_Thumbnail_Url;
	private String PropImageUrl;
	private String Description;
	
	public ChannelItem(){};
	
	public void setItemID(String itemID) {
		ItemID = itemID;
	}
	public String getItemID() {
		return ItemID;
	}
	public void setPropTitle(String propTitle) {
		PropTitle = propTitle;
	}
	public String getPropTitle() {
		return PropTitle;
	}
	public void setProp_Thumbnail_Url(String prop_Thumbnail_Url) {
		Prop_Thumbnail_Url = prop_Thumbnail_Url;
	}
	public String getProp_Thumbnail_Url() {
		return Prop_Thumbnail_Url;
	}
	public void setPropImageUrl(String propImageUrl) {
		PropImageUrl = propImageUrl;
	}
	public String getPropImageUrl() {
		return PropImageUrl;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getDescription() {
		return Description;
	}
	
	
	
}