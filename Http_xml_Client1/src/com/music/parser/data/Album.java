package com.music.parser.data;

public class Album {
	private String name;
	private String artist;
	private String date;
	private String publisher;
	private String imgArtistUrl;
	private String iconUrl;
	private String listUrl;
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getArtist() {
		return artist;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setListUrl(String listUrl) {
		this.listUrl = listUrl;
	}
	public String getListUrl() {
		return listUrl;
	}
	public void setImgArtistUrl(String imgArtistUrl) {
		this.imgArtistUrl = imgArtistUrl;
	}
	public String getImgArtistUrl() {
		return imgArtistUrl;
	}
	

}
