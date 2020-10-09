package uk.gov.hmcts.futurehearings.hmi.unit.testing.testsuites;

import static io.restassured.RestAssured.given;
import static uk.gov.hmcts.futurehearings.hmi.unit.testing.util.ResourcesResponseVerifier.thenValidateResponseForInvalidResource;
import static uk.gov.hmcts.futurehearings.hmi.unit.testing.util.ResourcesResponseVerifier.thenValidateResponseForRetrieve;
import static uk.gov.hmcts.futurehearings.hmi.unit.testing.util.ResourcesResponseVerifier.thenValidateResponseForMissingSubscriptionKeyHeader;
import static uk.gov.hmcts.futurehearings.hmi.unit.testing.util.ResourcesResponseVerifier.thenValidateResponseForInvalidSubscriptionKeyHeader;
import static uk.gov.hmcts.futurehearings.hmi.unit.testing.util.ResourcesResponseVerifier.thenValidateResponseForMissingOrInvalidHeader;
import static uk.gov.hmcts.futurehearings.hmi.unit.testing.util.ResourcesResponseVerifier.thenValidateResponseForMissingOrInvalidAcceptHeader;
import static uk.gov.hmcts.futurehearings.hmi.unit.testing.util.ResourcesResponseVerifier.thenValidateResponseForMissingOrInvalidContentTypeHeader;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.futurehearings.hmi.Application;
import uk.gov.hmcts.futurehearings.hmi.unit.testing.util.TestReporter;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = {Application.class})
@ActiveProfiles("test")
@ExtendWith(TestReporter.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("GET /resources - Retrieve Resources")
@SuppressWarnings("java:S2699")
class GET_resources_UnitTests {

    @Value("${targetInstance}")
    private String targetInstance;

    @Value("${targetSubscriptionKey}")
    private String targetSubscriptionKey;

    @Value("${resourcesApiRootContext}")
    private String resourcesApiRootContext;

    private final Map<String, Object> headersAsMap = new HashMap<>();

    @BeforeEach
    void initialiseValues() {
        headersAsMap.put("Ocp-Apim-Subscription-Key", targetSubscriptionKey);
        headersAsMap.put("Content-Type", "application/json");
        headersAsMap.put("Accept", "application/json");
        headersAsMap.put("Source-System", "CFT");
        headersAsMap.put("Destination-System", "S&L");
        headersAsMap.put("Request-Type", "THEFT");
        headersAsMap.put("Request-Created-At", "2018-01-29 20:36:01Z");
        headersAsMap.put("Request-Processed-At", "2018-02-29 20:36:01Z");
    }

    @Test
    @Order(1)
    @DisplayName("Test for Invalid Resource")
    void testRetrieveResourcesRequestForInvalidResource() {
        final Response response = whenRetrieveResourcesIsInvokedForInvalidResource();
        thenValidateResponseForInvalidResource(response);
    }

    @Test
    @Order(2)
    @DisplayName("Test for missing ContentType header")
    void testRetrieveResourcesRequestWithMissingContentTypeHeader() {
        headersAsMap.remove("Content-Type");
        final Response response = whenRetrieveResourcesIsInvokedWithMissingOrInvalidHeader();
        thenValidateResponseForMissingOrInvalidContentTypeHeader(response);
    }

    @Test
    @Order(3)
    @DisplayName("Test for invalid ContentType header")
    void testRetrieveResourcesRequestWithInvalidContentTypeHeader() {
        headersAsMap.remove("Content-Type");
        headersAsMap.put("Content-Type", "application/xml");

        final Response response = whenRetrieveResourcesIsInvokedWithMissingOrInvalidHeader();
        thenValidateResponseForMissingOrInvalidContentTypeHeader(response);
    }

    @Test
    @Order(4)
    @DisplayName("Test for missing Accept header")
    void testRetrieveResourcesRequestWithMissingAcceptHeader() {
        headersAsMap.remove("Accept");
        final Response response = whenRetrieveResourcesIsInvokedWithMissingOrInvalidHeader();
        thenValidateResponseForMissingOrInvalidAcceptHeader(response);
    }

    @Test
    @Order(5)
    @DisplayName("Test for invalid Accept header")
    void testRetrieveResourcesRequestWithInvalidAcceptHeader() {
        headersAsMap.remove("Accept");
        headersAsMap.put("Accept", "application/jsonxml");

        final Response response = whenRetrieveResourcesIsInvokedWithMissingOrInvalidHeader();
        thenValidateResponseForMissingOrInvalidAcceptHeader(response);
    }

    @Test
    @Order(6)
    @DisplayName("Test for missing Ocp-Apim-Subscription-Key header")
    void testRetrieveResourcesRequestWithMissingOcpSubKey() {
        headersAsMap.remove("Ocp-Apim-Subscription-Key");
        final Response response = whenRetrieveResourcesIsInvokedWithMissingOcpSubKey();
        thenValidateResponseForMissingSubscriptionKeyHeader(response);
    }

    @Test
    @Order(7)
    @DisplayName("Test for invalid Ocp-Apim-Subscription-Key header")
    void testRetrieveResourcesRequestWithInvalidOcpSubKey() {
        headersAsMap.remove("Ocp-Apim-Subscription-Key");
        headersAsMap.put("Ocp-Apim-Subscription-Key","invalidocpsubkey");
        final Response response = whenRetrieveResourcesIsInvokedWithMissingOcpSubKey();
        thenValidateResponseForInvalidSubscriptionKeyHeader(response);
    }

    @Order(8)
    @ParameterizedTest(name = "Test for missing {0} header")
    @ValueSource(strings = {"Source-System","Destination-System","Request-Created-At","Request-Processed-At","Request-Type"})
    void testRetrieveResourcesRequestWithMissingHeader(String iteration) {
        headersAsMap.remove(iteration);

        final Response response = whenRetrieveResourcesIsInvokedWithMissingOrInvalidHeader();
        thenValidateResponseForMissingOrInvalidHeader(response, iteration);
    }

    @Order(9)
    @ParameterizedTest(name = "Test for invalid {0} header")
    @ValueSource(strings = {"Source-System","Destination-System","Request-Created-At","Request-Processed-At","Request-Type"})
    void testRetrieveResourcesRequestWithInvalidHeader(String iteration) {
        headersAsMap.remove(iteration);
        headersAsMap.put(iteration, "A");

        final Response response = whenRetrieveResourcesIsInvokedWithMissingOrInvalidHeader();
        thenValidateResponseForMissingOrInvalidHeader(response, iteration);
    }

    @Test
    @Order(18)
    @DisplayName("Test for Correct Headers and No Parameters")
    void testRetrieveResourcesRequestWithCorrectRequestAndNoParams() {
        final Response response = whenRetrieveResourcesIsInvokedWithCorrectHeadersAndNoParams();
        thenValidateResponseForRetrieve(response);
    }

    private Response whenRetrieveResourcesIsInvokedForInvalidResource() {
        return retrieveResourcesResponseForInvalidResource(resourcesApiRootContext+"get", headersAsMap, targetInstance);
    }

    private Response whenRetrieveResourcesIsInvokedWithCorrectHeadersAndNoParams() {
        return retrieveResourcesResponseForCorrectRequestAndNoParams(resourcesApiRootContext, headersAsMap, targetInstance);
    }

    private Response whenRetrieveResourcesIsInvokedWithMissingOcpSubKey() {
        return retrieveResourcesResponseForAMissingOcpSubKey(resourcesApiRootContext, headersAsMap, targetInstance);
    }

    private Response whenRetrieveResourcesIsInvokedWithMissingOrInvalidHeader() {
        return retrieveResourcesResponseForMissingOrInvalidHeader(resourcesApiRootContext, headersAsMap, targetInstance);
    }

//INDIVIDUAL RESOURCE TESTS - START

    @Test
    @Order(19)
    @DisplayName("Test for Invalid Resource - Individual Resource")
    void testRetrieveIndividualResourceRequestForInvalidResource() {
        final Response response = whenRetrieveIndividualResourceIsInvokedForInvalidResource();
        thenValidateResponseForInvalidResource(response);
    }

    @Test
    @Order(20)
    @DisplayName("Test for missing ContentType header - Individual Resource")
    void testRetrieveIndividualResourceRequestWithMissingContentTypeHeader() {
        headersAsMap.remove("Content-Type");
        final Response response = whenRetrieveIndividualResourceIsInvokedWithMissingOrInvalidHeader();
        thenValidateResponseForMissingOrInvalidContentTypeHeader(response);
    }
    @Test
    @Order(21)
    @DisplayName("Test for invalid ContentType header - Individual Resource")
    void testRetrieveIndividualResourcesRequestWithInvalidContentTypeHeader() {
        headersAsMap.remove("Content-Type");
        headersAsMap.put("Content-Type", "application/xml");

        final Response response = whenRetrieveResourcesIsInvokedWithMissingOrInvalidHeader();
        thenValidateResponseForMissingOrInvalidContentTypeHeader(response);
    }

    @Test
    @Order(22)
    @DisplayName("Test for missing Accept header - Individual Resource")
    void testRetrieveIndividualResourceRequestWithMissingAcceptHeader() {
        headersAsMap.remove("Accept");
        final Response response = whenRetrieveIndividualResourceIsInvokedWithMissingOrInvalidHeader();
        thenValidateResponseForMissingOrInvalidAcceptHeader(response);
    }

    @Test
    @Order(23)
    @DisplayName("Test for invalid Accept header - Individual Resource")
    void testRetrieveIndividualResourceRequestWithInvalidAcceptHeader() {
        headersAsMap.remove("Accept");
        headersAsMap.put("Accept", "application/jsonxml");

        final Response response = whenRetrieveResourcesIsInvokedWithMissingOrInvalidHeader();
        thenValidateResponseForMissingOrInvalidAcceptHeader(response);
    }

    @Test
    @Order(24)
    @DisplayName("Test for missing Ocp-Apim-Subscription-Key header - Individual Resource")
    void testRetrieveIndividualResourceRequestWithMissingOcpSubKey() {
        headersAsMap.remove("Ocp-Apim-Subscription-Key");
        final Response response = whenRetrieveIndividualResourceIsInvokedWithMissingOcpSubKey();
        thenValidateResponseForMissingSubscriptionKeyHeader(response);
    }

    @Test
    @Order(25)
    @DisplayName("Test for invalid Ocp-Apim-Subscription-Key header - Individual Resource")
    void testRetrieveIndividualResourceRequestWithInvalidOcpSubKey() {
        headersAsMap.remove("Ocp-Apim-Subscription-Key");
        headersAsMap.put("Ocp-Apim-Subscription-Key","invalidocpsubkey");
        final Response response = whenRetrieveIndividualResourceIsInvokedWithMissingOcpSubKey();
        thenValidateResponseForInvalidSubscriptionKeyHeader(response);
    }

    @Order(26)
    @ParameterizedTest(name = "Test for missing {0} header")
    @ValueSource(strings = {"Source-System","Destination-System","Request-Created-At","Request-Processed-At","Request-Type"})
    void testRetrieveIndividualResourcesRequestWithMissingHeader(String iteration) {
        headersAsMap.remove(iteration);

        final Response response = whenRetrieveIndividualResourceIsInvokedWithMissingOrInvalidHeader();
        thenValidateResponseForMissingOrInvalidHeader(response, iteration);
    }

    @Order(27)
    @ParameterizedTest(name = "Test for invalid {0} header")
    @ValueSource(strings = {"Source-System","Destination-System","Request-Created-At","Request-Processed-At","Request-Type"})
    void testRetrieveIndividualResourcesRequestWithInvalidHeader(String iteration) {
        headersAsMap.remove(iteration);
        headersAsMap.put(iteration, "A");

        final Response response = whenRetrieveIndividualResourceIsInvokedWithMissingOrInvalidHeader();
        thenValidateResponseForMissingOrInvalidHeader(response, iteration);
    }

    @Test
    @Order(36)
    @DisplayName("Test for No Parameters - Individual Resource")
    void testRetrieveIndividualResourceRequestWithCorrectRequestAndNoParams() {
        final Response response = whenRetrieveIndividualResourceIsInvokedWithCorrectHeadersAndNoParams();
        thenValidateResponseForRetrieve(response);
    }

    private Response whenRetrieveIndividualResourceIsInvokedForInvalidResource() {
        return retrieveResourcesResponseForInvalidResource(resourcesApiRootContext+"get"+"/CASE123432", headersAsMap, targetInstance);
    }

    private Response whenRetrieveIndividualResourceIsInvokedWithCorrectHeadersAndNoParams() {
        return retrieveResourcesResponseForCorrectRequestAndNoParams(resourcesApiRootContext+ "/CASE123432", headersAsMap, targetInstance);
    }

    private Response whenRetrieveIndividualResourceIsInvokedWithMissingOcpSubKey() {
        return retrieveResourcesResponseForAMissingOcpSubKey(resourcesApiRootContext+ "/CASE123432", headersAsMap, targetInstance);
    }

    private Response whenRetrieveIndividualResourceIsInvokedWithMissingOrInvalidHeader() {
        return retrieveResourcesResponseForMissingOrInvalidHeader(resourcesApiRootContext+ "/CASE123432", headersAsMap, targetInstance);
    }


//INDIVIDUAL RESOURCE TESTS - END

    private Response retrieveResourcesResponseForInvalidResource(final String api, final Map<String, Object> headersAsMap, final String basePath) {

        return given()
                .headers(headersAsMap)
                .baseUri(basePath)
                .basePath(api)
                .when().get().then().extract().response();
    }

    private Response retrieveResourcesResponseForCorrectRequestAndNoParams(final String api, final Map<String, Object> headersAsMap, final String basePath) {

        return given()
                .headers(headersAsMap)
                .baseUri(basePath)
                .basePath(api)
                .when().get().then().extract().response();
    }



    private Response retrieveResourcesResponseForAMissingOcpSubKey(final String api, final Map<String, Object> headersAsMap, final String basePath) {

        return given()
                .headers(headersAsMap)
                .baseUri(basePath)
                .basePath(api)
                .when().get().then().extract().response();
    }

    private Response retrieveResourcesResponseForMissingOrInvalidHeader(final String api, final Map<String, Object> headersAsMap, final String basePath) {

        return given()
                .headers(headersAsMap)
                .baseUri(basePath)
                .basePath(api)
                .when().get().then().extract().response();

    }


}