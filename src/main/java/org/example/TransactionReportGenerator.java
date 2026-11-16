package org.example;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public abstract class TransactionReportGenerator {


    public static final double STAR_UNIT = 1000.0;

    private TransactionReportGenerator() {
    }

    public static void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("10 найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount());
        }
    }

    public static void printExpenseExtremesReport(String period,
                                                  Optional<Transaction> max,
                                                  Optional<Transaction> min) {
        System.out.println("Витрати за період " + period + ":");
        System.out.println("  Найбільша: " + formatTransaction(max));
        System.out.println("  Найменша:  " + formatTransaction(min));
    }

    public static void printExpensesByCategoryReport(Map<String, Double> byCategory) {
        System.out.println("Витрати по категоріях (1 * = " + (int) STAR_UNIT + " грн):");
        for (Map.Entry<String, Double> entry : byCategory.entrySet()) {
            System.out.println(formatBar(entry.getKey(), entry.getValue()));
        }
    }

    public static void printExpensesByMonthReport(Map<YearMonth, Double> byMonth) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM-yyyy");
        System.out.println("Витрати по місяцях (1 * = " + (int) STAR_UNIT + " грн):");
        for (Map.Entry<YearMonth, Double> entry : byMonth.entrySet()) {
            System.out.println(formatBar(entry.getKey().format(monthFormatter), entry.getValue()));
        }
    }

    public static String stars(double amount) {
        if (amount <= 0) {
            return "";
        }
        int count = Math.max(1, (int) Math.round(amount / STAR_UNIT));
        return "*".repeat(count);
    }

    private static String formatBar(String label, double amount) {
        return String.format(Locale.US, "  %-20s %10.2f грн | %s", label, amount, stars(amount));
    }

    private static String formatTransaction(Optional<Transaction> transaction) {
        return transaction
                .map(t -> t.getDate() + " " + t.getDescription() + ": " + t.getAmount())
                .orElse("немає витрат");
    }
}