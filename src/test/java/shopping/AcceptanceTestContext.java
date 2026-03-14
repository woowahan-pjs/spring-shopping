package shopping;

import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.stereotype.Component;

@Component
public class AcceptanceTestContext {

    private final ManualRestDocumentation restDocumentation = new ManualRestDocumentation();
    private ExtractableResponse<Response> response;
    private String createdProductId;
    private String previousProductId;
    private String token;

    public void setUp(String methodName) {
        restDocumentation.beforeTest(getClass(), methodName);
    }

    public void tearDown() {
        restDocumentation.afterTest();
    }

    public RequestSpecification documentSpec(String identifier) {
        return new RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    public RequestSpecification spec() {
        return new RequestSpecBuilder().build();
    }

    public ExtractableResponse<Response> getResponse() {
        return response;
    }

    public void setResponse(ExtractableResponse<Response> response) {
        this.response = response;
    }

    public String getCreatedProductId() {
        return createdProductId;
    }

    public void setCreatedProductId(String createdProductId) {
        this.createdProductId = createdProductId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPreviousProductId() {
        return previousProductId;
    }

    public void setPreviousProductId(String previousProductId) {
        this.previousProductId = previousProductId;
    }
}
