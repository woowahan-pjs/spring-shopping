package shopping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import java.util.UUID;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.springframework.beans.factory.annotation.Autowired;

public class ProductStepDefinitions {

    private static final String TEST_EMAIL = "product-test@test.com";
    private static final String TEST_PASSWORD = "password123";

    @Autowired
    private AcceptanceTestContext context;

    @Autowired
    private FakeProfanityChecker fakeProfanityChecker;

    @Before(order = 1)
    public void authenticate() {
        if (context.getToken() != null) {
            return;
        }
        try {
            RestAssured
                    .given().spec(context.spec()).contentType(ContentType.JSON).body("{\"email\":\""
                            + TEST_EMAIL + "\",\"password\":\"" + TEST_PASSWORD + "\"}")
                    .when().post("/api/members/register");
        } catch (Exception ignored) {
        }
        var loginResponse = RestAssured.given().spec(context.spec()).contentType(ContentType.JSON)
                .body("{\"email\":\"" + TEST_EMAIL + "\",\"password\":\"" + TEST_PASSWORD + "\"}")
                .when().post("/api/members/login").then().statusCode(200).extract();
        context.setToken(loginResponse.jsonPath().getString("token"));
    }

    @After
    public void resetProfanityChecker() {
        fakeProfanityChecker.setShouldFail(false);
    }

    @Given("a product exists with name {string} price {long} and imageUrl {string}")
    public void aProductExistsWithNamePriceAndImageUrl(String name, long price, String imageUrl) {
        var response = RestAssured.given().spec(context.spec())
                .header("Authorization", "Bearer " + context.getToken())
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\",\"price\":" + price + ",\"imageUrl\":\""
                        + imageUrl + "\"}")
                .when().post("/api/products").then().statusCode(201).extract();
        context.setCreatedProductId(response.jsonPath().getString("id"));
    }

    @When("I create a product with name {string} price {long} and imageUrl {string}")
    public void iCreateAProductWithNamePriceAndImageUrl(String name, long price, String imageUrl) {
        context.setResponse(RestAssured.given().spec(context.documentSpec("product-create"))
                .filter(document("product-create"))
                .header("Authorization", "Bearer " + context.getToken())
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .contentType(ContentType.JSON).body("{\"name\":\"" + name + "\",\"price\":" + price
                        + ",\"imageUrl\":\"" + imageUrl + "\"}")
                .when().post("/api/products").then().extract());
        String id = context.getResponse().jsonPath().getString("id");
        if (id != null) {
            context.setCreatedProductId(id);
        }
    }

    @When("I find the product by id")
    public void iFindTheProductById() {
        context.setResponse(RestAssured.given().spec(context.documentSpec("product-find"))
                .filter(document("product-find"))
                .header("Authorization", "Bearer " + context.getToken()).when()
                .get("/api/products/{id}", context.getCreatedProductId()).then().extract());
    }

    @When("I update the product with name {string} price {long} and imageUrl {string}")
    public void iUpdateTheProductWithNamePriceAndImageUrl(String name, long price,
            String imageUrl) {
        context.setResponse(RestAssured.given().spec(context.documentSpec("product-update"))
                .filter(document("product-update"))
                .header("Authorization", "Bearer " + context.getToken())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\",\"price\":" + price + ",\"imageUrl\":\""
                        + imageUrl + "\"}")
                .when().put("/api/products/{id}", context.getCreatedProductId()).then().extract());
    }

    @When("I delete the product")
    public void iDeleteTheProduct() {
        context.setResponse(RestAssured.given().spec(context.documentSpec("product-delete"))
                .filter(document("product-delete"))
                .header("Authorization", "Bearer " + context.getToken()).when()
                .delete("/api/products/{id}", context.getCreatedProductId()).then().extract());
    }

    @When("I find all products")
    public void iFindAllProducts() {
        context.setResponse(RestAssured.given().spec(context.documentSpec("product-find-all"))
                .filter(document("product-find-all"))
                .header("Authorization", "Bearer " + context.getToken()).when().get("/api/products")
                .then().extract());
    }

    @Then("the product should be created")
    public void theProductShouldBeCreated() {
        assertThat(context.getResponse().statusCode()).isEqualTo(201);
    }

    @Then("the product should be found")
    public void theProductShouldBeFound() {
        assertThat(context.getResponse().statusCode()).isEqualTo(200);
    }

    @Then("the product should be updated")
    public void theProductShouldBeUpdated() {
        assertThat(context.getResponse().statusCode()).isEqualTo(200);
    }

    @Then("the product should be deleted")
    public void theProductShouldBeDeleted() {
        assertThat(context.getResponse().statusCode()).isEqualTo(204);
    }

    @Then("the products should be found")
    public void theProductsShouldBeFound() {
        assertThat(context.getResponse().statusCode()).isEqualTo(200);
    }

    @And("the product response should have name {string}")
    public void theProductResponseShouldHaveName(String name) {
        assertThat(context.getResponse().jsonPath().getString("name")).isEqualTo(name);
    }

    @Given("the profanity API is unavailable")
    public void theProfanityApiIsUnavailable() {
        fakeProfanityChecker.setShouldFail(true);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        assertThat(context.getResponse().statusCode()).isEqualTo(statusCode);
    }

    @And("the response should have message {string}")
    public void theResponseShouldHaveMessage(String message) {
        assertThat(context.getResponse().jsonPath().getString("message")).isEqualTo(message);
    }

    @When("I create a product with name {string} price {long} and imageUrl {string} without idempotency key")
    public void iCreateAProductWithoutIdempotencyKey(String name, long price, String imageUrl) {
        context.setResponse(RestAssured.given().spec(context.spec())
                .header("Authorization", "Bearer " + context.getToken())
                .contentType(ContentType.JSON).body("{\"name\":\"" + name + "\",\"price\":" + price
                        + ",\"imageUrl\":\"" + imageUrl + "\"}")
                .when().post("/api/products").then().extract());
    }

    @When("I create a product with name {string} price {long} and imageUrl {string} using idempotency key {string}")
    public void iCreateAProductWithIdempotencyKey(String name, long price, String imageUrl,
            String idempotencyKey) {
        context.setPreviousProductId(context.getCreatedProductId());
        context.setResponse(RestAssured.given().spec(context.spec())
                .header("Authorization", "Bearer " + context.getToken())
                .header("Idempotency-Key", idempotencyKey).contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\",\"price\":" + price + ",\"imageUrl\":\""
                        + imageUrl + "\"}")
                .when().post("/api/products").then().extract());
        String id = context.getResponse().jsonPath().getString("id");
        if (id != null) {
            context.setCreatedProductId(id);
        }
    }

    @And("the product id should be the same as the previously created product")
    public void theProductIdShouldBeTheSameAsThePreviouslyCreatedProduct() {
        assertThat(context.getCreatedProductId()).isEqualTo(context.getPreviousProductId());
    }

    @And("the product id should be different from the previously created product")
    public void theProductIdShouldBeDifferentFromThePreviouslyCreatedProduct() {
        assertThat(context.getCreatedProductId()).isNotEqualTo(context.getPreviousProductId());
    }
}
