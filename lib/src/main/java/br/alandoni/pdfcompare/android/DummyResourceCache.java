package br.alandoni.pdfcompare.android;

import com.tom_roush.pdfbox.cos.COSObject;
import com.tom_roush.pdfbox.pdmodel.ResourceCache;
import com.tom_roush.pdfbox.pdmodel.documentinterchange.markedcontent.PDPropertyList;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.graphics.PDXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDColorSpace;
import com.tom_roush.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import com.tom_roush.pdfbox.pdmodel.graphics.shading.PDShading;
import com.tom_roush.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.io.IOException;

public class DummyResourceCache implements ResourceCache {

    @Override
    public PDFont getFont(final COSObject indirect) throws IOException {
        return null;
    }

    @Override
    public PDColorSpace getColorSpace(final COSObject indirect) throws IOException {
        return null;
    }

    @Override
    public PDExtendedGraphicsState getExtGState(final COSObject indirect) {
        return null;
    }

    @Override
    public PDShading getShading(final COSObject indirect) throws IOException {
        return null;
    }

    @Override
    public PDAbstractPattern getPattern(final COSObject indirect) throws IOException {
        return null;
    }

    @Override
    public PDPropertyList getProperties(final COSObject indirect) {
        return null;
    }

    @Override
    public PDXObject getXObject(final COSObject indirect) throws IOException {
        return null;
    }

    @Override
    public void put(final COSObject indirect, final PDFont font) throws IOException {}

    @Override
    public void put(final COSObject indirect, final PDColorSpace colorSpace) throws IOException {}

    @Override
    public void put(final COSObject indirect, final PDExtendedGraphicsState extGState) {}

    @Override
    public void put(final COSObject indirect, final PDShading shading) throws IOException {}

    @Override
    public void put(final COSObject indirect, final PDAbstractPattern pattern) throws IOException {}

    @Override
    public void put(final COSObject indirect, final PDPropertyList propertyList) {}

    @Override
    public void put(final COSObject indirect, final PDXObject xobject) throws IOException {}
}
