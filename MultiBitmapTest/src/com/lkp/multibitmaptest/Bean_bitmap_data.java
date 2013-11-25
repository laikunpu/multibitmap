package com.lkp.multibitmaptest;


public class Bean_bitmap_data {
	private int index;
	private String url;
	private Bean_common_socketToHttp socketToHttp;
	private boolean isRuning;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isRuning() {
		return isRuning;
	}

	public void setRuning(boolean isRuning) {
		this.isRuning = isRuning;
	}

	public Bean_common_socketToHttp getSocketToHttp() {
		return socketToHttp;
	}

	public void setSocketToHttp(Bean_common_socketToHttp socketToHttp) {
		this.socketToHttp = socketToHttp;
	}

	public Bean_bitmap_data(int index, String url, Bean_common_socketToHttp socketToHttp) {
		this.index = index;
		this.url = url;
		this.socketToHttp = socketToHttp;
	}

	public Bean_bitmap_data() {
	}
}
