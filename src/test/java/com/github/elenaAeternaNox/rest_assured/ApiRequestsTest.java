package com.github.elenaAeternaNox.rest_assured;

import io.restassured.RestAssured;
import org.assertj.core.error.ShouldBeToday;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ApiRequestsTest {

    private String body;

    @BeforeAll
    static void prepare() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void registerSuccessful() {
        body = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        given()
                .contentType(JSON)
                .body(body)
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"), "id", is(4));
    }

    @Test
    void registerUnsuccessful() {
        body = "{ \"email\": \"sydney@fife\" }";

        given()
                .contentType(JSON)
                .body(body)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void createUser() {
        body = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        Instant input = LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toInstant();
        Instant instant = Instant.parse(input.toString());
        System.out.println ( "instant: " + instant );

        given()
                .contentType(JSON)
                .body(body)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"), "job", is("leader"), "id", notNullValue());
    }

    @Test
    void singleUserNotFound() {
        given()
                .when()
                .get("/api/users/23")
                .then()
                .statusCode(404)
                .body(is("{}"));
    }

    @Test
    void singleResource() {
        given()
                .when()
                .get("/api/unknown/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2), "data.name", is("fuchsia rose"), "data.year", is(2001),
                        "data.color", is("#C74375"), "data.pantone_value", is("17-2031"),
                        "support.url", is("https://reqres.in/#support-heading"),
                        "support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }
}
