package store.validator;

public class CommonValidator {
    public void validateEmpty(String input) {
        if (input.isBlank() || input.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }
}
