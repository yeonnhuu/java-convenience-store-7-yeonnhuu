package store;

import java.io.IOException;
import store.controller.StoreManager;

public class Application {
    public static void main(String[] args) {
        try {
            StoreManager storeManager = new StoreManager();
            storeManager.run();
        } catch (IOException e) {
            System.err.println("[ERROR] 프로그램 실행 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
