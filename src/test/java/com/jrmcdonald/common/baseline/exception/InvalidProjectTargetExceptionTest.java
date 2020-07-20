package com.jrmcdonald.common.baseline.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidProjectTargetExceptionTest {

    @Test
    @DisplayName("Should construct a valid bean")
    void shouldConstructAValidBean() {
        var exception = new InvalidProjectTargetException("something went wrong");
        assertThat(exception).isInstanceOf(RuntimeException.class);
        assertThat(exception).hasMessage("something went wrong");
    }
}