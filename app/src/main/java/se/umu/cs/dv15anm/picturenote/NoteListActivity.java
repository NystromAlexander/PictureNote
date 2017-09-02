package se.umu.cs.dv15anm.picturenote;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import se.umu.cs.dv15anm.picturenote.helpers.SingleFragmentActivity;

/**
 * This is the main activity, the one that starts.
 */
public class NoteListActivity extends SingleFragmentActivity {

    private static final String TAG = "NoteListActivity";
    private static final int RC_HANDLE_ALL_PERM = 2;

    /**
     * Create the activity and setup the UI.
     * Request permission to use the camera and storage.
     * @param savedInstanceState The saved data from the activity.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();

    }

    /**
     *
     * @return A new NoteListFragment
     */
    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }

    /**
     * Check if the application has the required permissions
     * @param context The context of the calling activity
     * @param permissions The permissions required by the application.
     * @return False if there is at least one permission missing else true.
     */
    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null
                && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Request permission to use camera and external storage.
     */
    private void requestPermissions() {
        String[] permissions = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_ALL_PERM);
        }
    }


    /**
     * Handles the result after asking for permissions.
     * @param requestCode The code passed when asking for permission.
     * @param permissions The requested permissions.
     * @param grantResults The grant result for the permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case RC_HANDLE_ALL_PERM:
                if (grantResults.length != 0 && checkGrantResults(grantResults)) {
                    Log.d(TAG, "Permissions granted");
                } else {
                    Log.w(TAG,"Permissions denied");
                    if (shoulsAskAgain()) {
                        new AlertDialog.Builder(this)
                                .setMessage("This app requires camera and storage permission to " +
                                        "properly function. Without these the app has very " +
                                        "limited to no functionality.")
                                .setPositiveButton(R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions();
                                    }
                                }).setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
                    }
                }
                break;
            default:
                Log.d(TAG, "Got unexpected permission result: " + requestCode);
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
               break;
        }

    }

    private boolean shoulsAskAgain() {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);

    }


    private boolean checkGrantResults(int[] grantResults) {
        for (int res: grantResults) {
            if (res != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }
}
