package se.umu.cs.dv15anm.picturenote.camera.ocr;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;

import java.io.IOException;

import se.umu.cs.dv15anm.picturenote.camera.overlay.Overlay;

/**
 * Camera source preview showing what the camera sees
 */

public class OcrCameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "OcrCameraPreview";

    private Context mContext;
    private CameraSource mCameraSource;
    private SurfaceHolder mHolder;
    private Overlay mGraphicOverlay;
    private boolean mStarted;

    /**
     * Create the camera preview
     * @param context The context of the activity calling.
     */
    public OcrCameraPreview(Context context) {
        super(context);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mStarted = false;
    }

    /**
     * Create the camera preview
     * @param context The context of the activity calling
     * @param attributeSet The attributes for the surface
     */
    public OcrCameraPreview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mStarted = false;
    }

    /**
     * If the right requirements are met start the camera.
     */
    public void start() {
        if (mCameraSource != null && !mStarted) {
            try {
                mCameraSource.start(mHolder);
                mStarted = true;
                if (mGraphicOverlay != null) {
                    Size size = mCameraSource.getPreviewSize();
                    int min = Math.min(size.getWidth(), size.getHeight());
                    int max = Math.max(size.getWidth(), size.getHeight());
                    int orientation = mContext.getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {

                        mGraphicOverlay.setCameraSettings(min, max);
                    } else {
                        mGraphicOverlay.setCameraSettings(max, min);
                    }
                    mGraphicOverlay.clear();
                }
            } catch (SecurityException e) {
                Log.e(TAG, "Permission not granted, should have been requested at earlier stage. " +
                        e.getMessage());
            } catch (IOException e) {
                Log.w(TAG, "Issue with the preview or display: " + e.getMessage());
            }
        }
    }

    public void setCameraSource(CameraSource cameraSource) {
        mCameraSource = cameraSource;
    }

    /**
     * Set the camera source and overlay to be displayed.
     * @param cameraSource The camera source that the preview will show.
     * @param graphicOverlay The overlay that will draw upon the preview.
     */
    public void setCameraWithOverlay(CameraSource cameraSource, Overlay graphicOverlay) {
        mCameraSource = cameraSource;
        mGraphicOverlay = graphicOverlay;
    }

    /**
     * Stop the camera
     */
    public void stop() {
        if (mCameraSource != null) {
            mCameraSource.stop();
            mStarted = false;
        }
    }

    /**
     * Release the camera
     */
    public void release() {
        if (mCameraSource != null) {
            mCameraSource.release();
            mCameraSource = null;
            mStarted = false;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
