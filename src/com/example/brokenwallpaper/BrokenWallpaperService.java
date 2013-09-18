package com.example.brokenwallpaper;

import java.util.Random;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class BrokenWallpaperService extends WallpaperService {

/*- ********************************************************************************** */
/*- *********** OVERRIDE ************* */
/*- ********************************************************************************** */
@Override
public Engine onCreateEngine() {
  return new BrokenWallpaperEngine();
}

/*- ********************************************************************************** */
/*- *********** PRIVATE CLASS IMAGE************* */
/*- ********************************************************************************** */
private class Image {
private int id;
private float xDespl, yDespl;
public Image(int id, float xDespl, float yDespl) {
	this.id = id;
	this.xDespl = xDespl;
	this.yDespl = yDespl;
}
}

private final Image[] IMAGES = new Image[] { new Image(R.drawable.img1, 0.52f, 0.29f), new Image(R.drawable.img2, 0.54f, 0.45f),
	new Image(R.drawable.img3, 0.53f, 0.36f), new Image(R.drawable.img4, 0.48f, 0.50f) };

/*- ********************************************************************************** */
/*- *********** WALLPAPER ENGINE ************* */
/*- ********************************************************************************** */
private class BrokenWallpaperEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener {

private static final String TAG = "Broken Wallpaper";
private Paint paint= iniPaint();
private Random r = new Random();
private BitmapDrawable bitmap = null; 

private SharedPreferences prefs;
private static final String KEY_BROKEN_ENABLED = "brokenEnabled";
private static final String KEY_CLEAR_ENABLED = "clearEnabled";
private boolean brokenEnabled;
private boolean clearEnabled;

public BrokenWallpaperEngine() {
	prefs = PreferenceManager.getDefaultSharedPreferences(BrokenWallpaperService.this);
	prefs.registerOnSharedPreferenceChangeListener(this);
	onSharedPreferenceChanged(prefs, null);
//	onSharedPreferenceChanged(prefs, KEY_BROKEN_ENABLED);
//	onSharedPreferenceChanged(prefs, KEY_CLEAR_ENABLED);
}


/*- ********************************************************************************** */
/*- *********** WALLPAPER ENGINE OVERRIDE ************* */
/*- ********************************************************************************** */
@Override
public void onCreate(SurfaceHolder surfaceHolder) {
	super.onCreate(surfaceHolder);
    setTouchEventsEnabled(true);
}

@Override
public void onVisibilityChanged(boolean visible) {
	if (visible) {
		draw();
	}
}

//@Override
//public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset,
//	int yPixelOffset) {
//	Log.w(TAG, "onOffsetsChanged");
//    super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
//}	

@Override
public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	Log.w(TAG, "onSurfaceChanged");
    super.onSurfaceChanged(holder, format, width, height);
}

@Override
public void onTouchEvent(MotionEvent event) {
	if (event.getAction() == MotionEvent.ACTION_DOWN && brokenEnabled) {
		float x = event.getX();
		float y = event.getY();
		mergeAndDrawBitmap(x, y);
	} else if (event.getAction() == MotionEvent.ACTION_MOVE && clearEnabled) {
		bitmap = null;
		draw();
	}
	super.onTouchEvent(event);
}

@Override
public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	brokenEnabled = prefs.getBoolean(KEY_BROKEN_ENABLED, true);
	clearEnabled = prefs.getBoolean(KEY_CLEAR_ENABLED, true);
}

/*- ********************************************************************************** */
/*- *********** WALLPAPER ENGINE PRIVATE ************* */
/*- ********************************************************************************** */
private void draw() {
	SurfaceHolder holder = getSurfaceHolder();
	Canvas canvas = null;
	try {
		canvas = holder.lockCanvas();
		if (canvas != null) {
			if (bitmap == null) {
				bitmap = iniBitmap(canvas.getWidth(), canvas.getHeight());
			}
			canvas.drawBitmap(bitmap.getBitmap(), 0, 0, paint);
		}
	} finally {
		if (canvas != null) holder.unlockCanvasAndPost(canvas);
	}
}

private BitmapDrawable iniBitmap(int w, int h) {
	Bitmap b = Bitmap.createBitmap(w,h, Config.ARGB_8888);
	Canvas c = new Canvas(b);
	c.drawColor(Color.BLACK);	
	return new BitmapDrawable(b);
}

public void mergeAndDrawBitmap(float x, float y) {
	SurfaceHolder holder = getSurfaceHolder();
	Canvas canvas = null;
	try {
		canvas = holder.lockCanvas();
		if (canvas != null) {
			Image img = IMAGES[r.nextInt(IMAGES.length)];
			bitmap = mergeBitmap(canvas.getWidth(),canvas.getHeight(),x,y,img);
			canvas.drawBitmap(bitmap.getBitmap(), 0, 0, paint);
		}
	} finally {
		if (canvas != null) holder.unlockCanvasAndPost(canvas);
	}
}
private BitmapDrawable mergeBitmap(int w, int h, float x, float y, Image img) {
	Bitmap b = Bitmap.createBitmap(w,h, Config.ARGB_8888);
	Bitmap overlay = BitmapFactory.decodeResource(getResources(), img.id);
	x = x - img.xDespl*overlay.getWidth();
	y = y - img.yDespl*overlay.getHeight();
	Canvas c = new Canvas(b);
	c.drawBitmap(bitmap.getBitmap(), 0, 0,paint);
	c.drawBitmap(overlay, (int)x, (int)y,paint);
	return new BitmapDrawable(b);
}

private Paint iniPaint() {
	Paint ret = new Paint();
	ret.setAntiAlias(true);
	return ret;
}

}
}