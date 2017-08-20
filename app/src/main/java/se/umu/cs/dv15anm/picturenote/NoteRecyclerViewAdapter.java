package se.umu.cs.dv15anm.picturenote;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import se.umu.cs.dv15anm.picturenote.models.Note;

/**
 * Recycle view adapter used in the NoteList.
 */

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private List<Note> mNotes;
    private int mPosition;

    /**
     * Create a new adapter
     * @param notes List containing all the notes to be displayed.
     */
    public NoteRecyclerViewAdapter( List<Note> notes) {
        mNotes = notes;
    }

    /**
     * Set a new list with notes
     * @param notes The list with the notes to be displayed.
     */
    public void setNotes(List<Note> notes) {
        mNotes = notes;
    }

    /**
     * Creates a new NoteViewHolder for the notes.
     * @param parent The viewGroup the view will be attached to.
     * @param viewType The type of the new view.
     * @return The new view holder.
     */
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_note, parent, false);
        return new NoteViewHolder(view);
    }

    /**
     * Displays the data at the specific position.
     * @param holder The NoteViewHolder that will be updated to display the given note.
     * @param position The position of the item.
     */
    @Override
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }
        });
        Note note = mNotes.get(position);
        holder.bind(note);
    }

    /**
     * Retrieve the note at the given position.
     * @param position The position of the note
     * @return Note at given position
     */
    public Note getNote(int position) {
        return mNotes.get(position);
    }

    /**
     *
     * @return the position of the item that got a long click
     */
    public int getPosition() {
        return mPosition;
    }

    /**
     * Set the position of the item that got a long click
     * @param position The position of the note.
     */
    public void setPosition(int position) {
        mPosition = position;
    }

    /**
     *
     * @return The number of notes in the list.
     */
    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    /**
     * When a view is reused, remove the long click listener to avoid reference issues.
     * @param holder The view holder for the view being recycled.
     */
    @Override
    public void onViewRecycled(NoteViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }
}
