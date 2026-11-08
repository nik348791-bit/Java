package org.example;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static final Calculator calculator = new Calculator();

    // Окремий метод, щоб його можна було тестувати
    public static double processRequest(String first, String second, String operation)
            throws InvalidInputException {
        double a;
        double b;
        try {
            a = Double.parseDouble(first.trim().replace(',', '.'));
            b = Double.parseDouble(second.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Введено не число: \"" + first + "\" або \"" + second + "\"");
        }

        switch (operation.trim()) {
            case "+": return calculator.add(a, b);
            case "-": return calculator.subtract(a, b);
            case "*": return calculator.multiply(a, b);
            case "/": return calculator.divide(a, b);
            default:
                throw new InvalidInputException("Невідома операція: \"" + operation + "\". Доступні: + - * /");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Введіть перше число: ");
            String first = scanner.nextLine();

            System.out.print("Введіть друге число: ");
            String second = scanner.nextLine();

            System.out.print("Введіть операцію (+, -, *, /): ");
            String operation = scanner.nextLine();

            double result = processRequest(first, second, operation);
            System.out.println("Результат: " + result);

        } catch (InvalidInputException e) {
            System.out.println("Помилка вводу: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Арифметична помилка: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println("Помилка: вхідні дані відсутні.");
        } finally {
            System.out.println("Обробку запиту завершено.");
            scanner.close();
        }
    }
}
