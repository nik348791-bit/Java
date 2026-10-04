package org.example;

public class Product {
    private int id;
    private String name;
    private double price;
    private String description;
    private Category category;

    public Product(int id, String name, double price, String desription, Category category)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = desription;
        this.category = category;
    }
    public int getid(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return "Товар{" +
                "id=" + id +
                ", назва='" + name + '\'' +
                ", ціна=" + price +
                ", опис='" + description + '\'' +
                ", категорія='" + category.getName() + '\'' +
                '}';
    }

}
