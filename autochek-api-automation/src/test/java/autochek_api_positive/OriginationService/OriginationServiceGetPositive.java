package autochek_api_positive.OriginationService;


import Base.Initialization;
import Listener.AllureLogs;
import Utils.Postive_Data_Extractor;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.*;

import static io.restassured.RestAssured.given;
@Epic("Origination")
@Feature("Positive Tests Response Payload Reduction (V2 APIs)")
public class OriginationServiceGetPositive {
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
    @Description("Validates GET /v2/origination/loans with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "severity", "testDescription", "page_size", "expectedStatusCode"})
    @Story("Get Loans List")
    public void GetLoansList(String type, String severity, String testDescription,
                             String page_size, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        // Send GET request with query params
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", country)
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

        // Validate pagination fields
        int total = response.path("pagination.total");
        int pageSize = response.path("pagination.pageSize");
        int currentPage = response.path("pagination.currentPage");
        List<String> loanIds = response.path("loanLeads.id");

        // Assert: IDs list is not empty
        AllureLogs.softAssertTrue(
                softAssert,
                loanIds != null && loanIds.size() > 0,
                "Result loan IDs list should not be empty"
        );

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) total / pageSize);

        if (total <= pageSize) {
            AllureLogs.softAssertTrue(
                    softAssert,
                    loanIds.size() == total && currentPage == 1 && totalPages == 1,
                    "When total < pageSize, current page should be 1 and totalPages should be 1"
            );
        } else {
            AllureLogs.softAssertEquals(
                    softAssert,
                    loanIds.size(),
                    pageSize,
                    "Total pages calculation should match expected formula"
            );
        }
        long responseTime = response.time();
        AllureLogs.softAssertTrue(
                softAssert,
                responseTime <= 2000,
                "Response time should be <= 2000ms, actual: " + responseTime + "ms"
        );

        int responseSize = response.asByteArray().length;

// Always log the pageSize
        AllureLogs.step("Page size returned by API: " + pageSize);

