package datadriven;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

public class UserDD {
    String uri = "https://petstore.swagger.io/v2/user";
    Data data;

    @DataProvider
    public Iterator<Object[]> provider() throws IOException {
        List<Object[]> testCases = new ArrayList<>();
        String[] testCase;
        String linha;
        BufferedReader bufferedReader = new BufferedReader(new FileReader("data/users.csv"));

        while((linha = bufferedReader.readLine()) != null) {
            testCase = linha.split(",");
            testCases.add(testCase);
        }

        return testCases.iterator();
    }

    @BeforeMethod
    public void setup() {
        data = new Data();
    }

    @Test(priority = 1, dataProvider = "provider")
    public void incluirUser(
            String id,
            String username,
            String firstName,
            String lastName,
            String email,
            String password,
            String phone,
            String userStatus
    ) throws IOException {
        String jsonBody = new JSONObject()
                .put("id", id)
                .put("username", username)
                .put("firstName", firstName)
                .put("lastName", lastName)
                .put("email", email)
                .put("password", password)
                .put("phone", phone)
                .put("userStatus", userStatus)
                .toString();

        String userId =
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
                        .extract()
                        .path("message")
                ;

        System.out.println("O userId e " + userId);
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

    @Test(priority = 3)
    public void consultarUser() {
        String username = "batman";

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(uri + "/" + username)
        .then()
                .log().all()
                .statusCode(200)
                .body("username", is("batman"))
                .body("email", is("bruce.wayne@batman.com"))
        ;
    }

    @Test(priority = 4)
    public void alterarUser() throws IOException {
        String jsonBody = data.lerJson("data/user2.json");
        String username = "batman";

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri + "/" + username)
        .then()
                .log().all()
                .statusCode(200)
                .body("type", is("unknown"))
                .body("code", is(200))
        ;
    }

    @Test(priority = 5)
    public void excluirUser() {
        String username = "batman";

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .delete(uri + "/" + username)
        .then()
                .log().all()
                .statusCode(200)
                .body("type", is("unknown"))
                .body("code", is(200))
                .body("message", is(username))
        ;
    }
}
