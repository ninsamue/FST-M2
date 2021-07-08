package Project;

import static io.restassured.RestAssured.given;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

//RESTAssured GitHub Project
public class GitHub_Project {
	// Declare request specification
	RequestSpecification requestSpec;
	// Declare response specification
	ResponseSpecification responseSpec;

	String SSHKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDAkNT3v5YYSKJBDAzWvlQCx4xgtkeICUoSLCiCFfkTGOjggvgGDkG/KbeixY6kade2xhu8oX9SMaUbcMV2eGBCoArIkNUWYPe+1KRoDsfP9u0DTaukp5yJXnWrYZo8xYyn6NbTJDwhYc/kYtC+4UiGikZZIAWFbiETxd9b5FQJMPbfPDEiK3h00JPFGIWQt/2uMyHpMM+ETyhAFWThCD7KlgL2y7gqApFy2OuxX9y4Idtgem9hZbIvVVB29jzhH2sKXekC8V5LGjhy/kZYUIsA6hwBg4tBgE36Odqsvtu/wHBCsQfcqtPBzvVQHoJ1K/NvUrK6RBQkn5XYivwyxdG5";
	Integer id;

	@BeforeClass
	public void setUp() {
		// Create request specification
		requestSpec = new RequestSpecBuilder()
				// Set content type
				.setContentType(ContentType.JSON)
				//Set Authoriation Token
				.addHeader("Authorization", "token ghp_XXX")
				// Set base URL
				.setBaseUri("https://api.github.com")
				// Build request specification
				.build();

		responseSpec = new ResponseSpecBuilder()
				// Check status code in response
				.expectStatusCode(201)
				// Check response content type
				.expectContentType("application/json")
				// Build response specification
				.build();
	}

    //Add SSH Keys to GitHub
	@Test(priority = 1)
	public void addKeys() {
		String reqBody = "{\"title\": \"TestAPIKey\", \"key\": \""+SSHKey+"\"}";
		Response response = given().spec(requestSpec).body(reqBody).when().post("/user/keys"); 

		String responseBody = response.getBody().asPrettyString();
		System.out.println("Response Body is =>  " + responseBody);
		Reporter.log(responseBody);

	    id = response.then().extract().path("id");
	    System.out.println("Id returned in the response : "+id);
	    Reporter.log("Id returned in the response : "+id);
		
	    response.then().spec(responseSpec); 


	}

	//Get All SSH Keys from GitHub Account
	@Test(priority = 2) 
	public void getKeys() {
		Response response = given().spec(requestSpec).get("/user/keys"); 

		String responseBody = response.getBody().asPrettyString();
		System.out.println("Response Body is =>  " + responseBody);
		Reporter.log("All SSH Keys are retrieved");

	    response.then().statusCode(200);
	    


	}
	
	//Delete the SSH Key added using the ID
	@Test(priority = 3) 
	public void deleteKeys() {
		Response response = given().spec(requestSpec).pathParam("id",id).when().delete("/user/keys/{id}"); 

		String responseBody = response.getBody().asPrettyString();
		System.out.println("Response Body is =>  " + responseBody);
		Reporter.log("SSH Key is delete successfully");
		
		response.then().statusCode(204);

	}

}
