package autochek_api_negative.OriginationService;

import Base.Initialization;
import Listener.AllureLogs;
import Utils.Negative_Data_Extractor;
import Utils.Postive_Data_Extractor;
import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
@Epic("Origination")
@Feature("Negative Tests Response Payload Reduction (V2 APIs)")
public class OriginationServiceGetNegative {
    Initialization initialization = new Initialization();

    // Global variables used across tests
    private static String franchiseId;
    private static String user_id;
    private static String token;
    private static String Consoletoken;
    private static String random_email;
    private static String random_phoneNumber;
    private static String random_accountNumber;
    private static String random_accountBvn;
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


    @Test(dataProvider = "Get Loans")
    @Description("Validates GET /v2/origination/loans with dynamic parameters - Negative Test")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "severity", "testDescription", "page_size","CountryCode", "expectedStatusCode"})
    @Story("Get Loans List")
    public void GetLoansListNegative(String type, String severity, String testDescription,
                             String page_size, String CountryCode,String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        // Send GET request with query params
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", CountryCode)
                .queryParam("page_size", page_size)
                .when()
                .get("/v2/origination/loans")
                .then()
                .log().all()
                .extract().response();

        // Validate response status code
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );


        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Loans")
    public Object[][] GetLoans() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Get Loans Negative");
    }
    @Test(dataProvider = "Get Loan By ID")
    @Description("Fetching loan by ID")
    @Story("Get Loan By ID")
    @Severity(SeverityLevel.NORMAL)
    @Parameters({"type", "severity", "testDescription","loan_id", "expectedStatusCode"})
    public static  void GetLoanByIdNegative(String type,String Severity,String testDescription,String loan_id,String expectedStatusCode) throws InterruptedException {
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/v2/origination/loans/"+loan_id)
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), Integer.parseInt(expectedStatusCode), "Expected Status : 200");
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Loan By ID")
    public Object[][] GetLoanID() {
        // Reads test data from Excel sheet named "Get Offers"
        return Negative_Data_Extractor.ExcelData("Get Loan By ID Negative");
    }

    @Test(dataProvider = "Get Offers")
    @Description("Validates GET /v2/origination/offers with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "severity", "testDescription", "page_size","CountryCode", "expectedStatusCode"})
    @Story("Get Offers List")
    public void GetOffersListNegative(String type, String severity, String testDescription,
                              String page_size,String CountryCode, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", CountryCode)
                .queryParam("page_size",  page_size)
                .when()
                .get("/v2/origination/offers")
                .then()
                .log().all()
                .extract().response();

        AllureLogs.softAssertEquals(softAssert, response.statusCode(), Integer.parseInt(expectedStatusCode), "Expected status code: " + expectedStatusCode);

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Offers")
    public Object[][] GetOffers() {
        // Reads test data from Excel sheet named "Get Offers"
        return Negative_Data_Extractor.ExcelData("Get Offers Negative");
    }
    @Test(dataProvider = "Get Partners")
    @Description("Validates GET /v2/origination/partners with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "severity", "testDescription", "page_size","CountryCode", "expectedStatusCode"})
    @Story("Get Partners List")
    public void GetPartnersListNegative(String type, String severity, String testDescription,
                                String page_size,String CountryCode, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", CountryCode)
                .queryParam("page_size", page_size)
                .when()
                .get("/v2/origination/partners")
                .then()
                .log().all()
                .extract().response();

        AllureLogs.softAssertEquals(softAssert, response.statusCode(), Integer.parseInt(expectedStatusCode), "Expected status code: " + expectedStatusCode);

        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @DataProvider(name = "Get Partners")
    public Object[][] GetPartners() {
        // Reads test data from Excel sheet named "Get Partners"
        return Negative_Data_Extractor.ExcelData("Get Partners Negative");
    }


    @Test(dataProvider = "Get Offers By LoanID")
    @Description("Get Offer Details by LoanID")
    @Story("Get Offers By LoanID")
    @Parameters({"type", "severity", "testDescription","loan_id", "expectedStatusCode"})
    @Severity(SeverityLevel.NORMAL)
    public static  void GetOffersByLoanIdNegative(String type,String Severity,String testDescription,String loan_id,String expectedStatusCode) throws InterruptedException {
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/v2/origination/loans/" + loan_id + "/offers")
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), Integer.parseInt(expectedStatusCode), "Expected Status : 400");
        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @DataProvider(name = "Get Offers By LoanID")
    public Object[][] GetOffersByLoanId() {
        // Reads test data from Excel sheet named "Get Partners"
        return Negative_Data_Extractor.ExcelData("Get Offer By LoanID");
    }


    @Test(dataProvider = "Get Offers By ID")
    @Description("Get Offer Details by OfferID")
    @Story("Get Offers By OfferID")
    @Severity(SeverityLevel.NORMAL)
    public static  void GetOffersByIdNegative(String type,String Severity,String testDescription,String OfferID,String expectedStatusCode) throws InterruptedException {
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country","NG")
                .when()
                .get("/v2/origination/offers/" + OfferID)
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(),  Integer.parseInt(expectedStatusCode), "Expected Status : 200");


        // Execute all assertions
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Offers By ID")
    public Object[][] GetOffersById() {
        // Reads test data from Excel sheet named "Get Partners"
        return Negative_Data_Extractor.ExcelData("Get Offers By OfferID Negative");
    }



    @Test(dataProvider = "Get Sources")
    @Description("Validates GET /v2/origination/sources with dynamic parameters - Negative Test")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "severity", "testDescription", "page_size","CountryCode", "expectedStatusCode"})
    @Story("Get Sources List")
    public void GetSourcesListNegative(String type, String severity, String testDescription,
                                       String page_size,String CountryCode, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", CountryCode)
                .queryParam("page_size", page_size)
                .when()
                .get("/v2/origination/sources")
                .then()
                .log().all()
                .extract().response();

        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), Integer.parseInt(expectedStatusCode), "Expected Status : 400");
        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @DataProvider(name = "Get Sources")
    public Object[][] GetSources() {
        // Reads test data from Excel sheet named "Get Partners"
        return Negative_Data_Extractor.ExcelData("Get Sources Negative");
    }


    @Test(dataProvider = "Get banks")
    @Description("Validates GET /v2/origination/sources with dynamic parameters - Negative Test")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "severity", "testDescription", "page_size","CountryCode", "expectedStatusCode"})
    @Story("Get Banks List")
    public void GetBanksListNegative(String type, String severity, String testDescription,
                                       String page_size,String CountryCode, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", CountryCode)
                .queryParam("page_size", page_size)
                .when()
                .get("/v2/origination/banks")
                .then()
                .log().all()
                .extract().response();

        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), Integer.parseInt(expectedStatusCode), "Expected Status : 400");
        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @DataProvider(name = "Get banks")
    public Object[][] GetBanks() {
        // Reads test data from Excel sheet named "Get Partners"
        return Negative_Data_Extractor.ExcelData("Get Banks Negative");
    }

}
