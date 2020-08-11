package com.jrmcdonald.common.baseline.plugin;

import org.force66.beantester.BeanTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BaselinePluginExtensionTest {

    @Test
    @DisplayName("Should create a valid bean")
    void shouldCreateAValidBean() {
        var beanTester = new BeanTester();
        beanTester.testBean(BaselinePluginExtension.class);
    }

    @Test
    @DisplayName("Should default `springBootEnabled` to true")
    void shouldDefaultSpringEnabledToTrue() {
        var extension = new BaselinePluginExtension();
        assertThat(extension.isSpringBootEnabled()).isTrue();
    }

}