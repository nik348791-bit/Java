package org.example;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TransactionAnalyzer {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MM-yyyy");

    public static double calculateTotalBalance(List<Transaction> transactions) {
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }
        return balance;
    }

    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        int count = 0;
        for (Transaction transaction : transactions) {
            String transactionMonthYear = parseDate(transaction).format(monthYearFormatter);
            if (transactionMonthYear.equals(monthYear)) {
                count++;
            }
        }
        return count;
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparingDouble(Transaction::getAmount))
                .limit(10)
                .collect(Collectors.toList());
    }


    public static Optional<Transaction> findMaxExpense(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return expensesInPeriod(transactions, from, to)
                .min(Comparator.comparingDouble(Transaction::getAmount));
    }


    public static Optional<Transaction> findMinExpense(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return expensesInPeriod(transactions, from, to)
                .max(Comparator.comparingDouble(Transaction::getAmount));
    }


    public static Map<String, Double> sumExpensesByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        Transaction::getDescription,
                        TreeMap::new,
                        Collectors.summingDouble(t -> -t.getAmount())));
    }


    public static Map<YearMonth, Double> sumExpensesByMonth(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(parseDate(t)),
                        TreeMap::new,
                        Collectors.summingDouble(t -> -t.getAmount())));
    }

    private static Stream<Transaction> expensesInPeriod(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate date = parseDate(t);
                    return !date.isBefore(from) && !date.isAfter(to);
                });
    }

    private static LocalDate parseDate(Transaction transaction) {
        return LocalDate.parse(transaction.getDate(), dateFormatter);
    }
}