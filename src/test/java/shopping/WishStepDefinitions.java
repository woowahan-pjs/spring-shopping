package shopping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import java.util.UUID;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.springframework.beans.factory.annotation.Autowired;

public class WishStepDefinitions {

    @Autowired
    private AcceptanceTestContext context;

    private String token;

    @Given("I am a registered member with email {string} and password {string}")
    public void iAmARegisteredMemberWithEmailAndPassword(String email, String password) {
        RestAssured.given().spec(context.spec()).contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}").when()
                .post("/api/members/register").then().statusCode(201);
        var loginResponse = RestAssured.given().spec(context.spec()).contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}").when()
                .post("/api/members/login").then().statusCode(200).extract();
        token = loginResponse.jsonPath().getString("token");
        context.setToken(token);
    }

    @Given("the product is in my wishlist")
    public void theProductIsInMyWishlist() {
        RestAssured.given().spec(context.spec()).header("Authorization", "Bearer " + token)
                .header("Idempotency-Key", UUID.randomUUID().toString()).when()
                .post("/api/wishes/{productId}", context.getCreatedProductId()).then()
                .statusCode(201);
    }

    @Given("the product is deleted")
    public void theProductIsDeleted() {
        RestAssured.given().spec(context.spec())
                .header("Authorization", "Bearer " + context.getToken()).when()
                .delete("/api/products/{id}", context.getCreatedProductId()).then().statusCode(204);
    }

    @When("I add the product to my wishlist")
    public void iAddTheProductToMyWishlist() {
        context.setResponse(RestAssured.given().spec(context.documentSpec("wish-add"))
                .filter(document("wish-add")).header("Authorization", "Bearer " + token)
                .header("Idempotency-Key", UUID.randomUUID().toString()).when()
                .post("/api/wishes/{productId}", context.getCreatedProductId()).then().extract());
    }

    @When("I view my wishlist")
    public void iViewMyWishlist() {
        context.setResponse(RestAssured.given().spec(context.documentSpec("wish-find-all"))
                .filter(document("wish-find-all")).header("Authorization", "Bearer " + token).when()
                .get("/api/wishes").then().extract());
    }

    @When("I remove the product from my wishlist")
    public void iRemoveTheProductFromMyWishlist() {
        context.setResponse(RestAssured.given().spec(context.documentSpec("wish-remove"))
                .filter(document("wish-remove")).header("Authorization", "Bearer " + token).when()
                .delete("/api/wishes/{productId}", context.getCreatedProductId()).then().extract());
    }

    @Then("the wish should be created")
    public void theWishShouldBeCreated() {
        assertThat(context.getResponse().statusCode()).isEqualTo(201);
    }

    @Then("the wishlist should contain {int} product")
    public void theWishlistShouldContainProducts(int count) {
        assertThat(context.getResponse().statusCode()).isEqualTo(200);
        assertThat(context.getResponse().jsonPath().getList("$")).hasSize(count);
    }

    @Then("the wish response should have product name {string}")
    public void theWishResponseShouldHaveProductName(String name) {
        assertThat(context.getResponse().jsonPath().getString("name")).isEqualTo(name);
    }

    @Then("the wishlist should have product name {string}")
    public void theWishlistShouldHaveProductName(String name) {
        assertThat(context.getResponse().jsonPath().getString("[0].name")).isEqualTo(name);
    }

    @Then("the wish should be removed")
    public void theWishShouldBeRemoved() {
        assertThat(context.getResponse().statusCode()).isEqualTo(204);
    }

    @When("I log in as member {string} with password {string}")
    public void iLogInAsMember(String email, String password) {
        var loginResponse = RestAssured.given().spec(context.spec()).contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}").when()
                .post("/api/members/login").then().statusCode(200).extract();
        token = loginResponse.jsonPath().getString("token");
        context.setToken(token);
    }
}
