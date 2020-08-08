package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

public class CreateUserTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/";
    private static final String POST_METHOD = "/post";

    private static final Long CONFIRMED_USER_ID = 1L;
    private static final Long NOT_CONFIRMED_USER_ID = 2L;


    @Test
    public void createUserWithProperDataReturnsCreatedStatus() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy1@domain.com");
        given().accept(ContentType.JSON)
               .header("Content-Type", "application/json;charset=UTF-8")
               .body(jsonObj.toString())
               .expect()
               .log()
               .all()
               .statusCode(HttpStatus.SC_CREATED)
               .when()
               .post(USER_API);
    }

    @Test
    void createUserWithExistingEmailShouldResturnStatusSC_CONFLICT() {
        JSONObject jsonObj1 = new JSONObject().put("email", "tracy2@domain.com");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj1.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post(USER_API);

        JSONObject jsonObj2 = new JSONObject().put("email", "tracy2@domain.com");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj2.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CONFLICT)
                .when()
                .post(USER_API);
    }

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
