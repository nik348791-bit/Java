package org.example.storage;

import org.example.model.Clothing;
import org.example.model.Electronics;
import org.example.model.Product;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRepository {
    private final List<Product> products = new ArrayList<>();
    private final Faker faker = new Faker();

    public void generateFakeData(int count) {
        for (int i = 0; i < count; i++) {
            if (i % 2 == 0) {
                Electronics electronic = Electronics.builder()
                        .id(UUID.randomUUID().toString())
                        .name(faker.device().modelName())
                        .price(faker.number().randomDouble(2, 50, 2000))
                        .brand(faker.device().manufacturer())
                        .warrantyMonths(faker.number().numberBetween(6, 24))
                        .build();
                products.add(electronic);
            } else {
                Clothing clothing = Clothing.builder()
                        .id(UUID.randomUUID().toString())
                        .name(faker.commerce().productName())
                        .price(faker.number().randomDouble(2, 10, 300))
                        .size(faker.options().option("S", "M", "L", "XL"))
                        .material(faker.commerce().material()) // Виправлено з faker.fabric() на commerce().material()
                        .build();
                products.add(clothing);
            }
        }
    }

    public List<Product> getProducts() {
        return products;
    }
}