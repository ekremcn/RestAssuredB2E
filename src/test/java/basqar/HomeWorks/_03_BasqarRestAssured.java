package basqar.HomeWorks;

import basqar.HomeWorks.POJO._03_GradeLevels_POJO;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class _03_BasqarRestAssured {

    private Cookies cookies;
    private String randomName;
    private String id;

    _03_GradeLevels_POJO gradeLevels_pojo = new _03_GradeLevels_POJO();

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
    public void createGradeLevel() {

        gradeLevels_pojo.setName(randomDataGenerator("name"));
        gradeLevels_pojo.setShortName(randomDataGenerator("shortName"));
        gradeLevels_pojo.setOrder(randomDataGenerator("order"));
        gradeLevels_pojo.setActive(true);

        id = given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(gradeLevels_pojo)
                .when()
                .post("/school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")
        ;
    }

    @Test(dependsOnMethods = "createGradeLevel")
    public void editGradeLevel() {

        gradeLevels_pojo.setId(id);
        gradeLevels_pojo.setName(gradeLevels_pojo.getName() + "-->" + gradeLevels_pojo.getOrder());
        gradeLevels_pojo.setShortName(gradeLevels_pojo.getShortName() + "-->" + gradeLevels_pojo.getOrder());

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(gradeLevels_pojo)
                .log().body()
                .when()
                .put("/school-service/api/grade-levels")
                .then()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "editGradeLevel")
    public void deleteGradeLevel() {

        given()
                .cookies(cookies)
                .when()
                .delete("/school-service/api/grade-levels/" + id)
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
            case "order":
                data = RandomStringUtils.randomNumeric(2);
                break;
        }

        return data;
    }
}
