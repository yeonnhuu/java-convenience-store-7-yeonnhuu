package store.domain;

import java.util.Objects;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private final Promotion promotion;

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String promoName() {
        return promotion.getName();
    }

    public void reduceQuantity(int amount) {
        quantity -= amount;
    }

    public String getKeyForMap() {
        return name + "-" + promoName();
    }

    public boolean hasPromotion() {
        return !Objects.equals(promoName(), "null");
    }

    public int promoCycle() {
        return promoBuy() + promoFree();
    }

    public int promoBuy() {
        return promotion.getBuy();
    }

    public int promoFree() {
        return promotion.getFree();
    }
}
