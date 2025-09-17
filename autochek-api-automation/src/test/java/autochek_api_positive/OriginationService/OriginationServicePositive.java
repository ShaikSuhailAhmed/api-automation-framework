package autochek_api_positive.OriginationService;

import Base.Initialization;
import Listener.AllureLogs;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.util.*;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class OriginationServicePositive {
    Initialization initialization = new Initialization();
    private static List<String> offerIds = new ArrayList<>();
    private static final String[] Countries = {"KE", "NG"};
    private static final Random random = new Random();
    public static String user_id;

   public static Map<String, Object> partnerDetails = new HashMap<>();;
    public static String ByPassKey;
    public static String token;
    public static String underWriterToken;
    public static String vin;
    private static int get_loanValue;
    public static final String CountryCode = "NG";// Countries[random.nextInt(Countries.length)];
    private static final String product_id = "hj43BXLXe";
    private static String car_id;
    public static String loan_id;
    private static float income;
    private static String tyreSpecsCheck;
    private static String inspector_token;
    private static Map<String, Object> car;
    public static  String partnerId;
    public static String partnerName;
    private static Map<String, Object> danielInspector = null;
    private static String InspectionId;
    private static int loanValue;
    private static int monthlyPaymentAfterdivison;
    private static final List<String> pendingPartnerIds = new ArrayList<>();
    private static final List<String> pendingPartnerIdsAfter1min = new ArrayList<>();
    private static final Map<String, Map<String, String>> tyreMap = new LinkedHashMap<>();
    private static String randomCarId;
    private static String id;

    private static final String STATIC_USER_TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJCUWxRQmJMVFciLCJpYXQiOjE3NTYyODc1OTcsImV4cCI6MTc4NzgyMzU5N30.oScWjmZlmF2v-qAoXQ3A0-P8QXDXP2CFmDSjDGJ5llcig2cw5DSXX_zVytrWCUjqTiL4NA5qBxxRay4cQ38VgQ";
    private static Map<String, String>  details=new HashMap<>();
    @BeforeClass
    public void setUp() {
        initialization.initializeEnvironment(); // Sets base URI and tokens
        user_id = initialization.franchiseAdminToken.get("userId");
        token = initialization.franchiseAdminToken.get("token");
        ByPassKey = initialization.ByPassKey;

    }
    public static String dealerplus="https://api.staging.myautochek.com";

//    @Test()
//    @Description("Get Car Details to fetch Car ID")
//    @Story("Get Car Details")
//    @Severity(SeverityLevel.NORMAL)
public static String underwriterURL="https://81l3dcfgqe.execute-api.us-east-1.amazonaws.com/stage";
    public static String getCarDetails() {
        System.out.println("Token_Franchise: " + token);
        baseURI = dealerplus;
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
//         car_id = response.jsonPath().getString("carList[3].id");
//        income = Float.parseFloat(response.jsonPath().getString("carList[3].installment")) * 3;
//        //System.out.println("car_id: " + car_id);
//        System.out.println("income: " + income);

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

         // Print all stored data
        System.out.println("CarList Response Map:");
        for (Map.Entry<String, Map<String, String>> entry : tyreMap.entrySet()) {
            System.out.println("Car ID: " + entry.getKey() + " => " + entry.getValue());
        }
        AllureLogs.executeSoftAssertAll(softAssert);
       return randomCarId;
        //return null;
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

    private static String firstName = getRandomFirstName();
    private static String lastName = getRandomLastName();
    private static String phoneNumber = getRandomPhoneNumber();
    private static String Email = getRandomEmail();
    private static String Gender = getRandomGender();
    private static String bvnNumber = getBvnNumber();
    private static String ninNumber = getNinNumber();

    @Test()
    @Description("Creating loan for first fetched Toyota Car")
    @Story("Create Loan")
    @Severity(SeverityLevel.NORMAL)
    public static String postCreateLoan() {
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
        baseURI = dealerplus;
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
        return loan_id;
    }

//    @Test(dependsOnMethods = "postCreateLoan")
//    @Description("Submitting the loan")
//    @Story("Update Loan")
//    @Severity(SeverityLevel.NORMAL)
    public static String putUpdateLoan() throws InterruptedException {

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
                "            \"value\": \"" + details.get("installment") + "\",\n" +
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
        baseURI = dealerplus;
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
return loan_id;

    }
   // @Test()
//    @Description("Fetching loan by ID")
//    @Story("Get Loan By ID")
//    @Severity(SeverityLevel.NORMAL)

    public static  Map<String, Object>  getLoanById() throws InterruptedException {

        Thread.sleep(120000);
        baseURI = dealerplus;
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/v1/origination/loans/"+loan_id+"")
               // .get("/v1/origination/loans/" + loan_id + "")
                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Expected Status : 200");
        String get_response_loan_id = response.jsonPath().getString("id");
        get_loanValue = response.jsonPath().getInt("car.loanValue");
        System.out.println(get_loanValue);
        vin=response.jsonPath().getString("vin");
        List<Map<String, Object>> offers = response.jsonPath().getList("offers");

        Map<String, Map<String, String>> partners = new HashMap<>();

        for (Map<String, Object> offer : offers) {
            String status = (String) offer.get("status");
            if ("PENDING".equalsIgnoreCase(status)) {
                Map<String, Object> partner = (Map<String, Object>) offer.get("partner");

                if (partner != null) {
                    partnerId = (String) partner.get("id");
                     partnerName = (String) partner.get("code");
                    partnerDetails.put("id", partnerId);
                    partnerDetails.put("code", partnerName);

                    break;
                }
            }
        }

        System.out.println("First Pending Partner: " + partnerDetails);

        AllureLogs.executeSoftAssertAll(softAssert);
        return partnerDetails;

    }
    public static final String filePath = "src\\test\\java\\TestData\\bmw.jpg";
    public static final boolean isPublic = true;
    public static  Response uplooad_response;
//@Test()
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
    private List<Map<String, Object>> uploadedDocs = new ArrayList<>();
//@Test(dependsOnMethods = "uploadDocument")
    public void uploadJSONDocuments() {
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
                "      \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
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
                "      \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"dealerInvoice\",\n" +
                "      \"password\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"identity\",\n" +
                "      \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "      \"description\": \"National ID\",\n" +
                "      \"password\": \"\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"name\": \"nameTitle\",\n" +
                "      \"value\": \"Mr\",\n" +
                "      \"type\": \"selection\",\n" +
                "      \"label\": \"Title\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"maritalStatus\",\n" +
                "      \"value\": \"Single\",\n" +
                "      \"type\": \"radio\",\n" +
                "      \"label\": \"Marital status\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"address\",\n" +
                "      \"value\": \"Niger, Nigeria\",\n" +
                "      \"type\": \"google-address\",\n" +
                "      \"label\": \"Address\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"state\",\n" +
                "      \"value\": \"Niger\",\n" +
                "      \"type\": \"selection\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"houseNumber\",\n" +
                "      \"value\": \"111\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"House/Apartment number\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"streetName\",\n" +
                "      \"value\": \"Street 123\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Street name\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"lga\",\n" +
                "      \"value\": \"Bida\",\n" +
                "      \"type\": \"selection\",\n" +
                "      \"label\": \"LGA of residence\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"city\",\n" +
                "      \"value\": \"Kontagora\",\n" +
                "      \"type\": \"selection\",\n" +
                "      \"label\": \"City of residence\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"nextOfKinName\",\n" +
                "      \"value\": \"Test123\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Next of Kin's name\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"nextOfKinAddress\",\n" +
                "      \"value\": \"Test 123456\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Next of Kin's Address\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"nextOfKinTelephone\",\n" +
                "      \"value\": \"2349998887771\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Next of Kin's Phone number\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"nextOfKinEmail\",\n" +
                "      \"value\": \"eshareddy@frugaltestingin.com\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Next of Kin's Email\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"nextOfKinRelationship\",\n" +
                "      \"value\": \"Sister\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"label\": \"Next of Kin's Relationship\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"accountHolderName\",\n" +
                "      \"value\": \"Esha Reddy\",\n" +
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
                "    },\n" +
                "    {\n" +
                "      \"name\": \"sideBusinessBank\",\n" +
                "      \"value\": \"Accion Microfinance Bank\",\n" +
                "      \"type\": \"selection\",\n" +
                "      \"label\": \"Side business bank (Optional)\",\n" +
                "      \"valid\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"sideBusinessAccountType\",\n" +
                "      \"value\": \"Cheque/Current\",\n" +
                "      \"type\": \"selection\",\n" +
                "      \"label\": \"Side business account type (Optional)\",\n" +
                "      \"valid\": true\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
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

        uploadedDocs = response.jsonPath().getList("documents");
        AllureLogs.executeSoftAssertAll(softAssert);
    }

    public void PutApproveDocuments() throws InterruptedException {
        // Build reviews list dynamically

        List<Map<String, String>> reviews = new ArrayList<>();

        for (Map<String, Object> doc : uploadedDocs) {
            Map<String, String> review = new HashMap<>();
            review.put("id", doc.get("id").toString());
            review.put("name", doc.get("name").toString());
            review.put("status", "APPROVED");  // you can make this parameterized if needed
            reviews.add(review);

        }

        // Final payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("reviews", reviews);
        payload.put("loan_id", loan_id); // you can parameterize loan_id


        // Call Approve API
        underWriterToken=initialization.UnderWriterUIUser.get("token");
        System.out.println(underWriterToken);
        Response response = given()
                .header("Authorization", "Bearer " + underWriterToken)

               .header("x-autochek-app","zoho")
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .put("/v2/origination/loan/customerDocuments/review")
                .then()
                .statusCode(200)
                .extract()
                .response();
        System.out.println(underWriterToken);

    }
    public static String documentID;
    public void UploadCustomDocuments()
    {
        String UploadCustomDocumentsPayload="{\n" +
                "    \"documents\": [\n" +
                "        {\n" +
                "            \"name\": \"customs-document\",\n" +
                "            \"file_url\": \""+uplooad_response.jsonPath().get("file.url")+"\",\n" +
                "            \"description\": \"\",\n" +
                "            \"c_number\": \"98765\",\n" +
                "            \"port_of_entry\": \"98764\",\n" +
                "            \"date_of_entry\": \"2025-09-02\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"car_id\": \""+randomCarId+"\",\n" +
                "    \"country\": \"NG\",\n" +
                "    \"loan_id\": \""+loan_id+"\"\n" +
                "}";
        SoftAssert softAssert = new SoftAssert();
        Response res = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .header("x-autochek-app","dealerplus")
                .body(UploadCustomDocumentsPayload)
                .when()
                .post("/v1/inventory/documents")
                .then()
                .log().all()
                .extract()
                .response();

        softAssert.assertEquals(res.statusCode(), 200, "Status code mismatch");
        softAssert.assertAll();
     documentID = res.jsonPath().getString("id");
        System.out.println("Car ID: " + id);
    }

    public void PutApproveCustomDocuments()
    {
        String ApproveCustomDocumentsPayload="{\n" +
                "  \"approval_status\": \"Valid\"\n" +
                "}";
        SoftAssert softAssert = new SoftAssert();
        Response res = given()
                .header("Authorization", "Bearer " + underWriterToken)
                .header("Content-Type", "application/json")
                .header("x-autochek-app","dealerplus")
                .body(ApproveCustomDocumentsPayload)
                .when()
                .put("/v1/inventory/documents/"+documentID+"/status")
                .then()
                .log().all()
                .extract()
                .response();

        softAssert.assertEquals(res.statusCode(), 200, "Status code mismatch");
        softAssert.assertAll();


    }


public static String inspectionIds;
    public void InspectionRequest() {
        // Construct payload

            String Inspection = "{\n" +
                    "    \"year\": 2025,\n" +
                    "    \"vin\": \""+vin+"\",\n" +
                    "    \"country\": \"NG\",\n" +
                    "    \"model_id\": 1891,\n" +
                    "    \"inspection_type\": \"warranty\",\n" +
                    "    \"car_id\": \""+randomCarId+"\",\n" +
                    "    \"state\": \"Abuja\",\n" +
                    "    \"city\": \"Airport Junction Citec\",\n" +
                    "    \"franchise_id\": \"kpMngdaMZ\",\n" +
                    "    \"assignee_id\": \"xgWQ5CfoC\",\n" +
                    "    \"note\": \"PPI\",\n" +
                    "    \"loan_id\": \""+loan_id+"\"\n" +
                    "}";

            Response response =
                    given()
                            .header("Authorization", "Bearer " +token)
                            .header("x-autochek-app","dealerplus")
                            .contentType(ContentType.JSON)
                            .body(Inspection)
                            .when()
                            .post("/v1/inventory/inspection_request")

                            .then()
                            .log().all()
                            .extract().response();

            // ✅ Extract "id" from response JSON
      inspectionIds = response.jsonPath().getString("id");


            SoftAssert softAssert = new SoftAssert();
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status Code");
            AllureLogs.executeSoftAssertAll(softAssert);
        }
    private static String TyreCode="225/60R17";
    private static List<String> TyreCodes=new ArrayList<>();

    public void CreateInspection() {
        inspector_token = Initialization.InspectorAppToken.get("token");
            String payload = "{\n" +
                    "  \"form_id\": \"3\",\n" +
                    "  \"form_data\": \"{\\\"sections\\\":[{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"Toyota\\\",\\\"name\\\":\\\"make\\\"},{\\\"entry\\\":\\\"Camry\\\",\\\"name\\\":\\\"model\\\"},{\\\"entry\\\":\\\"2024\\\",\\\"name\\\":\\\"year\\\"},{\\\"entry\\\":\\\"TOYOTATOYOTATOYOT\\\",\\\"name\\\":\\\"vin\\\"},{\\\"entry\\\":\\\"3200\\\",\\\"name\\\":\\\"odometer\\\"},{\\\"entry\\\":\\\"km\\\",\\\"name\\\":\\\"odometerUnit\\\"},{\\\"entry\\\":\\\"13/08/2025\\\",\\\"name\\\":\\\"inspectionDate\\\"},{\\\"entry\\\":\\\"Daniel inspector qa\\\",\\\"name\\\":\\\"inspectorName\\\"}," +
                    "{\\\"entry\\\":\\\""+TyreCode+"\\\",\\\"name\\\":\\\"tyreCode\\\"}],\\\"name\\\":\\\"intro\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"powerWindows\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"powerWindowsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"powerWindowsImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"powerWindowsComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"sunRoof\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"sunRoofSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"sunRoofImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"sunRoofComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"audioSystem\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"audioSystemSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"audioSystemImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"audioSystemComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"windshieldWipers\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"windshieldWipersSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"windshieldWipersImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"windshieldWipersComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"reverseCameraScreen\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"reverseCameraScreenSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"reverseCameraScreenImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"reverseCameraScreenComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"innerDomeMapLight\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"innerDomeMapLightSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"innerDomeMapLightImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"innerDomeMapLightComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"seatAdjuster\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"seatAdjusterSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"seatAdjusterImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"seatAdjusterComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"seatCooler\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"seatCoolerSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"seatCoolerImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"seatCoolerComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"airConditioning\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"airConditioningSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"airConditioningImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"airConditioningComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"dashboardIndicators\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"dashboardIndicatorsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"dashboardIndicatorsImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"dashboardIndicatorsComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"parkingSensor\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"parkingSensorSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"parkingSensorImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"parkingSensorComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"horn\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"hornSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"hornImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"hornComments\\\"},{\\\"entry\\\":\\\"Good\\\",\\\"name\\\":\\\"electricalObservations\\\"}],\\\"name\\\":\\\"electricals\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"dashboard\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"dashboardSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"dashboardImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"steering\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"steeringSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"steeringImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"seats\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"seatsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"seatsImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"roof\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"roofSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"roofImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"carpet\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"carpetSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"carpetImages\\\"},{\\\"entry\\\":\\\"Fair\\\",\\\"name\\\":\\\"interiorObservations\\\"}],\\\"name\\\":\\\"interior\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"bodyDents\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"bodyDentsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"bodyDentsImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"windshield\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"windshieldSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"windshieldImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"exteriorScratches\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"exteriorScratchesSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"exteriorScratchesImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"headlights\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"headlightsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"headlightsImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"rearAndBrakeLights\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"rearAndBrakeLightsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"rearAndBrakeLightsImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"logosEmblems\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"logosEmblemsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"logosEmblemsImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"rustCorrosion\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"rustCorrosionSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"rustCorrosionImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"exhaust\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"exhaustSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"exhaustImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"tyreTreadDepth\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"tyreTreadDepthSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"tyreTreadDepthImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"paint\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"paintSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"paintImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"exteriorObservations\\\"}],\\\"name\\\":\\\"exterior\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"engineFiring\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"engineFiringSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"engineFiringImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"gearSwitching\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"gearSwitchingSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"gearSwitchingImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"oilLeakage\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"oilLeakageSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"oilLeakageImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"battery\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"batterySeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"batteryImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"serpentineBelt\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"serpentineBeltSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"serpentineBeltImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"engineMounts\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"engineMountsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"engineMountsImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"catalyticConverter\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"catalyticConverterSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"catalyticConverterImages\\\"},{\\\"entry\\\":\\\"Good \\\",\\\"name\\\":\\\"engineAndTransmissionObservations\\\"}],\\\"name\\\":\\\"engineAndTransmission\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"available\\\",\\\"name\\\":\\\"jack\\\"},{\\\"entry\\\":\\\"available\\\",\\\"name\\\":\\\"wheelSpanner\\\"},{\\\"entry\\\":\\\"available\\\",\\\"name\\\":\\\"spareTyre\\\"},{\\\"entry\\\":\\\"available\\\",\\\"name\\\":\\\"spareKey\\\"},{\\\"entry\\\":\\\"available\\\",\\\"name\\\":\\\"customsPaper\\\"}],\\\"name\\\":\\\"accessories\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"okay\\\",\\\"name\\\":\\\"radiatorWaterCoolant\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"radiatorWaterCoolantImages\\\"},{\\\"entry\\\":\\\"okay\\\",\\\"name\\\":\\\"brakeFluid\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"brakeFluidImages\\\"},{\\\"entry\\\":\\\"okay\\\",\\\"name\\\":\\\"steeringFluid\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"steeringFluidImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"transmissionFluid\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"transmissionFluidImages\\\"},{\\\"entry\\\":\\\"okay\\\",\\\"name\\\":\\\"engineOil\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"engineOilImages\\\"}],\\\"name\\\":\\\"fluidLevels\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"no\\\",\\\"name\\\":\\\"includeObds\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"scanReport\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"searchObds\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"obds\\\"}],\\\"name\\\":\\\"obd\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"\\\",\\\"media\\\":[{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"leftFront\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/G245kHiS.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"rightFront\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/MjgXon5t.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"front\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/u4Gtaemd.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"back\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/f58g7yD9.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"leftBack\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/f53SSP7H.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"rightBack\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/McfMM26l.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"interiorFront\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/gBmVZz7t.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"interiorBack\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/yjr712tx.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"vinImage\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/yMk6su0P.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"tyreImage\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/nVs5tzyW.webp\\\"}],\\\"name\\\":\\\"snapshots\\\"}],\\\"name\\\":\\\"carSnapshots\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"yes\\\",\\\"name\\\":\\\"autoFailure\\\"}],\\\"name\\\":\\\"outro\\\"}]}\",\n" +
                    "  \"car_id\": \""+randomCarId+"\",\n" +
                    "  \"inspection_request_summary_id\": \"O9ZDCvr8C\",\n" +
                    "  \"inspection_request_id\": \""+inspectionIds+"\",   \n" +
                    "  \"location\": \"lat:-1.264334,long:36.8068715\",\n" +
                    "  \"inspection_type\": \"warranty\",\n" +
                    "  \"completed\": true,\n" +
                    "  \"wheel_type\": \"2WD\",\n" +
                    "  \"id\": \"3\",\n" +
                    "  \"country\": \"NG\"\n" +
                    "}";
            Response response = given()
                    .header("Authorization", "Bearer " + inspector_token)
                    .header("x-autochek-app","workshop")
                    .header("Content-Type","application/json")
                    .body(payload)
                    .when()
                    .post("/v1/inspection")
                    .then()
                    .log().all()
                    .extract().response();
            SoftAssert softAssert = new SoftAssert();
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status code");

            AllureLogs.executeSoftAssertAll(softAssert);
        }


}
