package com.lkp.multibitmaptest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.polites.android.GestureImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class AsyncBitmapLoader {
	private int threadNum = 5;
	private ExecutorService downloadExecutor = Executors.newFixedThreadPool(threadNum);
	private Bitmap newBp;
	private static AsyncBitmapLoader loader;
	private List<Bean_bitmap_data> taskQueue = new ArrayList<Bean_bitmap_data>();
	private int BpWidth = 0;
	private int BpHeight = 0;
	private Handler handler = new Handler();

	private AsyncBitmapLoader() {

	}

	public static AsyncBitmapLoader getInstance() {
		if (null == loader) {
			loader = new AsyncBitmapLoader();
		}
		return loader;
	}

	public void setGestureImageViewWithData(final List<Bean_bitmap_data> datas, final GestureImageView gestureImageView) {
		cleanData();
		if (null == datas || datas.size() == 0) {
			return;
		}
		taskQueue = datas;
		final int bpNum = datas.size();
		System.out.println("KyConstant.SCREENHEIGHT=" + KyConstant.SCREENHEIGHT);
		System.out.println("KyConstant.SCREENWIDTH=" + KyConstant.SCREENWIDTH);
		newBp = Bitmap.createBitmap(KyConstant.SCREENWIDTH, KyConstant.SCREENHEIGHT*bpNum, Config.ARGB_8888);
		System.out.println("newBp.getWidth()=" + newBp.getWidth());
		System.out.println("newBp.getHeight()=" + newBp.getHeight());
		final Canvas canvas = new Canvas(newBp);
		gestureImageView.setImageBitmap(newBp);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (null != taskQueue && taskQueue.size() > 0) {
					final Bean_bitmap_data task = getTask(taskQueue);
					if (null == task) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("所有任务都在进行中");
						continue;
					}
					downloadExecutor.execute(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							InputStream is = null;
							try {
								try {
									is = getInputStream(task);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									task.setUrl(KyUtil.replaceImgSuffix(task.getUrl()));
									is = getInputStream(task);
								}
								if (null != is) {
									Options opts = new Options();
//									 opts.outWidth = newBp.getWidth();
//									 opts.outHeight = newBp.getHeight()/bpNum;
//									 opts.inSampleSize = 2;
									Bitmap bp = BitmapFactory.decodeStream(is, null, opts);
//									Matrix matrix = new Matrix();
//									matrix.postScale(0.6f, 0.6f); // 长和宽放大缩小的比例
//									Bitmap resizeBmp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(),
//											bp.getHeight(), matrix, true);
									System.out.println("newBp.getHeight() / bpNum * task.getIndex()="
											+ newBp.getHeight() / bpNum * task.getIndex());
									System.out.println("bp.getWidth()=" + bp.getWidth());
									System.out.println("bp.getHeight()=" + bp.getHeight());
									canvas.drawBitmap(bp, 0, newBp.getHeight() / bpNum * task.getIndex(), null);
//									resizeBmp.recycle();
									bp.recycle();
									handler.post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											gestureImageView.redraw();
										}
									});
									taskQueue.remove(task);

									System.out.println("成功的任务:" + task.getIndex());
								} else {
									task.setRuning(false);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								task.setRuning(false);
								System.out.println("失败的任务:" + task.getIndex() + "  重新放入任务队列");
							}
						}

					});
				}
				System.out.println("所有任务已经完成");
			}
		}).start();
	}

	private InputStream getInputStream(final Bean_bitmap_data data) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) new URL(data.getUrl()).openConnection();
		conn.setInstanceFollowRedirects(true);

		if (null != data.getSocketToHttp() && null != data.getSocketToHttp().getProperties()) {
			for (int i = 0; i < data.getSocketToHttp().getProperties().size(); i++) {
				conn.setRequestProperty(data.getSocketToHttp().getProperties().get(i).getField(), data
						.getSocketToHttp().getProperties().get(i).getNewValue());

			}
		}
		return new BufferedInputStream(conn.getInputStream());
	}

	// private Bitmap getBitmapFromMulti(Bitmap[] bitmaps) {
	//
	//
	//
	// for(Bitmap bitmap:bitmaps){
	// BpWidth+=bitmap.getWidth();
	// BpHeight+=bitmap.getHeight();
	// }
	//
	// Bitmap newBp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	// Canvas canvas = new Canvas(newBp);
	//
	// // for (Bitmap bitmap : bitmaps) {
	// // canvas.drawBitmap(bitmap, left, top, null);
	// // }
	// canvas.drawBitmap(bitmaps[0], 0, 0, null);
	// canvas.drawBitmap(bitmaps[1], 0, bitmaps[0].getHeight(), null);
	// bitmaps[0].recycle();
	// bitmaps[1].recycle();
	// return newBp;
	// }
	private void cleanData() {
		if (null != taskQueue) {
			taskQueue.clear();
		}
		if (null != newBp && !newBp.isRecycled()) {
			newBp.recycle();
			newBp = null;
		}
		downloadExecutor.shutdown();
		downloadExecutor = Executors.newFixedThreadPool(threadNum);
	}

	private Bean_bitmap_data getTask(List<Bean_bitmap_data> tasks) {
		for (int i = 0; i < tasks.size(); i++) {
			Bean_bitmap_data task = tasks.get(i);
			if (!task.isRuning()) {
				task.setRuning(true);
				return task;
			}
		}
		return null;
	}

}
