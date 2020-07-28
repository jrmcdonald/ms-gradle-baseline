package com.jrmcdonald.common.baseline.core.versions;

import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentSelectionWithCurrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultComponentFilterTest {

    private static final boolean STABLE = false;
    private static final boolean UNSTABLE = true;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ComponentSelectionWithCurrent selection;

    private DefaultComponentFilter strategy;

    @BeforeEach
    void beforeEach() {
        strategy = new DefaultComponentFilter();
    }

    private static Stream<Arguments> versionsWithExpectations() {
        return Stream.of(
                Arguments.of("0.1.0-RELEASE", STABLE),
                Arguments.of("1.2-FINAL", STABLE),
                Arguments.of("2.3.4-GA", STABLE),
                Arguments.of("v0.1.2", STABLE),
                Arguments.of("v0,1,2", STABLE),
                Arguments.of("v0.1.2-r", STABLE),
                Arguments.of("v0.1.2-r", STABLE),
                Arguments.of("v0.1.a-RELEASE", STABLE),
                Arguments.of("a.b.c", UNSTABLE),
                Arguments.of("SNAPSHOT", UNSTABLE),
                Arguments.of("0.1.0-SNAPSHOT", UNSTABLE)
        );
    }

    @ParameterizedTest(name = "[{index}] expect {0} to be {1}")
    @MethodSource("versionsWithExpectations")
    @DisplayName("Should reject if an unstable version")
    void shouldRejectIfAnUnstableVersion(String version, boolean stability) {
        when(selection.getCandidate().getVersion()).thenReturn(version);
        assertThat(strategy.reject(selection)).isEqualTo(stability);
    }

}