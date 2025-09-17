package autochek_api_positive.DetachTyreSpecification;

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
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@Epic("Detach Tyre Specification")
@Feature("Positive Tests Detach Tyre Specification")
public class DetachTyreSpecificationPositive {
    // private static final Logger log = LoggerFactory.getLogger(DetachTyreSpecificationPositive.class);
    Initialization initialization = new Initialization();
    private static List<String> offerIds = new ArrayList<>();
    private static final String[] Countries = {"KE", "NG"};
    private static final Random random = new Random();
    private static String user_id;
    private static String ByPassKey;
    private static String token;
    private static String InspectionToken;
    private static int get_loanValue;
    private static final String CountryCode = "NG";// Countries[random.nextInt(Countries.length)];
    private static final String product_id = "hj43BXLXe";
    // private static List<String> car_id=new ArrayList<>();
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
    private static Map<String, String> entry = new HashMap<>();
    private static final String STATIC_USER_TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJCUWxRQmJMVFciLCJpYXQiOjE3NTYyODc1OTcsImV4cCI6MTc4NzgyMzU5N30.oScWjmZlmF2v-qAoXQ3A0-P8QXDXP2CFmDSjDGJ5llcig2cw5DSXX_zVytrWCUjqTiL4NA5qBxxRay4cQ38VgQ";
    private static String fileUrl;

    @BeforeClass
    public void setUp() {
        initialization.initializeEnvironment(); // Sets base URI and tokens
        user_id = Initialization.franchiseAdminToken.get("userId");
        token = Initialization.franchiseAdminToken.get("token");
        ByPassKey = Initialization.ByPassKey;
        inspector_token = Initialization.InspectorAppToken.get("token");
    }


    public static final String filePath = "C:\\Users\\ravit\\Downloads\\autochek-api-automation 9\\autochek-api-automation\\src\\test\\java\\TestData\\bmw.jpg";
    public static final boolean isPublic = true;
    public static Response uplooad_response;

    @Test(priority = 1)
    @Description("Upload Car Image")
    @Story("Upload Car Image")
    @Severity(SeverityLevel.CRITICAL)
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
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 201, "status");

