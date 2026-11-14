package org.example;

import org.example.storage.OrderRepository;
import org.example.threading.OrderManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Ініціалізація багатомодульної багатопотокової системи управління замовленнями...");

        OrderRepository repository = new OrderRepository();
        repository.generateFakeData(6);

        OrderManager manager = new OrderManager(repository);

        System.out.println("\n[Старт] Паралельна обробка всіх замовлень:");
        manager.processOrdersConcurrently();

        manager.filterAndProcessExpensiveOrders(100.0);

        System.out.println("\n[Завершення] Усі замовлення опрацьовано.");
    }
}