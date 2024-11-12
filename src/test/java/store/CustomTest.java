package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class CustomTest extends NsTest {
    @Test
    void 올바르지_않은_형식_예외_테스트() {
        assertSimpleTest(() -> {
            runException("오렌지주스-1");
            assertThat(output()).contains("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        });
    }

    @Test
    void 재고_수량_초과_예외_테스트() {
        assertSimpleTest(() -> {
            runException("[오렌지주스-20]");
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        });
    }

    @Test
    void 존재하지_않는_상품_예외_테스트() {
        assertSimpleTest(() -> {
            runException("[오렌지쥬스-1]");
            assertThat(output()).contains("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        });
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
