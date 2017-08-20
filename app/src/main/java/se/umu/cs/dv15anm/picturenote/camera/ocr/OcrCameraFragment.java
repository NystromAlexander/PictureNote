package se.umu.cs.dv15anm.picturenote.camera.ocr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import se.umu.cs.dv15anm.picturenote.NoteActivity;
import se.umu.cs.dv15anm.picturenote.NoteListActivity;
import se.umu.cs.dv15anm.picturenote.R;
import se.umu.cs.dv15anm.picturenote.camera.CameraActivity;
import se.umu.cs.dv15anm.picturenote.camera.overlay.Overlay;
import se.umu.cs.dv15anm.picturenote.helpers.ImageOcr;
import se.umu.cs.dv15anm.picturenote.helpers.NoteType;

/**
 * Fragment hosting the camera source to capture and get text from the image.
 */
public class OcrCameraFragment extends Fragment {

    private static final String TAG = "OcrCameraFragment";
    private static final String ARG_NOTE_TYPE = "note_type";
    private static final String SAVED_TEXT_LIST = "text_list";
    private static final String SAVED_IMAGE_PATH = "image_paths";
    private static final String SAVED_PICTURES_TAKEN = "pictures_taken";

    private CameraSource mCameraSource;
    private OcrCameraPreview mPreview;
    private Overlay mGraphicOverlay;
    private FrameLayout mPreviewFrame;
    private NoteType mNoteType;
    private ArrayList<String> mTexts;
    private ArrayList<String> mImagePaths;
    private int mPicturesTaken;


    public OcrCameraFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment with the note type as an argument.
     * @param noteType The type of note to be captured
     * @return Instance of OcrCameraFragment.
     */
    public static OcrCameraFragment newInstance(NoteType noteType) {
        OcrCameraFragment fragment = new OcrCameraFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_TYPE, noteType);
        fragment.setArguments(args);

