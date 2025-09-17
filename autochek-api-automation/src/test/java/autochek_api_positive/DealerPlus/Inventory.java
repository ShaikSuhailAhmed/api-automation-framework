package autochek_api_positive.DealerPlus;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

import static io.restassured.RestAssured.given;

import Utils.Postive_Data_Extractor;
import Base.Initialization;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.util.List;

import static io.restassured.RestAssured.given;

public class Inventory {

    Initialization initialization = new Initialization();

    @BeforeClass
    public void setUp() {
        initialization.initializeEnvironment(); // Sets base URI
    }

    @Test(dataProvider = "Get Car Make")
    @Parameters({"use_cache", "page_size"})
    public void getCarMake(String use_cache, String page_size) {

        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .queryParam("use_cache", use_cache)
                .queryParam("page_size", page_size)
                .when()
                .get("/v1/inventory/make")
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status Code should be 200");

        List<Map<String, Object>> makeList = response.jsonPath().getList("makeList");
        Map<String, Object> pagination = response.jsonPath().getMap("pagination");

        softAssert.assertNotNull(makeList, "Make list should not be null");
        softAssert.assertTrue(makeList.size() > 0, "Make list should not be empty");
        softAssert.assertTrue(makeList.stream().anyMatch(m -> "Acura".equals(m.get("name"))),
                "Expected make 'Acura' not found");

        softAssert.assertEquals(pagination.get("pageSize"), Integer.parseInt(page_size), "Mismatch in page size");
        softAssert.assertEquals(pagination.get("currentPage"), 1, "Expected current page to be 1");
        softAssert.assertTrue((int) pagination.get("total") > 0, "Total should be > 0");

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Car Make")
    public Object[][] getCarMakeData() {
        return Postive_Data_Extractor.ExcelData("Get Car Make");
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

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Car Model")
    public Object[][] getCarModelData() {
        return Postive_Data_Extractor.ExcelData("Get Car Model");
    }

    @Test(dataProvider = "Get Car Body Type")
    @Parameters({"page_size"})
    public void getCarBodyType(String page_size) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .queryParam("page_size", page_size)
                .when()
                .get("/v1/inventory/body_type")
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status Code should be 200");

        List<Map<String, Object>> bodyTypeList = response.jsonPath().getList("bodyTypeList");
        Map<String, Object> pagination = response.jsonPath().getMap("pagination");

        softAssert.assertNotNull(bodyTypeList, "Body type list should not be null");
        softAssert.assertTrue(bodyTypeList.size() > 0, "Body type list should not be empty");
        softAssert.assertTrue(bodyTypeList.stream().anyMatch(b -> "SUV".equalsIgnoreCase((String) b.get("name"))),
                "Expected 'SUV' to be present in body type list");

        softAssert.assertEquals(pagination.get("pageSize"), Integer.parseInt(page_size), "Page size should match");
        softAssert.assertEquals(pagination.get("currentPage"), 1, "Current page should be 1");
        softAssert.assertTrue((int) pagination.get("total") >= bodyTypeList.size(), "Total should be >= list size");

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Car Body Type")
    public Object[][] getCarBodyTypeData() {
        return Postive_Data_Extractor.ExcelData("Get Car Body Type");
    }

    @Test
    public void getCarColours() {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .when()
                .get("/v1/inventory/marketplace/colors")
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status Code should be 200");

        List<String> colors = response.jsonPath().getList("colors");

        softAssert.assertNotNull(colors, "Colors list should not be null");
        softAssert.assertTrue(colors.size() > 0, "Colors list should not be empty");
        softAssert.assertTrue(colors.contains("Black"), "'Black' should be present in color list");
        softAssert.assertTrue(colors.contains("White"), "'White' should be present in color list");
        softAssert.assertTrue(colors.contains("Red"), "'Red' should be present in color list");

        softAssert.assertAll();
    }

    @Test(dataProvider = "Get All Cars")
    @Parameters({"country", "franchise_id", "add_inspection_status", "exclude_category_name", "page_size", "status"})
    public void getAllCars(String country, String franchise_id, String add_inspection_status, String exclude_category_name, String page_size, String status) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + initialization.franchiseAdminToken)
                .queryParam("country", country)
                .queryParam("franchise_id", franchise_id)
                .queryParam("add_inspection_status", add_inspection_status)
                .queryParam("exclude_category_name", exclude_category_name)
                .queryParam("page_size", page_size)
                .queryParam("status", status)
                .when()
                .get("/v1/inventory/car")
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status Code should be 200");

        List<Map<String, Object>> carList = response.jsonPath().getList("carList");
        Map<String, Object> pagination = response.jsonPath().getMap("pagination");

        softAssert.assertNotNull(carList, "Car list should not be null");
        softAssert.assertTrue(carList.size() > 0, "Car list should not be empty");
        softAssert.assertEquals(pagination.get("pageSize"), Integer.parseInt(page_size), "Page size mismatch");
        softAssert.assertEquals(pagination.get("currentPage"), 1, "Current page should be 1");
        softAssert.assertTrue((int) pagination.get("total") >= carList.size(), "Pagination total should be >= car list size");

        boolean validCountry = carList.stream()
                .allMatch(c -> country.equalsIgnoreCase((String) c.get("country")));
        softAssert.assertTrue(validCountry, "All cars should belong to country: " + country);

        softAssert.assertAll();
    }

