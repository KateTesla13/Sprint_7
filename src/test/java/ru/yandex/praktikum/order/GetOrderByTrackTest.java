package ru.yandex.praktikum.order;

import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.models.Order;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderByTrackTest extends BaseTest {

    @Test
    public void getOrderByTrackSuccess() { //успешное получение заказа по его номеру
        // Создаём заказ
        Order order = new Order(
                "Naruto", "Uzumaki", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", null
        );

        Response orderResponse = given()
                .header("Content-type", "application/json")
                .body(order)
                .post("/api/v1/orders");

        orderResponse.then().statusCode(201);
        int track = orderResponse.then().extract().path("track");

        // Получаем заказ по track
        given()
                .queryParam("t", track)
                .get("/api/v1/orders/track")
                .then()
                .statusCode(200)
                .body("order", notNullValue());
    }

    @Test
    public void getOrderByTrackWithoutTrack() { //получение заказа без указания track
        given()
                .get("/api/v1/orders/track")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    public void getOrderByTrackWithWrongTrack() { //получение заказа с неправильным track
        given()
                .queryParam("t", 999999)
                .get("/api/v1/orders/track")
                .then()
                .statusCode(404)
                .body("message", equalTo("Заказ не найден"));
    }
}