package basqar.HomeWorks;

import basqar.HomeWorks.POJO._02_Location_POJO;
import basqar.HomeWorks.POJO.School;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class _02_BasqarRestAssured {

    private Cookies cookies;
    private String randomName;
    private String id;

    @BeforeClass
    public void init() {
        baseURI = "https://test.basqar.techno.study";

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
    public void createLocation() {

        School school = new School();
        _02_Location_POJO location_pojo = new _02_Location_POJO();

        location_pojo.setActive(true);
        location_pojo.setName(randomDataGenerator("name"));
        location_pojo.setShortName(randomDataGenerator("shortName"));
        location_pojo.setCapacity(randomDataGenerator("capacity"));
        location_pojo.setType("CLASS");
        school.setId();
        location_pojo.setSchool(school);

        id = given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(location_pojo)
                .when()
                .post("/school-service/api/location")
                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")
        ;

    }

    @Test(dependsOnMethods = "createLocation" )
    public void deleteLocation(){

        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/location/" + id)
                .then()
                .statusCode(200)
        ;
    }

    public String randomDataGenerator(String data) {

        switch (data) {
            case "name":
                randomName = RandomStringUtils.randomAlphabetic(8);
                data = randomName;
                break;
            case "shortName":
                data = randomName.substring(0, 3).toUpperCase();
                break;
            case "capacity":
                data = String.valueOf((new Random().nextInt(25) + 5));
                break;
        }

        return data;
    }
}
