package com.example.harbon.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by harbon on 15-6-26.
 */
public class WindView extends View {
    private Bitmap mBitmap;
    private boolean canDraw = false;
    private int width;
    private int height;
    private int mBitmapWidth;
    private int mBitmapHeight;
    private int columnCount;
    private int rowCount;
    private int[][] pixelsArrayX;
    private int[][] pixelsArrayY;
    private Paint paint;
    private int blockWidth = 2;
    private int blockHeight = 2;
    private int transparentBlockCount = 0;
    private long timeStamp;
    public WindView(Context context, AttributeSet attrs) {
        super(context, attrs);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, options);
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        columnCount = mBitmapWidth / blockWidth;
        rowCount = mBitmapHeight / blockHeight;
        paint = new Paint();
        pixelsArrayX = new int[rowCount][columnCount];
        pixelsArrayY = new int[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                pixelsArrayX[i][j] = j * blockWidth;
                pixelsArrayY[i][j] = i * blockHeight;
                setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        canDraw = true;
                        invalidate();
                    }
                });
            }
        }
    }

            @Override
            protected void onDraw (Canvas canvas){
                width = getWidth();
                height = getHeight();
//        canvas.drawBitmap(mBitmap, new Rect(0, 0, 10, 10), new Rect(0, 0, 10, 10), null);
//        canvas.drawBitmap(mBitmap, new Rect(10, 10, 20, 20), new Rect(100, 100, 120, 120), null);
//                windEffect(canvas);
                if (canDraw) {
                    Log.i("aaaaaa", "duration:"+(System.currentTimeMillis() - timeStamp));
                    timeStamp = System.currentTimeMillis();

                    invalidate();
                }

            }
        private void windEffect (Canvas canvas){
            Log.i("aaaaaa", "duration:"+(System.currentTimeMillis() - timeStamp));
            timeStamp = System.currentTimeMillis();

            transparentBlockCount = 0;
                for (int i = 0; i < rowCount; i++) {
                    for (int j = 0; j < columnCount; j++) {
                        Rect sRect = new Rect(blockWidth * j, blockHeight * i, blockWidth * j + blockWidth - 1, blockHeight * i + blockHeight - 1);
                        Rect dRect = new Rect(pixelsArrayX[i][j], pixelsArrayY[i][j], pixelsArrayX[i][j] + blockWidth, pixelsArrayY[i][j] + blockHeight);
                        canvas.drawBitmap(mBitmap, sRect, dRect, paint);
//                    if (blockCanMove(i, j)) {
                        if (canDraw) {
                            pixelsArrayX[i][j] = pixelsArrayX[i][j] + (int) (8 * Math.random());
                            pixelsArrayY[i][j] = pixelsArrayY[i][j] + (int) (8 * Math.random());
                        }
//                        for (int i = blockWidth)
//                    }
                    }
                }
                if (canDraw) {
                    for (int i = 0; i < rowCount; i++) {
                        for (int j = 0; j < columnCount; j++) {
                            int random = (int) (Math.random() * 5);
                            if (random == 0) {
                                transparentBlockCount++;
                            } else {
                                random = 1;
                            }
                            int[] pixels = new int[blockHeight * blockWidth];
                            mBitmap.getPixels(pixels, 0, blockWidth, blockWidth * j, blockHeight * i, blockWidth, blockHeight);
                            for (int k = 0; k < pixels.length; k++) {
                                int red = Color.red(pixels[k]);
                                int green = Color.green(pixels[k]);
                                int blue = Color.green(pixels[k]);
                                int alpha = Color.alpha(pixels[k]) * random;
                                int color = Color.argb(alpha, red, green, blue);
                                pixels[k] = color;
                            }
                            mBitmap.setPixels(pixels, 0, blockWidth, blockWidth * j, blockHeight * i, blockWidth, blockHeight);
                        }
                    }
                }
                if (transparentBlockCount >= rowCount * columnCount) {
                    canDraw = false;
                }

        }
    }