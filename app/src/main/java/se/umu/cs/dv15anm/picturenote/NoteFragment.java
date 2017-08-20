package se.umu.cs.dv15anm.picturenote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;

import se.umu.cs.dv15anm.picturenote.camera.PicturePagerActivity;
import se.umu.cs.dv15anm.picturenote.database.NoteBoard;
import se.umu.cs.dv15anm.picturenote.helpers.NoteType;
import se.umu.cs.dv15anm.picturenote.models.Note;

/**
 * Fragment to Display a note
 */
public class NoteFragment extends Fragment {

    private static final String TAG = "NoteFragment";
    private static final String ARG_NOTE_ID = "note_id";
    private static final String ARG_NOTE_TEXT_LIST = "note_text_list";
    private static final String ARG_NOTE_IMG_PATH_LIST = "note_img_uri_list";
    private static final String ARG_NOTE_TEXT = "note_text";
    private static final String ARG_NOTE_IMG_PATH = "note_img_path_list";

    private Note mNote;
    private EditText mTitleField;
    private EditText mNoteText;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment
     */
    public NoteFragment() {
    }

    /**
     * Create a new instance of the fragment with database id as argument.
     * @param noteId The notes database id.
     * @return The new fragment.
     */
    public static NoteFragment newInstance(long noteId) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a new instance of the fragment.
     * @param noteText The text extracted from one or more images.
     * @param imageUri The images that the text was extracted from.
     * @return The new fragment.
     */
    public static NoteFragment newInstance(ArrayList<String> noteText, ArrayList<String> imageUri) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_NOTE_TEXT_LIST, noteText);
        args.putStringArrayList(ARG_NOTE_IMG_PATH_LIST, imageUri);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a new instance of the fragment.
     * @param noteText The text that was extracted from a image.
     * @param imageUri The image that the text was extracted from.
     * @return The new fragment.
     */
    public static NoteFragment newInstance(String noteText, String imageUri) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOTE_TEXT, noteText);
        args.putString(ARG_NOTE_IMG_PATH, imageUri);
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
            if (getArguments().containsKey(ARG_NOTE_ID)) {
                long id = getArguments().getLong(ARG_NOTE_ID);
                mNote = NoteBoard.getNoteBoard(getActivity()).getNote(id);

            } else if (getArguments().containsKey(ARG_NOTE_TEXT_LIST) &&
                    getArguments().containsKey(ARG_NOTE_IMG_PATH_LIST)) {
                mNote = new Note(NoteType.STANDARD);
                //A note can only have one text and image so only the first item should be retrieved
                String noteText = getArguments().getStringArrayList(ARG_NOTE_TEXT_LIST).get(0);
                String imageUri = getArguments().getStringArrayList(ARG_NOTE_IMG_PATH_LIST).get(0);
                mNote.setText(noteText);
                mNote.setNoteImage(imageUri);

            } else if (getArguments().containsKey(ARG_NOTE_TEXT) &&
                    getArguments().containsKey(ARG_NOTE_IMG_PATH)) {
                mNote = new Note(NoteType.STANDARD);
                String noteText = getArguments().getString(ARG_NOTE_TEXT);
                String imageUri = getArguments().getString(ARG_NOTE_IMG_PATH);
                mNote.setText(noteText);
                mNote.setNoteImage(imageUri);
            }
        } else {
            mNote = new Note(NoteType.STANDARD);
        }
    }

    /**
     * Create the UI.
     * @param inflater The LayoutInflater to inflate the views.
     * @param container The parent that the fragment view will be attached to.
     * @param savedInstanceState The saved data from the fragment
     * @return The view with the fragments UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        mTitleField = (EditText) view.findViewById(R.id.note_title);
        mTitleField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNoteText = (EditText) view.findViewById(R.id.note_text);
        mNoteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (mNote != null) {
            setupExistingNote();
        }

        return view;
    }

    /**
     * When the fragment is paused save the data to the database.
     */
    @Override
    public void onPause() {
        super.onPause();
        saveToDb();
    }

    /**
     * Create the menu on the actionbar
     * @param menu The menu where the items will be placed
     * @param inflater The inflater that will inflate the UI.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note, menu);
    }

    /**
     * Handles the return calls when an item is selected in the options menu.
     *
     * If the menu item view_picture is selected, a new activity will be started where the images
     * related to the note will be shown.
     *
     * @param item The selected menu item.
     * @return true if handled else false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_picture:
                ArrayList<String> images = new ArrayList<>();
                images.add(mNote.getNoteImage());
                Intent intent = PicturePagerActivity.newIntent(getActivity(), images);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Sets up the text fields if the note already has text.
     * If the note has the default title the text will be marked to make the title change easier.
     */
    private void setupExistingNote() {
        String title = mNote.getTitle();
        if (!title.isEmpty()) {
            mTitleField.setText(title);
            if (title.equals(getResources().getString(R.string.default_title))) {
                mTitleField.selectAll();
            }
        }
        mNoteText.setText(mNote.getText());
    }

    /**
     * Saves the note to the database
     */
    private void saveToDb(){
        mNote.setDate(new Date());
        if (mNote.getTitle().isEmpty()) {
            String title = getResources().getString(R.string.default_title);
            mNote.setTitle(title);
        }
        if (mNote.getId() != 0) {
            NoteBoard.getNoteBoard(getActivity()).updateNote(mNote);
        } else {
            NoteBoard.getNoteBoard(getActivity()).addNote(mNote);
        }
    }



}
