package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class TransactionCSVReaderTest {

    @TempDir
    Path tempDir;

    @Test
    public void testReadTransactionsFromCsv() throws IOException {
        Path csv = tempDir.resolve("transactions.csv");
        String content = String.join("\n",
                "01-01-2024, 1000.50, Зарплата",
                "02-01-2024, -250.75, Продукти",
                "03-01-2024,-99.99,Транспорт");
        Files.writeString(csv, content, StandardCharsets.UTF_8);

        List<Transaction> result = TransactionCSVReader.readTransactions(csv.toString());

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("01-01-2024", result.get(0).getDate());
        Assertions.assertEquals(1000.50, result.get(0).getAmount(), 0.001);
        Assertions.assertEquals("Зарплата", result.get(0).getDescription());
        Assertions.assertEquals(-250.75, result.get(1).getAmount(), 0.001);
        Assertions.assertEquals("Транспорт", result.get(2).getDescription());
    }

    @Test
    public void testReadTransactionsSkipsHeaderAndInvalidLines() throws IOException {
        Path csv = tempDir.resolve("dirty.csv");
        String content = String.join("\n",
                "date,amount,description",
                "01-01-2024,-100,Їжа",
                "рядок без ком",
                "02-01-2024,abc,Помилка",
                "03-01-2024,-50,Кава");
        Files.writeString(csv, content, StandardCharsets.UTF_8);

        List<Transaction> result = TransactionCSVReader.readTransactions(csv.toString());

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Їжа", result.get(0).getDescription());
        Assertions.assertEquals("Кава", result.get(1).getDescription());
    }

    @Test
    public void testReadTransactionsMissingFileReturnsEmptyList() {
        List<Transaction> result = TransactionCSVReader.readTransactions(
                tempDir.resolve("missing.csv").toString());

        Assertions.assertTrue(result.isEmpty());
    }
}