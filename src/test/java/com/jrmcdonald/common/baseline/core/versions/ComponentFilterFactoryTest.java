package com.jrmcdonald.common.baseline.core.versions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentFilterFactoryTest {

    @Test
    @DisplayName("Should return DefaultComponentFilter as default")
    void shouldReturnDefaultDependencyUpdateResolutionStrategyAsDefault() {
        assertThat(ComponentFilterFactory.get()).isInstanceOf(DefaultComponentFilter.class);
    }

}