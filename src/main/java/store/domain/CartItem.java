package store.domain;

public class CartItem {
    private final Product product;
    private final int totalQuantity;
    private final int promotionSet;

    public CartItem(Product product, int totalQuantity, int promotionSet) {
        this.product = product;
        this.totalQuantity = totalQuantity;
        this.promotionSet = promotionSet;
    }

    public String getName() {
        return product.getName();
    }

    public int totalPrice() {
        return totalRetailPrice() + totalPromoPrice();
    }

    public int totalRetailPrice() {
        return product.getPrice() * (totalQuantity - calcPromoQuantity());
    }

    public int totalPromoPrice() {
        return product.getPrice() * calcPromoQuantity();
    }

    public int totalFreePrice() {
        return product.getPrice() * calcFreeQuantity();
    }

    public int calcTotalQuantity() {
        return totalQuantity;
    }

    private int calcPromoQuantity() {
        return promotionSet * product.promoCycle();
    }

    public int calcFreeQuantity() {
        return promotionSet * product.promoFree();
    }
}
