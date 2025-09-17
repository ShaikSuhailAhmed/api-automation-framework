package autochek_api_positive.OriginationService;
import Base.Initialization;
import Listener.AllureLogs;
import Utils.Postive_Data_Extractor;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;
import java.util.*;

import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.given;
@Epic("API Automation")
@Feature("Postive Origination Service")
@Story("")
public class OriginationServicePositive2 {
    Initialization initialization = new Initialization();
    private static String user_id;
    private static String token;
    private static final String[] Countries = {"KE", "NG"};
    private static final Random random = new Random();
    private static final String CountryCode = "NG";// Countries[random.nextInt(Countries.length)];
    private static final String product_id = "hj43BXLXe";
    private static String car_id;
    private static String loan_id;
    private static String url;
    private static final Map<String, Map<String, String>> depositMap = new LinkedHashMap<>();
    private static String income="10000000";
    private static String inspector_token;
    private static Map<String, Object> car;
    private static Map<String, Object> danielInspector = null;
    private static String InspectionId;
    private static List<String> createdDepositIds = new ArrayList<>();
    @BeforeClass
    public void setUp() {
        initialization.initializeEnvironment(); // Sets base URI and tokens
        user_id = Initialization.franchiseAdminToken.get("userId");
        token = Initialization.franchiseAdminToken.get("token");
    }

    public static String getRandomFirstName() {
        return "API" + UUID.randomUUID().toString().substring(0, 4);
    }

    public static String getRandomLastName() {
        return "Testing" + UUID.randomUUID().toString().substring(0, 4);
    }

    public static String getRandomPhoneNumber() {
        Random rand = new Random();
        // Nigerian number format: 23480XXXXXXX -> 234 + 80X + 7-digit number
        int operatorDigit = rand.nextInt(10); // 0-9 for the third digit
        int subscriberNumber = 1000000 + rand.nextInt(9000000); // 7-digit number
        return "2" + operatorDigit + subscriberNumber;
    }


    public static String getRandomEmail() {
        return "FrugalAutomation" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
    }

    public static String getRandomGender() {
        return (new Random().nextBoolean()) ? "Male" : "Female";
    }

    String firstName = getRandomFirstName();
    String lastName = getRandomLastName();
    String phoneNumber = getRandomPhoneNumber();
    String Email = getRandomEmail();
    String Gender = getRandomGender();

