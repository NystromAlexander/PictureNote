package se.umu.cs.dv15anm.picturenote.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ListAdapter;

import se.umu.cs.dv15anm.picturenote.R;

/**
 * This class is used to create a dialog asking the user to either get image from camera or the
 * gallery.
 */

public class DialogCreator {

    /**
     * Creates a diolog asking the user to get image from camera or gallery.
     * @param context The context of the calling activity.
     * @param clickListener The click listener that handles the callback from the dialog.
     */
    public static void showImageSelectDialog(Context context, DialogInterface.OnClickListener clickListener) {
        final String[] items = new String[] {"From Gallery", "From Camera"};
        final Integer[] icons = new Integer[] {android.R.drawable.ic_menu_gallery,
                android.R.drawable.ic_menu_camera};
        ListAdapter adapter = new ArrayAdapterWithIcon(context, items, icons);

        new AlertDialog.Builder(context)
                .setTitle("Select Image")
                .setAdapter(adapter, clickListener)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}
