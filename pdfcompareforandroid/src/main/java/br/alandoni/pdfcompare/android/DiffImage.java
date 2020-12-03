package br.alandoni.pdfcompare.android;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.alandoni.pdfcompare.android.env.Environment;

import static br.alandoni.pdfcompare.android.PdfComparator.MARKER_WIDTH;

public class DiffImage {

    private static final Logger LOG = LoggerFactory.getLogger(DiffImage.class);
    /*package*/ static final int MARKER_RGB = Color.rgb(230, 0, 230);
    private final ImageWithDimension expectedImage;
    private final ImageWithDimension actualImage;
    private final int page;
    private final Environment environment;
    private final Exclusions exclusions;
    private int expectedImageWidth;
    private int expectedImageHeight;
    private int actualImageWidth;
    private int actualImageHeight;
    private Bitmap resultImage;
    private int diffAreaX1, diffAreaY1, diffAreaX2, diffAreaY2;
    private final ResultCollector compareResult;
    private PageDiffCalculator diffCalculator;

    public DiffImage(final ImageWithDimension expectedImage, final ImageWithDimension actualImage, final int page,
                     final Environment environment, final Exclusions exclusions, final ResultCollector compareResult) {
        this.expectedImage = expectedImage;
        this.actualImage = actualImage;
        this.page = page;
        this.environment = environment;
        this.exclusions = exclusions;
        this.compareResult = compareResult;
    }

    public Bitmap getImage() {
        return resultImage;
    }

    public int getPage() {
        return page;
    }

    public void diffImages() {
        Bitmap expectBuffImage = this.expectedImage.bufferedImage;
        Bitmap actualBuffImage = this.actualImage.bufferedImage;

        expectedImageWidth = expectBuffImage.getWidth();
        expectedImageHeight = expectBuffImage.getHeight();
        actualImageWidth = actualBuffImage.getWidth();
        actualImageHeight = actualBuffImage.getHeight();

        int resultImageWidth = Math.max(expectedImageWidth, actualImageWidth);
        int resultImageHeight = Math.max(expectedImageHeight, actualImageHeight);
        resultImage = Bitmap.createBitmap(resultImageWidth, resultImageHeight, actualBuffImage.getConfig());

        diffCalculator = new PageDiffCalculator(resultImageWidth * resultImageHeight, environment.getAllowedDiffInPercent());

        int expectedElement;
        int actualElement;
        final PageExclusions pageExclusions = exclusions.forPage(page + 1);
        Log.d("DiffImage", "PageExclusions: " + pageExclusions.getExclusions().size());
        for (int y = 0; y < resultImageHeight; y++) {
            for (int x = 0; x < resultImageWidth; x++) {
                expectedElement = getExpectedElement(x, y);
                actualElement = getActualElement(x, y);
                int element = getElement(expectedElement, actualElement);

                if (pageExclusions.contains(x, y)) {
                    element = ImageTools.fadeExclusion(element);
                    if (expectedElement != actualElement) {
                        diffCalculator.diffFoundInExclusion();
                    }
                } else {
                    if (expectedElement != actualElement) {
                        extendDiffArea(x, y);
                        diffCalculator.diffFound();
                        LOG.trace("Difference found on page: {} at x: {}, y: {}", page + 1, x, y);
                        mark(environment, resultImage, x, y);
                    }
                }
                resultImage.setPixel(x, y, element);
            }
        }
        if (diffCalculator.differencesFound()) {
            diffCalculator.addDiffArea(new PageArea(page + 1, diffAreaX1, diffAreaY1, diffAreaX2, diffAreaY2));
            LOG.info("Differences found at { page: {}, x1: {}, y1: {}, x2: {}, y2: {} }", page + 1, diffAreaX1, diffAreaY1, diffAreaX2,
                    diffAreaY2);
        }
        final float maxWidth = Math.max(expectedImage.width, actualImage.width);
        final float maxHeight = Math.max(expectedImage.height, actualImage.height);
        compareResult.addPage(diffCalculator, page, expectedImage, actualImage, new ImageWithDimension(resultImage, maxWidth, maxHeight));
    }

    private void extendDiffArea(final int x, final int y) {
        if (!diffCalculator.differencesFound()) {
            diffAreaX1 = x;
            diffAreaY1 = y;
        }
        diffAreaX1 = Math.min(diffAreaX1, x);
        diffAreaX2 = Math.max(diffAreaX2, x);
        diffAreaY1 = Math.min(diffAreaY1, y);
        diffAreaY2 = Math.max(diffAreaY2, y);
    }

    private int getElement(final int expectedElement, final int actualElement) {
        if (expectedElement != actualElement) {
            int expectedIntensity = calcCombinedIntensity(expectedElement);
            int actualIntensity = calcCombinedIntensity(actualElement);
            if (expectedIntensity > actualIntensity) {
                int color = environment.getActualColor();
                return Color.rgb(levelIntensity(expectedIntensity, Color.red(color)), Color.green(color), Color.blue(color));
            } else {
                int color = environment.getExpectedColor();
                return Color.rgb(Color.red(color), levelIntensity(expectedIntensity, Color.green(color)), Color.blue(color));
            }
        } else {
            return ImageTools.fadeElement(expectedElement);
        }
    }

    private int getExpectedElement(final int x, final int y) {
        if (x < expectedImageWidth && y < expectedImageHeight) {
            return expectedImage.bufferedImage.getPixel(x, y);
        }
        return 0;
    }

    private int getActualElement(final int x, final int y) {
        if (x < actualImageWidth && y < actualImageHeight) {
            return actualImage.bufferedImage.getPixel(x, y);
        }
        return 0;
    }

    /**
     * Levels the color intensity to at least 50 and at most maxIntensity.
     *
     * @param darkness     color component to level
     * @param maxIntensity highest possible intensity cut off
     * @return A value that is at least 50 and at most maxIntensity
     */
    private static int levelIntensity(final int darkness, final int maxIntensity) {
        return Math.min(maxIntensity, Math.max(50, darkness));
    }

    /**
     * Calculate the combined intensity of a pixel and normalize it to a value of at most 255.
     *
     * @param color a pixel encoded as an integer
     * @return the intensity of all colors combined cut off at a maximum of 255
     */
    private static int calcCombinedIntensity(final int color) {
        return Math.min(255, (Color.red(color) + Color.green(color) + Color.blue(color)) / 3);
    }

    private static void mark(Environment environment, final Bitmap image, final int x, final int y) {
        for (int i = 0; i < MARKER_WIDTH; i++) {
            image.setPixel(x, i, MARKER_RGB);
            image.setPixel(i, y, MARKER_RGB);
        }
    }

    @Override
    public String toString() {
        return "DiffImage{" +
                "page=" + page +
                '}';
    }
}
