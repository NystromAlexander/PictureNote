package se.umu.cs.dv15anm.picturenote;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;

import se.umu.cs.dv15anm.picturenote.helpers.NoteType;
import se.umu.cs.dv15anm.picturenote.helpers.SingleFragmentActivity;

/**
 * Activity to host a note or recipe fragment.
 */
public class NoteActivity extends SingleFragmentActivity {

    private static final String TAG = "NoteActivity";

    private static final String EXTRA_NOTE_TEXT = "note_text";
    private static final String EXTRA_NOTE_TEXT_LIST = "note_text_list";
    private static final String EXTRA_NOTE_ID = "note_id";
    private static final String EXTRA_NOTE_IMG_URI = "note_img_uri";
    private static final String EXTRA_NOTE_IMG_LIST = "ntoe_img_path_list";
    private static final String EXTRA_NOTE_TYPE = "note_type";

    /**
     *
     * @return A NoteFragment or RecipeFragment, depending on NoteType sent with the intent.
     */
    @Override
    protected Fragment createFragment() {
        NoteType type = (NoteType) getIntent().getExtras().getSerializable(EXTRA_NOTE_TYPE);

        if (type == NoteType.STANDARD) {
            return createNoteFragment();
        } else if (type == NoteType.RECIPE) {
            return createRecipeFragment();
        }

        return null;

    }

    /**
     * Create a NoteFragment with correct arguments depending on which extras was sent in the intent
     * @return The new NoteFragment.
     */
    private Fragment createNoteFragment() {
        if (getIntent().getExtras().containsKey(EXTRA_NOTE_ID)) {
            long noteId = getIntent().getExtras().getLong(EXTRA_NOTE_ID);
            return NoteFragment.newInstance(noteId);
        } else if (getIntent().getExtras().containsKey(EXTRA_NOTE_TEXT) &&
                getIntent().getExtras().containsKey(EXTRA_NOTE_IMG_URI)) {

            String noteText = getIntent().getExtras().getString(EXTRA_NOTE_TEXT);
            String imgUri = getIntent().getExtras().getString(EXTRA_NOTE_IMG_URI);
            return NoteFragment.newInstance(noteText, imgUri);
        } else if (getIntent().getExtras().containsKey(EXTRA_NOTE_TEXT_LIST) &&
                getIntent().getExtras().containsKey(EXTRA_NOTE_IMG_LIST)) {
            ArrayList<String> noteText = getIntent().getExtras().getStringArrayList(EXTRA_NOTE_TEXT_LIST);
            ArrayList<String> imgUri = getIntent().getExtras().getStringArrayList(EXTRA_NOTE_IMG_LIST);
            return NoteFragment.newInstance(noteText, imgUri);
        }
        Log.w(TAG, "Received unexpected intent");
        return null;
    }

    /**
     * Create a RecipeFragment with the correct arguments depending on the extras sent with
     * the intent.
     * @return The new RecipeFragment.
     */
    private Fragment createRecipeFragment() {
        if (getIntent().getExtras().containsKey(EXTRA_NOTE_ID)) {
            long recipeId = getIntent().getExtras().getLong(EXTRA_NOTE_ID);
            return RecipeFragment.newInstance(recipeId);
        } else {
            ArrayList<String> texts = getIntent().getStringArrayListExtra(EXTRA_NOTE_TEXT_LIST);
            ArrayList<String> imagePaths = getIntent().getStringArrayListExtra(EXTRA_NOTE_IMG_LIST);
            return RecipeFragment.newInstance(texts, imagePaths);
        }
    }

    /**
     * Create a new intent.
     * @param packageContext Context of the calling activity.
     * @param noteText The texts found in one or more images.
     * @param imageUri The images that the text was extracted from.
     * @param type The type of note that shall be displayed.
     * @return The complete intent
     */
    public static Intent newIntent(Context packageContext, ArrayList<String> noteText,
                                   ArrayList<String> imageUri, NoteType type) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_TEXT_LIST, noteText);
        intent.putExtra(EXTRA_NOTE_IMG_LIST, imageUri);
        intent.putExtra(EXTRA_NOTE_TYPE, type);

        return intent;
    }

    /**
     * Create a new intent.
     * @param packageContext Context of the calling activity.
     * @param text The text found in the image.
     * @param imgPath The image that the text was extracted.
     * @param type The type of note that shall be displayed.
     * @return The complete intent
     */
    public static Intent newIntent(Context packageContext, String text, String imgPath,
                                   NoteType type) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_TEXT, text);
        intent.putExtra(EXTRA_NOTE_IMG_URI, imgPath);
        intent.putExtra(EXTRA_NOTE_TYPE, type);

        return intent;
    }

    /**
     * Create a new intent.
     * @param packageContext Context of the calling activity.
     * @param noteId The database id of the note.
     * @param type The type of note that shall be displayed
     * @return The complete intent.
     */
    public static Intent newIntent(Context packageContext, long noteId, NoteType type) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        intent.putExtra(EXTRA_NOTE_TYPE, type);

        return intent;
    }
}
