import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RepRestServices {
    @BeforeEach
        public void setup(){
            RestAssured.baseURI="https://reqres.in";
            RestAssured.basePath="/api";
            RestAssured.filters(new RequestLoggingFilter(),new ResponseLoggingFilter());
            RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        }

        @Test
    public void LoginTest(){
        given()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .post("login")
                .then().statusCode(HttpStatus.SC_OK).body("token",notNullValue());
        }

    @Test
    public void GetListUser(){
        when()
                .get("https://reqres.in/api/unknown")
                .then()
                .body("page",equalTo(1));
    }
    @Test
    public void deleteUser(){
        when().delete("https://reqres.in/api/users/2");
    }

    @Test
    public void PutUpdateUnknown(){
        given()
                .log().all()
                .body("{\n" +
                        "    \"name\": \"Daniela Guzman\",\n" +
                        "    \"job\": \"QA Automation student \"\n" +
                        "}")
                .put("https://reqres.in/api/users/7");
    }
    @Test
    public void patch(){
        String nameUpdate=given()
                .when()
                .body("{\n" +
                        "    \"name\": \"Dani\",\n" +
                        "    \"job\": \"student\"\n" +
                        "}")
                .patch("user/2")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().get("name");

        assertThat(nameUpdate,equalTo("Dani"));


    }
}