// Get file URL
        fileUrl = json.getString("file.url");

        AllureLogs.executeSoftAssertAll(softAssert);
    }

    // Random random = new Random();
    private static String vin;
    // 🔹 Generate unique Engine Number (< 10 digits)
    private static String engineNumber; // max 9 digits

    // Put into payload
    private static List<String> carId = new ArrayList<>();
    private static List<String> vinNumber = new ArrayList<>();


    @Test(priority = 2, dataProvider = "Create Car")
    @Description("Create Car and Validate Response")
    @Story("Create Car")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "Severity", "testDescription", "owner_id", "year", "mileage", "mileage_unit", "model_id", "country", "price", "car_manager_id",
            "engine_type", "fuel_type", "transmission", "trim_id", "selling_condition", "body_type_id",
            "interior_color", "exterior_color", "franchise_id", "created_by", "cc_measurement", "tyre_code",
            "is_draft", "expectedStatusCode"})
    public void CreateCar(String type, String Severity, String testDescription, String owner_id, String year, String mileage, String mileage_unit, String model_id,
                          String country, String price, String car_manager_id, String engine_type,
                          String fuel_type, String transmission, String trim_id, String selling_condition,
                          String body_type_id, String interior_color, String exterior_color, String franchise_id,
                          String created_by, String cc_measurement, String tyre_code, String is_draft,
                          String expectedStatusCode) {
        vin = String.format("%017d", Math.abs(random.nextLong()) % 100000000000000000L);
        // Construct payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("owner_id", owner_id);
        payload.put("year", year);
        payload.put("mileage", mileage);
        payload.put("mileage_unit", mileage_unit);
        payload.put("vin", vin);
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
        payload.put("image_url", fileUrl);
        payload.put("created_by", created_by);
        payload.put("cc_measurement", cc_measurement);
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
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status Code");

        JsonPath json = response.jsonPath();

        // 🔹 Validate core fields from payload
        AllureLogs.softAssertEquals(softAssert, json.getString("ownerId"), payload.get("owner_id"), "Owner ID");
        AllureLogs.softAssertEquals(softAssert, String.valueOf(json.getInt("year")), payload.get("year"), "Year");
        AllureLogs.softAssertEquals(softAssert, String.valueOf(json.getInt("mileage")), payload.get("mileage"), "Mileage");
        AllureLogs.softAssertEquals(softAssert, json.getString("mileageUnit"), payload.get("mileage_unit"), "Mileage Unit");
        AllureLogs.softAssertEquals(softAssert, json.getString("vin"), payload.get("vin"), "VIN");
        AllureLogs.softAssertEquals(softAssert, String.valueOf(json.getInt("model.id")), String.valueOf(payload.get("model_id")), "Model ID");
        AllureLogs.softAssertEquals(softAssert, json.getString("country"), payload.get("country"), "Country");
        AllureLogs.softAssertEquals(softAssert, String.valueOf(json.getInt("price")), payload.get("price"), "Price");
        AllureLogs.softAssertEquals(softAssert, json.getString("carManagerId"), payload.get("car_manager_id"), "Car Manager ID");
        AllureLogs.softAssertEquals(softAssert, json.getString("engineType"), payload.get("engine_type"), "Engine Type");
        AllureLogs.softAssertEquals(softAssert, json.getString("fuelType"), payload.get("fuel_type"), "Fuel Type");
        AllureLogs.softAssertEquals(softAssert, json.getString("transmission"), payload.get("transmission"), "Transmission");
        AllureLogs.softAssertEquals(softAssert, json.getString("sellingCondition"), payload.get("selling_condition"), "Selling Condition");
        AllureLogs.softAssertEquals(softAssert, String.valueOf(json.getMap("bodyType").get("id")), String.valueOf(payload.get("body_type_id")), "Body Type ID");
        AllureLogs.softAssertEquals(softAssert, json.getString("createdBy"), payload.get("created_by"), "Created By");

        // 🔹 Conditional validation for tyre_code
        if (payload.containsKey("tyre_code")) {
            String tyreSpecs = json.getString("tyreSpecs");
            AllureLogs.softAssertNotNull(softAssert, tyreSpecs, "TyreSpecs should be present when tyre_code is provided");

            // Optional: Validate tyre code exists inside tyreSpecs JSON string
            AllureLogs.softAssertTrue(
                    softAssert,
                    tyreSpecs.contains(payload.get("tyre_code").toString()),
                    "TyreSpecs should contain provided tyre_code: " + payload.get("tyre_code")
            );
        } else {
            AllureLogs.softAssertNull(softAssert, json.getString("tyreSpecs"), "TyreSpecs should not be present when tyre_code is missing");
        }

        String id = json.getString("id");   // get the ID from this response
        carId.add(id);
        System.out.println(carId);
        AllureLogs.softAssertNotNull(softAssert, carId, "Car ID is returned");
        String vin_number=json.getString("vin");
        vinNumber.add(vin_number);
        // Execute all validations
        AllureLogs.executeSoftAssertAll(softAssert);
    }

    @DataProvider(name = "Create Car")
    public Object[][] CreateCar() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Postive_Data_Extractor.ExcelData("Create Car 2");
    }

    @Test(dependsOnMethods = "CreateCar", priority = 3, dataProvider = "Update Car")
    @Description("Update Car and Validate Response")
    @Story("Update Car")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"type", "Severity", "testDescription", "license_plate", "city", "state", "address", "owner_type", "is_upgraded", "expectedStatusCode"})
    public void updateCar(String type, String Severity, String testDescription, String license_plate, String city, String state, String address,
                          String owner_type, String is_upgraded, String isDraft, String expectedStatusCode) {

        // API Call
        for (String id : carId) {
            engineNumber = String.valueOf(new Random().nextInt(999999999));
            Map<String, Object> payload = new HashMap<>();
            payload.put("license_plate", license_plate);
            payload.put("city", city);
            payload.put("state", state);
            payload.put("address", address);
            payload.put("owner_type", owner_type);
            payload.put("is_upgraded", is_upgraded);
            payload.put("engine_number", engineNumber);
            payload.put("created_by", "BQlQBbLTW");
            payload.put("tyre_code", "265/65R17");   // conditionally validated
            payload.put("is_draft", isDraft);

            Response response =
                    given()
                            .header("Authorization", "Bearer " + Initialization.franchiseAdminToken.get("token"))
                            .contentType(ContentType.JSON)
                            .body(payload)
                            .when()
                            .put("/v1/inventory/car/" + id)
                            .then()
                            .log().all()
                            .extract().response();

            SoftAssert softAssert = new SoftAssert();
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status Code");

            JsonPath json = response.jsonPath();

            // 🔹 Validate all payload values against response
            AllureLogs.softAssertEquals(softAssert, json.getString("licensePlate"), payload.get("license_plate"), "License Plate");
            AllureLogs.softAssertEquals(softAssert, json.getString("city"), payload.get("city"), "City");
            AllureLogs.softAssertEquals(softAssert, json.getString("state"), payload.get("state"), "State");
            AllureLogs.softAssertEquals(softAssert, json.getString("address"), payload.get("address"), "Address");
            AllureLogs.softAssertEquals(softAssert, json.getString("ownerType"), payload.get("owner_type"), "Owner Type");
            AllureLogs.softAssertEquals(softAssert, json.getString("isUpgraded"), payload.get("is_upgraded"), "Is Upgraded");
            AllureLogs.softAssertEquals(softAssert, json.getString("engineNumber"), payload.get("engine_number"), "Engine Number");
            AllureLogs.softAssertEquals(softAssert, json.getString("createdBy"), payload.get("created_by"), "Created By");
            // AllureLogs.softAssertEquals(softAssert, json.getString("isDraft"), payload.get("is_draft"), "Is Draft");

            // 🔹 Conditional validation for tyre_code → tyreSpecs
            if (payload.containsKey("tyre_code")) {
                String tyreSpecs = json.getString("tyreSpecs");
                AllureLogs.softAssertNotNull(softAssert, tyreSpecs, "TyreSpecs should be present when tyre_code is provided");

                // Optional: validate that the provided tyre_code exists in tyreSpecs JSON
                AllureLogs.softAssertTrue(
                        softAssert,
                        tyreSpecs.contains(payload.get("tyre_code").toString()),
                        "TyreSpecs should contain provided tyre_code: " + payload.get("tyre_code")
                );
            } else {
                AllureLogs.softAssertNull(softAssert, json.getString("tyreSpecs"), "TyreSpecs should not be present when tyre_code is missing");
            }

            // 🔹 CarId must still exist after update
            AllureLogs.softAssertNotNull(softAssert, json.getString("id"), "Car ID is returned after update");

            // Execute all soft assertions

        }
    }

    @DataProvider(name = "Update Car")
    public Object[][] UpdateCar() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Postive_Data_Extractor.ExcelData("Update Car 2");
    }
