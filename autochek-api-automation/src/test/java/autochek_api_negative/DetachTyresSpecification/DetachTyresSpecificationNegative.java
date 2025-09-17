package autochek_api_negative.DetachTyresSpecification;

import Base.Initialization;
import Listener.AllureLogs;
import Utils.Negative_Data_Extractor;
import Utils.Postive_Data_Extractor;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.util.*;

import static io.restassured.RestAssured.given;

@Epic("Detach Tyre Specification")
@Feature("Negative Tests Detach Tyre Specification")
public class DetachTyresSpecificationNegative {
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
    private static String fileUrl;


    @BeforeClass
    public void setUp() {
        initialization.initializeEnvironment(); // Sets base URI and tokens
        user_id = Initialization.franchiseAdminToken.get("userId");
        token = Initialization.franchiseAdminToken.get("token");
        inspector_token=Initialization.InspectorAppToken.get("token");
        ByPassKey = Initialization.ByPassKey;
    }

    public static final String filePath = "C:\\Users\\ravit\\Downloads\\autochek-api-automation 9\\autochek-api-automation\\src\\test\\java\\TestData\\bmw.jpg";
    public static final boolean isPublic = true;
    public static Response uplooad_response;
    @Test(priority = 1)
    public void UploadImage() {
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .multiPart("file", new File(filePath))
                //  .formParam("public", isPublic)
                .when()
                .post("/image/upload")
                .then()
                .log().all()
                .extract().response();
        uplooad_response = response;
        // Validation
        JsonPath json = response.jsonPath();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert,response.getStatusCode(),201,"status");

// Get file URL
        fileUrl = json.getString("file.url");

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    private static  String vin2 ;
    // 🔹 Generate unique Engine Number (< 10 digits)
    private static String engineNumber; // max 9 digits
  //  private static List<String> carId=new ArrayList<>();
    // Put into payload
    private static String carId;
    private static String vin1;
    @Test(priority = 2,dataProvider = "Create Car")
    @Description("Create Car and Validate Response")
    @Story("Create Car")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type","severity","testDescription","owner_id","year","mileage","mileage_unit","vin1","model_id","country","price","car_manager_id",
            "engine_type","fuel_type","transmission","trim_id","selling_condition","body_type_id",
            "interior_color","exterior_color","franchise_id","created_by","cc_measurement","tyre_code",
            "is_draft","expectedStatusCode"})
    public void CreateCar(String type,String severity,String testDescription,String owner_id, String year, String mileage, String mileage_unit,String vin1, String model_id,
                          String country, String price, String car_manager_id, String engine_type,
                          String fuel_type, String transmission, String trim_id, String selling_condition,
                          String body_type_id, String interior_color, String exterior_color, String franchise_id,String imageUrl,
                          String created_by, String cc_measurement, String tyre_code, String is_draft,
                          String expectedStatusCodeStr){
        int expectedStatusCode = Integer.parseInt(expectedStatusCodeStr.trim());
    vin2= String.format("%017d", Math.abs(random.nextLong()) % 100000000000000000L);
        System.out.println(vin2);
        // Construct payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("owner_id", owner_id);
        payload.put("year", year);
        payload.put("mileage", mileage);
        payload.put("mileage_unit", mileage_unit);
        if (vin1 != null && !vin1.trim().equalsIgnoreCase("null") && !vin1.trim().isEmpty()) {
            payload.put("vin", vin1);  // use VIN from sheet
        } else {
            payload.put("vin", vin2);  // auto-generate unique VIN
        }
        payload.put("model_id", model_id);
        payload.put("country", country);
        payload.put("price", price);
        payload.put("car_manager_id", car_manager_id);
        payload.put("engine_type", engine_type);
        payload.put("fuel_type", fuel_type);
        payload.put("transmission", transmission);
        payload.put("trim_id", trim_id);
        payload.put("selling_condition", selling_condition);
        payload.put("body_type_id", body_type_id);
        payload.put("interior_color", interior_color);
        payload.put("exterior_color", exterior_color);
        payload.put("franchise_id", franchise_id);
        if (imageUrl != null && !imageUrl.trim().equalsIgnoreCase("null") && !imageUrl.trim().isEmpty()) {
            payload.put("image_url", imageUrl);
        } else {
            payload.put("image_url", fileUrl);
        }
        payload.put("created_by", created_by);
        payload.put("cc_measurement",cc_measurement);
        payload.put("tyre_code", tyre_code);
        payload.put("is_draft", is_draft);

        // API Call
        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .contentType(ContentType.JSON)
                        .body(payload)
                        .when()
                        .post("/v1/inventory/car")
                        .then()
                        .log().all()
                        .extract().response();

        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), expectedStatusCode, "Expected Status Code");
        JsonPath json = response.jsonPath();
        carId = json.getString("id");   // get the ID from this response

        vin2=json.getString("vin");
        System.out.println(vin2);
        // Execute all validations
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Create Car")
    public Object[][] CreateCar() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Create Car Negative 2");
    }

    @Test(dependsOnMethods = "CreateCar", priority = 3, dataProvider = "Update Car")
    @Description("Update Car and Validate Response")
    @Story("Update Car")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "Severity", "testDescription", "license_plate", "city", "state", "address", "owner_type", "is_upgraded","engineNumber1","created_by","Tyre_code","isDraft", "expectedStatusCode"})
    public void PutUpdateCar(String type, String Severity, String testDescription, String license_plate, String city, String state, String address,
                          String owner_type, String is_upgraded,String engineNumber1,String created_by,String Tyre_code, String isDraft, String expectedStatusCodeStr) {
        int expectedStatusCode = Integer.parseInt(expectedStatusCodeStr.trim());

            engineNumber = String.valueOf(new Random().nextInt(999999999));
            Map<String, Object> payload = new HashMap<>();
            payload.put("license_plate", license_plate);
            payload.put("city", city);
            payload.put("state", state);
            payload.put("address", address);
            payload.put("owner_type", owner_type);
            payload.put("is_upgraded", is_upgraded);
        if (engineNumber1 != null && !engineNumber1.trim().equalsIgnoreCase("null") && !engineNumber1.trim().isEmpty()) {
            payload.put("engine_number", engineNumber1);
        } else {
            payload.put("engine_number", engineNumber);
        }
        //    payload.put("engine_number", engineNumber);
            payload.put("created_by", created_by);
            payload.put("tyre_code", Tyre_code);
            // conditionally validated
            payload.put("is_draft", isDraft);

            Response response =
                    given()
                            .header("Authorization", "Bearer " + Initialization.franchiseAdminToken.get("token"))
                            .contentType(ContentType.JSON)
                            .body(payload)
                            .when()
                            .put("/v1/inventory/car/" + carId)
                            .then()
                            .log().all()
                            .extract().response();

            SoftAssert softAssert = new SoftAssert();
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), expectedStatusCode, "Status Code");
        AllureLogs.executeSoftAssertAll(softAssert);


    }

    @DataProvider(name = "Update Car")
    public Object[][] PutUpdateCar() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("Update Car Negative");
    }


    @Test(priority = 3)
    @Description("Update Car and Validate Response")
    @Story("Complete Car")
    @Severity(SeverityLevel.CRITICAL)
    public void PutCompleteCar() {
        // Construct payload
        String completeCarPayload="{\n" +
                "    \"summary\": \"TEST123\",\n" +
                "    \"form_id\": 2,\n" +
                "    \"form_data\": \"{\\\"name\\\":\\\"createDealerCar\\\",\\\"label\\\":\\\"Create Dealer Car\\\",\\\"sections\\\":[{\\\"name\\\":\\\"carDetailsRequired\\\",\\\"items\\\":[{\\\"name\\\":\\\"country\\\",\\\"entry\\\":\\\"NG\\\"},{\\\"name\\\":\\\"isDealer\\\",\\\"entry\\\":\\\"true\\\"},{\\\"name\\\":\\\"ownerId\\\",\\\"entry\\\":\\\"-lh7_mmKJ\\\"},{\\\"name\\\":\\\"createdBy\\\",\\\"entry\\\":\\\"BQlQBbLTW\\\"},{\\\"name\\\":\\\"franchise\\\",\\\"entry\\\":\\\"100% Automobile Nig.\\\"},{\\\"name\\\":\\\"franchiseId\\\",\\\"entry\\\":\\\"kpMngdaMZ\\\"},{\\\"name\\\":\\\"carCondition\\\",\\\"entry\\\":\\\"new\\\"},{\\\"name\\\":\\\"vin\\\",\\\"entry\\\":\\\"\\\",\\\"comment\\\":\\\"The (**) fields will be auto-populated based on the VIN\\\"},{\\\"name\\\":\\\"year\\\",\\\"entry\\\":\\\"2025\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"make\\\",\\\"entry\\\":\\\"Toyota\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"makeId\\\",\\\"entry\\\":\\\"106\\\"},{\\\"name\\\":\\\"model\\\",\\\"entry\\\":\\\"Corolla\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"modelId\\\",\\\"entry\\\":\\\"1891\\\"},{\\\"name\\\":\\\"trim\\\",\\\"entry\\\":\\\"\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"trimId\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"price\\\",\\\"entry\\\":\\\"909900\\\"},{\\\"name\\\":\\\"bodyType\\\",\\\"entry\\\":\\\"Coupe\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"bodyTypeId\\\",\\\"entry\\\":\\\"7\\\"},{\\\"name\\\":\\\"fuelType\\\",\\\"entry\\\":\\\"petrol\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"engineType\\\",\\\"entry\\\":\\\"2-cylinder(I2)\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"interiorColor\\\",\\\"entry\\\":\\\"Blue\\\"},{\\\"name\\\":\\\"exteriorColor\\\",\\\"entry\\\":\\\"Green\\\"},{\\\"name\\\":\\\"mileage\\\",\\\"entry\\\":\\\"2555\\\"},{\\\"name\\\":\\\"mileageUnit\\\",\\\"entry\\\":\\\"km\\\"},{\\\"name\\\":\\\"transmission\\\",\\\"entry\\\":\\\"automatic\\\"},{\\\"name\\\":\\\"engineCc\\\",\\\"entry\\\":\\\"1\\\"},{\\\"name\\\":\\\"carThumbnail\\\",\\\"entry\\\":\\\"https://media.autochek.africa/file/w_900,q_100/z7LmywTx.webp\\\",\\\"comment\\\":\\\"This is the main image that appears in search results and attracts customers to click on your listing\\\"},{\\\"name\\\":\\\"imageUrl\\\",\\\"entry\\\":\\\"https://media.autochek.africa/file/w_900,q_100/z7LmywTx.webp\\\"},{\\\"name\\\":\\\"carDetailsRequiredSave\\\"},{\\\"name\\\":\\\"carDetailsRequiredSubmit\\\"}]},{\\\"name\\\":\\\"carDetailsOptional\\\",\\\"items\\\":[{\\\"name\\\":\\\"ownershipStatus\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"registrationStatus\\\",\\\"entry\\\":\\\"no\\\"},{\\\"name\\\":\\\"plateNumber\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"engineNumber\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"registrationSeries\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"state\\\",\\\"entry\\\":\\\"test\\\"},{\\\"name\\\":\\\"stateId\\\",\\\"entry\\\":309},{\\\"name\\\":\\\"city\\\",\\\"entry\\\":\\\"test\\\"},{\\\"name\\\":\\\"location\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"cityId\\\",\\\"entry\\\":3251},{\\\"name\\\":\\\"isUpgraded\\\",\\\"entry\\\":\\\"no\\\"},{\\\"name\\\":\\\"upgradedFrom\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"upgradedTo\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"carDetailsRequiredSave\\\"},{\\\"name\\\":\\\"carDetailsRequiredSubmit\\\"}]},{\\\"name\\\":\\\"carSnapshots\\\",\\\"items\\\":[{\\\"name\\\":\\\"snapshots\\\",\\\"entry\\\":[{\\\"name\\\":\\\"leftFront\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/w_900,q_100/SclbGR99.webp\\\"}]},{\\\"name\\\":\\\"carImagesSave\\\"},{\\\"name\\\":\\\"carImagesSubmit\\\"}]},{\\\"name\\\":\\\"carDocuments\\\",\\\"items\\\":[{\\\"name\\\":\\\"documents\\\",\\\"entry\\\":[]},{\\\"name\\\":\\\"carDocumentsSave\\\"},{\\\"name\\\":\\\"carDocumentsSubmit\\\"}]},{\\\"name\\\":\\\"carFeatures\\\",\\\"items\\\":[{\\\"name\\\":\\\"searchFeatures\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"features\\\",\\\"entry\\\":[\\\"1\\\",\\\"2\\\",\\\"3\\\",\\\"28\\\",\\\"39\\\",\\\"44\\\",\\\"66\\\",\\\"67\\\",\\\"68\\\",\\\"69\\\",\\\"70\\\",\\\"71\\\",\\\"72\\\",\\\"73\\\",\\\"74\\\",\\\"75\\\",\\\"76\\\",\\\"77\\\",\\\"78\\\",\\\"79\\\",\\\"80\\\",\\\"81\\\",\\\"82\\\",\\\"83\\\",\\\"84\\\",\\\"85\\\",\\\"86\\\",\\\"87\\\",\\\"88\\\"]},{\\\"name\\\":\\\"carFeaturesSave\\\"},{\\\"name\\\":\\\"carFeaturesSubmit\\\"}]},{\\\"name\\\":\\\"carSummary\\\",\\\"items\\\":[{\\\"name\\\":\\\"summary\\\",\\\"entry\\\":\\\"TEST123\\\"},{\\\"name\\\":\\\"carSummarySave\\\"},{\\\"name\\\":\\\"submit\\\"}]}]}\",\n" +
                "    \"is_draft\": false\n" +
                "}";
            Response response =
                    given()
                            .header("Authorization", "Bearer " + Initialization.franchiseAdminToken.get("token"))
                            .contentType(ContentType.JSON)
                            .body(completeCarPayload)
                            .when()
                            .put("/v1/inventory/car/" + carId)
                            .then()
                            .log().all()
                            .extract().response();

            SoftAssert softAssert = new SoftAssert();
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status Code");

            JsonPath json = response.jsonPath();

    }


    private static String inspection_id;
    @Test( priority = 4)
    @Description("Update Car and Validate Response")
    @Story("Update Car")
    @Severity(SeverityLevel.CRITICAL)
    //@Parameters({"type", "Severity", "testDescription", "license_plate", "city", "state", "address", "owner_type", "is_upgraded", "expectedStatusCode"})
    public void InspectionRequest() {
        // Construct payload

            String Inspection = "{\n" +
                    "    \"vin\": \"" + vin2 + "\",\n" +
                    "    \"model_id\": 1891,\n" +
                    "    \"country\": \"NG\",\n" +
                    "    \"inspection_type\": \"dealer-regular\",\n" +
                    "    \"source\": \"franchise\",\n" +
                    "    \"franchise_id\": \"kpMngdaMZ\"\n" +
                    "}";

            Response response =
                    given()
                            .header("Authorization", "Bearer " + Initialization.franchiseAdminToken.get("token"))
                            .header("x-autochek-app","dealerplus")
                            .contentType(ContentType.JSON)
                            .body(Inspection)
                            .when()
                            .post("/v1/inventory/inspection_request")

                            .then()
                            .log().all()
                            .extract().response();

            // ✅ Extract "id" from response JSON
        inspection_id = response.jsonPath().getString("id");
           // inspectionIds.add(inspectionId);

            SoftAssert softAssert = new SoftAssert();
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status Code");
            AllureLogs.executeSoftAssertAll(softAssert);
        }

    @Test(dataProvider = "Get TyreSpecs",priority = 5)
    @Description("Get TyreSpecs Details by Car ID")
    @Story("Get TyreSpecs")
    @Severity(SeverityLevel.NORMAL)
    @Parameters({"type","severity","testDescription","car_id","expectedStatusCode"})
    public void GetTyreSpecs(String type, String severity, String testDescription,
                             String car_id, String expectedStatusCodeStr){
        int expectedStatusCode = Integer.parseInt(expectedStatusCodeStr.trim());

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/v1/inventory/car/"+car_id+"/tyre-specs")

                .then()
                .log().all()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), expectedStatusCode, "Status code");

        // Execute all assertions
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get TyreSpecs")
    public Object[][] GetTyreSpecs() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Negative_Data_Extractor.ExcelData("TyreSpecs Negative");
    }

    private static String TyreCode="265/65R17";
    private static List<String> TyreCodes=new ArrayList<>();
    @Test(priority = 6,dataProvider = "Create Inpsection")
    @Description("Create Inspection for the carID generated in Car Creation")
    @Story("Create Inspection")
    @Severity(SeverityLevel.NORMAL)
    public void PostCreateInspection(String type, String severity, String testDescription, String form_id, String car_id, String inspection_request_summary_id, String inspection_request_id, String location, String inspection_type, String completed, String wheel_type, String id, String country, String tyreCode, String expectedStatusCode) {
        String payload = "{\n" +
                "  \"form_id\": \"" + form_id + "\",\n" +
                "  \"form_data\": \"{\\\"sections\\\":[{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"Toyota\\\",\\\"name\\\":\\\"make\\\"},{\\\"entry\\\":\\\"Camry\\\",\\\"name\\\":\\\"model\\\"},{\\\"entry\\\":\\\"2024\\\",\\\"name\\\":\\\"year\\\"},{\\\"entry\\\":\\\"TOYOTATOYOTATOYOT\\\",\\\"name\\\":\\\"vin\\\"},{\\\"entry\\\":\\\"3200\\\",\\\"name\\\":\\\"odometer\\\"},{\\\"entry\\\":\\\"km\\\",\\\"name\\\":\\\"odometerUnit\\\"},{\\\"entry\\\":\\\"13/08/2025\\\",\\\"name\\\":\\\"inspectionDate\\\"},{\\\"entry\\\":\\\"Daniel inspector qa\\\",\\\"name\\\":\\\"inspectorName\\\"}," +
                "{\\\"entry\\\":\\\"" + tyreCode + "\\\",\\\"name\\\":\\\"tyreCode\\\"}],\\\"name\\\":\\\"intro\\\"}]}\",\n" +
                "  \"car_id\": \"" + car_id + "\",\n" +
                "  \"inspection_request_summary_id\": \"" + inspection_request_summary_id + "\",\n" +
                "  \"inspection_request_id\": \"" + inspection_id + "\",\n" +
                "  \"location\": \"" + location + "\",\n" +
                "  \"inspection_type\": \"" + inspection_type + "\",\n" +
                "  \"completed\": \"" + completed + "\",\n" +
                "  \"wheel_type\": \"" + wheel_type + "\",\n" +
                "  \"id\": \"" + id + "\",\n" +
                "  \"country\": \"" + country + "\"\n" +
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
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 400, "Status code");

            AllureLogs.executeSoftAssertAll(softAssert);
        }
@DataProvider(name = "Create Inpsection")
public Object[][] PostCreateInspection() {
    // Reads test data from Excel sheet named "Create Franchise"
    return Negative_Data_Extractor.ExcelData("Create Inspection Negative");
}

}
