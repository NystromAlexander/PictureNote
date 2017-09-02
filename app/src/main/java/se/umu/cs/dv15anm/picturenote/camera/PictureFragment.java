package se.umu.cs.dv15anm.picturenote.camera;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import se.umu.cs.dv15anm.picturenote.R;
import se.umu.cs.dv15anm.picturenote.helpers.ImageAssists;

/**
 * Fragment displaying a picture.
 */
public class PictureFragment extends Fragment {

    private static final String ARGS_IMAGE_PATH = "image_path";
    private static final String ARGS_IMAGE = "image";
    private static final String TAG = "PictureFragment";

    private String mImagePath;
    private ImageView mImageView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment
     */
    public PictureFragment() {

    }


    /**
     * Set up the UI and set the image.
     * @param inflater Used to inflate the UI elements.
     * @param container The container that will host the fragment.
     * @param savedInstanceState Contains the saved data of the fragment
     * @return The finished UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        mImageView = (ImageView) view.findViewById(R.id.picture_view);

        if (getArguments().containsKey(ARGS_IMAGE_PATH)) {
            mImagePath = getArguments().getString(ARGS_IMAGE_PATH);
            retrieveImage();
        } else if (getArguments().containsKey(ARGS_IMAGE)) {
            byte[] bytes = getArguments().getByteArray(ARGS_IMAGE);
            Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            mImageView.setImageBitmap(image);
        }


        return view;
    }

    /**
     * Retrieve the image from the file path and add it to the image view.
     */
    private void retrieveImage() {
        File imageFile = new File(mImagePath);
        int rotateImage = ImageAssists.getCameraPhotoOrientation(imageFile);

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));
            bitmap = ImageAssists.fixOrientation(bitmap, rotateImage);
            mImageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            Log.w(TAG, "Could not load image file: "+e.getMessage());
            Toast.makeText(getActivity(),"Could not load image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get a instance of this fragment with the image path as an argument.
     * @param imagePath The image path that will be passed as an argument.
     * @return Instance of PictureFragment with argument.
     */
    public static PictureFragment newInstance(String imagePath) {
        PictureFragment fragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_IMAGE_PATH, imagePath);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Get an instance of ths fragment with an image as a byte array as argument-
     * @param image The image that will be passed as an argument
     * @return Instance of PictureFragment with argument.
     */
    public static PictureFragment newInstance(byte[] image) {
        PictureFragment fragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putByteArray(ARGS_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

}
