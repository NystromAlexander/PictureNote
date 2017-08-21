package se.umu.cs.dv15anm.picturenote.camera.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import se.umu.cs.dv15anm.picturenote.helpers.TextConverter;

/**
 * Class that has a static method to retrieve text from a bitmap representation of a image
 */

public class ImageOcr {

    private static final String TAG = "ImageOcr";

    /**
     * Detects text in a image
     * @param picture The image to be analyzed
     * @param context The context of the caller
     * @return The text found in the image.
     */
    public static String findTextFromImage(Bitmap picture, Context context) {
        Frame pictureFrame = new Frame.Builder().setBitmap(picture).build();
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        SparseArray<TextBlock> text = textRecognizer.detect(pictureFrame);
        String completeText = TextConverter.TextBlocksToString(text);
        Log.d(TAG, "Finished text:\n" + completeText);
        textRecognizer.release();

        return completeText;
    }
}
