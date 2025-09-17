package autochek_api_positive.OfferSimulation;

import Base.Initialization;
import Listener.AllureLogs;
import java.lang.Thread;

import Utils.Postive_Data_Extractor;
import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

//import static Base.Initialization.ByPassKey;
import static io.restassured.RestAssured.given;

@Epic("Offer Simulation")
@Feature("Positive Tests Simulation Offer")
public class OfferSimulationPositive {
    Initialization initialization = new Initialization();
    private static List<String> offerIds = new ArrayList<>();
    private static final String[] Countries = {"KE", "NG"};
    private static final Random random = new Random();
    private static String user_id;
    private static String ByPassKey;
    private static String token;
    private static int get_loanValue;
    private static final String CountryCode = "NG";// Countries[random.nextInt(Countries.length)];
    private static final String product_id = "hj43BXLXe";
    private static String car_id;
    private static String loan_id;
    private static float income;
    private static String tyreSpecsCheck;
    private static String inspector_token;
    private static Map<String, Object> car;
    private static Map<String, Object> danielInspector = null;
    private static String InspectionId;
    private static int loanValue;
    private static int monthlyPaymentAfterdivison;
    private static final List<String> pendingPartnerIds = new ArrayList<>();
    private static final List<String> pendingPartnerIdsAfter1min = new ArrayList<>();
    private static final Map<String, Map<String, String>> tyreMap = new LinkedHashMap<>();
    private static String randomCarId;
    private static String id;
   private static Map<String, String> details = new HashMap<>();
    private static Map<String, String> entry=new HashMap<>();
    private static final String STATIC_USER_TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJCUWxRQmJMVFciLCJpYXQiOjE3NTYyODc1OTcsImV4cCI6MTc4NzgyMzU5N30.oScWjmZlmF2v-qAoXQ3A0-P8QXDXP2CFmDSjDGJ5llcig2cw5DSXX_zVytrWCUjqTiL4NA5qBxxRay4cQ38VgQ";

    @BeforeClass
    public void setUp() {
        initialization.initializeEnvironment(); // Sets base URI and tokens
        user_id = Initialization.franchiseAdminToken.get("userId");
        token = Initialization.franchiseAdminToken.get("token");
        ByPassKey = Initialization.ByPassKey;
    }

    @Test()
    @Description("Get Car Details to fetch Car ID")
    @Story("Get Car Details")
    @Severity(SeverityLevel.NORMAL)
    public void GetCarDetails() {
        System.out.println("Token_Franchise: " + token);
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("exclude_sold_cars", true)
                .queryParam("exclude_category_name", "logbook")
                .queryParam("page_number", 1)
                .queryParam("franchise", "kpMngdaMZ")
                .queryParam("country", CountryCode)
                .queryParam("franchise_id", "kpMngdaMZ")
                .queryParam("name", "Toyota")
                .when()
                .get("/v1/inventory/car")
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "status code");
//        car_id = response.jsonPath().get("carList[3].id");
//        // car_id = response.jsonPath().getString("carList[3].id");
//        income = Float.parseFloat(response.jsonPath().getString("carList[3].installment")) * 3;
//        System.out.println("car_id: " + car_id);
//        System.out.println("income: " + income);
        AllureLogs.executeSoftAssertAll(softAssert);
        List<Float> installments = response.jsonPath().getList("carList.installment", Float.class);
        List<String> ids = response.jsonPath().getList("carList.id", String.class);
        List<String> tyreSpecs = response.jsonPath().getList("carList.tyreSpecs", String.class);
        List<Float> prices = response.jsonPath().getList("carList.price", Float.class);

        for (int i = 0; i < ids.size(); i++) {

            details.put("installment", String.valueOf(installments.get(i)*3));  // convert float → string
            details.put("tyreSpecs", tyreSpecs.get(i));
            details.put("price", String.valueOf(prices.get(i)));  // convert float → string
            tyreMap.put(ids.get(i), details);
        }

