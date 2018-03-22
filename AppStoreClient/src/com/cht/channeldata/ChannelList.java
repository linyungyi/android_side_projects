package com.cht.channeldata;

public class ChannelList{
	
	private String Channel_ID;
	private String Channel_Name;
	private String Price;
	private String SmallPicUrl;
	private String BigPicUrl;
	private String Description;
	private String SubscriptionType;
	private String ChannelUrl;
	private String Category;
	
	private int Channel_Category_ID;
	private int TrialDays;
	private int Channel_Template;
	private int BigPicVersion;
	private int SmallPicVersion;
	private int EntityVersion;
	
	private boolean Subsricbed;

	public ChannelList(){};
	
	public void setChannel_ID(String channel_ID) {
		Channel_ID = channel_ID;
	}

	public String getChannel_ID() {
		return Channel_ID;
	}

	public void setChannel_Name(String channel_Name) {
		Channel_Name = channel_Name;
	}

	public String getChannel_Name() {
		return Channel_Name;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getPrice() {
		return Price;
	}

	public void setSmallPicUrl(String smallPicUrl) {
		SmallPicUrl = smallPicUrl;
	}

	public String getSmallPicUrl() {
		return SmallPicUrl;
	}

	public void setBigPicUrl(String bigPicUrl) {
		BigPicUrl = bigPicUrl;
	}

	public String getBigPicUrl() {
		return BigPicUrl;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getDescription() {
		return Description;
	}

	public void setSubscriptionType(String subscriptionType) {
		SubscriptionType = subscriptionType;
	}

	public String getSubscriptionType() {
		return SubscriptionType;
	}

	public void setChannelUrl(String channelUrl) {
		ChannelUrl = channelUrl;
	}

	public String getChannelUrl() {
		return ChannelUrl;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public String getCategory() {
		return Category;
	}

	public void setTrialDays(int trialDays) {
		TrialDays = trialDays;
	}

	public int getTrialDays() {
		return TrialDays;
	}

	public void setChannel_Template(int channel_Template) {
		Channel_Template = channel_Template;
	}

	public int getChannel_Template() {
		return Channel_Template;
	}

	public void setBigPicVersion(int bigPicVersion) {
		BigPicVersion = bigPicVersion;
	}

	public int getBigPicVersion() {
		return BigPicVersion;
	}

	public void setSmallPicVersion(int smallPicVersion) {
		SmallPicVersion = smallPicVersion;
	}

	public int getSmallPicVersion() {
		return SmallPicVersion;
	}

	public void setEntityVersion(int entityVersion) {
		EntityVersion = entityVersion;
	}

	public int getEntityVersion() {
		return EntityVersion;
	}

	public void setSubsricbed(boolean subsricbed) {
		Subsricbed = subsricbed;
	}

	public boolean isSubsricbed() {
		return Subsricbed;
	}

	public void setChannel_Category_ID(int channel_Category_ID) {
		Channel_Category_ID = channel_Category_ID;
	}

	public int getChannel_Category_ID() {
		return Channel_Category_ID;
	}

	
	
}