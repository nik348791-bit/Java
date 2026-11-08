package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private static final double DELTA = 1e-9;
    private final Calculator calc = new Calculator();



    @Test
    void addWorks() throws InvalidInputException {
        assertEquals(5.0, calc.add(2, 3), DELTA);
        assertEquals(-1.0, calc.add(2, -3), DELTA);
        assertEquals(0.3, calc.add(0.1, 0.2), 1e-9);
    }

    @Test
    void subtractWorks() throws InvalidInputException {
        assertEquals(-1.0, calc.subtract(2, 3), DELTA);
        assertEquals(5.0, calc.subtract(2, -3), DELTA);
    }

    @Test
    void multiplyWorks() throws InvalidInputException {
        assertEquals(6.0, calc.multiply(2, 3), DELTA);
        assertEquals(0.0, calc.multiply(5, 0), DELTA);
        assertEquals(-6.0, calc.multiply(-2, 3), DELTA);
    }

    @Test
    void divideWorks() throws InvalidInputException {
        assertEquals(2.5, calc.divide(5, 2), DELTA);
        assertEquals(-2.0, calc.divide(6, -3), DELTA);
        assertEquals(0.0, calc.divide(0, 7), DELTA);
    }

    @Test
    void sqrtWorks() throws InvalidInputException {
        assertEquals(3.0, calc.sqrt(9), DELTA);
        assertEquals(0.0, calc.sqrt(0), DELTA);
    }



    @Test
    void divideByZeroThrowsArithmeticException() {
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> calc.divide(10, 0));
        assertEquals("Ділення на нуль неможливе!", ex.getMessage());
    }

    @Test
    void divideByNegativeZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calc.divide(10, -0.0));
    }

    @Test
    void zeroDividedByZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calc.divide(0, 0));
    }



    @Test
    void nanThrowsInvalidInputInEveryMethod() {
        assertThrows(InvalidInputException.class, () -> calc.add(Double.NaN, 1));
        assertThrows(InvalidInputException.class, () -> calc.subtract(1, Double.NaN));
        assertThrows(InvalidInputException.class, () -> calc.multiply(Double.NaN, Double.NaN));
        assertThrows(InvalidInputException.class, () -> calc.divide(Double.NaN, 1));
        assertThrows(InvalidInputException.class, () -> calc.sqrt(Double.NaN));
    }

    @Test
    void infinityThrowsInvalidInputInEveryMethod() {
        assertThrows(InvalidInputException.class, () -> calc.add(Double.POSITIVE_INFINITY, 1));
        assertThrows(InvalidInputException.class, () -> calc.subtract(1, Double.NEGATIVE_INFINITY));
        assertThrows(InvalidInputException.class, () -> calc.multiply(Double.POSITIVE_INFINITY, 2));
        assertThrows(InvalidInputException.class, () -> calc.divide(1, Double.POSITIVE_INFINITY));
        assertThrows(InvalidInputException.class, () -> calc.sqrt(Double.POSITIVE_INFINITY));
    }

    @Test
    void sqrtOfNegativeThrowsInvalidInput() {
        InvalidInputException ex = assertThrows(InvalidInputException.class, () -> calc.sqrt(-4));
        assertTrue(ex.getMessage().contains("від'ємного"));
    }

    @Test
    void validationHappensBeforeDivisionByZeroCheck() {
        // NaN / 0 -> спочатку валідація, тому саме InvalidInputException, а не ArithmeticException
        assertThrows(InvalidInputException.class, () -> calc.divide(Double.NaN, 0));
    }

    @Test
    void invalidInputExceptionKeepsMessage() {
        InvalidInputException ex = new InvalidInputException("test");
        assertEquals("test", ex.getMessage());
    }



    @Test
    void processRequestValidOperations() throws InvalidInputException {
        assertEquals(5.0, Main.processRequest("2", "3", "+"), DELTA);
        assertEquals(-1.0, Main.processRequest("2", "3", "-"), DELTA);
        assertEquals(6.0, Main.processRequest("2", "3", "*"), DELTA);
        assertEquals(2.0, Main.processRequest("6", "3", "/"), DELTA);
    }

    @Test
    void processRequestAcceptsCommaAndSpaces() throws InvalidInputException {
        assertEquals(4.0, Main.processRequest(" 1,5 ", " 2,5 ", " + "), DELTA);
    }

    @Test
    void processRequestStringInsteadOfNumber() {
        InvalidInputException ex = assertThrows(InvalidInputException.class,
                () -> Main.processRequest("abc", "2", "+"));
        assertTrue(ex.getMessage().contains("не число"));

        assertThrows(InvalidInputException.class, () -> Main.processRequest("2", "xyz", "+"));
        assertThrows(InvalidInputException.class, () -> Main.processRequest("", "2", "+"));
        assertThrows(InvalidInputException.class, () -> Main.processRequest("2", "   ", "*"));
        assertThrows(InvalidInputException.class, () -> Main.processRequest("1.2.3", "2", "-"));
    }

    @Test
    void processRequestUnknownOperation() {
        InvalidInputException ex = assertThrows(InvalidInputException.class,
                () -> Main.processRequest("2", "3", "%"));
        assertTrue(ex.getMessage().contains("Невідома операція"));

        assertThrows(InvalidInputException.class, () -> Main.processRequest("2", "3", ""));
        assertThrows(InvalidInputException.class, () -> Main.processRequest("2", "3", "plus"));
    }

    @Test
    void processRequestNanAndInfinityStringsAreRejected() {
        // Double.parseDouble("NaN") / ("Infinity") проходять парсинг, але відсікаються validate()
        assertThrows(InvalidInputException.class, () -> Main.processRequest("NaN", "1", "+"));
        assertThrows(InvalidInputException.class, () -> Main.processRequest("Infinity", "1", "*"));
    }

    @Test
    void processRequestDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> Main.processRequest("10", "0", "/"));
    }

    

    private InputStream originalIn;
    private PrintStream originalOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    void redirectIo() {
        originalIn = System.in;
        originalOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreIo() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private String runMain(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        Main.main(new String[0]);
        return out.toString(StandardCharsets.UTF_8);
    }

    @Test
    void mainPrintsResultAndFinally() {
        String output = runMain("6\n3\n/\n");
        assertTrue(output.contains("Результат: 2.0"));
        assertTrue(output.contains("Обробку запиту завершено."));
    }

    @Test
    void mainHandlesDivisionByZero() {
        String output = runMain("10\n0\n/\n");
        assertTrue(output.contains("Арифметична помилка: Ділення на нуль неможливе!"));
        assertTrue(output.contains("Обробку запиту завершено."));
    }

    @Test
    void mainHandlesNonNumericInput() {
        String output = runMain("abc\n2\n+\n");
        assertTrue(output.contains("Помилка вводу"));
        assertTrue(output.contains("Обробку запиту завершено."));
    }

    @Test
    void mainHandlesUnknownOperation() {
        String output = runMain("1\n2\n^\n");
        assertTrue(output.contains("Помилка вводу: Невідома операція"));
        assertTrue(output.contains("Обробку запиту завершено."));
    }

    @Test
    void mainHandlesEmptyInput() {
        String output = runMain("");
        assertTrue(output.contains("вхідні дані відсутні"));
        assertTrue(output.contains("Обробку запиту завершено."));
    }
}