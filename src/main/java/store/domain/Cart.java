package store.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart {
    private final List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        items.add(item);
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int totalQuantity() {
        return items.stream().mapToInt(CartItem::calcTotalQuantity).sum();
    }

    public int totalPrice() {
        return items.stream().mapToInt(CartItem::totalPrice).sum();
    }

    public int totalRetailPrice() {
        return items.stream().mapToInt(CartItem::totalRetailPrice).sum();
    }

    public int totalFreePrice() {
        return items.stream().mapToInt(CartItem::totalFreePrice).sum();
    }
}

