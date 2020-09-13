package basqar.HomeWorks;

import basqar.HomeWorks.POJO._03_GradeLevels_POJO;
import basqar.HomeWorks.POJO._04_Country_POJO;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class _04_BasqarRestAssured {

    private Cookies cookies;
    private String randomName;
    private String randomShortName;
    private String randomCode;
    private String id;

    _04_Country_POJO country_pojo = new _04_Country_POJO();

    @BeforeClass
    public void init() {
        baseURI = "https://test.basqar.techno.study";

        String country = "Country";
        randomName = RandomStringUtils.randomAlphabetic(6) + country;
        randomShortName = randomName.substring(0, 2).toUpperCase() + country.substring(0, 1);
        randomCode = RandomStringUtils.randomNumeric(3);


        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "daulet2030@gmail.com");
        credentials.put("password", "TechnoStudy123@");

        cookies = given()
                .body(credentials)
                .contentType(ContentType.JSON)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().response().getDetailedCookies();
    }

    @Test
    public void createCountry() {

        country_pojo.setName(randomName);
        country_pojo.setShortName(randomShortName);
        country_pojo.setCode(randomCode);

        id = given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country_pojo)
                .when()
                .post("/school-service/api/countries")
                .then()
                .statusCode(201)
                .log().body()
                .extract().path("id")
        ;
    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative() {

        country_pojo.setName(randomName);
        country_pojo.setShortName(randomShortName);
        country_pojo.setCode(randomCode);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country_pojo)
                .when()
                .post("/school-service/api/countries")
                .then()
                .statusCode(400)
                .log().body()
                .body("message", equalTo("The Country with Name \"" + randomName + "\" already exists."))
        ;
    }

    @Test(dependsOnMethods = "createCountry")
    public void updateCountry() {

        country_pojo.setId(id);
        country_pojo.setName(country_pojo.getName() + "-->" + country_pojo.getCode());
        country_pojo.setShortName(country_pojo.getShortName() + "-->" + country_pojo.getCode());

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country_pojo)
                .when()
                .put("/school-service/api/countries")
                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry() {

        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/countries/" + id)
                .then()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative() {

        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/countries/" + id)
                .then()
                .statusCode(404)
                .log().body()
                .body("message", equalTo("Country not found"))
        ;
    }
}