        List<String> carIds = new ArrayList<>(tyreMap.keySet());
        Random random = new Random();
        randomCarId = carIds.get(random.nextInt(carIds.size()));

        // ✅ Get tyreSpecs for that car
        tyreSpecsCheck = tyreMap.get(randomCarId).get("tyreSpecs");

        System.out.println("Picked Car ID: " + randomCarId);
        System.out.println("Tyre Specs: " + tyreSpecs);

        // 🔹 Print all stored data
        System.out.println("CarList Response Map:");
        for (Map.Entry<String, Map<String, String>> entry : tyreMap.entrySet()) {
            System.out.println("Car ID: " + entry.getKey() + " => " + entry.getValue());
        }
        AllureLogs.executeSoftAssertAll(softAssert);
    }

    public static String getRandomFirstName() {
        return "API" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String getRandomLastName() {
        return "Testing" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String getRandomPhoneNumber() {
        Random rand = new Random();
        // Operator digit (0–9)
        int operatorDigit = rand.nextInt(10);

        // Generate 9-digit subscriber number
        long subscriberNumber = 100000000 + rand.nextInt(900000000);

        // Total = 3 (234) + 1 (operator) + 9 = 13 digits
        return "234" + operatorDigit + subscriberNumber;
    }

    public static String getRandomPhoneNumber11() {
        Random rand = new Random();
        // Operator digit (0–9)
        int operatorDigit = rand.nextInt(10);

        // Generate 9-digit subscriber number
        long subscriberNumber = 100000000 + rand.nextInt(900000000);

        // Total = 3 (234) + 1 (operator) + 9 = 13 digits
        return "2" + operatorDigit + subscriberNumber;
    }

    public static String getRandomEmail() {
        return "API" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
    }

    private static final Set<String> usedNumbers = new HashSet<>();

    public static String getRandomGender() {
        return (new Random().nextBoolean()) ? "Male" : "Female";
    }

    public static String getBvnNumber() {
        return getRandomPhoneNumber11();
    }

    public static String getNinNumber() {
        return getRandomPhoneNumber11();
    }

    String firstName = getRandomFirstName();
    String lastName = getRandomLastName();
    String phoneNumber = getRandomPhoneNumber();
    String Email = getRandomEmail();
    String Gender = getRandomGender();
    String bvnNumber = getBvnNumber();
    String ninNumber = getNinNumber();

    @Test()
    @Description("Creating loan for first fetched Toyota Car")
    @Story("Create Loan")
    @Severity(SeverityLevel.NORMAL)
    public void PostCreateLoan() {
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
                "            \"value\": \"" + firstName + "\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"label\": \"First name\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"lastName\",\n" +
                "            \"value\": \"" + lastName + "\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"label\": \"Last name\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"telephone\",\n" +
                "            \"value\": \"" + phoneNumber + "\",\n" +
                "            \"type\": \"tel\",\n" +
                "            \"label\": \"Phone number\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"email\",\n" +
                "            \"value\": \"" + Email + "\",\n" +
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
                "            \"value\": \"" + details.get("installment") + "\",\n" +
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
                "    \"car_id\": \""+randomCarId+"\",\n" +
                "    \"user_id\": \"BQlQBbLTW\",\n" +
                "    \"product_id\": \"hj43BXLXe\",\n" +
                "    \"country\": \"NG\",\n" +
                "    \"is_draft\": true,\n" +
                "    \"captcha_token\": \"" + ByPassKey + "\"\n" +
                "}";

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .header("x-autochek-app", "marketplace_web")
                .body(requestBody)
                .when()
                .post("/v2/origination/loan")
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "status code");

        loan_id = response.jsonPath().getString("id");
        System.out.println("loan id: " + loan_id);
        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @Test(dependsOnMethods = "PostCreateLoan")
    @Description("Submitting the loan")
    @Story("Update Loan")
    @Severity(SeverityLevel.NORMAL)
    public void PutUpdateLoan() throws InterruptedException {

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
                "            \"value\": \"" + firstName + "\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"label\": \"First name\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"lastName\",\n" +
                "            \"value\": \"" + lastName + "\",\n" +
                "            \"type\": \"text\",\n" +
                "            \"label\": \"Last name\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"telephone\",\n" +
                "            \"value\": \"" + phoneNumber + "\",\n" +
                "            \"type\": \"tel\",\n" +
                "            \"label\": \"Phone number\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"email\",\n" +
                "            \"value\": \"" + Email + "\",\n" +
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
                "            \"value\": \"" + details.get("installment") + "\",\n" +
                "            \"type\": \"amount\",\n" +
                "            \"label\": \"Total Take-Home Pay (after salary deductions) (₦)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"sourceOfFunds\",\n" +
                "            \"value\": \"Wages, bonuses, dividends, and other income from employment\",\n" +
                "            \"label\": \"Source of Funds\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"dob\",\n" +
                "            \"value\": \"1997-06-12\",\n" +
                "            \"type\": \"date\",\n" +
                "            \"label\": \"Date of birth\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"nin\",\n" +
                "            \"value\": \"" + ninNumber + "\",\n" +
                "            \"type\": \"number\",\n" +
                "            \"label\": \"National Identification Number (NIN)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"BVN\",\n" +
                "            \"value\": \"" + bvnNumber + "\",\n" +
                "            \"type\": \"number\",\n" +
                "            \"label\": \"Your BVN\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"gender\",\n" +
                "            \"value\": \"" + Gender + "\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"label\": \"Gender\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"nationality\",\n" +
                "            \"value\": \"Nigeria\",\n" +
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
                "            \"value\": \"Abuja\",\n" +
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
                "            \"value\": \"Agriculture\",\n" +
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
                "            \"value\": \"1\",\n" +
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
                "            \"value\": \"Coronation Merchant Bank\",\n" +
                "            \"label\": \"Your bank\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"monthlyExpenses\",\n" +
                "            \"value\": \"65000\",\n" +
                "            \"type\": \"amount\",\n" +
                "            \"label\": \"Total monthly expenses (₦)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"monthlyGrossIncome2\",\n" +
                "            \"value\": \"" +details.get("installment")+ "\",\n" +
                "            \"type\": \"amount\",\n" +
                "            \"label\": \"Total Take-Home Pay (after deductions) (₦)\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"vehicleUse\",\n" +
                "            \"value\": \"Personal\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"label\": \"What will the vehicle be used for?\",\n" +
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
                "            \"value\": true,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"label\": \"Vehicle Registration\",\n" +
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
                "            \"value\": false,\n" +
                "            \"type\": \"checkbox\",\n" +
                "            \"valid\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"vehicleDiscount\",\n" +
                "            \"value\": \"0%\",\n" +
                "            \"label\": \"Total Discount %\",\n" +
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
                "            \"name\": \"platformPolicy\",\n" +
                "            \"value\": \"Yes\",\n" +
                "            \"type\": \"radio\",\n" +
                "            \"label\": \"I agree to the: \\n (a) terms and Conditions of the platform (https://autochek.africa/ng/terms-of-service) and the \\n (b) privacy policy of the platform (https://autochek.africa/ng/privacy-policy)\",\n" +
                "            \"valid\": true\n" +
                "        }\n" +
                "    ],\n" +
                "    \"car_id\": \"" + randomCarId + "\",\n" +
                "    \"user_id\": \"BQlQBbLTW\",\n" +
                "    \"product_id\": \"hj43BXLXe\",\n" +
                "    \"country\": \"NG\",\n" +
                "    \"is_draft\": false,\n" +
                "    \"captcha_token\": \"" + ByPassKey + "\"\n" +
                "}";

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .header("x-autochek-app", "marketplace_web")
                .body(requestBody)
                .when()
                .put("/v2/origination/loan/" + loan_id)
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "status code");
        System.out.println("loan id: " + response.jsonPath().getString("id"));
        AllureLogs.executeSoftAssertAll(softAssert);


    }

    @Test()
    @Description("Fetching loan by ID")
    @Story("Get Loan By ID")
    @Severity(SeverityLevel.NORMAL)
    public void GetLoanById() throws InterruptedException {
       Thread.sleep(240000);
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                 .get("/v1/origination/loans/" + loan_id + "")
            //    .get("/v1/origination/loans/VOR4JF6pR")
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Expected Status : 200");
        String get_response_loan_id = response.jsonPath().getString("id");
        get_loanValue = response.jsonPath().getInt("car.loanValue");
        System.out.println(get_loanValue);
        List<Map<String, Object>> offers = response.jsonPath().getList("offers");

        List<Map<String, String>> pendingOfferPartnerList = new ArrayList<>();

        for (Map<String, Object> offer : offers) {
            String status = (String) offer.get("status");
            if ("PENDING".equalsIgnoreCase(status)) {
                String offerId = (String) offer.get("id");
                Map<String, Object> partner = (Map<String, Object>) offer.get("partner");
                String partnerId = (String) partner.get("id");
               entry = new HashMap<>();
                entry.put("offerId", offerId);
                entry.put("partnerId", partnerId);

                pendingOfferPartnerList.add(entry);
            }
        }

        System.out.println("Pending Partner IDs: " + entry);

        AllureLogs.executeSoftAssertAll(softAssert);
//        return pendingPartnerIds;

    }


    @Test()
    @Description("Get the list of Tyre brands")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get Tyre Brands")
    public void GetTyreBrands() {
        SoftAssert softAssert = new SoftAssert();
        // Send GET request with query params
        String CountryCode = Countries[random.nextInt(Countries.length)];
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", "NG")
               // .queryParam("car_id", randomCarId)
                .queryParam("car_id", "684EEv-eE")
                .queryParam("currentPage", 3)
                .header("x-autochek-app", "dealerplus")
                .when()
                .get("/v1/inventory/tyres/brands")
                .then()
                .log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
//        if (tyreSpecsCheck != null && !tyreSpecsCheck.isEmpty()) {
            // ✅ 1. Status code validation
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status Code Validation");

            // ✅ 2. Validate brands array is not null/empty
            List<String> brandNames = jsonPath.getList("brands.name");
            AllureLogs.softAssertTrue(softAssert, brandNames != null && !brandNames.isEmpty(), "Brands list is not empty");

            // ✅ 4. Validate each brand name is not blank/null
            for (int i = 0; i < brandNames.size(); i++) {
                AllureLogs.softAssertTrue(softAssert, brandNames.get(i) != null && !brandNames.get(i).trim().isEmpty(),
                        "Brand name at index " + i + " is valid");
            }
            // ✅ 5. Validate pagination fields
            AllureLogs.softAssertTrue(softAssert, jsonPath.getInt("pagination.total") > 0, "Pagination total is greater than 0");
            // ✅ 6. Validate that total >= returned brands count
            int total = jsonPath.getInt("pagination.total");
            AllureLogs.softAssertTrue(softAssert, total >= brandNames.size(), "Total >= brands count on current page");

//        } else {
//            List<Object> brandsList = response.jsonPath().getList("brands");
//            AllureLogs.softAssertEquals(softAssert, brandsList.size(), 0, "Expected 'brands' to be empty");
//
//        }

        // ✅ Collect all assertions
        AllureLogs.executeSoftAssertAll(softAssert);

    }
    private void addIfNotNull(List<Map<String, Object>> preferences,
                              String name,
                              Object value,
                              String type,
                              String label,
                              boolean valid) {
        if (value != null && !(value instanceof String && ((String) value).trim().isEmpty())) {
            Map<String, Object> pref = new HashMap<>();
            pref.put("name", name);
            pref.put("value", value);
            if (type != null) pref.put("type", type);
            if (label != null) pref.put("label", label);
            pref.put("valid", valid);
            preferences.add(pref);
        }
    }

    @Test(dataProvider = "Submit loan preference")
    @Description("Submit Loan Customer Preferences")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Submit Simulate Offer")
    @Parameters({"type", "severity", "description", "vehicleDiscount", "dealDiscount", "desiredLoanCurrency", "interestRateType", "interestRate", "residualPercentage", "term", "includeRoadworthinessFees", "includeLicenceRenewalFees", "feePaymentMethod", "upfrontVehicleRegistration", "upfrontInsurance", "upfrontVehicleTracking", "upfrontMaintenancePlan", "upfrontCreditLifeInsurance", "upfrontWarranty", "upfrontTyres", "tyreBrand", "requestedWarrantyPlan", "tyreQuantity", "ExpectedStatusCode"})
    public void PostOfferSimulation(String type, String severity, String description, String vehicleDiscount, String dealDiscount, String desiredLoanCurrency, String interestRateType, String interestRate, String residualPercentage, String term, String includeRoadworthinessFees, String includeLicenceRenewalFees, String feePaymentMethod, String upfrontVehicleRegistration, String upfrontInsurance, String upfrontVehicleTracking, String upfrontMaintenancePlan, String upfrontCreditLifeInsurance, String upfrontWarranty, String upfrontTyres, String tyreBrand, String requestedWarrantyPlan, String tyreQuantity, String ExpectedStatusCode) {
        SoftAssert softAssert = new SoftAssert();
        // Prepare request body as Map (easier to construct than raw JSON string)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("loan_id", loan_id);
        requestBody.put("car_id", randomCarId);

//        requestBody.put("loan_id", "VOR4JF6pR");
//        requestBody.put("car_id", "684EEv-eE");
//        requestBody.put("partner_id", pendingPartnerIds.get(0));
       // requestBody.put("partner_id",entry.get("partnerId"));
        requestBody.put("partner_id", entry.get("partnerId"));

// ✅ add offer_id from entry map
        requestBody.put("offer_id", entry.get("offerId"));

        // Build customer_preferences list
        loanValue = (get_loanValue * 50) / 100;
        monthlyPaymentAfterdivison = loanValue / 12;
        List<Map<String, Object>> preferences = new ArrayList<>();

        addIfNotNull(preferences, "vehicleDiscount", vehicleDiscount, "selection", null, true);
        addIfNotNull(preferences, "dealDiscount", dealDiscount, "selection", null, true);
        addIfNotNull(preferences, "desiredLoanCurrency", desiredLoanCurrency, "selection", null, true);
        addIfNotNull(preferences, "equityContribution", loanValue, "amount", null, true);
        addIfNotNull(preferences, "monthlyPayment", monthlyPaymentAfterdivison, "amount", null, true);
        addIfNotNull(preferences, "interestRateType", interestRateType, "selection", null, true);
        addIfNotNull(preferences, "interestRate", interestRate, "selection", null, true);
        addIfNotNull(preferences, "residualPercentage", residualPercentage, "selection", null, true);
        addIfNotNull(preferences, "term", term, "selection", null, true);
        addIfNotNull(preferences, "includeRoadworthinessFees", includeRoadworthinessFees, "radio", null, true);
        addIfNotNull(preferences, "includeLicenceRenewalFees", includeLicenceRenewalFees, "radio", null, true);
        addIfNotNull(preferences, "feePaymentMethod", feePaymentMethod, "radio", null, true);
        addIfNotNull(preferences, "upfrontVehicleRegistration", upfrontVehicleRegistration, "checkbox", "Vehicle Registration", true);
        addIfNotNull(preferences, "upfrontInsurance", upfrontInsurance, "checkbox", null, true);
        addIfNotNull(preferences, "upfrontVehicleTracking", upfrontVehicleTracking, "checkbox", null, true);
        addIfNotNull(preferences, "upfrontMaintenancePlan", upfrontMaintenancePlan, "checkbox", null, true);
        addIfNotNull(preferences, "upfrontCreditLifeInsurance", upfrontCreditLifeInsurance, "checkbox", "Credit Life Insurance", true);
        addIfNotNull(preferences, "upfrontWarranty", upfrontWarranty, "checkbox", null, true);
        addIfNotNull(preferences, "upfrontTyres", upfrontTyres, "checkbox", null, true);

        if (tyreSpecsCheck != null && !tyreSpecsCheck.isEmpty()) {
            addIfNotNull(preferences, "tyreBrand", tyreBrand, null, "Tyre Brand", true);
            addIfNotNull(preferences, "tyreQuantity", tyreQuantity, null, "Tyre Quantity", true);
        }

        addIfNotNull(preferences, "requestedWarrantyPlan", requestedWarrantyPlan, null, "Requested Warranty Plan", true);
//        requestBody.put("OfferID", entry.get("offerId"));
// Add preferences to main body
        requestBody.put("customer_preferences", preferences);

        // Send POST request
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .header("x-autochek-app", "dealerPlus")
                .body(requestBody)
                .when()
                .post("/v2/origination/loans/offer-simulations")
                .then()
                .log().all()
                .extract().response();
        id = response.jsonPath().getString("id");
        offerIds.add(id);
        System.out.println("✅ Stored Offer ID: " + id);
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "status code");
        System.out.println(id);


// Extract request/response preferences
        List<Map<String, Object>> responsePreferences = response.jsonPath().getList("customerPreferences");

        List<Map<String, Object>> requestPreferences = (List<Map<String, Object>>) requestBody.get("customer_preferences");

// For every possible field, if it was null in Excel → check it's absent in response
        Map<String, Object> allFields = new HashMap<>();

        allFields.put("vehicleDiscount", vehicleDiscount);
        allFields.put("dealDiscount", dealDiscount);
        allFields.put("desiredLoanCurrency", desiredLoanCurrency);
        allFields.put("equityContribution", loanValue);
        allFields.put("monthlyPayment", monthlyPaymentAfterdivison);
        allFields.put("interestRateType", interestRateType);
        allFields.put("interestRate", interestRate);
        allFields.put("residualPercentage", residualPercentage);
        allFields.put("term", term);
        allFields.put("includeRoadworthinessFees", includeRoadworthinessFees);
        allFields.put("includeLicenceRenewalFees", includeLicenceRenewalFees);
        allFields.put("feePaymentMethod", feePaymentMethod);
        allFields.put("upfrontVehicleRegistration", upfrontVehicleRegistration);
        allFields.put("upfrontInsurance", upfrontInsurance);
        allFields.put("upfrontVehicleTracking", upfrontVehicleTracking);
        allFields.put("upfrontMaintenancePlan", upfrontMaintenancePlan);
        allFields.put("upfrontCreditLifeInsurance", upfrontCreditLifeInsurance);
        allFields.put("upfrontWarranty", upfrontWarranty);
        allFields.put("upfrontTyres", upfrontTyres);
        allFields.put("tyreBrand", tyreBrand);
        allFields.put("tyreQuantity", tyreQuantity);
        allFields.put("requestedWarrantyPlan", requestedWarrantyPlan);

        for (Map.Entry<String, Object> entry : allFields.entrySet()) {
            String param = entry.getKey();
            Object value = entry.getValue();

            if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                // param was omitted in request → validate it's absent in response
                boolean exists = responsePreferences.stream().anyMatch(p -> p.get("name").equals(param));
                AllureLogs.softAssertEquals(softAssert, exists, false, param + " should not be present in response");
            }
        }

