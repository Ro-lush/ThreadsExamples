package ex4;

import jakarta.persistence.*;


@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int stock;  // Количество на складе

    @Version
    private Long version;  // Для оптимистичной блокировки

    // Геттеры и сеттеры
    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalStateException("Недостаточно товара");
        }
        this.stock -= quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStock(int i) {
        this.stock = i;
    }

    public int getStock() {
        return stock;
    }
}
