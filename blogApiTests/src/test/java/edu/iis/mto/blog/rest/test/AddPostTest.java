package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AddPostTest extends FunctionalTests {
    private static final String USER_API = "/blog/user/";
    private static final String POST_METHOD = "/post";

    private static final Long CONFIRMED_USER_ID = 1L;
    private static final Long NOT_CONFIRMED_USER_ID = 2L;

    @Test
    public void confirmedUserShouldBeAbleToCreatePost() {
        JSONObject jsonObject = new JSONObject().put("entry", "Java tutorial");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post(USER_API + CONFIRMED_USER_ID + POST_METHOD);
    }

    @Test
    void nonConfirmedUserShouldNotBeAbleToCreatePost() {
        JSONObject jsonObject = new JSONObject().put("entry", "Cpp tutorial");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(USER_API + NOT_CONFIRMED_USER_ID + POST_METHOD);
    }
}
