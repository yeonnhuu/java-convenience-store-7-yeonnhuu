package store.controller;

import static store.handler.ErrorHandler.PRODUCT_NOT_FOUND;
import static store.handler.ErrorHandler.QUANTITY_EXCEEDS_STOCK;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.Cart;
import store.domain.CartItem;
import store.domain.Product;
import store.handler.PromotionsFileHandler;
import store.handler.StocksFileHandler;
import store.view.InputView;

public class CartManager {
    private List<Product> products;
    private final InputView inputView = new InputView();
    private Cart cart;
    private int totalCnt;
    private int promoSet;

    public CartManager() throws IOException {
        loadPromotionsAndStocks();
    }

    private void loadPromotionsAndStocks() throws IOException {
        new PromotionsFileHandler().loadPromotions();
        this.products = new StocksFileHandler().loadStocks();
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public List<Product> findProductsByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public Cart generateCart(String purchaseList) {
        cart = new Cart();
        String[] cartItems = purchaseList.split(",");
        for (String item : cartItems) {
            validateCartItem(item);
        }
        for (String item : cartItems) {
            processCartItem(item);
        }
        return cart;
    }

    private void validateCartItem(String item) {
        String[] parts = parseItem(item);
        String name = parts[0];
        int requestedCnt = Integer.parseInt(parts[1]);

        List<Product> matchingProducts = findProductsByName(name);
        validateProductAvailability(matchingProducts);
        validateStockAvailability(matchingProducts, requestedCnt);
    }

    private void processCartItem(String item) {
        String[] parts = parseItem(item);
        String name = parts[0];
        int requestedCnt = Integer.parseInt(parts[1]);

        List<Product> matchingProducts = findProductsByName(name);
        processPromotionsAndQuantities(matchingProducts, name, requestedCnt);
    }

    private String[] parseItem(String item) {
        return item.replaceAll("[\\[\\]]", "").split("-");
    }

    private void processPromotionsAndQuantities(List<Product> products, String name, int requestedCnt) {
        int remainingCnt = requestedCnt;
        totalCnt = requestedCnt;
        promoSet = 0;
        for (Product product : products) {
            if (remainingCnt <= 0) {
                break;
            }
            remainingCnt = handleProductPromotion(product, name, remainingCnt);
        }
        cart.addItem(new CartItem(products.getFirst(), totalCnt, promoSet));
    }

    private int handleProductPromotion(Product product, String name, int remainingCnt) {
        int availableCnt = Math.min(remainingCnt, product.getQuantity());
        if (!product.hasPromotion()) {
            product.reduceQuantity(remainingCnt);
            return 0;
        }
        promoSet = calculatePromoSet(product, availableCnt);
        remainingCnt = processIncompletePromoSet(product, name, remainingCnt);
        remainingCnt = processExactPromoSet(product, name, remainingCnt);
        return remainingCnt;
    }

    private int processIncompletePromoSet(Product product, String name, int remainingCnt) {
        if (isExactPromoSet(product, remainingCnt) && (promoSet + 1) * product.promoCycle() <= product.getQuantity()) {
            String response = inputView.readAdditionalPromotion(name, calculateNonPromoCount(product, remainingCnt));
            if (response.equals("Y")) {
                promoSet += 1;
                totalCnt += product.promoFree();
            }
        }
        product.reduceQuantity(promoSet * product.promoCycle());
        return Math.max(remainingCnt - promoSet * product.promoCycle(), 0);
    }

    private int processExactPromoSet(Product product, String name, int remainingCnt) {
        if (!hasIncompletePromoSet(product, remainingCnt)) {
            return remainingCnt;
        }
        String response = inputView.readProceedWithoutPromotion(name, remainingCnt);
        int nonPromoCnt = calculateNonPromoCount(product, remainingCnt);
        if (response.equals("N")) {
            totalCnt -= nonPromoCnt;
            return remainingCnt;
        }
        remainingCnt -= nonPromoCnt;
        product.reduceQuantity(nonPromoCnt);
        return remainingCnt;
    }

    private void validateProductAvailability(List<Product> products) {
        if (products.isEmpty()) {
            throw PRODUCT_NOT_FOUND.getException();
        }
    }

    private void validateStockAvailability(List<Product> products, int requestedQuantity) {
        int availableQuantity = products.stream().mapToInt(Product::getQuantity).sum();
        if (availableQuantity < requestedQuantity) {
            throw QUANTITY_EXCEEDS_STOCK.getException();
        }
    }

    private boolean hasIncompletePromoSet(Product product, int quantity) {
        return quantity % product.promoCycle() != 0;
    }

    private boolean isExactPromoSet(Product product, int quantity) {
        int remainder = quantity % product.promoCycle();
        return remainder > 0 && remainder % product.promoBuy() == 0;
    }

    private int calculateNonPromoCount(Product product, int quantity) {
        int remainder = quantity % product.promoCycle();
        if (remainder % product.promoCycle() == 0) {
            return product.promoFree();
        }
        return remainder;
    }

    private int calculatePromoSet(Product product, int quantity) {
        return quantity / product.promoCycle();
    }
}
