package ru.seredavin.gitlicenseparser.utils;

import java.util.Arrays;
import java.util.List;

public enum Licenses {
    MIT("mit", Arrays.asList("/\\sMIT\\s/", "/\\s\\(MIT\\)\\s/")),
    LGPL30("lgpl-3.0", Arrays.asList("/\\sLGPL\\s/")),
    MPL20("lgpl-3.0", Arrays.asList("/\\sMPL\\s/")),
    AGPL30("agpl-3.0", Arrays.asList("/\\sAGPL\\s/")),
    Apache20("apache-2.0", Arrays.asList("/\\sApache\\sLicen[cs]e\\s/")),
    GPL30("gpl-3.0", Arrays.asList("/\\sGPL\\s/", "/\\sGPLv2\\s/"));

    private String name;
    private List<String> patterns;

    Licenses(String name, List<String> patterns) {
        this.name = name;
        this.patterns = patterns;
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public String getName() {
        return name;
    }
}