    @DataProvider(name = "Get All Cars")
    public Object[][] getAllCarsData() {
        return Postive_Data_Extractor.ExcelData("Get All Cars");
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

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Car Categories")
    public Object[][] getCarCategoriesData() {
        return Postive_Data_Extractor.ExcelData("Get Car Categories");
    }

    @Test(dataProvider = "Get Single Car Details")
    @Parameters({"car_id"})
    public void getSingleCarDetails(String car_id) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .when()
                .get("/v2/inventory/car/" + car_id)
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status Code should be 200");

        // Car Object
        Map<String, Object> car = response.jsonPath().getMap("car");
        softAssert.assertEquals(car.get("id"), car_id, "Car ID should match");
        softAssert.assertEquals(car.get("country"), "NG", "Country should be NG");
        softAssert.assertTrue(car.get("carName").toString().contains("Audi"), "Car name should contain 'Audi'");
        softAssert.assertTrue((int) car.get("year") >= 2010, "Car year should be >= 2010");

        // Model & Make
        Map<String, Object> model = (Map<String, Object>) car.get("model");
        Map<String, Object> make = (Map<String, Object>) model.get("make");
        softAssert.assertEquals(make.get("name"), "Audi", "Make name should be Audi");

        // Features
        List<Map<String, Object>> features = response.jsonPath().getList("features");
        softAssert.assertTrue(features.size() > 0, "Car should have features");
        boolean hasABS = features.stream()
                .anyMatch(f -> "Anti-lock Braking System".equalsIgnoreCase((String) ((Map<String, Object>) f.get("feature")).get("name")));
        softAssert.assertTrue(hasABS, "Should contain Anti-lock Braking System");

        // Media
        List<Map<String, Object>> mediaList = response.jsonPath().getList("media");
        softAssert.assertTrue(mediaList.size() > 0, "Media list should not be empty");
        softAssert.assertTrue(mediaList.stream().allMatch(m -> m.get("type").equals("image")), "All media should be images");

        // Body Type
        Map<String, Object> bodyType = (Map<String, Object>) car.get("bodyType");
        softAssert.assertEquals(bodyType.get("name"), "Sedan", "Body type should be Sedan");

        // Financing Settings
        Map<String, Object> financingSettings = response.jsonPath().getMap("financingSettings.loanCalculator.ranges");
        softAssert.assertNotNull(financingSettings.get("minInterestRate"), "Min interest rate should be present");
      //  softAssert.assertTrue((float) financingSettings.get("tenure") >= 12, "Tenure should be >= 12 months");
        Number tenure = (Number) financingSettings.get("tenure");
        softAssert.assertTrue(tenure.intValue() >= 12, "Tenure should be >= 12 months");

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Single Car Details")
    public Object[][] getSingleCarDetailsData() {
        return Postive_Data_Extractor.ExcelData("Get Single Car Details");
    }

    @Test(dataProvider = "Get Car Details")
    @Parameters({"country", "franchise_id", "add_inspection_status", "exclude_category_name", "page_size", "status"})
    public void getCarListDetails(String country, String franchise_id, String addInspection, String excludeCategory, String pageSize, String status) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + initialization.franchiseAdminToken)
                .queryParam("country", country)
                .queryParam("franchise_id", franchise_id)
                .queryParam("add_inspection_status", addInspection)
                .queryParam("exclude_category_name", excludeCategory)
                .queryParam("page_size", pageSize)
                .queryParam("status", status)
                .when()
                .get("/v1/inventory/car")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        List<Map<String, Object>> carList = response.jsonPath().getList("carList");
        softAssert.assertTrue(carList.size() > 0, "Car list should not be empty");

