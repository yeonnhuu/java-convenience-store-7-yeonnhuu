package store.handler;

public enum ErrorHandler {
    INVALID_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    QUANTITY_EXCEEDS_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    GENERIC_INVALID_INPUT("잘못된 입력입니다. 다시 입력해 주세요.");

    private final String message;

    ErrorHandler(String message) {
        this.message = message;
    }

    public IllegalArgumentException getException() {
        return new IllegalArgumentException(message);
    }
}
