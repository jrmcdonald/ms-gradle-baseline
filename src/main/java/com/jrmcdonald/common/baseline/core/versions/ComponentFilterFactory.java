package com.jrmcdonald.common.baseline.core.versions;

import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentFilter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ComponentFilterFactory {

    public static ComponentFilter get() { return new DefaultComponentFilter(); }

}
