package Base;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Initialization {
    public static String ByPassKey;
    public static String AuthorizeKey;
    public static Map<String,String> franchiseAdminToken,accountManagerToken,dealerDsaToken,dsaAgentToken,consoleAdminToken,InspectorAppToken, UnderWriterUI,InspectionToken,UnderWriterUIUser;
    public static RequestSpecification stagingAPI;
    public static RequestSpecification underWriterAPI;
    @BeforeSuite
    public void cleanAllureResults() {
        File allureResultsDir = new File("allure-results");
        if (allureResultsDir.exists() && allureResultsDir.isDirectory()) {
            for (File file : allureResultsDir.listFiles()) {
                file.delete();
            }
            System.out.println("✅ Allure results folder cleaned before suite execution.");
        }
    }
    public void initializeEnvironment() {
        urlSetUp();
        franchiseAdminToken = generateAuthToken("suhail+1@frugaltestingin.com", "Autochek@123");
//        accountManagerToken = generateAuthToken("suhail@frugaltestingin.com", "Autochek@123");
//        dealerDsaToken = generateAuthToken("suhail@frugaltestingin.com", "Autochek@123");
//        dsaAgentToken = generateAuthToken("suhail@frugaltestingin.com", "Autochek@123");
    consoleAdminToken = generateAuthToken("bharti@frugaltesting.com", "AutochekF123");
//     InspectorAppToken = generateAuthToken("daniel.o+workng@autochek.africa","Password");
//        UnderWriterUIUser=generateAuthToken("eshareddy@frugaltestingin.com","Autochek@123");
//        System.out.println(franchiseAdminToken.get("token"));

//        InspectorAppToken = generateAuthToken("daniel.o+workng@autochek.africa","Password");
//        System.out.println(franchiseAdminToken.get("token"));
        ByPassKey="6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI";
    }

    public void urlSetUp() {
        RestAssured.baseURI = "https://api.staging.myautochek.com";
        System.out.println("intilization "+"https://api.staging.myautochek.com");
    }

    public Map<String, String> generateAuthToken(String email, String password) {
        String requestBody = "{\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"password\": \"" + password + "\"\n" +
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/v1/auth/login");

        String token = response.jsonPath().getString("token");
        String userId = response.jsonPath().getString("user.id");
        Map<String,String> auth_details = new HashMap<>();
        auth_details.put("token",token);
        auth_details.put("userId",userId);
        System.out.println("Token generated for " + email + ": " + token + " User id : "+userId);
        return auth_details;
    }

    public void initializeEnvironmentUnderWriterUI() {
        urlSetUpUnderWriterUI();
        UnderWriterUI = generateAuthTokenUnderWriterUI("eshareddy@frugaltestingin.com", "Autochek@123");
        AuthorizeKey="y53gD0eyhQuLbp9jfM376iLcGwQy6EU4KcjxGTgd";
    }

    public void urlSetUpUnderWriterUI() {
        RestAssured.baseURI = "https://81l3dcfgqe.execute-api.us-east-1.amazonaws.com/stage";
    }

    public Map<String, String> generateAuthTokenUnderWriterUI(String email, String password) {
        String requestBody = "{\n" +
                "  \"userId\": \"IprIPvMH9\",\n" +
                "  \"roles\": [\n" +
                "    \"LOAN ADMIN\",\n" +
                "    \"CREDIT_OFFICER\",\n" +
                "    \"CREDIT_ADMIN\"\n" +
                "  ],\n" +
                "  \"email\": \"eshareddy@frugaltestingin.com\"\n" +
                "}";

        Response response = given()
                .header("x-api-key", "y53gD0eyhQuLbp9jfM376iLcGwQy6EU4KcjxGTgd")  // API Key
                .contentType(ContentType.JSON)  // request content type
                .body(requestBody)
                .when()
                .post("/app/auth")   // endpoint
                .then()
                .statusCode(201)
                .log().all()// expected response code
                .extract()
                .response();
        String token = response.jsonPath().getString("accessToken");
        String userId = response.jsonPath().getString("user.id");
        Map<String,String> auth_details = new HashMap<>();
        auth_details.put("token",token);
        auth_details.put("userId",userId);
        System.out.println("Token generated for " + email + ": " + token + " User id : "+userId);
        return auth_details;
    }
}













