package ru.netology.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class CreditPage {
    private SelenideElement heading = Selenide.$x("//h3[text()='Кредит по данным карты']");
    private SelenideElement cardNumber = Selenide.$x("//span[text()='Номер карты']/following-sibling::span/input");
    private SelenideElement month = Selenide.$x("//span[text()='Месяц']/following-sibling::span/input");
    private SelenideElement year = Selenide.$x("//span[text()='Год']/following-sibling::span/input");
    private SelenideElement holder = Selenide.$x("//span[text()='Владелец']/following-sibling::span/input");
    private SelenideElement cvc = Selenide.$x("//span[text()='CVC/CVV']/following-sibling::span/input");
    private SelenideElement buttonNext = Selenide.$x("//span[text()='Продолжить']");
    private SelenideElement errorRestricted = Selenide.$x("//div[text()='Ошибка!" + " Банк отказал в " +
            "проведении операции.']");
    private SelenideElement success = Selenide.$x("//div[text()='Успешно']");

    private SelenideElement wrongCardNumberField = Selenide.$x("//span[text()='Номер карты']" +
            "/following-sibling::span[@class='input__sub']");
    private SelenideElement wrongMonthField = Selenide.$x("//span[text()='Месяц']" +
            "/following-sibling::span[@class='input__sub']");
    private SelenideElement wrongYearField = Selenide.$x("//span[text()='Год']" +
            "/following-sibling::span[@class='input__sub']");
    private SelenideElement wrongCardHolderField = Selenide.$x("//span[text()='Владелец']" +
            "/following-sibling::span[@class='input__sub']");
    private SelenideElement wrongCvcField = Selenide.$x("//span[text()='CVC/CVV']" +
            "/following-sibling::span[@class='input__sub']");

    public CreditPage() {
        heading.shouldBe(visible);
    }

    public void insertCardData(DataHelper.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        month.setValue(cardInfo.getMonth());
        year.setValue(cardInfo.getYear());
        holder.setValue(cardInfo.getCardHolder());
        cvc.setValue(cardInfo.getCvc());
        buttonNext.click();
    }

    public void errorRestricted() {
        errorRestricted.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void clickButtonNext() {
        buttonNext.click();
    }

    public void confirmationOfBank() {
        success.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void insertPayCardIfEmptyCardNumber(DataHelper.CardInfo cardInfo) {
        month.setValue(cardInfo.getMonth());
        year.setValue(cardInfo.getYear());
        holder.setValue(cardInfo.getCardHolder());
        cvc.setValue(cardInfo.getCvc());
        clickButtonNext();
    }

    public void insertPayCardIfEmptyMonth(DataHelper.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        year.setValue(cardInfo.getYear());
        holder.setValue(cardInfo.getCardHolder());
        cvc.setValue(cardInfo.getCvc());
        clickButtonNext();
    }

    public void insertPayCardEmptyHolder(DataHelper.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        month.setValue(cardInfo.getMonth());
        year.setValue(cardInfo.getYear());
        cvc.setValue(cardInfo.getCvc());
        clickButtonNext();
    }

    public void insertPayCardEmptyYear(DataHelper.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        month.setValue(cardInfo.getMonth());
        holder.setValue(cardInfo.getCardHolder());
        cvc.setValue(cardInfo.getCvc());
        clickButtonNext();
    }

    public void wrongNumberCard(String attentionText) {
        wrongCardNumberField.shouldBe(visible);
        wrongCardNumberField.shouldHave(text(attentionText));
    }

    public void insertPayCardEmptyCVC(DataHelper.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        month.setValue(cardInfo.getMonth());
        year.setValue(cardInfo.getYear());
        holder.setValue(cardInfo.getCardHolder());
        clickButtonNext();
    }

    public void wrongYear(String attentionText) {
        wrongYearField.shouldBe(visible);
        wrongYearField.shouldHave(text(attentionText));
    }

    public void wrongMonth(String attentionText) {
        wrongMonthField.shouldBe(visible);
        wrongMonthField.shouldHave(text(attentionText));
    }

    public void attentionUnderCVC(String attentionText) {
        wrongCvcField.shouldBe(visible);
        wrongCvcField.shouldHave(text(attentionText));
    }

    public void wrongName(String attentionText) {
        wrongCardHolderField.shouldBe(visible);
        wrongCardHolderField.shouldHave(text(attentionText));
    }
}