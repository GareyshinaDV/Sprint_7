import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.*;

public class ListOfOrdersReturnTest extends BaseTest {

    @Test
    @DisplayName("Проверка возврата списка заказов")
    @Description("Тест проверяет, что возвращается список заказов в ответе на запрос списка заказов")
    public void checkReturnsListOfOrderTest(){
        OrderApi listOfOrders = new OrderApi();
        Response response = listOfOrders.getListOfOrders();
        response.then().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(SC_OK);
    }
}
