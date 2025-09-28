package autochek_api_negative.DynamicResponse;

import Base.Initialization;
import Listener.AllureLogs;
import Utils.Negative_Data_Extractor;
import Utils.Postive_Data_Extractor;
import io.qameta.allure.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;




@Epic("Inventory Service")
@Feature("Negative Tests Dynamic Response")
public class DynamicResponseNegative {
    Initialization initialization = new Initialization();

    // Global variables used across tests
    private static String franchiseId;
    private static String user_id;
    private static String token;
    private static String Consoletoken;

    private static String status = "pending";
    private static String country = "NG";

    // Stores created franchise data for validation across tests
    private static final Map<String, Map<String, String>> franchiseResponseMap = new LinkedHashMap<>();

    @BeforeClass
    public void setUp() {
        initialization.initializeEnvironment(); // Sets base URI and tokens
        user_id = Initialization.franchiseAdminToken.get("userId");
        token = Initialization.franchiseAdminToken.get("token");
        Consoletoken = Initialization.consoleAdminToken.get("token");
    }

    @Test(dataProvider = "Get Cars")
    @Description("Validates GET /v1/inventory/car with dynamic parameters - Negative Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Cars List")
    public void GetCarListNegative(String type, String severity, String testDescription,
                           String fields, String relations, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        RequestSpecification request = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", country);

        if (fields != null && !fields.equals("null") && !fields.trim().isEmpty()) {
            request.queryParam("fields", fields);
        } else if (relations != null && !relations.equals("null") && !relations.trim().isEmpty()) {
            request.queryParam("relations", relations);
        }

        // ✅ Send GET request
        Response response = request
                .when()
                .get("/v1/inventory/car")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status code validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );

        List<Map<String, Object>> carList = response.jsonPath().getList("carList");
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Cars")
    public Object[][] GetCarList() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Get Cars Negative");
    }




    @Test(dataProvider = "Get Make")
    @Description("Validates GET /v1/inventory/make with dynamic fields filtering - Negative Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Make")
    public void GetMakeNegative(String type, String severity, String testDescription,
                        String fields, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        // Send GET request with fields param
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("fields", fields)
                .when()
                .get("/v1/inventory/make")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status Code Validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Make")
    public Object[][] GetmakeNegative() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Get Make Negative");
    }


    @Test(dataProvider = "Get Model")
    @Description("Validates GET /v1/inventory/model with dynamic parameters - Negative Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Model List")
    public void GetModelListNegative(String type, String severity, String testDescription,
                             String fields, String relations, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        RequestSpecification request = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", country);

        if (fields != null && !fields.equals("null") && !fields.trim().isEmpty()) {
            request.queryParam("fields", fields);
        } else if (relations != null && !relations.equals("null") && !relations.trim().isEmpty()) {
            request.queryParam("relations", relations);
        }

        // ✅ Send GET request
        Response response = request
                .when()
                .get("/v1/inventory/model")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status code validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Model")
    public Object[][] GetModelList() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Get Model Negative");
    }




    @Test(dataProvider = "Get Trims")
    @Description("Validates GET /v1/inventory/trims with dynamic parameters - Negative Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Trims")
    public void GetTrimsNegative(String type, String severity, String testDescription,
                         String fields, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        // Send GET request with fields param
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("fields", fields)
                .when()
                .get("/v1/inventory/trims")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status Code Validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Trims")
    public Object[][] GetTrims() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Get Trims Negative");
    }


    @Test(dataProvider = "Get Inspection Request")
    @Description("Validates GET /v1/inventory/inspection_request with dynamic parameters - Negative Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Inspection Request")
    public void GetInspectionRequestNegative(String type, String severity, String testDescription,
                                     String fields, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        // Send GET request with fields param
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("fields", fields)
                .when()
                .get("/v1/inventory/inspection_request")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status Code Validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Inspection Request")
    public Object[][] GetInspectionRequest() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Get Inspection Request Negative");
    }


    @Test(dataProvider = "Get Inspection")
    @Description("Validates GET /v1/inspection with dynamic parameters - Negative Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Inspection")
    public void GetInspectionNegative(String type, String severity, String testDescription,
                              String fields, String relations, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        RequestSpecification request = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", country);
        request.queryParam("page_number", "1");
        request.queryParam("page_size", "10");
        request.queryParam("type", "regular");


        if (fields != null && !fields.equals("null") && !fields.trim().isEmpty()) {
            request.queryParam("fields", fields);
        } else if (relations != null && !relations.equals("null") && !relations.trim().isEmpty()) {
            request.queryParam("relations", relations);
        }

        // ✅ Send GET request
        Response response = request
                .when()
                .get("/v1/inspection")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status code validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );


        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Inspection")
    public Object[][] GetInspection() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Get Inspection Negative");
    }




    @Test(dataProvider = "Get Body Type")
    @Description("Validates GET /v1/inventory/body_type with dynamic parameters - Negative Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Trims")
    public void GetBodyTypeNegative(String type, String severity, String testDescription,
                            String fields, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        // Send GET request with fields param
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("fields", fields)
                .when()
                .get("/v1/inventory/body_type")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status Code Validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Body Type")
    public Object[][] GetBodyType() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Get BodyType Negative");
    }




    @Test(dataProvider = "Get obds")
    @Description("Validates GET /v1/inventory/obds with dynamic parameters - Negative Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get obds")
    public void GetObdsNegative(String type, String severity, String testDescription,
                        String fields, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        // Send GET request with fields param
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("fields", fields)
                .when()
                .get("/v1/inventory/obds")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status Code Validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get obds")
    public Object[][] Getobds() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Get obds Negative");
    }




}
