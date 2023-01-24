import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

public class BaseTest {
    @Before
    public void setUp() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru";
    }

    @After
    public void deleteCourierAfterTest() {
        CourierApi courier = new CourierApi("agatha", "123456", "Агата");
        courier.deletionOfCourier(courier);
    }
}
