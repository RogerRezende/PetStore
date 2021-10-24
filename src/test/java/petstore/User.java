package petstore;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

public class User {
    String uri = "https://petstore.swagger.io/v2/user";

    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test(priority = 1)
    public void incluirUser() throws IOException {
        String jsonBody = lerJson("data/user1.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .post(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("type", is("unknown"))
                .body("code", is(200))
        ;
    }

    @Test(priority = 2)
    public void login() {
        given()
                .contentType("application/json")
                .log().all()
                .pathParam("username", "batman")
                .pathParam("password", "Batman123")
        .when()
                .get(uri + "/login?" + "username={username}" + "&" + "password={password}")
        .then()
                .log().all()
                .statusCode(200)
                .body("type", is("unknown"))
                .body("code", is(200))
                .body("message", containsString("logged in user session"))
        ;
    }
}
