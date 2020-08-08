package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

public class FindPostTest extends FunctionalTests {

    private static final String USER_API = "/blog/user/";
    private static final String POST_METHOD = "/post";
    private static final String LIKE_METHOD = "/like/";

    private static final Long POST_ID = 1L;
    private static final Long LIKE_USER_ID = 3L;
    private static final Long REMOVED_USER_ID = 4L;

    @Test
    void cannotSearchForRemovedUserPosts() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .get(USER_API + REMOVED_USER_ID + POST_METHOD);
    }

    @Test
    void canSearchForNotRemovedUserPosts() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(USER_API + LIKE_USER_ID + POST_METHOD);
    }

    @Test
    void shouldReturnOneLike() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(USER_API + LIKE_USER_ID + LIKE_METHOD + POST_ID);

        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("likesCount", hasItems(1))
                .when()
                .get(USER_API + POST_ID + POST_METHOD);
    }
}
