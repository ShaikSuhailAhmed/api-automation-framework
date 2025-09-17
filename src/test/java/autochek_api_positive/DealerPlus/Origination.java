package autochek_api_positive.DealerPlus;

import Base.Initialization;
import Listener.AllureLogs;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;

import io.restassured.response.Response;
import Utils.Postive_Data_Extractor;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
@Epic("API Automation")
@Feature("Positive")
@Story("Origination")
public class Origination {
    Initialization initialization = new Initialization();

    @BeforeClass
    public void setUp() {
        initialization.initializeEnvironment(); // Sets base URI
    }

    @Test(dataProvider = "Get all loan products")
    @Parameters({"name", "country"})
    public void getLoanProduct(String name, String country) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .baseUri("https://api.staging.myautochek.com")
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken ) // Remove this if API is public
                .queryParam("name", name)
                .queryParam("country", country)
                .when()
                .get("/v1/origination/product")
                .then()
                .log().all()
                .extract().response();

        System.out.println("API Response: " + response.asString());

        // Basic Validation

// Validate status code using custom method
        AllureLogs.softAssertEquals(softAssert, response.statusCode(), 200, "Status Code");

// Validate result array is not empty
        List<Object> results = response.jsonPath().getList("result");
        AllureLogs.softAssertNotNull(softAssert, results, "Result List");
        AllureLogs.softAssertTrue(softAssert, results != null && !results.isEmpty(), "Result List Not Empty");
        System.out.println(results);

// Validate first item fields
        String actualName = response.jsonPath().getString("result[0].name");
        String actualCountry = response.jsonPath().getString("result[0].country");
        Boolean isActive = response.jsonPath().getBoolean("result[0].active");
        List<String> requestedDocs = response.jsonPath().getList("result[0].requestedDocuments");

// Use custom soft assertion methods
        AllureLogs.softAssertEquals(softAssert, actualName, name, "Loan Product Name");
        AllureLogs.softAssertEquals(softAssert, actualCountry, country, "Country");
        AllureLogs.softAssertNotNull(softAssert, isActive, "Product Active Status");
        AllureLogs.softAssertTrue(softAssert, isActive, "Product Should Be Active");
        AllureLogs.softAssertNotNull(softAssert, requestedDocs, "Requested Documents");
        AllureLogs.softAssertTrue(softAssert, requestedDocs != null && requestedDocs.size() > 0, "Requested Documents Not Empty");

// Validate specific document exists using custom contains method
        AllureLogs.softAssertContains(softAssert, requestedDocs, "Work ID", "Work ID in Requested Documents");

