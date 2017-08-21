package se.umu.cs.dv15anm.picturenote;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import se.umu.cs.dv15anm.picturenote.helpers.ImageAssists;
import se.umu.cs.dv15anm.picturenote.camera.ocr.OcrCameraActivity;
import se.umu.cs.dv15anm.picturenote.database.NoteBoard;
import se.umu.cs.dv15anm.picturenote.helpers.DialogCreator;
import se.umu.cs.dv15anm.picturenote.camera.ocr.ImageOcr;
import se.umu.cs.dv15anm.picturenote.helpers.NoteType;
import se.umu.cs.dv15anm.picturenote.models.Note;

/**
 * Fragment displaying all the notes as a list. This is the main view of the app, the view the
 * user will meet at startup.
 */
public class NoteListFragment extends Fragment {

    private static final String TAG = "NoteListFragment";
    private static final int SELECT_IMAGE = 1;

    private NoteRecyclerViewAdapter mAdapter;
    private RecyclerView mNoteRecyclerview;
    private List<Note> mNotes;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment
     */
    public NoteListFragment() {

    }

    /**
     * Initialize the fragment.
     * @param savedInstanceState The saved data from the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * Create the UI of the fragment.
     * @param inflater The LayoutInflater to inflate the views.
     * @param container The parent that the fragment view will be attached to.
     * @param savedInstanceState The saved data from the fragment
     * @return The view with the fragments UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list,container,false);
        mNoteRecyclerview = (RecyclerView) view.findViewById(R.id.note_list);
        mNoteRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUi();
        return view;
    }

    /**
     * Update the UI by getting the notes and giving them to the adapter.
     */
    private void updateUi(){
        NoteBoard noteBoard = NoteBoard.getNoteBoard(getActivity());
        mNotes = noteBoard.getNotes();

        if (mAdapter == null) {
            mAdapter = new NoteRecyclerViewAdapter(mNotes);
            mNoteRecyclerview.setAdapter(mAdapter);
        } else {
            mAdapter.setNotes(mNotes);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * When the fragment is resumed update the UI, to make sure the adapter has what it needs.
     */
    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }


    /**
     * Create the menu on the actionbar
     * @param menu The menu where the items will be placed
     * @param inflater The inflater that will inflate the UI.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_list, menu);
    }

    /**
     * Handles the return calls when an item is selected in the options menu.
     *
     * If new_note is selected a dialog will be shown to let the user select image from gallery or
     * camera, and the the appropriate activity will be started.
     *
     * if new_recipe is selected the ocr camera will be started.
     *
     * @param item The selected menu item.
     * @return true if handled else false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_note:
                DialogCreator.showImageSelectDialog(getActivity(),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, SELECT_IMAGE);
                                break;
                            case 1:
                                Intent cameraIntent = OcrCameraActivity.newIntent(getActivity(),
                                        NoteType.STANDARD);
                                startActivity(cameraIntent);
                                break;
                        }
                    }
                });
                return true;
            case R.id.new_recipe:
                Intent cameraIntent = OcrCameraActivity.newIntent(getActivity(), NoteType.RECIPE);
                startActivity(cameraIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * This view has a context menu, if a user has longpressed a list item, they can select to
     * remove that item. This method handles the removal of the item it that option was selected.
     * @param item The selected menu item
     * @return True if the action was handled else false.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.remove_item:
                new AlertDialog.Builder(getActivity()).setTitle(R.string.remove_dialog_title)
                        .setMessage(R.string.remove_dialog_message)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = mAdapter.getPosition();
                        NoteBoard.getNoteBoard(getActivity())
                                .removeNote(mAdapter.getNote(position));
                        mNotes.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }


    /**
     * If the user selected to get a image from the gallery, this method handles the result from
     * that activity, and retrieves teh image and then starts the note activity.
     * @param requestCode The code sent to the activity.
     * @param resultCode The code for the result returned from the activity.
     * @param data The data returned from the activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver()
                        .openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                String imageText = ImageOcr.findTextFromImage(selectedImage, getActivity());
                String imagePath = ImageAssists.getRealPathFromUri(getActivity(), imageUri);
                Intent intent = NoteActivity.newIntent(getActivity(), imageText, imagePath,
                        NoteType.STANDARD);
                startActivity(intent);
            } catch (FileNotFoundException e) {
                Log.w(TAG, "Could not load image " + e.getMessage());
            }
        }
    }


}
