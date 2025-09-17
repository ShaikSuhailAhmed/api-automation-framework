package autochek_api_negative.DecisionEngine;

import Base.Initialization;
import Listener.AllureLogs;
import Utils.Negative_Data_Extractor;
import Utils.Postive_Data_Extractor;
import autochek_api_positive.OriginationService.OriginationServicePositive;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class DecisionEngineNegative {

    Initialization initialization = new Initialization();
    private static String token2;
    private static String user_id2;
    private static  String token3;
    private static String user_id;
    private static  Map<String, Object> partner_id=new HashMap<>();
    private static String loan_id;
    public  static String ByPassKey2;
    public static String underwriterURL="https://81l3dcfgqe.execute-api.us-east-1.amazonaws.com/stage";

    @BeforeClass
    public void setUp() {
        // Franchise Admin setup
        initialization.initializeEnvironment();

        user_id  = Initialization.franchiseAdminToken.get("userId");
        token2   = Initialization.franchiseAdminToken.get("token");
        ByPassKey2 = Initialization.ByPassKey;
        System.out.println("token2  "+token2);

        // Assign to OriginationServicePositive so other tests can use it
        OriginationServicePositive.token     = token2;
        System.out.println("token2(Org)  "+OriginationServicePositive.token  );
        OriginationServicePositive.ByPassKey = ByPassKey2;

        //  UnderWriter setup
        initialization.initializeEnvironmentUnderWriterUI();
        user_id2 = Initialization.UnderWriterUI.get("userId");
        token3   = Initialization.UnderWriterUI.get("token");
        System.out.println("token 3 "+token3);
    }
    public static String car_id;
    private static OriginationServicePositive os = new OriginationServicePositive();
    @Test
    @Description("Verify loan preparation helper methods")
    @Story("Validate Car Details, Update Loan and Get Partner Bank Details flow")
    @Severity(SeverityLevel.MINOR)
    public static void  verifyLoanFlowMethods() throws InterruptedException {

        // Step 1: Get Car Details
        car_id = os.getCarDetails();
        System.out.println(car_id);
        loan_id= os.postCreateLoan();
        System.out.println("Car ID from helper method: " + car_id);

        // Step 2: Update Loan
        String tempLoanId = os.putUpdateLoan();
        System.out.println("Loan ID from helper method: " + tempLoanId);

        //Step 3: Fetch Partner Bank Details
        partner_id = os.getLoanById();
        System.out.println(partner_id);
        // Soft assertions just to validate IDs are not null
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(!partner_id.isEmpty(), "Partner details should not be empty");
        softAssert.assertAll();
    }
    @Test(dataProvider = "Update Assign Loan")
    @Description("Put Decision Engine - Assign Loan")
    @Story("Assign a loan to an assessor")
    @Severity(SeverityLevel.NORMAL)
    @Parameters({"type","Severity","testDescription","Email","loanid","expectedStatucCode"})
    public void updateAssignLoan(String type,String Severity,String testDescription,String Email,String loanId,String expectedStatucCode) {

        // ✅ Build request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("assessorEmail", "Email");
        requestBody.put("loanId", "loanId");

        baseURI=underwriterURL;
        // ✅ API Call
        Response response = given()
                .header("Authorization", "Bearer " + token3)
                .header("x-api-key", "y53gD0eyhQuLbp9jfM376iLcGwQy6EU4KcjxGTgd")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/decision-engine/assign")
                .then()
                .log().all()
                .extract().response();
        System.out.println(token3);
        // ✅ Assertions
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 400, "Status Code Validation");
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Update Assign Loan")
    public Object[][] updateAssignLoan() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Update Assign Loan Negative");
    }
    @Test(dataProvider = "Partner Bank Details")
    @Description("Get Decision Engine - Periculum Override")
    @Story("Get Partner Bank Details")
    @Severity(SeverityLevel.NORMAL)
    @Parameters({"type","Severity","testDescription","partnerCode","loanid","expectedStatucCode"})
    public void GetPartnerBankDetails(String type,String Severity,String testDescription,String partnerCode,String loanId,String expectedStatucCode) throws InterruptedException {
        baseURI=underwriterURL;
        Response response = given()
                .header("Authorization", "Bearer " + token3)
                .header("x-api-key", "y53gD0eyhQuLbp9jfM376iLcGwQy6EU4KcjxGTgd")
                .contentType(ContentType.JSON)
                .when()
                .get("/decision-engine/offer-by-loan-id/"+partnerCode+"/"+loanId+"")
                .then()
                .log().all()
                .extract().response();

        // ✅ Assertions
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 400, "Status Code Validation");

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Partner Bank Details")
    public Object[][] GetPartnerBankDetails() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Partner Bank Details Negative");
    }

    //need to write negative testcases
    @Test(dataProvider = "Periculum Override")
    @Description("Put Decision Engine - Periculum Override")
    @Story("Periculum Override")
    @Severity(SeverityLevel.NORMAL)
    public void PericulumOverride(String type,String Severity,String testDescription,String offerID,String expectedStatucCode) {
        // ✅ Build request body using Map (cleaner than raw JSON string)

        // ✅ API Call
        baseURI=underwriterURL;
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
                .header("Authorization", "Bearer " + token3)
                .header("x-api-key", "y53gD0eyhQuLbp9jfM376iLcGwQy6EU4KcjxGTgd")
                .contentType(ContentType.JSON)
                .when()
                .put("/decision-engine/periculum-override/"+offerID+"")  // ⚠️ Check spelling: is it `decision-engine` or `descision-engine`?
                .then()
                .log().all()
                .extract().response();

        // ✅ Assertions
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 400, "Status Code Validation");

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Periculum Override")
    public Object[][] PericulumOverride() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Periculum Override Negative");
    }
    @Test(dataProvider = "Validation Review")
    @Description("Put Decision Engine -validation-review")
    @Story("Updating validation-review")
    @Severity(SeverityLevel.NORMAL)
    public void ValidationReview(String type,String Severity,String testDescription,String OfferID,String expectedStatucCode) throws InterruptedException {
        baseURI=underwriterURL;
        Response response = given()
                .header("Authorization", "Bearer " + token3)
                .header("x-api-key", "y53gD0eyhQuLbp9jfM376iLcGwQy6EU4KcjxGTgd")
                .contentType(ContentType.JSON)
                .when()
                .put("/decision-engine/validation-review/"+OfferID+"")
                .then()
                .log().all()
                .extract().response();

        // ✅ Assertions
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(),400 , "Status Code Validation");
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Validation Review")
    public Object[][] ValidationReview() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Validation Review Negative");
    }

    @Test(dataProvider = "Submit Offer Simulation")
    @Description("POST  Decision Engine - SubmitOfferSimulation Negative")
    @Story("SubmitOfferSimulation")
    @Severity(SeverityLevel.NORMAL)
    @Parameters({"type","Severity","testDescription","OfferID","expectedStatusCode"})
    public void SubmitOfferSimulation(String type,String Severity,String testDescription,String OfferID,String expectedStatucCode) throws InterruptedException {
        baseURI=underwriterURL;
        Response response = given()
                .header("Authorization", "Bearer " + token3)
                .header("x-api-key", "y53gD0eyhQuLbp9jfM376iLcGwQy6EU4KcjxGTgd")
                .contentType(ContentType.JSON)
                .when()
                .post("/decision-engine/offers/"+OfferID+"/submit-simulated-offer")
                .then()
                .log().all()
                .extract().response();

        // ✅ Assertions
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 400, "Status Code Validation");

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Submit Offer Simulation")
    public Object[][] SubmitOfferSimulation() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Submit OfferSimulation Negative");
    }

}

