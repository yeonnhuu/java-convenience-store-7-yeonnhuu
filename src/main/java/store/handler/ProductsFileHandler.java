package store.handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.domain.Product;

public class ProductsFileHandler {
    private static final Path PRODUCTS_FILE_PATH = Path.of("src/main/resources/products.md");
    public static final String HEADER = "name,price,quantity,promotion";

    private final Path latestFilePath;

    public ProductsFileHandler(Path latestFilePath) {
        this.latestFilePath = latestFilePath;
    }

    public void loadLatestProductsFile() throws IOException {
        List<Product> products = parseItemsFromFile(PRODUCTS_FILE_PATH);
        Map<String, Boolean> nonPromotionalEntries = identifyNonPromotionalProducts(products);
        List<String> lines = buildProductLines(products, nonPromotionalEntries);
        Files.write(latestFilePath, lines);
    }

    public List<Product> parseItemsFromFile(Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path)) {
            return lines.skip(1)
                    .map(this::parseProductLine)
                    .collect(Collectors.toList());
        }
    }

    public boolean shouldAddNonPromotional(Product product, Map<String, Boolean> nonPromotionalEntries) {
        return product.hasPromotion() && !nonPromotionalEntries.getOrDefault(product.getName(), false);
    }

    public String formatProductLine(Product product) {
        return String.format("%s,%d,%d,%s", product.getName(), product.getPrice(),
                product.getQuantity(), product.promoName());
    }

    private Product parseProductLine(String line) {
        String[] parts = line.split(",");
        return new Product(
                parts[0].trim(),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim()),
                PromotionsFileHandler.getPromotionByName(parts[3].trim())
        );
    }

    private Map<String, Boolean> identifyNonPromotionalProducts(List<Product> products) {
        Map<String, Boolean> nonPromotionalEntries = new HashMap<>();
        products.stream()
                .filter(product -> !product.hasPromotion())
                .forEach(product -> nonPromotionalEntries.put(product.getName(), true));
        return nonPromotionalEntries;
    }

    private List<String> buildProductLines(List<Product> products, Map<String, Boolean> nonPromotionalEntries) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        products.forEach(product -> addProductLines(lines, product, nonPromotionalEntries));
        return lines;
    }

    private void addProductLines(List<String> lines, Product product, Map<String, Boolean> nonPromotionalEntries) {
        lines.add(formatProductLine(product));
        if (shouldAddNonPromotional(product, nonPromotionalEntries)) {
            lines.add(formatProductLine(
                    new Product(product.getName(), product.getPrice(), 0, PromotionsFileHandler.NON_PROMOTION)));
            nonPromotionalEntries.put(product.getName(), true);
        }
    }
}
