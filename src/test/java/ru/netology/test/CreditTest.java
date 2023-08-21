package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.page.StartPage;

import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;


public class CreditTest {

    StartPage page = open("http://localhost:8080/", StartPage.class);

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    void tearDown() {
        closeWindow();
    }

    // Кредитная карта одобрена
    @Test
    @DisplayName("The credit card must be approved")
    void shouldSuccessTransactionWithCreditCard() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithApprovedCard();
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }

    // Кредитная карта отклонена
    @Test
    @DisplayName("The credit card must be declined")
    void shouldUnSuccessTransactionWithCreditCard() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithDeclineCard();
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkErrorMsgDeclineFromBank();
    }

    //    Кредитная карта должна быть одобрена с указанием месяца одной цифрой
    @Test
    @DisplayName("A credit card must be approved indicating the month in one digit")
    void shouldSuccessTransactionWithMonthWithoutZero() {
        var creditPage = page.creditPage();
        var validYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 1;
        var cardInfo = DataGenerator.dataWithApprovedCardAndParametrizedMonthAndYear
                ("8", String.valueOf(validYear));
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }

    //    Кредитная карта с максимальной датой должна быть одобрена
    @Test
    @DisplayName("A credit card with a maximum date must be approved")
    void shouldSuccessTransactionWithMaxAllowedDate() {
        var creditPage = page.creditPage();
        var currentMonth = DataGenerator.getCurrentMonth();
        var maxYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 5;
        var cardInfo = DataGenerator.dataWithApprovedCardAndParametrizedMonthAndYear(currentMonth,
                String.valueOf(maxYear));
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }

    //    Кредитная карта с минимальной датой (текущий месяц) должна быть одобрена
    @Test
    @DisplayName("A credit card with a minimum date (current month) must be approved")
    void shouldSuccessTransactionWithMinAllowedDate() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithApprovedCardAndParametrizedMonthAndYear
                (DataGenerator.getCurrentMonth(), DataGenerator.getCurrentYear());
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }

    //    Кредитная карта со сроком действия в следующем месяце должна быть одобрена
    @Test
    @DisplayName("A credit card with an expiration date next month must be approved")
    void shouldSuccessTransactionWithPreMinAllowedDate() {
        var creditPage = page.creditPage();
        var nextMonth = Integer.parseInt(DataGenerator.getCurrentMonth()) + 1;
        var cardInfo = DataGenerator.dataWithApprovedCardAndParametrizedMonthAndYear
                (String.valueOf(nextMonth), DataGenerator.getCurrentYear());
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkApprovedMsgFromBank();
    }


    //    Должна быть отклонена кредитная карта со случайным номером
    @Test
    @DisplayName("Should reject a credit card with a random number")
    void shouldDeclineWithRandomCreditCard() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithRandomCardNumber();
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkErrorMsgDeclineFromBank();
    }


    //    Должно отображаться красное предупреждение с просроченной картой на месяц
    @Test
    @DisplayName("A red warning with an expired card for a month should be displayed")
    void shouldShowMsgWithExpiredCardForMonth() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithAnExpiredCardForOneMonth();
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }


    //    Должно отображаться красное предупреждение с пустым полем номера карты
    @Test
    @DisplayName("A red warning should be displayed with an empty card number field")
    void shouldShowMsgWithEmptyCardNumberField() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithApprovedCard();
        cardInfo.setNumber("");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCardNumberField("Поле обязательно для заполнения");
    }

    //   Должно отображаться красное предупреждение с пустым полем месяца
    @Test
    @DisplayName("A red warning should be displayed with an empty month field")
    void shouldShowMsgWithEmptyMonthField() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithApprovedCard();
        cardInfo.setMonth("");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderMonthField("Поле обязательно для заполнения");
    }


    //    Должно отображаться красное предупреждение с пустым полем года
    @Test
    @DisplayName("A red warning should be displayed with an empty year field")
    void shouldShowMsgWithEmptyYearField() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithApprovedCard();
        cardInfo.setYear("");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderYearField("Поле обязательно для заполнения");
    }

    //    Должно отображаться красное предупреждение с пустым полем держателя карты
    @Test
    @DisplayName("A red warning should be displayed with an empty card holder field")
    void shouldShowMsgWithEmptyCardHolderField() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithApprovedCard();
        cardInfo.setHolder("");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCardHolderField("Поле обязательно для заполнения");
    }

    //    Должно отображаться красное предупреждение с пустым полем cvc
    @Test
    @DisplayName("A red warning should be displayed with an empty cvc field")
    void shouldShowMsgWithEmptyCvcField() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithApprovedCard();
        cardInfo.setCvc("");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCvcField("Поле обязательно для заполнения");
    }


    //    Красное предупреждение должно отображаться с пустым полем для всех
    @Test
    @DisplayName("A red warning should be displayed with an empty all field")
    void shouldShowMsgWithEmptyAllField() {
        var creditPage = page.creditPage();
        creditPage.clickContinueButton();
        creditPage.checkWarningUnderCardNumberField("Поле обязательно для заполнения");
        creditPage.checkWarningUnderMonthField("Поле обязательно для заполнения");
        creditPage.checkWarningUnderYearField("Поле обязательно для заполнения");
        creditPage.checkWarningUnderCardHolderField("Поле обязательно для заполнения");
        creditPage.checkWarningUnderCvcField("Поле обязательно для заполнения");
    }

    //    Должно отображаться красное предупреждение с неверными данными в поле месяца
    @Test
    @DisplayName("A red warning should be displayed with invalid data in the month field")
    void shouldShowMsgWithInvalidMonthData() {
        var creditPage = page.creditPage();
        var currentYear = DataGenerator.getCurrentYear();
        var cardInfo = DataGenerator.dataWithApprovedCardAndParametrizedMonthAndYear("13",
                currentYear);
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    //    Должно отображаться красное предупреждение с месяцем 00
    @Test
    @DisplayName("A red warning with 00 month should be displayed")
    void shouldShowMsgWhenMonthIsZero() {
        var creditPage = page.creditPage();
        var validYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 1;
        var cardInfo = DataGenerator.dataWithApprovedCardAndParametrizedMonthAndYear
                ("00", String.valueOf(validYear));
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }


    //    Должно появиться красное предупреждение с именем держателя карты, написанным кириллицей
    @Test
    @DisplayName("A red warning should be displayed with the cardholder's name written in Cyrillic")
    void shouldShowMsgWithCyrillicCardHolderName() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithParametrizedCardHolderName("Иванов Иван");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCardHolderField("Допускаются только латинские буквы, пробел и дефис");
    }


    //    Должно отображаться красное предупреждение, если имя держателя карты содержит цифры
    @Test
    @DisplayName("A red warning should be displayed if the cardholder's name contains numbers")
    void shouldShowMsgWithNumbersInCardHolderName() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithParametrizedCardHolderName("Ivan0v Ivan");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCardHolderField("Допускаются только латинские буквы, пробел и дефис");
    }

    //    Должно отображаться красное предупреждение, если имя держателя карты содержит специальные символы
    @Test
    @DisplayName("A red warning should be displayed if the cardholder's name contains special characters")
    void shouldShowMsgWithSpecCharsCardHolderName() {
        var creditPage = page.creditPage();
        var cardInfo = DataGenerator.dataWithParametrizedCardHolderName("Iv@nov Ivan");
        creditPage.insertValidCreditCardDataForBank(cardInfo);
        creditPage.checkWarningUnderCardHolderField("Допускаются только латинские буквы, пробел и дефис");
    }
}
