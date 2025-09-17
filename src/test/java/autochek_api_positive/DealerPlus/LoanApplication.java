package autochek_api_positive.DealerPlus;
import java.io.File;
import java.util.Random;
import java.util.UUID;

import Base.Initialization;
import Utils.Postive_Data_Extractor;
import com.google.gson.internal.bind.util.ISO8601Utils;
import groovyjarjarasm.asm.tree.IntInsnNode;
import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import Listener.AllureLogs;

import javax.lang.model.element.NestingKind;
import java.util.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
@Epic("API Automation")
@Feature("Dealer Plus")
@Story("Loan Application Positive Flow")
public class LoanApplication {
    Initialization initialization = new Initialization();
    private static final String[] Countries = {"KE", "NG"};
    private static final Random random = new Random();
    private static final String CountryCode = "NG";// Countries[random.nextInt(Countries.length)];
    private static final String product_id = "hj43BXLXe";
    private static String car_id;
    private static String user_id;
    private static String loan_id;
    private static String token;
    private static float income;
    private static String inspector_token;
    private static Map<String, Object> car;
    private static Map<String, Object> danielInspector = null;
    private static String  InspectionId;
    @BeforeClass
    public void setUp() {
        initialization.initializeEnvironment();// Sets base URI
        user_id = Initialization.franchiseAdminToken.get("userId");
        token = Initialization.franchiseAdminToken.get("token");
        //inspector_token = Initialization.InspectorAppToken.get("token");
    }


//    @Test(dataProvider = "Get_Banks")
//    @Epic("API Automation")
//    @Feature("Negative")
//    @Story("Dealer Plus")
//    @Severity(SeverityLevel.NORMAL)
//    @Description("Loan Application - Get Bank API")


    public void getBanksList(String S_No,String Type,String Severity,String Test_Description,String Country, String Expected_Status){



        Response response = given()
                .header("Authorization", "Bearer " +Initialization.franchiseAdminToken.get("token"))
                .queryParam("country",Country)
                .when()
                .get("/v1/origination/banks")
                .then()
                .log().all()
                .extract().response();
        System.out.println("GET Bank API Response: "+response.asString());



        JsonPath json = response.jsonPath();
        // Response content validations
        List<?> data = response.jsonPath().getList("banks");
        if (data != null) {
            System.out.println("Size: " + data.size());
        } else {
            System.out.println("List is null. Check if 'result' exists in the response.");
        }
        // Create SoftAssert instance for banks validation
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), Expected_Status, "Expected status code"+Expected_Status);
        List<Map<String, Object>> banks = json.getList("banks");
        // Validate banks list
        if(Objects.equals(Type, "Negative")){
            AllureLogs.softAssertEquals(softAssert, banks.size(), 0,  " Bank Array Size Should be Zero");
        }
        else{

            AllureLogs.softAssertNotNull(softAssert, banks, "Banks List");
            AllureLogs.softAssertTrue(softAssert, banks != null && !banks.isEmpty(), "Banks List Not Empty");

            for (Map<String, Object> bank : banks) {
                softAssert.assertNotNull(bank.get("id"), "'id' should not be null");
                softAssert.assertNotNull(bank.get("name"), "'name' should not be null");
                if(bank.containsKey("logo")){
                    softAssert.assertNotNull(bank.get("logo"), "'logo' should not be null");
                }
                softAssert.assertTrue(bank.containsKey("country"), "'country' field should be present");
                softAssert.assertTrue(bank.containsKey("active"), "'active' field should be present");

                // Additional validations you might want to add:
                softAssert.assertEquals(bank.get("country"), Country,
                        "Bank country should match the query parameter: " + Country);
            }
            AllureLogs.executeSoftAssertAll(softAssert);
            softAssert.assertAll();
        }

        //Validate each bank in the list
