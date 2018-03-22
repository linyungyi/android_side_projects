package com.music.parser.data;

public class MusicBox {

	private String musicid;
	private String musicname;
	private String price;
	private String content;
	private String cpname;
	private String bigicon;
	private String smallicon;
	private String listurl;
	public void setMusicId(String musicid) {
		this.musicid = musicid;
	}
	public String getMusicId() {
		return musicid;
	}
	public void setMusicname(String musicname) {
		this.musicname = musicname;
	}
	public String getMusicName() {
		return musicname;
	}
	public void setCpname(String cpname) {
		this.cpname = cpname;
	}
	public String getCpName() {
		return cpname;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrice() {
		return price;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setSmallicon(String smallicon) {
		this.smallicon = smallicon;
	}
	public String getSmallIconUrl() {
		return smallicon;
	}
	public void setListurl(String listurl) {
		this.listurl = listurl;
	}
	public String getListUrl() {
		return listurl;
	}
	public void setBigicon(String bigicon) {
		this.bigicon = bigicon;
	}
	public String getBigIcon() {
		return bigicon;
	}
	
}
