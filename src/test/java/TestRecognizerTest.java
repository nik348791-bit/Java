import org.example.TestRecognizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestRecognizerTest {

    private TestRecognizer recognizer;

    @BeforeEach
    void setUp() {
        recognizer = new TestRecognizer();
    }

    @ParameterizedTest
    @CsvSource({
            "abcTESTabc, F",
            "abcTES, THREE",
            "TEST, F",
            "TEST123, F",
            "123TEST, F",
            "T, ONE",
            "abcTE, TWO",
            "abcdef, S",
            "test, S",
            "TE_ST, ONE"
    })
    void testDiagramImplementation(String input, TestRecognizer.State expectedState) {
        assertEquals(expectedState, recognizer.recognizeStrictByDiagram(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testNullAndEmpty(String input) {
        assertEquals(TestRecognizer.State.S, recognizer.recognizeStrictByDiagram(input));
        assertEquals(TestRecognizer.State.S, recognizer.recognizeFixed(input));
    }

    @ParameterizedTest
    @CsvSource({
            "TTEST",
            "TTTTEST",
            "TETEST",
            "abcTETESTxyz",
            "T_TTEST"
    })
    void testEdgeCasesWithOverlappingPrefixes(String input) {
        assertNotEquals(TestRecognizer.State.F, recognizer.recognizeStrictByDiagram(input));
        assertEquals(TestRecognizer.State.F, recognizer.recognizeFixed(input));
    }

    @ParameterizedTest
    @CsvSource({
            "TT, ONE",
            "TET, ONE",
            "TETE, TWO",
            "TETES, THREE",
            "TTES, THREE"
    })
    void testFixedIntermediateStates(String input, TestRecognizer.State expectedState) {
        assertEquals(expectedState, recognizer.recognizeFixed(input));
    }
}