package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;

public class StartPage {

    private SelenideElement heading = $$("h2").find(text("Путешествие дня"));
    private SelenideElement paymentButton = $$("button").find(exactText("Купить"));
    private SelenideElement creditButton = $$("button").find(exactText("Купить в кредит"));

    public StartPage() {
        heading.shouldBe(visible);
    }

    public PaymentPage paymentPage() {
        paymentButton.click();
        return new PaymentPage();
    }

    public CreditPage creditPage() {
        creditButton.click();
        return new CreditPage();
    }
}




