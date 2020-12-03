package br.alandoni.pdfcompare.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.io.PrintWriter;
import java.io.StringWriter;

import br.alandoni.pdfcompare.android.env.Environment;

public class StacktraceImage {

    private final String msg;
    private final Throwable throwable;
    private final int width;
    private final int height;

    public StacktraceImage(String msg, Throwable t, Environment environment) {
        this.msg = msg;
        this.throwable = t;
        this.width = 8 * environment.getDPI();
        this.height = 11 * environment.getDPI();
    }

    public ImageWithDimension getBlankImage() {
        return new ImageWithDimension(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565), width, height);
    }

    public ImageWithDimension getImage() {
        Bitmap errorImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas graphics = new Canvas();

        Paint paint = new Paint();

        int color = Color.BLACK;

        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setColor(color);
        paint.setTextSize(24);

        float nextY = drawString(graphics, msg, 100, 100, paint);
        nextY += getLineHeight(paint);

        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));

        drawString(graphics, sw.toString(), 100, nextY, paint);

        return new ImageWithDimension(errorImage, errorImage.getWidth(), errorImage.getHeight());
    }

    private float drawString(Canvas canvas, String text, float x, float y, Paint paint) {
        float lineHeight = getLineHeight(paint);
        for (String line : text.split("\n")) {
            canvas.drawText(line, x, y += lineHeight, paint);
        }
        return y;
    }

    private float getLineHeight(Paint paint) {
        return  paint.getFontMetrics().descent - paint.getFontMetrics().ascent;
    }
}
