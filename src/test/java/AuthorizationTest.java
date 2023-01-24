import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.*;

public class AuthorizationTest extends BaseTest{

    @Before
    public void newCourierForAuthorizationClass() {
        CourierApi courier = new CourierApi("agatha", "123456", "Агата");
        courier.creationCourier(courier);
    }

    @Test
    @DisplayName("Проверка возможности авторизации курьера")
    @Description("Тест проверяет что курьер может авторизоваться при передаче обязательных полей и значений логина и пароля")
    public void checkPositiveAuthorizationTest(){
        CourierApi courier = new CourierApi("agatha", "123456", "Агата");
        Response response = courier.authorizationOfCourier(courier);
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Проверка обязательности полей для авторизации")
    @Description("Тест проверяет, что для авторизации необходимо поле логин")
    public void checkFieldLoginIsNeedForAuthorizationTest(){
        CourierApi courier = new CourierApi("123456", "Агата");
        Response response = courier.authorizationOfCourier(courier);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }


    @Test
    @DisplayName("Проверка возврата ошибки в случае ввода несуществующей пары логин-пароль")
    @Description("Тест проверяет, что будет возвращена ошибка при вводе несуществующей пары логин-пароль")
    public void checkIncorrectnessPairLoginPasswordTest(){
        CourierApi courier = new CourierApi("agatha", "1234567", "Агата");
        Response response = courier.authorizationOfCourier(courier);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
    }

}