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
import static org.hamcrest.CoreMatchers.notNullValue;

public class AuthorizationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        Courier courier = new Courier("agatha", "123456", "Агата");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post("api/v1/courier");
    }

    @Test
    @DisplayName("Проверка возможности авторизации курьера")
    @Description("Тест проверяет что курьер может авторизоваться при передаче обязательных полей и значений логина и пароля")
    public void checkPositiveAuthorizationTest(){
        Courier courier = new Courier("agatha", "123456", "Агата");
        Response response = given()
                .header("Content-type", "application/json")
                .body("{ \"login\": \"" +courier.getLogin()+"\", \"password\": \"" +courier.getPassword()+"\"}")
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверка обязательности полей для авторизации")
    @Description("Тест проверяет, что для авторизации необходимо поле логин")
    public void checkFieldLoginIsNeedForAuthorizationTest(){
        Courier courier = new Courier("agatha", "123456", "Агата");
        Response response = given()
                .header("Content-type", "application/json")
                .body("{ \"password\": \"" +courier.getPassword()+"\"}")
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }


    @Test
    @DisplayName("Проверка возврата ошибки в случае ввода несуществующей пары логин-пароль")
    @Description("Тест проверяет, что будет возвращена ошибка при вводе несуществующей пары логин-пароль")
    public void checkIncorrectnessPairLoginPasswordTest(){
        Response response = given()
                .header("Content-type", "application/json")
                .body("{ \"login\": \"agatha\", \"password\": \"1234567\"}")
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
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