        return fragment;
    }


    /**
     * Create the fragment and if there is a savedInstanceState retrieve the saved data.
     * @param savedInstanceState Contains the save data from the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPicturesTaken = 0;
        if (getArguments() != null) {
            mNoteType = (NoteType) getArguments().getSerializable(ARG_NOTE_TYPE);
            mTexts = new ArrayList<>();
            mImagePaths = new ArrayList<>();
        }

        if (savedInstanceState != null) {
            mTexts = savedInstanceState.getStringArrayList(SAVED_TEXT_LIST);
            mImagePaths = savedInstanceState.getStringArrayList(SAVED_IMAGE_PATH);
            mPicturesTaken = savedInstanceState.getInt(SAVED_PICTURES_TAKEN);
        }

    }

    /**
     * Create the UI and give the user a hint with a snackBar if the note being captured
     * is a recipe
     * @param inflater Used to inflate the UI elements.
     * @param container The container that will host the fragment.
     * @param savedInstanceState Contains the saved data of the fragment
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ocr_camera, container, false);
        mPreview = new OcrCameraPreview(getActivity());
        mGraphicOverlay = (Overlay) view.findViewById(R.id.graphic_overlay);
        mPreviewFrame = (FrameLayout) view.findViewById(R.id.preview_frame);

        if (havePermission()) {
            createCameraSource();
            mPreview.setCameraWithOverlay(mCameraSource, mGraphicOverlay);
        }

        Button capture = (Button) view.findViewById(R.id.ocr_capture_button);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraSource.takePicture(null, mPictureCallback);

            }
        });

        if (mNoteType == NoteType.RECIPE && mPicturesTaken == 0) {
            Snackbar.make(mGraphicOverlay, "Take picture of the recipe",
                    Snackbar.LENGTH_LONG)
                    .show();
        } else if (mNoteType == NoteType.RECIPE && mPicturesTaken > 0) {
            Snackbar.make(mPreview, "Take picture of ingredients",
                    Snackbar.LENGTH_LONG)
                    .show();
        }

        return view;
    }

    /**
     * Start a NoteActivity with a NoteFragment to display the captured note.
     */
    private void startNoteActivity() {
        Intent intent = NoteActivity.newIntent(getActivity(),mTexts, mImagePaths, NoteType.STANDARD);
        startActivity(intent);
    }

    /**
     * Start a NoteActivity with a RecipeFragment to display the captured recipe.
     */
    private void startRecipeActivity() {
        Log.d(TAG, "list sizes: "+mTexts.size()+" "+mImagePaths.size());
        Intent intent = NoteActivity.newIntent(getActivity(),mTexts,mImagePaths,NoteType.RECIPE);
        startActivity(intent);
    }

    /**
     * Save the captured texts, image paths and how many pictures have been captured.
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVED_TEXT_LIST, mTexts);
        outState.putStringArrayList(SAVED_IMAGE_PATH, mImagePaths);
        outState.putInt(SAVED_PICTURES_TAKEN, mPicturesTaken);
    }

    /**
     * When the fragment is resumed add the preview to the active frame.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mPreview != null) {
            mPreviewFrame.addView(mPreview, 0);
        }
    }

    /**
     * When the fragment is paused stop the camera preview and remove it from the active frame.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
            mPreviewFrame.removeView(mPreview);
        }
    }

    /**
     * When the fragment is destroyed release the preview.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    /**
     * Check if the app has permission to use the camera.
     * @return true if the application has permission else false.
     */
    private boolean havePermission() {
        int rc = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        Log.d(TAG, "Don't have camera permission");
        return false;
    }

    /**
     * Create the camera source and the text recognizer with the processor to actively detect text.
     */
   private void createCameraSource(){
       TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity()).build();
        textRecognizer.setProcessor(new OcrProcessor(mGraphicOverlay));
       if (!textRecognizer.isOperational()) {
           Log.w(TAG, "Detector not ready.");
       }

       mCameraSource = new CameraSource.Builder(getActivity(),textRecognizer)
               .setFacing(CameraSource.CAMERA_FACING_BACK)
               .setRequestedPreviewSize(1280, 1024)
               .setAutoFocusEnabled(true)
               .build();
   }


    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), NoteListActivity.APP_NAME);

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }
        String fileName = "IMG_";
        if (mNoteType == NoteType.STANDARD) {
            fileName = "IMG_NOTE_";
        } else if (mNoteType == NoteType.RECIPE) {
            fileName = "IMG_RECIPE_";
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == CameraActivity.MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    fileName+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * Callback to handle the captured image, the image get's processed and the text is extracted
     * from the image and the corresponding activity is started.
     */
    CameraSource.PictureCallback mPictureCallback = new CameraSource.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes) {
            File pictureFile = getOutputMediaFile(CameraActivity.MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(bytes);
                fos.close();
                mPicturesTaken++;
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
            Bitmap image = createBitmap(bytes);
            mTexts.add(ImageOcr.findTextFromImage(image, getActivity()));
            mImagePaths.add(pictureFile.getAbsolutePath());
            if (mNoteType == NoteType.STANDARD) {
                startNoteActivity();
            } else if (mNoteType == NoteType.RECIPE && mPicturesTaken == 2) {
                startRecipeActivity();
            } else { //For recipe the ingredients are captured after the recipe it self has been.
                Snackbar.make(mPreview, "Take picture of ingredients",
                        Snackbar.LENGTH_LONG)
                        .show();
            }


        }

    };

    /**
     * Create a bitmap from the byte array returned from the camera source.
     * @param imageBytes The image returned from the camera source
     * @return finished bitmap with the correct rotation if rotation required.
     */
    private Bitmap createBitmap(byte[] imageBytes) {
        Bitmap picture = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Size previewSize = mCameraSource.getPreviewSize();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture,previewSize.getWidth(),
                    previewSize.getHeight(),true);
            picture = Bitmap.createBitmap(scaledBitmap,0,0,scaledBitmap.getWidth(),
                    scaledBitmap.getHeight(),matrix,true);
        }


        return picture;
    }



}
