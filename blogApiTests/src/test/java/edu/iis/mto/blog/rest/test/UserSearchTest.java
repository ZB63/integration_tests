package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class UserSearchTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";
    private static final String FIND_METHOD = "/find";

    @Test
    void fromTwoUsersWithSameNameOnlyNotRemovedShouldBeReturned() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .formParam("searchString", "thomas@domain")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(USER_API + FIND_METHOD)
                .then()
                .assertThat()
                .body("size()", is(1));
    }

    @Test
    void shouldFindManyUsersExceptRemovedOnes() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .formParam("searchString", ".com")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(USER_API + FIND_METHOD)
                .then()
                .assertThat()
                .body("size()", is(3));
    }

    @Test
    void shouldReturnZeroUsers() {
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .formParam("searchString", "removed@wp.pl")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(USER_API + FIND_METHOD)
                .then()
                .assertThat()
                .body("size()", is(0));
    }
}
