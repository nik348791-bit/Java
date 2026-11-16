package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class TransactionAnalyzerTest {

    private List<Transaction> sampleTransactions() {
        return Arrays.asList(
                new Transaction("05-01-2024", -100.0, "Їжа"),
                new Transaction("10-01-2024", -500.0, "Оренда"),
                new Transaction("15-01-2024", -20.0, "Їжа"),
                new Transaction("20-01-2024", 1000.0, "Зарплата"),
                new Transaction("05-02-2024", -900.0, "Техніка")
        );
    }

    @Test
    public void testCalculateTotalBalance() {
        Transaction transaction1 = new Transaction("01-01-2023", 100.0, "Дохід");
        Transaction transaction2 = new Transaction("02-01-2023", -50.0, "Витрата");
        Transaction transaction3 = new Transaction("03-01-2023", 150.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        double result = TransactionAnalyzer.calculateTotalBalance(transactions);

        Assertions.assertEquals(200.0, result, "Розрахунок загального балансу неправильний");
    }

    @Test
    public void testCountTransactionsByMonth() {
        Transaction transaction1 = new Transaction("01-02-2023", 50.0, "Дохід");
        Transaction transaction2 = new Transaction("15-02-2023", -20.0, "Витрата");
        Transaction transaction3 = new Transaction("05-03-2023", 100.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        int countFeb = TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2023");
        int countMar = TransactionAnalyzer.countTransactionsByMonth(transactions, "03-2023");

        Assertions.assertEquals(2, countFeb, "Кількість транзакцій за лютий неправильна");
        Assertions.assertEquals(1, countMar, "Кількість транзакцій за березень неправильна");
    }

    @Test
    public void testFindTopExpensesReturnsTenLargestSorted() {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            transactions.add(new Transaction("01-01-2024", -10.0 * i, "Витрата " + i));
        }
        transactions.add(new Transaction("02-01-2024", 5000.0, "Дохід"));

        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);

        Assertions.assertEquals(10, top.size());
        Assertions.assertEquals(-120.0, top.get(0).getAmount(), 0.001);
        Assertions.assertEquals(-30.0, top.get(9).getAmount(), 0.001);
        Assertions.assertTrue(top.stream().allMatch(t -> t.getAmount() < 0));
        for (int i = 1; i < top.size(); i++) {
            Assertions.assertTrue(top.get(i - 1).getAmount() <= top.get(i).getAmount());
        }
    }

    @Test
    public void testFindTopExpensesWithFewerThanTen() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("01-01-2024", -10.0, "А"),
                new Transaction("02-01-2024", 100.0, "Дохід"),
                new Transaction("03-01-2024", -30.0, "Б")
        );

        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);

        Assertions.assertEquals(2, top.size());
        Assertions.assertEquals("Б", top.get(0).getDescription());
    }

    @Test
    public void testFindMaxAndMinExpenseInPeriod() {
        List<Transaction> transactions = sampleTransactions();
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 1, 31);

        Optional<Transaction> max = TransactionAnalyzer.findMaxExpense(transactions, from, to);
        Optional<Transaction> min = TransactionAnalyzer.findMinExpense(transactions, from, to);

        Assertions.assertTrue(max.isPresent());
        Assertions.assertTrue(min.isPresent());
        Assertions.assertEquals(-500.0, max.get().getAmount(), 0.001);
        Assertions.assertEquals(-20.0, min.get().getAmount(), 0.001);
    }

    @Test
    public void testFindMaxExpenseIncludesPeriodBoundaries() {
        List<Transaction> transactions = sampleTransactions();
        LocalDate day = LocalDate.of(2024, 2, 5);

        Optional<Transaction> max = TransactionAnalyzer.findMaxExpense(transactions, day, day);

        Assertions.assertTrue(max.isPresent());
        Assertions.assertEquals("Техніка", max.get().getDescription());
    }

    @Test
    public void testFindExpensesInEmptyPeriodReturnsEmpty() {
        List<Transaction> transactions = sampleTransactions();
        LocalDate from = LocalDate.of(2024, 3, 1);
        LocalDate to = LocalDate.of(2024, 3, 31);

        Assertions.assertTrue(TransactionAnalyzer.findMaxExpense(transactions, from, to).isEmpty());
        Assertions.assertTrue(TransactionAnalyzer.findMinExpense(transactions, from, to).isEmpty());
    }

    @Test
    public void testSumExpensesByCategory() {
        Map<String, Double> result = TransactionAnalyzer.sumExpensesByCategory(sampleTransactions());

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(120.0, result.get("Їжа"), 0.001);
        Assertions.assertEquals(500.0, result.get("Оренда"), 0.001);
        Assertions.assertEquals(900.0, result.get("Техніка"), 0.001);
        Assertions.assertFalse(result.containsKey("Зарплата"));
    }

    @Test
    public void testSumExpensesByMonth() {
        Map<YearMonth, Double> result = TransactionAnalyzer.sumExpensesByMonth(sampleTransactions());

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(620.0, result.get(YearMonth.of(2024, 1)), 0.001);
        Assertions.assertEquals(900.0, result.get(YearMonth.of(2024, 2)), 0.001);
        Assertions.assertEquals(YearMonth.of(2024, 1), result.keySet().iterator().next());
    }
}