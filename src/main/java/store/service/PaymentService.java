package store.service;

import store.controller.CartManager;
import store.domain.Cart;
import store.domain.Receipt;

public class PaymentService {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private final static int MEMBERSHIP_DISCOUNT_LIMIT = 8000;

    private final CartManager cartManager;
    private int membershipDiscountBalance;

    public PaymentService(CartManager cartManager) {
        this.cartManager = cartManager;
        this.membershipDiscountBalance = MEMBERSHIP_DISCOUNT_LIMIT;
    }

    public Cart getCartItems(String purchaseList) {
        return cartManager.generateCart(purchaseList);
    }

    public Receipt createReceipt(Cart cart, boolean applyMembership) {
        int membershipDiscount = calcMembershipDiscount(applyMembership, cart.totalRetailPrice());
        return new Receipt(cart, cart.totalPrice(), cart.totalFreePrice(), membershipDiscount);
    }

    private int calcMembershipDiscount(boolean apply, int eligibleAmount) {
        if (apply) {
            int discountAmount = Math.min((int) (eligibleAmount * MEMBERSHIP_DISCOUNT_RATE),
                    membershipDiscountBalance);
            membershipDiscountBalance -= discountAmount;

            return discountAmount;
        }
        return 0;
    }
}
