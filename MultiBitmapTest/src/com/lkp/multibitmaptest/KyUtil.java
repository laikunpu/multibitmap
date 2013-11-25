package com.lkp.multibitmaptest;

public class KyUtil {

	public static String replaceImgSuffix(String imgUrl) {
		String frontPartUrl = imgUrl.substring(0, imgUrl.lastIndexOf(".") + 1);
		String preSuffix = imgUrl.substring(imgUrl.lastIndexOf(".") + 1);
		String hindSuffix = "jpg";
		if (preSuffix.toLowerCase().equals("jpg")) {
			hindSuffix = "png";
		} else if (preSuffix.toLowerCase().equals("png")) {
			hindSuffix = "jpg";
		}
		String newImgUrl = frontPartUrl + hindSuffix;

		return newImgUrl;
	}


}
