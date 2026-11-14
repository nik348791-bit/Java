package org.example.model;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@SuperBuilder
public class Electronics extends Product {
    private int warrantyMonths;
    private String brand;
}