        if (pageSize <= 10) {
            // If pageSize <= 10, validate response size
            AllureLogs.step("Response size returned by API: " + responseSize + " bytes");
            AllureLogs.softAssertTrue(
                    softAssert,
                    responseSize <= 5120,
                    "Response size should be <= 5KB, actual: " + responseSize + " bytes"
            );
        } else {
            // If pageSize > 10, only log response size (no validation)
            AllureLogs.step("Page size is > 10. Response size returned by API: " + responseSize + " bytes");
        }


        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Loans")
    public Object[][] GetLoans() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Postive_Data_Extractor.ExcelData("Get Loans");
    }
    private static String loan_id="49yHgdwcN";
    @Test()
    @Description("Fetching loan by ID")
    @Story("Get Loan By ID")
    @Severity(SeverityLevel.NORMAL)

    public static  void GetLoanById() throws InterruptedException {
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/v2/origination/loans/"+loan_id)
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Expected Status : 200");

        JsonPath json = response.jsonPath();

        AllureLogs.softAssertEquals(softAssert, json.getString("id"), loan_id, "Loan ID");
        AllureLogs.softAssertNotNull(softAssert, json.getString("carId"), "Car ID");
        AllureLogs.softAssertNotNull(softAssert, json.getString("country"), "Country");
        AllureLogs.softAssertNotNull(softAssert, json.getString("customerId"), "Customer ID");
        AllureLogs.softAssertNotNull(softAssert, json.getString("loanCurrency"), "Loan Currency");
        AllureLogs.softAssertNotNull(softAssert, json.getString("status"), "Loan Status");

        AllureLogs.softAssertNotNull(softAssert, json.getString("firstName"), "First Name");
        AllureLogs.softAssertNotNull(softAssert, json.getString("lastName"), "Last Name");
        AllureLogs.softAssertNotNull(softAssert, json.getString("email"), "Email");
        AllureLogs.softAssertNotNull(softAssert, json.getString("phone"), "Phone");
        AllureLogs.softAssertNotNull(softAssert, json.getString("creator.firstname"), "Creator FistName");
        AllureLogs.softAssertNotNull(softAssert, json.getString("creator.lastname"), "Creator LastName");
        AllureLogs.softAssertNotNull(softAssert, json.getString("loanCustomerValidation"), "loanCustomerValidation");


        long responseTime = response.time();
        AllureLogs.softAssertTrue(
                softAssert,
                responseTime <= 2000,
                "Response time should be <= 2000ms, actual: " + responseTime + "ms"
        );

        int responseSize = response.asByteArray().length;

            AllureLogs.step("Response size returned by API: " + responseSize + " bytes");
            AllureLogs.softAssertTrue(
                    softAssert,
                    responseSize <= 5120,
                    "Response size should be <= 5KB, actual: " + responseSize + " bytes"
            );

         AllureLogs.executeSoftAssertAll(softAssert);


    }
    @Test(dataProvider = "Get Offers")
    @Description("Validates GET /v2/origination/offers with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "severity", "testDescription", "page_size", "expectedStatusCode"})
    @Story("Get Offers List")
    public void GetOffersList(String type, String severity, String testDescription,
                              String page_size, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", country)
                .queryParam("page_size", page_size)
                .when()
                .get("/v2/origination/offers")
                .then()
                .log().all()
                .extract().response();

        AllureLogs.softAssertEquals(softAssert, response.statusCode(), Integer.parseInt(expectedStatusCode), "Expected status code: " + expectedStatusCode);

        long responseTime = response.time();
        AllureLogs.step("Response time: " + responseTime + " ms");
        AllureLogs.softAssertTrue(softAssert, responseTime <= 2000, "Response time should be <= 2s");

        int responseSize = response.asByteArray().length;
        AllureLogs.step("Response size returned by API: " + responseSize + " bytes");

        if (Integer.parseInt(page_size) <= 10) {
            AllureLogs.softAssertTrue(
                    softAssert,
                    responseSize <= 5120,
                    "Response size should be <= 5KB, actual: " + responseSize + " bytes"
            );
        } else {
            AllureLogs.step("Page size is > 10. Only logging response size: " + responseSize + " bytes");
        }

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Offers")
    public Object[][] GetOffers() {
        // Reads test data from Excel sheet named "Get Offers"
        return Postive_Data_Extractor.ExcelData("Get Offers");
    }

    @Test(dataProvider = "Get Partners")
    @Description("Validates GET /v2/origination/partners with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "severity", "testDescription", "page_size", "expectedStatusCode"})
    @Story("Get Partners List")
    public void GetPartnersList(String type, String severity, String testDescription,
                                String page_size, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", country)
                .queryParam("page_size", page_size)
                .when()
                .get("/v2/origination/partners")
                .then()
                .log().all()
                .extract().response();

        AllureLogs.softAssertEquals(softAssert, response.statusCode(), Integer.parseInt(expectedStatusCode), "Expected status code: " + expectedStatusCode);

        long responseTime = response.time();
        AllureLogs.step("Response time: " + responseTime + " ms");
        AllureLogs.softAssertTrue(softAssert, responseTime <= 2000, "Response time should be <= 2s");

        int responseSize = response.asByteArray().length;
        AllureLogs.step("Response size returned by API: " + responseSize + " bytes");

        if (Integer.parseInt(page_size) <= 10) {
            AllureLogs.softAssertTrue(
                    softAssert,
                     responseSize <= 5120,
                    "Response size should be <= 5KB, actual: " + responseSize + " bytes"
            );
        } else {
            AllureLogs.step("Page size is > 10. Only logging response size: " + responseSize + " bytes");
        }

        JsonPath json = response.jsonPath();
        List<Map<String, Object>> partners = response.jsonPath().getList("partners");

        AllureLogs.softAssertTrue(softAssert,
                partners != null && !partners.isEmpty(),
                "Sources list should not be empty");

        for (int i = 0; i < partners.size(); i++) {
            Map<String, Object> source = partners.get(i);

            AllureLogs.softAssertNotNull(softAssert,
                    source.get("id"),
                    "Source[" + i + "].id should not be null");

            AllureLogs.softAssertNotNull(softAssert,
                    source.get("name"),
                    "Source[" + i + "].name should not be null");

            AllureLogs.softAssertNotNull(softAssert,
                    source.get("logoUrl"),
                    "Source[" + i + "].logoUrl should not be null");


        }

        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @DataProvider(name = "Get Partners")
    public Object[][] GetPartners() {
        // Reads test data from Excel sheet named "Get Partners"
        return Postive_Data_Extractor.ExcelData("Get Partners");
    }

    @Test()
    @Description("Get Offer Details by LoanID")
    @Story("Get Offers By LoanID")
    @Severity(SeverityLevel.NORMAL)
    public static  void GetOffersByLoanId() throws InterruptedException {
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/v2/origination/loans/"+loan_id+"/offers")
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Expected Status : 200");
        List<Map<String, Object>> offers = response.jsonPath().getList("offers");
        AllureLogs.softAssertNotNull(softAssert, offers, "Offers list should not be null");
        AllureLogs.softAssertEquals(softAssert, offers.isEmpty(), false, "Offers list should not be empty");

        for (int i = 0; i < offers.size(); i++) {
            String base = "offers[" + i + "]";
            String status = response.jsonPath().getString(base + ".status");

            // ---------- Scenario 1: Rejection Case ----------
            if ("OFFER_REJECTED".equalsIgnoreCase(status)) {
                Map<String, Boolean> rejectionReasons = response.jsonPath().getMap(base + ".rejectionReasons");
                AllureLogs.softAssertNotNull(softAssert, rejectionReasons, "rejectionReasons must be present for rejected offers");

                if (rejectionReasons != null) {
                    for (Map.Entry<String, Boolean> entry : rejectionReasons.entrySet()) {
                        AllureLogs.softAssertEquals(softAssert, entry.getValue(), true,
                                "All rejectionReasons should have true values: " + entry.getKey());
                    }
                }

                // loanSummary should not exist
                AllureLogs.softAssertEquals(softAssert, response.jsonPath().getString(base + ".loanSummary"), null,
                        "loanSummary must not be present for rejected offers");
            }

            // ---------- Scenario 2: Non-Rejection Case ----------
            else {
                // rejectionReasons should not exist
                Object rr = response.jsonPath().get(base + ".rejectionReasons");
                AllureLogs.softAssertEquals(softAssert, rr, null,
                        "rejectionReasons should not be present for non-rejected offers");
            }

            // ---------- Scenario 3 & 4: loanSummary Handling ----------

            Map<String, Object> loanSummary = response.jsonPath().getMap(base + ".loanSummary");

            if (loanSummary != null && loanSummary.containsKey("loan")) {
                String loanValue = String.valueOf(loanSummary.get("loan"));

                // Regex to match only numbers or decimals (e.g. 123, 123.45)
                if (loanValue.matches("^\\d+(\\.\\d+)?$")) {
                    AllureLogs.step("loanSummary.loan is valid numeric: " + loanValue);
                } else {
                    AllureLogs.softAssertEquals(softAssert, false, true,
                            "loanSummary.loan value is NaN: " + loanValue);
                }
            }
        }

        AllureLogs.executeSoftAssertAll(softAssert);

    }


    private static String offerID="4EMI0UuCW";
    @Test()
    @Description("Get Offer Details by OfferID")
    @Story("Get Offers By OfferID")
    @Severity(SeverityLevel.NORMAL)
    public static  void GetOffersById() throws InterruptedException {
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/v2/origination/offers/" + offerID)
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Expected Status : 200");

        String id = response.jsonPath().getString("id");
        Map<String, Object> partner = response.jsonPath().getMap("partner");
        Map<String, Object> loanSummary = response.jsonPath().getMap("loanSummary");
        Map<String, Object> rejectionReasons = response.jsonPath().getMap("rejectionReasons");

        // -------------------------
        // 1. Validate ID matches requested offerID
        // -------------------------
        AllureLogs.softAssertEquals(softAssert, id, offerID, "Validate Offer ID matches requested ID");

        // -------------------------
        // 2. Validate 'partner' object present and has required keys
        // -------------------------
        if (partner != null) {
            AllureLogs.softAssertTrue(softAssert, partner.containsKey("id"), "partner.id is present");
            AllureLogs.softAssertTrue(softAssert, partner.containsKey("name"), "partner.name is present");
            AllureLogs.softAssertTrue(softAssert, partner.containsKey("logoUrl"), "partner.logoUrl is present");
        } else {
            AllureLogs.softAssertEquals(softAssert, false, true, "partner object is missing");
        }

        // -------------------------
        // 3. Validate 'loanSummary' object logic
        // -------------------------
        if (loanSummary != null) {
            // Verify loanSummary.loan is a valid numeric (not NaN)
            String loanValue = String.valueOf(loanSummary.get("loan"));
            if (loanValue.matches("^\\d+(\\.\\d+)?$")) {
                AllureLogs.step("loanSummary.loan is valid numeric: " + loanValue);
            } else {
                AllureLogs.softAssertEquals(softAssert, false, true, "loanSummary.loan is NaN: " + loanValue);
            }

            // Ensure other expected fields exist
            AllureLogs.softAssertTrue(softAssert, loanSummary.containsKey("equity"), "loanSummary.equity is present");
            AllureLogs.softAssertTrue(softAssert, loanSummary.containsKey("additionalFees"), "loanSummary.additionalFees is present");
        } else {
            AllureLogs.softAssertEquals(softAssert, false, true, "loanSummary object is missing");
        }

        // -------------------------
        // 4. Validate rejectionReasons conditional logic
        // -------------------------
        if (rejectionReasons != null) {
            // If present: must have at least one key = true
            boolean hasTrue = rejectionReasons.values().stream()
                    .anyMatch(v -> String.valueOf(v).equalsIgnoreCase("true"));
            AllureLogs.softAssertTrue(softAssert, hasTrue,
                    "rejectionReasons present and has at least one true key");
        } else {
            // If not present, ensure status is not a rejected status
            String status = response.jsonPath().getString("status");
            if ("OFFER_REJECTED".equalsIgnoreCase(status)) {
                AllureLogs.softAssertEquals(softAssert, false, true,
                        "status is OFFER_REJECTED but rejectionReasons object is missing");
            } else {
                AllureLogs.step("rejectionReasons correctly not present for non-rejected offer");
            }
        }
        long responseTime = response.time();
        AllureLogs.softAssertTrue(
                softAssert,
                responseTime <= 2000,
                "Response time should be <= 2000ms, actual: " + responseTime + "ms"
        );

        int responseSize = response.asByteArray().length;

        AllureLogs.step("Response size returned by API: " + responseSize + " bytes");
        AllureLogs.softAssertTrue(
                softAssert,
                responseSize <= 5120,
                "Response size should be <= 5KB, actual: " + responseSize + " bytes"
        );
        // Execute all assertions
        AllureLogs.executeSoftAssertAll(softAssert);
    }



    @Test(dataProvider = "Get Sources")
    @Description("Validates GET /v2/origination/sources with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "severity", "testDescription", "page_size", "expectedStatusCode"})
    @Story("Get Sources List")
    public void GetSourcesList(String type, String severity, String testDescription,
                                String page_size, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", country)
                .queryParam("page_size", page_size)
                .when()
                .get("/v2/origination/sources")
                .then()
                .log().all()
                .extract().response();

// ✅ Status code check
        AllureLogs.softAssertEquals(softAssert,
                response.statusCode(),
                Integer.parseInt(expectedStatusCode),
                "Expected status code: " + expectedStatusCode);
// Validate pagination fields
        int total = response.path("pagination.total");
        int pageSize = response.path("pagination.pageSize");
        int currentPage = response.path("pagination.currentPage");
        List<String> sourceIds = response.path("sources.id");


        // Calculate total pages
        int totalPages = (int) Math.ceil((double) total / pageSize);
        long responseTime = response.time();
        AllureLogs.step("Response time: " + responseTime + " ms");
        AllureLogs.softAssertTrue(softAssert, responseTime <= 2000, "Response time should be <= 2s");

        int responseSize = response.asByteArray().length;
        AllureLogs.step("Response size returned by API: " + responseSize + " bytes");

        if (Integer.parseInt(page_size) <= 10) {
            AllureLogs.softAssertTrue(
                    softAssert,
                    responseSize <= 5120,
                    "Response size should be <= 5KB, actual: " + responseSize + " bytes"
            );
        } else {
            AllureLogs.step("Page size is > 10. Only logging response size: " + responseSize + " bytes");
        }
        if (total <= pageSize) {
            AllureLogs.softAssertTrue(
                    softAssert,
                    sourceIds.size() == total && currentPage == 1 && totalPages == 1,
                    "When total < pageSize, current page should be 1 and totalPages should be 1"
            );
        } else {
            AllureLogs.softAssertEquals(
                    softAssert,
                    sourceIds.size(),
                    pageSize,
                    "Total pages calculation should match expected formula"
            );
        }
        // ✅ Validate response structure and values
        List<Map<String, Object>> sources = response.jsonPath().getList("sources");

        AllureLogs.softAssertTrue(softAssert,
                sources != null && !sources.isEmpty(),
                "Sources list should not be empty");

        for (int i = 0; i < sources.size(); i++) {
            Map<String, Object> source = sources.get(i);

            AllureLogs.softAssertNotNull(softAssert,
                    source.get("label"),
                    "Source[" + i + "].label should not be null");

            AllureLogs.softAssertNotNull(softAssert,
                    source.get("name"),
                    "Source[" + i + "].name should not be null");

            AllureLogs.softAssertNotNull(softAssert,
                    source.get("id"),
                    "Source[" + i + "].id should not be null");

            AllureLogs.softAssertEquals(softAssert,
                    source.get("country"),
                    country,
                    "Source[" + i + "].country should be: " + country);
        }

        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @DataProvider(name = "Get Sources")
    public Object[][] GetSources() {
        // Reads test data from Excel sheet named "Get Partners"
        return Postive_Data_Extractor.ExcelData("Get Sources");
    }




    @Test(dataProvider = "Get Banks")
    @Description("Validates GET /v2/origination/banks with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "severity", "testDescription", "page_size", "expectedStatusCode"})
    @Story("Get Banks List")
    public void GetBanksList(String type, String severity, String testDescription,
                               String page_size, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", country)
                .queryParam("page_size", page_size)
                .when()
                .get("/v2/origination/banks")
                .then()
                .log().all()
                .extract().response();

// ✅ Status code check
        AllureLogs.softAssertEquals(softAssert,
                response.statusCode(),
                Integer.parseInt(expectedStatusCode),
                "Expected status code: " + expectedStatusCode);
        long responseTime = response.time();
        AllureLogs.step("Response time: " + responseTime + " ms");
        AllureLogs.softAssertTrue(softAssert, responseTime <= 2000, "Response time should be <= 2s");

        int responseSize = response.asByteArray().length;
        AllureLogs.step("Response size returned by API: " + responseSize + " bytes");

        if (Integer.parseInt(page_size) <= 10) {
            AllureLogs.softAssertTrue(
                    softAssert,
                    responseSize <= 5120,
                    "Response size should be <= 5KB, actual: " + responseSize + " bytes"
            );
        } else {
            AllureLogs.step("Page size is > 10. Only logging response size: " + responseSize + " bytes");
        }
// Validate pagination fields
        int total = response.path("pagination.total");
        int pageSize = response.path("pagination.pageSize");
        int currentPage = response.path("pagination.currentPage");
        List<String> banksIds = response.path("banks.id");


        // Calculate total pages
        int totalPages = (int) Math.ceil((double) total / pageSize);

        if (total <= pageSize) {
            AllureLogs.softAssertTrue(
                    softAssert,
                    banksIds.size() == total && currentPage == 1 && totalPages == 1,
                    "When total < pageSize, current page should be 1 and totalPages should be 1"
            );
        } else {
            AllureLogs.softAssertEquals(
                    softAssert,
                    banksIds.size(),
                    pageSize,
                    "Total pages calculation should match expected formula"
            );
        }
        // ✅ Validate response structure and values
        List<Map<String, Object>> banks = response.jsonPath().getList("banks");

        for (int i = 0; i < banks.size(); i++) {
            Map<String, Object> banksMap = banks.get(i);

            AllureLogs.softAssertNotNull(softAssert,
                    banksMap.get("id"),
                    "Source[" + i + "].label should not be null");

            AllureLogs.softAssertNotNull(softAssert,
                    banksMap.get("name"),
                    "Source[" + i + "].name should not be null");

            AllureLogs.softAssertEquals(softAssert,
                    banksMap.get("country"),
                    country,
                    "Source[" + i + "].country should be: " + country);
        }

        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @DataProvider(name = "Get Banks")
    public Object[][] GetBanks() {
        // Reads test data from Excel sheet named "Get Partners"
        return Postive_Data_Extractor.ExcelData("Get Banks");
    }


//
//    @Test()
//    @Description("Get Offer Details by OfferID")
//    @Story("Get Offers By OfferID")
//    @Severity(SeverityLevel.NORMAL)
//    public static  void GetLoanDocumentByOfferID() throws InterruptedException {
//        Response response = given()
//                .header("Authorization", "Bearer " + token)
//                .when()
//                .get("/v2/origination/offers/"+offerID+"/loan-document")
//                .then()
//                .log().all()
//                .extract().response();
//        SoftAssert softAssert = new SoftAssert();
//        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Expected Status : 200");
//
//
//        // Execute all assertions
//        AllureLogs.executeSoftAssertAll(softAssert);
//    }




}