    @Test()
    public void postCreateLoan() {

        String requestBody = "{\n" +
                "    \"profile\": [\n" +
                "        \n" +
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
                "            \"value\": \"" + income + "\",\n" +
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
                "    \"car_id\": \"" + car_id + "\",\n" +
                "    \"user_id\": \"" + user_id + "\",\n" +
                "    \"product_id\":  \"hj43BXLXe\",\n" +
                "    \"country\": \"" + CountryCode + "\",\n" +
                "    \"is_draft\": true\n" +
                "}";

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
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
                "    \"car_id\": \"0zV7bPoNG\",\n" +
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

    File imageFile = new File("src/test/java/TestData/bmw.jpg");
    public static final boolean isPublic = true;
    public static final String mimeType = "image/jpeg";
    public static Response uplooad_response;

    @Test()
    public void uploadDocument() {
        // Prepare request body
        Map<String, Object> fileData = new HashMap<>();
        fileData.put("originalName", imageFile.getName()); // only "bmw.jpg"
        fileData.put("mimeType", mimeType);
        fileData.put("public", isPublic);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("files", Collections.singletonList(fileData));

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/v1/storage/write-urls")
                .then()
                .log().all()
                .extract().response();
        uplooad_response = response;
        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "status");
        url = json.getString("urls[0].url");
        AllureLogs.softAssertNotNull(softAssert, json.getString("urls[0].originalName"), "originalName should not be null");
        AllureLogs.softAssertNotNull(softAssert, json.getString("urls[0].name"), "name should not be null");
        AllureLogs.softAssertNotNull(softAssert, json.getString("urls[0].tempFilePath"), "tempFilePath should not be null");
        AllureLogs.softAssertNotNull(softAssert, json.getString("urls[0].url"), "url should not be null");
        AllureLogs.softAssertNotNull(softAssert, json.getString("urls[0].uploadURL"), "uploadURL should not be null");
        AllureLogs.softAssertNotNull(softAssert, json.getString("urls[0].id"), "id should not be null");
        // Optional value match validations
        AllureLogs.softAssertEquals(softAssert, json.getString("urls[0].originalName"), imageFile.getName(), "originalName mismatch");

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    String loan_id1="QW-QmWTBU";

    private static String dealername="100% Automobile Nig.";
    private static String paid_to="Paid_to_Dealer";
    @Test(dataProvider = "Upload Deposit")
    @Description("Submit deposit payment proof Submission with valid data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Deposit Payment Proof Submission")
    @Parameters({"type", "severity", "testDescription", "amount_paid", "password", "expectedStatusCodeStr"})
    public void UploadDeposit(String type, String severity, String testDescription,String amount_paid,String password,String expectedStatusCodeStr) {
        uploadDocument();
        SoftAssert softAssert = new SoftAssert();
        int expectedStatusCode = Integer.parseInt(expectedStatusCodeStr.trim());

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("loan_id", "QW-QmWTBU");
        requestBody.put("amount_paid", amount_paid);
        requestBody.put("payment_proof_url", url);
        requestBody.put("paid_to", paid_to);
        requestBody.put("dealer_name", dealername);
        System.out.println(amount_paid);
        // Send POST request
        Response response = given()
                .header("X-autochek-app", "dealerplus")
                .header("Content-Type","application/json")
                .header("Authorization","Bearer "+token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/v2/origination/loans/"+loan_id1+"/equity-deposits") // <-- change to your actual API path
                .then()
                .log().all()
                .extract().response();

        // Validate status code
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), expectedStatusCode, "Expected Status Code");

        JsonPath json = response.jsonPath();

        // Basic null checks

        AllureLogs.softAssertNotNull(softAssert, json.get("success"), "success should not be null");
        AllureLogs.softAssertNotNull(softAssert, json.getString("depositId"), "depositId should not be null");

        String depositId = json.getString("depositId");
        createdDepositIds.add(depositId);

        // Finish assertions
        Map<String, String> depositPayload = new HashMap<>();
        depositPayload.put("loanid", loan_id1);
        depositPayload.put("amountPaid", amount_paid);
        depositPayload.put("payment_proofUrl", url);
        depositPayload.put("paid_to", paid_to);
        depositPayload.put("dealer_name", dealername);
        depositMap.put(depositId, depositPayload);
        AllureLogs.executeSoftAssertAll(softAssert);
        System.out.println(depositMap);
    }

