package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DbUtils;
import ru.netology.page.CreditPage;
import ru.netology.page.StartPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {
    StartPage startPage = open("http://localhost:8080/", StartPage.class);

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUP() {
        Configuration.holdBrowserOpen = true;
    }

    @BeforeEach
    public void openPage() throws SQLException {
        DbUtils.clearTables();
        String url = System.getProperty("sut.url");
        open(url);
    }

    @Test
    void shouldMakeSuccessTransactionByApprovedCard() throws SQLException {
        startPage.creditPage();
        var cardInfo = DataHelper.generatedDataIfApprovedCard();
        var creditPage = new CreditPage();
        creditPage.insertCardData(cardInfo);
        creditPage.confirmationOfBank();
        assertEquals("APPROVED", DbUtils.findCreditStatus());
    }

    @Test
    void shouldDeclineIfRandomNumberCard() throws SQLException {
        startPage.creditPage();
        var cardInfo = DataHelper.generatedDataIfRandomCard();
        var creditPage = new CreditPage();
        creditPage.insertCardData(cardInfo);
        creditPage.errorRestricted();
        assertEquals("0", DbUtils.countRecords());
    }
}
