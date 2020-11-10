package uk.gov.hmcts.futurehearings.hmi.functional.directlisting.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.futurehearings.hmi.functional.common.rest.RestClientTemplate.callRestEndpointWithPayload;
import static uk.gov.hmcts.futurehearings.hmi.functional.common.rest.RestClientTemplate.callRestEndpointWithQueryParams;
import static uk.gov.hmcts.futurehearings.hmi.functional.directlisting.process.DirectListingResponseProcess.getSessionId;

import java.util.Map;

import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class DirectListingSteps {

    private String actor;

    @Step("User makes a request to Get the Sessions for relevant search parameters on the Sessions API")
    public String getSessionIdForDirectListing(final String apiURL,
                                               final Map<String, Object> headersAsMap,
                                               final String authorizationToken,
                                               final Map<String, String> queryParameters) {

        return getSessionId(callRestEndpointWithQueryParams(apiURL,
                headersAsMap,
                authorizationToken,
                queryParameters, HttpStatus.OK));
    }

    @Step("User makes a request to List a Hearing Request Directly (PUT in the Listing API)")
    public void performDirectListingForGivenSessionId(final String apiURL,
                                                        final Map<String, Object> headersAsMap,
                                                        final String authorizationToken,
                                                        final String body) {
        Response response = callRestEndpointWithPayload(apiURL,
                headersAsMap,
                authorizationToken,
                body, HttpMethod.PUT,HttpStatus.NO_CONTENT);
        assertEquals(HttpStatus.NO_CONTENT.value(),response.getStatusCode());
    }
}
