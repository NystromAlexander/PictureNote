package se.umu.cs.dv15anm.picturenote.camera.ocr;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import se.umu.cs.dv15anm.picturenote.helpers.NoteType;
import se.umu.cs.dv15anm.picturenote.helpers.SingleFragmentActivity;

/**
 * Activity that will host the OcrCameraFragment
 */
public class OcrCameraActivity extends SingleFragmentActivity {

    private static final String EXTRA_NOTE_TYPE = "note_type";

    /**
     * Create the fragment and get the type of note from the intent.
     * @return t
     */
    @Override
    protected Fragment createFragment() {
        NoteType type = (NoteType) getIntent().getExtras().getSerializable(EXTRA_NOTE_TYPE);
        return OcrCameraFragment.newInstance(type);
    }

    /**
     * Create an intent designed to start this activity with the type of note to be captured as
     * an extra.
     * @param packageContext The context of the activity calling the method
     * @param noteType The type of note to be captured by the camera.
     * @return The finished intent.
     */
    public static Intent newIntent(Context packageContext, NoteType noteType) {
        Intent intent = new Intent(packageContext,OcrCameraActivity.class);
        intent.putExtra(EXTRA_NOTE_TYPE, noteType);

        return intent;
    }

}
