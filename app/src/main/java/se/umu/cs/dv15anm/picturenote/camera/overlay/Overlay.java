package se.umu.cs.dv15anm.picturenote.camera.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * View to draw a overlay over a camera preview.
 */

public class Overlay extends View {

    private  float mWidthScaleFactor = 1.0f;
    private  float mHeightScaleFactor = 1.0f;

    private final Object mLock = new Object();
    private int mCameraPreviewHeight;
    private int mCameraPreviewWidth;
    private List<OverlayGraphic> mGraphics = new ArrayList<>();

    /**
     * Create a new overlay
     * @param context context of the calling activity
     */
    public Overlay(Context context) {
        super(context);
    }

    /**
     * Create a new overlay
     * @param context context of the calling activity.
     * @param attributeSet the attributes for the view.
     */
    public Overlay(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /**
     * Add a graphic to be drawn on the overlay
     * @param graphic Graphic to be drawn.
     */
    public void add(OverlayGraphic graphic) {
        synchronized (mLock){
            mGraphics.add(graphic);
        }
        postInvalidate();
    }

    /**
     * Remove all the graphics in the overlay.
     */
    public void clear() {
        synchronized (mLock) {
            mGraphics.clear();
        }
        postInvalidate();
    }

    /**
     *
     * @return the width scale factor.
     */
    public float getWidthScaleFactor() {
        synchronized (mLock) {
            return mWidthScaleFactor;
        }
    }

    /**
     *
     * @return the height scale factor
     */
    public float getHeightScaleFactor() {
        synchronized (mLock) {
            return mHeightScaleFactor;
        }
    }

    /**
     * Set the settings of the camera preview that the overlay will cover.
     * @param width The width of the camera preview
     * @param height The height of the camera preview
     */
    public void setCameraSettings(int width, int height) {
        synchronized (mLock) {
            mCameraPreviewHeight = height;
            mCameraPreviewWidth = width;
        }
        postInvalidate();
    }

    /**
     * Call each graphic to draw when the view is getting drawn.
     * @param canvas The canvas where the graphics get drawn.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        synchronized (mLock) {
            if ((mCameraPreviewWidth != 0) && (mCameraPreviewHeight != 0)) {
                mWidthScaleFactor = (float) canvas.getWidth() / (float) mCameraPreviewWidth;
                mHeightScaleFactor = (float) canvas.getHeight() / (float) mCameraPreviewHeight;
            }

            for (OverlayGraphic graphic: mGraphics) {
                graphic.draw(canvas);
            }
        }
    }
}
