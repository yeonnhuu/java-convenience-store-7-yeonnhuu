package store.handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.domain.Product;

public class StocksFileHandler {
    private static final Path STOCKS_FILE_PATH = Path.of("src/main/resources/stocks.md");

    private final ProductsFileHandler productsFileHandler;

    public StocksFileHandler() {
        this.productsFileHandler = new ProductsFileHandler(STOCKS_FILE_PATH);
    }

    public List<Product> loadStocks() throws IOException {
        ensureStocksFileExists();
        productsFileHandler.loadLatestProductsFile();
        updateStocksFile();
        return productsFileHandler.parseItemsFromFile(STOCKS_FILE_PATH);
    }

    private void ensureStocksFileExists() throws IOException {
        if (Files.notExists(STOCKS_FILE_PATH)) {
            Files.createFile(STOCKS_FILE_PATH);
        }
    }

    private void updateStocksFile() throws IOException {
        Map<String, Product> productMap = buildProductsMap();
        writeProductsToStocksFile(productMap);
    }

    private Map<String, Product> buildProductsMap() throws IOException {
        List<Product> products = productsFileHandler.parseItemsFromFile(STOCKS_FILE_PATH);
        Map<String, Product> productMap = new LinkedHashMap<>();
        for (Product product : products) {
            productMap.merge(product.getKeyForMap(), product, this::mergeProducts);
        }
        return productMap;
    }

    private Product mergeProducts(Product existing, Product toAdd) {
        return new Product(existing.getName(), existing.getPrice(),
                existing.getQuantity() + toAdd.getQuantity(), existing.getPromotion());
    }

    private void writeProductsToStocksFile(Map<String, Product> productMap) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(ProductsFileHandler.HEADER);
        productMap.values().forEach(product -> lines.add(productsFileHandler.formatProductLine(product)));
        Files.write(STOCKS_FILE_PATH, lines);
    }
}