//        for (int i = 0; i < (banks != null ? banks.size() : 0); i++) {
//            Map<String, Object> bank = banks.get(i);
//            String bankIndex = "Bank[" + i + "]";
//
//            // Validate required fields
//            AllureLogs.softAssertNotNull(softAssert, bank.get("id"), bankIndex + " ID");
//            AllureLogs.softAssertNotNull(softAssert, bank.get("name"), bankIndex + " Name");
//
//            // Conditional validation for logo
//            if (bank.containsKey("logo")) {
//                AllureLogs.softAssertNotNull(softAssert, bank.get("logo"), bankIndex + " Logo");
//            }
//
//            // Validate field presence
//            AllureLogs.softAssertTrue(softAssert, bank.containsKey("country"), bankIndex + " Country Field Present");
//            AllureLogs.softAssertTrue(softAssert, bank.containsKey("active"), bankIndex + " Active Field Present");
//
//            // Validate country matches query parameter
//            AllureLogs.softAssertEquals(softAssert, bank.get("country"), CountryCode, bankIndex + " Country Match");
//        }
//
//// Execute all soft assertions for banks validation
//        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @DataProvider(name = "Get_Banks")
    public Object[][] getDealerNotificationsDataFromExcel() {
        return Postive_Data_Extractor.ExcelData("Get_Banks");
    }

    //@Test(priority = 2)
    public void getSourceList(){
        SoftAssert softAssert = new SoftAssert();
        String CountryCode = Countries[random.nextInt(Countries.length)];
        Response response = given()
                .header("Authorization", "Bearer " +Initialization.franchiseAdminToken)
                .queryParam("country",CountryCode)
                .when()
                .get("/v1/origination/sources")
                .then()
                .log().all()
                .extract().response();
        System.out.println("GET Bank API Response: "+response.asString());

        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        JsonPath json = response.jsonPath();
        // Response content validations
        List<?> data = response.jsonPath().getList("sources");
        if (data != null) {
            System.out.println("Size: " + data.size());
        } else {
            System.out.println("List is null. Check if 'result' exists in the response.");
        }
        List<Map<String, Object>> sources = json.getList("sources");
        softAssert.assertNotNull(sources, "'sources' should not be null");
        softAssert.assertTrue(!sources.isEmpty(), "'sources' should not be empty");

        for (Map<String, Object> item : sources) {
            softAssert.assertNotNull(item.get("id"), "'id' should not be null");
            softAssert.assertNotNull(item.get("name"), "'name' should not be null");

            softAssert.assertNotNull(item.get("label"), "'label' should not be null");
            softAssert.assertTrue(item.containsKey("country"), "'country' field should be present");


            // Additional validations you might want to add:
            softAssert.assertEquals(item.get("country"), CountryCode,
                    "Bank country should match the query parameter: " + CountryCode);
        }
        softAssert.assertAll();
    }

    @Test(priority = 1)
    public void getCarDetails(){
        System.out.println("Token_Franchise: "+token);
        Response response = given().
                header("Authorization","Bearer "+token)
                .queryParam("exclude_sold_cars",true)
                .queryParam("exclude_category_name","logbook")
                .queryParam("page_number",1)
                .queryParam("franchise","kpMngdaMZ")
                .queryParam("country",CountryCode)
                .queryParam("franchise_id","kpMngdaMZ")
                .when()
                .get("/v1/inventory/car")
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status code");
        car_id= response.jsonPath().get("carList[2]");
       // car_id = response.jsonPath().getString("carList[3].id");
        income = Float.parseFloat(response.jsonPath().getString("carList[3].installment"))*3;
        System.out.println("car_id: "+car_id);
        System.out.println("income: "+income);
        AllureLogs.executeSoftAssertAll(softAssert);
    }

