package store.controller;

import static store.handler.ErrorHandler.GENERIC_INVALID_INPUT;
import static store.handler.ErrorHandler.INVALID_FORMAT;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import store.domain.Cart;
import store.domain.Product;
import store.domain.Receipt;
import store.service.PaymentService;
import store.view.InputView;
import store.view.OutputView;

public class StoreManager {
    private static final Pattern PURCHASE_LIST_PATTERN = Pattern.compile(
            "\\[(\\p{L}+)-(\\d+)](,\\[(\\p{L}+)-(\\d+)])*");

    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();
    private final PaymentService paymentService;
    private final List<Product> products;

    public StoreManager() throws IOException {
        CartManager cartManager = new CartManager();
        paymentService = new PaymentService(cartManager);
        products = cartManager.getAllProducts();
    }

    public void run() {
        boolean continueShopping;
        do {
            displayStoreProducts();
            Cart cart = getValidCart();
            boolean applyMembership = getMembershipStatus();
            processPayment(cart, applyMembership);
            continueShopping = checkContinueShopping();
        } while (continueShopping);
    }

    private void displayStoreProducts() {
        outputView.printWelcomeMessage();
        outputView.printStockOverviewMessage();
        outputView.printStockItemDetails(products);
    }

    private Cart getValidCart() {
        return repeatUntilSuccess(() -> {
            String purchaseList = inputView.readPurchaseList();
            validatePurchaseList(purchaseList);
            return paymentService.getCartItems(purchaseList);
        });
    }

    private boolean getMembershipStatus() {
        return repeatUntilSuccess(() -> {
            String input = inputView.readMembershipDiscount();
            validateYesNoInput(input);
            return input.equals("Y");
        });
    }

    private void processPayment(Cart cart, boolean applyMembership) {
        Receipt receipt = paymentService.createReceipt(cart, applyMembership);
        outputView.printReceipt(receipt);
    }

    private boolean checkContinueShopping() {
        return repeatUntilSuccess(() -> {
            String input = inputView.readContinueShopping();
            validateYesNoInput(input);
            return input.equals("Y");
        });
    }

    private void validatePurchaseList(String input) {
        if (!PURCHASE_LIST_PATTERN.matcher(input).matches()) {
            throw INVALID_FORMAT.getException();
        }
    }

    private void validateYesNoInput(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw GENERIC_INVALID_INPUT.getException();
        }
    }

    private <T> T repeatUntilSuccess(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }
}