        for (Map<String, Object> car : carList) {
            softAssert.assertNotNull(car.get("id"), "Car ID should not be null");
            softAssert.assertEquals(car.get("country"), country, "Country mismatch");
            softAssert.assertTrue(((int) car.get("year")) >= 2010, "Year should be >= 2010");

            Map<String, Object> model = (Map<String, Object>) car.get("model");
            softAssert.assertNotNull(model.get("name"), "Model name should be present");

            Map<String, Object> make = (Map<String, Object>) model.get("make");
            softAssert.assertNotNull(make.get("name"), "Make name should be present");

            Map<String, Object> bodyType = (Map<String, Object>) car.get("bodyType");
            softAssert.assertNotNull(bodyType.get("name"), "Body type should be present");

            softAssert.assertNotNull(car.get("fuelType"), "Fuel Type should not be null");
            softAssert.assertNotNull(car.get("transmission"), "Transmission should not be null");
        }

        Map<String, Object> pagination = response.jsonPath().getMap("pagination");
        softAssert.assertEquals(String.valueOf(pagination.get("pageSize")), pageSize, "Page size mismatch");

        softAssert.assertAll();

    }

    @DataProvider(name = "Get Car Details")
    public Object[][] getCarDetailsData() {
        return Postive_Data_Extractor.ExcelData("Get Car Details");
    }


    @Test(dataProvider = "Get Car Details Admin")
    @Parameters({"car_id"})
    public void getCarDetailsById(String car_id) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .when()
                .get("/v1/inventory/admin/car/" + car_id)
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status Code should be 200");

        // Root object
        softAssert.assertEquals(response.jsonPath().getString("id"), car_id, "Car ID mismatch");
        softAssert.assertEquals(response.jsonPath().getString("country"), "KE", "Country should be KE");
        softAssert.assertEquals(response.jsonPath().getString("state"), "Meru", "State mismatch");
        softAssert.assertEquals(response.jsonPath().getString("city"), "Maua", "City mismatch");
        softAssert.assertEquals(response.jsonPath().getString("fuelType"), "diesel", "Fuel type mismatch");
        softAssert.assertEquals(response.jsonPath().getString("transmission"), "automatic", "Transmission mismatch");
        softAssert.assertEquals(response.jsonPath().getString("ownerType"), "self", "Owner type mismatch");

        // Body Type
        Map<String, Object> bodyType = response.jsonPath().getMap("bodyType");
        if (bodyType != null) {
            softAssert.assertEquals(bodyType.get("name"), "Multi-Purpose Vehicle (MPV)", "Body type mismatch");
        }

        // Model and Make
        Map<String, Object> model = response.jsonPath().getMap("model");
        if (model != null) {
            softAssert.assertEquals(model.get("name"), "118", "Model name mismatch");
            Map<String, Object> make = (Map<String, Object>) model.get("make");
            if (make != null) {
                softAssert.assertEquals(make.get("name"), "BMW", "Make name mismatch");
            }
        }

        // Boolean values (check for nulls before assertion)
        Boolean hasFinancing = response.jsonPath().getBoolean("hasFinancing");
        if (hasFinancing != null) {
            softAssert.assertTrue(hasFinancing, "Car should have financing");
        }

        Boolean hasWarranty = response.jsonPath().getBoolean("hasWarranty");
        if (hasWarranty != null) {
            softAssert.assertFalse(hasWarranty, "Car should not have warranty");
        }

        // Colors
        softAssert.assertEquals(response.jsonPath().getString("interiorColor"), "black", "Interior color mismatch");
        softAssert.assertEquals(response.jsonPath().getString("exteriorColor"), "red", "Exterior color mismatch");

        // Loan/Installment
        softAssert.assertTrue(response.jsonPath().getFloat("installment") > 0, "Installment should be positive");
        softAssert.assertTrue(response.jsonPath().getInt("loanValue") > 0, "Loan value should be positive");

        // Grade Score & Summary
        softAssert.assertTrue(response.jsonPath().getFloat("gradeScore") >= 0, "Grade score should be non-negative");
        softAssert.assertEquals(response.jsonPath().getString("summary"), "fastesttt", "Summary mismatch");

        // Inspection
        softAssert.assertFalse(response.jsonPath().getBoolean("inspected"), "Car should not be inspected");

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Car Details Admin")
    public Object[][] getCarDetailsDataAdmin() {
        return Postive_Data_Extractor.ExcelData("Get Car Details Admin"); // Sheet name as per your Excel
    }

    @Test(dataProvider = "Get Car Moderations")
    @Parameters({"car_id", "page_number", "page_size", "status"})
    public void getCarModerations(String car_id, String page_number, String page_size, String status) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .queryParam("car_id", car_id)
                .queryParam("page_number", page_number)
                .queryParam("page_size", page_size)
                .queryParam("status", status)
                .when()
                .get("/v1/inventory/cars/moderations")
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status code should be 200");

        List<String> moderationCarIds = response.jsonPath().getList("moderations.carId");
        List<String> moderationStatuses = response.jsonPath().getList("moderations.status");
        List<String> moderationReasons = response.jsonPath().getList("moderations.moderationReason");

        // Validate carId in response
        for (String id : moderationCarIds) {
            softAssert.assertEquals(id, car_id, "Returned carId should match requested");
        }

        // Validate moderation status
        for (String st : moderationStatuses) {
            softAssert.assertTrue(status.contains(st), "Returned status should match requested");
        }

        // Validate moderationReason is not null or empty
        for (String reason : moderationReasons) {
            softAssert.assertNotNull(reason, "Moderation reason should not be null");
            softAssert.assertTrue(!reason.isEmpty(), "Moderation reason should not be empty");
        }

        // Pagination check
        int currentPage = response.jsonPath().getInt("pagination.currentPage");
        int pageSizeReturned = response.jsonPath().getInt("pagination.pageSize");
        int total = response.jsonPath().getInt("pagination.total");

        softAssert.assertEquals(currentPage, Integer.parseInt(page_number), "Pagination currentPage mismatch");
        softAssert.assertEquals(pageSizeReturned, Integer.parseInt(page_size), "Pagination pageSize mismatch");
        softAssert.assertTrue(total >= 0, "Total items should be >= 0");

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Car Moderations")
    public Object[][] getCarModerationDataFromExcel() {
        return Postive_Data_Extractor.ExcelData("Get Car Moderations"); // Match your Excel sheet name exactly
    }

    @Test(dataProvider = "Get User Loan Notifications")
    @Parameters({"user_id", "current_page", "page_size"})
    public void getUserLoanNotifications(String user_id, String current_page, String page_size) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .queryParam("user_id", user_id)
                .queryParam("current_page", current_page)
                .queryParam("page_size", page_size)
                .when()
                .get("/v2/origination/kCkHwnW6c/notifications")
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status code should be 200");

        // Validating userId
        List<String> userIds = response.jsonPath().getList("notifications.userId");
        for (String id : userIds) {
            softAssert.assertEquals(id, user_id, "Returned userId should match requested");
        }


        // Validate messages are not null or empty
        List<String> messages = response.jsonPath().getList("notifications.message");
        for (String msg : messages) {
            softAssert.assertNotNull(msg, "Message should not be null");
            softAssert.assertFalse(msg.isEmpty(), "Message should not be empty");
        }

        // Validate pagination details
        int currentPage = response.jsonPath().getInt("pagination.currentPage");
        int pageSize = response.jsonPath().getInt("pagination.pageSize");
        int total = response.jsonPath().getInt("pagination.total");

        softAssert.assertEquals(currentPage, Integer.parseInt(current_page), "Current page mismatch");
        softAssert.assertEquals(pageSize, Integer.parseInt(page_size), "Page size mismatch");
        softAssert.assertTrue(total >= 0, "Total notifications should be >= 0");

        softAssert.assertAll();
    }

    @DataProvider(name = "Get User Loan Notifications")
    public Object[][] getUserLoanNotificationDataFromExcel() {
        return Postive_Data_Extractor.ExcelData("Get User Loan Notifications");
    }

    @Test(dataProvider = "Get Moderation Alert Messages")
    @Parameters({"page_size", "page_number", "status"})