String car_id1="0zV7bPoNG";
    public static String getRandomFirstName() {
        return "API" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String getRandomLastName() {
        return "Testing" + UUID.randomUUID().toString().substring(0, 8);
    }
    private static final Set<String> usedNumbers = new HashSet<>();
    public static String getRandomPhoneNumber() {
        Random rand = new Random();
        // Nigerian number format: 23480XXXXXXX -> 234 + 80X + 7-digit number
        int operatorDigit = rand.nextInt(10); // 0-9 for the third digit
        int subscriberNumber = 10000002 + rand.nextInt(9000000); // 7-digit number
        return "234" + operatorDigit + subscriberNumber;
    }

    private static String generateUnique11DigitNumber() {
        String number;
        do {
            long num = 10000000000L + (long) (random.nextDouble() * 89999999999L);
            number = String.valueOf(num);
        } while (usedNumbers.contains(number));
        usedNumbers.add(number);
        return number;
    }
    public static String getRandomEmail() {
        return "API" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
    }

    public static String getRandomGender() {
        return (new Random().nextBoolean()) ? "Male" : "Female";
    }
    public static String getBvnNumber() {
        return generateUnique11DigitNumber();
    }

    public static String getNinNumber() {
        return generateUnique11DigitNumber();
    }
    String firstName = getRandomFirstName();
    String lastName = getRandomLastName();
    String phoneNumber = getRandomPhoneNumber();
    String Email = getRandomEmail();
    String Gender = getRandomGender();
    String bvnNumber = getBvnNumber();
    String ninNumber = getNinNumber();

    @Test()
    public void postCreateLoan(){
        String requestBody = "{\n" +
                "    \"profile\": [\n" +
                "        {\n" +
                "            \"name\": \"sourceDataId\",\n" +
                "            \"value\": \"kpMngdaMZ\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"sourceData\",\n" +
                "            \"value\": \"100% Automobile Nig.\",\n" +
                "            \"type\": \"email\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"source\",\n" +
                "            \"value\": \"FRANCHISE\",\n" +
                "            \"type\": \"selection\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"referrerSource\",\n" +
                "            \"value\": \"Franchise\",\n" +
                "            \"type\": \"selection\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"referrerData\",\n" +
                "            \"value\": \"100% Automobile Nig.\",\n" +
                "            \"type\": \"selection\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"referrerDataId\",\n" +
                "            \"value\": \"kpMngdaMZ\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"firstName\",\n" +
                "            \"value\": \"test\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"label\": \"First name\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"lastName\",\n" +
                "            \"value\": \"test\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"label\": \"Last name\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"telephone\",\n" +
                "            \"value\": \"2348956231578\",\n" +
                "            \"type\": \"tel\",\n" +
                "            \"label\": \"Phone number\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"email\",\n" +
                "            \"value\": \"test564588@gmail.com\",\n" +
                "            \"type\": \"email\",\n" +
                "            \"label\": \"Email address\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"description\",\n" +
                "            \"value\": \"Salary earner\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"label\": \"What best describes you\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"monthlyGrossIncome\",\n" +
                "            \"value\": \"1000000\",\n" +
                "            \"type\": \"amount\",\n" +
                "            \"label\": \"Total Take-Home Pay (after salary deductions) (₦)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"sourceOfFunds\",\n" +
                "            \"value\": \"Interest from personal savings\",\n" +
                "            \"label\": \"Source of Funds\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"dob\",\n" +
                "            \"value\": \"2000-06-14\",\n" +
                "            \"type\": \"date\",\n" +
                "            \"label\": \"Date of birth\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"nin\",\n" +
                "            \"value\": \"66665766655\",\n" +
                "            \"type\": \"number\",\n" +
                "            \"label\": \"National Identification Number (NIN)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"BVN\",\n" +
                "            \"value\": \"32655675666\",\n" +
                "            \"type\": \"number\",\n" +
                "            \"label\": \"Your BVN\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"gender\",\n" +
                "            \"value\": \"Male\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"label\": \"Gender\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"nationality\",\n" +
                "            \"value\": \"Algeria\",\n" +
                "            \"label\": \"Nationality\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"nationalityStatus\",\n" +
                "            \"value\": \"Citizen\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"label\": \"What is your nationality status\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"state\",\n" +
                "            \"value\": \"Abia \",\n" +
                "            \"label\": \"State of residence\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"employmentType\",\n" +
                "            \"value\": \"Permanent\",\n" +
                "            \"label\": \"Employment type\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"industry\",\n" +
                "            \"value\": \"Consultancy - HR\",\n" +
                "            \"label\": \"Industry\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"employerName\",\n" +
                "            \"value\": \"test\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"label\": \"Name of employer\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"employerAddress\",\n" +
                "            \"value\": \"test\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"label\": \"Employer address\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"yearWithEmployer\",\n" +
                "            \"value\": \"5\",\n" +
                "            \"type\": \"number\",\n" +
                "            \"label\": \"Year(s) with employer\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"sideBusiness\",\n" +
                "            \"value\": \"No\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"label\": \"Do you have a side business?\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"primaryBank\",\n" +
                "            \"value\": \"Covenant Microfinance Bank Ltd\",\n" +
                "            \"label\": \"Your bank\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"monthlyExpenses\",\n" +
                "            \"value\": \"100000\",\n" +
                "            \"type\": \"amount\",\n" +
                "            \"label\": \"Total monthly expenses (₦)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"monthlyGrossIncome2\",\n" +
                "            \"value\": \"1000000\",\n" +
                "            \"type\": \"amount\",\n" +
                "            \"label\": \"Total Take-Home Pay (after deductions) (₦)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"vehicleUse\",\n" +
                "            \"value\": \"Business\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"label\": \"What will the vehicle be used for?\",\n" +
                "            \"valid\": true\n" +
                "        }\n" +
                "    ],\n" +
                "    \"loan_fields\": [\n" +
                "        {\n" +
                "            \"name\": \"desiredLoanCurrency\",\n" +
                "            \"value\": \"USD\",\n" +
                "            \"label\": \"Desired Loan Currency\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"interestRateType\",\n" +
                "            \"value\": \"Floating/Variable\",\n" +
                "            \"type\": \"selection\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"residualPercentage\",\n" +
                "            \"value\": \"0%\",\n" +
                "            \"type\": \"selection\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"includeRoadworthinessFees\",\n" +
                "            \"value\": \"Yes\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"includeLicenceRenewalFees\",\n" +
                "            \"value\": \"Yes\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"feePaymentMethod\",\n" +
                "            \"value\": \"Upfront\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontVehicleRegistration\",\n" +
                "            \"value\": false,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontInsurance\",\n" +
                "            \"value\": true,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"label\": \"Insurance (First 12 months)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontVehicleTracking\",\n" +
                "            \"value\": true,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"label\": \"Vehicle Tracking\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontMaintenancePlan\",\n" +
                "            \"value\": false,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontCreditLifeInsurance\",\n" +
                "            \"value\": false,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontWarranty\",\n" +
                "            \"value\": false,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontTyres\",\n" +
                "            \"value\": true,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"label\": \"Tyres\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"vehicleDiscount\",\n" +
                "            \"value\": \"1%\",\n" +
                "            \"label\": \"Total Discount %\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"dealDiscount\",\n" +
                "            \"value\": \"2%\",\n" +
                "            \"label\": \"Discount % Applied to the Deal (the discount that will be used to reduce the client rate)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"equityContribution\",\n" +
                "            \"value\": \"4665665\",\n" +
                "            \"type\": \"amount\",\n" +
                "            \"label\": \"Desired equity contribution (₦)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"creditCheckConsent\",\n" +
                "            \"value\": \"Yes\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"label\": \"I hereby consent that Autochek is allowed to make enquiries and access my credit information regarding my credit history with any credit bureau. I also consent to Autochek sharing my credit information with their banking partners as required by law in order to finalise or fulfil my loan agreement as part of this application. \\nI consent that Autochek reports the conclusion of any credit agreement with me to the relevant credit reporting regulator. I hereby declare that the information provided by me is true and correct.\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"financeAndVehicleConsent\",\n" +
                "            \"value\": \"Yes\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"label\": \"Finance is subject to an approval based on your credit profile and affordability of the vehicle. The submission of this finance application will not result in the immediate reservation of this vehicle, nor will it guarantee the availability of this vehicle in the future.\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"interestRate\",\n" +
                "            \"value\": \"28\",\n" +
                "            \"label\": \"Desired interest rate (%)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"term\",\n" +
                "            \"value\": \"12 months\",\n" +
                "            \"label\": \"Desired loan term\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"monthlyPayment\",\n" +
                "            \"value\": \"6655545\",\n" +
                "            \"type\": \"amount\",\n" +
                "            \"label\": \"Desired monthly payment (₦)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"platformPolicy\",\n" +
                "            \"value\": \"Yes\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"label\": \"I agree to the: \\n (a) terms and Conditions of the platform (https://autochek.africa/ng/terms-of-service) and the \\n (b) privacy policy of the platform (https://autochek.africa/ng/privacy-policy)\",\n" +
                "            \"valid\": true\n" +
                "        }\n" +
                "    ],\n" +
                "    \"car_id\": \""+car_id+"\",\n" +
                "    \"user_id\": \"BQlQBbLTW\",\n" +
                "    \"product_id\": \"hj43BXLXe\",\n" +
                "    \"country\": \"NG\",\n" +
                "    \"is_draft\": false\n" +
                "}";

        Response response = given()
                .header("Content-Type","application/json")
                .header("Authorization","Bearer "+token)
                .header("x-autochek-app","marketplace_web")
                .body(requestBody)
                .when()
                .post("/v2/origination/loan")
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status code");

        loan_id = response.jsonPath().getString("id");
        System.out.println("loan id: "+loan_id);
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @Test()
    public void putUpdateLoan(){

        String requestBody = "{\n" +
                "    \"profile\": [\n" +
                "        {\n" +
                "            \"name\": \"sourceDataId\",\n" +
                "            \"value\": \"kpMngdaMZ\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"sourceData\",\n" +
                "            \"value\": \"100% Automobile Nig.\",\n" +
                "            \"type\": \"email\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"source\",\n" +
                "            \"value\": \"FRANCHISE\",\n" +
                "            \"type\": \"selection\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"referrerSource\",\n" +
                "            \"value\": \"Franchise\",\n" +
                "            \"type\": \"selection\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"referrerData\",\n" +
                "            \"value\": \"100% Automobile Nig.\",\n" +
                "            \"type\": \"selection\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"referrerDataId\",\n" +
                "            \"value\": \"kpMngdaMZ\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"firstName\",\n" +
                "            \"value\": \"test\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"label\": \"First name\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"lastName\",\n" +
                "            \"value\": \"test\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"label\": \"Last name\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"telephone\",\n" +
                "            \"value\": \"2349256555666\",\n" +
                "            \"type\": \"tel\",\n" +
                "            \"label\": \"Phone number\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"email\",\n" +
                "            \"value\": \"test8907@gmail.com\",\n" +
                "            \"type\": \"email\",\n" +
                "            \"label\": \"Email address\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"description\",\n" +
                "            \"value\": \"Salary earner\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"label\": \"What best describes you\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"monthlyGrossIncome\",\n" +
                "            \"value\": \"1000000000\",\n" +
                "            \"type\": \"amount\",\n" +
                "            \"label\": \"Total Take-Home Pay (after salary deductions) (₦)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"sourceOfFunds\",\n" +
                "            \"value\": \"Wages, bonuses, dividends, and other income from employment\",\n" +
                "            \"label\": \"Source of Funds\",\n" +
                "            \"valid\": true\n" +
                "        }\n" +
                "    ],\n" +
                "    \"loan_fields\": [\n" +
                "        {\n" +
                "            \"name\": \"desiredLoanCurrency\",\n" +
                "            \"value\": \"NGN\",\n" +
                "            \"type\": \"selection\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"interestRateType\",\n" +
                "            \"value\": \"Floating/Variable\",\n" +
                "            \"type\": \"selection\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"residualPercentage\",\n" +
                "            \"value\": \"0%\",\n" +
                "            \"type\": \"selection\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"includeRoadworthinessFees\",\n" +
                "            \"value\": \"Yes\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"includeLicenceRenewalFees\",\n" +
                "            \"value\": \"Yes\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"feePaymentMethod\",\n" +
                "            \"value\": \"Upfront\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontVehicleRegistration\",\n" +
                "            \"value\": false,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontInsurance\",\n" +
                "            \"value\": false,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontVehicleTracking\",\n" +
                "            \"value\": false,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontMaintenancePlan\",\n" +
                "            \"value\": false,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontCreditLifeInsurance\",\n" +
                "            \"value\": false,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontWarranty\",\n" +
                "            \"value\": false,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"upfrontTyres\",\n" +
                "            \"value\": false,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"valid\": true\n" +
                "        }\n" +
                "    ],\n" +
                "    \"car_id\": \""+car_id+"\",\n" +
                "    \"user_id\": \"BQlQBbLTW\",\n" +
                "    \"product_id\": \"hj43BXLXe\",\n" +
                "    \"country\": \"NG\",\n" +
                "    \"is_draft\": true,\n" +
               // "    \"captcha_token\": \"03AFcWeA6XJpOLKvZBm1KtXfEixPQx3Pn_H1wMUMKpMVTCWhxh6SdlZdZWbe9s7ZTchg2C2ufg_R-TX5APNzFKElAc2MZ3vKhOXxmu8QeQD5XGRrOLFlHf7-644CzdVaVtRnOlV3UbPvx3ZOc73S5S9ScKCnM7lnqdi3B4A7O8yYWq5LPfrYIXYZA0RLrpCRiZvSTEmDWLWiBPO3cscsPR3t6g1x_OD__htf1154501FdcvZxOsIABDw-ApLoFSNEn86kc6vak6zzSPMbJXG_0dcJtdHTI52yaeeNNQneaFADvRIDSBeWBzvL7MVcovbDWQQU7NoqwNqjjwv31Mg1KJgFfooiG5BoShhnOKJ_rDDW3SU-SWZR2096CI5txQh_xHvGnywIkt9gqp3I3HVL7EpYCI0FWuDsrZl-zALG2M79hGhh35YbKu5WKQAAzOl9WVxM1iAR_1_KgY-Z1qs8Z9bgj4g1pWNDRyFdMayWw-R15ehUZS4mwzk6RWvV_DMHX69ySznla-MLtmsWR3CrMKDscL46MUTF3COMR-ZzIs9bPvVxSraho9AFfoHJXd5jWKFONiCJMFGR1hux9hl4ZCDDzz0Q0wHeNaJafeVG7m3TB3CHtmTU_l64TdAnuV4_ZHFvE0xZDUUEH2L00qk7G5BHVVQChTb8xwZZQbL8VKzd_Ou7YbR6uzaY6ryiwhQ6pnfJEQlxgDz8rXjmNNA_pFV-Z5Zt9FzMfynv5M7TArX6TFHgRQgvI00u0fHukvqI14wWpvsOY4CXeq9OQ7XHMegOdagwAy34KJLA6jAgcWtvbJzpVAX8bVaNetnw0ApFm7LDk-c5E3yJ_mQ0Wnb0lTtPYHfUSXqVycrcplUbGE0D8fuVe_diruBr3a9S7mL6jkojIK3Lt5EG-pBiTokyp85IWpRKSIJirnFWwdRTbiBOhvnhb1ZHB6vpXvE8LdvcXt6Xuen6idRiCKh6CLEg9dQZ3Ufy4Uo7Fccmpz5SdAkIv4JjSU5HCvmw1iSZH9wOGZScdYK_HkYB6eDq4iCrpqAy9Snn5wq9_GqmUudtQ5dZBVJj6936csUTBPSqeFMLp2sxdS0a5becl5eWDmIityUb37yJ7vd9ZTWiVZQTOHXdEgmfbAIFp7c2jqvczYghODliNoizCJjx96wSPEreTaEGlYX5jsifQmlpYppr7daS6zUo7kaH6IQK4Ifja495OcH90Dbtv-IEhzqA0PrAh-jRJFFRnVodA1KJvkLLTU06gtB85_ZM4hhrwetYKFpJMedXmu6gwdhYmo31sniSEMFbh-wccSn7hmypBSE8tlLs4uCtLiDorSIjJJTSfdfa9yKTI_h54lapx1zjXU-RiHpSn6LTem2KnvjiHUoOvQ1FkjUPnhhH3aFA8sybMIORX2FEiTkGBSq3OvyDhLPJEN1PXZv58I0vCODzxyUh_kTo5I7avYfIIEn1qN1EorjCT3CkqYp7xlblx90Q_h8MpDGk-f252A3kN01rx383QY-MvpbJXCRVxjZZsUgcUG-VIxZK8U6bsqj4eNeJzXAyTx0PlvcrYkd44Zb7XHGakJO1HFRinvZyCfAY66SBEnw7YVjJOeB12TNPsB2ikJz3hLg_ZUShN61kb2IDnaFEzgC7csGVckzodH45WyG-scVpv1fZsHJxGgVUtmZg1cctZa_qQzj0XclHtG_Z3FGNfTWGRVo8JjOGdGmLNdSZ43ZQxbJCn64hbtqrVqn4Rga30s8V9gGKN85SX9OedNqH-EJ_M_udZXgjxFw_-api-zYEeqztM4w4uC-225XsMmoKl_DB6rIeqVp6UO3Avgtxxjgq1-oj32-Kt8TlCNQF0xwJOZfU2GcKb6ITCaJxEWoxrI9KwQTF8AcafYLfAZxEDQQienrt4aa8oon_uOTbCUBlQFG2GZo7_23DKj6fxgyh1dWbCkG65elrLtEO4Wv9wvh4Lhu58JXu8U9QZQZD66tM9s6ZXmMD6a1iZu1vZhuMF9csdmPW1xDuQ__OUIv-2skmgaF8SgjdkD0DQeA78ndpQ0ZtWuOkHPS0QJ3tNkdogIsxzmdetuwfiTTuj8Fpq35uS5T_JYK8u-LY\"\n" +
                "}\n";

        Response response = given()
                .header("Content-Type","application/json")
                .header("Authorization","Bearer "+token)
                .header("x-autochek-app","marketplace_web")
                .body(requestBody)
                .when()
                .put("/v2/origination/loan/"+loan_id)
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status code");
        System.out.println("loan id: "+response.jsonPath().getString("id"));
        AllureLogs.executeSoftAssertAll(softAssert);

    }

    @Test(priority = 4)
    public void getLoanById(){
        Response response = given()
                .header("Authorization","Bearer "+token)
                .when()
                .get("/v1/origination/loans/"+loan_id)
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"Expected Status : 200");
        String get_response_loan_id = response.jsonPath().getString("id");
        AllureLogs.softAssertEquals(softAssert,get_response_loan_id,loan_id,"loan_id");
        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @Test(priority = 5)
    public void getLoanStatus(){
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/v1/origination/loans/"+loan_id)
                .then()
                .log().all()
                .extract().response();
        System.out.println(loan_id);
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"Expected Status : 200");
        String loan_status = response.jsonPath().getString("status");
        AllureLogs.softAssertEquals(softAssert,loan_status,"APPLICATION_COMPLETED","status");

        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @Test(priority = 6,dependsOnMethods = "getLoanStatus")
    public void getLoanDocumentsConfigTest() {
        SoftAssert softAssert = new SoftAssert();
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("loan_id",loan_id)
                .when()
                .get("/v1/origination/documents")
                .then()
                .log().all()
                .extract().response();
        System.out.println(response);
        // Status Code Assertion

        AllureLogs.softAssertEquals(softAssert,response.statusCode(),200,"status code");
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

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @Test(dataProvider = "Get Car Categories")
    @Parameters({"search"})
    public void getCarCategories(String search) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + initialization.franchiseAdminToken)
                .queryParam("search", search)
                .when()
                .get("/v1/inventory/category")
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status Code should be 200");

        List<Map<String, Object>> categoryList = response.jsonPath().getList("categoryList");
        Map<String, Object> pagination = response.jsonPath().getMap("pagination");

        softAssert.assertNotNull(categoryList, "Category list should not be null");
        softAssert.assertTrue(categoryList.size() > 0, "Category list should not be empty");

        boolean matchFound = categoryList.stream()
                .anyMatch(cat -> ((String) cat.get("name")).toLowerCase().contains(search.toLowerCase()));
        softAssert.assertTrue(matchFound, "At least one category should match search: " + search);

        softAssert.assertTrue((int) pagination.get("total") >= categoryList.size(), "Pagination total should be >= list size");

        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @DataProvider(name = "Get Car Categories")
    public Object[][] getCarCategoriesData() {
        return Postive_Data_Extractor.ExcelData("Get Car Categories");
    }

    @Test(dataProvider = "Get Car Model")
    @Parameters({"make_id", "use_cache", "page_size"})
    public void getCarModel(String make_id, String use_cache, String page_size) {

        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .queryParam("make_id", make_id)
                .queryParam("use_cache", use_cache)
                .queryParam("page_size", page_size)
                .when()
                .get("/v1/inventory/model")
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status Code should be 200");

        List<Map<String, Object>> modelList = response.jsonPath().getList("modelList");
        Map<String, Object> pagination = response.jsonPath().getMap("pagination");

        softAssert.assertNotNull(modelList, "Model list should not be null");
        softAssert.assertTrue(modelList.size() > 0, "Model list should not be empty");

        // Check that all returned models belong to the correct make
        for (Map<String, Object> model : modelList) {
            Map<String, Object> make = (Map<String, Object>) model.get("make");
            softAssert.assertEquals(make.get("id").toString(), make_id,
                    "Each model should have make_id = " + make_id);
        }

        softAssert.assertEquals(pagination.get("pageSize"), Integer.parseInt(page_size), "Mismatch in page size");
        softAssert.assertEquals(pagination.get("currentPage"), 1, "Expected current page to be 1");
        softAssert.assertTrue((int) pagination.get("total") > 0, "Total should be > 0");

        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @DataProvider(name = "Get Car Model")
    public Object[][] getCarModelData() {
        return Postive_Data_Extractor.ExcelData("Get Car Model");
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

// Execute all soft asrtions using custom method
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get all loan products")
    public Object[][] getLoanProductTestData() {
        return Postive_Data_Extractor.ExcelData("Get all loan products");
    }

    @Test(dataProvider = "Get Country List")
    @Parameters({"page_size"})
    public void getCountryList(String pageSize) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + token) // Remove this if API is public
                .queryParam("page_size", pageSize)
                .when()
                .get("/v1/country")
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status Code should be 200");

        List<Map<String, Object>> countries = response.jsonPath().getList("countries");
        softAssert.assertNotNull(countries, "Countries list should not be null");
        softAssert.assertTrue(countries.size() <= Integer.parseInt(pageSize), "Countries list size exceeds page_size");
        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @DataProvider(name = "Get Country List")
    public Object[][] getCountryListData() {
        return Postive_Data_Extractor.ExcelData("Get Country List");
    }



    @Test
    public void getCountryDetailsByCode() {

        Response response = given()
                //.baseUri("https://api.staging.myautochek.com")
                .header("Authorization","Bearer "+token)// Remove this if API is public
                //.queryParam("country", country)
                .get("/v1/country/"+CountryCode)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract().response();

        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(json.get("id"), "ID is null");
        softAssert.assertEquals(json.get("code"), CountryCode, "Incorrect country code");


        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @Test()
    public void loanMissingDocuments(){
        Response response = given()
                .header("Authorization","Bearer "+token)
                .header("X-autochek-app","dealerplus")
                .queryParam("loan_id", loan_id)
                .when()
                .get("/v2/origination/loans/"+loan_id+"/missing-documents")
                .then()
                .log().all()
                .extract().response();

        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        List<String> documents = json.getList("documents");
        AllureLogs.softAssertEquals(softAssert,response.statusCode(),200,"status");
        AllureLogs.softAssertTrue(softAssert, !documents.isEmpty(),"document");


        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @Test()
    public void loanDocumentConfig(){
        Response response = given()
                .header("Authorization","Bearer "+token)
                .when()
                .get("/v2/origination/loan/document-config/"+loan_id)
                .then()
                .log().all()
                .extract().response();

        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        List<String> documents = json.getList("documents");
        AllureLogs.softAssertEquals(softAssert,response.statusCode(),200,"status");
        AllureLogs.softAssertTrue(softAssert, !documents.isEmpty(),"document");


        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @Test()
    public void LoanStatusTrail(){
        Response response = given()
                .header("Authorization","Bearer "+token)
                .when()
                .get("/v2/origination/"+loan_id+"/status-trail")
                .then()
                .log().all()
                .extract().response();

        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        List<Map<String, Object>> documents = json.getList("documents");
        AllureLogs.softAssertEquals(softAssert,response.statusCode(),200,"status");
        AllureLogs.softAssertTrue(softAssert, !json.getList("documents").isEmpty(),"documents");
        AllureLogs.softAssertNotNull(softAssert, documents, "Documents should be an object array");
        AllureLogs.softAssertTrue(softAssert, documents.size() > 0, "Documents array should not be empty");

        for (int i = 0; i < documents.size(); i++) {
            List<Object> fields = (List<Object>) documents.get(i).get("fields");
            AllureLogs.softAssertTrue(softAssert, fields != null && !fields.isEmpty(), "Document index-[" + i + "] fields should not be empty");
        }

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @Test()
    public void carDocuments(){
        Response response = given()
                .header("Authorization","Bearer "+token)

                .queryParam("country", CountryCode)
                .queryParam("carId",car_id)
                .when()
                .get("/v1/inventory/documents")
                .then()
                .log().all()
                .extract().response();

        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status");


        AllureLogs.executeSoftAssertAll(softAssert);
    }
    public static final String filePath = "src/test/java/TestData/bmw.jpeg";
    public static final boolean isPublic = true;
    public static  Response uplooad_response;
    @Test(priority = 7)
    public void uploadDocument() {
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .multiPart("file", new File(filePath))
                .formParam("public", isPublic)
                .when()
                .post("/document/upload")
                .then()
                .log().all()
                .extract().response();
        uplooad_response = response;
        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),201,"status");


        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @Test(priority = 8)
    public void uploadJSONDocuments(){
        String Request = "{\n" +
                "  \"loan_id\": \""+loan_id+"\",\n" +
                "  \"documents\": [\n" +
                "    {\n" +
                "      \"name\": \"workId\",\n" +
                "      \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"workId\",\n" +
                "      \"password\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"workId2\",\n" +
                "      \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"workId2\",\n" +
                "      \"password\": \"pdf123\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"proofOfEmployment\",\n" +
                "      \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"Employment confirmation letter\",\n" +
                "      \"password\": \"pdf123\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"proofOfEmploymentPayslip\",\n" +
                "      \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"proofOfEmploymentPayslip\",\n" +
                "      \"password\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"proofOfEmploymentPayslip2\",\n" +
                "      \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"proofOfEmploymentPayslip2\",\n" +
                "      \"password\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"proofOfEmploymentPayslip3\",\n" +
                "      \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"proofOfEmploymentPayslip3\",\n" +
                "      \"password\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"personalBankStatement\",\n" +
                "     \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"personalBankStatement\",\n" +
                "      \"password\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"businessBankStatement\",\n" +
                "      \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"businessBankStatement\",\n" +
                "      \"password\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"mobileMoneyStatement\",\n" +
                "      \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"mobileMoneyStatement\",\n" +
                "      \"password\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"utilityBill\",\n" +
                "      \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"utilityBill\",\n" +
                "      \"password\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"dealerInvoice\",\n" +
                "     \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"dealerInvoice\",\n" +
                "      \"password\": \"\"\n" +
                "    },\n" +
                "{\n" +
                "      \"name\": \"identity\",\n" +
                "      \"file_url\": \""+filePath+"\",\n" +
                "      \"description\": \"National ID\",\n" +
                "      \"password\": \"\"\n" +
                "    }"+
                "  ],\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"name\": \"nameTitle\",\n" +
                "      \"value\": \"Mrs\",\n" +
                "      \"type\": \"selection\",\n" +
                "      \"label\": \"Title\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"maritalStatus\",\n" +
                "      \"value\": \"Married\",\n" +
                "      \"type\": \"radio\",\n" +
                "      \"label\": \"Marital status\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"address\",\n" +
                "      \"value\": \"nigeria\",\n" +
                "      \"type\": \"\",\n" +
                "      \"label\": \"\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"houseNumber\",\n" +
                "      \"value\": \"signsfgosdnvk\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"House/Apartment number\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"streetName\",\n" +
                "      \"value\": \"sgdpgodsm\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Street name\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"state\",\n" +
                "      \"value\": \"Anambra\",\n" +
                "      \"type\": \"selection\",\n" +
                "      \"label\": \"State of residence\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"lga\",\n" +
                "      \"value\": \"Ekwusigo\",\n" +
                "      \"type\": \"selection\",\n" +
                "      \"label\": \"LGA of residence\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"city\",\n" +
                "      \"value\": \"Ihiala\",\n" +
                "      \"type\": \"selection\",\n" +
                "      \"label\": \"City of residence\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"nextOfKinName\",\n" +
                "      \"value\": \"spvmdp\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Next of Kin's name\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"nextOfKinAddress\",\n" +
                "      \"value\": \"doghnbspgnfo\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Next of Kin's Address\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"nextOfKinTelephone\",\n" +
                "      \"value\": \"9816161356\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Next of Kin's Phone number\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"nextOfKinEmail\",\n" +
                "      \"value\": \"sgmsdv nio@gmail.com\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Next of Kin's Email\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"nextOfKinRelationship\",\n" +
                "      \"value\": \"FATHER\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Next of Kin's Relationship\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"accountHolderName\",\n" +
                "      \"value\": \"sdhomdspm\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Account holder name\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"accountNumber\",\n" +
                "      \"value\": \"9876543210\",\n" +
                "      \"type\": \"number\",\n" +
                "      \"label\": \"Your account number\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"accountType\",\n" +
                "      \"value\": \"Savings\",\n" +
                "      \"type\": \"selection\",\n" +
                "      \"label\": \"Account type\",\n" +
                "      \"valid\": true\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("X-autochek-app","dealerplus")
                .header("Content-Type","application/json")
                .body(Request)
                .when()
                .post("/v2/origination/documents/CreateDocuments")
                .then()
                .log().all()
                .extract().response();
        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status");


        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @Test(priority = 9)
    public void getUser() {
        Response response = given()
                .header("Authorization", "Bearer " + inspector_token)
                .queryParam("country",CountryCode)
                .queryParam("role_name","SALES_OFFICER")
                .queryParam("page_size", 1000)
                .when()
                .get("/v1/user")
                .then()
                .log().all()
                .extract().response();

        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status");
        List<Map<String, Object>> users = json.getList("users");

        // Find the user with firstname "Daniel" and lastname "Inspector qa"
        for (Map<String, Object> user : users) {
            String firstName = (String) user.get("firstname");
            String lastName = (String) user.get("lastname");

            if ("Daniel".equals(firstName) && " inspector qa".equals(lastName)) {
                danielInspector = user;
                break;
            }
        }

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    //@Test(priority = 10)
    public void getInspectionWorkShop() {
        String Request  = "{\n" +
                "    \"year\": 2018,\n" +
                "    \"vin\": \"45647864738648643\",\n" +
                "    \"country\": \""+CountryCode+"\",\n" +
                "    \"model_id\": 1887,\n" +
                "    \"inspection_type\": \"warranty\",\n" +
                "    \"car_id\": \"oB2p6L6Df\",\n" +
                "    \"state\": \"Lagos\",\n" +
                "    \"city\": \"Alimosho\",\n" +
                "    \"franchise_id\": \"kpMngdaMZ\",\n" +
                "    \"assignee_id\": \"6WAtJ1HtN\",\n" +
                "    \"note\": \"PPI\",\n" +
                "    \"loan_id\": \""+loan_id+"\"\n" +
                "}";
        Response response = given()
                .header("Authorization", "Bearer " + inspector_token)
                .body(Request)
                .when()
                .get("/v1/franchise/inspection_workshop")
                .then()
                .log().all()
                .extract().response();

        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status");


        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @Test(priority = 10)
    public void postInspectionRequest() {

        Map<String,Object> model =(Map<String, Object>) car.get("model");
        Integer modelId =(Integer) model.get("id");
        if(danielInspector!=null){
            String assigneeId = (String) danielInspector.get("id");
            String Request  = "{\n" +
                    "    \"year\": "+(Integer)car.get("year")+",\n" +
                    "    \"vin\": \""+(String) car.get("vin")+"\",\n" +
                    "    \"country\": \""+CountryCode+"\",\n" +
                    "    \"model_id\": "+modelId+",\n" +
                    "    \"inspection_type\": \"warranty\",\n" +
                    "    \"car_id\": \""+car_id+"\",\n" +
                    "    \"state\": \""+(String) car.get("state")+"\",\n" +
                    "    \"city\": \""+(String) car.get("city")+"\",\n" +
                    "    \"franchise_id\": \"kpMngdaMZ\",\n" +
                    "    \"assignee_id\": \""+assigneeId+"\",\n" +
                    "    \"note\": \"PPI\",\n" +
                    "    \"loan_id\": \""+loan_id+"\"\n" +
                    "}";
            Response response = given()
                    .header("Authorization", "Bearer " + inspector_token)
                    .header("Content-Type","application/json")
                    .header("X-autochek-app","dealerplus")
                    .body(Request)
                    .when()
                    .post("/v1/inventory/inspection_request")
                    .then()
                    .log().all()
                    .extract().response();

            // Validation
            JsonPath json = response.jsonPath();
            InspectionId = json.getString("id");
            SoftAssert softAssert = new SoftAssert();
            AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status");


            AllureLogs.executeSoftAssertAll(softAssert);
        }
    }
    @Test(priority = 11)
    public void alertMessages() {
        Response response = given()
                .header("Authorization", "Bearer " + inspector_token)
                .queryParam("country",CountryCode)
                .queryParam("page_size",10)
                .queryParam("page_number",1)
                .queryParam("channel","in_app")
                .queryParam("category","moderation_created,moderation_updated,moderation_resolved")
                .queryParam("status","sent")
                .when()
                .get("/v1/inventory/admin/alerts/messages")
                .then()
                .log().all()
                .extract().response();

        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status");


        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @Test(priority = 12)
    public void dashBoard_Country() {

        Response response = given()
                .header("Authorization", "Bearer " + inspector_token)
                .queryParam("country",CountryCode)
                .when()
                .get("/dashboard?country=KE")
                .then()
                .log().all()
                .extract().response();

        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status");


        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @Test(priority = 13)
    public void state() {

        Response response = given()
                .header("Authorization", "Bearer " + inspector_token)
                .queryParam("country",CountryCode)
                .queryParam("page_size",250)
                .when()
                .get("v1/state")
                .then()
                .log().all()
                .extract().response();

        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status");


        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @Test(priority = 14)
    public void franchise_Inspection() {

        Response response = given()
                .header("Authorization", "Bearer " + inspector_token)
                .queryParam("country",CountryCode)
                .queryParam("inspection_type","warranty")
                .when()
                .get("v1/franchise/inspection_workshop")
                .then()
                .log().all()
                .extract().response();

        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status");


        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @Test(priority = 15)
    public void v1_getUser() {

        Response response = given()
                .header("Authorization", "Bearer " + inspector_token)
                .queryParam("current_page",CountryCode)
                .queryParam("page_size",10)
                .queryParam("country","KE")
                .queryParam("role_name","SALES_OFFICER")
                .queryParam("search","daniel")
                .when()
                .get("v1/user")
                .then()
                .log().all()
                .extract().response();
        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),200,"status");


        AllureLogs.executeSoftAssertAll(softAssert);
    }

}




