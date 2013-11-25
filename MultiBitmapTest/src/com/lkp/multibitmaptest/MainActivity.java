package com.lkp.multibitmaptest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.polites.android.GestureImageView;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {
	protected GestureImageView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		setContentView(R.layout.activity_main);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		KyConstant.SCREENWIDTH=dm.widthPixels;
		KyConstant.SCREENHEIGHT=dm.heightPixels;
		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		view = new GestureImageView(this);
		
		
		List<Bean_bitmap_data> datas = new ArrayList<Bean_bitmap_data>();
		Bean_bitmap_data data1=new Bean_bitmap_data(0, "http://imgsrc.baidu.com/forum/pic/item/53234f4a20a44623a855cded9822720e0df3d7a7.jpg", null);
		Bean_bitmap_data data2=new Bean_bitmap_data(1, "http://imgsrc.baidu.com/forum/pic/item/c5ecab64034f78f01dea600379310a55b2191c5a.jpg", null);
		Bean_bitmap_data data3=new Bean_bitmap_data(2, "http://imgsrc.baidu.com/forum/pic/item/f310728b4710b91273fa130ec3fdfc039245220c.jpg", null);
		datas.add(data1);
		datas.add(data2);
		datas.add(data3);
		AsyncBitmapLoader.getInstance().setGestureImageViewWithData(datas, view);
		

		ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
		view.setLayoutParams(params);
		layout.addView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Bitmap getBitmapFromMulti(Bitmap[] bitmaps) {

		int width = 0;
		int height = 0;
		for(Bitmap bitmap:bitmaps){
			width+=bitmap.getWidth();
			height+=bitmap.getHeight();
		}
		
		Bitmap newBp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBp);
		
//		for (Bitmap bitmap : bitmaps) {
//			canvas.drawBitmap(bitmap, left, top, null);
//		}
		canvas.drawBitmap(bitmaps[0], 0, 0, null);
		canvas.drawBitmap(bitmaps[1], 0, bitmaps[0].getHeight(), null);
		bitmaps[0].recycle();
		bitmaps[1].recycle();
		return newBp;
	}
}
