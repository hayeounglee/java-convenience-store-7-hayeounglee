package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.model.Product;
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

    public boolean getOneMoreFree(Product product) {
        System.out.printf("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)", product.getName());
        System.out.println();
        return getYesOrNo();
    }

    public boolean getPurchaseOrNot(String name, int count) {
        System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", name, count);
        System.out.println();
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
        if (input.equals("Y")) {
            return true;
        }
        return false;
    }
}
