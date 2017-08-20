package se.umu.cs.dv15anm.picturenote.helpers;

import android.util.SparseArray;

import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.List;



/**
 * Used to convert text from the text recognizer into a string.
 */

public class TextConverter {

    private static final String TAG = "TextConverter";

    /**
     * Take the result from a text recognizer and turn it into a string.
     * @param textBlocks The result from the TextRecognizer.
     * @return String with the complete text.
     */
    public static String TextBlocksToString(SparseArray<TextBlock> textBlocks) {
        String completeText = "";

        for (int i = 0; i < textBlocks.size(); i++){
            List<? extends Text> texts = textBlocks.valueAt(i).getComponents();

            for (Text text: texts) {
                completeText = completeText + "\n" + text.getValue();
            }
            completeText = completeText + "\n";
        }

        return completeText;
    }
}
