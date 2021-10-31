package petstore;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class Pet {
    String uri = "https://petstore.swagger.io/v2/pet";

    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test(priority = 1)
    public void incluirPet() throws IOException {
        String jsonBody = lerJson("data/pet1.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .post(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Julie"))
                .body("category.name", containsString("dog"))
        ;
    }

    @Test(priority = 2)
    public void consultarPet() {
        String petId = "1986012120211031";

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Julie"))
                .body("category.name", containsString("dog"))
        ;
    }

    @Test(priority = 3)
    public void alterarPet() throws IOException {
        String jsonBody = lerJson("data/pet2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Mingau"))
                .body("category.name", containsString("cat"))
        ;
    }

    @Test(priority = 4)
    public void excluirPet() {
        String petId = "1986012120211031";

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .delete(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("type", is("unknown"))
                .body("code", is(200))
                .body("message", is(petId))
        ;
    }

    @Test(priority = 5)
    public void consultarPetPorStatus() {
        String petStatus = "available";

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(uri + "/findByStatus?status=" + petStatus)
        .then()
                .log().all()
                .statusCode(200)
                .body("name[]", everyItem(equalTo("Julie")))
        ;
    }
}
