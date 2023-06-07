package ru.netology.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;

public class StartPage {
    private SelenideElement heading = Selenide.$x("//h2[text()='Путешествие дня']");
    private SelenideElement payButton = Selenide.$x("//span[text()='Купить']");
    private SelenideElement creditButton = Selenide.$x("//span[text()='Купить в кредит']");

    public StartPage() {
        heading.shouldBe(visible);
    }

    public PaymentPage paymentPage() {
        payButton.click();
        return new PaymentPage();
    }

    public CreditPage creditPage() {
        creditButton.click();
        return new CreditPage();
    }
}
