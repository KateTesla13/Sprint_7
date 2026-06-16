package ru.yandex.praktikum.order;

import org.junit.Test;
import ru.yandex.praktikum.BaseTest;
import ru.yandex.praktikum.clients.OrderClient;
import ru.yandex.praktikum.models.Order;
import java.util.List;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest extends BaseTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    public void createOrderWithoutColor() {
        Order order = new Order(
                "Naruto", "Uzumaki", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", null
        );

        orderClient.create(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Test
    public void createOrderWithBlackColor() {
        Order order = new Order(
                "Hinata", "Hyuga", "Konoha, 1455 apt.", 4, "+7 912 555 15 12", 5, "2020-06-06", "Naruto, stop it!", List.of("BLACK")
        );

        orderClient.create(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Test
    public void createOrderWithGreyColor() {
        Order order = new Order(
                "Sakura", "Haruno", "Konoha, 115 apt.", 4, "+7 515 666 13 15", 5, "2020-06-06", "Saske, come back to Konoha", List.of("GREY")
        );

        orderClient.create(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Test
    public void createOrderWithBothColors() {
        Order order = new Order(
                "Saske", "Uchiha", "Konoha, 112 apt.", 4, "+7 113 355 35 35", 5, "2020-06-06", "I’m going to restore my clan, and kill a certain someone", List.of("BLACK", "GREY")
        );

        orderClient.create(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}