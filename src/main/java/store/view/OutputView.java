package store.view;

import java.util.List;
import store.domain.CartItem;
import store.domain.Product;
import store.domain.Receipt;

public class OutputView {

    public void printErrorMessage(String message) {
        System.out.println("[ERROR] " + message);
    }

    public void printWelcomeMessage() {
        System.out.println("\n안녕하세요. W편의점입니다.");
    }

    public void printStockOverviewMessage() {
        System.out.println("현재 보유하고 있는 상품입니다.\n");
    }

    public void printStockItemDetails(List<Product> products) {
        for (Product product : products) {
            String stockStatus = formatStockStatus(product);
            String promotionName = formatPromotionName(product);
            System.out.printf("- %s %,d원 %s %s%n", product.getName(), product.getPrice(), stockStatus, promotionName);
        }
    }

    public void printReceipt(Receipt receipt) {
        printReceiptHeader();
        printReceiptItems(receipt);
        printFreeItems(receipt);
        printReceiptFooter(receipt);
    }

    private void printReceiptHeader() {
        System.out.println("\n==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");
    }

    private void printReceiptItems(Receipt receipt) {
        for (CartItem item : receipt.getItems()) {
            System.out.printf("%s\t\t%-2d\t%,-6d%n", item.getName(), item.calcTotalQuantity(), item.totalPrice());
        }
    }

    private void printFreeItems(Receipt receipt) {
        if (!receipt.getFreeItems().isEmpty()) {
            System.out.println("=============증\t정===============");
            for (CartItem freeItem : receipt.getFreeItems()) {
                System.out.printf("%s\t\t%-2d%n", freeItem.getName(), freeItem.calcFreeQuantity());
            }
        }
    }

    private void printReceiptFooter(Receipt receipt) {
        System.out.println("====================================");
        System.out.printf("총구매액\t\t%d\t%,d%n", receipt.getTotalQuantity(), receipt.getTotalAmountBeforeDiscount());
        System.out.printf("행사할인\t\t\t-%,d%n", receipt.getPromotionDiscount());
        System.out.printf("멤버십할인\t\t\t-%,d%n", receipt.getMembershipDiscount());
        System.out.printf("내실돈\t\t\t %,-6d%n", receipt.getFinalPrice());
    }

    private String formatStockStatus(Product product) {
        if (product.getQuantity() > 0) {
            return product.getQuantity() + "개";
        }
        return "재고 없음";
    }

    private String formatPromotionName(Product product) {
        if (product.hasPromotion()) {
            return product.promoName();
        }
        return "";
    }
}
