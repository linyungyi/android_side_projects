package com.music.parser.data;

public class Activity {

	private String begin_time;
	private String end_time;
	private String content;
	private String img;
	private String name;
	private String url;
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getBeginDate() {
		return begin_time;
	}
	public void setEndDate(String end_time) {
		this.end_time = end_time;
	}
	public String getEndDate() {
		return end_time;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setImgUrl(String img) {
		this.img = img;
	}
	public String getImgUrl() {
		return img;
	}
	public void setSongsUrl(String url) {
		this.url = url;
	}
	public String getSongsUrl() {
		return url;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
}
