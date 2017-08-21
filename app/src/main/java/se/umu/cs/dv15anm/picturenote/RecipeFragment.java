package se.umu.cs.dv15anm.picturenote;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

import se.umu.cs.dv15anm.picturenote.camera.CameraActivity;
import se.umu.cs.dv15anm.picturenote.helpers.ImageAssists;
import se.umu.cs.dv15anm.picturenote.camera.PicturePagerActivity;
import se.umu.cs.dv15anm.picturenote.database.NoteBoard;
import se.umu.cs.dv15anm.picturenote.helpers.DialogCreator;
import se.umu.cs.dv15anm.picturenote.models.Recipe;

/**
 * Fragment to display a recipe.
 */
public class RecipeFragment extends Fragment {

    public static final int RECIPE = 0;
    public static final int INGREDIENTS = 1;

    private static final String TAG = "RecipeFragment";
    private static final String ARG_RECIPE_ID = "recipe_id";
    private static final String ARG_RECIPE_TEXT = "recipe_text";
    private static final String ARG_RECIPE_IMG_PATH = "recipe_img_uri";
    private static final String SAVED_PATH = "saved_img_path";
    private static final int SELECT_IMAGE = 1;
    private static final int TAKE_PICTURE = 2;

    private Recipe mRecipe;
    private EditText mTitleField;
    private EditText mRecipeField;
    private EditText mIngredientsField;
    private ImageButton mFoodImage;
    private TextView mNoImageText;
    private String mFoodImagePath;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment
     */
    public RecipeFragment() {
    }

