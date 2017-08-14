package se.umu.cs.dv15anm.picturenote;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.umu.cs.dv15anm.picturenote.NoteFragment.OnListFragmentInteractionListener;
import se.umu.cs.dv15anm.picturenote.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteHolder> {

//    private final List<DummyItem> mValues;
    private List<Note> mNotes;
//    private final OnListFragmentInteractionListener mListener;

    public NoteRecyclerViewAdapter( List<Note> notes) {
//        mValues = items;
        mNotes = notes;
//        mListener = listener;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);
//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
        Note note = mNotes.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;
//        public final TextView mIdView;
//        public final TextView mContentView;
//        public DummyItem mItem;
        private TextView mTitle;
        private TextView mDate;
        private TextView mPreview;
        private Note mNote;

        public NoteHolder(View view) {
            super(view);
            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.id);
//            mContentView = (TextView) view.findViewById(R.id.content);

            mTitle = (TextView) view.findViewById(R.id.note_title_text);
            mDate = (TextView) view.findViewById(R.id.note_date_text);
            mPreview = (TextView) view.findViewById(R.id.note_preview_text);
        }

        public void bind(Note note) {
            mNote = note;
            mTitle.setText(mNote.getmTitle());
            //TODO Fix preview
            mPreview.setText(mNote.getmText());
            mDate.setText(mNote.getmDate());
        }



//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }

        @Override
        public void onClick(View v) {
            //TODO start activity showing the note.
        }
    }
}
