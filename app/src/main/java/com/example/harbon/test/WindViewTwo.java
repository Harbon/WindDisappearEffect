package com.example.harbon.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by harbon on 15-6-26.
 */
public class WindViewTwo extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Bitmap mBitmapBg;
    private Bitmap mBitmapIcon;
    private boolean canDraw = false;
    private int mBitmapBgWidth;
    private int mBitmapBgHeight;
    private int mBitmapIconWidth;
    private int mBitmapIconHeight;
    private int offsetX;
    private int offsetY;
    private int columnCount;
    private int rowCount;
    private int[][] pixelsArrayX;
    private int[][] pixelsArrayY;
    private Paint paint;
    private int blockWidth = 4;
    private int blockHeight = 4;
    private boolean hasNoTransparentBlock = true;

    public WindViewTwo(Context context, AttributeSet attrs) {
        super(context, attrs);
        setZOrderOnTop(true);
        this.surfaceHolder = this.getHolder();
        this.surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        this.surfaceHolder.addCallback(this);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        mBitmapIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, options);
        mBitmapIconWidth = mBitmapIcon.getWidth();
        mBitmapIconHeight = mBitmapIcon.getHeight();
        Drawable drawable = getResources().getDrawable(R.drawable.roundbg);
        mBitmapBg = drawableToBitmap(drawable);
        mBitmapBgWidth = mBitmapBg.getWidth();
        mBitmapBgHeight = mBitmapBg.getHeight();
        offsetX = (mBitmapBgWidth - mBitmapIconWidth)/2;
        offsetY = (mBitmapBgHeight - mBitmapIconHeight)/2;
        columnCount = mBitmapBgWidth / blockWidth;
        rowCount = mBitmapBgHeight / blockHeight;
        paint = new Paint();
        pixelsArrayX = new int[rowCount][columnCount];
        pixelsArrayY = new int[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                pixelsArrayX[i][j] = j * blockWidth;
                pixelsArrayY[i][j] = i * blockHeight;

            }
        }
//        thread = new Thread(this);
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                canDraw = true;
//                thread.start();
//            }
//        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("aaaaaa", "surfaceCreated");
        canvas = surfaceHolder.lockCanvas();
        canvas.drawBitmap(mBitmapBg, 0, 0,null);
        canvas.drawBitmap(mBitmapIcon, offsetX, offsetY, null);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("aaaaaa", "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("aaaaaa", "surfaceDestroy");
    }

    @Override
    public void run() {
        hasNoTransparentBlock = true;
        while (canDraw) {
            canvas = this.surfaceHolder.lockCanvas();
            try {
                synchronized(surfaceHolder) {
                    if (canvas == null)
                        return;
                    windEffect(canvas);
                }
            }finally {
                if (canvas == null)
                    return;
                this.surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
    private void windEffect (Canvas canvas){
        hasNoTransparentBlock = true;
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                Rect sRect = new Rect(blockWidth * j, blockHeight * i, blockWidth * j + blockWidth - 1, blockHeight * i + blockHeight - 1);
                Rect dRect = new Rect(pixelsArrayX[i][j], pixelsArrayY[i][j], pixelsArrayX[i][j] + blockWidth, pixelsArrayY[i][j] + blockHeight);
                canvas.drawBitmap(mBitmapBg, sRect, dRect, paint);
                if (canDraw) {
                    pixelsArrayX[i][j] = pixelsArrayX[i][j] + (int) (15 * Math.random());
                    pixelsArrayY[i][j] = pixelsArrayY[i][j] - (int) (15 * Math.random());
                }
//
            }
        }
        canvas.drawBitmap(mBitmapIcon, offsetX, offsetY, null);
        if (canDraw) {
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    int random = (int) (Math.random() * 2);
                    if (random != 0) {
                        random = 1;
                    }
                    int[] pixels = new int[blockHeight * blockWidth];
                    mBitmapBg.getPixels(pixels, 0, blockWidth, blockWidth * j, blockHeight * i, blockWidth, blockHeight);
                    for (int k = 0; k < pixels.length; k++) {
                        int red = Color.red(pixels[k]);
                        int green = Color.green(pixels[k]);
                        int blue = Color.green(pixels[k]);
                        if (Color.alpha(pixels[k]) > 0) {
                            hasNoTransparentBlock = false;
                        }
                        int alpha = Color.alpha(pixels[k]) * random;
                        int color = Color.argb(alpha, red, green, blue);
                        pixels[k] = color;
                    }
                    mBitmapBg.setPixels(pixels, 0, blockWidth, blockWidth * j, blockHeight * i, blockWidth, blockHeight);
                }
            }
        }
        if (hasNoTransparentBlock) {
            canDraw = false;
        }

    }
    public void startWind(){
        Thread thread = new Thread(this);
        canDraw = true;
        thread.start();
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
