package se.umu.cs.dv15anm.picturenote.camera;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Collection of static methods that are useful when working with images
 */

public class ImageAssists {

    public static Bitmap fixOrientation(Bitmap image, int rotation) {
        if (rotation != 0) {
            Bitmap bMapRotate;
            Matrix mat = new Matrix();
            mat.postRotate(rotation);
            bMapRotate = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(),
                    mat, true);
            image.recycle();
            return bMapRotate;
        }
        return image;
    }

    /**
     * A method to get how many degrees the image has to be rotated.
     * Found at this forum thread:
     * https://stackoverflow.com/questions/12726860/android-how-to-detect-the-image-orientation-portrait-or-landscape-picked-fro
     * @param imageFile The image file
     * @return Degrees the image need to be rotated.
     */
    public static int getCameraPhotoOrientation(File imageFile){
        int rotate = 0;
        try {

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    /**
     * Method to get the real path from a content uri.
     * Credit to this forum thread:
     * https://stackoverflow.com/questions/20028319/how-to-convert-content-media-external-images-media-y-to-file-storage-sdc
     * @param context The context of the activity that called.
     * @param contentUri The content uri to the image
     * @return The path to the image.
     */
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
