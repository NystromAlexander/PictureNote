package se.umu.cs.dv15anm.picturenote.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import se.umu.cs.dv15anm.picturenote.NoteListActivity;
import se.umu.cs.dv15anm.picturenote.R;

/**
 * Activity using the camera api to take picture and return the path to the take picture.
 */
public class CameraActivity extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final String TAG = "CameraActivity";
    private static final String IMAGE_PATH = "image_path";

    private Camera mCamera;
    private CameraPreview mPreview;
    FrameLayout mPreviewFrame;

    /**
     * Create the activity and setup the camera
     * @param savedInstanceState the save data from the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        mPreviewFrame = (FrameLayout) findViewById(R.id.preview_frame);
        if (rc == PackageManager.PERMISSION_GRANTED && checkCamera(this)) {
            setupCamera();
        }
        Button mCaptureButton = (Button) findViewById(R.id.capture_button);
        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null,null,mPicture);
            }
        });

    }

    /**
     * When the activity is resumed either start the camera or create it.
     */
    @Override
    protected void onResume() {

        super.onResume();
        if (mCamera == null) {
            setupCamera();
        }
        startCamera();
    }

    /**
     * When the activity is paused stop the preview and release the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            mPreview = null;
        }
    }

    /**
     * Check if the phone has a camera
     * @param context The activity
     * @return True if the phone has a camera else false.
     */
    private boolean checkCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * If there is no camera active open a new camera and start it.
     */
    private void setupCamera() {
        if (mCamera != null) {
            return;
        }
        try {
            mCamera = Camera.open();

            startCamera();
        } catch (Exception e) {
            Log.e(TAG, "Error opening camera:" + e.getMessage(), e);
        }
    }

    /**
     * Start the camera and the preview.
     */
    private void startCamera() {
        if (mCamera != null) {
            mPreview = new CameraPreview(this, mCamera);
            mPreviewFrame.removeAllViews();
            mPreviewFrame.addView(mPreview);
        }
    }

    /**
     * Callback method for handling the captured image.
     */
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                setResult(pictureFile.getAbsolutePath());
                finish();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    /**
     * Add the path to the captured image to the result.
     * @param imagePath Path to the captured image.
     */
    private void setResult(String imagePath) {
        Intent data = new Intent();
        data.putExtra(IMAGE_PATH, imagePath);

        setResult(RESULT_OK, data);
    }

    /**
     * Method for retrieving the image path.
     * @param result Result return from this activity
     * @return The path to the captured image
     */
    public static final String getCapturedPicture(Intent result) {
        return result.getExtras().getString(IMAGE_PATH);
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), NoteListActivity.APP_NAME);

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_FOOD_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

}
