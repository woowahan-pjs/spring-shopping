package shopping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.springframework.beans.factory.annotation.Autowired;

public class MemberStepDefinitions {

    @Autowired
    private AcceptanceTestContext context;

    @Before
    public void setUp(Scenario scenario) {
        context.setUp(scenario.getName());
    }

    @After
    public void tearDown() {
        context.tearDown();
        context.setToken(null);
    }

    @Given("a member exists with email {string} and password {string}")
    public void aMemberExistsWithEmailAndPassword(String email, String password) {
        RestAssured.given().spec(context.spec()).contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}").when()
                .post("/api/members/register").then().statusCode(201);
    }

    @When("I register with email {string} and password {string}")
    public void iRegisterWithEmailAndPassword(String email, String password) {
        context.setResponse(RestAssured.given().spec(context.documentSpec("member-register"))
                .filter(document("member-register")).contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}").when()
                .post("/api/members/register").then().extract());
    }

    @When("I login with email {string} and password {string}")
    public void iLoginWithEmailAndPassword(String email, String password) {
        context.setResponse(RestAssured.given().spec(context.documentSpec("member-login"))
                .filter(document("member-login")).contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}").when()
                .post("/api/members/login").then().extract());
    }

    @Then("the registration should be successful")
    public void theRegistrationShouldBeSuccessful() {
        assertThat(context.getResponse().statusCode()).isEqualTo(201);
    }

    @Then("the login should be successful")
    public void theLoginShouldBeSuccessful() {
        assertThat(context.getResponse().statusCode()).isEqualTo(200);
    }

    @Then("the request should fail")
    public void theRequestShouldFail() {
        assertThat(context.getResponse().statusCode()).isEqualTo(400);
    }

    @And("the response should contain a token")
    public void theResponseShouldContainAToken() {
        String token = context.getResponse().jsonPath().getString("token");
        assertThat(token).isNotBlank();
    }
}
