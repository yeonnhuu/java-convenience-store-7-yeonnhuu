package store.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Receipt {
    private final Cart cart;
    private final int totalPrice;
    private final int promotionDiscount;
    private final int membershipDiscount;
    private final int finalPrice;

    public Receipt(Cart cart, int totalPrice, int promotionDiscount, int membershipDiscount) {
        this.cart = cart;
        this.totalPrice = totalPrice;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.finalPrice = totalPrice - promotionDiscount - membershipDiscount;
    }

    public List<CartItem> getItems() {
        return cart.getItems();
    }

    public int getTotalAmountBeforeDiscount() {
        return totalPrice;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public int getTotalQuantity() {
        return cart.totalQuantity();
    }

    public List<CartItem> getFreeItems() {
        return cart.getItems().stream()
                .filter(item -> item.calcFreeQuantity() > 0)
                .collect(Collectors.toList());
    }
}
