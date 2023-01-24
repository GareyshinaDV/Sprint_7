import data.Login;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreationTest extends BaseTest
{

    @Test
    @DisplayName("Проверка создания нового курьера")
    @Description("Тест проверяет корректность создания нового курьера с валидными данными")
    public void creationNewCourierTest() {

        CourierApi courier = new CourierApi("agatha", "123456", "Агата");
        Response response = courier.creationCourier(courier);
                 response.then().assertThat().body("ok", equalTo(true))
                         .and()
                         .statusCode(SC_CREATED);
    }

    @Test
    @DisplayName("Проверка невозможности создания двух одинаковых курьеров")
    @Description("Тест проверяет, что невозможно создать двух одинаковых курьеров")
    public void checkImpossibilityTwoEqualsCourierCreationsTest(){
        CourierApi courier = new CourierApi("agatha", "123456", "Агата");
        courier.creationCourier(courier);
        Response response = courier.creationCourier(courier);
                        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                                .and()
                                .statusCode(SC_CONFLICT);
    }

    @Test
    @DisplayName("Проверка обязательности заполнения поля логин")
    @Description("Тест проверяет, что невозможно курьера без логина")
    public void checkLoginRequiredTest(){
        CourierApi courier = new CourierApi(null, "123456", "Агата");
        Response response = courier.creationCourier(courier);
            response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
            .and()
             .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Проверка обязательности заполнения поля пароль")
    @Description("Тест проверяет, что невозможно курьера без пароля")
    public void checkPasswordRequiredTest(){
        CourierApi courier = new CourierApi("agatha", null, "Агата");
        Response response = courier.creationCourier(courier);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Проверка обязательности наличия поля логин")
    @Description("Тест проверяет, что поле логин обязательно")
    public void checkCreationCourierWithoutFieldLoginImpossibilityTest(){
            CourierApi courier = new CourierApi("123456", "Агата");
            Response response = courier.creationCourier(courier);
            response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Проверка обязательности наличия поля пароль")
    @Description("Тест проверяет, что поле пароль обязательно")
    public void checkCreationCourierWithoutFieldPasswordImpossibilityTest(){
        CourierApi courier = new CourierApi("agatha");
        Response response = courier.creationCourier(courier);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Проверка невозможности создания двух курьеров с одинаковым логином")
    @Description("Тест проверяет, что логин уникален")
    public void checkLoginUniquenessTest(){
        CourierApi courier = new CourierApi("agatha", "123456", "Агата");
        courier.creationCourier(courier);
        CourierApi courierWithTheSameLogin = new CourierApi("agatha", "85900", null);
        Response response = courier.creationCourier(courierWithTheSameLogin);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);
    }

}