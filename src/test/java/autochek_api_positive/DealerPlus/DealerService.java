package autochek_api_positive.DealerPlus;

import Base.Initialization;
import Utils.Postive_Data_Extractor;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class DealerService {

    Initialization initialization = new Initialization();

    @BeforeClass
    public void setUp() {
        initialization.initializeEnvironment(); // Sets base URI
    }



    @Test(dataProvider = "Dealer Notifications")
    @Parameters({"dealer_id", "page_size", "current_page"})
    public void dealerNotificationsTest(String dealer_id, String page_size, String current_page) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .queryParam("page_size", page_size)
                .queryParam("current_page", current_page)
                .when()
                .get("/v1/dealer/" + dealer_id + "/notifications")
                .then()
                .log().all()
                .extract().response();

        System.out.println("API Response: " + response.asString());

        // Assert status
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        JsonPath json = response.jsonPath();
        // Response content validations
        List<?> data = response.jsonPath().getList("notifications");
        if (data != null) {
            System.out.println("Size: " + data.size());
        } else {
            System.out.println("List is null. Check if 'result' exists in the response.");
        }
        List<Map<String, Object>> notifications = json.getList("notifications");
        softAssert.assertNotNull(notifications, "'notifications' should not be null");
        softAssert.assertTrue(notifications.size() > 0, "'notifications' should not be empty");

        for (Map<String, Object> notification : notifications) {
            softAssert.assertNotNull(notification.get("id"), "'id' should not be null");
            softAssert.assertEquals(notification.get("dealerId"), "OLf1NYnD3", "'dealerId' mismatch");
            softAssert.assertNotNull(notification.get("message"), "'message' should not be null");
            softAssert.assertTrue(notification.containsKey("isRead"), "'isRead' field should be present");
            softAssert.assertTrue(notification.containsKey("createdAt"), "'createdAt' should be present");
        }

        // Validate pagination
        Map<String, Object> pagination = json.getMap("pagination");
        softAssert.assertNotNull(pagination, "'pagination' object should not be null");
        softAssert.assertEquals(pagination.get("currentPage"), 1, "'currentPage' should be 1");
        softAssert.assertEquals(pagination.get("pageSize"), 5, "'pageSize' should be 5");
        softAssert.assertEquals(pagination.get("pageNumber"), 1, "'pageNumber' should be 1");
        softAssert.assertTrue((int) pagination.get("total") >= notifications.size(), "'total' should be >= notifications count");


        softAssert.assertAll();
    }

    @DataProvider(name = "Dealer Notifications")
    public Object[][] getDealerNotificationsDataFromExcel() {
        return Postive_Data_Extractor.ExcelData("Dealer Notifications");
    }




    @Test(dataProvider = "Loan Statistics")
    @Parameters({"user_id", "current_page", "page_size", "hasCommission", "order", "sort_by", "productName"})
    public void dealerLoanStatisticsTest(String user_id, String current_page, String page_size,
                                         String hasCommission, String order, String sort_by, String productName) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .queryParam("user_id", user_id)
                .queryParam("current_page", current_page)
                .queryParam("page_size", page_size)
                .queryParam("hasCommission", hasCommission)
                .queryParam("order", order)
                .queryParam("sort_by", sort_by)
                .queryParam("productName", productName)
                .when()
                .get("/v1/origination/dig/loans/statistics")
                .then()
                .log().all()
                .extract().response();

        System.out.println("API Response: " + response.asString());

        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        JsonPath json = response.jsonPath();

        // top-level fields
        Map<String, Object> totalSales = json.getMap("totalSales");
        Map<String, Object> totalLoansCreated = json.getMap("totalLoansCreated");
        Map<String, Object> totalLoansWithoutDocuments = json.getMap("totalLoansWithoutDocuments");
        Map<String, Object> pagination = json.getMap("pagination");

        softAssert.assertNotNull(totalSales, "'totalSales' should not be null");
        softAssert.assertTrue(totalSales.containsKey("number"), "'totalSales.number' should be present");
        softAssert.assertTrue(totalSales.containsKey("value"), "'totalSales.value' should be present");


        softAssert.assertNotNull(totalLoansWithoutDocuments, "'totalLoansWithoutDocuments' should not be null");
        softAssert.assertTrue(totalLoansWithoutDocuments.containsKey("number"), "'totalLoansWithoutDocuments.number' should be present");


        softAssert.assertAll();
    }

    @DataProvider(name = "Loan Statistics")
    public Object[][] getLoanStatisticsDataFromExcel() {
        return Postive_Data_Extractor.ExcelData("Loan Statistics");
    }


    @Test(dataProvider = "Dealer Loan Tier")
    @Parameters({"franchiseId"})
    public void dealerLoanTierDetailsTest(String franchiseId) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .when()
                .get("/v1/dealer/" + franchiseId)
                .then()
                .log().all()
                .extract().response();

        System.out.println("API Response: " + response.asString());

        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        JsonPath json = response.jsonPath();

        // Validate top-level values
        String id = json.getString("results[0].id");
        softAssert.assertNotNull(id, "ID should not be null");

        String status = json.getString("results[0].status");
        softAssert.assertNotNull(status, "Status should not be null");

        // Validate utility object
        Integer limitValue = json.getInt("results[0].limitValue");
        softAssert.assertNotNull(limitValue, "limitValue should not be null");

        String dealerId = json.getString("results[0].dealerId");
        softAssert.assertEquals(dealerId, franchiseId, "Dealer ID mismatch");

        // Validate customer profile fields
        String firstName = json.getString("results[0].customer.profile.find { it.name == 'firstName' }.value");
        softAssert.assertEquals(firstName, "KE", "firstName mismatch");

        String email = json.getString("results[0].customer.profile.find { it.name == 'email' }.value");
        softAssert.assertTrue(email.contains("@"), "Invalid email format");
        softAssert.assertAll();
    }

    @DataProvider(name = "Dealer Loan Tier")
    public Object[][] getDealerLoanTierDataFromExcel() {
        return Postive_Data_Extractor.ExcelData("Dealer Loan Tier");
    }
    
}
