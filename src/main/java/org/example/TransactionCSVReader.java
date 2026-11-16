package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class TransactionCSVReader {

    public static List<Transaction> readTransactions(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = openReader(filePath)) {
            String line;
            while ((line = br.readLine()) != null) {
                Transaction transaction = parseLine(line);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private static BufferedReader openReader(String filePath) throws IOException {
        if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
            return new BufferedReader(new InputStreamReader(
                    URI.create(filePath).toURL().openStream(), StandardCharsets.UTF_8));
        }
        return Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);
    }


    private static Transaction parseLine(String line) {
        String[] values = line.split(",");
        if (values.length < 3) {
            return null;
        }
        try {
            return Transaction.builder()
                    .date(values[0].trim())
                    .amount(Double.parseDouble(values[1].trim()))
                    .description(values[2].trim())
                    .build();
        } catch (NumberFormatException e) {
            return null;
        }
    }
}