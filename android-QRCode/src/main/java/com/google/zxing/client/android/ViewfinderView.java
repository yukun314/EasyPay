/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 * 该视图是覆盖在相机的预览视图之上的一层视图。扫描区构成原理，其实是在预览视图上画四块遮罩层，
 * 中间留下的部分保持透明，并画上一条激光线，实际上该线条就是展示而已，与扫描功能没有任何关系。
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

  private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
  private static final long ANIMATION_DELAY = 80L;
  private static final int CURRENT_POINT_OPACITY = 0xA0;
  private static final int MAX_RESULT_POINTS = 20;
  private static final int POINT_SIZE = 6;

  private CameraManager cameraManager;
  private final Paint paint;
  private Bitmap resultBitmap;
  private final int maskColor;
  private final int resultColor;
  private final int frameColor1;
  private final int frameColor2;
  private final int laserColor;
  private final int resultPointColor;
  private final int cornerColor;
  private int scannerAlpha;
  private List<ResultPoint> possibleResultPoints;
  private List<ResultPoint> lastPossibleResultPoints;

  // This constructor is used when the class is built from an XML resource.
  public ViewfinderView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Initialize these once for performance rather than calling them every time in onDraw().
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Resources resources = getResources();
    maskColor = resources.getColor(R.color.viewfinder_mask);
    resultColor = resources.getColor(R.color.result_view);
    frameColor1 = resources.getColor(R.color.viewfinder_frame1);
    frameColor2 = resources.getColor(R.color.viewfinder_frame2);
    laserColor = resources.getColor(R.color.viewfinder_laser);
    resultPointColor = resources.getColor(R.color.possible_result_points);
    cornerColor = resources.getColor(R.color.corner);
    scannerAlpha = 0;
    possibleResultPoints = new ArrayList<>(5);
    lastPossibleResultPoints = null;
  }

  public void setCameraManager(CameraManager cameraManager) {
    this.cameraManager = cameraManager;
  }

  private float scanLineY = 0;
  @SuppressLint("DrawAllocation")
  @Override
  public void onDraw(Canvas canvas) {
    if (cameraManager == null) {
      return; // not ready yet, early draw before done configuring
    }
    //frame 扫描的区域
    Rect frame = cameraManager.getFramingRect();
    Rect previewFrame = cameraManager.getFramingRectInPreview();    
    if (frame == null || previewFrame == null) {
      return;
    }
    int width = canvas.getWidth();
    int height = canvas.getHeight();

    // Draw the exterior (i.e. outside the framing rect) darkened
    paint.setColor(resultBitmap != null ? resultColor : maskColor);
    //上部分
    canvas.drawRect(0, 0, width, frame.top - 2, paint);
    //左部分
    canvas.drawRect(0, frame.top - 2, frame.left - 2, frame.bottom + 3, paint);
    //右部分
    canvas.drawRect(frame.right + 3, frame.top - 2, width, frame.bottom + 3, paint);
    //下部分
    canvas.drawRect(0, frame.bottom + 3, width, height, paint);

    //边框部分
    paint.setColor(frameColor1);
    canvas.drawRect(frame.left - 2, frame.top - 2,frame.left - 1,frame.bottom + 2,paint);//左
    canvas.drawRect(frame.left - 2, frame.top - 2,frame.right + 2,frame.top - 1,paint);//上
    canvas.drawRect(frame.right + 1, frame.top - 2,frame.right + 2,frame.bottom + 2,paint);//右
    canvas.drawRect(frame.left - 2, frame.bottom + 1,frame.right + 2,frame.bottom + 2,paint);//下
    paint.setColor(frameColor2);
    canvas.drawRect(frame.left - 1, frame.top - 1, frame.left, frame.bottom + 1, paint);//左
    canvas.drawRect(frame.left - 1, frame.top - 1, frame.right + 1, frame.top, paint);//上
    canvas.drawRect(frame.right, frame.top - 1, frame.right + 1, frame.bottom + 1, paint);//右
    canvas.drawRect(frame.left - 1, frame.bottom, frame.right + 1, frame.bottom + 1, paint);//下

    //四个角
    paint.setColor(cornerColor);
    int l = frame.width()/12;
    int w = l/3;
    //左上角
    canvas.drawRect(frame.left - w, frame.top - w, frame.left + l, frame.top , paint);
    canvas.drawRect(frame.left - w, frame.top - w, frame.left , frame.top + l, paint);
    //右上角
    canvas.drawRect(frame.right - l, frame.top, frame.right + w, frame.top - w, paint);
    canvas.drawRect(frame.right, frame.top + l, frame.right + w, frame.top - w, paint);
    //左下角
    canvas.drawRect(frame.left - w, frame.bottom + w, frame.left, frame.bottom - l, paint);
    canvas.drawRect(frame.left - w, frame.bottom + w, frame.left + l, frame.bottom, paint);
    //右下角
    canvas.drawRect(frame.right , frame.bottom - l, frame.right + w, frame.bottom + w , paint);
    canvas.drawRect(frame.right - l , frame.bottom , frame.right + w, frame.bottom + w , paint);



    if (resultBitmap != null) {
      // Draw the opaque result bitmap over the scanning rectangle
      paint.setAlpha(CURRENT_POINT_OPACITY);
      canvas.drawBitmap(resultBitmap, null, frame, paint);
    } else {
      // Draw a red "laser scanner" line through the middle to show decoding is active
      paint.setColor(laserColor);
//      paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
//      scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//      int middle = frame.height() / 2 + frame.top;
//      canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);
      if(scanLineY <= frame.top) {
        scanLineY = frame.top;
      }
      scanLineY+=8;
      if(scanLineY >= frame.bottom) {
        scanLineY = frame.top;
      }
      canvas.drawRect(frame.left + 2, scanLineY, frame.right - 2, scanLineY + 1, paint);
      
      float scaleX = frame.width() / (float) previewFrame.width();
      float scaleY = frame.height() / (float) previewFrame.height();

      List<ResultPoint> currentPossible = possibleResultPoints;
      List<ResultPoint> currentLast = lastPossibleResultPoints;
      int frameLeft = frame.left;
      int frameTop = frame.top;
      if (currentPossible.isEmpty()) {
        lastPossibleResultPoints = null;
      } else {
        possibleResultPoints = new ArrayList<>(5);
        lastPossibleResultPoints = currentPossible;
        paint.setAlpha(CURRENT_POINT_OPACITY);
        paint.setColor(resultPointColor);
        synchronized (currentPossible) {
          for (ResultPoint point : currentPossible) {
            canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                              frameTop + (int) (point.getY() * scaleY),
                              POINT_SIZE, paint);
          }
        }
      }
      if (currentLast != null) {
        paint.setAlpha(CURRENT_POINT_OPACITY / 2);
        paint.setColor(resultPointColor);
        synchronized (currentLast) {
          float radius = POINT_SIZE / 2.0f;
          for (ResultPoint point : currentLast) {
            canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                              frameTop + (int) (point.getY() * scaleY),
                              radius, paint);
          }
        }
      }

      // Request another update at the animation interval, but only repaint the laser line,
      // not the entire viewfinder mask.
      postInvalidateDelayed(ANIMATION_DELAY,
                            frame.left - POINT_SIZE,
                            frame.top - POINT_SIZE,
                            frame.right + POINT_SIZE,
                            frame.bottom + POINT_SIZE);
    }
  }

  public void drawViewfinder() {
    Bitmap resultBitmap = this.resultBitmap;
    this.resultBitmap = null;
    if (resultBitmap != null) {
      resultBitmap.recycle();
    }
    invalidate();
  }

  /**
   * Draw a bitmap with the result points highlighted instead of the live scanning display.
   *
   * @param barcode An image of the decoded barcode.
   */
  public void drawResultBitmap(Bitmap barcode) {
    resultBitmap = barcode;
    invalidate();
  }

  public void addPossibleResultPoint(ResultPoint point) {
    List<ResultPoint> points = possibleResultPoints;
    synchronized (points) {
      points.add(point);
      int size = points.size();
      if (size > MAX_RESULT_POINTS) {
        // trim it
        points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
      }
    }
  }

}
