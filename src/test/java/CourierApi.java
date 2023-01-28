import data.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi extends Courier {

    private static String COURIER_ENDPOINT = "/api/v1/courier/";

    public CourierApi(String login, String password, String firstName) {
        super(login, password, firstName);
    }

    public CourierApi(String password, String firstName) {
        super(password, firstName);
    }

    public CourierApi(String login) {
        super(login);
    }


    @Step("Создание учетной записи курьера")
    public Response creationCourier(CourierApi courier){
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_ENDPOINT);
        return response;
    }

    @Step("Авторизация курьера")
    public Response authorizationOfCourier(CourierApi courier){
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_ENDPOINT + "login");
        return response;
    }
    @Step("Удаление учетной записи курьера")
    public static void deletionOfCourier(int courierId){
        given().log().all()
                .delete(COURIER_ENDPOINT+courierId);
    }

}
