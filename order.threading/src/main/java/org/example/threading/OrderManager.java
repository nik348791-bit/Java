package org.example.threading;

import org.example.exception.OrderProcessingException;
import org.example.model.Product;
import org.example.processing.OrderProcessor;
import org.example.storage.OrderRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OrderManager {
    private final OrderRepository repository;

    public OrderManager(OrderRepository repository) {
        this.repository = repository;
    }

    public void processOrdersConcurrently() {
        List<Product> products = repository.getProducts();

        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
            for (Product product : products) {
                // Використання лямбда-виразів для завдань потоків
                Runnable task = () -> {
                    try {
                        OrderProcessor<Product> processor = new OrderProcessor<>(product);
                        processor.processOrder();
                    } catch (OrderProcessingException e) {
                        System.err.println("Помилка обробки: " + e.getMessage());
                    }
                };
                executor.submit(task);
            }

            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Виконання потоків було перервано.");
        }
    }

    public void filterAndProcessExpensiveOrders(double threshold) {
        System.out.println("\n--- Фільтрація та обробка дорогих товарів (> $" + threshold + ") ---");

        List<Product> products = repository.getProducts();

        products.stream()
                .filter(p -> p.getPrice() > threshold) // Лямбда-фільтрація
                .map(OrderProcessor::new)              // Посилання на конструктор (Method Reference)
                .forEach(processor -> {
                    try {
                        processor.processOrder();
                    } catch (OrderProcessingException e) {
                        System.err.println(e.getMessage());
                    }
                });
    }
}