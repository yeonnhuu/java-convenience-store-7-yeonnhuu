package store.handler;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.Promotion;

public class PromotionsFileHandler {
    private static final Path PROMOTIONS_FILE_PATH = Path.of("src/main/resources/promotions.md");
    public static final String NON_PROMOTION_NAME = "null";
    public static final Promotion NON_PROMOTION = new Promotion(NON_PROMOTION_NAME, 1, 0);
    private static final Map<String, Promotion> promotions = new HashMap<>();

    public void loadPromotions() throws IOException {
        List<String> lines = Files.readAllLines(PROMOTIONS_FILE_PATH);
        promotions.put(NON_PROMOTION_NAME, NON_PROMOTION);
        lines.stream().skip(1).forEach(this::processPromotionLine); // Skip header line
    }

    private void processPromotionLine(String line) {
        String[] parts = parseAndTrimLine(line);
        String name = parts[0];
        int discountRate = Integer.parseInt(parts[1]);
        int limit = Integer.parseInt(parts[2]);

        if (isPromotionValid(parts)) {
            promotions.put(name, new Promotion(name, discountRate, limit));
        }
    }

    private String[] parseAndTrimLine(String line) {
        return Arrays.stream(line.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

    private boolean isPromotionValid(String[] parts) {
        LocalDate today = DateTimes.now().toLocalDate();
        LocalDate startDate = LocalDate.parse(parts[3]);
        LocalDate endDate = LocalDate.parse(parts[4]);
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public static Promotion getPromotionByName(String name) {
        return promotions.getOrDefault(name, promotions.get(NON_PROMOTION_NAME));
    }
}
