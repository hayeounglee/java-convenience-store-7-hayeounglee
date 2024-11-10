package store;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.model.receipt.PurchaseProducts;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PurchaseProductsTest {
    @DisplayName("올바르지 않는 형식으로 제품을 나열하여 입력하면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"[감자칩-3],", ",[감자칩-3]", "[감자칩-3],,"})
    void throwWhenInvalidProductName(String input) {
        assertThatThrownBy(() -> new PurchaseProducts(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
