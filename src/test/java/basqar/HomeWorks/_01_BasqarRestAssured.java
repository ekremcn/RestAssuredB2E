package basqar.HomeWorks;

import basqar.model._01_BankAcc_POJO;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;


public class _01_BasqarRestAssured {

    private Cookies cookies;
    private String randomGenIban;
    private String randomGenCode;
    private String randomGenName;
    private String bank_Name;

    @BeforeClass
    public void init() {
        baseURI = "https://test.basqar.techno.study";

        randomGenIban = RandomStringUtils.randomAlphabetic(2) + RandomStringUtils.randomNumeric(20);
        randomGenCode = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
        randomGenName = RandomStringUtils.randomAlphabetic(2).toUpperCase() + "Bank";

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
    public void createAndDeleteBankAcc() {
        _01_BankAcc_POJO bankAcc_pojo = new _01_BankAcc_POJO();
        bankAcc_pojo.setName(randomGenName);
        bankAcc_pojo.setIban(randomGenIban);
        bankAcc_pojo.setIntegrationCode(randomGenCode);
        bankAcc_pojo.setCurrency("USD");
        bankAcc_pojo.setActive(true);

        bank_Name = given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(bankAcc_pojo)
                .when()
                .post("/school-service/api/bank-accounts")
                .then()
                .log().body()
                .statusCode(201)
                .extract().path("name")
        ;
        System.out.println(bank_Name);

        given()
                .when()
                .then()

        ;
    }
}
