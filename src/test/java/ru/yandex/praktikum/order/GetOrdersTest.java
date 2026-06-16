package ru.yandex.praktikum.order;

import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.models.Courier;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTest extends BaseTest {

    @Test
    public void getOrdersList() { //получение списка заказов
        given()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }

    @Test
    public void getOrdersByCourierId() { //получение списка заказов курьера
        Courier courier = createCourier();

        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(new Courier(courier.getLogin(), courier.getPassword()))
                .post("/api/v1/courier/login");

        int courierId = loginResponse.then().extract().path("id");

        given()
                .queryParam("courierId", courierId)
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", notNullValue());

        this.courierId = courierId;
    }
    @Test
    public void getOrdersByCourierIdWithWrongId() { //получение списка заказов курьера с неверным Id
        int wrongCourierId = 999999;
        given()
                .queryParam("courierId", wrongCourierId)
                .get("/api/v1/orders")
                .then()
                .statusCode(404)
                .body("message", equalTo("Курьер с идентификатором " + wrongCourierId + " не найден"));
    }
}