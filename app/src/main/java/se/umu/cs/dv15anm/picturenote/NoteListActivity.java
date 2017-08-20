package se.umu.cs.dv15anm.picturenote;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import se.umu.cs.dv15anm.picturenote.helpers.SingleFragmentActivity;

/**
 * This is the main activity, the one that starts.
 */
public class NoteListActivity extends SingleFragmentActivity {
    public static final String APP_NAME = "Picture Note";
    private static final String TAG = "NoteListActivity";
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int RC_HANDLE_EXT_STORAGE_PERM = 3;
    private static final int RC_HANDLE_EXT_STORAGE_READ_PERM = 4;

    /**
     * Create the activity and setup the UI.
     * Request permission to use the camera and storage.
     * @param savedInstanceState The saved data from the activity.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestCameraPermission();
        requestStoragePermission();
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
     * If the app do not have camera permission request the permission.
     */
    private void requestCameraPermission() {
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "Camera permission is not granted. Requesting permission");

            final String[] permissions = new String[]{Manifest.permission.CAMERA};

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
                return;
            }
       }
    }


    /**
     * If the app do not have external storage permission request it.
     */
    private void requestStoragePermission() {

        int rc = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

       if (rc != PackageManager.PERMISSION_GRANTED) {
           Log.w(TAG, "Storage permission is not granted. Requesting permission");

           final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

           if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                   Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
               ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_EXT_STORAGE_PERM);

           }
       }

       rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
       if (rc != PackageManager.PERMISSION_GRANTED) {
           Log.w(TAG, "Storage read permission is not granted. Requesting permission");

           final String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

           if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                   Manifest.permission.READ_EXTERNAL_STORAGE)) {
               ActivityCompat.requestPermissions(this, permissions,
                       RC_HANDLE_EXT_STORAGE_READ_PERM);

           }
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
            case RC_HANDLE_CAMERA_PERM:
                if (grantResults.length != 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Camera permission granted");
                } else {
                    Log.w(TAG,"Camera permission denied");
                    new AlertDialog.Builder(this)
                            .setMessage("This app requires camera permission to properly function")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestCameraPermission();
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                }
                return;
            case RC_HANDLE_EXT_STORAGE_PERM:
                if (grantResults.length != 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Storage write permission granted");
                } else {
                    Log.w(TAG,"External storage permission denied");
                }
                return;
            default:
                Log.d(TAG, "Got unexpected permission result: " + requestCode);
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }

    }
}
