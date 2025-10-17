package testcases;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.*;
import payloads.Payload;
import pojo.User;
import routes.Routes;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserTests extends BaseClass {
    // 1 Fetch all the users
    @Test
    public void testGetAllUsers(){
        given()
                .when()
                .get(Routes.GET_ALL_USERS)
                .then()
                .statusCode(200)
                .log().body()
                .contentType(ContentType.JSON)
                .body("size()",greaterThan(0));
    }

    // 2 Test to fetch a specific user by ID
    @Test
    public void testGetUserById(){
        int userId=configReader.getIntProperty("userId");
        given()
                .pathParam("id", userId)
                .when()
                .get(Routes.GET_USER_BY_ID)
                .then()
                .log().body()
                .statusCode(200);
    }

    // 3 Test to fetch a limited number of users
    @Test
    public void testGetUsersWithLimit(){
        int limit=configReader.getIntProperty("limit");
        given()
                .pathParam("limit", limit)
                .when()
                .get(Routes.GET_USERS_WITH_LIMIT)
                .then()
                .statusCode(200)
                .log().body()
                .body("size()",equalTo(limit));
    }

    //4 Test to fetch users sorted in descending order
    @Test
    public void testGetUsersSorted() {
        Response response=given()
                .pathParam("order", "desc")
                .when()
                .get(Routes.GET_USERS_SORTED)
                .then()
                .statusCode(200)
                .extract().response();

        List<Integer> userIds=response.jsonPath().getList("id", Integer.class);
        assertThat(isSortedDescending(userIds),is(true));
    }

    //5 Test to fetch users sorted in ascending order
    @Test
    public void testGetUsersSortedAsc() {
        Response response=given()
                .pathParam("order", "asc")
                .when()
                .get(Routes.GET_USERS_SORTED)
                .then()
                .statusCode(200)
                .extract().response();

        List<Integer> userIds=response.jsonPath().getList("id", Integer.class);
        assertThat(isSortedAscending(userIds),is(true));
    }

    //6 Test to create new user
    @Test
    public void testCreateNewUser(){
        User newUser= Payload.userPayload();

        int id=given()
                .contentType(ContentType.JSON)
                .body(newUser)
        .when()
                .post(Routes.CREATE_USER)
        .then()
                .log().body()
                .statusCode(201)
                .body("id",notNullValue())
                .extract().jsonPath().getInt("id"); // Vytiahnem ID z response body
        System.out.println("Generated User ID: "+id);
    }

    //7 Test to update user
    @Test
    public void testUpdateUser(){
        int userId=configReader.getIntProperty("userId");
        User updateUser=Payload.userPayload();

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .body(updateUser)
        .when()
                .put(Routes.UPDATE_USER)
        .then()
                .log().body()
                .statusCode(200)
                .body("username",equalTo(updateUser.getUsername()));
    }

    //8 Test to delete a user
    @Test
    public void testDeleteUser() {
        int userId=configReader.getIntProperty("userId");
        given()
                .pathParam("id",userId)
                .when()
                .delete(Routes.DELETE_USER)
                .then()
                .statusCode(200);
    }
}