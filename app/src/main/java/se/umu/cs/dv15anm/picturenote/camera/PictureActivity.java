package se.umu.cs.dv15anm.picturenote.camera;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import se.umu.cs.dv15anm.picturenote.R;

/**
 * Activity that displays a image
 */
public class PictureActivity extends AppCompatActivity {

    private static final String TAG = "PictureActivity";
    private static final String EXTRA_IMG_PATH = "image_path";
    private String mImagePath;
    private ImageView mImageView;

    /**
     * Create the activity and get the data from the intent, and then  retrieve the image
     * @param savedInstanceState The saved data from the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        mImagePath = getIntent().getExtras().getString(EXTRA_IMG_PATH);
        mImageView = (ImageView) findViewById(R.id.picture_view);

        retrieveImage();
    }

    /**
     * Retrieve the image and add it to the image view.
     */
    private void retrieveImage() {
        File imageFile = new File(mImagePath);
        int rotateImage = ImageAssists.getCameraPhotoOrientation(imageFile);

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));
            bitmap = ImageAssists.fixOrientation(bitmap, rotateImage);
            mImageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a intent designed for the activity
     * @param packageContext The context of the activity that wants the intent
     * @param imagePath The path to the image to be displayed.
     * @return The finished intent.
     */
    public static Intent newIntent(Context packageContext, String imagePath) {
        Intent intent = new Intent(packageContext, PictureActivity.class);
        intent.putExtra(EXTRA_IMG_PATH, imagePath);

        return intent;
    }
}
