package autochek_api_positive.OfferSimulation;

import Base.Initialization;
import Listener.AllureLogs;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OfferSimulationFinalOfferPositive {
    Initialization initialization = new Initialization();
    private static String token;
    private static String user_id;
    private static String AuthorizeHeader;
    private static String id;
    @BeforeClass
    public void setUp() {
        initialization.initializeEnvironmentUnderWriterUI(); // Sets base URI and tokens
        user_id = Initialization.UnderWriterUI.get("userId");
        token = Initialization.UnderWriterUI.get("token");
        AuthorizeHeader = Initialization.AuthorizeKey;
    }

    @Test
    @Description("Put Decision Engine - Assign Loan")
    @Story("Assign a loan to an assessor")
    @Severity(SeverityLevel.NORMAL)
    public void updateAssignLoan() throws InterruptedException {
        // ✅ Build request body using Map (cleaner than raw JSON string)
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("assessorEmail", "eshareddy@frugaltestingin.com");
        requestBody.put("loanId", "yMq1v8dr3");

        // ✅ API Call
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("x-api-key", "y53gD0eyhQuLbp9jfM376iLcGwQy6EU4KcjxGTgd")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/decision-engine/assign")
                .then()
                .log().all()
                .extract().response();

        // ✅ Assertions
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status Code Validation");

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @Test
    @Description("Put Decision Engine - Assign Loan")
    @Story("Assign a loan to an assessor")
    @Severity(SeverityLevel.NORMAL)
    public void GetPartnerBankDetails() {
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("x-api-key", "y53gD0eyhQuLbp9jfM376iLcGwQy6EU4KcjxGTgd")
                .contentType(ContentType.JSON)
                .when()
                .get("/decision-engine/offer-by-loan-id/yMq1v8dr3/CREDITCORPVFD")
                .then()
                .log().all()
                .extract().response();

        // ✅ Assertions
        SoftAssert softAssert = new SoftAssert();
         id = response.jsonPath().getString("id");
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status Code Validation");

        AllureLogs.executeSoftAssertAll(softAssert);
        System.out.println(id);
    }
    @Test(dependsOnMethods ="GetPartnerBankDetails" )
    @Description("Put Decision Engine - Assign Loan")
    @Story("Assign a loan to an assessor")
    @Severity(SeverityLevel.NORMAL)
    public void OfferID() {
        // ✅ API Call
        String requestBody="{\n" +
                "  \"averageMonthlySpendOnAirtimeAndData\": \"string\",\n" +
                "  \"averageMonthlySpendOnUtilities\": \"string\",\n" +
                "  \"averageMonthlySpendOnTransportation\": \"string\",\n" +
                "  \"averageMonthlySpendOnHealth\": \"string\",\n" +
                "  \"averageMonthlySpendOnHospitalityAndFood\": \"string\",\n" +
                "  \"averageMonthlySpendOnRent\": \"string\",\n" +
                "  \"averageMonthlySpendOnInsurance\": \"string\",\n" +
                "  \"averageMonthlySpendOnChargesAndStampDuty\": \"string\",\n" +
                "  \"averageMonthlyRecurringExpense\": \"string\",\n" +
                "  \"averageMonthlyTotalExpensesBusiness\": \"string\",\n" +
                "  \"averageMonthlySpendOnSavingsAndInvestmentsBusiness\": \"string\",\n" +
                "  \"incomeMonth1\": \"string\",\n" +
                "  \"incomeMonth2\": \"string\",\n" +
                "  \"incomeMonth3\": \"string\",\n" +
                "  \"incomeMonth4\": \"string\",\n" +
                "  \"incomeMonth5\": \"string\",\n" +
                "  \"incomeMonth6\": \"string\",\n" +
                "  \"incomeMonth1Business\": \"\",\n" +
                "  \"incomeMonth2Business\": \"10000000\",\n" +
                "  \"incomeMonth3Business\": \"10000000\",\n" +
                "  \"incomeMonth4Business\": \"10000000\",\n" +
                "  \"incomeMonth5Business\": \"10000000\",\n" +
                "  \"incomeMonth6Business\": \"10000000\",\n" +
                "  \"primaryBank\": \"string\",\n" +
                "  \"accountHolderName\": \"string\",\n" +
                "  \"accountType\": \"string\",\n" +
                "  \"accountNumber\": \"string\"\n" +
                "}";
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("x-api-key", "y53gD0eyhQuLbp9jfM376iLcGwQy6EU4KcjxGTgd")
                .contentType(ContentType.JSON)
                .when()
                .put("/decision-engine/periculum-override/"+id+"")  // ⚠️ Check spelling: is it `decision-engine` or `descision-engine`?
                .then()
                .log().all()
                .extract().response();

        // ✅ Assertions
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status Code Validation");

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    }