private static List<String> TyreCodesAfterUpdateCar=new ArrayList<>();
    private static String TyreCodeUpdateCar="265/65R17";
    @Test(priority = 4)
    public void GetTyreSpecsAfterUpdateCar() {
        SoftAssert softAssert = new SoftAssert();
//        for (String id : carId) {
        String id = carId.get(carId.size() - 1);
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/v1/inventory/car/" + id + "/tyre-specs")
                //  .get("/v1/inventory/car/N2AaLLuqu/tyre-specs")
                .then()
                .log().all()
                .extract().response();
        //   SoftAssert softAssert = new SoftAssert();
        JsonPath jsonPath = new JsonPath(response.asString());
        AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status code");
        TyreCodesAfterUpdateCar = jsonPath.getList("tyreSpecs.tyres.code");

// collect all assertion results
        softAssert.assertAll();

        System.out.println("✅ All tyre codes match the input code: " + TyreCodeUpdateCar);
        //  }
        for (int i = 0; i < TyreCodesAfterUpdateCar.size(); i++) {
            String code = TyreCodesAfterUpdateCar.get(i);

            // Console log
            System.out.println("Comparing input: " + TyreCodeUpdateCar + " with tyre code[" + i + "]: " + code);

            // Allure + SoftAssert (your custom logger)
            AllureLogs.softAssertEquals(
                    softAssert,
                    code,
                    TyreCodeUpdateCar,
                    "Mismatch at index " + i + " -> expected: " + TyreCodeUpdateCar + " but found: " + code
            );
        }
        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @Test(dependsOnMethods = "CreateCar", priority = 5)
    @Description("Update Car and Validate Response")
    @Story("Complete Car")
    @Severity(SeverityLevel.CRITICAL)
    public void CompleteCar() {
        // Construct payload
String completeCarPayload="{\n" +
        "    \"summary\": \"TEST123\",\n" +
        "    \"form_id\": 2,\n" +
        "    \"form_data\": \"{\\\"name\\\":\\\"createDealerCar\\\",\\\"label\\\":\\\"Create Dealer Car\\\",\\\"sections\\\":[{\\\"name\\\":\\\"carDetailsRequired\\\",\\\"items\\\":[{\\\"name\\\":\\\"country\\\",\\\"entry\\\":\\\"NG\\\"},{\\\"name\\\":\\\"isDealer\\\",\\\"entry\\\":\\\"true\\\"},{\\\"name\\\":\\\"ownerId\\\",\\\"entry\\\":\\\"-lh7_mmKJ\\\"},{\\\"name\\\":\\\"createdBy\\\",\\\"entry\\\":\\\"BQlQBbLTW\\\"},{\\\"name\\\":\\\"franchise\\\",\\\"entry\\\":\\\"100% Automobile Nig.\\\"},{\\\"name\\\":\\\"franchiseId\\\",\\\"entry\\\":\\\"kpMngdaMZ\\\"},{\\\"name\\\":\\\"carCondition\\\",\\\"entry\\\":\\\"new\\\"},{\\\"name\\\":\\\"vin\\\",\\\"entry\\\":\\\"\\\",\\\"comment\\\":\\\"The (**) fields will be auto-populated based on the VIN\\\"},{\\\"name\\\":\\\"year\\\",\\\"entry\\\":\\\"2025\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"make\\\",\\\"entry\\\":\\\"Toyota\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"makeId\\\",\\\"entry\\\":\\\"106\\\"},{\\\"name\\\":\\\"model\\\",\\\"entry\\\":\\\"Corolla\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"modelId\\\",\\\"entry\\\":\\\"1891\\\"},{\\\"name\\\":\\\"trim\\\",\\\"entry\\\":\\\"\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"trimId\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"price\\\",\\\"entry\\\":\\\"909900\\\"},{\\\"name\\\":\\\"bodyType\\\",\\\"entry\\\":\\\"Coupe\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"bodyTypeId\\\",\\\"entry\\\":\\\"7\\\"},{\\\"name\\\":\\\"fuelType\\\",\\\"entry\\\":\\\"petrol\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"engineType\\\",\\\"entry\\\":\\\"2-cylinder(I2)\\\",\\\"comment\\\":\\\"**\\\"},{\\\"name\\\":\\\"interiorColor\\\",\\\"entry\\\":\\\"Blue\\\"},{\\\"name\\\":\\\"exteriorColor\\\",\\\"entry\\\":\\\"Green\\\"},{\\\"name\\\":\\\"mileage\\\",\\\"entry\\\":\\\"2555\\\"},{\\\"name\\\":\\\"mileageUnit\\\",\\\"entry\\\":\\\"km\\\"},{\\\"name\\\":\\\"transmission\\\",\\\"entry\\\":\\\"automatic\\\"},{\\\"name\\\":\\\"engineCc\\\",\\\"entry\\\":\\\"1\\\"},{\\\"name\\\":\\\"carThumbnail\\\",\\\"entry\\\":\\\"https://media.autochek.africa/file/w_900,q_100/z7LmywTx.webp\\\",\\\"comment\\\":\\\"This is the main image that appears in search results and attracts customers to click on your listing\\\"},{\\\"name\\\":\\\"imageUrl\\\",\\\"entry\\\":\\\"https://media.autochek.africa/file/w_900,q_100/z7LmywTx.webp\\\"},{\\\"name\\\":\\\"carDetailsRequiredSave\\\"},{\\\"name\\\":\\\"carDetailsRequiredSubmit\\\"}]},{\\\"name\\\":\\\"carDetailsOptional\\\",\\\"items\\\":[{\\\"name\\\":\\\"ownershipStatus\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"registrationStatus\\\",\\\"entry\\\":\\\"no\\\"},{\\\"name\\\":\\\"plateNumber\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"engineNumber\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"registrationSeries\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"state\\\",\\\"entry\\\":\\\"test\\\"},{\\\"name\\\":\\\"stateId\\\",\\\"entry\\\":309},{\\\"name\\\":\\\"city\\\",\\\"entry\\\":\\\"test\\\"},{\\\"name\\\":\\\"location\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"cityId\\\",\\\"entry\\\":3251},{\\\"name\\\":\\\"isUpgraded\\\",\\\"entry\\\":\\\"no\\\"},{\\\"name\\\":\\\"upgradedFrom\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"upgradedTo\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"carDetailsRequiredSave\\\"},{\\\"name\\\":\\\"carDetailsRequiredSubmit\\\"}]},{\\\"name\\\":\\\"carSnapshots\\\",\\\"items\\\":[{\\\"name\\\":\\\"snapshots\\\",\\\"entry\\\":[{\\\"name\\\":\\\"leftFront\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/w_900,q_100/SclbGR99.webp\\\"}]},{\\\"name\\\":\\\"carImagesSave\\\"},{\\\"name\\\":\\\"carImagesSubmit\\\"}]},{\\\"name\\\":\\\"carDocuments\\\",\\\"items\\\":[{\\\"name\\\":\\\"documents\\\",\\\"entry\\\":[]},{\\\"name\\\":\\\"carDocumentsSave\\\"},{\\\"name\\\":\\\"carDocumentsSubmit\\\"}]},{\\\"name\\\":\\\"carFeatures\\\",\\\"items\\\":[{\\\"name\\\":\\\"searchFeatures\\\",\\\"entry\\\":\\\"\\\"},{\\\"name\\\":\\\"features\\\",\\\"entry\\\":[\\\"1\\\",\\\"2\\\",\\\"3\\\",\\\"28\\\",\\\"39\\\",\\\"44\\\",\\\"66\\\",\\\"67\\\",\\\"68\\\",\\\"69\\\",\\\"70\\\",\\\"71\\\",\\\"72\\\",\\\"73\\\",\\\"74\\\",\\\"75\\\",\\\"76\\\",\\\"77\\\",\\\"78\\\",\\\"79\\\",\\\"80\\\",\\\"81\\\",\\\"82\\\",\\\"83\\\",\\\"84\\\",\\\"85\\\",\\\"86\\\",\\\"87\\\",\\\"88\\\"]},{\\\"name\\\":\\\"carFeaturesSave\\\"},{\\\"name\\\":\\\"carFeaturesSubmit\\\"}]},{\\\"name\\\":\\\"carSummary\\\",\\\"items\\\":[{\\\"name\\\":\\\"summary\\\",\\\"entry\\\":\\\"TEST123\\\"},{\\\"name\\\":\\\"carSummarySave\\\"},{\\\"name\\\":\\\"submit\\\"}]}]}\",\n" +
        "    \"is_draft\": false\n" +
        "}";
        // API Call
        for (String id : carId) {
            Response response =
                    given()
                            .header("Authorization", "Bearer " + Initialization.franchiseAdminToken.get("token"))
                            .contentType(ContentType.JSON)
                            .body(completeCarPayload)
                            .when()
                            .put("/v1/inventory/car/" + id)
                            .then()
                            .log().all()
                            .extract().response();

            SoftAssert softAssert = new SoftAssert();
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status Code");

            JsonPath json = response.jsonPath();

        }
    }


    @Test(priority = 6)
    @Description("Get TyreSpecs Details by Car ID")
    @Story("Get TyreSpecs")
    @Severity(SeverityLevel.NORMAL)
    public void GetTyreSpecs() {
        for (String id : carId) {
            Response response = given()
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .get("/v1/inventory/car/" + id + "/tyre-specs")
//                    .get("/v1/inventory/car/N2AaLLuqu/tyre-specs")
                    .then()
                    .log().all()
                    .extract().response();
            SoftAssert softAssert = new SoftAssert();
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status code");

            JsonPath json = response.jsonPath();

            // Extract nested list of prices -> gives List<List<Integer>>
            List<?> nestedPrices = json.getList("tyreSpecs.tyres.prices.price", List.class);
            System.out.println("Nested Prices: " + nestedPrices);

            List<Integer> allPrices = nestedPrices.stream()
                    .flatMap(item -> ((List<?>) item).stream())   // flatten inner lists
                    .map(price -> Integer.parseInt(price.toString())) // convert to Integer
                    .collect(Collectors.toList());

            System.out.println("All Prices Flattened: " + allPrices);

            // Get the minimum price
            int minPrice = allPrices.stream()
                    .min(Integer::compareTo)
                    .orElseThrow();

            // Get root-level price
            int rootPrice = json.getInt("tyreSpecs.price");
            System.out.println("Root Price: " + rootPrice + " | Min Price: " + minPrice);

            // Assertion
            AllureLogs.softAssertEquals(softAssert, rootPrice, minPrice,
                    "Root tyreSpecs.price should equal the minimum of prices array");

            // 🔹 Root level
            AllureLogs.softAssertNotNull(softAssert, json.getString("id"), "Car ID is present");
            AllureLogs.softAssertNotNull(softAssert, json.getMap("tyreSpecs"), "tyreSpecs object is present");


            // 🔹 Validate tyres array
            List<Map<String, Object>> tyres = json.getList("tyreSpecs.tyres");
            AllureLogs.softAssertTrue(softAssert, tyres.size() > 0, "tyres array should not be empty");

            for (Map<String, Object> tyre : tyres) {
                AllureLogs.softAssertNotNull(softAssert, tyre.get("id"), "Tyre ID is present");
                AllureLogs.softAssertNotNull(softAssert, tyre.get("brand"), "Tyre brand is present");
                AllureLogs.softAssertNotNull(softAssert, tyre.get("code"), "Tyre code is present");
                AllureLogs.softAssertNotNull(softAssert, tyre.get("description"), "Tyre description is present");

                // 🔹 Validate tyre prices
                List<Map<String, Object>> prices = (List<Map<String, Object>>) tyre.get("prices");
                AllureLogs.softAssertTrue(softAssert, prices.size() > 0, "Prices array should not be empty");
                for (Map<String, Object> price : prices) {
                    AllureLogs.softAssertNotNull(softAssert, price.get("vendorId"), "Vendor ID is present");
                    AllureLogs.softAssertEquals(softAssert, price.get("tyreId"), tyre.get("id"), "TyreId should match parent tyre id");
                    AllureLogs.softAssertNotNull(softAssert, price.get("price"), "Tyre price is present");
                    AllureLogs.softAssertEquals(softAssert, price.get("country"), "NG", "Country should be NG");
                    AllureLogs.softAssertNotNull(softAssert, price.get("createdAt"), "CreatedAt timestamp is present");
                    AllureLogs.softAssertNotNull(softAssert, price.get("updatedAt"), "UpdatedAt timestamp is present");
                    AllureLogs.softAssertNotNull(softAssert, price.get("id"), "Price ID is present");
                }
            }

            // 🔹 Validate addOns
            List<Map<String, Object>> addOns = json.getList("tyreSpecs.addOns");
            AllureLogs.softAssertTrue(softAssert, addOns.size() > 0, "addOns should not be empty");
            for (Map<String, Object> addOn : addOns) {
                AllureLogs.softAssertNotNull(softAssert, addOn.get("name"), "AddOn name is present");
                AllureLogs.softAssertNotNull(softAssert, addOn.get("price"), "AddOn price is present");
            }

            // Execute all assertions
            AllureLogs.executeSoftAssertAll(softAssert);
        }

    }


    List<String> inspectionIds = new ArrayList<>();
    @Test(dependsOnMethods = "CreateCar", priority = 7)
    @Description("Get the Inspection_id from the Response")
    @Story("Inspection Request")
    @Severity(SeverityLevel.CRITICAL)
    public void InspectionRequest() {
        // Construct payload


        for (int i = 0; i < vinNumber.size() && i < carId.size(); i++) {
            String id = vinNumber.get(i);
            String carId2 = carId.get(i);

            String Inspection = "{\n" +
                    "    \"vin\": \"" + id + "\",\n" +
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
            String inspectionId = response.jsonPath().getString("id");
            inspectionIds.add(inspectionId);

            SoftAssert softAssert = new SoftAssert();
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status Code");
            AllureLogs.executeSoftAssertAll(softAssert);
        }

// Print collected IDs
        System.out.println("Collected Inspection IDs: " + inspectionIds);
    }
    private static String TyreCode="225/60R17";
    private static List<String> TyreCodes=new ArrayList<>();
    @Test(priority = 8)
    @Description("Create Inspection for the carID generated in Car Creation")
    @Story("Create Inspection")
    @Severity(SeverityLevel.NORMAL)
    public void CreateInspection() {
        for (int i = 0; i < inspectionIds.size() && i < carId.size(); i++) {
            String id = inspectionIds.get(i);
            String carId2 = carId.get(i);

            String payload = "{\n" +
                    "  \"form_id\": \"3\",\n" +
                    "  \"form_data\": \"{\\\"sections\\\":[{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"Toyota\\\",\\\"name\\\":\\\"make\\\"},{\\\"entry\\\":\\\"Camry\\\",\\\"name\\\":\\\"model\\\"},{\\\"entry\\\":\\\"2024\\\",\\\"name\\\":\\\"year\\\"},{\\\"entry\\\":\\\"TOYOTATOYOTATOYOT\\\",\\\"name\\\":\\\"vin\\\"},{\\\"entry\\\":\\\"3200\\\",\\\"name\\\":\\\"odometer\\\"},{\\\"entry\\\":\\\"km\\\",\\\"name\\\":\\\"odometerUnit\\\"},{\\\"entry\\\":\\\"13/08/2025\\\",\\\"name\\\":\\\"inspectionDate\\\"},{\\\"entry\\\":\\\"Daniel inspector qa\\\",\\\"name\\\":\\\"inspectorName\\\"}," +
                    "{\\\"entry\\\":\\\""+TyreCode+"\\\",\\\"name\\\":\\\"tyreCode\\\"}],\\\"name\\\":\\\"intro\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"powerWindows\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"powerWindowsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"powerWindowsImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"powerWindowsComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"sunRoof\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"sunRoofSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"sunRoofImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"sunRoofComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"audioSystem\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"audioSystemSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"audioSystemImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"audioSystemComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"windshieldWipers\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"windshieldWipersSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"windshieldWipersImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"windshieldWipersComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"reverseCameraScreen\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"reverseCameraScreenSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"reverseCameraScreenImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"reverseCameraScreenComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"innerDomeMapLight\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"innerDomeMapLightSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"innerDomeMapLightImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"innerDomeMapLightComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"seatAdjuster\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"seatAdjusterSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"seatAdjusterImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"seatAdjusterComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"seatCooler\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"seatCoolerSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"seatCoolerImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"seatCoolerComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"airConditioning\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"airConditioningSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"airConditioningImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"airConditioningComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"dashboardIndicators\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"dashboardIndicatorsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"dashboardIndicatorsImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"dashboardIndicatorsComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"parkingSensor\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"parkingSensorSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"parkingSensorImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"parkingSensorComments\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"horn\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"hornSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"hornImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"hornComments\\\"},{\\\"entry\\\":\\\"Good\\\",\\\"name\\\":\\\"electricalObservations\\\"}],\\\"name\\\":\\\"electricals\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"dashboard\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"dashboardSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"dashboardImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"steering\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"steeringSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"steeringImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"seats\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"seatsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"seatsImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"roof\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"roofSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"roofImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"carpet\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"carpetSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"carpetImages\\\"},{\\\"entry\\\":\\\"Fair\\\",\\\"name\\\":\\\"interiorObservations\\\"}],\\\"name\\\":\\\"interior\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"bodyDents\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"bodyDentsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"bodyDentsImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"windshield\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"windshieldSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"windshieldImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"exteriorScratches\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"exteriorScratchesSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"exteriorScratchesImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"headlights\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"headlightsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"headlightsImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"rearAndBrakeLights\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"rearAndBrakeLightsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"rearAndBrakeLightsImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"logosEmblems\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"logosEmblemsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"logosEmblemsImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"rustCorrosion\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"rustCorrosionSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"rustCorrosionImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"exhaust\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"exhaustSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"exhaustImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"tyreTreadDepth\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"tyreTreadDepthSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"tyreTreadDepthImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"paint\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"paintSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"paintImages\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"exteriorObservations\\\"}],\\\"name\\\":\\\"exterior\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"engineFiring\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"engineFiringSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"engineFiringImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"gearSwitching\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"gearSwitchingSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"gearSwitchingImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"oilLeakage\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"oilLeakageSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"oilLeakageImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"battery\\\"},{\\\"entry\\\":\\\"low\\\",\\\"name\\\":\\\"batterySeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"batteryImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"serpentineBelt\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"serpentineBeltSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"serpentineBeltImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"engineMounts\\\"},{\\\"entry\\\":\\\"medium\\\",\\\"name\\\":\\\"engineMountsSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"engineMountsImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"catalyticConverter\\\"},{\\\"entry\\\":\\\"high\\\",\\\"name\\\":\\\"catalyticConverterSeverity\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"catalyticConverterImages\\\"},{\\\"entry\\\":\\\"Good \\\",\\\"name\\\":\\\"engineAndTransmissionObservations\\\"}],\\\"name\\\":\\\"engineAndTransmission\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"available\\\",\\\"name\\\":\\\"jack\\\"},{\\\"entry\\\":\\\"available\\\",\\\"name\\\":\\\"wheelSpanner\\\"},{\\\"entry\\\":\\\"available\\\",\\\"name\\\":\\\"spareTyre\\\"},{\\\"entry\\\":\\\"available\\\",\\\"name\\\":\\\"spareKey\\\"},{\\\"entry\\\":\\\"available\\\",\\\"name\\\":\\\"customsPaper\\\"}],\\\"name\\\":\\\"accessories\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"okay\\\",\\\"name\\\":\\\"radiatorWaterCoolant\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"radiatorWaterCoolantImages\\\"},{\\\"entry\\\":\\\"okay\\\",\\\"name\\\":\\\"brakeFluid\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"brakeFluidImages\\\"},{\\\"entry\\\":\\\"okay\\\",\\\"name\\\":\\\"steeringFluid\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"steeringFluidImages\\\"},{\\\"entry\\\":\\\"pass\\\",\\\"name\\\":\\\"transmissionFluid\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"transmissionFluidImages\\\"},{\\\"entry\\\":\\\"okay\\\",\\\"name\\\":\\\"engineOil\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"engineOilImages\\\"}],\\\"name\\\":\\\"fluidLevels\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"no\\\",\\\"name\\\":\\\"includeObds\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"scanReport\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"searchObds\\\"},{\\\"entry\\\":\\\"\\\",\\\"name\\\":\\\"obds\\\"}],\\\"name\\\":\\\"obd\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"\\\",\\\"media\\\":[{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"leftFront\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/G245kHiS.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"rightFront\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/MjgXon5t.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"front\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/u4Gtaemd.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"back\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/f58g7yD9.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"leftBack\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/f53SSP7H.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"rightBack\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/McfMM26l.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"interiorFront\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/gBmVZz7t.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"interiorBack\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/yjr712tx.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"vinImage\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/yMk6su0P.webp\\\"},{\\\"label\\\":\\\"\\\",\\\"name\\\":\\\"tyreImage\\\",\\\"type\\\":\\\"image\\\",\\\"url\\\":\\\"https://media.autochek.africa/file/nVs5tzyW.webp\\\"}],\\\"name\\\":\\\"snapshots\\\"}],\\\"name\\\":\\\"carSnapshots\\\"},{\\\"comment\\\":\\\"\\\",\\\"items\\\":[{\\\"entry\\\":\\\"yes\\\",\\\"name\\\":\\\"autoFailure\\\"}],\\\"name\\\":\\\"outro\\\"}]}\",\n" +
                    "  \"car_id\": \""+carId2+"\",\n" +
                    "  \"inspection_request_summary_id\": \"O9ZDCvr8C\",\n" +
                    "  \"inspection_request_id\": \""+id+"\",   \n" +
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

    @Test(priority = 9)
    public void GetTyreSpecsAfterInspection() {
        SoftAssert softAssert = new SoftAssert();
        String id = carId.get(carId.size() - 1);
            Response response = given()
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .get("/v1/inventory/car/" + id + "/tyre-specs")
                    .then()
                    .log().all()
                    .extract().response();
            JsonPath jsonPath = new JsonPath(response.asString());
            AllureLogs.softAssertEquals(softAssert, response.getStatusCode(), 200, "Status code");
            TyreCodes = jsonPath.getList("tyreSpecs.tyres.code");

// collect all assertion results
            softAssert.assertAll();

            System.out.println("✅ All tyre codes match the input code: " + TyreCode);
      //  }
        for (int i = 0; i < TyreCodes.size(); i++) {
            String code = TyreCodes.get(i);

            // Console log
            System.out.println("Comparing input: " + TyreCode + " with tyre code[" + i + "]: " + code);

            // Allure + SoftAssert (your custom logger)
            AllureLogs.softAssertEquals(
                    softAssert,
                    code,
                    TyreCode,
                    "Mismatch at index " + i + " -> expected: " + TyreCode + " but found: " + code
            );
        }
        AllureLogs.executeSoftAssertAll(softAssert);
        }


}

