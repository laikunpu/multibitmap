package com.view.test;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

/**
 * 测试surfaceview缩放,移动
 * 
 */
public class TestsurfaceviewActivity extends Activity {
	private SurfaceView surface_view;
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private boolean flag = true;
	private Bitmap bmpIcon;
	private Bitmap centerPoint;
	private Bitmap location;
	private Rect rect;// 图片的rect
	// 倍率
	private float rate = 1;
	// 记录上次的倍率
	private float oldRate = 1;
	// 判定是否头次多指触点屏幕
//	private boolean isFirst = true;
//	private boolean canDrag = false;// 判断是否点击在图片上，否则拖动无效
//	private int offsetX = 0, offsetY = 0;// 点击点离图片左上角的距离
	
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	PointF end = new PointF();
	PointF screenCenter = new PointF();
	PointF mapCenter = new PointF();
	float oldDist = 1f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		surface_view = (SurfaceView) findViewById(R.id.surface_view);
		sfh = surface_view.getHolder();
		sfh.addCallback(new DisplaySurfaceView());
		surface_view.setOnTouchListener(listener);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
	}
	
	

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);		
		//任务栏高度
		Rect frame =  new  Rect();   
		surface_view.getWindowVisibleDisplayFrame(frame);   
		int  statusBarHeight = frame.top;
		
		//标题栏高度
		int  contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();   
		//statusBarHeight是上面所求的状态栏的高度    
		int  titleBarHeight = contentTop - statusBarHeight;
		
		float x  = (this.getWindowManager().getDefaultDisplay().getWidth()) / 2;
		float y = (this.getWindowManager().getDefaultDisplay().getHeight()-statusBarHeight-titleBarHeight) / 2;
		screenCenter.set(x,y);
		mapCenter.set(x, y);
	}



	/**
	 * 
	 */
	class DisplaySurfaceView implements SurfaceHolder.Callback {
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			// new ImageThread().start();
			draw();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			flag = false;
		}

	}
	
	private OnTouchListener listener  = new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				start.set(event.getX(), event.getY());
				end.set(event.getX(), event.getY());
				mode = DRAG;				
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				// Log.d(TAG, "oldDist=" + oldDist);
				if (oldDist > 10f) {
					midPoint(mid, event);
					mode = ZOOM;
				}
				break;
			case MotionEvent.ACTION_UP:				
				oldRate = rate;
				//记录移动后地图中心位置
				mapCenter.set(mapCenter.x + (end.x - start.x), mapCenter.y + (end.y - start.y));
				break;
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					end.set(event.getX(), event.getY());
//					mapCenter.set(mapCenter.x + (end.x - start.x), mapCenter.y + (end.y - start.y));
				} else if (mode == ZOOM) {
					float newDist = spacing(event);
					// Log.d(TAG, "newDist=" + newDist);
					if (newDist > 10f) {
						rate =oldRate *(newDist / oldDist);
					}
				}
				draw();
				break;
			}
			return true;
		}
		
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 图片线程，现在没用到
	 * 
	 */
	class ImageThread extends Thread {
		@Override
		public void run() {
			while (flag) {
				long start = System.currentTimeMillis();
				draw();
				long end = System.currentTimeMillis();
				try {
					if (end - start < 50) {
						Thread.sleep(50 - (end - start));
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void draw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
//				canvas.drawColor(Color.BLACK);
//				canvas.save();
////				 缩放画布(以图片中心点进行缩放，XY轴缩放比�?
//				bmpIcon = BitmapFactory.decodeResource(this.getResources(),
//						R.drawable.swf15_1);
//				canvas.scale(rate, rate, screenW, screenH);		
//				int width = screenW + (newX - oldX) - bmpIcon.getWidth() / 2;
//				int height = screenH + (newY - oldY) - bmpIcon.getHeight() / 2;
//				// 绘制位图icon
//				canvas.drawBitmap(bmpIcon, width, height, paint);
//				
//				canvas.scale(1.0f, 1.0f, centerX, centerY);	
//				centerPoint = BitmapFactory.decodeResource(this.getResources(),R.drawable.center);
//				canvas.drawBitmap(centerPoint, centerX, centerY, paint);
//				canvas.restore();
				//----------------------------------
				//地图
				canvas.drawRect(0, 0, screenCenter.x*2, screenCenter.y*2, new Paint());
				bmpIcon=BitmapFactory.decodeResource(this.getResources(),
						R.drawable.xizhimen);
				Matrix matrix=new Matrix();				
				matrix.setScale(rate, rate, bmpIcon.getWidth()/2, bmpIcon.getHeight()/2);
				matrix.postTranslate(mapCenter.x + (end.x - start.x) - bmpIcon.getWidth() / 2,  mapCenter.y + (end.y - start.y) - bmpIcon.getHeight() / 2);
//				matrix.postTranslate(mapCenter.x - bmpIcon.getWidth() / 2,  mapCenter.y - bmpIcon.getHeight() / 2);
				canvas.drawBitmap(bmpIcon, matrix, paint);
				Log.v("TestsurfaceviewActivity", " 11 bmpIcon.getWidth() = " + bmpIcon.getWidth());
				
				//位置坐标
				matrix.setScale(1.0f, 1.0f);
				location = BitmapFactory.decodeResource(this.getResources(),R.drawable.location);
				/**
				 * 标记点坐标相对于地图左上角偏移（100,125）
				 * (newX - oldX) 移动时的偏移量
				 */
				matrix.postTranslate(mapCenter.x + (end.x - start.x) - location.getWidth()/2 - bmpIcon.getWidth()*rate/2 + 100*rate ,  mapCenter.y + (end.y - start.y) - location.getHeight() - bmpIcon.getHeight()* rate / 2 + 125*rate);
//				matrix.postTranslate(mapCenter.x - location.getWidth()/2 - bmpIcon.getWidth()*rate/2 + 100*rate ,  mapCenter.y - location.getHeight() - bmpIcon.getHeight()* rate / 2 + 125*rate);
				canvas.drawBitmap(location, matrix, paint);
				
				//把心
				matrix.setScale(1.0f, 1.0f);
				centerPoint = BitmapFactory.decodeResource(this.getResources(),R.drawable.center);
				matrix.postTranslate(screenCenter.x-centerPoint.getWidth()/2, screenCenter.y-centerPoint.getHeight()/2);
				canvas.drawBitmap(centerPoint, matrix, paint);
				
				//把心在地图上的相对坐标
//				Log.v("TestsurfaceviewActivity", "x = " + ( centerX - (screenW + (newX - oldX) - bmpIcon.getWidth()*rate/2 ) )/rate );
//				Log.v("TestsurfaceviewActivity", "y = " + ( centerY -  (screenH + (newY - oldY) - bmpIcon.getHeight()*rate/2 ) )/rate );

				//当前触摸点在地图上的相对坐标
//				Log.v("TestsurfaceviewActivity", "x2 = " + ( newX - (screenW + (newX - oldX) - bmpIcon.getWidth()*rate/2 ) )/rate );
//				Log.v("TestsurfaceviewActivity", "y2 = " + ( newY - (screenH + (newY - oldY) - bmpIcon.getHeight()*rate/2 ) )/rate );
				
				sfh.unlockCanvasAndPost(canvas);
				if(bmpIcon!=null)
					bmpIcon.recycle();
				if(centerPoint!=null)
					centerPoint.recycle();
				if(location!=null)
					location.recycle();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * 资源图片转为Bitmap
	 * 
	 * @param context
	 * @param imageId
	 * @return
	 */
	public static final Bitmap getImage(Context context, int imageId) {
		return BitmapFactory.decodeResource(context.getResources(), imageId);
	}
	
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}
