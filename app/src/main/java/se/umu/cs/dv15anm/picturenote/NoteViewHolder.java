package se.umu.cs.dv15anm.picturenote;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;

import se.umu.cs.dv15anm.picturenote.models.Note;

/**
 * Creates the list items showing a note.
 */
public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener{

        private static final String TAG = "NoteRecyclerViewAdapter";
        public final View mView;

        private TextView mTitle;
        private TextView mDate;
        private TextView mTime;
        private Note mNote;

    /**
     * Create a new viewHolder and get the UI elements.
     * @param view The view the items are placed in.
     */
        public NoteViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            mView = view;

            mTitle = (TextView) view.findViewById(R.id.note_list_title_text);
            mDate = (TextView) view.findViewById(R.id.note_list_date_text);
            mTime = (TextView) view.findViewById(R.id.note_list_time_text);
            view.setOnCreateContextMenuListener(this);
        }

    /**
     * Bind a note to the view, by adding information to the list view.
     * @param note The note to be bound.
     */
    public void bind(Note note) {
            mNote = note;
            mTitle.setText(mNote.getTitle());
            mDate.setText(DateFormat.getDateInstance().format(mNote.getDate()));
            mTime.setText(DateFormat.getTimeInstance().format(mNote.getDate()));
        }

    /**
     * When a note is clicked start a NoteActivity to display that note.
     * @param v The view that got clicked.
     */
        @Override
        public void onClick(View v) {
            Intent intent = NoteActivity.newIntent(mView.getContext(),mNote.getId(),
                    mNote.getType());
            mView.getContext().startActivity(intent);
        }

    /**
     * Create a context menu when an item is long clicked.
     * @param menu The menu that is being built.
     * @param v The view for which the menu is being built.
     * @param menuInfo Extra information about the item that the menu is being built.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = new MenuInflater(v.getContext());
        inflater.inflate(R.menu.fragment_note_list_context,menu);
    }
}
