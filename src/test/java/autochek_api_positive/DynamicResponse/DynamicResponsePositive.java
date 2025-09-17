package autochek_api_positive.DynamicResponse;

import Base.Initialization;
import Listener.AllureLogs;
import Utils.Postive_Data_Extractor;
import io.qameta.allure.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@Epic("Inventory Service")
@Feature("Positive Tests Dynamic Response")
public class DynamicResponsePositive {
    Initialization initialization = new Initialization();

    // Global variables used across tests
    private static String franchiseId;
    private static String user_id;
    private static String token;
    private static String Consoletoken;

    private static String status = "pending";
    private static String country = "NG";

    // Stores created franchise data for validation across tests
    private static final Map<String, Map<String, String>> franchiseResponseMap = new LinkedHashMap<>();

    @BeforeClass
    public void setUp() {
        initialization.initializeEnvironment(); // Sets base URI and tokens
        user_id = Initialization.franchiseAdminToken.get("userId");
        token = Initialization.franchiseAdminToken.get("token");
        Consoletoken = Initialization.consoleAdminToken.get("token");
    }

    @Test(dataProvider = "Get Cars")
    @Description("Validates GET /v1/inventory/car with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Cars List")
    public void GetCarList(String type, String severity, String testDescription,
                             String fields, String relations, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        RequestSpecification request = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", country);

        if (fields != null && !fields.equals("null") && !fields.trim().isEmpty()) {
            request.queryParam("fields", fields);
        } else if (relations != null && !relations.equals("null") && !relations.trim().isEmpty()) {
            request.queryParam("relations", relations);
        }

        // ✅ Send GET request
        Response response = request
                .when()
                .get("/v1/inventory/car")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status code validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );

        List<Map<String, Object>> carList = response.jsonPath().getList("carList");
        AllureLogs.softAssertTrue(softAssert,
                carList != null && !carList.isEmpty(),
                "Car list should not be empty");

