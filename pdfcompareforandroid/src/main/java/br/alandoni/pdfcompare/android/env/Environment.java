package br.alandoni.pdfcompare.android.env;

import java.nio.file.Path;

public interface Environment {

    Path getTempDirectory();

    int getNrOfImagesToCache();

    int getMergeCacheSize();

    int getSwapCacheSize();

    int getDocumentCacheSize();

    int getMaxImageSize();

    int getOverallTimeout();

    boolean useParallelProcessing();

    double getAllowedDiffInPercent();

    int getExpectedColor();

    int getActualColor();

    int getDPI();

    boolean addEqualPagesToResult();

    boolean failOnMissingIgnoreFile();
}