    @DataProvider(name = "Upload Deposit")
    public Object[][] UploadFile() {
        return Postive_Data_Extractor.ExcelData("Upload Deposit");
    }
    @Test()
    @Description("Get List of deposits")
    @Severity(SeverityLevel.CRITICAL)
    @Story("List of Deposits ")
    public void GetListDeposits() {

        SoftAssert softAssert = new SoftAssert();
        Response response = given()
                .header("X-autochek-app", "dealerplus")
                .header("Content-Type","application/json")
                .header("Authorization","Bearer "+token)
                .contentType(ContentType.JSON)
                .queryParam("loan_id", "QW-QmWTBU")
                .when()
                .get("/v2/origination/loans/"+loan_id1+"/equity-deposits") // <-- change to your actual API path
                .then()
                .log().all()
                .extract().response();

        // Validate status code
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Expected Status Code");


        JsonPath json = response.jsonPath();

        // Extract all IDs from the deposits list
        List<String> existingIds = json.getList("deposits.id");
        System.out.println("existingIds : " + existingIds);
        // Verify each created deposit ID exists in the list
        for (String id : createdDepositIds) {
            softAssert.assertTrue(existingIds.contains(id), "Deposit ID not found in list: " + id);
        }
        // ✅ Extract deposits list from API
        List<Map<String, Object>> depositsList = json.getList("deposits");

        // ✅ Validate each deposit from depositMap
        for (Map.Entry<String, Map<String, String>> entry : depositMap.entrySet()) {
            String depositId = entry.getKey();
            Map<String, String> expectedData = entry.getValue();

            // Find matching deposit in API response
            Map<String, Object> actualDeposit = depositsList.stream()
                    .filter(dep -> depositId.equals(dep.get("id")))
                    .findFirst()
                    .orElse(null);
            System.out.println(actualDeposit);
            softAssert.assertNotNull(actualDeposit, "Deposit ID not found in list: " + depositId);

            if (actualDeposit != null) {
                // loanId check
                AllureLogs.softAssertEquals(softAssert,
                        String.valueOf(actualDeposit.get("loanId")),
                        expectedData.get("loanid"),
                        "Loan ID mismatch for deposit: " + depositId);

                // amountPaid check (handle string/int mismatch)
                String actualAmount = String.valueOf(actualDeposit.get("amountPaid"));
                String expectedAmount = String.valueOf(expectedData.get("amountPaid"));
                AllureLogs.softAssertEquals(softAssert,
                        actualAmount,
                        expectedAmount,
                        "Amount Paid mismatch for deposit: " + depositId);

                // paymentProofUrl check
                AllureLogs.softAssertEquals(softAssert,
                        String.valueOf(actualDeposit.get("paymentProofUrl")),
                        expectedData.get("payment_proofUrl"),
                        "Payment Proof URL mismatch for deposit: " + depositId);

                // paidTo check
                AllureLogs.softAssertEquals(softAssert,
                        String.valueOf(actualDeposit.get("paidTo")),
                        expectedData.get("paid_to"),
                        "Paid To mismatch for deposit: " + depositId);

                // dealerName check
                AllureLogs.softAssertEquals(softAssert,
                        String.valueOf(actualDeposit.get("dealerName")),
                        expectedData.get("dealer_name"),
                        "Dealer Name mismatch for deposit: " + depositId);
            }
        }
        // Finish assertions
        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @Test(dependsOnMethods = "UploadDeposit")
    @Description("Fetch deposit details by loan_id and deposit_id for all stored deposits")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Deposit By ID")
    public void GetDepositByID() {
        SoftAssert softAssert = new SoftAssert();

        // Iterate through each deposit entry from the map
        for (Map.Entry<String, Map<String, String>> entry : depositMap.entrySet()) {
            String depositId = entry.getKey();
            String loanId = entry.getValue().get("loanid");

            // Send GET request
            Response response = given()
                    .header("X-autochek-app", "dealerplus")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .get("/v2/origination/loans/" + loanId + "/equity-deposits/" + depositId)
                    .then()
                    .log().all()
                    .extract().response();

            // Validate status code
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200,
                    "Expected Status Code for depositId: " + depositId);

            JsonPath json = response.jsonPath();

            // Validate top-level fields
            AllureLogs.softAssertNotNull(softAssert, json.getString("deposit.loanId"), "loanId should not be null");
            AllureLogs.softAssertEquals(softAssert, json.getString("deposit.loanId"), loanId,
                    "loanId should match the one in depositMap");

            AllureLogs.softAssertEquals(softAssert, json.getString("deposit.id"), depositId,
                    "depositId should match the one from depositMap");

            AllureLogs.softAssertNotNull(softAssert, json.get("deposit.amountPaid"), "amountPaid should not be null");
            AllureLogs.softAssertNotNull(softAssert, json.getString("deposit.paymentProofUrl"), "paymentProofUrl should not be null");
            AllureLogs.softAssertNotNull(softAssert, json.getString("deposit.paidTo"), "paidTo should not be null");
            AllureLogs.softAssertNotNull(softAssert, json.getString("deposit.dealerName"), "dealerName should not be null");
            AllureLogs.softAssertNotNull(softAssert, json.getString("deposit.createdAt"), "createdAt should not be null");
            AllureLogs.softAssertNotNull(softAssert, json.getString("deposit.updatedAt"), "updatedAt should not be null");

            // Optionally validate message
            AllureLogs.softAssertEquals(softAssert, json.getString("message"),
                    "Equity Deposit fetched successfully", "Response message check");
        }

        AllureLogs.executeSoftAssertAll(softAssert);
    }
}
