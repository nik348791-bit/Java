package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class TransactionCSVReader {

    // Статичний метод для читання транзакцій
    public static List<Transaction> readTransactions(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            BufferedReader br;
            if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
                URL url = new URL(filePath);
                br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            } else {
                br = Files.newBufferedReader(Paths.get(filePath));
            }

            try (br) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values.length >= 3) {
                        Transaction transaction = Transaction.builder()
                                .date(values[0].trim())
                                .amount(Double.parseDouble(values[1].trim()))
                                .description(values[2].trim())
                                .build();
                        transactions.add(transaction);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}