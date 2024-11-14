package store;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import store.model.Product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {
    @DisplayName("올바르지 않은 제품 형식을 입력하면 예외가 발생한다.")
    @EmptySource //null 검사를 해야 하나?
    @ParameterizedTest
    @ValueSource(strings = {"[", "]", "[-", "-]", "[-]", "[감자칩-]", "[-3]",""," "})
    void throwWhenInvalidProductForm(String input) {
        assertThatThrownBy(() -> new Product(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 제품 이름을 입력하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"[포르쉐-3]"})
    void throwWhenInvalidProductName(String input) {
        assertThatThrownBy(() -> new Product(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("올바르지 않은 제품 수량을 입력하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"[감자칩-감]", "[감자칩-0]", "[감자칩-1000]"})
    void throwWhenInvalidProductQuantity(String input) {
        assertThatThrownBy(() -> new Product(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
