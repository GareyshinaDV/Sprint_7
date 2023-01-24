import data.Login;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {

    private String login;
    private String password;
    private String firstName;

    private final static String COURIER_ENDPOINT = "/api/v1/courier/";

    public CourierApi(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public CourierApi(String password, String firstName) {
        this.password = password;
        this.firstName = firstName;
    }

    public CourierApi(String login) {
        this.login = login;
    }

    public CourierApi() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public Response creationCourier(CourierApi courier){
        Response response = given()
                .header("Content-type", "application/json")
//                .and()
                .body(courier)
                .when()
                .post(COURIER_ENDPOINT);
        return response;
    }

    public Response authorizationOfCourier(CourierApi courier){
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_ENDPOINT + "login");
        return response;
    }

    public void deletionOfCourier(CourierApi courier){
        Login login = courier.authorizationOfCourier(courier).as(Login.class);
        given()
                .delete(COURIER_ENDPOINT+login.getId());
    }


}
