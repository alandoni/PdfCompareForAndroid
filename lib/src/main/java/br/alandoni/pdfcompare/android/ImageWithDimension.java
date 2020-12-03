package br.alandoni.pdfcompare.android;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public class ImageWithDimension {

    public final Bitmap bufferedImage;
    public final float width;
    public final float height;

    public ImageWithDimension(@NonNull final Bitmap bufferedImage, final float width, final float height) {
        this.bufferedImage = bufferedImage;
        this.width = width;
        this.height = height;
    }
}