// Execute all soft assertions using custom method
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get all loan products")
    public Object[][] getLoanProductTestData() {
        return Postive_Data_Extractor.ExcelData("Get all loan products");
    }



    @Test(dataProvider = "Get Product Config")
    @Parameters({"productId"})
    public void getProductConfigTest(String productId) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken )
                .pathParam("productId", productId)
                .when()
                .get("/v1/origination/product/{productId}")
                .then()
                .log().all()
                .extract().response();

        System.out.println("API Response: " + response.asString());

        // Assert 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        // Validate the 'id' and 'name' fields
        String id = response.jsonPath().getString("id");
        String name = response.jsonPath().getString("name");
        String country = response.jsonPath().getString("country");
        Boolean active = response.jsonPath().getBoolean("active");

        softAssert.assertEquals(id, productId, "Product ID mismatch");
        softAssert.assertNotNull(name, "Name should not be null");
       // softAssert.assertEquals(country, "NG", "Country mismatch");
        softAssert.assertTrue(active, "Product should be active");

        List<String> documents = response.jsonPath().getList("requestedDocuments");


        softAssert.assertAll();
    }

    @DataProvider(name = "Get Product Config")
    public Object[][] getProductConfigTestDataFromExcel() {
        return Postive_Data_Extractor.ExcelData("Get Product Config");
    }

    @Test(dataProvider = "Get Loan Details")
    @Parameters({"loanId"})
    public void getLoanDetailsTest(String loanId) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .pathParam("loanId", loanId)
                .when()
                .get("/v1/origination/loans/{loanId}")
                .then()
                .log().all()
                .extract().response();

        System.out.println("API Response: " + response.asString());

        // Assert status 200
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected HTTP status code 200");

        // Basic field validations
        String id = response.jsonPath().getString("id");
        softAssert.assertEquals(id, loanId, "Loan ID mismatch");

        String status = response.jsonPath().getString("status");
        softAssert.assertNotNull(status, "Loan status should not be null");


        softAssert.assertAll();
    }

    @DataProvider(name = "Get Loan Details")
    public Object[][] getLoanDetailsTestDataFromExcel() {
        return Postive_Data_Extractor.ExcelData("Get Loan Details");
    }



    @Test(dataProvider = "Get Loan Documents Config")
    @Parameters({"loan_id"})
    public void getLoanDocumentsConfigTest(String loan_id) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .queryParam("loan_id",loan_id)
                .when()
                .get("/v1/origination/documents")
                .then()
                .log().all()
                .extract().response();
        System.out.println(response);
        // Status Code Assertion
        softAssert.assertEquals(response.statusCode(), 200, "Expected HTTP 200 OK");

        JsonPath json = response.jsonPath();

        // Validate root structure

        List<Map<String, Object>> documents = json.getList("documents");
        softAssert.assertNotNull(documents, "Expected 'documents' list in response");

        if (documents != null) {
            for (Map<String, Object> doc : documents) {
                // Null/Existence Checks
                softAssert.assertNotNull(doc.get("id"), "'id' should not be null");
                softAssert.assertNotNull(doc.get("name"), "'name' should not be null");
                softAssert.assertNotNull(doc.get("fileUrl"), "'fileUrl' should not be null");


                if (doc.containsKey("order") && doc.get("order") != null) {
                    Object orderObj = doc.get("order");
                    softAssert.assertTrue(orderObj instanceof Integer, "'order' should be an integer");

                }
            }
        }

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Loan Documents Config")
    public Object[][] getLoanDocumentsConfigData() {
        return Postive_Data_Extractor.ExcelData("Get Loan Documents Config");
    }



    @Test(dataProvider = "Get origination Loan Documents")
    @Parameters({"id"})
    public void getLoanDocumentsConfigV2Test(String id) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .pathParam("id", id)
                .when()
                .get("/v2/origination/loan/document-config/{id}")
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Expected HTTP 200 OK");

        JsonPath json = response.jsonPath();

        List<Map<String, Object>> documents = json.getList("documents");
        softAssert.assertNotNull(documents, "Expected 'documents' list in response");

        for (Map<String, Object> document : documents) {
            softAssert.assertNotNull(document.get("label"), "Document 'label' should not be null");
            softAssert.assertNotNull(document.get("name"), "Document 'name' should not be null");

            List<Map<String, Object>> fields = (List<Map<String, Object>>) document.get("fields");
            //softAssert.assertNotNull(fields, "'fields' list should not be null");

            for (Map<String, Object> field : fields) {
                // Basic Field Validations
                softAssert.assertNotNull(field.get("label"), "Field 'label' should not be null");
                softAssert.assertNotNull(field.get("name"), "Field 'name' should not be null");
                softAssert.assertNotNull(field.get("type"), "Field 'type' should not be null");

                // Type Validations
                softAssert.assertTrue(field.get("required") instanceof Boolean, "'required' should be boolean");
                softAssert.assertTrue(field.get("visibility") instanceof Boolean, "'visibility' should be boolean");

                // Optional fields validations
                if (field.containsKey("url")) softAssert.assertTrue(field.get("url") instanceof String, "'url' should be a string");
                if (field.containsKey("value")) softAssert.assertTrue(field.get("value") instanceof String, "'value' should be a string");
                if (field.containsKey("help_text")) softAssert.assertTrue(field.get("help_text") instanceof String, "'help_text' should be a string");
                if (field.containsKey("placeholder")) softAssert.assertTrue(field.get("placeholder") instanceof String, "'placeholder' should be a string");

                // Numeric constraints
                if (field.containsKey("min_length")) softAssert.assertTrue((int) field.get("min_length") >= 0, "min_length must be ≥ 0");
                if (field.containsKey("max_length")) softAssert.assertTrue((int) field.get("max_length") >= 0, "max_length must be ≥ 0");
                if (field.containsKey("min_value")) softAssert.assertTrue((int) field.get("min_value") >= 0, "min_value must be ≥ 0");
                if (field.containsKey("max_value")) softAssert.assertTrue((int) field.get("max_value") >= 0, "max_value must be ≥ 0");

                // Optional List Checks
                if (field.containsKey("options")) {
                    List<Object> options = (List<Object>) field.get("options");
                    softAssert.assertTrue(options instanceof List, "'options' must be a list");
                }

                // Final integrity check
               // softAssert.assertNotNull(field.get("id"), "Field 'id' should not be null");
            }
        }

        softAssert.assertAll();
    }

    @DataProvider(name = "Get origination Loan Documents")
    public Object[][] getLoanDocumentsConfigV2Data() {
        return Postive_Data_Extractor.ExcelData("Get origination Loan Documents");
    }


    @Test(dataProvider = "Get Status Trail")
    @Parameters({"id"})
    public void getStatusTrailTest(String id) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .pathParam("id", id)
                .when()
                .get("/v2/origination/{id}/status-trail")
                .then()
                .log().all()
                .extract().response();

        // Assert 200 OK
        softAssert.assertEquals(response.statusCode(), 200, "Expected HTTP 200 OK");

        JsonPath json = response.jsonPath();

        // Assert status trail array exists
        softAssert.assertTrue(json.getList("statusTrail") != null, "statusTrail should not be null");

        // Optional deep assertions
        List<Map<String, Object>> statusTrail = json.getList("statusTrail");

        if (statusTrail != null && !statusTrail.isEmpty()) {
            String status = json.getString("statusTrail[0].status");
            String createdBy = json.getString("statusTrail[0].created_by");
            String createdAt = json.getString("statusTrail[0].created_at");

            softAssert.assertNotNull(status, "Status should not be null");
            softAssert.assertNotNull(createdBy, "Created By should not be null");
            softAssert.assertNotNull(createdAt, "Created At should not be null");
            softAssert.assertTrue(createdAt.contains("T"), "created_at should be a datetime string");
        }
    }
        @DataProvider(name = "Get Status Trail")
    public Object[][] getStatusTrailData() {
        return Postive_Data_Extractor.ExcelData("Get Status Trail");
    }

    @Test(dataProvider = "Get Dealer Loans")
    @Parameters({"country", "user_id", "current_page", "page_size", "hasCommission", "order", "sort_by", "productName"})
    public void getDealerLoansTest(String country, String user_id, String current_page, String page_size,
                                   String hasCommission, String order, String sort_by, String productName) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .queryParam("country", country)
                .queryParam("user_id", user_id)
                .queryParam("current_page", current_page)
                .queryParam("page_size", page_size)
                .queryParam("hasCommission", hasCommission)
                .queryParam("order", order)
                .queryParam("sort_by", sort_by)
                .queryParam("productName", productName)
                .when()
                .get("/v1/origination/dig/loans")
                .then()
                .log().all()
                .extract().response();

        // Status Code Assertion
        softAssert.assertEquals(response.statusCode(), 200, "Expected status code 200 OK");

        JsonPath json = response.jsonPath();

        if (json.get("pagination") != null) {
            softAssert.assertEquals(json.getInt("pagination.currentPage"), Integer.parseInt(current_page), "Current page mismatch");
            softAssert.assertEquals(json.getInt("pagination.pageSize"), Integer.parseInt(page_size), "Page size mismatch");
        }

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Dealer Loans")
    public Object[][] getDealerLoansTestData() {
        return Postive_Data_Extractor.ExcelData("Get Dealer Loans");
    }

}
