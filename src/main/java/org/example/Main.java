package org.example;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        String filePath = "https://informer.com.ua/dut/java/pr2.csv";

        List<Transaction> transactions = TransactionCSVReader.readTransactions(filePath);

        double totalBalance = TransactionAnalyzer.calculateTotalBalance(transactions);
        TransactionReportGenerator.printBalanceReport(totalBalance);

        String monthYear = "01-2024";
        int transactionsCount = TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear);
        TransactionReportGenerator.printTransactionsCountByMonth(monthYear, transactionsCount);

        List<Transaction> topExpenses = TransactionAnalyzer.findTopExpenses(transactions);
        TransactionReportGenerator.printTopExpensesReport(topExpenses);

        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 12, 31);
        Optional<Transaction> maxExpense = TransactionAnalyzer.findMaxExpense(transactions, from, to);
        Optional<Transaction> minExpense = TransactionAnalyzer.findMinExpense(transactions, from, to);
        TransactionReportGenerator.printExpenseExtremesReport(from + " - " + to, maxExpense, minExpense);

        Map<String, Double> byCategory = TransactionAnalyzer.sumExpensesByCategory(transactions);
        TransactionReportGenerator.printExpensesByCategoryReport(byCategory);

        Map<YearMonth, Double> byMonth = TransactionAnalyzer.sumExpensesByMonth(transactions);
        TransactionReportGenerator.printExpensesByMonthReport(byMonth);
    }
}