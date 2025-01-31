package uk.gov.hmcts.futurehearings.hmi.smoke.sessions;

import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.futurehearings.hmi.Application;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.hmcts.futurehearings.hmi.smoke.common.rest.RestClient;
import uk.gov.hmcts.futurehearings.hmi.smoke.common.test.SmokeTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(classes = {Application.class})
@ActiveProfiles("smoke")
@DisplayName("Smoke Test for the HMI Sessions Context")
@SuppressWarnings("java:S2187")
class SessionsApiSmokeTest extends SmokeTest {

    @Value("${sessionsApiRootContext}")
    private String sessionsApiRootContext;

    @BeforeAll
    public void initialiseValues() throws Exception {
        this.setDestinationSystem("SNL");
        super.initialiseValues();
        setRootContext(sessionsApiRootContext);
    }

    @Test
    @DisplayName("Smoke Test to test the sessions endpoint")
    void testSessionsHmiApiGet() {
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("requestSessionType", "ADHOC");

        Response response = RestClient.makeGetRequest(getHeadersAsMap(),
                getAuthorizationToken(), queryParams, getRootContext());

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }
}
