package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Category {
    private int id; // Унікальний ідентифікатор категорії
    private String name; // Назва категорії
}