        // ✅ Validate filtering
        if (fields != null && !fields.equals("null") && !fields.trim().isEmpty()) {
            validateFieldsFiltering(carList, fields, softAssert);
        }
        if (relations != null && !relations.equals("null") && !relations.trim().isEmpty()) {
            validateRelationsFiltering(carList, relations, softAssert);
        }

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Cars")
    public Object[][] GetCarList() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Postive_Data_Extractor.ExcelData("Get Cars");
    }
    /**
     * ✅ Helper method: Validate fields filtering
     * Checks that only the requested fields are present (or their parent object)
     */
    private void validateFieldsFiltering(List<Map<String, Object>> carList, String fields, SoftAssert softAssert) {
        Map<String, Set<String>> includedMap = new HashMap<>();
        Map<String, Set<String>> excludedMap = new HashMap<>();
        Set<String> topLevelFields = new HashSet<>();

        // New tracking
        Set<String> excludedTopFields = new HashSet<>();
        Set<String> includedOnlyFields = new HashSet<>();
        boolean hasWildcard = false;

        // Parse fields string
        for (String token : fields.split(",")) {
            token = token.trim();
            if (token.equals("*")) {
                hasWildcard = true;
            } else if (token.startsWith("-")) {
                excludedTopFields.add(token.substring(1));
            } else if (!token.contains("(") && !token.startsWith("*.")) {
                includedOnlyFields.add(token);
            }
        }

        parseFields(null, fields, includedMap, excludedMap, topLevelFields);

        Set<String> ignoredFields = Set.of(
                "features","carFeatures","modelFeatures","delistReason",
                "damageMedia","damages","carMedias","media",
                "trimFeatures","trims"
        );

        for (int i = 0; i < carList.size(); i++) {
            validateObject(
                    carList.get(i),
                    "",
                    includedMap,
                    excludedMap,
                    topLevelFields,
                    ignoredFields,
                    softAssert,
                    i,
                    hasWildcard,
                    excludedTopFields,
                    includedOnlyFields
            );
        }
    }

    private void parseFields(String parent, String fields,
                             Map<String, Set<String>> includeMap, Map<String, Set<String>> excludeMap,
                             Set<String> topLevelFields) {

        int depth = 0;
        StringBuilder token = new StringBuilder();
        for (char c : fields.toCharArray()) {
            if (c == ',' && depth == 0) {
                processToken(parent, token.toString().trim(), includeMap, excludeMap, topLevelFields);
                token.setLength(0);
            } else {
                if (c == '(') depth++;
                if (c == ')') depth--;
                token.append(c);
            }
        }
        if (token.length() > 0) {
            processToken(parent, token.toString().trim(), includeMap, excludeMap, topLevelFields);
        }
    }

    private void processToken(String parent, String token,
                              Map<String, Set<String>> includeMap, Map<String, Set<String>> excludeMap,
                              Set<String> topLevelFields) {

        if (token.contains("(")) {
            String field = token.substring(0, token.indexOf("(")).trim();
            String inner = token.substring(token.indexOf("(") + 1, token.lastIndexOf(")"));
            if (parent == null) topLevelFields.add(field);
            String fullParent = (parent == null ? field : parent + "." + field);
            parseFields(fullParent, inner, includeMap, excludeMap, topLevelFields);
        } else {
            boolean isExcluded = token.startsWith("-");
            String clean = isExcluded ? token.substring(1) : token;

            if (parent == null) {
                topLevelFields.add(clean);
            } else if (isExcluded) {
                excludeMap.computeIfAbsent(parent, k -> new HashSet<>()).add(clean);
            } else {
                includeMap.computeIfAbsent(parent, k -> new HashSet<>()).add(clean);
            }
        }
    }

    private void validateObject(Map<String, Object> obj, String path,
                                Map<String, Set<String>> includeMap, Map<String, Set<String>> excludeMap,
                                Set<String> topLevelFields, Set<String> ignoredFields,
                                SoftAssert softAssert, int index,
                                boolean hasWildcard,
                                Set<String> excludedTopFields,
                                Set<String> includedOnlyFields) {

        for (String actualKey : obj.keySet()) {
            if (ignoredFields.contains(actualKey)) continue;

            String fullPath = path.isEmpty() ? actualKey : path + "." + actualKey;

            // ✅ If wildcard used
            if (path.isEmpty() && hasWildcard) {
                // If exclusions are present: check excluded fields are NOT present
                if (!excludedTopFields.isEmpty()) {
                    softAssert.assertFalse(excludedTopFields.contains(actualKey),
                            "Excluded field '" + actualKey + "' found in carList[" + index + "]");
                }

                // If only specific fields are allowed (like "*,id")
                if (!includedOnlyFields.isEmpty() && excludedTopFields.isEmpty()) {
                    softAssert.assertTrue(includedOnlyFields.contains(actualKey),
                            "Unexpected field '" + actualKey + "' present in carList[" + index + "] when only " + includedOnlyFields + " are allowed");
                }
            }

            // ✅ Normal top-level validation if not wildcard
            if (!hasWildcard && path.isEmpty()) {
                softAssert.assertTrue(topLevelFields.contains(actualKey),
                        "Unexpected field '" + actualKey + "' in carList[" + index + "]");
            }

            // ✅ Nested include/exclude validations
            if (includeMap.containsKey(path)) {
                softAssert.assertTrue(includeMap.get(path).contains(actualKey),
                        "Unexpected nested field '" + actualKey + "' in '" + path + "' of carList[" + index + "]");
            }

            if (excludeMap.containsKey(path)) {
                softAssert.assertFalse(excludeMap.get(path).contains(actualKey),
                        "Excluded field '" + actualKey + "' found in '" + path + "' of carList[" + index + "]");
            }

            // ✅ Recurse into nested objects
            if (obj.get(actualKey) instanceof Map) {
                validateObject((Map<String, Object>) obj.get(actualKey), fullPath,
                        includeMap, excludeMap, topLevelFields, ignoredFields,
                        softAssert, index, hasWildcard, excludedTopFields, includedOnlyFields);
            }
        }

        if (includeMap.containsKey(path)) {
            for (String expected : includeMap.get(path)) {
                softAssert.assertTrue(obj.containsKey(expected),
                        "Expected field '" + expected + "' missing in '" + path + "' of carList[" + index + "]");
            }
        }
    }
    /**
     * ✅ Helper method: Validate relations filtering
     * Confirms that related objects exist when requested
     */
   // @SuppressWarnings("unchecked")
    private void validateRelationsFiltering(List<Map<String, Object>> carList, String relations, SoftAssert softAssert) {
        // Parse relations into parent -> child set
        Map<String, Set<String>> relationMap = new HashMap<>();

        for (String rel : relations.split(",")) {
            rel = rel.trim();
            if (rel.contains("(") && rel.endsWith(")")) {
                String parent = rel.substring(0, rel.indexOf("(")).trim();
                String inner = rel.substring(rel.indexOf("(") + 1, rel.length() - 1);
                Set<String> children = Arrays.stream(inner.split(","))
                        .map(String::trim)
                        .collect(Collectors.toSet());
                relationMap.put(parent, children);
            } else {
                relationMap.put(rel, Collections.emptySet());
            }
        }

        for (int i = 0; i < carList.size(); i++) {
            Map<String, Object> car = carList.get(i);

            for (Map.Entry<String, Set<String>> entry : relationMap.entrySet()) {
                String parent = entry.getKey();
                Set<String> children = entry.getValue();

                // ✅ parent must exist
                softAssert.assertTrue(car.containsKey(parent),
                        "carList[" + i + "] should contain '" + parent + "' when relations=" + relations);

                if (children.isEmpty()) continue;

                Object parentObj = car.get(parent);
                if (parentObj instanceof Map) {
                    Map<String, Object> parentMap = (Map<String, Object>) parentObj;
                    for (String child : children) {
                        softAssert.assertTrue(parentMap.containsKey(child),
                                "carList[" + i + "]." + parent + " should contain '" + child +
                                        "' when relations includes " + parent + "(" + child + ")");
                    }
                } else {
                    softAssert.fail("carList[" + i + "]." + parent + " is not an object but expected nested relation");
                }
            }
        }
    }


    @Test(dataProvider = "Get Make")
    @Description("Validates GET /v1/inventory/make with dynamic fields filtering - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Make")
    public void GetMake(String type, String severity, String testDescription,
                        String fields, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        // Send GET request with fields param
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("fields", fields)
                .when()
                .get("/v1/inventory/make")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status Code Validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );
        validateMakeListFields(response, fields, softAssert);

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Make")
    public Object[][] Getmake() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Postive_Data_Extractor.ExcelData("Get make");
    }
    private void validateMakeListFields(Response response, String fields, SoftAssert softAssert) {
        List<Map<String, Object>> makeList = response.jsonPath().getList("makeList");

        if (makeList == null || makeList.isEmpty()) {
            AllureLogs.step("makeList is empty — nothing to validate");
            return;
        }

        Map<String, Object> firstObject = makeList.get(0);

        boolean hasWildcard = false;
        Set<String> excludedFields = new HashSet<>();
        Set<String> includedOnlyFields = new HashSet<>();

        for (String f : fields.split(",")) {
            f = f.trim();
            if (f.equals("*")) {
                hasWildcard = true;
            } else if (f.startsWith("-")) {
                excludedFields.add(f.substring(1));
            } else if (!f.isEmpty()) {
                includedOnlyFields.add(f);
            }
        }

        // ✅ If wildcard present
        if (hasWildcard) {
            // Case 1: * with exclusions (like *,-id,-name)
            if (!excludedFields.isEmpty()) {
                for (String excluded : excludedFields) {
                    AllureLogs.softAssertTrue(
                            softAssert,
                            !firstObject.containsKey(excluded),
                            "Excluded field '" + excluded + "' was found in first makeList object"
                    );
                }
            }

            // Case 2: * with inclusions (like *,id) → only id should appear
            if (!includedOnlyFields.isEmpty() && excludedFields.isEmpty()) {
                for (String actual : firstObject.keySet()) {
                    AllureLogs.softAssertTrue(
                            softAssert,
                            includedOnlyFields.contains(actual),
                            "Unexpected field '" + actual + "' found in first makeList object when only " + includedOnlyFields + " are allowed"
                    );
                }
                for (String expected : includedOnlyFields) {
                    AllureLogs.softAssertTrue(
                            softAssert,
                            firstObject.containsKey(expected),
                            "Expected field '" + expected + "' is missing in first makeList object"
                    );
                }
            }
        }
        // ✅ Normal case — no wildcard
        else {
            if (!includedOnlyFields.isEmpty()) {
                for (String field : includedOnlyFields) {
                    AllureLogs.softAssertTrue(
                            softAssert,
                            firstObject.containsKey(field),
                            "Expected field '" + field + "' is missing in first makeList object"
                    );
                }
                for (String actual : firstObject.keySet()) {
                    AllureLogs.softAssertTrue(
                            softAssert,
                            includedOnlyFields.contains(actual),
                            "Unexpected field '" + actual + "' found in first makeList object"
                    );
                }
            }

            if (!excludedFields.isEmpty()) {
                for (String excluded : excludedFields) {
                    AllureLogs.softAssertTrue(
                            softAssert,
                            !firstObject.containsKey(excluded),
                            "Excluded field '" + excluded + "' was found in first makeList object"
                    );
                }
            }
        }
    }

    @Test(dataProvider = "Get Model")
    @Description("Validates GET /v1/inventory/model with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Model List")
    public void GetModelList(String type, String severity, String testDescription,
                           String fields, String relations, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        RequestSpecification request = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", country);

        if (fields != null && !fields.equals("null") && !fields.trim().isEmpty()) {
            request.queryParam("fields", fields);
        } else if (relations != null && !relations.equals("null") && !relations.trim().isEmpty()) {
            request.queryParam("relations", relations);
        }

        // ✅ Send GET request
        Response response = request
                .when()
                .get("/v1/inventory/model")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status code validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );

        List<Map<String, Object>> modelList = response.jsonPath().getList("modelList");
        AllureLogs.softAssertTrue(softAssert,
                modelList != null && !modelList.isEmpty(),
                "modelList list should not be empty");

        // ✅ Validate filtering
        if (fields != null && !fields.equals("null") && !fields.trim().isEmpty()) {
            validateFieldsFiltering(modelList, fields, softAssert);
        }
        if (relations != null && !relations.equals("null") && !relations.trim().isEmpty()) {
            validateRelationsFiltering(modelList, relations, softAssert);
        }

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Model")
    public Object[][] GetModelList() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Postive_Data_Extractor.ExcelData("Get Model");
    }

    @Test(dataProvider = "Get Trims")
    @Description("Validates GET /v1/inventory/trims with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Trims")
    public void GetTrims(String type, String severity, String testDescription,
                        String fields, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        // Send GET request with fields param
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("fields", fields)
                .when()
                .get("/v1/inventory/trims")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status Code Validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );
        validateMakeListFields(response, fields, softAssert);

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Trims")
    public Object[][] GetTrims() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Postive_Data_Extractor.ExcelData("Get Trims");
    }


    @Test(dataProvider = "Get Inspection Request")
    @Description("Validates GET /v1/inventory/inspection_request with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Inspection Request")
    public void GetInspectionRequest(String type, String severity, String testDescription,
                        String fields, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        // Send GET request with fields param
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("fields", fields)
                .when()
                .get("/v1/inventory/inspection_request")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status Code Validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );
        validateMakeListFields(response, fields, softAssert);

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Inspection Request")
    public Object[][] GetInspectionRequest() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Postive_Data_Extractor.ExcelData("Get Inspection Request");
    }
    @Test(dataProvider = "Get Inspection")
    @Description("Validates GET /v1/inspection with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Inspection")
    public void GetInspection(String type, String severity, String testDescription,
                             String fields, String relations, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        RequestSpecification request = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("country", country);
                request.queryParam("page_number", "1");
                request.queryParam("page_size", "10");
                request.queryParam("type", "regular");


        if (fields != null && !fields.equals("null") && !fields.trim().isEmpty()) {
            request.queryParam("fields", fields);
        } else if (relations != null && !relations.equals("null") && !relations.trim().isEmpty()) {
            request.queryParam("relations", relations);
        }

        // ✅ Send GET request
        Response response = request
                .when()
                .get("/v1/inspection")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status code validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );

        List<Map<String, Object>> modelList = response.jsonPath().getList("result");
        AllureLogs.softAssertTrue(softAssert,
                modelList != null && !modelList.isEmpty(),
                "modelList list should not be empty");

        // ✅ Validate filtering
        if (fields != null && !fields.equals("null") && !fields.trim().isEmpty()) {
            validateFieldsFiltering(modelList, fields, softAssert);
        }
        if (relations != null && !relations.equals("null") && !relations.trim().isEmpty()) {
            validateRelationsFiltering(modelList, relations, softAssert);
        }

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Inspection")
    public Object[][] GetInspection() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Postive_Data_Extractor.ExcelData("Get Inspection");
    }




    @Test(dataProvider = "Get Body Type")
    @Description("Validates GET /v1/inventory/body_type with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Trims")
    public void GetBodyType(String type, String severity, String testDescription,
                         String fields, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        // Send GET request with fields param
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("fields", fields)
                .when()
                .get("/v1/inventory/body_type")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status Code Validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );
        validateMakeListFields(response, fields, softAssert);

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get Body Type")
    public Object[][] GetBodyType() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Postive_Data_Extractor.ExcelData("Get Body Type");
    }



    @Test(dataProvider = "Get obds")
    @Description("Validates GET /v1/inventory/obds with dynamic parameters - Positive Test")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get obds")
    public void GetObds(String type, String severity, String testDescription,
                            String fields, String expectedStatusCode) {

        SoftAssert softAssert = new SoftAssert();

        // Send GET request with fields param
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("fields", fields)
                .when()
                .get("/v1/inventory/obds")
                .then()
                .log().all()
                .extract().response();

        // ✅ Status Code Validation
        AllureLogs.softAssertEquals(
                softAssert,
                response.getStatusCode(),
                Integer.parseInt(expectedStatusCode),
                "Status code should match"
        );
        validateMakeListFields(response, fields, softAssert);

        AllureLogs.executeSoftAssertAll(softAssert);
    }
    @DataProvider(name = "Get obds")
    public Object[][] Getobds() {
        // Reads test data from Excel sheet named "Create Franchise"
        return Postive_Data_Extractor.ExcelData("Get obds");
    }


}
