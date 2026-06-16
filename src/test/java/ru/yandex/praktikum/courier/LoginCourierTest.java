package ru.yandex.praktikum.courier;

import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.models.Courier;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest extends BaseTest {

    @Test
    public void loginCourierSuccess() {

        Courier courier = createCourier();

        // логинимся
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(new Courier(courier.getLogin(), courier.getPassword()))
                .post("/api/v1/courier/login");

        // проверяем успешный логин
        loginResponse.then()
                .statusCode(200)
                .body("id", notNullValue());

        // сохраняем id для удаления
        courierId = loginResponse.then().extract().path("id");
    }

    @Test
    public void loginCourierWrongPassword() {
        Courier courier = createCourier();
// получаем id для удаления через правильный логин
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(new Courier(courier.getLogin(), courier.getPassword()))
                .post("/api/v1/courier/login");

        courierId = loginResponse.then().extract().path("id");

        // логинимся с неверным паролем
        given()
                .header("Content-type", "application/json")
                .body(new Courier(courier.getLogin(), "wrongPassword"))
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginCourierWrongLogin() {
        Courier courier = createCourier();

        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(new Courier(courier.getLogin(), courier.getPassword()))
                .post("/api/v1/courier/login");

        courierId = loginResponse.then().extract().path("id");

        // логинимся с неверным логином
        given()
                .header("Content-type", "application/json")
                .body(new Courier("wrongLogin", courier.getPassword()))
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginCourierNoPassword() {
        Courier courier = createCourier();

        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(new Courier(courier.getLogin(), courier.getPassword()))
                .post("/api/v1/courier/login");

        courierId = loginResponse.then().extract().path("id");

        // логинимся без пароля
        given()
                .header("Content-type", "application/json")
                .body(new Courier(courier.getLogin(), ""))
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}