package org.example.processing;

import org.example.exception.OrderProcessingException;
import org.example.model.Product;

public class OrderProcessor<T extends Product> {
    private final T product;

    public OrderProcessor(T product) {
        this.product = product;
    }

    public void processOrder() throws OrderProcessingException {
        if (product == null) {
            throw new OrderProcessingException("Помилка: Товар не може бути null!");
        }

        System.out.printf("[Потік: %s] Успішно оброблено замовлення: %s (Ціна: $%.2f)%n",
                Thread.currentThread().getName(),
                product.getName(),
                product.getPrice());
    }

    public T getProduct() {
        return product;
    }
}