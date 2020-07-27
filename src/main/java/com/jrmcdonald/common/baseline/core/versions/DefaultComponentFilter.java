package com.jrmcdonald.common.baseline.core.versions;

import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentFilter;
import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentSelectionWithCurrent;

import java.util.Set;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class DefaultComponentFilter implements ComponentFilter {

    private static final Set<String> STABLE_KEYWORDS = Set.of("RELEASE", "FINAL", "GA");
    private static final String STABLE_REGEX = "^[0-9,.v-]+(-r)?$";

    @Override
    public boolean reject(ComponentSelectionWithCurrent selection) {
        return isUnstableCandidateVersion(selection.getCandidate().getVersion());
    }

    private boolean isUnstableCandidateVersion(String version) {
        return !containsStableKeyword(version) && !matchesStableRegex(version);
    }

    private boolean containsStableKeyword(String version) {
        return isNotEmpty(version) && STABLE_KEYWORDS.stream().anyMatch(keyword -> version.toUpperCase().contains(keyword));
    }

    private boolean matchesStableRegex(String version) {
        return Pattern.matches(STABLE_REGEX, version);
    }

}
