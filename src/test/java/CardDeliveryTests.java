import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTests {

    private Faker faker;
    private Faker fakerEng;
    private String firstName;
    private String lastName;
    private String phone;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        faker = new Faker(new Locale("ru"));
        fakerEng = new Faker(new Locale("en"));
        firstName = faker.name().firstName().replace("ё", "е");
        lastName = faker.name().lastName().replace("ё", "е");
        phone = faker.phoneNumber().cellPhone();
}

    @Test
    @DisplayName("Должен успешно отправлять первичную заявку при валидных данных")
    void shouldSubmitFirstRequest() {
        startPage();
        $("[data-test-id=city] input.input__control").setValue(setCity());
        cleanDate();
        $("[data-test-id=date] input.input__control").setValue(setDeliveryDay());
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(withText("Успешно!")).waitUntil(Condition.visible, 5000);
    }

    @Test
    @DisplayName("Должен подтверждать смену даты доставки")
    void shouldConfirmNewDeliveryDate() {
        startPage();
        $("[data-test-id=city] input.input__control").setValue(setCity());
        cleanDate();
        $("[data-test-id=date] input.input__control").setValue(setDeliveryDay());
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(withText("Успешно!")).waitUntil(Condition.visible, 5000);
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input.input__control").setValue(setNewDeliveryDay());
        $(".button").click();
        $(withText("Перепланировать")).click();
        $(withText("Успешно!")).waitUntil(Condition.visible, 5000);
    }

    @Test
    @DisplayName("Не должен подтверждать заказ при вводе невалидного города")
    void shouldNotSubmitWithInvalidCity() {
        startPage();
        $("[data-test-id=city] input.input__control").setValue(setInvalidCity());
        cleanDate();
        $("[data-test-id=date] input.input__control").setValue(setDeliveryDay());
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен подтверждать заказ при вводе невалидной даты")
    void shouldNotSubmitWithInvalidDate() {
        startPage();
        $("[data-test-id=city] input.input__control").setValue(setCity());
        cleanDate();
        $("[data-test-id=date] input.input__control").setValue(setInvalidDate());
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен подтверждать заказ при вводе невалидного имени")
    void shouldNotSubmitWithInvalidName() {
        startPage();
        firstName = fakerEng.name().firstName();
        lastName = fakerEng.name().lastName();
        $("[data-test-id=city] input.input__control").setValue(setCity());
        cleanDate();
        $("[data-test-id=date] input.input__control").setValue(setDeliveryDay());
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен подтверждать заказ при вводе невалидного номера телефона") //заведомо провалится, т.к.есть баг
    void shouldNotSubmitWithInvalidPhone() {
        startPage();
        phone = "99911122";
        $("[data-test-id=city] input.input__control").setValue(setCity());
        cleanDate();
        $("[data-test-id=date] input.input__control").setValue(setDeliveryDay());
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен отправлять заявку без указания города")
    void shouldNotSubmitWithEmptyCity() {
        startPage();
        cleanDate();
        $("[data-test-id=date] input.input__control").setValue(setDeliveryDay());
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен отправлять заявку без указания даты")
    void shouldNotSubmitWithEmptyDate() {
        startPage();
        $("[data-test-id=city] input.input__control").setValue(setCity());
        cleanDate();
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен отправлять заявку без указания имени")
    void shouldNotSubmitWithEmptyName() {
        startPage();
        $("[data-test-id=city] input.input__control").setValue(setCity());
        cleanDate();
        $("[data-test-id=date] input.input__control").setValue(setDeliveryDay());
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен подтверждать заказ без номера телефона")
    void shouldNotSubmitWithoutPhone() {
        startPage();
        $("[data-test-id=city] input.input__control").setValue(setCity());
        cleanDate();
        $("[data-test-id=date] input.input__control").setValue(setDeliveryDay());
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $$("span").find(Condition.cssClass("input_invalid")).shouldHave(Condition.visible);
    }

    @Test
    @DisplayName("Не должен отправлять доставку, если не отмечен чекбокс")
    void shouldNotSubmitWithEmptyCheckBox() {
        startPage();
        $("[data-test-id=city] input.input__control").setValue(setCity());
        cleanDate();
        $("[data-test-id=date] input.input__control").setValue(setDeliveryDay());
        $("[data-test-id=name] input.input__control").setValue(lastName + " " + firstName);
        $("[data-test-id=phone] input.input__control").setValue(phone);
        $(".button").click();
        $("form").$("label").shouldHave(Condition.cssClass("input_invalid"));
    }

    public String setCity() {
        String [] cityList = {"Москва", "Майкоп", "Саранск", "Казань", "Барнаул", "Владивосток", "Калининград", "Иваново", "Новосибирск", "Псков", "Анадырь", "Ульяновск", "Тюмень", "Ростов-на-Дону"};
        int cityNumber = (int) (Math.random() * cityList.length);
        String city = cityList[cityNumber];
        return city;
    }

    public String setInvalidCity() {
        String [] invalidCityList = {"Люберцы", "Малаховка", "Жуковский", "Дзержинский", "NewYork", "5к5534", "№2"};
        int invalidCityNumber = (int) (Math.random()*invalidCityList.length);
        String invalidCity = invalidCityList[invalidCityNumber];
        return invalidCity;
    }

    public String setDeliveryDay() {
        LocalDate today = LocalDate.now();
        LocalDate todayPlusThreeDays = today.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deliveryDay = todayPlusThreeDays.format(formatter);
        return deliveryDay;
    }

    public String setInvalidDate() {
        LocalDate today = LocalDate.now();
        LocalDate todayPlusThreeDays = today.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String invalidDate = today.format(formatter);
        return invalidDate;
    }

    public String setNewDeliveryDay() {
        LocalDate today = LocalDate.now();
        LocalDate todayPlusThreeDays = today.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String newDeliveryDay = todayPlusThreeDays.plusDays(5).format(formatter);
        return newDeliveryDay;
    }

    public void cleanDate() {
        $("[data-test-id=date] input.input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
    }

    public void startPage() {
        open("http://localhost:9999/");
    }



}
