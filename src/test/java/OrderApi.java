import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {

    public Response getListOfOrders(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/v1/orders");
        return response;
    }

    public Response creationOrders(String order){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(order)
                        .post("/api/v1/orders");
        return response;
    }
}
