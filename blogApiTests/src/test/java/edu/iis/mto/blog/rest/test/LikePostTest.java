package edu.iis.mto.blog.rest.test;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class LikePostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/";
    private static final String LIKE_METHOD = "/like/";

    private static final Long CONFIRMED_USER_ID = 1L;
    private static final Long NOT_CONFIRMED_USER_ID = 2L;
    private static final Long FIRST_POST_ID = 1L;
    private static final Long SECOND_POST_ID = 3L;

    @Test
    void confirmedUserCanAddLikePost() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API + CONFIRMED_USER_ID + LIKE_METHOD + SECOND_POST_ID);
    }

    @Test
    void notConfirmedUserCannotAddLikePost() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API + NOT_CONFIRMED_USER_ID + LIKE_METHOD + SECOND_POST_ID);
    }

    @Test
    void userCannotLikeHisOwnPost() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(USER_API + CONFIRMED_USER_ID + LIKE_METHOD + FIRST_POST_ID);
    }

    @Test
    void likeSamePostTwoTimesShouldReturOkStatus() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API + CONFIRMED_USER_ID + LIKE_METHOD + SECOND_POST_ID);

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API + CONFIRMED_USER_ID + LIKE_METHOD + SECOND_POST_ID);
    }
}
