package se.umu.cs.dv15anm.picturenote.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import se.umu.cs.dv15anm.picturenote.R;

/**
 * Pager activity to display several picture fragments.
 */
public class PicturePagerActivity extends AppCompatActivity {

    private static final String EXTRA_IMAGE_PATHS = "image_paths";

    private ViewPager mViewPager;
    private ArrayList<String> mImagePaths;

    /**
     * Create the activity and get the list with the image paths from the intent, and set the pager
     * adapter to display the images in a PictureFragment.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_pager);
        mImagePaths = getIntent().getStringArrayListExtra(EXTRA_IMAGE_PATHS);
        mViewPager = (ViewPager) findViewById(R.id.picture_view_pager);


        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public int getCount() {
                return mImagePaths.size();
            }

            @Override
            public Fragment getItem(int position) {
                return PictureFragment.newInstance(mImagePaths.get(position));
            }
        });

        mViewPager.setCurrentItem(0);

    }

    /**
     * Create an intent designed to start this activity
     * @param packageContext The context of the activity that is calling the method.
     * @param imagePaths The image paths that shall be displayed in the fragments
     * @return The finished intent.
     */
    public static Intent newIntent(Context packageContext, ArrayList<String> imagePaths) {
        Intent intent = new Intent(packageContext, PicturePagerActivity.class);
        intent.putStringArrayListExtra(EXTRA_IMAGE_PATHS, imagePaths);

        return intent;
    }
}
