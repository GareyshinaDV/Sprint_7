import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class OrderCreationTest extends BaseTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String color;
    private final String comment;


    public OrderCreationTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate,  String color, String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.color = color;
        this.comment = comment;
    }

    // Тестовые данные
    @Parameterized.Parameters
    public static Object[][] getDataOrder() {
        return new Object[][]{
                {"Дарья", "Гарейшина", "Москва, улица Бибиревская, дом 3", "Бибирево", "+79778015997", 1, "03.12.2022", "[\"black\"]", "Если будет, то с розовыми цветами"},
                {"Игнат", "Свердлов", "Краснобогатырская, дом 2, строение 3", "Преображенская площадь", "89296400988", 2, "10.09.2023", "[\"grey\"]", " "},
                {"Агата", "Даеровна", "улица Эльфийская", "Зябликово", "89778015997", 3, "10.02.2022", "[\"black\",\"grey\"]", "Как же хочется в добрую и светлую сказку"},
                {"Савелий", "Крамаров", "Зеленоград, корп 923", "Комсомольская", "89296400988", 4, "02.05.2023", "[]", "скоро лето, скоро май, скоро будет тепло"},

        };
    }

    @Test
    @DisplayName("Проверка создания заказа с разными параметрами цветов")
    @Description("Тест проверяет, что можно создать заказ, указав один цвет, указав оба цвета и не указывая цвет")
    public void checkOrderCreationTest() {
        String order = "{" +
                "\"firstName\":\"" + firstName + "\"," +
                "\"lastName\":\"" + lastName + "\"," +
                "\"address\":\"" + address + "\"," +
                "\"metroStation\":\"" + metroStation + "\"," +
                "\"phone\":\"" + phone + "\"," +
                "\"rentTime\":" + rentTime + "," +
                "\"deliveryDate\":\"" + deliveryDate + "\"," +
                "\"comment\":\"" + comment + "\"," +
                "\"color\":" + color +
                "}";
        OrderApi newOrder = new OrderApi();
        Response response = newOrder.creationOrders(order);
        response
                .then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(SC_CREATED);
    }
}