package se.umu.cs.dv15anm.picturenote.camera.ocr;

import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import se.umu.cs.dv15anm.picturenote.camera.overlay.Overlay;
import se.umu.cs.dv15anm.picturenote.camera.overlay.OverlayGraphic;

/**
 * Processor to detect text in a active camera feed, and create graphics to draw upon the preview.
 */

public class OcrProcessor implements Detector.Processor<TextBlock> {

    private Overlay mGraphicOverlay;

    /**
     * create a new processor
     * @param graphicOverlay The overlay that will display the graphics
     */
    public OcrProcessor(Overlay graphicOverlay) {
        mGraphicOverlay = graphicOverlay;
    }

    /**
     * Clear the overlay on release
     */
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }

    /**
     * When text is detected create a graphics object that will draw the text and show where it was
     * found in the active camera preview.
     * @param detections The text detected
     */
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();

        SparseArray<TextBlock> items = detections.getDetectedItems();

        for (int i = 0; i < items.size(); i++) {
            TextBlock item = items.valueAt(i);
            OverlayGraphic graphic = new OverlayGraphic(mGraphicOverlay, item);
            mGraphicOverlay.add(graphic);
        }
    }
}
