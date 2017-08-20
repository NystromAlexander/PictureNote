package se.umu.cs.dv15anm.picturenote.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

/**
 * The preview showing what the camera sees.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Context mContext;

    /**
     * Create the camera preview
     * @param context The context of the calling activity
     * @param camera The camera that will be displayed in the preview
     */
    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    /**
     * When the surface is created start the preview
     * @param holder The view holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        } catch (java.io.IOException e) {
            Log.w(TAG, "Error when setting camera preview: "+e.getMessage());
        }
    }

    /**
     * When the surface changes, either by beeing rotated or other action causing it to change.
     * Fix the preview so that it matches the rotation.
     * @param holder The view holder
     * @param format The format of the view
     * @param width The width of the view
     * @param height The height of the view
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (mHolder.getSurface() == null) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.w(TAG, "Tried to stop a non-existing preview.");
        }


        Camera.Parameters parameters = mCamera.getParameters();
        Display display = ((WindowManager)mContext.getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();

        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        mCamera.setParameters(parameters);

        if(display.getRotation() == Surface.ROTATION_0)
        {
            Log.d(TAG, "rotation 0");
            parameters.setPreviewSize(height, width);
            mCamera.setDisplayOrientation(90);
        }

        if(display.getRotation() == Surface.ROTATION_90)
        {
            Log.d(TAG, "rotation 90");
            parameters.setPreviewSize(width, height);

        }

        if(display.getRotation() == Surface.ROTATION_180)
        {
            Log.d(TAG, "rotation 180");
            parameters.setPreviewSize(height, width);
        }

        if(display.getRotation() == Surface.ROTATION_270)
        {
            Log.d(TAG, "rotation 270");
            parameters.setPreviewSize(width, height);
            mCamera.setDisplayOrientation(180);
        }

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (java.io.IOException e) {
            Log.d(TAG, "Error when setting camera preview: "+e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