// ✅ Validate each preference sent is present and matches in the response
        for (Map<String, Object> requestPref : requestPreferences) {
            String name = requestPref.get("name").toString();

            // Find matching preference in response
            Optional<Map<String, Object>> match = responsePreferences.stream()
                    .filter(pref -> pref.get("name").equals(name))
                    .findFirst();

            if (match.isPresent()) {
                Map<String, Object> responsePref = match.get();

                // Validate value
                AllureLogs.softAssertEquals(
                        softAssert,
                        String.valueOf(responsePref.get("value")),
                        String.valueOf(requestPref.get("value")),
                        "Validation for value of " + name
                );

                // Validate type if present in request
                if (requestPref.containsKey("type")) {
                    AllureLogs.softAssertEquals(
                            softAssert,
                            String.valueOf(responsePref.get("type")),
                            String.valueOf(requestPref.get("type")),
                            "Validation for type of " + name
                    );
                }

                // Validate label if present in request
                if (requestPref.containsKey("label")) {
                    AllureLogs.softAssertEquals(
                            softAssert,
                            String.valueOf(responsePref.get("label")),
                            String.valueOf(requestPref.get("label")),
                            "Validation for label of " + name
                    );
                }

                // Validate valid flag
                AllureLogs.softAssertEquals(
                        softAssert,
                        String.valueOf(responsePref.get("valid")),
                        String.valueOf(requestPref.get("valid")),
                        "Validation for valid flag of " + name
                );

            } else {
                // Use a separate method for missing params instead of failLog
                AllureLogs.softAssertMissingParam(softAssert, name + " from request is missing in response");
            }
            AllureLogs.executeSoftAssertAll(softAssert);
        }
    }
        @DataProvider(name = "Submit loan preference")
        public Object[][] SubmitOffer() {
            // Reads test data from Excel sheet named "Create Franchise"
            return Postive_Data_Extractor.ExcelData("Submit loan Preference");
        }


    @Test()
    @Description("Accept Simulation Offer")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Accept Offer")
    public void AcceptOffer() {
        SoftAssert softAssert = new SoftAssert();

        for (String id : offerIds) {
            Response response = given()
                    .header("Authorization", "Bearer " + token)
                    .header("x-autochek-app", "dealerPlus")
                    .when()
                    .put("/v2/origination/loans/offer-simulations/" + id + "/accept")
                    .then()
                    .log().all()
                    .extract().response();

            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200,
                    "Expected Status : 200 for offerId: " + id);
            String offerId = response.jsonPath().getString("id");
            AllureLogs.softAssertNotNull(softAssert, offerId, "Offer ID should not be null");

            String carId = response.jsonPath().getString("carId");
            AllureLogs.softAssertNotNull(softAssert, carId, "Car ID should not be null");

            String status = response.jsonPath().getString("status");
            AllureLogs.softAssertEquals(softAssert, status, "created", "Offer status should be 'selected'");

            String country = response.jsonPath().getString("country");
            AllureLogs.softAssertEquals(softAssert, country, "NG", "Country code validation");

            // ✅ simulationRequest validations
            String loanId = response.jsonPath().getString("simulationRequest.loanId");
            AllureLogs.softAssertNotNull(softAssert, loanId, "Loan ID inside simulationRequest should not be null");

            // ✅ simulationResponse validations
            String instalmentOffer = response.jsonPath().getString("simulationResponse.instalmentOffer");
            AllureLogs.softAssertNotNull(softAssert, instalmentOffer, "Instalment offer should not be null");


            // ✅ offer sub-block validations
            String clientMonthlyFinanceInstalment = response.jsonPath().getString("simulationResponse.offer.clientMonthlyFinanceInstalment");
            AllureLogs.softAssertNotNull(softAssert, clientMonthlyFinanceInstalment, "Client Monthly Finance Instalment validation");

            String totalMonthlyInstalment = response.jsonPath().getString("simulationResponse.offer.totalMonthlyInstalment");
            AllureLogs.softAssertEquals(softAssert, totalMonthlyInstalment, instalmentOffer, "Total monthly instalment should match instalmentOffer");

            // ✅ partner validations
            String partnerId = response.jsonPath().getString("partner.id");
            AllureLogs.softAssertNotNull(softAssert, partnerId, "Partner ID should not be null");


            Boolean partnerActive = response.jsonPath().getBoolean("partner.active");
            AllureLogs.softAssertEquals(softAssert, partnerActive, true, "Partner should be active");
        }



        AllureLogs.executeSoftAssertAll(softAssert);

    }

}