//eshareddy@frugaltestingin..com
    public void getModerationAlertMessages(String page_size, String page_number, String status) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .queryParam("page_size", page_size)
                .queryParam("page_number", page_number)
                .queryParam("status", status)
                .when()
                .get("/v1/inventory/alerts/messages")
                .then()
                .log().all()
                .extract().response();
        System.out.println("API Response: " + response.asString());

        // Assert 200 OK
        softAssert.assertEquals(response.statusCode(), 200, "Expected status code 200");

        List<String> alertIds = response.jsonPath().getList("alertMessages.id");
        softAssert.assertTrue(alertIds.size() > 0, "Alert messages should not be empty");

        // Validate each alert message fields
        List<String> categories = response.jsonPath().getList("alertMessages.category");
        for (String category : categories) {
            softAssert.assertTrue(category.contains("moderation"), "Category should relate to moderation");
        }

        List<String> statuses = response.jsonPath().getList("alertMessages.status");
        for (String s : statuses) {
            softAssert.assertEquals(s, status, "Status should match request param");
        }

        List<String> contentTypes = response.jsonPath().getList("alertMessages.contentType");
        for (String ct : contentTypes) {
            softAssert.assertEquals(ct, "text", "Content type should be 'text'");
        }

        // Pagination checks
        softAssert.assertEquals(response.jsonPath().getInt("pagination.currentPage"), Integer.parseInt(page_number), "Page number mismatch");
        softAssert.assertEquals(response.jsonPath().getInt("pagination.pageSize"), Integer.parseInt(page_size), "Page size mismatch");

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Moderation Alert Messages")
    public Object[][] getModerationAlertTestDataFromExcel() {
        return Postive_Data_Extractor.ExcelData("Get Moderation Alert Messages");
    }

    @Test(dataProvider = "Get Car Features")
    @Parameters({"page_number", "page_size"})
        public void  getCarFeaturesData(String page_number, String page_size) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .queryParam("page_number", page_number)
                .queryParam("page_size", page_size)
                .when()
                .get("/v1/inventory/car_feature")
                .then()
                .log().all()
                .extract().response();
        System.out.println("API Response: " + response.asString());

        softAssert.assertEquals(response.getStatusCode(), 200, "Status code should be 200 OK");

        List<Map<String, Object>> featureList = response.jsonPath().getList("carFeatureList");

        softAssert.assertNotNull(featureList, "featureList should not be null");



        List<Integer> featureIds = featureList.stream().map(f -> (Integer) f.get("id")).toList();
        List<String> featureNames = featureList.stream().map(f -> (String) f.get("name")).toList();


        int currentPage = response.jsonPath().getInt("pagination.currentPage");
        int pageSize = response.jsonPath().getInt("pagination.pageSize");

        softAssert.assertEquals(currentPage, Integer.parseInt(page_number), "Mismatch in currentPage");
        softAssert.assertEquals(pageSize, Integer.parseInt(page_size), "Mismatch in pageSize");

        softAssert.assertAll();

    }

    @DataProvider(name = "Get Car Features")
    public Object[][] getCarFeaturesData() {
        return Postive_Data_Extractor.ExcelData("Get Car Features");
    }



    @Test(dataProvider = "Get Car Inspection")
    @Parameters({"id"})
    public void getCarInspectionData(String id) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .pathParam("id", id)
                .when()
                .get("/v1/inspection/{id}")
                .then()
                .log().all()
                .extract().response();

        System.out.println("API Response: " + response.asString());

        // Validate status code
        softAssert.assertEquals(response.statusCode(), 200, "Expected status code 200");

        // Validate ID matches
        softAssert.assertEquals(response.jsonPath().getString("id"), id, "ID should match");

        // Validate top-level fields
        softAssert.assertNotNull(response.jsonPath().getString("carId"), "Car ID should not be null");
        softAssert.assertEquals(response.jsonPath().getString("status"), "completed", "Status should be completed");
        softAssert.assertTrue(response.jsonPath().getString("pdfReport").contains(".pdf"), "PDF report link should be present");

        // Validate result.sections is not empty
        List<Object> sections = response.jsonPath().getList("result.sections");
        softAssert.assertTrue(sections.size() > 0, "Inspection sections should not be empty");

        // Validate a sample section and item condition
        List<String> sectionNames = response.jsonPath().getList("result.sections.name");
        softAssert.assertTrue(sectionNames.contains("engine"), "Engine section should exist");

        List<List<String>> conditionGroups = response.jsonPath().getList("result.sections.inspectionItems.condition");

        for (List<String> conditionList : conditionGroups) {
            for (String condition : conditionList) {
                softAssert.assertTrue(condition.equalsIgnoreCase("good"), "Each condition should be good");
            }
        }

        // Validate score exists
        float score = response.jsonPath().getFloat("score");
        softAssert.assertTrue(score >= 0 && score <= 5, "Score should be between 0 and 5");

        // Validate car model
        softAssert.assertEquals(response.jsonPath().getString("car.model.name"), "Corolla", "Model should be Corolla");
        softAssert.assertEquals(response.jsonPath().getString("car.model.make.name"), "Toyota", "Make should be Toyota");

        // Pagination not applicable here

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Car Inspection")
    public Object[][] getCarInspectionData() {
        return Postive_Data_Extractor.ExcelData("Get Car Inspection");
    }

    @Test(dataProvider = "Get Car Media Data")
    @Parameters({"page_number", "page_size", "car_id"})
    public void getCarMediaTestData(String page_number, String page_size, String car_id) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .queryParam("page_number", page_number)
                .queryParam("page_size", page_size)
                .queryParam("car_id", car_id)
                .when()
                .get("/v1/inventory/car_media")
                .then()
                .log().all()
                .extract().response();

        // Status Code Check
        softAssert.assertEquals(response.statusCode(), 200, "Expected HTTP 200 OK");

        // Extract and Validate Media Details
        List<String> mediaTypes = response.jsonPath().getList("carMediaList.type");
        for (String type : mediaTypes) {
            softAssert.assertTrue(type.startsWith("image"), "Media type should be image, found: " + type);
        }

        List<String> carIds = response.jsonPath().getList("carMediaList.carId");


        List<String> mediaUrls = response.jsonPath().getList("carMediaList.url");
        for (String url : mediaUrls) {
            softAssert.assertTrue(url.startsWith("https://"), "URL should be a valid https link");
        }

        // Pagination checks
        softAssert.assertEquals(response.jsonPath().getInt("pagination.currentPage"), Integer.parseInt(page_number), "Page number mismatch");
        softAssert.assertEquals(response.jsonPath().getInt("pagination.pageSize"), Integer.parseInt(page_size), "Page size mismatch");

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Car Media Data")
    public Object[][] getCarMediaTestData() {
        return Postive_Data_Extractor.ExcelData("Get Car Media Data");
    }


    @Test(dataProvider = "Get Inspection Request Data")
    @Parameters({"country", "page_number", "page_size", "is_existing_car", "type"})
    public void getInspectionRequestData(String country, String page_number, String page_size, String is_existing_car, String type) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .queryParam("country", country)
                .queryParam("page_number", page_number)
                .queryParam("page_size", page_size)
                .queryParam("is_existing_car", is_existing_car)
                .queryParam("type", type)
                .when()
                .get("/v1/inventory/inspection_request")
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Expected HTTP 200 OK");

        // Basic pagination check
        softAssert.assertEquals(response.jsonPath().getInt("pagination.currentPage"), Integer.parseInt(page_number), "Current page mismatch");
        softAssert.assertEquals(response.jsonPath().getInt("pagination.pageSize"), Integer.parseInt(page_size), "Page size mismatch");

        // Validate inspectionRequestStatus values
        List<Map<String, Object>> inspectionRequests = response.jsonPath().getList("inspectionRequests");

        for (Map<String, Object> request : inspectionRequests) {
            Object summaryObj = request.get("inspectionRequestSummary");

            if (summaryObj instanceof List<?>) {
                List<?> summaries = (List<?>) summaryObj;

                for (Object summary : summaries) {
                    if (summary instanceof Map<?, ?> summaryMap) {
                        softAssert.assertTrue(summaryMap.containsKey("inspectionStatus"), "Missing key: inspectionStatus");
                        softAssert.assertTrue(summaryMap.containsKey("createdAt"), "Missing key: createdAt");
                    }
                }
            } else {
                softAssert.fail("inspectionRequestSummary is not a List");
            }
        }


        softAssert.assertAll();
    }
    @DataProvider(name = "Get Inspection Request Data")
    public Object[][] getInspectionRequestData() {
        return Postive_Data_Extractor.ExcelData("Get Inspection Request Data");
    }



    @Test(dataProvider = "Get Car Latest Inspection")
    @Parameters({"id","full","type"})
    public void getCarLatestInspectionTest(String id, String full, String type) {
        SoftAssert softAssert = new SoftAssert();
        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .queryParam("full", full)
                .queryParam("type", type)
                .when()
                .get("/v1/inventory/car/" + id + "/inspection/marketplace")
                .then()
                .log().all()
                .extract().response();

        System.out.println("API Response: " + response.asString());

        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        JsonPath json = response.jsonPath();




//        // Validate root-level fields
//        softAssert.assertEquals(json.getString("carId"), id, "Car ID mismatch");
//        softAssert.assertEquals(json.getString("status"), "completed", "Status should be completed");
////        softAssert.assertTrue(json.getString("pdfReport").contains("https://"), "PDF report link should be valid");
//
//        // Validate result object
//        softAssert.assertNotNull(json.getList("result.sections"), "Inspection sections should not be null");
//        softAssert.assertTrue(json.getFloat("score") >= 0, "Score should be non-negative");


softAssert.assertAll();

    }

    @DataProvider(name = "Get Car Latest Inspection")
    public Object[][] getCarInspectionDataFromExcel() {
        return Postive_Data_Extractor.ExcelData("Get Car Latest Inspection");
    }



    @Test(dataProvider = "Get Car Document")
    @Parameters({"page_number", "page_size", "car_id"})
    public void getCarDocumentsTest(String page_number, String page_size, String car_id) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .queryParam("page_number", page_number)
                .queryParam("page_size", page_size)
                .queryParam("car_id", car_id)
                .when()
                .get("/v1/inventory/car_document")
                .then()
                .log().all()
                .extract().response();

        // Status Code Check
        softAssert.assertEquals(response.statusCode(), 200, "Expected HTTP 200 OK");

        // Extract and Validate cardocumentList
        List<String> documentNames = response.jsonPath().getList("cardocumentList.name");
        for (String name : documentNames) {
            softAssert.assertNotNull(name, "Document name should not be null");
        }

        List<String> statuses = response.jsonPath().getList("cardocumentList.status");
        for (String status : statuses) {
            softAssert.assertEquals(status, "submitted", "Document status should be 'submitted'");
        }

        List<String> urls = response.jsonPath().getList("cardocumentList.url");
        for (String url : urls) {
            softAssert.assertTrue(url.startsWith("https://"), "Document URL should start with https://");
        }

        List<Integer> ids = response.jsonPath().getList("cardocumentList.id");
        for (Integer id : ids) {
            softAssert.assertNotNull(id, "Document ID should not be null");
        }

        // Pagination checks
        softAssert.assertEquals(response.jsonPath().getInt("pagination.currentPage"), Integer.parseInt(page_number), "Page number mismatch");
        softAssert.assertEquals(response.jsonPath().getInt("pagination.pageSize"), Integer.parseInt(page_size), "Page size mismatch");

        softAssert.assertAll();
    }

    @DataProvider(name = "Get Car Document")
    public Object[][] getCarDocumentTestData() {
        return Postive_Data_Extractor.ExcelData("Get Car Document");
    }


    @Test
    public void createFranchiseTest() {
        SoftAssert softAssert = new SoftAssert();

        // Generate unique values
        // Random unique generators
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        Random rand = new Random();
        int randomNum = 10000 + rand.nextInt(90000);

        String name = "Testfranchise_" + uuid;
        String email = "autotest" + randomNum + "@mail.com";
        String phone = "090" + (1000000 + rand.nextInt(8999999));
        String accountNumber = String.valueOf(100000 + rand.nextInt(900000));
        String accountBvn = String.valueOf(200000 + rand.nextInt(900000));

        // Build the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("owner", "diyrHzEg6");
        requestBody.put("email", email);
        requestBody.put("phonenumber", phone);
        requestBody.put("city_id", "1309");
        requestBody.put("account_name", "TestUser");
        requestBody.put("bank_name", "Autobank");
        requestBody.put("account_number", accountNumber);
        requestBody.put("account_bvn", accountBvn);
        requestBody.put("type", "dealer");
        requestBody.put("display_address", "Autocity");
        requestBody.put("address", "123 Main Street");
        requestBody.put("is_anchor", false);
        requestBody.put("latitude", 9.081999);
        requestBody.put("longitude", 8.675277);
        requestBody.put("onboarding_source", "diyrHzEg6");
        requestBody.put("map_address", "Nigeria");

        // Perform POST request
        Response response = given()
              //  .baseUri("https://api.staging.myautochek.com")
                .basePath("/v1/franchise")
                .header("Authorization", "Bearer " + Initialization.consoleAdminToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post()
                .then()
                .log().all()
                .extract().response();

        // Basic assertions
        softAssert.assertEquals(response.statusCode(), 200, "Expected 200 OK on franchise creation");

        // Optional response structure check
        softAssert.assertNotNull(response.jsonPath().getString("id"), "Franchise ID should not be null");
        softAssert.assertEquals(response.jsonPath().getString("name"), name, "Franchise name mismatch");
        softAssert.assertEquals(response.jsonPath().getString("email"), email, "Franchise email mismatch");

        softAssert.assertAll();
    }

    @Test(dataProvider = "Get User Profile")
    @Parameters({"user_id"})
    public void getUserProfile(String user_id) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .when()
                .get("/v1/user/" + user_id)
                .then()
                .log().all()
                .extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status Code should be 200");

        String id = response.jsonPath().getString("id");
        String email = response.jsonPath().getString("email");
        Boolean active = response.jsonPath().getBoolean("active");
        List<Map<String, Object>> roles = response.jsonPath().getList("roles");

        softAssert.assertEquals(id, user_id, "User ID should match");
        softAssert.assertTrue(email.contains("@"), "Email should be valid");
        softAssert.assertTrue(active, "User should be active");
        softAssert.assertTrue(roles.size() > 0, "User should have at least one role");

        boolean hasFranchiseAdmin = roles.stream()
                .anyMatch(role -> "FRANCHISE ADMIN".equalsIgnoreCase((String) role.get("name")));
        softAssert.assertTrue(hasFranchiseAdmin, "User should have role 'FRANCHISE ADMIN'");

        List<Map<String, Object>> permissions = response.jsonPath().getList("roles[0].permissions");
        boolean hasUpdateCarPermission = permissions.stream()
                .anyMatch(p -> "CAN_UPDATE_CAR".equalsIgnoreCase((String) p.get("name")));
        softAssert.assertTrue(hasUpdateCarPermission, "User should have permission 'CAN_UPDATE_CAR'");

        softAssert.assertAll();
    }

    @DataProvider(name = "Get User Profile")
    public Object[][] getUserProfileData() {
        return Postive_Data_Extractor.ExcelData("Get User Profile");
    }

    @Test(dataProvider = "Dealer Tiers")
    @Parameters({"id"})
    public void dealerTierSummaryTest(String id) {
        SoftAssert softAssert = new SoftAssert();

        Response response = given()
                .header("Authorization", "Bearer " + Initialization.franchiseAdminToken)
                .pathParam("id", id)
                .when()
                .get("/v1/franchise/{id}/dealer-tiers/summary")
                .then()
                .log().all()
                .extract().response();

        System.out.println("Response Body: " + response.asString());
        softAssert.assertEquals(response.statusCode(), 200, "Expected status code 200");

        JsonPath json = response.jsonPath();

        // Soft assertions for the expected fields
        softAssert.assertEquals(json.getString("id"), "674", "Mismatch in id");
        softAssert.assertNotNull(json.getString("dealership"), "Dealership should not be null");
        softAssert.assertNotNull(json.getString("level"), "Level should not be null");

        softAssert.assertTrue(json.getInt("points") >= 0, "Points should be >= 0");
        softAssert.assertTrue(json.getInt("potentialPoints") >= 0, "Potential Points should be >= 0");
        softAssert.assertTrue(json.getInt("lostPoints") >= 0, "Lost Points should be >= 0");

        softAssert.assertTrue(json.getInt("commissions") >= 0, "Commissions should be >= 0");
        softAssert.assertTrue(json.getInt("revenue") >= 0, "Revenue should be >= 0");

        softAssert.assertTrue(json.getString("conversionRate").contains("/"), "Invalid conversion rate format");
        softAssert.assertTrue(json.getInt("position") >= 0, "Position should be >= 0");
        softAssert.assertTrue(json.getInt("totalPosition") > 0, "Total Position should be > 0");

        softAssert.assertAll();
    }

    @DataProvider(name = "Dealer Tiers")
    public Object[][] getDealerTierDataFromExcel() {
        return Postive_Data_Extractor.ExcelData("Dealer Tiers");
    }
}



