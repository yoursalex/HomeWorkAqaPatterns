import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTests {
    private String [] cityList = {"Москва", "Майкоп", "Саранск", "Казань", "Барнаул", "Владивосток", "Калининград", "Иваново", "Новосибирск", "Псков", "Анадырь", "Ульяновск", "Тюмень", "Ростов-на-Дону"};
    private String [] invalidCityList = {"Люберцы", "Малаховка", "Жуковский", "Дзержинский", "NewYork", "5к5534", "№2"};
    private String city;
    private String invalidCity;
    private String deliveryDay;
    private String newDeliveryDay;
    private Faker faker;
    private Faker fakerEng;
    private String invalidDate;


    @BeforeEach
    public void setUp() {
        faker = new Faker(new Locale("ru"));
        fakerEng = new Faker(new Locale("en"));

        int cityNumber = (int) (Math.random() * cityList.length);
        int invalidCityNumber = (int) (Math.random()*invalidCityList.length);
        city = cityList[cityNumber];
        invalidCity = invalidCityList[invalidCityNumber];

        LocalDate today = LocalDate.now();
        LocalDate todayPlusThreeDays = today.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        deliveryDay = todayPlusThreeDays.format(formatter);
        invalidDate = today.format(formatter);
        newDeliveryDay = todayPlusThreeDays.plusDays(5).format(formatter);
}

    @Test
    @DisplayName("Должен успешно отправлять первичную заявку при валидных данных")
    void shouldSubmitFirstRequest() {
        open("http://localhost:9999/");
        String firstName = faker.name().firstName().replace("ё", "е");
        String lastName = faker.name().lastName().replace("ё", "е");
        String phone = faker.phoneNumber().cellPhone();

        $("[data-test-id=city] input.input__control").setValue(city);
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input.input__control").setValue(deliveryDay);
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(withText("Успешно!")).waitUntil(Condition.visible, 5000);
    }

    @Test
    @DisplayName("Должен подтверждать смену даты доставки")
    void shouldConfirmNewDeliveryDate() {
        open("http://localhost:9999/");
        String firstName = faker.name().firstName().replace("ё", "е");
        String lastName = faker.name().lastName().replace("ё", "е");
        String phone = faker.phoneNumber().cellPhone();

        $("[data-test-id=city] input.input__control").setValue(city);
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input.input__control").setValue(deliveryDay);
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(withText("Успешно!")).waitUntil(Condition.visible, 5000);
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input.input__control").setValue(newDeliveryDay);
        $(".button").click();
        $(withText("Перепланировать")).click();
        $(withText("Успешно!")).waitUntil(Condition.visible, 5000);
    }

    @Test
    @DisplayName("Не должен подтверждать заказ при вводе невалидного города")
    void shouldNotSubmitWithInvalidCity() {
        open("http://localhost:9999/");
        String firstName = faker.name().firstName().replace("ё", "е");
        String lastName = faker.name().lastName().replace("ё", "е");
        String phone = faker.phoneNumber().cellPhone();

        $("[data-test-id=city] input.input__control").setValue(invalidCity);
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input.input__control").setValue(deliveryDay);
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен подтверждать заказ при вводе невалидной даты")
    void shouldNotSubmitWithInvalidDate() {
        open("http://localhost:9999/");
        String firstName = faker.name().firstName().replace("ё", "е");
        String lastName = faker.name().lastName().replace("ё", "е");
        String phone = faker.phoneNumber().cellPhone();

        $("[data-test-id=city] input.input__control").setValue(city);
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input.input__control").setValue(invalidDate);
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен подтверждать заказ при вводе невалидного имени")
    void shouldNotSubmitWithInvalidName() {
        open("http://localhost:9999/");
        String firstName = fakerEng.name().firstName();
        String lastName = fakerEng.name().lastName();
        String phone = faker.phoneNumber().cellPhone();

        $("[data-test-id=city] input.input__control").setValue(city);
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input.input__control").setValue(deliveryDay);
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен подтверждать заказ при вводе невалидного номера телефона") //заведомо провалится, т.к.есть баг
    void shouldNotSubmitWithInvalidPhone() {
        open("http://localhost:9999/");
        String firstName = faker.name().firstName().replace("ё", "е");
        String lastName = faker.name().lastName().replace("ё", "е");
        String phone = "99911122";

        $("[data-test-id=city] input.input__control").setValue(city);
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input.input__control").setValue(deliveryDay);
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен отправлять заявку без указания города")
    void shouldNotSubmitWithEmptyCity() {
        open("http://localhost:9999/");
        String firstName = faker.name().firstName().replace("ё", "е");
        String lastName = faker.name().lastName().replace("ё", "е");
        String phone = faker.phoneNumber().cellPhone();

        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input.input__control").setValue(deliveryDay);
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен отправлять заявку без указания даты")
    void shouldNotSubmitWithEmptyDate() {
        open("http://localhost:9999/");
        String firstName = faker.name().firstName().replace("ё", "е");
        String lastName = faker.name().lastName().replace("ё", "е");
        String phone = faker.phoneNumber().cellPhone();

        $("[data-test-id=city] input.input__control").setValue(city);
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен отправлять заявку без указания имени")
    void shouldNotSubmitWithEmptyName() {
        open("http://localhost:9999/");
        String phone = faker.phoneNumber().cellPhone();

        $("[data-test-id=city] input.input__control").setValue(city);
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input.input__control").setValue(deliveryDay);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен подтверждать заказ без номера телефона")
    void shouldNotSubmitWithoutPhone() {
        open("http://localhost:9999/");
        String firstName = faker.name().firstName().replace("ё", "е");
        String lastName = faker.name().lastName().replace("ё", "е");

        $("[data-test-id=city] input.input__control").setValue(city);
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input.input__control").setValue(deliveryDay);
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен отправлять доставку, если не отмечен чекбокс")
    void shouldNotSubmitWithEmptyCheckBox() {
        open("http://localhost:9999/");
        String firstName = faker.name().firstName().replace("ё", "е");
        String lastName = faker.name().lastName().replace("ё", "е");
        String phone = faker.phoneNumber().cellPhone();

        $("[data-test-id=city] input.input__control").setValue(city);
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input.input__control").setValue(deliveryDay);
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $(".button").click();
        $("form").$("label").shouldHave(Condition.cssClass("input_invalid"));
    }

}
