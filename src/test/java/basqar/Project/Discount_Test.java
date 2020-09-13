package basqar.Project;

import groovy.grape.GrapeIvy;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class Discount_Test {

    private String id;
    private Cookies cookies;
    private String randomDescription;
    private String randomCode;
    private String randomPriority;

    Discount_pojo discount_pojo = new Discount_pojo();


    @BeforeClass
    public void init() {

        baseURI = "https://test.basqar.techno.study";

        randomDescription = RandomStringUtils.randomAlphabetic(6);
        randomCode = RandomStringUtils.randomNumeric(3);
        randomPriority = RandomStringUtils.randomNumeric(1);

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
    public void createDiscount() {

        discount_pojo.setActive(true);
        discount_pojo.setCode(randomCode);
        discount_pojo.setDescription(randomDescription);
        discount_pojo.setPriority(randomPriority);

        id = given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(discount_pojo)
                .when()
                .post("/school-service/api/discounts")
                .then()
                .statusCode(201)
                .log().body()
                .extract().path("id");
    }

    @Test(dependsOnMethods = "createDiscount")
    public void createDiscountNegative() {

        discount_pojo.setActive(true);
        discount_pojo.setCode(randomCode);
        discount_pojo.setDescription(randomDescription);
        discount_pojo.setPriority(randomPriority);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(discount_pojo)
                .when()
                .post("/school-service/api/discounts")
                .then()
                .statusCode(400)
                .log().body()
        ;
    }

    @Test(dependsOnMethods = "createDiscount", priority = 1)
    public void updateDiscount() {

        discount_pojo.setId(id);
        discount_pojo.setActive(true);
        discount_pojo.setCode(discount_pojo.getCode() + "-->edit");
        discount_pojo.setDescription(discount_pojo.getDescription() + "-->edit");
        discount_pojo.setPriority(randomPriority);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(discount_pojo)
                .when()
                .put("/school-service/api/discounts")
                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test(dependsOnMethods = "updateDiscount")
    public void deleteDiscount() {

        given()
                .cookies(cookies)
                .pathParam("discountID", id)
                .when()
                .delete("/school-service/api/discounts/{discountID}")
                .then()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "updateDiscount")
    public void deleteDiscountNegative() {

        given()
                .cookies(cookies)
                .pathParam("discountID", id)
                .when()
                .delete("/school-service/api/discounts/{discountID}")
                .then()
                .statusCode(404)
        ;
    }

}
