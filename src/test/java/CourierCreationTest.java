import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import data.Courier;
import data.Login;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка создания нового курьера")
    @Description("Тест проверяет корректность создания нового курьера с валидными данными")
    public void creationNewCourierTest() {

        Courier courier = new Courier("agatha", "123456", "Агата");
        Response response =
                given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("api/v1/courier");
                 response.then().assertThat().body("ok", equalTo(true))
                         .and()
                         .statusCode(201);
    }

    @Test
    @DisplayName("Проверка невозможности создания двух одинаковых курьеров")
    @Description("Тест проверяет, что невозможно создать двух одинаковых курьеров")
    public void checkImpossibilityTwoEqualsCourierCreationsTest(){
        Courier courier = new Courier("agatha", "123456", "Агата");
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("api/v1/courier");

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("api/v1/courier");
                        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                                .and()
                                .statusCode(409);
    }

    @Test
    @DisplayName("Проверка обязательности заполнения поля логин")
    @Description("Тест проверяет, что невозможно курьера без логина")
    public void checkLoginRequiredTest(){
        Courier courier = new Courier(null, "123456", "Агата");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Проверка обязательности заполнения поля пароль")
    @Description("Тест проверяет, что невозможно курьера без пароля")
    public void checkPasswordRequiredTest(){
        Courier courier = new Courier("agatha", null, "Агата");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Проверка обязательности наличия поля логин")
    @Description("Тест проверяет, что поле логин обязательно")
    public void checkCreationCourierWithoutFieldLoginImpossibilityTest(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{ \"password\": \"123456\"}")
                        .when()
                        .post("api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Проверка обязательности наличия поля пароль")
    @Description("Тест проверяет, что поле пароль обязательно")
    public void checkCreationCourierWithoutFieldPasswordImpossibilityTest(){
        Courier courier = new Courier("agatha", null, "Агата");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{ \"login\": \"agatha\"}")
                        .when()
                        .post("api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Проверка невозможности создания двух курьеров с одинаковым логином")
    @Description("Тест проверяет, что логин уникален")
    public void checkLoginUniquenessTest(){
        Courier courier = new Courier("agatha", "123456", "Агата");
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("api/v1/courier");

        Courier courierWithTheSameLogin = new Courier("agatha", "85900", null);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierWithTheSameLogin)
                        .when()
                        .post("api/v1/courier");
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @After
    public void deleteCourierAfterTest() {
        Courier courier = new Courier("agatha", "123456", "Агата");
        Login login = given()
                .header("Content-type", "application/json")
                .body("{ \"login\": \"" +courier.getLogin()+"\", \"password\": \"" +courier.getPassword()+"\"}")
                .post("/api/v1/courier/login")
                .as(Login.class);

        given()
                .delete("/api/v1/courier/"+login.getId());

    }

}