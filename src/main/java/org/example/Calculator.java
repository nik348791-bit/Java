package org.example;

public class Calculator {

    public double add(double a, double b) throws InvalidInputException {
        validate(a, b);
        return a + b;
    }

    public double subtract(double a, double b) throws InvalidInputException {
        validate(a, b);
        return a - b;
    }

    public double multiply(double a, double b) throws InvalidInputException {
        validate(a, b);
        return a * b;
    }

    public double divide(double a, double b) throws InvalidInputException {
        validate(a, b);
        if (b == 0) {
            throw new ArithmeticException("Ділення на нуль неможливе!");
        }
        return a / b;
    }


    public double sqrt(double a) throws InvalidInputException {
        validate(a);
        if (a < 0) {
            throw new InvalidInputException("Не можна добути квадратний корінь з від'ємного числа: " + a);
        }
        return Math.sqrt(a);
    }


    private void validate(double... values) throws InvalidInputException {
        for (double v : values) {
            if (Double.isNaN(v) || Double.isInfinite(v)) {
                throw new InvalidInputException("Некоректне число: " + v);
            }
        }
    }
}
