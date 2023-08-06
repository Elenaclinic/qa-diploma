package ru.netology.data;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private static Faker faker = new Faker(new Locale("en"));
    private static int validYear = Integer.parseInt(getCurrentYear()) + 1;
    private static String approvedCard = "4444 4444 4444 4441";
    private static String declinedCard = "4444 4444 4444 4442";


    // данные с утвержденной картой
    public static CardInfo dataWithApprovedCard() {
        var randomName = faker.name().fullName();
        var randomCvc = faker.number().digits(3);
        return new CardInfo(approvedCard, getCurrentMonth(), String.valueOf(validYear), randomName, randomCvc);
    }

    // данные с не утвержденной картой
    public static CardInfo dataWithDeclineCard() {
        var randomName = faker.name().fullName();
        var randomCvc = faker.number().digits(3);
        return new CardInfo(declinedCard, getCurrentMonth(), String.valueOf(validYear), randomName, randomCvc);
    }

    // данные со случайным номером карты
    public static CardInfo dataWithRandomCardNumber() {
        var randomName = faker.name().fullName();
        var randomCardNumber = faker.number().digits(16);
        var randomCvc = faker.number().digits(3);
        return new CardInfo(randomCardNumber, getCurrentMonth(), String.valueOf(validYear), randomName, randomCvc);
    }

    // данные с утвержденной картой и параметризованным месяцем и годом
    public static CardInfo dataWithApprovedCardAndParametrizedMonthAndYear(String month, String year) {
        var randomName = faker.name().fullName();
        var randomCvc = faker.number().digits(3);
        return new CardInfo(approvedCard, month, year, randomName, randomCvc);
    }


    // данные с именем держателя карты
    public static CardInfo dataWithParametrizedCardHolderName(String name) {
        var randomCvc = faker.number().digits(3);
        return new CardInfo(approvedCard, getCurrentMonth(), String.valueOf(validYear), name, randomCvc);
    }

    // данные с просроченной картой в течение одного месяца
    public static CardInfo dataWithAnExpiredCardForOneMonth() {
        var randomName = faker.name().fullName();
        var randomCvc = faker.number().digits(3);
        var currentMonth = Integer.parseInt(getCurrentMonth());
        var currentYear = Integer.parseInt(getCurrentYear());
        if (currentMonth == 1) {
            currentMonth = 12;
            currentYear = currentYear - 1;
        } else {
            currentMonth = currentMonth - 1;
        }
        var minusOneFromCurrentMonth = "";
        if (currentMonth < 10) {
            minusOneFromCurrentMonth = "0" + currentMonth;
        }
        return new CardInfo(approvedCard, minusOneFromCurrentMonth,
                String.valueOf(currentYear), randomName, randomCvc);
    }

    public static String getCurrentMonth() {
        var date = LocalDate.now();
        var currentMonth = date.format(DateTimeFormatter.ofPattern("MM"));
        return currentMonth;
    }

    public static String getCurrentYear() {
        var date = LocalDate.now();
        var currentYear = date.format(DateTimeFormatter.ofPattern("yy"));
        return currentYear;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardInfo {
        String number;
        String month;
        String year;
        String holder;
        String cvc;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditCardData {
        private String id;
        private String bank_id;
        private String created;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentCardData {
        private String id;
        private String amount;
        private String created;
        private String status;
        private String transaction_id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TableOrderEntity {
        private String id;
        private String created;
        private String credit_id;
        private String payment_id;
    }
}
