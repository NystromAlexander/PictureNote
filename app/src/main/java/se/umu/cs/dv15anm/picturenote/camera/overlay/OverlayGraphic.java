package se.umu.cs.dv15anm.picturenote.camera.overlay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.List;

/**
 * Class representing a graphic item that get drawn over the camera preview
 */

public class OverlayGraphic {

    private static final String TAG = "OverlayGraphic";
    private static final int TEXT_COLOR = Color.WHITE;
    private static final float STROKE_SIZE = 4.0f;
    private static final float TEXT_SIZE = 54.0f;

    private static Paint sRectanglePaint;
    private static Paint sTextPaint;
    private final TextBlock mText;
    private Overlay mOverlay;

    /**
     * Create a new overlay graphic item
     * @param overlay The overlay that will host the graphic
     * @param text The text and text information that will be displayed by the graphics.
     */
    public OverlayGraphic(Overlay overlay, TextBlock text) {
        mOverlay = overlay;
        mText = text;

        sRectanglePaint = new Paint();
        sRectanglePaint.setStyle(Paint.Style.STROKE);
        sRectanglePaint.setStrokeWidth(STROKE_SIZE);
        sRectanglePaint.setColor(TEXT_COLOR);

        sTextPaint = new Paint();
        sTextPaint.setStyle(Paint.Style.STROKE);
        sTextPaint.setTextSize(TEXT_SIZE);
        sTextPaint.setColor(TEXT_COLOR);

        mOverlay.postInvalidate();
    }

    /**
     * Adjusts a horizontal value of the supplied value from the preview scale to the view
     * scale.
     */
    public float scaleX(float horizontal) {
        return horizontal * mOverlay.getWidthScaleFactor();
    }

    /**
     * Adjusts a vertical value of the supplied value from the preview scale to the view scale.
     */
    public float scaleY(float vertical) {
        return vertical * mOverlay.getHeightScaleFactor();
    }

    /**
     * Draw a rectangle and the text contain in the rectangle on the canvas
     * @param canvas The canvas where the graphic gets drawn
     */
    public void draw(Canvas canvas){
        if (mText == null) {
            return;
        }

        RectF rect = new RectF(mText.getBoundingBox());
        rect.top = scaleY(rect.top);
        rect.right = scaleX(rect.right);
        rect.bottom = scaleY(rect.bottom);
        rect.left = scaleX(rect.left);

        canvas.drawRect(rect, sRectanglePaint);

        List<? extends Text> texts = mText.getComponents();
        for (Text text : texts) {
            float left = scaleX(text.getBoundingBox().left);
            float bottom = scaleY(text.getBoundingBox().bottom);
            canvas.drawText(text.getValue(), left, bottom, sTextPaint);
        }
    }
}