    /**
     * Create a new instance of the fragment with the database id for the recipe.
     * @param recipeId The database id for the recipe.
     * @return The new fragment.
     */
    public static RecipeFragment newInstance(long recipeId) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a new instance of the fragment.
     * @param recipeTexts The texts retrieved from the images.
     * @param imagePaths The images from which the text was extracted.
     * @return The new fragment.
     */
    public static RecipeFragment newInstance(ArrayList<String> recipeTexts,
                                             ArrayList<String> imagePaths) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_RECIPE_TEXT, recipeTexts);
        args.putStringArrayList(ARG_RECIPE_IMG_PATH, imagePaths);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Initialize the fragment and get the arguments.
     * @param savedInstanceState The saved data from the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {

            if (getArguments().containsKey(ARG_RECIPE_ID)) {
                long id = getArguments().getLong(ARG_RECIPE_ID);
                mRecipe = NoteBoard.getNoteBoard(getActivity()).getRecipe(id);
            } else if (getArguments().containsKey(ARG_RECIPE_TEXT) &&
                    getArguments().containsKey(ARG_RECIPE_IMG_PATH)) {
                mRecipe = new Recipe();
                ArrayList<String> recipeTexts = getArguments().getStringArrayList(ARG_RECIPE_TEXT);
                ArrayList<String> imagePaths = getArguments()
                        .getStringArrayList(ARG_RECIPE_IMG_PATH);
                //The recipe will be added first to the list, ingredients will be added second
                mRecipe.setText(recipeTexts.get(RECIPE));
                mRecipe.setIngredients(recipeTexts.get(INGREDIENTS));
                mRecipe.setNoteImage(imagePaths.get(RECIPE));
                mRecipe.setIngredientsImg(imagePaths.get(INGREDIENTS));
            }
        } else {
            mRecipe = new Recipe();
        }
    }

    /**
     * Create and initialize the UI.
     * @param inflater The LayoutInflater to inflate the views.
     * @param container The parent that the fragment view will be attached to.
     * @param savedInstanceState The saved data from the fragment
     * @return The view with the fragments UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        mNoImageText = (TextView) view.findViewById(R.id.no_image_text);
        mTitleField = (EditText) view.findViewById(R.id.recipe_title);
        mTitleField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mRecipeField = (EditText) view.findViewById(R.id.recipe_text);
        mIngredientsField = (EditText) view.findViewById(R.id.ingredients_text);
        initializeTextListeners();

        mFoodImage = (ImageButton) view.findViewById(R.id.food_image);
        //Let the user choose image to represent the recipe.
        mFoodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFoodImage.getDrawable() == null) {
                    DialogCreator.showImageSelectDialog(getActivity(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, SELECT_IMAGE);
                                    break;
                                case 1:
                                    Intent cameraIntent = new Intent(getActivity(),
                                            CameraActivity.class);
                                    startActivityForResult(cameraIntent,TAKE_PICTURE);
                                    break;
                            }
                        }
                    });
                } else {
                    ArrayList<String> image = new ArrayList<>();
                    image.add(mRecipe.getFoodImage());
                    Intent intent = PicturePagerActivity.newIntent(getActivity(),image);
                    startActivity(intent);
                }

            }
        });

        if (savedInstanceState != null) {
            Log.d(TAG, "getting saved path");
            mFoodImagePath = savedInstanceState.getString(SAVED_PATH);
        }

        if (mRecipe != null) {
            setUpExistingRecipe();
        }

        return view;
    }

    /**
     * Sets up the text fields if the recipe already has text, and if there is a showing the
     * result of the recipe load that image.
     * If the note has the default title the text will be marked to make the title change easier.
     */
    private void setUpExistingRecipe() {
        String title = mRecipe.getTitle();
        if (!title.isEmpty()) {
            mTitleField.setText(title);
            if (title.equals(getResources().getString(R.string.default_title))) {
                mTitleField.setText("");

            }
        }
        mRecipeField.setText(mRecipe.getText());
        mIngredientsField.setText(mRecipe.getIngredients());
        if (mRecipe.getFoodImage() != null || mFoodImagePath != null) {
            loadImage();
        }
    }

    /**
     * When the fragment is paused save the data to the database.
     */
    @Override
    public void onPause() {
        Log.d(TAG, "pause");
        super.onPause();
        saveToDb();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_PATH, mFoodImagePath);
    }

    /**
     * Create the menu on the actionbar
     * @param menu The menu where the items will be placed
     * @param inflater The inflater that will inflate the UI.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_recipe, menu);
    }

    /**
     * Handles the return calls when an item is selected in the options menu.
     *
     * If the menu item view_picture is selected, a new activity will be started where the images
     * related to the recipe will be shown.
     *
     * @param item The selected menu item.
     * @return true if handled else false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_pictures:
                ArrayList<String> images = new ArrayList<>();
                images.add(mRecipe.getRecipeImg());
                images.add(mRecipe.getIngredientsImg());
                Intent intent = PicturePagerActivity.newIntent(getActivity(), images);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Handles the result from the gallery or camera activity, by loading the image.
     * @param requestCode The code sent to the activity.
     * @param resultCode The code for the result returned from the activity.
     * @param data The data returned from the activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_IMAGE:
                    final Uri imageUri = data.getData();
                    mFoodImagePath = ImageAssists.getRealPathFromUri(getActivity(), imageUri);
                    mRecipe.setFoodImage(mFoodImagePath);
                    saveToDb();
                    loadImage();

                    break;
                case TAKE_PICTURE:
                    mFoodImagePath = CameraActivity.getCapturedPicture(data);
                    mRecipe.setFoodImage(mFoodImagePath);
                    saveToDb();
                    loadImage();
                    break;
            }

        }
    }

    /**
     * Load the image showing the result of the recipe.
     */
    private void loadImage() {
        File imageFile;
        if (mFoodImagePath != null) {
            imageFile = new File(mFoodImagePath);
        } else {
            imageFile = new File(mRecipe.getFoodImage());
        }

        int rotateImage = ImageAssists.getCameraPhotoOrientation(imageFile);

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));
            bitmap = ImageAssists.fixOrientation(bitmap, rotateImage);
            mFoodImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
                    mFoodImage.getLayoutParams().width, mFoodImage.getLayoutParams().height,
                    false));
            bitmap.recycle();
            //Hide the text saying that there is no image.
            mNoImageText.setVisibility(View.INVISIBLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the recipe to the database.
     */
    private void saveToDb(){
        mRecipe.setDate(new Date());
        if (mRecipe.getTitle().isEmpty()) {
            String title = getResources().getString(R.string.default_title);
            mRecipe.setTitle(title);
        }
        if (mRecipe.getId() != 0) {
            NoteBoard.getNoteBoard(getActivity()).updateRecipe(mRecipe);
        } else {
            NoteBoard.getNoteBoard(getActivity()).addRecipe(mRecipe);
        }
    }

    /**
     * Initialize the text changed listeners for the text fields.
     */
    private void initializeTextListeners() {
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecipe.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mRecipeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecipe.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mIngredientsField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecipe.setIngredients(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

}
