package se.umu.cs.dv15anm.picturenote.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Adapter used to display list dialog with text and icons.
 * The code was found at: https://stackoverflow.com/questions/8533394/icons-in-a-list-dialog
 */

public class ArrayAdapterWithIcon extends ArrayAdapter<String>{

    private List<Integer> mImages;

    /**
     * Create a new array adapter
     * @param context The context of the calling activity
     * @param items The text to be displayed in the dialog
     * @param images The images to be displayed next to the text.
     */
    public ArrayAdapterWithIcon(Context context, String[] items, Integer[] images){
        super(context, android.R.layout.select_dialog_item, items);
        mImages = Arrays.asList(images);
    }

    /**
     * Retrieve the view and set the text and icons.
     * @param position The position in the list.
     * @param convertView The old view to be reused.
     * @param parent The parent the view will be attached to.
     * @return The view with the images added.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);

        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(mImages.get(position), 0, 0, 0);

        textView.setCompoundDrawablePadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getContext().
                        getResources().getDisplayMetrics()));

        return view;
    }
}
