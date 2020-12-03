package br.alandoni.pdfcompare.android.env;

import com.typesafe.config.ConfigFactory;

public class DefaultEnvironment {

    public static Environment create() {
        ConfigFactory.invalidateCaches();
        return new ConfigFileEnvironment(ConfigFactory.load());
    }
}
