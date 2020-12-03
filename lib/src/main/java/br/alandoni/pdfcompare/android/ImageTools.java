package br.alandoni.pdfcompare.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ImageTools {

    public static Bitmap blankImage(final Bitmap image) {
        Bitmap graphics = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
        Canvas canvas = new Canvas();
        int color = Color.rgb(255, 255, 255);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0, 0, image.getWidth(), image.getHeight(), paint);
        return image;
    }

    public static int fadeElement(final int i) {
        return Color.rgb(
                fade(Color.red(i)),
                fade(Color.green(i)),
                fade(Color.blue(i)));
    }

    public static int fadeExclusion(final int color) {
        if (Color.red(color) > 245 && Color.green(color) > 245 && Color.blue(color) > 245) {
            return Color.rgb(255, 255, 100);
        }
        return fadeElement(color);
    }

    private static int fade(final int i) {
        return i + ((255 - i) * 3 / 5);
    }

    public static Bitmap deepCopy(Bitmap image) {
        return image.copy(image.getConfig(), true);
    }
}
