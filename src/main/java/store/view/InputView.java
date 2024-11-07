package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.validator.CommonValidator;

public class InputView {
    private final CommonValidator commonInputValidator = new CommonValidator();

    public String getProductAndPrice() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return getInput();
    }

    public boolean getMembershipDiscountOrNot() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return getYesOrNo();
    }

    public boolean getAdditionalPurchase() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return getYesOrNo();
    }

    private String getInput() {
        String input = Console.readLine();
        commonInputValidator.validateEmpty(input);
        return input;
    }

    private boolean getYesOrNo() {
        String input = Console.readLine();
        commonInputValidator.validateEmpty(input);
        if (input == "Y") {
            return true;
        }
        return false;
    }
}
