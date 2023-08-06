package ru.netology.test;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.page.StartPage;

import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;

public class PaymentTest {
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

    //    карта должна быть одобрена
    @Test
    @DisplayName("The payment card must be approved")
    void shouldSuccessTransactionWithPaymentCard() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithApprovedCard();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMsgFromBank();
    }

    //    карта должна быть отклонена
    @Test
    @DisplayName("The payment card must be declined")
    void shouldThePaymentCardMustBeDeclined() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithDeclineCard();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkErrorMsgDeclineFromBank();
    }

    //    Платежная карта должна быть одобрена в течение одного месяца
    @Test
    @DisplayName("The payment card must be approved with one month")
    void shouldSuccessThePaymentCardMustBeApprovedWithOneMonth() {
        var paymentPage = page.paymentPage();
        var validYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 1;
        var cardInfo = DataGenerator.dataWithApprovedCardAndParametrizedMonthAndYear
                ("5", String.valueOf(validYear));
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMsgFromBank();
    }


    //    карта с минимальной датой (текущий месяц) должна быть одобрена
    @Test
    @DisplayName("A payment card with a minimum date (current month) must be approved")
    void shouldSuccessTransactionWithMinAllowedDate() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithApprovedCardAndParametrizedMonthAndYear
                (DataGenerator.getCurrentMonth(), DataGenerator.getCurrentYear());
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkApprovedMsgFromBank();
    }

    //    Должна быть отклонена платежная карта со случайным номером
    @Test
    @DisplayName("Should reject a payment card with a random number")
    void shouldDeclineWithRandomPaymentCard() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithRandomCardNumber();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkErrorMsgDeclineFromBank();
    }

    //    Просроченная карта
    @Test
    @DisplayName("A red warning should be displayed with an expired card")
    void ShouldShowAnExpiredCardMessage() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithAnExpiredCardForOneMonth();
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    //    пустое поле номера карты
    @Test
    @DisplayName("A red warning should be displayed with an empty card number field")
    void shouldShowMsgWithEmptyCardNumberField() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithApprovedCard();
        cardInfo.setNumber("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardNumberField("Поле обязательно для заполнения");
    }

    //    пустое поле месяца
    @Test
    @DisplayName("A red warning about an empty month field should be displayed")
    void ShouldShowAMessageAboutAnEmptyMonthField() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithApprovedCard();
        cardInfo.setMonth("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderMonthField("Поле обязательно для заполнения");
    }

    //    пустое поле года
    @Test
    @DisplayName("A red warning about an empty year field should be displayed")
    void shouldShowAMessageAboutAnEmptyYearField() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithApprovedCard();
        cardInfo.setYear("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderYearField("Поле обязательно для заполнения");
    }

    //    пустое поле имени
    @Test
    @DisplayName("Should display a red warning about an empty name field")
    void shouldDisplayAMessageAboutAnEmptyNameField() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithApprovedCard();
        cardInfo.setHolder("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardHolderField("Поле обязательно для заполнения");
    }

    //    пустое поле cvc
    @Test
    @DisplayName("Should show red warning about empty cvc field")
    void ShouldShowRedWarningAboutEmptyCvcField() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithApprovedCard();
        cardInfo.setCvc("");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCvcField("Поле обязательно для заполнения");
    }

    //    все поля пустые
    @Test
    @DisplayName("Red warning must be displayed empty fields for all")
    void shouldShowRedWarningMustBeDisplayedEmptyFieldsForAll() {
        var paymentPage = page.paymentPage();
        paymentPage.clickContinueButton();
        paymentPage.checkWarningUnderCardNumberField("Поле обязательно для заполнения");
        paymentPage.checkWarningUnderMonthField("Поле обязательно для заполнения");
        paymentPage.checkWarningUnderYearField("Поле обязательно для заполнения");
        paymentPage.checkWarningUnderCardHolderField("Поле обязательно для заполнения");
        paymentPage.checkWarningUnderCvcField("Поле обязательно для заполнения");
    }


    //    13 месяц
    @Test
    @DisplayName("Should display a red warning about invalid data in the month field")
    void shouldDisplayARedWarningAboutInvalidDataInTheMonthField13() {
        var paymentPage = page.paymentPage();
        var currentYear = DataGenerator.getCurrentYear();
        var cardInfo = DataGenerator.dataWithApprovedCardAndParametrizedMonthAndYear("13",
                currentYear);
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    //    00 месяц
    @Test
    @DisplayName("Should display a red warning about invalid data in the month field")
    void shouldDisplayARedWarningAboutInvalidDataInTheMonthField00() {
        var paymentPage = page.paymentPage();
        var validYear = Integer.parseInt(DataGenerator.getCurrentYear()) + 1;
        var cardInfo = DataGenerator.dataWithApprovedCardAndParametrizedMonthAndYear
                ("00", String.valueOf(validYear));
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    //    имя на кириллице
    @Test
    @DisplayName("A red warning about the name written in Cyrillic should appear")
    void shouldShowARedWarningAboutTheNameWrittenInCyrillicShouldAppear() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithParametrizedCardHolderName("Иванов Иван");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardHolderField("Допускаются только латинские буквы, пробел и дефис");
    }

    @Test
    @DisplayName("A red warning should be displayed if the cardholder's name contains numbers")
    void aMessageShouldBeDisplayedAboutThePresenceOfInvalidValues() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithParametrizedCardHolderName("Ivan0v Ivan");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardHolderField("Допускаются только латинские буквы, пробел и дефис");
    }

    @Test
    @DisplayName("A red warning should be displayed if the cardholder's name contains special characters")
    void shouldShowMsgWithSpecCharsCardHolderName() {
        var paymentPage = page.paymentPage();
        var cardInfo = DataGenerator.dataWithParametrizedCardHolderName("Iv@nov Ivan");
        paymentPage.insertValidPaymentCardDataForBank(cardInfo);
        paymentPage.checkWarningUnderCardHolderField("Допускаются только латинские буквы, пробел и дефис");
    }
}